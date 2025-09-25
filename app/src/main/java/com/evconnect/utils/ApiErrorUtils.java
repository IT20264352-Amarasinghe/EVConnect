package com.evconnect.utils;

import org.json.JSONObject;
import retrofit2.Response;

/**
 * ApiErrorUtils is a utility class designed to handle and parse error responses
 * from Retrofit API calls. It extracts a meaningful error message from the
 * server's response body
 */
public class ApiErrorUtils {

    public static String getErrorMessage(Response<?> response) {
        try {
            // Check if the response object actually contains an error body
            if (response.errorBody() != null) {
                // Read the entire error body as a string.
                String errorBody = response.errorBody().string();
                // Attempt to parse the error body string into a JSON object.
                JSONObject jsonObject = new JSONObject(errorBody);
                // Extract the value of the "message" field from the JSON object.
                return jsonObject.optString("message", "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unexpected error occurred";
    }
}
