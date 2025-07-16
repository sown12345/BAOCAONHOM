package com.example.baocaonhom.database;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBHelper {
    public static Connection connect() {
        Connection conn = null;

        String ip = "10.0.2.2";
        String port = "1433";
        String db = "FashionShop";
        String user = "sa";
        String pass = "1234";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + db;
            conn = DriverManager.getConnection(connUrl, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static int getMinPriceByProductId(int productId) {
        int price = 0;
        try (Connection conn = connect()) {
            String sql = "SELECT MIN(Price) AS MinPrice FROM ProductVariants WHERE ProductId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                price = rs.getInt("MinPrice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

}
