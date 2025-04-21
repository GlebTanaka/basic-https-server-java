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