package com.library.dto;

import jakarta.validation.constraints.NotBlank;

public class LendingDTO {
    
    private String lendingId;
    
    @NotBlank(message = "Book ID is required")
    private String bookId;
    
    @NotBlank(message = "Member ID is required")
    private String memberId;
    
    private String lendingDate;
    private String returnDate; // nullable
    
    // Constructors
    public LendingDTO() {}
    
    public LendingDTO(String lendingId, String bookId, String memberId, String lendingDate, String returnDate) {
        this.lendingId = lendingId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.lendingDate = lendingDate;
        this.returnDate = returnDate;
    }
    
    // Getters and Setters
    public String getLendingId() {
        return lendingId;
    }
    
    public void setLendingId(String lendingId) {
        this.lendingId = lendingId;
    }
    
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
    
    public String getLendingDate() {
        return lendingDate;
    }
    
    public void setLendingDate(String lendingDate) {
        this.lendingDate = lendingDate;
    }
    
    public String getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
    
    @Override
    public String toString() {
        return "LendingDTO{" +
                "lendingId='" + lendingId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", lendingDate='" + lendingDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                '}';
    }
}
