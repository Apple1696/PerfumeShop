package com.example.perfumeshop.data.models.entities;

public class User {
    private String _id;
    private String fullName;
    private String email;
    private int yob;
    private boolean gender;
    private boolean isAdmin;

    // Constructors
    public User() {}

    public User(String _id, String fullName, String email, int yob, boolean gender, boolean isAdmin) {
        this._id = _id;
        this.fullName = fullName;
        this.email = email;
        this.yob = yob;
        this.gender = gender;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getYob() { return yob; }
    public void setYob(int yob) { this.yob = yob; }

    public boolean isGender() { return gender; }
    public void setGender(boolean gender) { this.gender = gender; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", yob=" + yob +
                ", gender=" + gender +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
