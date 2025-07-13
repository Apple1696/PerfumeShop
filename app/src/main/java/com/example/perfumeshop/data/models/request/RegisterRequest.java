package com.example.perfumeshop.data.models.request;

public class RegisterRequest {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String phone;
    private int yob;
    private boolean gender;

    public RegisterRequest(String fullName, String username, String email, String password, String phone, int yob, boolean gender) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.yob = yob;
        this.gender = gender;
    }

    // Existing getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // New getter and setter for phone
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getYob() { return yob; }
    public void setYob(int yob) { this.yob = yob; }

    public boolean isGender() { return gender; }
    public void setGender(boolean gender) { this.gender = gender; }
}