package com.example.perfumeshop.data.models.entities;

public class Comment {
    private String _id;
    private String content;
    private String userId;
    private String userName;
    private String createdAt;

    public Comment() {}

    public Comment(String _id, String content, String userId, String userName, String createdAt) {
        this._id = _id;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
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
}
