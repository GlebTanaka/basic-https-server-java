package org.example;

import org.example.server.HttpServer;
import org.example.server.SimpleRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main application class that starts the HTTP server.
 */
public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) 
    {
        // Determine the port to use
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.warn("Invalid port number: {}. Using default port: {}", args[0], DEFAULT_PORT);
            }
        }

        // Create and start the server
        try {
            logger.info("Starting HTTP server on port {}", port);
            HttpServer server = new HttpServer(port, new SimpleRequestHandler());
            server.start();

            // Add shutdown hook to stop the server gracefully
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.info("Shutting down HTTP server");
                    server.stop();
                } catch (IOException e) {
                    logger.error("Error stopping server", e);
                }
            }));

            logger.info("Server started successfully. Press Ctrl+C to stop.");
        } catch (IOException e) {
            logger.error("Failed to start server", e);
        }
    }
}
