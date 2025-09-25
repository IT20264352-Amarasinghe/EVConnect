package com.evconnect.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONObject;

/**
 * TokenManager is a utility class that acts as the central point for managing
 * application-wide persistent data, primarily the JWT authentication token,
 * using Android's SharedPreferences. It also handles basic token expiration checking.
 */
public class TokenManager {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_IS_OFFLINE = "IS_OFFLINE";
    private static final String KEY_CURRENT_USER_NIC = "current_user_nic";

    private final SharedPreferences sharedPreferences;

    // Constructor
    public TokenManager(Context context) {
        // Retrieve the SharedPreferences instance named 'MyAppPrefs' in private mode.
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

            // The payload is the second part (index 1) and is Base64 encoded.
            // Decode the payload part using URL-safe and no-padding/no-wrap flags.
            byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
            String payload = new String(decodedBytes);
            JSONObject json = new JSONObject(payload);

            if (!json.has("exp")) return true;

            long exp = json.getLong("exp"); // exp in seconds
            long now = System.currentTimeMillis() / 1000;

            // Compare current time with expiration time.
            boolean isExpired = now >= exp;
            // If the token is expired, remove it from storage immediately
            if(isExpired){
                clearToken();
            }
            return isExpired;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    // Offline flag methods
    public void setOffline(boolean isOffline) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_OFFLINE, isOffline);
        editor.apply();
    }

    public boolean isOffline() {
        return sharedPreferences.getBoolean(KEY_IS_OFFLINE, false);
    }

    // Save current user's NIC
    public void setCurrentUserNic(String nic) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CURRENT_USER_NIC, nic);
        editor.apply();
    }

    // Get current user's NIC
    public String getCurrentUserNic() {
        return sharedPreferences.getString(KEY_CURRENT_USER_NIC, null);
    }
}
