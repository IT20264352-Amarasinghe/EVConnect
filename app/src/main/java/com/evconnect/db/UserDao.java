package com.evconnect.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
    private DBHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean registerUser(String nic, String name, String email, String phone, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_NIC, nic);
        values.put(DBHelper.COL_NAME, name);
        values.put(DBHelper.COL_EMAIL, email);
        values.put(DBHelper.COL_PHONE, phone);
        values.put(DBHelper.COL_PASSWORD, password);
        values.put(DBHelper.COL_STATUS, "ACTIVE");

        long result = db.insert(DBHelper.TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean loginUser(String nic, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLE_USER,
                new String[]{DBHelper.COL_NIC},
                DBHelper.COL_NIC + "=? AND " + DBHelper.COL_PASSWORD + "=? AND " + DBHelper.COL_STATUS + "=?",
                new String[]{nic, password, "ACTIVE"},
                null, null, null);

        boolean loggedIn = cursor.moveToFirst();
        cursor.close();
        db.close();
        return loggedIn;
    }
}