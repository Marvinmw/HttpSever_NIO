package com.nio.http;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Http request object from the client
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
public class Request {
    private Method method;
    private String uri;
    private String version;
    private String body;
    private final Map<String, String> headers;

    public Request() {
        headers = new LinkedHashMap<>();
    }

    public enum Method {
        GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH;

        /**
         * Return the corresponding method enum from string
         *
         * @param method string value
         * @return enum value
         */
        public static Method lookup(final String method) {
            for (final Method m : values()) {
                if (m.toString().equalsIgnoreCase(method)) {
                    return m;
                }
            }
            throw new IllegalArgumentException("Invalid http method: " + method + ". Valid methods are: " + values());
        }
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(final String uri) {
        this.uri = uri;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(final Method method) {
        this.method = method;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(final String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Request [method=" + method + ", uri=" + uri + ", version=" + version + ", body=" + body + ", headers="
                + headers + "]";
    }
}
