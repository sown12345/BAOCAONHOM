package com.example.baocaonhom.dto;

import java.util.List;

public class OrderHistoryResponse {
    private int orderId;
    private String orderDate;
    private String status;
    private double totalAmount;
    private List<OrderItemHistory> items;

    // Getters & Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItemHistory> getItems() { return items; }
    public void setItems(List<OrderItemHistory> items) { this.items = items; }
}

