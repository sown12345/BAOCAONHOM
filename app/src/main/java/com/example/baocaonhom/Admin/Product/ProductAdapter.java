package com.example.baocaonhom.Admin.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baocaonhom.R;
import com.example.baocaonhom.models.Product;
import android.util.Log;


import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface ProductAdapterListener {
        void onEdit(Product product);
        void onDelete(int productId);
        void onManageImages(Product product);
        void onManageVariants(Product product);
    }

    private Context context;
    private List<Product> productList;
    private ProductAdapterListener listener;

    public ProductAdapter(Context context, List<Product> productList, ProductAdapterListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public void setProductList(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.tvName.setText(p.getName());
        holder.tvCategory.setText(p.getCategory() != null ? p.getCategory().getName() : "");

        if (p.getVariants() != null && !p.getVariants().isEmpty()) {
            holder.tvPrice.setText("Giá: " + p.getVariants().get(0).getPrice() + "đ");
        } else {
            holder.tvPrice.setText("Giá: N/A");
        }

        if (p.getImages() != null) {
            p.getImages().stream()
                    .filter(img -> img.isThumbnail())
                    .findFirst()
                    .ifPresent(img -> {
                        Log.d("THUMBNAIL", "Loading image: " + img.getImageUrl());
                        Glide.with(context).load(img.getImageUrl()).into(holder.ivThumbnail);
                    });
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(p));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(p.getId()));
        holder.btnImages.setOnClickListener(v -> listener.onManageImages(p));
        holder.btnVariants.setOnClickListener(v -> listener.onManageVariants(p));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvName, tvPrice, tvCategory;
        ImageButton btnEdit, btnDelete, btnImages, btnVariants;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnImages = itemView.findViewById(R.id.btnImages);
            btnVariants = itemView.findViewById(R.id.btnVariants);
        }
    }
}
