package com.example.perfumeshop.data.models.response;

import com.example.perfumeshop.data.models.entities.User;

public class AuthResponse {
    private User user;
    private String token;

    public AuthResponse() {}

    public AuthResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    // Getters and Setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "user=" + user +
                ", token='" + token + '\'' +
                '}';
    }
}