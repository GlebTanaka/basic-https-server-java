package org.example.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple implementation of the RequestHandler interface.
 */
public class SimpleRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(SimpleRequestHandler.class);

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        logger.info("Handling request: {} {}", request.getMethod(), request.getPath());

        String method = request.getMethod();

        // Handle different HTTP methods
        switch (method) {
            case "PUT":
                handlePutRequest(request, response);
                break;
            case "GET":
            case "POST":
            default:
                handleDefaultRequest(request, response);
                break;
        }
    }

    /**
     * Handles PUT requests.
     *
     * @param request the HTTP request
     * @param response the HTTP response to be filled
     */
    private void handlePutRequest(HttpRequest request, HttpResponse response) {
        logger.info("Processing PUT request for path: {}", request.getPath());

        // Set response content type to HTML
        response.setContentType("text/html");

        // Create a simple HTML response for PUT
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html>\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("    <title>PUT Request Processed</title>\n");
        htmlBuilder.append("</head>\n");
        htmlBuilder.append("<body>\n");
        htmlBuilder.append("    <h1>PUT Request Processed</h1>\n");
        htmlBuilder.append("    <p>The server has successfully processed your PUT request.</p>\n");
        htmlBuilder.append("    <h2>Request Details:</h2>\n");
        htmlBuilder.append("    <ul>\n");
        htmlBuilder.append("        <li>Method: ").append(request.getMethod()).append("</li>\n");
        htmlBuilder.append("        <li>Path: ").append(request.getPath()).append("</li>\n");
        htmlBuilder.append("        <li>HTTP Version: ").append(request.getVersion()).append("</li>\n");
        htmlBuilder.append("    </ul>\n");

        // Display request body if present
        if (request.getBody() != null && !request.getBody().isEmpty()) {
            htmlBuilder.append("    <h2>Request Body:</h2>\n");
            htmlBuilder.append("    <pre>").append(request.getBody()).append("</pre>\n");
        }

        htmlBuilder.append("    <h2>Headers:</h2>\n");
        htmlBuilder.append("    <ul>\n");

        for (String headerName : request.getHeaders().keySet()) {
            htmlBuilder.append("        <li>")
                    .append(headerName)
                    .append(": ")
                    .append(request.getHeader(headerName))
                    .append("</li>\n");
        }

        htmlBuilder.append("    </ul>\n");
        htmlBuilder.append("</body>\n");
        htmlBuilder.append("</html>");

        // Set the response body
        response.setBody(htmlBuilder.toString());
    }

    /**
     * Handles default requests (GET, POST, etc.).
     *
     * @param request the HTTP request
     * @param response the HTTP response to be filled
     */
    private void handleDefaultRequest(HttpRequest request, HttpResponse response) {
        // Set response content type to HTML
        response.setContentType("text/html");

        // Create a simple HTML response
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html>\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("    <title>Basic HTTP Server</title>\n");
        htmlBuilder.append("</head>\n");
        htmlBuilder.append("<body>\n");
        htmlBuilder.append("    <h1>Hello, World!</h1>\n");
        htmlBuilder.append("    <p>This is a response from the Basic HTTP Server.</p>\n");
        htmlBuilder.append("    <h2>Request Details:</h2>\n");
        htmlBuilder.append("    <ul>\n");
        htmlBuilder.append("        <li>Method: ").append(request.getMethod()).append("</li>\n");
        htmlBuilder.append("        <li>Path: ").append(request.getPath()).append("</li>\n");
        htmlBuilder.append("        <li>HTTP Version: ").append(request.getVersion()).append("</li>\n");
        htmlBuilder.append("    </ul>\n");
        htmlBuilder.append("    <h2>Headers:</h2>\n");
        htmlBuilder.append("    <ul>\n");

        for (String headerName : request.getHeaders().keySet()) {
            htmlBuilder.append("        <li>")
                    .append(headerName)
                    .append(": ")
                    .append(request.getHeader(headerName))
                    .append("</li>\n");
        }

        htmlBuilder.append("    </ul>\n");
        htmlBuilder.append("</body>\n");
        htmlBuilder.append("</html>");

        // Set the response body
        response.setBody(htmlBuilder.toString());
    }
}
