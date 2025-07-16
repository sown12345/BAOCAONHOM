package com.example.baocaonhom;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUserSession(int userId, String fullName, String role) {
        editor.putInt("user_id", userId);
        editor.putString("user_name", fullName);
        editor.putString("user_role", role);
        editor.apply();
    }


    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1); // -1 = chưa đăng nhập
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }
}
