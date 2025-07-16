package com.example.baocaonhom.Customer.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.CartItemResponse;

import java.text.DecimalFormat;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {

    private Context context;
    private List<CartItemResponse> itemList;
    private OnCartActionListener listener;

    public interface OnCartActionListener {
        void onPlus(CartItemResponse item);
        void onMinus(CartItemResponse item);
        void onDelete(CartItemResponse item);
    }

    public CartItemAdapter(Context context, List<CartItemResponse> itemList, OnCartActionListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemResponse item = itemList.get(position);
        holder.txtName.setText(item.getProductName());
        holder.txtVariant.setText(item.getSize() + " / " + item.getColor());

        DecimalFormat df = new DecimalFormat("###,###");
        holder.txtPrice.setText(df.format(item.getPrice()) + " đ");
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(context).load(item.getThumbnailUrl()).into(holder.imgThumbnail);

        holder.btnPlus.setOnClickListener(v -> listener.onPlus(item));
        holder.btnMinus.setOnClickListener(v -> listener.onMinus(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtName, txtVariant, txtPrice, txtQuantity;
        ImageButton btnDelete, btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtVariant = itemView.findViewById(R.id.txtVariant);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
