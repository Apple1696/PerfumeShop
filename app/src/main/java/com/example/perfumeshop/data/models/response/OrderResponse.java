package com.example.perfumeshop.data.models.response;

public class OrderResponse {
    private boolean success;
    private String orderId;
    private long orderCode;
    private long totalAmount;
    private String paymentUrl; // Only for PAYOS
    private String message; // Only for COD

    public OrderResponse() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(long orderCode) {
        this.orderCode = orderCode;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPayOSPayment() {
        return paymentUrl != null && !paymentUrl.isEmpty();
    }

    public boolean isCODPayment() {
        return message != null && !message.isEmpty();
    }
}
