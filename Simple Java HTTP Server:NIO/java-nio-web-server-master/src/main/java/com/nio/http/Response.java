package com.nio.http;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Response object to serve back to the client
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
public class Response {
    private Status status;
    private String version;
    private final Map<String, String> headers;
    private byte[] content;

    public Response() {
        headers = new LinkedHashMap<>();
    }

    public Response(final Status status) {
        this();
        this.setStatus(status);
    }

    public enum Status {
        OK(200, "OK"),

        CREATED(201, "Created"),

        ACCEPTED(202, "Accepted"),

        NO_CONTENT(204, "No Content"),

        MOVED_PERMANENTLY(301, "Moved Permanently"),

        BAD_REQUEST(400, "Bad Request"),

        UNAUTHORIZED(401, "Unauthorized"),

        FORBIDDEN(403, "Forbidden"),

        NOT_FOUND(404, "Not Found"),

        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

        SERVICE_UNAVAILABLE(503, "Service Unavailable"),

        DROP(1001, "Silently Drop The Request");

        private final int statusCode;
        private final String reason;

        Status(final int statusCode, final String reason) {
            this.statusCode = statusCode;
            this.reason = reason;
        }

        /**
         * @return the statusCode
         */
        public int getStatusCode() {
            return statusCode;
        }

        public String getReason() {
            return reason;
        }
    }

    /**
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(final byte[] content) {
        this.content = content;
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

    public void defaultHeaders() {
        headers.put("Date", new Date().toString());
        headers.put("Server", "NIO Server v0.1");
        headers.put("Connection", "close");
        if (content == null) {
            headers.put("Content-Length", "0");
        } else {
            headers.put("Content-Length", Integer.toString(content.length));
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }
}
