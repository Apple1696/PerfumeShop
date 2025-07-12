package com.example.perfumeshop.data.models.request;

public class CommentRequest {
    private int rating;
    private String content;

    public CommentRequest() {}

    public CommentRequest(int rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    // Getters and Setters
    public int getRating() { 
        return rating; 
    }
    
    public void setRating(int rating) { 
        this.rating = rating; 
    }

    public String getContent() { 
        return content; 
    }
    
    public void setContent(String content) { 
        this.content = content; 
    }
}
