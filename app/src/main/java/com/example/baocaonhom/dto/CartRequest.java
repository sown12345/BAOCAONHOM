package com.example.baocaonhom.dto;

public class CartRequest {
    private int userId;
    private int productVariantId;
    private int quantity;

    public CartRequest(int userId, int productVariantId, int quantity) {
        this.userId = userId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
