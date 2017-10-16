package com.nio.http.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.nio.http.Request;
import com.nio.http.Response;

@Component
public class RequestFilter {
    /** the wordpress xmlrpc file */
    private static final String XML_RPC_URI = "/xmlrpc.php";

    /** change to read database of malicious attacks */
    private final Map<String, String> maliciousHeaders = new HashMap<>();

    public RequestFilter() {
        maliciousHeaders.put("User-agent", "Mozilla/4.0 (compatible: MSIE 7.0; Windows NT 6.0)");
        maliciousHeaders.put("Connection", "close");
    }

    public Response filter(final Request req) {
        if (matchSignature(req)) {
            return new Response(Response.Status.DROP);
        }

        return new Response(Response.Status.OK);
    }

    private boolean matchSignature(final Request req) {
        int violations = 0;
        final Map<String, String> headers = req.getHeaders();
        final Set<Map.Entry<String, String>> entries = maliciousHeaders.entrySet();
        for (final Map.Entry<String, String> entry : entries) {
            final String key = entry.getKey();
            final String val = entry.getValue();

            if (val.equalsIgnoreCase(headers.get(key))) {
                violations++;
            }
        }

        // check if we match all the violations
        return Request.Method.POST.equals(req.getMethod()) && XML_RPC_URI.equalsIgnoreCase(req.getUri())
                && (violations == maliciousHeaders.size());
    }
}
