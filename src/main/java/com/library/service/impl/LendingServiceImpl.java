package com.library.service.impl;

import com.library.dto.LendingDTO;
import com.library.exception.BookNotAvailableException;
import com.library.exception.LendingNotFoundException;
import com.library.service.BookService;
import com.library.service.LendingService;
import com.library.service.MemberService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class LendingServiceImpl implements LendingService {
    
    private static final Logger LOG = Logger.getLogger(LendingServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Inject
    BookService bookService;
    
    @Inject
    MemberService memberService;
    
    // In-memory storage using ConcurrentHashMap for thread safety
    private final Map<String, LendingDTO> lendings = new ConcurrentHashMap<>();
    
    @Override
    public List<LendingDTO> getAllLendings() {
        LOG.info("Retrieving all lendings. Total lendings: " + lendings.size());
        return new ArrayList<>(lendings.values());
    }
    
    @Override
    public LendingDTO getLendingById(String id) {
        LOG.info("Retrieving lending with ID: " + id);
        LendingDTO lending = lendings.get(id);
        if (lending == null) {
            LOG.error("Lending not found with ID: " + id);
            throw new LendingNotFoundException("Lending not found with ID: " + id);
        }
        return lending;
    }
    
    @Override
    public LendingDTO lendBook(String bookId, String memberId) {
        LOG.info("Lending book. Book ID: " + bookId + ", Member ID: " + memberId);
        
        // Validate book exists and is available
        var book = bookService.getBookById(bookId);
        if (!book.isAvailable()) {
            LOG.error("Book is not available for lending. Book ID: " + bookId);
            throw new BookNotAvailableException("Book is not available for lending. Book ID: " + bookId);
        }
        
        // Validate member exists
        memberService.getMemberById(memberId);
        
        // Create lending record
        String lendingId = UUID.randomUUID().toString();
        String lendingDate = LocalDateTime.now().format(DATE_FORMATTER);
        
        LendingDTO lending = new LendingDTO(lendingId, bookId, memberId, lendingDate, null);
        lendings.put(lendingId, lending);
        
        // Mark book as unavailable
        bookService.updateBookAvailability(bookId, false);
        
        LOG.info("Book lent successfully: " + lending);
        return lending;
    }
    
    @Override
    public LendingDTO returnBook(String lendingId) {
        LOG.info("Returning book. Lending ID: " + lendingId);
        
        LendingDTO lending = getLendingById(lendingId);
        
        // Check if book is already returned
        if (lending.getReturnDate() != null) {
            LOG.warn("Book is already returned. Lending ID: " + lendingId);
            return lending;
        }
        
        // Mark book as returned
        String returnDate = LocalDateTime.now().format(DATE_FORMATTER);
        lending.setReturnDate(returnDate);
        lendings.put(lendingId, lending);
        
        // Mark book as available
        bookService.updateBookAvailability(lending.getBookId(), true);
        
        LOG.info("Book returned successfully: " + lending);
        return lending;
    }
    
    @Override
    public List<LendingDTO> getLendingHistory() {
        LOG.info("Retrieving lending history. Total lendings: " + lendings.size());
        return new ArrayList<>(lendings.values());
    }
}
