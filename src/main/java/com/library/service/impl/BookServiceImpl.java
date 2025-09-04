package com.library.service.impl;

import com.library.dto.BookRequestDTO;
import com.library.dto.BookResponseDTO;
import com.library.exception.BookNotFoundException;
import com.library.service.BookService;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class BookServiceImpl implements BookService {
    
    private static final Logger LOG = Logger.getLogger(BookServiceImpl.class);
    
    // In-memory storage using ConcurrentHashMap for thread safety
    private final Map<String, BookResponseDTO> books = new ConcurrentHashMap<>();
    
    @Override
    public List<BookResponseDTO> getAllBooks() {
        LOG.info("Retrieving all books. Total books: " + books.size());
        return new ArrayList<>(books.values());
    }
    
    @Override
    public BookResponseDTO getBookById(String id) {
        LOG.info("Retrieving book with ID: " + id);
        BookResponseDTO book = books.get(id);
        if (book == null) {
            LOG.error("Book not found with ID: " + id);
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        return book;
    }
    
    @Override
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
                true, // New books are available by default
                1    // Start with quantity 1
            );
            
            books.put(id, bookResponseDTO);
            LOG.info("Created new book: " + bookResponseDTO);
            
            return bookResponseDTO;
        }
    }
    
    @Override
    public BookResponseDTO updateBook(String id, BookRequestDTO bookRequestDTO) {
        LOG.info("Updating book with ID: " + id);
        
        if (!books.containsKey(id)) {
            LOG.error("Book not found with ID: " + id);
            throw new BookNotFoundException("Book not found with ID: " + id);
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
    
    @Override
    public void deleteBook(String id) {
        LOG.info("Deleting book with ID: " + id);
        
        if (!books.containsKey(id)) {
            LOG.error("Book not found with ID: " + id);
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        
        books.remove(id);
        LOG.info("Deleted book with ID: " + id);
    }
    
    @Override
    public BookResponseDTO updateBookAvailability(String id, boolean available) {
        LOG.info("Updating book availability. ID: " + id + ", Available: " + available);
        
        BookResponseDTO book = getBookById(id);
        BookResponseDTO updatedBook = new BookResponseDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            available,
            book.getQuantity()
        );
        books.put(id, updatedBook);
        
        LOG.info("Updated book availability: " + updatedBook);
        return updatedBook;
    }
    
    // New method to decrease quantity when book is lent
    public BookResponseDTO decreaseBookQuantity(String id) {
        LOG.info("Decreasing book quantity. ID: " + id);
        
        BookResponseDTO book = getBookById(id);
        if (book.getQuantity() <= 0) {
            LOG.error("Book has no quantity available. ID: " + id);
            throw new RuntimeException("Book has no quantity available. ID: " + id);
        }
        
        int newQuantity = book.getQuantity() - 1;
        BookResponseDTO updatedBook = new BookResponseDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            newQuantity > 0, // Available if quantity > 0
            newQuantity
        );
        books.put(id, updatedBook);
        
        LOG.info("Decreased book quantity: " + updatedBook);
        return updatedBook;
    }
    
    // New method to increase quantity when book is returned
    public BookResponseDTO increaseBookQuantity(String id) {
        LOG.info("Increasing book quantity. ID: " + id);
        
        BookResponseDTO book = getBookById(id);
        int newQuantity = book.getQuantity() + 1;
        BookResponseDTO updatedBook = new BookResponseDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            true, // Always available when returned
            newQuantity
        );
        books.put(id, updatedBook);
        
        LOG.info("Increased book quantity: " + updatedBook);
        return updatedBook;
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
