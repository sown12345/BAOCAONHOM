package com.example.baocaonhom.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baocaonhom.Admin.Category.ManageCategoriesActivity;
import com.example.baocaonhom.Admin.Product.ManageProductsActivity;
import com.example.baocaonhom.LoginActivity;
import com.example.baocaonhom.R;
import com.example.baocaonhom.SessionManager;

public class AdminHomeActivity extends AppCompatActivity {
    Button btnLogout;
    Button btnCategory;
    Button btnProduct;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        session = new SessionManager(this);
        btnLogout = findViewById(R.id.btnLogoutAdmin);
        btnCategory = findViewById(R.id.btnCategoryAdmin);
        btnProduct = findViewById(R.id.btnProductAdmin);

        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnCategory.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageCategoriesActivity.class));
            finish();
        });

        btnProduct.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageProductsActivity.class));
            finish();
        });

    }
}


