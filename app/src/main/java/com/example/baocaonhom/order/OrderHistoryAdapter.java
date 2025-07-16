package com.example.baocaonhom.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.Customer.orderdetail.OrderDetailActivity;
import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.OrderHistoryResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<OrderHistoryResponse> orderList;

    public OrderHistoryAdapter(List<OrderHistoryResponse> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderHistoryResponse order = orderList.get(position);

        holder.tvOrderId.setText("Mã đơn: #" + order.getOrderId());

        // Parse ngày đặt
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        String formattedDate;
        try {
            Date parsedDate = isoFormat.parse(order.getOrderDate());
            formattedDate = displayFormat.format(parsedDate);
        } catch (ParseException e) {
            formattedDate = order.getOrderDate(); // fallback nếu lỗi định dạng
        }
        holder.tvOrderDate.setText("Ngày đặt: " + formattedDate);

        holder.tvStatus.setText("Trạng thái: " + order.getStatus());
        holder.tvTotal.setText(String.format("Tổng tiền: %,.0f đ", order.getTotalAmount()));
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, OrderDetailActivity.class);

            // Convert đơn hàng thành JSON và gửi qua Intent
            String orderJson = new Gson().toJson(order);
            intent.putExtra("order", orderJson);

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvStatus, tvTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
