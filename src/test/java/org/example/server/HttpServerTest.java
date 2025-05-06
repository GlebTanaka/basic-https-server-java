package org.example.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the HTTP server.
 */
public class HttpServerTest {

    private static final int TEST_PORT = 8888;
    private HttpServer server;

    @BeforeEach
    public void setUp() throws IOException {
        // Create a simple request handler for testing
        RequestHandler handler = (request, response) -> {
            response.setContentType("text/plain");
            response.setBody("Test response");
        };

        // Create and start the server
        server = new HttpServer(TEST_PORT, handler);
        server.start();

        // Wait a bit for the server to start
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Stop the server after each test
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testServerRespondsToGetRequest() throws IOException {
        // Create a connection to the server
        URL url = new URL("http://localhost:" + TEST_PORT + "/test");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Get the response
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Response code should be 200 OK");

        // Read the response body
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Verify the response
        assertEquals("Test response", response.toString(), "Response body should match");

        // Verify the content type
        assertEquals("text/plain", connection.getHeaderField("Content-Type"), "Content-Type should be text/plain");
    }

    @Test
    public void testServerHandlesPostRequest() throws IOException {
        // Create a connection to the server
        URL url = new URL("http://localhost:" + TEST_PORT + "/test");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Send a request body
        String requestBody = "Hello, server!";
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes());
        }

        // Get the response
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Response code should be 200 OK");

        // Read the response body
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Verify the response
        assertEquals("Test response", response.toString(), "Response body should match");
    }

    @Test
    public void testCustomRequestHandler() throws IOException {
        // Stop the default server
        server.stop();

        // Create a server with a custom handler that echoes the request path
        AtomicBoolean handlerCalled = new AtomicBoolean(false);
        RequestHandler customHandler = (request, response) -> {
            handlerCalled.set(true);
            response.setContentType("text/plain");
            response.setBody("You requested: " + request.getPath());
        };

        // Start a new server with the custom handler
        server = new HttpServer(TEST_PORT, customHandler);
        server.start();

        // Wait a bit for the server to start
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Send a request
        URL url = new URL("http://localhost:" + TEST_PORT + "/custom/path");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Get the response
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Response code should be 200 OK");

        // Read the response body
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Verify the handler was called
        assertTrue(handlerCalled.get(), "Custom handler should have been called");

        // Verify the response contains the request path
        assertEquals("You requested: /custom/path", response.toString(), "Response should echo the request path");
    }

    @Test
    public void testServerHandlesPutRequest() throws IOException {
        // Stop the default server
        server.stop();

        // Create a server with a custom handler that verifies PUT requests
        AtomicBoolean putHandlerCalled = new AtomicBoolean(false);
        RequestHandler putHandler = (request, response) -> {
            // Verify this is a PUT request
            assertEquals("PUT", request.getMethod(), "Request method should be PUT");
            putHandlerCalled.set(true);

            // Echo back the request body
            response.setContentType("text/plain");
            response.setBody("PUT request received with body: " + request.getBody());
        };

        // Start a new server with the custom handler
        server = new HttpServer(TEST_PORT, putHandler);
        server.start();

        // Wait a bit for the server to start
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Send a PUT request
        URL url = new URL("http://localhost:" + TEST_PORT + "/resource");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        // Send a request body
        String requestBody = "Updated resource data";
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes());
        }

        // Get the response
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Response code should be 200 OK");

        // Read the response body
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Verify the handler was called
        assertTrue(putHandlerCalled.get(), "PUT handler should have been called");

        // Verify the response contains the echoed request body
        assertEquals("PUT request received with body: " + requestBody, response.toString(), 
                "Response should echo the PUT request body");
    }
}
