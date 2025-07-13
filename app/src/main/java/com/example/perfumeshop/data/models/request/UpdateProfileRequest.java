package com.example.perfumeshop.data.models.request;


public class UpdateProfileRequest {
    private String fullName;
    private int yob;
    private boolean gender;
    private String phone;

    public UpdateProfileRequest(String fullName, int yob, boolean gender, String phone) {
        this.fullName = fullName;
        this.yob = yob;
        this.gender = gender;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYob() {
        return yob;
    }

    public void setYob(int yob) {
        this.yob = yob;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}