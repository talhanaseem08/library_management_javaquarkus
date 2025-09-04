package com.library.dto;

import jakarta.validation.constraints.NotBlank;

public class LendingRequestDTO {
    
    @NotBlank(message = "Book ID is required")
    private String bookId;
    
    @NotBlank(message = "Member ID is required")
    private String memberId;
    
    // Constructors
    public LendingRequestDTO() {}
    
    public LendingRequestDTO(String bookId, String memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
    }
    
    // Getters and Setters
    public String getBookId() {
        return bookId;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public String getMemberId() {
        return memberId;
    }
    
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    
    @Override
    public String toString() {
        return "LendingRequestDTO{" +
                "bookId='" + bookId + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}

