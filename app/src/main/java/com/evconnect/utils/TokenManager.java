package com.evconnect.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "jwt_token";

    private final SharedPreferences sharedPreferences;

    // Constructor
    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Save token
    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // Retrieve token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Remove token (e.g., on logout)
    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
