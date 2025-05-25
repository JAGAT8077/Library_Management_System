import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String userId;
    private String userType;
    private List<String> booksBorrowed;

    public User(String name, String userId, String userType) {
        this.name = name;
        this.userId = userId;
        this.userType = userType;
        this.booksBorrowed = new ArrayList<>();
    }

    public void addBook(String isbn) {
        booksBorrowed.add(isbn);
    }

    public void removeBook(String isbn) {
        booksBorrowed.remove(isbn);
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s, Type: %s)", name, userId, userType);
    }
}
