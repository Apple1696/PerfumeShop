package com.example.perfumeshop.data.models.response;

import com.example.perfumeshop.data.models.entities.User;

public class UpdateProfileResponse {
    private String message;
    private User user;

    public UpdateProfileResponse() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}