package com.example.baocaonhom.Admin.Category;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.R;
import com.example.baocaonhom.models.Category;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCategoriesActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private EditText edtSearch;
    private Button btnAddCategory;
    private CategoryAdapter adapter;
    private List<Category> originalList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        rvCategories = findViewById(R.id.rvCategories);
        edtSearch = findViewById(R.id.edtSearch);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        adapter = new CategoryAdapter(this, originalList, new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onEdit(Category category) {
                Intent intent = new Intent(ManageCategoriesActivity.this, AddEditCategoryActivity.class);
                intent.putExtra("id", category.getId());
                intent.putExtra("name", category.getName());
                startActivity(intent);
            }


            @Override
            public void onDelete(Category category) {
                new AlertDialog.Builder(ManageCategoriesActivity.this)
                        .setTitle("Xoá danh mục")
                        .setMessage("Bạn có chắc muốn xoá \"" + category.getName() + "\"?")
                        .setPositiveButton("Xoá", (dialog, which) -> deleteCategory(category.getId()))
                        .setNegativeButton("Huỷ", null)
                        .show();
            }
        });

        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(ManageCategoriesActivity.this, AddEditCategoryActivity.class);
            startActivity(intent);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    private void loadCategories() {
        apiService.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalList = response.body();

                    // Log kiểm tra dữ liệu có đổ về không
                    Log.d("CATEGORY_DEBUG", "Số danh mục: " + originalList.size());
                    for (Category c : originalList) {
                        Log.d("CATEGORY_DEBUG", c.getId() + " - " + c.getName());
                    }

                    adapter.updateList(originalList);
                } else {
                    Log.e("CATEGORY_DEBUG", "Response không thành công hoặc body null");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(ManageCategoriesActivity.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
                Log.e("CATEGORY_DEBUG", "Lỗi kết nối API: " + t.getMessage());
            }
        });
    }


    private void deleteCategory(int id) {
        apiService.deleteCategory(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ManageCategoriesActivity.this, "Đã xoá", Toast.LENGTH_SHORT).show();
                loadCategories();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageCategoriesActivity.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList(String keyword) {
        List<Category> filtered = new ArrayList<>();
        for (Category c : originalList) {
            if (c.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(c);
            }
        }
        adapter.updateList(filtered);
    }
}
