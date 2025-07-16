package com.example.baocaonhom.dto;

import java.util.List;

public class OrderCreateRequest {
    private int userId;
    private List<OrderItem> items;
    private PaymentRequest payment;

    public OrderCreateRequest(int userId, List<OrderItem> items, PaymentRequest payment) {
        this.userId = userId;
        this.items = items;
        this.payment = payment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public PaymentRequest getPayment() {
        return payment;
    }

    public void setPayment(PaymentRequest payment) {
        this.payment = payment;
    }
}

