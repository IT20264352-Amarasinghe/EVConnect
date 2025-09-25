package com.evconnect.network;

import android.content.Context;

import com.evconnect.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AuthInterceptor is an OkHttpClient Interceptor responsible for adding an
 * Authorization header to outgoing requests if a valid access token is available.
 */
public class AuthInterceptor implements Interceptor {

    // Instance of TokenManager to handle storage and retrieval of the access token.
    private final TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        this.tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Get the original request being made.
        Request originalRequest = chain.request();

        // Retrieve the current access token from the TokenManager.
        String token = tokenManager.getToken();

        // Check if a token exists AND if it hasn't expired.
        if (token != null && !tokenManager.isTokenExpired(token)) {
            // Build a new request based on the original one.
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        // If no token, just continue without header
        return chain.proceed(originalRequest);
    }
}
