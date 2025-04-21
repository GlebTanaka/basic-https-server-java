package org.example.server;

/**
 * Interface for handling HTTP requests.
 */
public interface RequestHandler {
    
    /**
     * Handles an HTTP request and generates a response.
     *
     * @param request the HTTP request
     * @param response the HTTP response to be filled
     */
    void handle(HttpRequest request, HttpResponse response);
}