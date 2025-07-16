package com.example.baocaonhom.database;


public class SqlQueries {
    // --- USERS ---
    public static final String LOGIN_USER =
            "SELECT Id, FullName, Role FROM Users WHERE Email = ? AND Password = ?";

    public static final String REGISTER_USER =
            "INSERT INTO Users (FullName, Email, Password, Role) VALUES (?, ?, ?, ?)";

    public static final String CHECK_EMAIL_EXISTS =
            "SELECT COUNT(*) FROM Users WHERE Email = ?";




// --- CATEGORIES ---
    public static final String GET_ALL_CATEGORIES =
            "SELECT * FROM Categories ORDER BY Id DESC";

    public static final String INSERT_CATEGORY =
            "INSERT INTO Categories (Name) VALUES (?)";

    public static final String DELETE_CATEGORY =
            "DELETE FROM Categories WHERE Id = ?";
    public static final String UPDATE_CATEGORY =
            "UPDATE Categories SET Name = ? WHERE Id = ?";

    // --- PRODUCTS ---
    public static final String GET_ALL_PRODUCTS =
            "SELECT * FROM Products ORDER BY Id DESC";

    public static final String INSERT_PRODUCT =
            "INSERT INTO Products (Name, Description, CategoryId) VALUES (?, ?, ?)";

    public static final String UPDATE_PRODUCT =
            "UPDATE Products SET Name = ?, Description = ?, CategoryId = ? WHERE Id = ?";

    public static final String DELETE_PRODUCT =
            "DELETE FROM Products WHERE Id = ?";

    // --- PRODUCT IMAGES ---
    public static final String GET_IMAGES_BY_PRODUCT_ID =
            "SELECT * FROM ProductImages WHERE ProductId = ? ORDER BY Id DESC";

    public static final String INSERT_PRODUCT_IMAGE =
            "INSERT INTO ProductImages (ProductId, ImagePath) VALUES (?, ?)";

    //--- PRODUCT VARIANT ----
    public static final String DELETE_PRODUCT_IMAGE =
            "DELETE FROM ProductImages WHERE Id = ?";

    public static final String GET_VARIANTS_BY_PRODUCT_ID =
            "SELECT * FROM ProductVariants WHERE ProductId = ?";

    public static final String INSERT_VARIANT =
            "INSERT INTO ProductVariants (ProductId, Size, Color, Price, Quantity) VALUES (?, ?, ?, ?, ?)";

    public static final String UPDATE_VARIANT =
            "UPDATE ProductVariants SET Size = ?, Color = ?, Price = ?, Quantity = ? WHERE Id = ?";

    public static final String DELETE_VARIANT =
            "DELETE FROM ProductVariants WHERE Id = ?";



}

