package com.evconnect.network;

import com.evconnect.models.LoginRequest;
import com.evconnect.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}