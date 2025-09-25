package com.evconnect.network;

import android.content.Context;

import com.evconnect.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//ApiClient is a Singleton class responsible for creating and providing a configured
//Retrofit instance for making network requests.
public class ApiClient {
    // A static variable to hold the single instance of Retrofit.
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        // Check if the Retrofit instance has not been initialized yet.
        if (retrofit == null) {
            // 1. Configure the OkHttpClient for advanced networking needs.
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();
            // 2. Build the Retrofit instance.
            retrofit = new Retrofit.Builder()
                    // Set the base URL for the backend API endpoints.
                    .baseUrl(Constants.BASE_URL) // backend base URL
                    // Set the custom configured OkHttpClient.
                    .client(client)
                    // Add a converter factory (Gson) to handle the serialization
                    // and deserialization of Java objects to/from JSON.
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        // Return the existing (or newly created) Retrofit instance.
        return retrofit;
    }
}
