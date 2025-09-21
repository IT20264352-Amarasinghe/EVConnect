package com.evconnect.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.evconnect.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingDao {

    private final DBHelper dbHelper;

    public BookingDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Inserts a booking into the local database.
     * Uses CONFLICT_REPLACE to update if a booking with the same ID already exists.
     */
    public boolean insertBooking(Booking booking) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_BOOKING_ID, booking.getId());
        values.put(DBHelper.COL_BOOKING_CUSTOMER_NIC, booking.getCustomerNic());

// Charger
        if (booking.getCharger() != null) {
            values.put(DBHelper.COL_BOOKING_CHARGER_ID, booking.getCharger().getChargerId());
            values.put("chargerCode", booking.getCharger().getCode());
            values.put("chargerLocation", booking.getCharger().getLocation());
        }

// Slot
        if (booking.getSlot() != null) {
            values.put(DBHelper.COL_BOOKING_SLOT_ID, booking.getSlot().getSlotId());
            values.put("slotDate", booking.getSlot().getDate());
            values.put("slotStartTime", booking.getSlot().getStartTime());
            values.put("slotEndTime", booking.getSlot().getEndTime());
            values.put("slotStatus", booking.getSlot().getStatus());
        }

        values.put(DBHelper.COL_BOOKING_CREATED_AT, booking.getCreatedAt());
        values.put(DBHelper.COL_BOOKING_UPDATED_AT, booking.getUpdatedAt());
        values.put(DBHelper.COL_BOOKING_STATUS, booking.getStatus());

        long result = db.insertWithOnConflict(DBHelper.TABLE_BOOKING, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return result != -1;
    }

    /**
     * Retrieves all bookings for a specific customer NIC.
     */
    public List<Booking> getBookingsByCustomerNic(String customerNic) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DBHelper.TABLE_BOOKING,
                null, // All columns
                DBHelper.COL_BOOKING_CUSTOMER_NIC + "=?",
                new String[]{customerNic},
                null, null, DBHelper.COL_BOOKING_CREATED_AT + " DESC" // Order by latest booking first
        );

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();

                // Common fields
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_ID)));
                booking.setCustomerNic(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CUSTOMER_NIC)));
                booking.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CREATED_AT)));
                booking.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_UPDATED_AT)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_STATUS)));

                // Charger fields
                Booking.Charger charger = new Booking.Charger();
                charger.setChargerId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CHARGER_ID)));
                charger.setCode(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CHARGER_CODE)));
                charger.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CHARGER_LOCATION)));
                booking.setCharger(charger);

                // Slot fields
                Booking.Slot slot = new Booking.Slot();
                slot.setSlotId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_SLOT_ID)));
                slot.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_SLOT_DATE)));
                slot.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_SLOT_START_TIME)));
                slot.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_SLOT_END_TIME)));
                slot.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_SLOT_STATUS)));
                booking.setSlot(slot);

                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookings;
    }
}
