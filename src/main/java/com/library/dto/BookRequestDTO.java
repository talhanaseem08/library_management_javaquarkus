package com.library.dto;

import jakarta.validation.constraints.NotBlank;

public class BookRequestDTO {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Author is required")
    private String author;
    
    // Constructors
    public BookRequestDTO() {}
    
    public BookRequestDTO(String title, String author) {
        this.title = title;
        this.author = author;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    @Override
    public String toString() {
        return "BookRequestDTO{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}

