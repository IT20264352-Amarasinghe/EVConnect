package com.evconnect.network;

import com.evconnect.models.BookingRequest;
import com.evconnect.models.BookingResponse;
import com.evconnect.models.Charger;
import com.evconnect.models.LoginRequest;
import com.evconnect.models.LoginResponse;
import com.evconnect.models.RegistrationRequest;
import com.evconnect.models.RegistrationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("users/register")
    Call<RegistrationResponse> register(@Body RegistrationRequest request);

    @GET("chargers")
    Call<List<Charger>> getChargers();

    @POST("bookings")
    Call<BookingResponse> createBooking(@Body BookingRequest request);
}