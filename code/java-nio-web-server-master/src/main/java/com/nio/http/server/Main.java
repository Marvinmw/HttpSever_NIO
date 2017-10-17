package com.nio.http.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.nio.config.SpringConfig;

/**
 * Main entry point to start the web server. This is a spring-managed
 * application for dependency injection.
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
public class Main {
    /** Application context to lookup the spring beans */
    private static AbstractApplicationContext ctx;

    public static void main(final String[] args) {
        // annotation configuration
        ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        // register hook to close the spring context when the app is shutdown
        ctx.registerShutdownHook();

        final Server server = ctx.getBean(Server.class);

        while (true) {
            server.run();
            try {
                // let the server kick off before any requests are made
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                server.shutdown();
            }
        }
    }
}