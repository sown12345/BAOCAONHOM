package com.example.baocaonhom.Admin.Product;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baocaonhom.R;
import com.example.baocaonhom.models.ProductImage;

import java.util.List;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder> {

    private final Context context;
    private final List<ProductImage> imageList;
    private final OnImageActionListener listener;

    public interface OnImageActionListener {
        void onDelete(ProductImage image);
        void onSetThumbnail(ProductImage image);
    }

    public ProductImageAdapter(Context context, List<ProductImage> imageList, OnImageActionListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_image_admin, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ProductImage image = imageList.get(position);

        Log.d("GLIDE_IMAGE", "Loading: " + image.getImagePath());

        Glide.with(context)
                .load(image.getImagePath())
                .placeholder(R.drawable.ic_image)
                .error(android.R.drawable.ic_dialog_alert)
                .into(holder.ivImage);

        // Hiển thị ảnh đại diện
        if (image.isThumbnail()) {
            holder.ivImage.setAlpha(1.0f);
            holder.ivImage.setBackgroundResource(R.drawable.bg_thumbnail_highlight);
        } else {
            holder.ivImage.setAlpha(0.6f);
            holder.ivImage.setBackground(null);
        }

        // Xử lý nút Xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(image);
        });

        // Xử lý nút Đặt làm đại diện
        holder.btnSetThumbnail.setOnClickListener(v -> {
            if (listener != null) listener.onSetThumbnail(image);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        Button btnDelete, btnSetThumbnail; // Sửa kiểu từ ImageButton thành Button

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnSetThumbnail = itemView.findViewById(R.id.btnSetThumbnail); // không còn lỗi
        }
    }
}
