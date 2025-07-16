package com.example.baocaonhom.Admin.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baocaonhom.R;
import com.example.baocaonhom.models.Category;
import com.example.baocaonhom.models.Product;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProductsActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private Spinner spFilterCategory;
    private EditText edtSearch;
    private ProductAdapter adapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        rvProducts = findViewById(R.id.rvProducts);
        spFilterCategory = findViewById(R.id.spFilterCategory);
        edtSearch = findViewById(R.id.edtSearch);

        adapter = new ProductAdapter(context, allProducts, new ProductAdapter.ProductAdapterListener() {
            @Override
            public void onEdit(Product product) {
                Intent intent = new Intent(context, AddEditProductActivity.class);
                intent.putExtra("mode", "edit");
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(int productId) {
                new AlertDialog.Builder(context)
                        .setTitle("Xoá sản phẩm")
                        .setMessage("Bạn có chắc muốn xoá sản phẩm này không?")
                        .setPositiveButton("Xoá", (dialog, which) -> {
                            ApiService api = RetrofitClient.getInstance().create(ApiService.class);
                            api.deleteProduct(productId).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "Đã xoá sản phẩm", Toast.LENGTH_SHORT).show();
                                        loadProducts();
                                    } else {
                                        Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(context, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            }


            @Override
            public void onManageImages(Product product) {
                if (product == null || product.getId() <= 0) {
                    Toast.makeText(context, "Sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(ManageProductsActivity.this, ManageProductImagesActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }


            @Override
            public void onManageVariants(Product product) {
                Intent intent = new Intent(context, ManageProductVariantsActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }
        });

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        loadCategories();
        loadProducts();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spFilterCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        findViewById(R.id.btnAddProduct).setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditProductActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
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
                    names.add("Tất cả");
                    for (Category c : categories) names.add(c.getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spFilterCategory.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(context, "Lỗi tải danh mục: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "loadCategories failed", t);
            }


        });
    }

    private void loadProducts() {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                allProducts = response.body();

                if (allProducts != null && !allProducts.isEmpty()) {
                    Log.d("API_TEST", "Sản phẩm: " + new com.google.gson.Gson().toJson(allProducts.get(0)));
                }

                Log.d("TEST_LOAD", "Đã nhận " + allProducts.size() + " sản phẩm");

                filterProducts();

            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi tải sản phẩm: " + t.getMessage(), t);
            }

        });
    }

    private void filterProducts() {
        String keyword = edtSearch.getText().toString().toLowerCase();
        int categoryPos = spFilterCategory.getSelectedItemPosition();

        List<Product> filtered = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword))
                .filter(p -> categoryPos == 0 || p.getCategory().getName().equals(spFilterCategory.getSelectedItem().toString()))
                .collect(Collectors.toList());

        adapter.setProductList(filtered);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

}