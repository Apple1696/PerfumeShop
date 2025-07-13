package com.example.perfumeshop.data.models.entities;

public class OrderItem {
    private String _id;
    private Perfume perfume;
    private int quantity;
    private long totalPrice;

    public OrderItem() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Perfume getPerfume() {
        return perfume;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFormattedTotalPrice() {
        return String.format("%,d VND", totalPrice);
    }
}