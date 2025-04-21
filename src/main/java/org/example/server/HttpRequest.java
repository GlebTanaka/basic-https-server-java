package org.example.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request.
 */
public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    
    private String method;
    private String path;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private String body;
    
    /**
     * Parses an HTTP request from an input stream.
     *
     * @param inputStream the input stream to read from
     * @return the parsed HTTP request
     * @throws IOException if an I/O error occurs
     */
    public static HttpRequest parse(InputStream inputStream) throws IOException {
        HttpRequest request = new HttpRequest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        // Parse request line
        String requestLine = reader.readLine();
        if (requestLine != null) {
            String[] parts = requestLine.split(" ");
            if (parts.length >= 3) {
                request.method = parts[0];
                request.path = parts[1];
                request.version = parts[2];
            }
        }
        
        // Parse headers
        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            int colonIndex = headerLine.indexOf(':');
            if (colonIndex > 0) {
                String name = headerLine.substring(0, colonIndex).trim();
                String value = headerLine.substring(colonIndex + 1).trim();
                request.headers.put(name, value);
            }
        }
        
        // Parse body if Content-Length is present
        if (request.headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(request.headers.get("Content-Length"));
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars, 0, contentLength);
                request.body = new String(bodyChars);
            }
        }
        
        logger.debug("Parsed HTTP request: {} {} {}", request.method, request.path, request.version);
        return request;
    }
    
    /**
     * Gets the HTTP method.
     *
     * @return the HTTP method
     */
    public String getMethod() {
        return method;
    }
    
    /**
     * Gets the request path.
     *
     * @return the request path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Gets the HTTP version.
     *
     * @return the HTTP version
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Gets a header value.
     *
     * @param name the header name
     * @return the header value, or null if not present
     */
    public String getHeader(String name) {
        return headers.get(name);
    }
    
    /**
     * Gets all headers.
     *
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }
    
    /**
     * Gets the request body.
     *
     * @return the request body
     */
    public String getBody() {
        return body;
    }
}