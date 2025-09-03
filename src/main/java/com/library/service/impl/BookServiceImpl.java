package com.library.service.impl;

import com.library.dto.BookDTO;
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
    private final Map<String, BookDTO> books = new ConcurrentHashMap<>();
    
    @Override
    public List<BookDTO> getAllBooks() {
        LOG.info("Retrieving all books. Total books: " + books.size());
        return new ArrayList<>(books.values());
    }
    
    @Override
    public BookDTO getBookById(String id) {
        LOG.info("Retrieving book with ID: " + id);
        BookDTO book = books.get(id);
        if (book == null) {
            LOG.error("Book not found with ID: " + id);
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        return book;
    }
    
    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        String id = UUID.randomUUID().toString();
        bookDTO.setId(id);
        bookDTO.setAvailable(true); // New books are available by default
        
        books.put(id, bookDTO);
        LOG.info("Created new book: " + bookDTO);
        
        return bookDTO;
    }
    
    @Override
    public BookDTO updateBook(String id, BookDTO bookDTO) {
        LOG.info("Updating book with ID: " + id);
        
        if (!books.containsKey(id)) {
            LOG.error("Book not found with ID: " + id);
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        
        bookDTO.setId(id);
        books.put(id, bookDTO);
        LOG.info("Updated book: " + bookDTO);
        
        return bookDTO;
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
    public BookDTO updateBookAvailability(String id, boolean available) {
        LOG.info("Updating book availability. ID: " + id + ", Available: " + available);
        
        BookDTO book = getBookById(id);
        book.setAvailable(available);
        books.put(id, book);
        
        LOG.info("Updated book availability: " + book);
        return book;
    }
}
