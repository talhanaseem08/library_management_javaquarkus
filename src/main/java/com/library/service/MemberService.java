package com.library.service;

import com.library.dto.MemberRequestDTO;
import com.library.dto.MemberResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class MemberService {
    
    private static final Logger LOG = Logger.getLogger(MemberService.class);
    
    // In-memory storage using ConcurrentHashMap for thread safety
    private final Map<String, MemberResponseDTO> members = new ConcurrentHashMap<>();
    
    public List<MemberResponseDTO> getAllMembers() {
        LOG.info("Retrieving all members. Total members: " + members.size());
        return new ArrayList<>(members.values());
    }
    
    public MemberResponseDTO getMemberById(String id) {
        LOG.info("Retrieving member with ID: " + id);
        MemberResponseDTO member = members.get(id);
        if (member == null) {
            LOG.error("Member not found with ID: " + id);
            throw new RuntimeException("Member not found with ID: " + id);
        }
        return member;
    }
    
    public MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO) {
        LOG.info("Creating new member: " + memberRequestDTO);
        
        // Check for duplicate email
        if (isEmailDuplicate(memberRequestDTO.getEmail())) {
            LOG.error("Member already exists with email: " + memberRequestDTO.getEmail());
            throw new RuntimeException("Member already exists with email: '" + memberRequestDTO.getEmail() + "'");
        }
        
        String id = UUID.randomUUID().toString();
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO(
            id,
            memberRequestDTO.getName(),
            memberRequestDTO.getEmail()
        );
        
        members.put(id, memberResponseDTO);
        LOG.info("Created new member: " + memberResponseDTO);
        
        return memberResponseDTO;
    }
    
    public MemberResponseDTO updateMember(String id, MemberRequestDTO memberRequestDTO) {
        LOG.info("Updating member with ID: " + id);
        
        if (!members.containsKey(id)) {
            LOG.error("Member not found with ID: " + id);
            throw new RuntimeException("Member not found with ID: " + id);
        }
        
        // Check for duplicate email (excluding the current member being updated)
        if (isEmailDuplicateExcludingId(memberRequestDTO.getEmail(), id)) {
            LOG.error("Member already exists with email: " + memberRequestDTO.getEmail());
            throw new RuntimeException("Member already exists with email: '" + memberRequestDTO.getEmail() + "'");
        }
        
        MemberResponseDTO updatedMember = new MemberResponseDTO(
            id,
            memberRequestDTO.getName(),
            memberRequestDTO.getEmail()
        );
        
        members.put(id, updatedMember);
        LOG.info("Updated member: " + updatedMember);
        
        return updatedMember;
    }
    
    public void deleteMember(String id) {
        LOG.info("Deleting member with ID: " + id);
        
        if (!members.containsKey(id)) {
            LOG.error("Member not found with ID: " + id);
            throw new RuntimeException("Member not found with ID: " + id);
        }
        
        members.remove(id);
        LOG.info("Deleted member with ID: " + id);
    }
    
    // Helper method to check for duplicate email
    private boolean isEmailDuplicate(String email) {
        return members.values().stream()
                .anyMatch(member -> member.getEmail().equalsIgnoreCase(email));
    }
    
    // Helper method to check for duplicate email excluding a specific ID (for updates)
    private boolean isEmailDuplicateExcludingId(String email, String excludeId) {
        return members.values().stream()
                .anyMatch(member -> !member.getId().equals(excludeId) && 
                                member.getEmail().equalsIgnoreCase(email));
    }
}