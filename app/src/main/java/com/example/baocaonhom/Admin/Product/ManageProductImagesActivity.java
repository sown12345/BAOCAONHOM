package com.example.baocaonhom.Admin.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.SetThumbnailRequest;
import com.example.baocaonhom.models.ProductImage;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProductImagesActivity extends AppCompatActivity {

    private RecyclerView rvImages;
    private Button btnAddImage;
    private List<ProductImage> imageList;
    private ProductImageAdapter adapter;
    private Context context = this;
    private int productId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product_images);

        rvImages = findViewById(R.id.rvImages);
        btnAddImage = findViewById(R.id.btnAddImage);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        productId = getIntent().getIntExtra("productId", -1);
        if (productId == -1) {
            Toast.makeText(this, "Không có ProductId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        imageList = new ArrayList<>();
        adapter = new ProductImageAdapter(context, imageList, new ProductImageAdapter.OnImageActionListener() {
            @Override
            public void onDelete(ProductImage image) {
                deleteImage(image);
            }

            @Override
            public void onSetThumbnail(ProductImage image) {
                setThumbnail(image);
            }
        });

        rvImages.setLayoutManager(new LinearLayoutManager(this));
        rvImages.setAdapter(adapter);

        btnAddImage.setOnClickListener(v -> showAddImageDialog());

        loadImages();
    }

    private void showAddImageDialog() {
        EditText input = new EditText(this);
        input.setHint("Dán link ảnh (https://...)");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        new AlertDialog.Builder(this)
                .setTitle("Thêm ảnh sản phẩm")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String url = input.getText().toString().trim();
                    if (!url.startsWith("http")) {
                        Toast.makeText(context, "Link ảnh không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    insertImage(url, false);
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void loadImages() {
        apiService.getImagesByProduct(productId).enqueue(new Callback<List<ProductImage>>() {
            @Override
            public void onResponse(Call<List<ProductImage>> call, Response<List<ProductImage>> response) {
                if (response.isSuccessful()) {
                    imageList.clear();
                    imageList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Lỗi tải ảnh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductImage>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void insertImage(String imagePath, boolean isThumbnail) {
        ProductImage image = new ProductImage(0, productId, imagePath, isThumbnail);
        apiService.addProductImage(image).enqueue(new Callback<ProductImage>() {
            @Override
            public void onResponse(Call<ProductImage> call, Response<ProductImage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã thêm ảnh", Toast.LENGTH_SHORT).show();
                    loadImages();
                } else {
                    Toast.makeText(context, "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductImage> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi thêm ảnh", Toast.LENGTH_SHORT).show();
                Log.e("ADD_IMAGE_FAIL", t.getMessage());
            }
        });
    }

    private void deleteImage(ProductImage image) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá ảnh")
                .setMessage("Bạn chắc chắn muốn xoá ảnh này?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    apiService.deleteProductImage(image.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Đã xoá ảnh", Toast.LENGTH_SHORT).show();
                                loadImages();
                            } else {
                                Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Lỗi xoá ảnh", Toast.LENGTH_SHORT).show();
                            Log.e("DELETE_IMAGE_FAIL", t.getMessage());
                        }
                    });
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void setThumbnail(ProductImage image) {
        SetThumbnailRequest request = new SetThumbnailRequest(image.getProductId(), image.getId());
        apiService.setThumbnail(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã đặt làm ảnh đại diện", Toast.LENGTH_SHORT).show();
                    loadImages();
                } else {
                    Toast.makeText(context, "Lỗi đặt đại diện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi mạng khi đặt thumbnail", Toast.LENGTH_SHORT).show();
                Log.e("THUMBNAIL_FAIL", t.getMessage());
            }
        });
    }
}
