package com.example.perfumeshop.data.models.response;

import com.example.perfumeshop.data.models.entities.Order;

import java.util.List;

public class OrderHistoryResponse {
    private int status;
    private String message;
    private List<Order> orders;

    public OrderHistoryResponse() {}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}