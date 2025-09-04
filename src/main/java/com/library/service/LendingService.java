package com.library.service;

import com.library.dto.LendingRequestDTO;
import com.library.dto.LendingResponseDTO;
import java.util.List;

public interface LendingService {
    
    List<LendingResponseDTO> getAllLendings();
    
    LendingResponseDTO getLendingById(String id);
    
    LendingResponseDTO lendBook(LendingRequestDTO lendingRequestDTO);
    
    LendingResponseDTO returnBook(String lendingId);
    
    List<LendingResponseDTO> getLendingHistory();
}
