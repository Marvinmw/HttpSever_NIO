package com.nio.http.parser;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

import com.nio.http.Request;

/**
 * Parser to take a raw string request and turn it into a {@link Request}
 * object.
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
@Component
public class RequestParser {
    /** split request headers on carriage return and new line */
    private static final String NEW_LINE = "\r\n";

    /** split header values with the colon */
    private static final String COLON = ":";

    /** split header strings in 2 substrings after the first colon */
    private static final int LIMIT = 2;

    /**
     * Parse given string into request object
     *
     * @param rawRequest the raw string
     * @return request object
     */
    public Request parse(final String rawRequest) {
        if (rawRequest == null || rawRequest.isEmpty()) {
            throw new IllegalArgumentException("request string can not be null or empty");
        }
        final StringTokenizer tokenizer = new StringTokenizer(rawRequest);

        final Request req = new Request();

        // these tokens refer to the example: GET /abc/index.html HTTP/1.1
        req.setMethod(Request.Method.lookup(tokenizer.nextToken()));
        req.setUri(tokenizer.nextToken());
        req.setVersion(tokenizer.nextToken());

        String[] lines = rawRequest.split(NEW_LINE);

        // request body is at the end
        if (Request.Method.POST.equals(req.getMethod()) || Request.Method.PUT.equals(req.getMethod())) {
            final String body = lines[lines.length - 1];

            if (body != null) {
                req.setBody(body);
            }
            lines = Arrays.copyOfRange(lines, 0, lines.length - 1);
        }

        // parse the headers
        req.getHeaders().putAll(parseHeaders(lines));

        return req;
    }

    private Map<String, String> parseHeaders(final String[] lines) {
        final Map<String, String> headers = new LinkedHashMap<>();

        // we don't care about the first line
        for (int i = 1; i < lines.length; i++) {
            // example - Host: localhost:9000
            final String[] keyVal = lines[i].split(COLON, LIMIT);

            if (keyVal.length == 2) {
                // string 1: Host
                final String header = keyVal[0];

                // string 2: localhost:9000
                final String value = keyVal[1];

                headers.put(header.trim(), value.trim());
            }
        }

        return headers;
    }
}
