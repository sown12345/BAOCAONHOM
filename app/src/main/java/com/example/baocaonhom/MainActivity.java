package com.example.baocaonhom;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baocaonhom.database.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.txtResult);
        fetchProductNames();
    }

    private void fetchProductNames() {
        Connection conn = DBHelper.connect();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Products");
                StringBuilder sb = new StringBuilder();
                boolean hasData = false;

                while (rs.next()) {
                    hasData = true;
                    String name = rs.getString("ProductName");
                    sb.append("- ").append(name).append("\n");
                }

                if (hasData) {
                    txtResult.setText(sb.toString());
                } else {
                    txtResult.setText("Kết nối cơ sở dữ liệu thành công");
                };
            } catch (Exception ex) {
                Log.e("SQL_ERROR", ex.getMessage());
                txtResult.setText("Lỗi truy vấn: " + ex.getMessage());
            }
        } else {
            txtResult.setText("Không thể kết nối đến CSDL.");
            Log.e("SQL", "Không thể kết nối");
        }
    }
}
