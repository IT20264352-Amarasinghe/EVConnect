package com.evconnect.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONObject;

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

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return true; // invalid token

            byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
            String payload = new String(decodedBytes);
            JSONObject json = new JSONObject(payload);

            if (!json.has("exp")) return true;

            long exp = json.getLong("exp"); // exp in seconds
            long now = System.currentTimeMillis() / 1000;

            return now >= exp;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
