package com.example.perfumeshop.data.models.response;

import com.example.perfumeshop.data.models.entities.Order;

public class OrderDetailResponse {
    private int status;
    private String message;
    private Order order;

    public OrderDetailResponse() {}

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
