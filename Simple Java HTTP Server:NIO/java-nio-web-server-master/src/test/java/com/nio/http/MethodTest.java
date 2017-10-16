package com.nio.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class MethodTest {
    @Test
    public void testLookup() {
        assertEquals(Request.Method.lookup("get"), Request.Method.GET);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLookupException() {
        Request.Method.lookup("hey");
    }
}