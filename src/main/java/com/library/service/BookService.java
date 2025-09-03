package com.library.service;

import com.library.dto.BookDTO;
import java.util.List;

public interface BookService {
    
    List<BookDTO> getAllBooks();
    
    BookDTO getBookById(String id);
    
    BookDTO createBook(BookDTO bookDTO);
    
    BookDTO updateBook(String id, BookDTO bookDTO);
    
    void deleteBook(String id);
    
    BookDTO updateBookAvailability(String id, boolean available);
}
