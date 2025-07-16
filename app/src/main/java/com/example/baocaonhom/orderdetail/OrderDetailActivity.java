package com.example.baocaonhom.orderdetail;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.OrderHistoryResponse;
import com.example.baocaonhom.dto.OrderItemHistory;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderDate, tvStatus, tvTotal;
    private Button btnCancelOrder;
    private RecyclerView rvItems;
    private OrderItemAdapter itemAdapter;
    private OrderHistoryResponse order;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvStatus = findViewById(R.id.tvStatus);
        tvTotal = findViewById(R.id.tvTotal);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        rvItems = findViewById(R.id.rvOrderItems);

        apiService = RetrofitClient.getInstance().create(ApiService.class);

        String orderJson = getIntent().getStringExtra("order");
        order = new Gson().fromJson(orderJson, OrderHistoryResponse.class);

        setupUI();
    }

    private void setupUI() {
        tvOrderId.setText("Mã đơn: #" + order.getOrderId());

        try {
            SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat display = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = iso.parse(order.getOrderDate());
            tvOrderDate.setText("Ngày đặt: " + display.format(date));
        } catch (Exception e) {
            tvOrderDate.setText("Ngày đặt: " + order.getOrderDate());
        }

        tvStatus.setText("Trạng thái: " + order.getStatus());
        tvTotal.setText(String.format("Tổng tiền: %,.0f đ", order.getTotalAmount()));

        if (order.getStatus().equalsIgnoreCase("Chờ xử lý")) {
            btnCancelOrder.setVisibility(View.VISIBLE);
            btnCancelOrder.setOnClickListener(v -> cancelOrder());
        } else {
            btnCancelOrder.setVisibility(View.GONE);
        }

        itemAdapter = new OrderItemAdapter(order.getItems());
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(itemAdapter);
    }

    private void cancelOrder() {
        apiService.cancelOrder(order.getOrderId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OrderDetailActivity.this, "Đã huỷ đơn hàng", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Huỷ đơn thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

