package com.example.baocaonhom.dto;

public class ProductVariantCreateDTO {
    private int productId;
    private String color;
    private String size;
    private double price;
    private int quantity;

    public ProductVariantCreateDTO(int productId, String color, String size, double price, int quantity) {
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters & Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

