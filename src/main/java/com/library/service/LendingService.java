package com.library.service;

import com.library.dto.LendingDTO;
import java.util.List;

public interface LendingService {
    
    List<LendingDTO> getAllLendings();
    
    LendingDTO getLendingById(String id);
    
    LendingDTO lendBook(String bookId, String memberId);
    
    LendingDTO returnBook(String lendingId);
    
    List<LendingDTO> getLendingHistory();
}
