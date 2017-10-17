package com.nio.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

public class ResponseTest {
    @Test
    public void test() {
        final Response r = new Response();
        r.setContent(new byte[] {});
        r.setStatus(Response.Status.OK);
        r.setVersion("HTTP/1.1");
        r.defaultHeaders();

        assertEquals(r.getStatus().getReason(), "OK");
        assertEquals(r.getStatus().getStatusCode(), 200);
        assertEquals(r.getVersion(), "HTTP/1.1");
        assertEquals(r.getContent(), new byte[] {});
        assertFalse(r.getHeaders().isEmpty());

        final Response r2 = new Response(Response.Status.NOT_FOUND);
        r2.setContent(new byte[] { 68 });
        r2.defaultHeaders();
        assertEquals(r2.getStatus().getReason(), "Not Found");
        assertEquals(r2.getStatus().getStatusCode(), 404);
        assertFalse(r2.getHeaders().isEmpty());
    }
}