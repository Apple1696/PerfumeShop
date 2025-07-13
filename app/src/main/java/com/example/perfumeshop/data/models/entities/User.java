package com.example.perfumeshop.data.models.entities;

public class User {
    private String _id;
    private String fullName;
    private String email;
    private String username;
    private String phone;
    private int yob;
    private boolean gender;
    private boolean isAdmin;

    // Constructors
    public User() {}

    public User(String _id, String fullName, String email, String username, String phone, int yob, boolean gender, boolean isAdmin) {
        this._id = _id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.yob = yob;
        this.gender = gender;
        this.isAdmin = isAdmin;
    }

    // Existing getters and setters...

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    // Add these methods to User.java
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public int getYob() { return yob; }
    public void setYob(int yob) { this.yob = yob; }

    public boolean isGender() { return gender; }
    public void setGender(boolean gender) { this.gender = gender; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", yob=" + yob +
                ", gender=" + gender +
                ", isAdmin=" + isAdmin +
                '}';
    }
}