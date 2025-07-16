package com.example.baocaonhom.Customer.checkout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.CartItemResponse;
import com.example.baocaonhom.dto.CartResponse;
import com.example.baocaonhom.dto.OrderCreateRequest;
import com.example.baocaonhom.dto.OrderItem;
import com.example.baocaonhom.dto.PaymentRequest;
import com.example.baocaonhom.models.User;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutConfirmActivity extends AppCompatActivity {

    private TextView tvAddress, tvPaymentMethod, tvTotal;
    private Button btnConfirmOrder;
    private int userId;
    private double totalAmount = 0;
    private List<OrderItem> orderItems = new ArrayList<>();
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_confirm);

        tvAddress = findViewById(R.id.tvAddress);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvTotal = findViewById(R.id.tvTotal);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        userId = getUserId();
        api = RetrofitClient.getInstance().create(ApiService.class);

        loadUserInfo();
        loadCartInfo();

        btnConfirmOrder.setOnClickListener(v -> placeOrder());
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }

    private void loadUserInfo() {
        api.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String address = response.body().getAddress();
                    tvAddress.setText("Địa chỉ: " + address);
                } else {
                    Log.e("ORDER", "Lỗi load user info: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(CheckOutConfirmActivity.this, "Lỗi lấy địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartInfo() {
        api.getCartByUserId(userId).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItemResponse> items = response.body().getItems();
                    totalAmount = 0;
                    orderItems.clear();

                    for (CartItemResponse item : items) {
                        int variantId = item.getProductVariantId();
                        int qty = item.getQuantity();
                        double price = item.getPrice();
                        totalAmount += qty * price;

                        orderItems.add(new OrderItem(variantId, qty, price));
                    }

                    tvTotal.setText(String.format("Tổng tiền: %,.0f đ", totalAmount));
                } else {
                    Log.e("ORDER", "Lỗi load cart: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(CheckOutConfirmActivity.this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
                Log.e("ORDER", "Lỗi load cart: " + t.getMessage());
            }
        });
    }

    private void placeOrder() {
        if (orderItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        PaymentRequest payment = new PaymentRequest(totalAmount, "COD");
        OrderCreateRequest request = new OrderCreateRequest(userId, orderItems, payment);

        // Debug log thông tin gửi đi
        Log.d("ORDER", "UserId: " + request.getUserId());
        Log.d("ORDER", "Items size: " + request.getItems().size());
        for (OrderItem item : request.getItems()) {
            Log.d("ORDER", "→ VariantId: " + item.getProductVariantId() +
                    " | Qty: " + item.getQuantity() +
                    " | Price: " + item.getUnitPrice());
        }

        api.createOrder(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CheckOutConfirmActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        Log.e("ORDER", "Lỗi đặt hàng: " + response.code());
                        Log.e("ORDER", "ErrorBody: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("ORDER", "Không đọc được lỗi", e);
                    }
                    Toast.makeText(CheckOutConfirmActivity.this, "Lỗi đặt hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CheckOutConfirmActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ORDER", "Lỗi mạng: " + t.getMessage());
            }
        });
    }
}
