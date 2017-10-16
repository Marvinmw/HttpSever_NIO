package com.nio.http.client.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nio.http.client.Client;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClientImpl implements Client {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private SocketChannel sc;
    private Selector selector;
    private int port;

    public ClientImpl() {
        // nothing to see here
    }

    public ClientImpl(final int port) {
        init(new InetSocketAddress(port));
    }

    private void init(final InetSocketAddress address) {
        LOG.debug("Initializing client...");
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.bind(new InetSocketAddress(0));
            sc.connect(address);
            selector = Selector.open();
            sc.register(selector, SelectionKey.OP_CONNECT);

            LOG.debug("Client is connected at http://{}:{}", address.getHostName(), address.getPort());
        } catch (final IOException e) {
            throw new RuntimeException("Exception while creating client", e);
        }
    }

    @Override
    public void invoke(final String req) throws IOException {
        LOG.debug("Invoking client...");

        while (selector.select() > 0) {
            final Set<SelectionKey> keys = selector.selectedKeys();
            final Iterator<SelectionKey> it = keys.iterator();

            while (it.hasNext()) {
                final SelectionKey key = it.next();
                final SocketChannel channel = (SocketChannel) key.channel();
                it.remove();

                if (key.isConnectable()) {
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    channel.write(ByteBuffer.wrap(req.getBytes()));
                }
            }
        }
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(final int port) {
        this.port = port;
    }
}
