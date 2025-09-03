package com.library.service;

import com.library.dto.MemberDTO;
import java.util.List;

public interface MemberService {
    
    List<MemberDTO> getAllMembers();
    
    MemberDTO getMemberById(String id);
    
    MemberDTO createMember(MemberDTO memberDTO);
    
    MemberDTO updateMember(String id, MemberDTO memberDTO);
    
    void deleteMember(String id);
}
