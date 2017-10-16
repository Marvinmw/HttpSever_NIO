package com.nio.http.client;

import java.io.IOException;

public interface Client {
    void invoke(final String req) throws IOException;
}
