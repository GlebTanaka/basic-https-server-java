# Basic HTTP Server

A simple HTTP server built from scratch using TCP primitives. This project demonstrates how to build a basic HTTP server step by step in an iterative way.

## Project Structure

- `src/main/java/org/example/server/` - Server implementation
  - `HttpServer.java` - Main server class that listens for connections
  - `RequestHandler.java` - Interface for handling HTTP requests
  - `HttpRequest.java` - Class representing an HTTP request
  - `HttpResponse.java` - Class representing an HTTP response
  - `SimpleRequestHandler.java` - A simple implementation of RequestHandler
- `src/main/java/org/example/App.java` - Main application class
- `src/test/java/org/example/server/HttpServerTest.java` - Tests for the server

## Development Loop

This project is set up for a solid development loop with the following features:

### Building the Project

```bash
mvn clean compile
```

### Running Tests

```bash
mvn test
```

### Running the Application

```bash
mvn exec:java
```

Or with a custom port:

```bash
mvn exec:java -Dexec.args="8081"
```

### Building an Executable JAR

```bash
mvn package
```

This will create an executable JAR file in the `target` directory that includes all dependencies.

### Running the JAR

```bash
java -jar target/basic-http-server-1.0-SNAPSHOT.jar
```

Or with a custom port:

```bash
java -jar target/basic-http-server-1.0-SNAPSHOT.jar 8081
```

## Development Workflow

1. Make changes to the code
2. Run tests to verify your changes: `mvn test`
3. Run the application to see it in action: `mvn exec:java`
4. Access the server in your browser at http://localhost:8080
5. Repeat

## Next Steps

Here are some ideas for extending the server:

1. Add support for different HTTP methods (PUT, DELETE, etc.)
2. Implement routing to handle different paths
3. Add support for serving static files
4. Implement HTTP status codes for different scenarios
5. Add support for cookies and sessions
6. Implement middleware for request processing
7. Add support for JSON responses
8. Implement a simple template engine for HTML responses

## Logging

The application uses SLF4J with Logback for logging. The log configuration is in `src/main/resources/logback.xml`.

- Console logs show basic information
- File logs are stored in the `logs` directory
- The default log level is INFO, with DEBUG level for the application code

## Contributing

Feel free to fork this project and submit pull requests with improvements or bug fixes.