package com.example.baocaonhom.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    private int id;
    private String name;
    private String description;
    private int categoryId;
    private Category category;

    @SerializedName("variants")
    private List<ProductVariant> productVariants;

    @SerializedName("images")
    private List<ProductImage> productImages;

    private int tempPrice;
    public int getTempPrice() { return tempPrice; }
    public void setTempPrice(int tempPrice) { this.tempPrice = tempPrice; }

    public Product(int id, String name, String description, int categoryId, Category category, List<ProductVariant> productVariants, List<ProductImage> productImages) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.category = category;
        this.productVariants = productVariants;
        this.productImages = productImages;
    }

    public Product(int id, String name, String description, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
    }

    public Product() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<ProductVariant> getProductVariants() { return productVariants; }
    public void setProductVariants(List<ProductVariant> productVariants) { this.productVariants = productVariants; }

    public List<ProductImage> getProductImages() { return productImages; }
    public void setProductImages(List<ProductImage> productImages) { this.productImages = productImages; }

    public List<ProductVariant> getVariants() { return productVariants; }
    public void setVariants(List<ProductVariant> variants) { this.productVariants = variants; }

    public List<ProductImage> getImages() { return productImages; }
    public void setImages(List<ProductImage> images) { this.productImages = images; }

    public String getCategoryName() {
        return category != null ? category.getName() : "Chưa rõ";
    }
}
