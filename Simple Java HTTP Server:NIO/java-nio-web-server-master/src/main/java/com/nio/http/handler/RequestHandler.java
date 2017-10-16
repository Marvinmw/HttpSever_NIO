package com.nio.http.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nio.http.ContentType;
import com.nio.http.Request;
import com.nio.http.Response;
import com.nio.http.filter.RequestFilter;
import com.nio.http.parser.RequestParser;

/**
 * Handler to handle http requests. When a request for a page or url is
 * received, we parse the raw string request into a request object and serve the
 * request accordingly.
 *
 * If the page is not found, then return a 404 default page. If the user does
 * not have access to the page, then return a 403 response. Otherwise try to
 * honor the request
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
@Component
public class RequestHandler {
    /** base path for the public folder */
    private static final String BASE_SERVER_URI = "htdocs";

    /** page not found url */
    private static final String PAGE_NOT_FOUND_URI = "htdocs/404.html";

    private static final String INTERNAL_SERVER_ERROR_URI = "htdocs/500.html";

    private static final String CONTENT_TYPE = "Content-Type";

    /** parser to transform raw string request into request object */
    private final RequestParser requestParser;

    /** filter the request for malicious web attacks */
    private final RequestFilter requestFilter;

    @Autowired
    public RequestHandler(final RequestParser requestParser, final RequestFilter requestFilter) {
        this.requestParser = requestParser;
        this.requestFilter = requestFilter;
    }

    /**
     * Handle the request by first parsing the string then evaluating the
     * request method and uri
     *
     * @param rawRequest the raw request string
     * @return corresponding response
     */
    public Response handle(final String rawRequest) {
        final Request req = requestParser.parse(rawRequest);

        return handleRequest(req);
    }

    private Response handleRequest(final Request req) {
        final Response res = requestFilter.filter(req);

        // stop processing the request if it's malicious
        if (Response.Status.DROP.equals(res.getStatus())) {
            return res;
        }

        switch (req.getMethod()) {
        case GET:
            return handleGetReq(req);
        case POST:
            return handlePostReq(req);
        default:
            return notSupportedRes();
        }
    }

    private Response handleGetReq(final Request req) {
        final String resource = getResourceName(req.getUri());
        if (resource == null) {
            return new Response(Response.Status.BAD_REQUEST);
        }

        Response res = new Response();

        try {
            res.setContent(getcontent(BASE_SERVER_URI + resource));
            res.setStatus(Response.Status.OK);
            res.getHeaders().put(CONTENT_TYPE, ContentType.lookupType(resource));
        } catch (final IOException e) {
            res = getErrorResponse(e);
        }
        return res;
    }

    private Response notSupportedRes() {
        final Response r = new Response(Response.Status.BAD_REQUEST);
        r.setContent("<html><body>This request method is not support</body></html>".getBytes());

        return r;
    }

    private Response handlePostReq(final Request req) {
        // not implemented yet
        return new Response(Response.Status.BAD_REQUEST);
    }

    private byte[] getcontent(final String path) throws IOException {
        return Files.readAllBytes((Paths.get(path)));
    }

    private Response getErrorResponse(final IOException e) {
        String page = INTERNAL_SERVER_ERROR_URI;
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        if (e instanceof NoSuchFileException || e instanceof NotDirectoryException
                || (e.getMessage() != null && e.getMessage().contains("Not a directory"))) {
            status = Response.Status.NOT_FOUND;
            page = PAGE_NOT_FOUND_URI;
        }

        final Response res = new Response(status);
        try {
            res.setContent(getcontent(page));
            res.getHeaders().put(CONTENT_TYPE, ContentType.HTML.getType());
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }

        return res;
    }

    private String getResourceName(final String uri) {
        String fileName = uri;

        // needs to start with /abc or /abc/ or /abc/index.html
        final int firstSlash = fileName.indexOf("/");
        if (firstSlash <= -1) {
            return null;
        }

        // check if there is a query param /index.html?a=b
        final int qParam = fileName.indexOf("?");

        if (qParam > firstSlash) {
            fileName = fileName.substring(firstSlash, qParam);
        }

        if (fileName.endsWith("/") && !fileName.contains(".")) {
            // try appending index.html
            fileName = fileName + "index.html";
        }

        return fileName;
    }
}