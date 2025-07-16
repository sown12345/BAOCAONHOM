package com.example.baocaonhom.dto;

public class SetThumbnailRequest {
    private int productId;
    private int imageId;

    public SetThumbnailRequest(int productId, int imageId) {
        this.productId = productId;
        this.imageId = imageId;
    }

    public int getProductId() { return productId; }
    public int getImageId() { return imageId; }
}
