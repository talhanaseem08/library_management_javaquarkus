package com.library.dto;

public class BookResponseDTO {
    
    private String id;
    private String title;
    private String author;
    private boolean available;
    private int quantity; // Number of copies available
    
    // Constructors
    public BookResponseDTO() {}
    
    public BookResponseDTO(String id, String title, String author, boolean available, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
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
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "BookResponseDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", available=" + available +
                ", quantity=" + quantity +
                '}';
    }
}
