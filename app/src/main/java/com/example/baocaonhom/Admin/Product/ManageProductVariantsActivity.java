package com.example.baocaonhom.Admin.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.ProductVariantCreateDTO;
import com.example.baocaonhom.models.ProductVariant;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProductVariantsActivity extends AppCompatActivity {

    private RecyclerView rvVariants;
    private Button btnAddVariant;
    private List<ProductVariant> variantList;
    private ProductVariantAdapter adapter;
    private Context context = this;
    private int productId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product_variants);

        rvVariants = findViewById(R.id.rvVariants);
        btnAddVariant = findViewById(R.id.btnAddVariant);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        productId = getIntent().getIntExtra("productId", -1);
        if (productId == -1) {
            Toast.makeText(this, "Không có ProductId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        variantList = new ArrayList<>();
        adapter = new ProductVariantAdapter(context, variantList, new ProductVariantAdapter.OnVariantActionListener() {
            @Override
            public void onEdit(ProductVariant variant) {
                showEditVariantDialog(variant);
            }

            @Override
            public void onDelete(ProductVariant variant) {
                deleteVariant(variant);
            }
        });

        rvVariants.setLayoutManager(new LinearLayoutManager(this));
        rvVariants.setAdapter(adapter);

        btnAddVariant.setOnClickListener(v -> showAddVariantDialog());

        loadVariants();
    }

    private void loadVariants() {
        apiService.getVariantsByProduct(productId).enqueue(new Callback<List<ProductVariant>>() {
            @Override
            public void onResponse(Call<List<ProductVariant>> call, Response<List<ProductVariant>> response) {
                if (response.isSuccessful()) {
                    variantList.clear();
                    variantList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Không tải được biến thể", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductVariant>> call, Throwable t) {
                Toast.makeText(context, "Lỗi API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddVariantDialog() {
        showVariantInputDialog(null);
    }

    private void showEditVariantDialog(ProductVariant variant) {
        showVariantInputDialog(variant);
    }

    private void showVariantInputDialog(ProductVariant variant) {
        boolean isEdit = (variant != null);

        EditText edtColor = new EditText(this);
        edtColor.setHint("Màu sắc (VD: Đen, Trắng)");
        edtColor.setInputType(InputType.TYPE_CLASS_TEXT);
        edtColor.setTextColor(Color.BLACK);
        if (isEdit) edtColor.setText(variant.getColor());

        EditText edtSize = new EditText(this);
        edtSize.setHint("Size (VD: M, L, XL)");
        edtSize.setInputType(InputType.TYPE_CLASS_TEXT);
        edtSize.setTextColor(Color.BLACK);
        if (isEdit) edtSize.setText(variant.getSize());

        EditText edtPrice = new EditText(this);
        edtPrice.setHint("Giá");
        edtPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtPrice.setTextColor(Color.BLACK);
        if (isEdit && variant.getPrice() != null) {
            edtPrice.setText(String.valueOf(variant.getPrice()));
        }


        EditText edtQty = new EditText(this);
        edtQty.setHint("Số lượng");
        edtQty.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtQty.setTextColor(Color.BLACK);
        if (isEdit) edtQty.setText(String.valueOf(variant.getQuantity()));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);
        layout.addView(edtColor);
        layout.addView(edtSize);
        layout.addView(edtPrice);
        layout.addView(edtQty);

        new AlertDialog.Builder(this)
                .setTitle(isEdit ? "Sửa biến thể" : "Thêm biến thể")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String color = edtColor.getText().toString().trim();
                    String size = edtSize.getText().toString().trim();
                    String priceStr = edtPrice.getText().toString().trim();
                    String qtyStr = edtQty.getText().toString().trim();

                    if (color.isEmpty() || size.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double price = Double.parseDouble(priceStr);
                        int quantity = Integer.parseInt(qtyStr);

                        if (isEdit) {
                            variant.setColor(color);
                            variant.setSize(size);
                            variant.setPrice(price);
                            variant.setQuantity(quantity);
                            updateVariant(variant);
                        } else {
                            ProductVariantCreateDTO dto = new ProductVariantCreateDTO(productId, color, size, price, quantity);
                            insertVariant(dto);
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Giá và số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void insertVariant(ProductVariantCreateDTO dto) {
        apiService.addProductVariant(dto).enqueue(new Callback<ProductVariant>() {
            @Override
            public void onResponse(Call<ProductVariant> call, Response<ProductVariant> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã thêm biến thể", Toast.LENGTH_SHORT).show();
                    loadVariants();
                } else {
                    Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("INSERT_ERROR", response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductVariant> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi thêm", Toast.LENGTH_SHORT).show();
                Log.e("API_FAIL", t.getMessage());
            }
        });
    }

    private void updateVariant(ProductVariant variant) {
        apiService.updateProductVariant(variant.getId(), variant).enqueue(new Callback<ProductVariant>() {
            @Override
            public void onResponse(Call<ProductVariant> call, Response<ProductVariant> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                    loadVariants();
                } else {
                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductVariant> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteVariant(ProductVariant variant) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá biến thể")
                .setMessage("Bạn chắc chắn muốn xoá?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    apiService.deleteProductVariant(variant.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Đã xoá", Toast.LENGTH_SHORT).show();
                                loadVariants();
                            } else {
                                Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Lỗi khi xoá", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
