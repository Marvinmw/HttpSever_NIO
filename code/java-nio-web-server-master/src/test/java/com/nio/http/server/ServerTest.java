package com.nio.http.server;

import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.nio.http.BaseSpringTest;
import com.nio.http.client.Client;
import com.nio.http.client.impl.ClientImpl;

public class ServerTest extends BaseSpringTest {
    @Autowired
    private Server server;

    @Test
    public void testServerNotNull() {
        assertNotNull(server);
    }

    @AfterClass
    public void tearDown() {
        server.shutdown();
    }

    @Test
    public void test() throws IOException, InterruptedException {
        final long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        // run for 10 seconds
        while (elapsedTime < 2 * 1000) {
            server.run();

            final Client client = new ClientImpl(9000);
            client.invoke(newRequest());

            synchronized (this) {
                wait(1000);
            }
            elapsedTime = (new Date()).getTime() - startTime;
        }
    }

    @Test
    public void testMalicious() throws IOException, InterruptedException {
        final long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        // run for 10 seconds
        while (elapsedTime < 2 * 1000) {
            server.run();

            final ClientImpl client = new ClientImpl(9000);
            client.invoke(maliciousRequest());

            synchronized (this) {
                wait(1000);
            }
            elapsedTime = (new Date()).getTime() - startTime;
        }
    }

    @Test
    public void testBadRequest() throws IOException, InterruptedException {
        final long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        // run for 10 seconds
        while (elapsedTime < 2 * 1000) {
            server.run();

            final ClientImpl client = new ClientImpl(9000);
            client.invoke(badRequest());

            synchronized (this) {
                wait(1000);
            }
            elapsedTime = (new Date()).getTime() - startTime;
        }
    }

    @Test
    public void testQueryParams() throws IOException, InterruptedException {
        final long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        // run for 10 seconds
        while (elapsedTime < 3 * 1000) {
            server.run();

            final ClientImpl client = new ClientImpl(9000);
            client.invoke(queryParams());

            synchronized (this) {
                wait(1000);
            }
            elapsedTime = (new Date()).getTime() - startTime;
        }
    }

    private String newRequest() {
        return new StringBuilder("GET / HTTP/1.1\r\n").append("User-Agent: curl/7.37.1\r\n")
                .append("Host: localhost:9000\r\n").append("Accept: */*").toString();
    }

    private String queryParams() {
        return new StringBuilder("GET /abc.html?a=b HTTP/1.1\r\n").append("User-Agent: curl/7.37.1\r\n")
                .append("Host: localhost:9000\r\n").append("Accept: */*").toString();
    }

    private String badRequest() {
        return new StringBuilder("GET notgood HTTP/1.1\r\n").append("User-Agent: curl/7.37.1\r\n")
                .append("Host: localhost:9000\r\n").append("Accept: */*").toString();
    }

    private String maliciousRequest() {
        return new StringBuilder("POST /xmlrpc.php HTTP/1.0\r\n")
                .append("User-agent: Mozilla/4.0 (compatible: MSIE 7.0; Windows NT 6.0)\r\n")
                .append("Connection: close\r\n").append("Accept: */*").toString();
    }
}