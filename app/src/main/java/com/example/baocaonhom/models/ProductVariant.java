package com.example.baocaonhom.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductVariant {
    private int id;
    private int productId;
    private String color;
    private String size;
    private Double price;
    private int quantity;

    public ProductVariant(int id, int productId, String color, String size, Double price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

