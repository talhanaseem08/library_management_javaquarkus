package com.library.service;

import com.library.dto.MemberRequestDTO;
import com.library.dto.MemberResponseDTO;
import java.util.List;

public interface MemberService {
    
    List<MemberResponseDTO> getAllMembers();
    
    MemberResponseDTO getMemberById(String id);
    
    MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO);
    
    MemberResponseDTO updateMember(String id, MemberRequestDTO memberRequestDTO);
    
    void deleteMember(String id);
}
