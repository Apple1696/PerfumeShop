package com.example.perfumeshop.data.models.entities;

public class Comment {
    private String _id;
    private String content;
    private String userId;
    private String userName;
    private String createdAt;
    private int rating;
    private Author author;

    public Comment() {}

    public Comment(String _id, String content, String userId, String userName, String createdAt, int rating, Author author) {
        this._id = _id;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.rating = rating;
        this.author = author;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    // Inner class for author information
    public static class Author {
        private String _id;
        private String username;
        private String email;

        public Author() {}

        public Author(String _id, String username, String email) {
            this._id = _id;
            this.username = username;
            this.email = email;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
