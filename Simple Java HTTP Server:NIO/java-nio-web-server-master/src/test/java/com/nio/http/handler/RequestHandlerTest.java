package com.nio.http.handler;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.nio.http.BaseSpringTest;
import com.nio.http.Response;

public class RequestHandlerTest extends BaseSpringTest {
    @Autowired
    private RequestHandler handler;

    @Test
    public void testNotNull() {
        assertNotNull(handler);
    }

    @Test
    public void testGetOk() {
        final StringBuilder req = new StringBuilder("GET /jsoft/index.html HTTP/1.1\r\n");
        req.append("User-Agent: curl/7.37.1\r\n").append("Host: localhost:9000\r\n").append("Accept: */*");

        final Response res = handler.handle(req.toString());

        assertNotNull(res);
        assertEquals(res.getStatus(), Response.Status.OK);
    }

    @Test
    public void testGet404() {
        final StringBuilder req = new StringBuilder("GET /jsoft/notfound.html HTTP/1.1\r\n");
        req.append("User-Agent: curl/7.37.1\r\n").append("Host: localhost:9000\r\n").append("Accept: */*");

        final Response res = handler.handle(req.toString());
        assertNotNull(res);
        assertEquals(res.getStatus(), Response.Status.NOT_FOUND);
    }

    @Test
    public void testPostNotImplemented() {
        final StringBuilder req = new StringBuilder("POST /jsoft/index.html HTTP/1.1\r\n");
        req.append("User-Agent: curl/7.37.1\r\n").append("Host: localhost:9000\r\n").append("Accept: */*");

        final Response res = handler.handle(req.toString());
        assertNotNull(res);
        assertEquals(res.getStatus(), Response.Status.BAD_REQUEST);
    }

    @Test
    public void testNotSupported() {
        final StringBuilder req = new StringBuilder("PUT /jsoft/index.html HTTP/1.1\r\n");
        req.append("User-Agent: curl/7.37.1\r\n").append("Host: localhost:9000\r\n").append("Accept: */*");

        final Response res = handler.handle(req.toString());
        assertNotNull(res);
        assertEquals(res.getStatus(), Response.Status.BAD_REQUEST);
        assertEquals(res.getContent(), "<html><body>This request method is not support</body></html>".getBytes());
    }
}