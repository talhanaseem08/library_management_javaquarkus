package com.library.service.impl;

import com.library.dto.MemberDTO;
import com.library.exception.MemberNotFoundException;
import com.library.service.MemberService;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class MemberServiceImpl implements MemberService {
    
    private static final Logger LOG = Logger.getLogger(MemberServiceImpl.class);
    
    // In-memory storage using ConcurrentHashMap for thread safety
    private final Map<String, MemberDTO> members = new ConcurrentHashMap<>();
    
    @Override
    public List<MemberDTO> getAllMembers() {
        LOG.info("Retrieving all members. Total members: " + members.size());
        return new ArrayList<>(members.values());
    }
    
    @Override
    public MemberDTO getMemberById(String id) {
        LOG.info("Retrieving member with ID: " + id);
        MemberDTO member = members.get(id);
        if (member == null) {
            LOG.error("Member not found with ID: " + id);
            throw new MemberNotFoundException("Member not found with ID: " + id);
        }
        return member;
    }
    
    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        String id = UUID.randomUUID().toString();
        memberDTO.setId(id);
        
        members.put(id, memberDTO);
        LOG.info("Created new member: " + memberDTO);
        
        return memberDTO;
    }
    
    @Override
    public MemberDTO updateMember(String id, MemberDTO memberDTO) {
        LOG.info("Updating member with ID: " + id);
        
        if (!members.containsKey(id)) {
            LOG.error("Member not found with ID: " + id);
            throw new MemberNotFoundException("Member not found with ID: " + id);
        }
        
        memberDTO.setId(id);
        members.put(id, memberDTO);
        LOG.info("Updated member: " + memberDTO);
        
        return memberDTO;
    }
    
    @Override
    public void deleteMember(String id) {
        LOG.info("Deleting member with ID: " + id);
        
        if (!members.containsKey(id)) {
            LOG.error("Member not found with ID: " + id);
            throw new MemberNotFoundException("Member not found with ID: " + id);
        }
        
        members.remove(id);
        LOG.info("Deleted member with ID: " + id);
    }
}
