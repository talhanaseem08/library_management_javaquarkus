package com.library.service;

import com.library.dto.BookRequestDTO;
import com.library.dto.BookResponseDTO;
import java.util.List;

public interface BookService {
    
    List<BookResponseDTO> getAllBooks();
    
    BookResponseDTO getBookById(String id);
    
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);
    
    BookResponseDTO updateBook(String id, BookRequestDTO bookRequestDTO);
    
    void deleteBook(String id);
    
    BookResponseDTO updateBookAvailability(String id, boolean available);
    
    // New methods for quantity management
    BookResponseDTO decreaseBookQuantity(String id);
    
    BookResponseDTO increaseBookQuantity(String id);
}
