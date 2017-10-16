package com.nio.http;

/**
 * Enum for the content type requested and sent to the client.
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
public enum ContentType {
    HTML(".html", "text/html"),

    HTM(".htm", "text/html"),

    XML(".xml", "text/xml"),

    CSS(".css", "text/css"),

    JAVASCRIPT(".js", "text/javascript"),

    IMAGE_GIF(".gif", "image/gif"),

    IMAGE_PNG(".png", "image/png"),

    IMAGE_JPG(".jpg", "image/jpg"),

    TEXT_PLAIN(".txt", "text/plain");

    private final String extension;
    private final String type;

    ContentType(final String extension, final String type) {
        this.extension = extension;
        this.type = type;
    }

    public static String lookupType(final String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            for (final ContentType t : values()) {
                if (fileName.endsWith(t.getExtension())) {
                    return t.getType();
                }
            }
        }

        // default to text
        return TEXT_PLAIN.getType();
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
}