package com.evconnect.network;

import android.content.Context;

import com.evconnect.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        this.tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        String token = tokenManager.getToken();

        if (token != null && !tokenManager.isTokenExpired(token)) {
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        // If no token, just continue without header
        return chain.proceed(originalRequest);
    }
}
