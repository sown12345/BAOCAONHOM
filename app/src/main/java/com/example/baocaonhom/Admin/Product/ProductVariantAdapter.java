package com.example.baocaonhom.Admin.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.R;
import com.example.baocaonhom.models.ProductVariant;

import java.util.List;

public class ProductVariantAdapter extends RecyclerView.Adapter<ProductVariantAdapter.VariantViewHolder> {

    public interface OnVariantActionListener {
        void onEdit(ProductVariant variant);
        void onDelete(ProductVariant variant);
    }

    private final Context context;
    private final List<ProductVariant> variantList;
    private final OnVariantActionListener listener;

    public ProductVariantAdapter(Context context, List<ProductVariant> variantList, OnVariantActionListener listener) {
        this.context = context;
        this.variantList = variantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_variant, parent, false);
        return new VariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariantViewHolder holder, int position) {
        ProductVariant variant = variantList.get(position);

        holder.tvSize.setText("Size: " + (variant.getSize() != null ? variant.getSize() : "N/A"));
        holder.tvColor.setText("Màu: " + (variant.getColor() != null ? variant.getColor() : "N/A"));
        holder.tvPrice.setText("Giá: " + variant.getPrice() + " đ");
        holder.tvQuantity.setText("SL: " + variant.getQuantity());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(variant);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(variant);
        });
    }

    @Override
    public int getItemCount() {
        return variantList.size();
    }

    public static class VariantViewHolder extends RecyclerView.ViewHolder {
        TextView tvSize, tvColor, tvPrice, tvQuantity;
        ImageButton btnEdit, btnDelete;

        public VariantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
