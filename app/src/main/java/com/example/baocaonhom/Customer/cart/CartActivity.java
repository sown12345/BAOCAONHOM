
package com.example.baocaonhom.Customer.cart;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.widget.Button;

import com.example.baocaonhom.R;
import com.example.baocaonhom.Customer.cart.CartItemAdapter;
import com.example.baocaonhom.dto.CartItemUpdateRequest;
import com.example.baocaonhom.dto.CartItemResponse;
import com.example.baocaonhom.dto.CartResponse;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtTotal;
    private Button btnCheckout;
    private CartItemAdapter adapter;
    private List<CartItemResponse> itemList = new ArrayList<>();
    private int userId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        // Lấy userId từ SharedPreferences
        userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.rvCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        adapter = new CartItemAdapter(this, itemList, new CartItemAdapter.OnCartActionListener() {
            @Override
            public void onPlus(CartItemResponse item) {
                int newQuantity = item.getQuantity() + 1;
                updateQuantity(item.getId(), newQuantity);
            }

            @Override
            public void onMinus(CartItemResponse item) {
                int newQuantity = item.getQuantity() - 1;
                if (newQuantity > 0) {
                    updateQuantity(item.getId(), newQuantity);
                } else {
                    deleteItem(item.getId());
                }
            }

            @Override
            public void onDelete(CartItemResponse item) {
                deleteItem(item.getId());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, com.example.baocaonhom.Customer.checkout.CheckoutConfirmActivity.class);
            startActivity(intent);
        });

        loadCart();
    }

    private void loadCart() {
        Log.d("API_CART", "Gọi API getCartByUserId với userId = " + userId);

        apiService.getCartByUserId(userId).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_CART", "API thành công");
                    Log.d("API_CART", "Số lượng sản phẩm: " + response.body().getItems().size());

                    if (!response.body().getItems().isEmpty()) {
                        CartItemResponse firstItem = response.body().getItems().get(0);
                        Log.d("API_CART", "Tên sản phẩm đầu: " + firstItem.getProductName());
                        Log.d("API_CART", "URL ảnh đầu: " + firstItem.getThumbnailUrl());
                    }

                    itemList.clear();
                    itemList.addAll(response.body().getItems());
                    adapter.notifyDataSetChanged();
                    updateTotal();
                } else {
                    Log.e("API_CART", "API trả về không hợp lệ hoặc body null");
                    Toast.makeText(CartActivity.this, "Giỏ hàng rỗng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API_CART", "API lỗi: " + t.getMessage());
                Toast.makeText(CartActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateTotal() {
        double total = 0;
        for (CartItemResponse item : itemList) {
            total += item.getPrice() * item.getQuantity();
        }
        DecimalFormat df = new DecimalFormat("###,###");
        txtTotal.setText(df.format(total) + " đ");
    }

    private void updateQuantity(int cartItemId, int quantity) {
        apiService.updateCartItem(cartItemId, new CartItemUpdateRequest(quantity)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadCart();
                } else {
                    Toast.makeText(CartActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem(int cartItemId) {
        apiService.deleteCartItem(cartItemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadCart();
                } else {
                    Toast.makeText(CartActivity.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }
}
