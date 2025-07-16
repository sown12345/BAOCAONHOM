package com.example.baocaonhom.orderdetail;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.OrderItemHistory;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder> {

    private List<OrderItemHistory> itemList;

    public OrderItemAdapter(List<OrderItemHistory> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        OrderItemHistory item = itemList.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvColor.setText("Màu: " + item.getColor());
        holder.tvSize.setText("Size: " + item.getSize());
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvPrice.setText(String.format("Đơn giá: %,.0f đ", item.getUnitPrice()));

        Glide.with(holder.itemView.getContext())
                .load(item.getThumbnailUrl())
                .placeholder(R.drawable.no_image)
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvProductName, tvColor, tvSize, tvQuantity, tvPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}

