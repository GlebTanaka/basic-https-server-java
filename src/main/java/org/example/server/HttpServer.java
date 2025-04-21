package org.example.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple HTTP server implementation using TCP sockets.
 */
public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    
    private final int port;
    private final RequestHandler requestHandler;
    private final ExecutorService executorService;
    private ServerSocket serverSocket;
    private boolean running;
    
    /**
     * Creates a new HTTP server instance.
     *
     * @param port the port to listen on
     * @param requestHandler the handler for incoming requests
     */
    public HttpServer(int port, RequestHandler requestHandler) {
        this.port = port;
        this.requestHandler = requestHandler;
        this.executorService = Executors.newFixedThreadPool(10); // Thread pool for handling connections
    }
    
    /**
     * Starts the server.
     *
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        
        logger.info("HTTP Server started on port {}", port);
        
        // Accept connections in a separate thread
        new Thread(this::acceptConnections).start();
    }
    
    /**
     * Stops the server.
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void stop() throws IOException {
        running = false;
        
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        
        executorService.shutdown();
        logger.info("HTTP Server stopped");
    }
    
    /**
     * Accepts incoming connections and handles them in separate threads.
     */
    private void acceptConnections() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleConnection(clientSocket));
            } catch (IOException e) {
                if (running) {
                    logger.error("Error accepting connection", e);
                }
            }
        }
    }
    
    /**
     * Handles a client connection.
     *
     * @param clientSocket the client socket
     */
    private void handleConnection(Socket clientSocket) {
        try {
            // Create request and response objects
            HttpRequest request = HttpRequest.parse(clientSocket.getInputStream());
            HttpResponse response = new HttpResponse();
            
            // Let the handler process the request
            requestHandler.handle(request, response);
            
            // Send the response back to the client
            response.send(clientSocket.getOutputStream());
            
            // Close the connection
            clientSocket.close();
        } catch (IOException e) {
            logger.error("Error handling connection", e);
            try {
                clientSocket.close();
            } catch (IOException ex) {
                logger.error("Error closing client socket", ex);
            }
        }
    }
}