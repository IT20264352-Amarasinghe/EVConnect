package com.evconnect.fragments;
import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evconnect.R;
import com.evconnect.adapter.BookingAdapter;
import com.evconnect.db.BookingDao;
import com.evconnect.db.UserDao;
import com.evconnect.models.Booking;
import com.evconnect.models.UserInfo;
import com.evconnect.network.ApiClient;
import com.evconnect.network.ApiService;
import com.evconnect.utils.JwtUtils;
import com.evconnect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<Booking> bookingList = new ArrayList<>();
    private TokenManager tokenManager ;
    private TextView offlineWarning;
    UserInfo user;
    private TextView tvNoBookings;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_bookings, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(adapter);
        offlineWarning = view.findViewById(R.id.offlineWarning);
        tvNoBookings = view.findViewById(R.id.tvNoBookings);
        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();

        if(token != null){
            user = JwtUtils.extractUserInfo(token);
        }else{
            String currentNic = tokenManager.getCurrentUserNic();
            if (currentNic != null) {
                UserDao userDao = new UserDao(requireContext());
                user = userDao.getUserByNic(currentNic);
                if(user == null){
                    Toast.makeText(getContext(), "User not found on offline. Please login using internet.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Please login with internet. User name not found", Toast.LENGTH_SHORT).show();
            }
        }

        fetchBookings();

        return view;
    }

    private void fetchBookings() {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getBookings(user.getNic()).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();
                    if (bookings.isEmpty()) {
                        tvNoBookings.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvNoBookings.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    bookingList.clear();
                    bookingList.addAll(bookings);
                    adapter.notifyDataSetChanged();

                    offlineWarning.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "No bookings found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                // Show offline warning
                offlineWarning.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Failed to fetch from server. Loading offline data...", Toast.LENGTH_SHORT).show();

                // ✅ Load from local DB
                BookingDao bookingDao = new BookingDao(requireContext());
                List<Booking> localBookings = bookingDao.getBookingsByCustomerNic(user.getNic());

                if (!localBookings.isEmpty()) {
                    bookingList.clear();
                    bookingList.addAll(localBookings);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No offline bookings found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
