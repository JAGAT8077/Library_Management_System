import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;
    private int totalCopies;
    private int availableCopies;
    private Map<String, LocalDate> borrowers;

    public Book(String title, String author, String isbn, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.borrowers = new HashMap<>();
    }

    public boolean checkOut(String userId) {
        if (availableCopies > 0) {
            availableCopies--;
            borrowers.put(userId, LocalDate.now());
            return true;
        }
        return false;
    }

    public boolean checkIn(String userId) {
        if (borrowers.containsKey(userId)) {
            availableCopies++;
            borrowers.remove(userId);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s by %s (ISBN: %s) - Available: %d/%d", 
                title, author, isbn, availableCopies, totalCopies);
    }
}
