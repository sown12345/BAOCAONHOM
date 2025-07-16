package com.example.baocaonhom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baocaonhom.Admin.AdminHomeActivity;
import com.example.baocaonhom.Customer.home.UserHomeActivity;
import com.example.baocaonhom.dto.LoginRequest;
import com.example.baocaonhom.models.User;
import com.example.baocaonhom.network.ApiService;
import com.example.baocaonhom.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView btnRegister;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.tvRegister);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putInt("userId", user.getId())
                            .putString("fullName", user.getFullName())
                            .putString("role", user.getRole())
                            .apply();

                    Toast.makeText(LoginActivity.this, "Chào " + user.getFullName(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this,
                            user.getRole().equalsIgnoreCase("Admin") ?
                                    AdminHomeActivity.class : UserHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
