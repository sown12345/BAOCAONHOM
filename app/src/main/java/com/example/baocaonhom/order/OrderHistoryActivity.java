package com.example.baocaonhom.order;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.OrderHistoryResponse;
import com.example.baocaonhom.models.Order;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private ApiService apiService;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.rvOrderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getInstance().create(ApiService.class);
        userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getInt("userId", -1);

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        apiService.getOrdersByUser(userId).enqueue(new Callback<List<OrderHistoryResponse>>() {
            @Override
            public void onResponse(Call<List<OrderHistoryResponse>> call, Response<List<OrderHistoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderHistoryResponse> orders = response.body();
                    adapter = new OrderHistoryAdapter(orders);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Lỗi khi tải lịch sử", Toast.LENGTH_SHORT).show();
                    Log.e("ORDER_HISTORY", "Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<OrderHistoryResponse>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("ORDER_HISTORY", "onFailure: " + t.getMessage());
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadOrderHistory();
    }
}
