import java.io.*;
import java.util.*;

public class Library implements Serializable {
    private Map<String, Book> books = new HashMap<>();
    private Map<String, User> users = new HashMap<>();

    public boolean addBook(String title, String author, String isbn, int totalCopies) {
        if (books.containsKey(isbn)) {
            return false;
        }
        books.put(isbn, new Book(title, author, isbn, totalCopies));
        saveData();
        return true;
    }

    public boolean removeBook(String isbn) {
        if (books.containsKey(isbn)) {
            books.remove(isbn);
            saveData();
            return true;
        }
        return false;
    }

    public boolean addUser(String name, String userId, String userType) {
        if (users.containsKey(userId)) {
            return false;
        }
        users.put(userId, new User(name, userId, userType));
        saveData();
        return true;
    }

    public boolean removeUser(String userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            saveData();
            return true;
        }
        return false;
    }

    public boolean checkoutBook(String isbn, String userId) {
        if (!books.containsKey(isbn) || !users.containsKey(userId)) {
            return false;
        }
        
        Book book = books.get(isbn);
        User user = users.get(userId);
        
        if (book.checkOut(userId)) {
            user.addBook(isbn);
            saveData();
            return true;
        }
        return false;
    }

    public boolean returnBook(String isbn, String userId) {
        if (!books.containsKey(isbn) || !users.containsKey(userId)) {
            return false;
        }
        
        Book book = books.get(isbn);
        User user = users.get(userId);
        
        if (book.checkIn(userId)) {
            user.removeBook(isbn);
            saveData();
            return true;
        }
        return false;
    }

    public List<Book> listAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<User> listAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("library_data.ser"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error saving library data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File("library_data.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Library savedLibrary = (Library) ois.readObject();
                this.books = savedLibrary.books;
                this.users = savedLibrary.users;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading library data: " + e.getMessage());
            }
        }
    }
}
