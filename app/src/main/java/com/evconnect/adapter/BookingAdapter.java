package com.evconnect.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evconnect.R;
import com.evconnect.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    // A list to hold the booking data that will be displayed in the RecyclerView.
    private List<Booking> bookings;

    // Constructor to initialize the adapter with a list of bookings.
    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // This is where you inflate the layout for each item and return a new ViewHolder.
    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    //This method updates the contents of the ViewHolder to reflect the item at the given position.
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.tvChargerCode.setText("Charger: " + booking.getCharger().getCode());
        holder.tvLocation.setText("Location: " + booking.getCharger().getLocation());
        holder.tvDate.setText("Date: " + booking.getSlot().getDate());
        holder.tvTime.setText("Time: " + booking.getSlot().getStartTime() + " - " + booking.getSlot().getEndTime());
        holder.tvStatus.setText("Status: " + booking.getSlot().getStatus());
        holder.tvCreatedAt.setText("Created At: " + booking.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    //This method updates the contents of the ViewHolder to reflect the item at the given position.
    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvChargerCode, tvLocation, tvDate, tvTime, tvStatus, tvCreatedAt;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChargerCode = itemView.findViewById(R.id.tv_charger_code);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvCreatedAt = itemView.findViewById(R.id.tv_created_at);
        }
    }
}

