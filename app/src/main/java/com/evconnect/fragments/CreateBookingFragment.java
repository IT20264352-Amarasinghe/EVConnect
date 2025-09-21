package com.evconnect.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evconnect.R;
import com.evconnect.models.BookingRequest;
import com.evconnect.models.BookingResponse;
import com.evconnect.models.Charger;
import com.evconnect.models.Slot;
import com.evconnect.models.UserInfo;
import com.evconnect.network.ApiClient;
import com.evconnect.network.ApiService;
import com.evconnect.utils.ApiErrorUtils;
import com.evconnect.utils.JwtUtils;
import com.evconnect.utils.TokenManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBookingFragment extends Fragment {

    private Spinner spinnerChargers, spinnerSlots;
    private Button btnCreateBooking;
    private ImageButton btnPickDate;
    private TextView offlineWarning;
    private TextView tvChargerCode, tvChargerName, tvChargerLocation, tvSelectedDate;
    private List<Charger> chargerList = new ArrayList<>();
    private List<Slot> slotList = new ArrayList<>();
    private Charger selectedCharger;
    private Slot selectedSlot;
    private String selectedDate; // yyyy-MM-dd
    private TokenManager tokenManager ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_booking, container, false);

        spinnerChargers = view.findViewById(R.id.spinnerChargers);
        spinnerSlots = view.findViewById(R.id.spinnerSlots);
        btnCreateBooking = view.findViewById(R.id.btnCreateBooking);
        btnPickDate = view.findViewById(R.id.btnPickDate);

        tvChargerCode = view.findViewById(R.id.tvChargerCode);
        tvChargerName = view.findViewById(R.id.tvChargerName);
        tvChargerLocation = view.findViewById(R.id.tvChargerLocation);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);

        tokenManager = new TokenManager(getContext());
        String token = tokenManager.getToken();
        offlineWarning = view.findViewById(R.id.offlineWarning);
        if (tokenManager.isOffline() ){
            offlineWarning.setVisibility(View.VISIBLE);
            // Disable booking actions
            return view;
        }else{
            offlineWarning.setVisibility(View.GONE);
        }

        UserInfo user = JwtUtils.extractUserInfo(token);;

        loadChargers();

        spinnerChargers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Placeholder selected → clear card
                    tvChargerCode.setText("Code: N/A");
                    tvChargerName.setText("Name: N/A");
                    tvChargerLocation.setText("Location: N/A");
                    selectedCharger = null;
                    return;
                }
                // Adjust position by -1 because of the placeholder
                selectedCharger = chargerList.get(position - 1);
                // Update Card
                tvChargerCode.setText("Code: " + selectedCharger.getCode());
                tvChargerName.setText("Name: " + selectedCharger.getCode() +"-"+ selectedCharger.getLocation() );
                tvChargerLocation.setText("Location: " + selectedCharger.getLocation());

                if (selectedDate != null) {
                    loadSlots(selectedCharger.getId(), selectedDate);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnPickDate.setOnClickListener(v -> showDatePicker());

        btnCreateBooking.setOnClickListener(v -> {
            int slotIndex = spinnerSlots.getSelectedItemPosition();
            if (slotIndex >= 0 && slotIndex < slotList.size()) {
                selectedSlot = slotList.get(slotIndex);
                createBooking(user.getNic(), selectedCharger.getId(), selectedSlot.getId());
            }
        });

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    tvSelectedDate.setText(selectedDate);
                    if (selectedCharger != null) {
                        loadSlots(selectedCharger.getId(), selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void loadChargers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getChargers().enqueue(new Callback<List<Charger>>() {
            @Override
            public void onResponse(Call<List<Charger>> call, Response<List<Charger>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chargerList = response.body();

                    List<String> chargerNames = new ArrayList<>();
                    // Add a placeholder at the start
                    chargerNames.add("Select Charger");
                    for (Charger c : chargerList) {
                        chargerNames.add(c.getCode() + " - " + c.getLocation());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, chargerNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerChargers.setAdapter(adapter);
                }else{
                    String errorMessage = ApiErrorUtils.getErrorMessage(response);
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Charger>> call, Throwable t) {
                offlineWarning.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Charger Loading Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSlots(String chargerId, String date) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getSlots(chargerId, date).enqueue(new Callback<List<Slot>>() {
            @Override
            public void onResponse(Call<List<Slot>> call, Response<List<Slot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    slotList = response.body();

                    List<String> slotStrings = new ArrayList<>();
                    for (Slot s : slotList) {
                        slotStrings.add(s.getStartTime() + " - " + s.getEndTime() +
                                ("Available".equals(s.getStatus()) ? "" : " (Unavailable)"));
                    }

                    ArrayAdapter<String> slotAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, slotStrings) {
                        @Override
                        public boolean isEnabled(int position) {
                            return "Available".equals(slotList.get(position).getStatus());
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if ("Available".equals(slotList.get(position).getStatus())) {
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.GRAY);
                            }
                            return view;
                        }
                    };

                    slotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSlots.setAdapter(slotAdapter);
                }else{
                    String errorMessage = ApiErrorUtils.getErrorMessage(response);
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Slot>> call, Throwable t) {
                offlineWarning.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "LoadSlots Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createBooking(String customerNic, String chargerCode, String slotId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        BookingRequest bookingRequest = new BookingRequest(customerNic, chargerCode, slotId);

        apiService.createBooking(bookingRequest).enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Booking Created!", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = ApiErrorUtils.getErrorMessage(response);
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

