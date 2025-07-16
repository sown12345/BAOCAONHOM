package com.example.baocaonhom.Admin.Product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baocaonhom.R;
import com.example.baocaonhom.dto.ProductUpdateDTO;
import com.example.baocaonhom.models.Category;
import com.example.baocaonhom.models.Product;
import com.example.baocaonhom.dto.ProductCreateDTO;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditProductActivity extends AppCompatActivity {

    private EditText edtName, edtDesc;
    private Spinner spCategory;
    private Button btnSave;
    private List<Category> categories = new ArrayList<>();
    private Category selectedCategory;
    private int productId = -1;
    private String mode = "add";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        edtName = findViewById(R.id.edtProductName);
        edtDesc = findViewById(R.id.edtProductDesc);
        spCategory = findViewById(R.id.spCategory);
        btnSave = findViewById(R.id.btnSave);

        if (getIntent() != null) {
            mode = getIntent().getStringExtra("mode");
            if ("edit".equals(mode)) {
                productId = getIntent().getIntExtra("productId", -1);
            }
        }

        loadCategories();

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();

            if (name.isEmpty() || selectedCategory == null) {
                Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api = RetrofitClient.getInstance().create(ApiService.class);

            if ("add".equals(mode)) {
                ProductCreateDTO newProduct = new ProductCreateDTO(name, desc, selectedCategory.getId());
                api.createProduct(newProduct).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Log.e("API_CREATE_ERROR", error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(context, "Không thêm được (Lỗi server)", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(context, "Lỗi khi thêm sản phẩm: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("API_CREATE_FAIL", t.getMessage(), t);
                    }
                });

            } else {
                ProductUpdateDTO updated = new ProductUpdateDTO(name, desc, selectedCategory.getId());

                ApiService apiUpdate = RetrofitClient.getInstance().create(ApiService.class);
                apiUpdate.updateProduct(productId, updated).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Log.e("API_UPDATE_ERROR", error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(context, "Không cập nhật được (Lỗi server)", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(context, "Lỗi khi cập nhật sản phẩm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("API_UPDATE_FAIL", t.getMessage(), t);
                    }
                });
            }
        });
    }

    private void loadCategories() {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categories = response.body();
                    List<String> names = new ArrayList<>();
                    for (Category c : categories) names.add(c.getName());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategory.setAdapter(adapter);

                    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategory = categories.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                    if ("edit".equals(mode)) {
                        loadProductById(productId);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductById(int id) {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getProductById(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product p = response.body();
                    edtName.setText(p.getName());
                    edtDesc.setText(p.getDescription());

                    spCategory.post(() -> {
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getId() == p.getCategoryId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
