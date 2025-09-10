package com.evconnect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ev_connect.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER = "user";
    public static final String COL_NIC = "nic";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_PASSWORD = "password";
    public static final String COL_STATUS = "status";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COL_NIC + " TEXT PRIMARY KEY, " +
                    COL_NAME + " TEXT, " +
                    COL_EMAIL + " TEXT, " +
                    COL_PHONE + " TEXT, " +
                    COL_PASSWORD + " TEXT, " +
                    COL_STATUS + " TEXT CHECK(" + COL_STATUS + " IN ('ACTIVE','DEACTIVATED')) DEFAULT 'ACTIVE')";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}