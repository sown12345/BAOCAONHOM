package com.example.baocaonhom.network;

import com.example.baocaonhom.dto.CartItemUpdateRequest;
import com.example.baocaonhom.dto.CartRequest;
import com.example.baocaonhom.dto.CartResponse;
import com.example.baocaonhom.dto.LoginRequest;
import com.example.baocaonhom.dto.OrderCreateRequest;
import com.example.baocaonhom.dto.OrderHistoryResponse;
import com.example.baocaonhom.dto.ProductCreateDTO;
import com.example.baocaonhom.dto.ProductHomeDTO;
import com.example.baocaonhom.dto.ProductUpdateDTO;
import com.example.baocaonhom.dto.ProductVariantCreateDTO;
import com.example.baocaonhom.dto.RegisterRequest;
import com.example.baocaonhom.dto.SetThumbnailRequest;
import com.example.baocaonhom.dto.UserUpdateDTO;
import com.example.baocaonhom.models.Product;
import com.example.baocaonhom.models.Category;
import com.example.baocaonhom.models.ProductImage;
import com.example.baocaonhom.models.ProductVariant;
import com.example.baocaonhom.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // PRODUCT
    @GET("admin/product")
    Call<List<Product>> getAllProducts();

    @GET("admin/product/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @POST("admin/product")
    Call<Product> createProduct(@Body ProductCreateDTO product);

    @PUT("admin/product/{id}")
    Call<Product> updateProduct(@Path("id") int id, @Body ProductUpdateDTO product);
    @DELETE("admin/product/{id}")
    Call<Void> deleteProduct(@Path("id") int id);

    //CATEGORIES
    @GET("admin/category")
    Call<List<Category>> getAllCategories();

    @POST("admin/category")
    Call<Category> createCategory(@Body Category category);

    @PUT("admin/category/{id}")
    Call<Category> updateCategory(@Path("id") int id, @Body Category category);


    @DELETE("admin/category/{id}")
    Call<Void> deleteCategory(@Path("id") int id);


    // PRODUCT IMAGES
    @GET("admin/productimage/by-product/{productId}")
    Call<List<ProductImage>> getImagesByProduct(@Path("productId") int productId);

    @POST("admin/productimage")
    Call<ProductImage> addProductImage(@Body ProductImage image);

    @retrofit2.http.DELETE("admin/productimage/{id}")
    Call<Void> deleteProductImage(@Path("id") int id);

    @POST("admin/productimage/set-thumbnail")
    Call<Void> setThumbnail(@Body SetThumbnailRequest request);

    // PRODUCT VARIANTS
    @GET("admin/productvariant/by-product/{productId}")
    Call<List<ProductVariant>> getVariantsByProduct(@Path("productId") int productId);

    @POST("admin/productvariant")
    Call<ProductVariant> addProductVariant(@Body ProductVariantCreateDTO dto);


    @PUT("admin/productvariant/{id}")
    Call<ProductVariant> updateProductVariant(@Path("id") int id, @Body ProductVariant variant);

    @retrofit2.http.DELETE("admin/productvariant/{id}")
    Call<Void> deleteProductVariant(@Path("id") int id);

    // Customer
    @GET("home/customer/Home/products")
    Call<List<ProductHomeDTO>> getHomeProducts();

    @GET("customer/user/{id}")
    Call<User> getUserById(@Path("id") int id);

    @PUT("customer/user/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body UserUpdateDTO dto);

    @POST("customer/user/login")
    Call<User> login(@Body LoginRequest request);
    @GET("home/customer/Home/product/{id}")
    Call<Product> getProductDetail(@Path("id") int id);

    //CART
    @POST("customer/cart/add")
    Call<Void> addToCart(@Body CartRequest request);
    @DELETE("customer/cart/user/{userId}")
    Call<Void> clearCartByUserId(@Path("userId") int userId);
    @DELETE("customer/Cart/cartitem/{id}")
    Call<Void> deleteCartItem(@Path("id") int id);

    @PUT("customer/Cart/cartitem/{id}")
    Call<Void> updateCartItem(@Path("id") int id, @Body CartItemUpdateRequest request);

    @GET("customer/cart/user/{userId}")
    Call<CartResponse> getCartByUserId(@Path("userId") int userId);

    //ORDER
    @POST("customer/order")
    Call<Void> createOrder(@Body OrderCreateRequest request);

    @GET("customer/order/user/{userId}")
    Call<List<OrderHistoryResponse>> getOrdersByUser(@Path("userId") int userId);
    @GET("customer/order/{orderId}")
    Call<OrderHistoryResponse> getOrderDetail(@Path("orderId") int orderId);
    @PUT("customer/order/cancel/{orderId}")
    Call<Void> cancelOrder(@Path("orderId") int orderId);

    //LOGIN
    @POST("customer/user/register")
    Call<Void> register(@Body RegisterRequest request);

}


