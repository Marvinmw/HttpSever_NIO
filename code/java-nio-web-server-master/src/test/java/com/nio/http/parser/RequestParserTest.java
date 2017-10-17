package com.nio.http.parser;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.nio.http.BaseSpringTest;
import com.nio.http.Request;

public class RequestParserTest extends BaseSpringTest {
    @Autowired
    private RequestParser requestParser;

    @Test
    public void testNotNull() {
        assertNotNull(requestParser);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseNull() {
        requestParser.parse(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseEmpty() {
        requestParser.parse("");
    }

    @Test
    public void testParse() {
        final StringBuilder rawReq = new StringBuilder("GET /users HTTP/1.1\r\n");
        rawReq.append("User-Agent: curl/7.37.1\r\n").append("Host: localhost:9000\r\n").append("Accept: */*");

        final Request req = requestParser.parse(rawReq.toString());

        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put("User-Agent", "curl/7.37.1");
        headers.put("Host", "localhost:9000");
        headers.put("Accept", "*/*");

        assertEquals(req.getMethod(), Request.Method.GET);
        assertEquals(req.getUri(), "/users");
        assertEquals(req.getVersion(), "HTTP/1.1");
        assertEquals(req.getBody(), null);
        assertEquals(req.getHeaders(), headers);
        assertFalse(req.toString().isEmpty());
    }
}