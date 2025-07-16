package com.example.baocaonhom.models;

public class ProductImage {
    private int id;
    private int productId;
    private String imagePath;
    private boolean isThumbnail;

    public ProductImage() {
    }

    public ProductImage(int id, int productId, String imagePath, boolean isThumbnail) {
        this.id = id;
        this.productId = productId;
        this.imagePath = imagePath;
        this.isThumbnail = isThumbnail;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isThumbnail() { return isThumbnail; }
    public void setThumbnail(boolean thumbnail) { isThumbnail = thumbnail; }

    public String getImageUrl() { return imagePath; }
}
