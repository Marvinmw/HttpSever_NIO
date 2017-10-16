package com.nio.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nio.http.Response;
import com.nio.http.handler.RequestHandler;
import com.nio.http.handler.SelectorHandler;

/**
 * Java NIO Server. The server is configured to run on port 9000. Currently all
 * requests/responses are served on the same thread which may or may not be
 * ideal. Will update once I figure out how to make it more performant.
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
@Component
public class Server implements Runnable {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /** Default port to start the server */
    private static final int DEFAULT_SERVER_PORT = 9000;

    private final RequestHandler requestHandler;
    private boolean isRunning = true;
    private Selector selector;
    private ServerSocketChannel ssc;

    /**
     * Create a server running on the default port 9000
     *
     * @param requestHandler request handler
     */
    @Autowired
    public Server(final RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        init();
    }

    private void init() {
        final InetSocketAddress address = new InetSocketAddress(DEFAULT_SERVER_PORT);
        LOG.debug("Initializing server...");
        initServer(address);
        LOG.debug("Server is running at http://localhost:{}", address.getPort());
    }

    private void initServer(final InetSocketAddress address) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(address);
            ssc.configureBlocking(false);
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (final IOException e) {
            throw new RuntimeException("Exception creating server...", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public final void run() {
        if (isRunning) {
            try {
                selectKeys();
            } catch (final IOException e) {
                shutdown();
                throw new RuntimeException("Encountered exception", e);
            }
        }
    }

    private void selectKeys() throws IOException {
        selector.selectNow();
        final Iterator<SelectionKey> i = selector.selectedKeys().iterator();
        while (i.hasNext()) {
            final SelectionKey key = i.next();
            i.remove();
            if (!key.isValid()) {
                continue;
            }

            try {
                if (key.isAcceptable()) {
                    accept();
                } else if (key.isReadable()) {
                    read(key);
                }
            } catch (final Exception ex) {
                LOG.error("Error handling client: {}. Error: {}", key.channel(), ex);
                if (key.attachment() instanceof SelectorHandler) {
                    ((SelectorHandler) key.attachment()).close();
                }
            }
        }
    }

    private void accept() throws IOException {
        final SocketChannel client = ssc.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void read(final SelectionKey key) throws IOException {
        final SocketChannel client = (SocketChannel) key.channel();
        SelectorHandler handler = (SelectorHandler) key.attachment();
        if (handler == null) {
            handler = new SelectorHandler(client);
            key.attach(handler);
        }
        if (handler.read()) {
            final String rawString = handler.getLinesRead().toString();
            LOG.debug("Raw request: \n{}", rawString);

            final Response res = requestHandler.handle(rawString);

            LOG.debug("Response status: {}", res.getStatus());
            // don't return a response if the request is malicious
            if (Response.Status.DROP.equals(res.getStatus())) {
                LOG.debug("Malicious attack detected from IP: {}", client.getRemoteAddress());
            } else {
                handler.write(res);
            }
        }
        handler.close();
    }

    /**
     * Shutdown this server.
     */
    public final void shutdown() {
        isRunning = false;
        try {
            selector.close();
            ssc.close();
            LOG.debug("Server shutdown successfully!");
        } catch (final IOException e) {
            throw new RuntimeException("Exception shutting down the server", e);
        }
    }
}
