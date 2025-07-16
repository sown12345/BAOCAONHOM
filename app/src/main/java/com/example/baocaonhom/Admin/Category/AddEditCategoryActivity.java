package com.example.baocaonhom.Admin.Category;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baocaonhom.R;
import com.example.baocaonhom.models.Category;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditCategoryActivity extends AppCompatActivity {

    private EditText edtCategoryName;
    private Button btnSave;
    private ApiService apiService;
    private int categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnSave = findViewById(R.id.btnSaveCategory);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Lấy dữ liệu nếu là sửa
        if (getIntent() != null && getIntent().hasExtra("id")) {
            categoryId = getIntent().getIntExtra("id", -1);
            String name = getIntent().getStringExtra("name");
            edtCategoryName.setText(name);
        }

        btnSave.setOnClickListener(v -> {
            String name = edtCategoryName.getText().toString().trim();
            Log.d("CATEGORY_DEBUG", "Tên danh mục gửi: " + name);
            if (name.isEmpty()) {
                edtCategoryName.setError("Nhập tên danh mục");
                return;
            }

            if (categoryId == -1) {
                // Thêm mới
                Category newCategory = new Category(0, name);
                apiService.createCategory(newCategory).enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                        Toast.makeText(AddEditCategoryActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                        Log.e("CATEGORY_CREATE", "Lỗi thêm: " + t.getMessage());
                        Toast.makeText(AddEditCategoryActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                Category updated = new Category(categoryId, name);
                apiService.updateCategory(categoryId, updated).enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                        Toast.makeText(AddEditCategoryActivity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                        Log.e("CATEGORY_UPDATE", "Lỗi cập nhật: " + t.getMessage());
                        Toast.makeText(AddEditCategoryActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}
