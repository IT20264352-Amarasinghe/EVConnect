package com.evconnect.network;

import com.evconnect.models.LoginRequest;
import com.evconnect.models.LoginResponse;
import com.evconnect.models.RegistrationRequest;
import com.evconnect.models.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("users/register")
    Call<RegistrationResponse> register(@Body RegistrationRequest request);
}