package com.nio.http.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;

import com.nio.http.Response;

/**
 * Simple object to provide reading and writing between the client and the
 * server.
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
public class SelectorHandler {
    /** Current suppported http version */
    private static final String VERSION = "HTTP/1.1";

    /** Encoder to translate string into bytes */
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    private final SocketChannel channel;
    private final ByteBuffer buffer;
    private final StringBuilder linesRead;
    private int mark;

    public SelectorHandler(final SocketChannel channel) {
        this.channel = channel;
        buffer = ByteBuffer.allocate(8192);
        linesRead = new StringBuilder();
    }

    /**
     * @return the linesRead
     */
    public StringBuilder getLinesRead() {
        return linesRead;
    }

    public boolean read() throws IOException {
        int num;
        while (true) {
            num = channel.read(buffer);
            if (num < 0)
                return false;
            else if (num == 0)
                return true;
            buffer.flip();
            linesRead.append(new String(buffer.array()).trim());
            linesRead.append('\n');
            buffer.position(mark);
        }
    }

    public void write(final Response response) {
        response.defaultHeaders();
        try {
            writeLine(VERSION + " " + response.getStatus().getStatusCode() + " " + response.getStatus().getReason());
            for (final Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                writeLine(header.getKey() + ": " + header.getValue());
            }
            writeLine("");
            if (response.getContent() != null) {
                channel.write(ByteBuffer.wrap(response.getContent()));
            }
        } catch (final IOException e) {
            throw new RuntimeException("Exception while sending response.", e);
        }
    }

    private void writeLine(final String line) {
        try {
            channel.write(UTF8_CHARSET.newEncoder().encode(CharBuffer.wrap(line + "\r\n")));
        } catch (final IOException e) {
            throw new RuntimeException("Exception writing to client.", e);
        }
    }

    /**
     * close the channel
     */
    public void close() {
        try {
            channel.close();
        } catch (final IOException e) {
            throw new RuntimeException("Exception closing the channel.", e);
        }
    }
}
