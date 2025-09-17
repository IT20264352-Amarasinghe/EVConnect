package com.evconnect.utils;

import org.json.JSONObject;
import retrofit2.Response;

public class ApiErrorUtils {

    public static String getErrorMessage(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorBody);
                return jsonObject.optString("message", "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unexpected error occurred";
    }
}
