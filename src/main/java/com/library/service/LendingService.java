package com.library.service;

import com.library.dto.LendingRequestDTO;
import com.library.dto.LendingResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class LendingService {
    
    private static final Logger LOG = Logger.getLogger(LendingService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Inject
    BookService bookService;
    
    @Inject
    MemberService memberService;
    
    // In-memory storage using ConcurrentHashMap for thread safety
    private final Map<String, LendingResponseDTO> lendings = new ConcurrentHashMap<>();
    
    public List<LendingResponseDTO> getAllLendings() {
        LOG.info("Retrieving all lendings. Total lendings: " + lendings.size());
        return new ArrayList<>(lendings.values());
    }
    
    public LendingResponseDTO getLendingById(String id) {
        LOG.info("Retrieving lending with ID: " + id);
        LendingResponseDTO lending = lendings.get(id);
        if (lending == null) {
            LOG.error("Lending not found with ID: " + id);
            throw new RuntimeException("Lending not found with ID: " + id);
        }
        return lending;
    }
    
    public LendingResponseDTO lendBook(LendingRequestDTO lendingRequestDTO) {
        LOG.info("Lending book. Book ID: " + lendingRequestDTO.getBookId() + 
                ", Member ID: " + lendingRequestDTO.getMemberId());
        
        // Validate book exists and has quantity available
        var book = bookService.getBookById(lendingRequestDTO.getBookId());
        if (book.getQuantity() <= 0) {
            LOG.error("Book has no quantity available for lending. Book ID: " + lendingRequestDTO.getBookId());
            throw new RuntimeException("Book has no quantity available for lending. Book ID: " + lendingRequestDTO.getBookId());
        }
        
        // Validate member exists
        memberService.getMemberById(lendingRequestDTO.getMemberId());
        
        // Create lending record
        String lendingId = UUID.randomUUID().toString();
        String lendingDate = LocalDateTime.now().format(DATE_FORMATTER);
        
        LendingResponseDTO lending = new LendingResponseDTO(
            lendingId, 
            lendingRequestDTO.getBookId(), 
            lendingRequestDTO.getMemberId(), 
            lendingDate, 
            null // returnDate is null when book is lent
        );
        lendings.put(lendingId, lending);
        
        // Decrease book quantity
        bookService.decreaseBookQuantity(lendingRequestDTO.getBookId());
        
        LOG.info("Book lent successfully: " + lending);
        return lending;
    }
    
    public LendingResponseDTO returnBook(String lendingId) {
        LOG.info("Returning book. Lending ID: " + lendingId);
        
        LendingResponseDTO lending = getLendingById(lendingId);
        
        // Check if book is already returned
        if (lending.getReturnDate() != null) {
            LOG.warn("Book is already returned. Lending ID: " + lendingId);
            return lending;
        }
        
        // Mark book as returned
        String returnDate = LocalDateTime.now().format(DATE_FORMATTER);
        LendingResponseDTO updatedLending = new LendingResponseDTO(
            lending.getLendingId(),
            lending.getBookId(),
            lending.getMemberId(),
            lending.getLendingDate(),
            returnDate
        );
        lendings.put(lendingId, updatedLending);
        
        // Increase book quantity
        bookService.increaseBookQuantity(lending.getBookId());
        
        LOG.info("Book returned successfully: " + updatedLending);
        return updatedLending;
    }
    
    public List<LendingResponseDTO> getLendingHistory() {
        LOG.info("Retrieving lending history. Total lendings: " + lendings.size());
        return new ArrayList<>(lendings.values());
    }
}