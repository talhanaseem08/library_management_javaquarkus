package com.library.service;

import com.library.dto.BookRequestDTO;
import com.library.dto.BookResponseDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class BookService {
    
    private static final Logger LOG = Logger.getLogger(BookService.class);
    
    // In-memory storage using ConcurrentHashMap for thread safety
    private final Map<String, BookResponseDTO> books = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void initializeBooks() {
        LOG.info("Initializing BookService with sample books...");
        
        // Add some sample books
        String[][] sampleBooks = {
            {"Clean Code", "Robert C. Martin"},
            {"Design Patterns", "Gang of Four"},
            {"Effective Java", "Joshua Bloch"},
            {"Head First Java", "Kathy Sierra"},
            {"Java: The Complete Reference", "Herbert Schildt"},
            {"Spring in Action", "Craig Walls"},
            {"Java Concurrency in Practice", "Brian Goetz"},
            {"Thinking in Java", "Bruce Eckel"},
            {"To Kill a Mockingbird", "Harper Lee"},
            {"1984", "George Orwell"},
            {"Pride and Prejudice", "Jane Austen"},
            {"The Great Gatsby", "F. Scott Fitzgerald"},
            {"The Hobbit", "J.R.R. Tolkien"},
            {"The Lord of the Rings", "J.R.R. Tolkien"},
            {"Harry Potter and the Philosopher's Stone", "J.K. Rowling"},
            {"The Hunger Games", "Suzanne Collins"},
            {"The Fault in Our Stars", "John Green"},
            {"Gone Girl", "Gillian Flynn"},
            {"The Da Vinci Code", "Dan Brown"},
            {"The Kite Runner", "Khaled Hosseini"}
        };
        
        for (String[] book : sampleBooks) {
            try {
                String id = UUID.randomUUID().toString().substring(0,5);
                BookResponseDTO bookResponseDTO = new BookResponseDTO(
                    id, 
                    book[0], 
                    book[1], 
                    true, // Available
                    1     // Quantity
                );
                
                books.put(id, bookResponseDTO);
                LOG.info("Added sample book: " + book[0] + " by " + book[1]);
                
            } catch (Exception e) {
                LOG.warn("Error adding sample book: " + e.getMessage());
            }
        }
        
        LOG.info("BookService initialization completed. Total books: " + books.size());
    }
    
    public List<BookResponseDTO> getAllBooks() {
        LOG.info("Retrieving all books. Total books: " + books.size());
        return new ArrayList<>(books.values());
    }
    
    public BookResponseDTO getBookById(String id) {
        LOG.info("Retrieving book with ID: " + id);
        BookResponseDTO book = books.get(id);
        if (book == null) {
            LOG.error("Book not found with ID: " + id);
            throw new RuntimeException("Book not found with ID: " + id);
        }
        return book;
    }
    
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        LOG.info("Creating new book: " + bookRequestDTO);
        
        // Check if book already exists (same title and author)
        BookResponseDTO existingBook = findBookByTitleAndAuthor(bookRequestDTO.getTitle(), bookRequestDTO.getAuthor());
        
        if (existingBook != null) {
            // Book exists, increase quantity
            LOG.info("Book already exists, increasing quantity. Current quantity: " + existingBook.getQuantity());
            existingBook.setQuantity(existingBook.getQuantity() + 1);
            existingBook.setAvailable(existingBook.getQuantity() > 0);
            books.put(existingBook.getId(), existingBook);
            LOG.info("Updated book quantity: " + existingBook);
            return existingBook;
        } else {
            // New book, create with quantity 1
            String id = UUID.randomUUID().toString().substring(0,5);
            BookResponseDTO bookResponseDTO = new BookResponseDTO(
                id, 
                bookRequestDTO.getTitle(), 
                bookRequestDTO.getAuthor(), 
                true, // Available
                1     // Quantity
            );
            
            books.put(id, bookResponseDTO);
            LOG.info("Created new book: " + bookResponseDTO);
            return bookResponseDTO;
        }
    }
    
    public BookResponseDTO updateBook(String id, BookRequestDTO bookRequestDTO) {
        LOG.info("Updating book with ID: " + id);
        
        if (!books.containsKey(id)) {
            LOG.error("Book not found with ID: " + id);
            throw new RuntimeException("Book not found with ID: " + id);
        }
        
        BookResponseDTO existingBook = books.get(id);
        BookResponseDTO updatedBook = new BookResponseDTO(
            id,
            bookRequestDTO.getTitle(),
            bookRequestDTO.getAuthor(),
            existingBook.getQuantity() > 0, // Available if quantity > 0
            existingBook.getQuantity() // Keep existing quantity
        );
        
        books.put(id, updatedBook);
        LOG.info("Updated book: " + updatedBook);
        
        return updatedBook;
    }
    
    public void deleteBook(String id) {
        LOG.info("Deleting book with ID: " + id);
        
        if (!books.containsKey(id)) {
            LOG.error("Book not found with ID: " + id);
            throw new RuntimeException("Book not found with ID: " + id);
        }
        
        books.remove(id);
        LOG.info("Deleted book with ID: " + id);
    }
    
    public BookResponseDTO updateBookAvailability(String id, boolean available) {
        LOG.info("Updating book availability. ID: " + id + ", Available: " + available);
        
        BookResponseDTO book = books.get(id);
        if (book == null) {
            LOG.error("Book not found with ID: " + id);
            throw new RuntimeException("Book not found with ID: " + id);
        }
        
        // Update quantity based on availability
        if (available && book.getQuantity() == 0) {
            book.setQuantity(1);
        } else if (!available && book.getQuantity() > 0) {
            book.setQuantity(0);
        }
        
        book.setAvailable(book.getQuantity() > 0);
        books.put(id, book);
        LOG.info("Updated book availability: " + book);
        
        return book;
    }
    
    public BookResponseDTO decreaseBookQuantity(String id) {
        LOG.info("Decreasing book quantity for ID: " + id);
        
        BookResponseDTO book = books.get(id);
        if (book == null) {
            LOG.error("Book not found with ID: " + id);
            throw new RuntimeException("Book not found with ID: " + id);
        }
        
        if (book.getQuantity() <= 0) {
            LOG.error("Cannot decrease quantity. Book has no copies available. Book ID: " + id);
            throw new RuntimeException("Book has no quantity available for lending. Book ID: " + id);
        }
        
        book.setQuantity(book.getQuantity() - 1);
        book.setAvailable(book.getQuantity() > 0);
        books.put(id, book);
        LOG.info("Decreased book quantity: " + book);
        
        return book;
    }
    
    public BookResponseDTO increaseBookQuantity(String id) {
        LOG.info("Increasing book quantity for ID: " + id);
        
        BookResponseDTO book = books.get(id);
        if (book == null) {
            LOG.error("Book not found with ID: " + id);
            throw new RuntimeException("Book not found with ID: " + id);
        }
        
        book.setQuantity(book.getQuantity() + 1);
        book.setAvailable(true);
        books.put(id, book);
        LOG.info("Increased book quantity: " + book);
        
        return book;
    }
    
    // Helper method to find book by title and author
    private BookResponseDTO findBookByTitleAndAuthor(String title, String author) {
        return books.values().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title) && 
                               book.getAuthor().equalsIgnoreCase(author))
                .findFirst()
                .orElse(null);
    }
}