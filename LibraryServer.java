import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

public class LibraryServer {
    private static Library library = new Library();

    public static void main(String[] args) throws IOException {
        // Initialize with some sample data
        library.addBook("Java Programming", "John Doe", "123-456-789", 5);
        library.addBook("Python Basics", "Jane Smith", "987-654-321", 3);
        library.addUser("Admin User", "admin1", "librarian");
        library.addUser("Regular User", "user1", "member");

        // Create HTTP server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        
        // Define API endpoints
        server.createContext("/api/books", exchange -> {
            handleBooksRequest(exchange);
        });
        
        server.createContext("/api/users", exchange -> {
            handleUsersRequest(exchange);
        });
        
        server.createContext("/api/checkout", exchange -> {
            handleCheckoutRequest(exchange);
        });
        
        server.createContext("/api/return", exchange -> {
            handleReturnRequest(exchange);
        });
        
        server.setExecutor(null);
        System.out.println("Server started on port 8000");
        server.start();
    }

    private static void handleBooksRequest(java.net.HttpExchange exchange) throws IOException {
        String response;
        if ("GET".equals(exchange.getRequestMethod())) {
            response = library.listAllBooks().toString();
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines().collect(Collectors.joining("\n"));
            // Parse request body and add book
            // Format: title,author,isbn,totalCopies
            String[] parts = requestBody.split(",");
            if (parts.length == 4) {
                library.addBook(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                response = "Book added successfully";
            } else {
                response = "Invalid book data";
            }
        } else {
            response = "Unsupported method";
        }
        
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleUsersRequest(java.net.HttpExchange exchange) throws IOException {
        String response;
        if ("GET".equals(exchange.getRequestMethod())) {
            response = library.listAllUsers().toString();
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines().collect(Collectors.joining("\n"));
            // Parse request body and add user
            // Format: name,userId,userType
            String[] parts = requestBody.split(",");
            if (parts.length == 3) {
                library.addUser(parts[0], parts[1], parts[2]);
                response = "User added successfully";
            } else {
                response = "Invalid user data";
            }
        } else {
            response = "Unsupported method";
        }
        
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleCheckoutRequest(java.net.HttpExchange exchange) throws IOException {
        String response;
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines().collect(Collectors.joining("\n"));
            // Parse request body
            // Format: isbn,userId
            String[] parts = requestBody.split(",");
            if (parts.length == 2) {
                if (library.checkoutBook(parts[0], parts[1])) {
                    response = "Book checked out successfully";
                } else {
                    response = "Failed to checkout book";
                }
            } else {
                response = "Invalid checkout data";
            }
        } else {
            response = "Unsupported method";
        }
        
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleReturnRequest(java.net.HttpExchange exchange) throws IOException {
        String response;
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines().collect(Collectors.joining("\n"));
            // Parse request body
            // Format: isbn,userId
            String[] parts = requestBody.split(",");
            if (parts.length == 2) {
                if (library.returnBook(parts[0], parts[1])) {
                    response = "Book returned successfully";
                } else {
                    response = "Failed to return book";
                }
            } else {
                response = "Invalid return data";
            }
        } else {
            response = "Unsupported method";
        }
        
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
