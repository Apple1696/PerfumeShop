package com.example.perfumeshop.data.models.request;

import java.util.List;

public class OrderRequest {
    private String address;
    private String phone;
    private String email;
    private String customerName;
    private String paymentMethod;
    private List<OrderItem> orderItems;

    public OrderRequest(String address, String phone, String email, String customerName,
                       String paymentMethod, List<OrderItem> orderItems) {
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
        this.orderItems = orderItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public static class OrderItem {
        private String perfumeId;
        private int quantity;

        public OrderItem(String perfumeId, int quantity) {
            this.perfumeId = perfumeId;
            this.quantity = quantity;
        }

        public String getPerfumeId() {
            return perfumeId;
        }

        public void setPerfumeId(String perfumeId) {
            this.perfumeId = perfumeId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
