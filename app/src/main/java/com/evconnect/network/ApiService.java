package com.evconnect.network;

import com.evconnect.models.Booking;
import com.evconnect.models.BookingRequest;
import com.evconnect.models.Charger;
import com.evconnect.models.LoginRequest;
import com.evconnect.models.LoginResponse;
import com.evconnect.models.RegistrationRequest;
import com.evconnect.models.RegistrationResponse;
import com.evconnect.models.Slot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegistrationResponse> register(@Body RegistrationRequest request);

    @GET("chargers")
    Call<List<Charger>> getChargers();

    @POST("bookings")
    Call<Booking> createBooking(@Body BookingRequest request);

    @GET("slots/{chargerId}")
    Call<List<Slot>> getSlots(@Path("chargerId") String chargerId,
                              @Query("date") String date);

    @GET("bookings")
    Call<List<Booking>> getBookings(@Query("customerNic") String customerNic);
}