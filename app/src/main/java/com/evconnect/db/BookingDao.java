package com.evconnect.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.evconnect.models.BookingResponse;

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
    public boolean insertBooking(BookingResponse booking) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_BOOKING_ID, booking.getId());
        values.put(DBHelper.COL_BOOKING_CUSTOMER_NIC, booking.getCustomerNic());
        values.put(DBHelper.COL_BOOKING_CHARGER_ID, booking.getChargerId());
        values.put(DBHelper.COL_BOOKING_SLOT_ID, booking.getSlotId());
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
    public List<BookingResponse> getBookingsByCustomerNic(String customerNic) {
        List<BookingResponse> bookings = new ArrayList<>();
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
                BookingResponse booking = new BookingResponse();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_ID)));
                booking.setCustomerNic(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CUSTOMER_NIC)));
                booking.setChargerId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CHARGER_ID)));
                booking.setSlotId(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_SLOT_ID)));
                booking.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_CREATED_AT)));
                booking.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_UPDATED_AT)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKING_STATUS)));

                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookings;
    }
}
