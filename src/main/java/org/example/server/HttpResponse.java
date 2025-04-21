package org.example.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP response.
 */
public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    
    private int statusCode = 200;
    private String statusMessage = "OK";
    private final Map<String, String> headers = new HashMap<>();
    private String body = "";
    
    /**
     * Creates a new HTTP response with default status 200 OK.
     */
    public HttpResponse() {
        headers.put("Content-Type", "text/plain");
        headers.put("Server", "BasicHttpServer/1.0");
    }
    
    /**
     * Sets the status code and message.
     *
     * @param statusCode the status code
     * @param statusMessage the status message
     * @return this response for chaining
     */
    public HttpResponse setStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        return this;
    }
    
    /**
     * Sets a header.
     *
     * @param name the header name
     * @param value the header value
     * @return this response for chaining
     */
    public HttpResponse setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }
    
    /**
     * Sets the response body.
     *
     * @param body the response body
     * @return this response for chaining
     */
    public HttpResponse setBody(String body) {
        this.body = body;
        setHeader("Content-Length", String.valueOf(body.length()));
        return this;
    }
    
    /**
     * Sets the content type.
     *
     * @param contentType the content type
     * @return this response for chaining
     */
    public HttpResponse setContentType(String contentType) {
        setHeader("Content-Type", contentType);
        return this;
    }
    
    /**
     * Sends the response to the output stream.
     *
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    public void send(OutputStream outputStream) throws IOException {
        PrintWriter writer = new PrintWriter(outputStream, true);
        
        // Write status line
        writer.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        
        // Write headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            writer.println(header.getKey() + ": " + header.getValue());
        }
        
        // Write empty line to separate headers from body
        writer.println();
        
        // Write body
        writer.print(body);
        writer.flush();
        
        logger.debug("Sent HTTP response: {} {}", statusCode, statusMessage);
    }
    
    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Gets the status message.
     *
     * @return the status message
     */
    public String getStatusMessage() {
        return statusMessage;
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
     * Gets the response body.
     *
     * @return the response body
     */
    public String getBody() {
        return body;
    }
}