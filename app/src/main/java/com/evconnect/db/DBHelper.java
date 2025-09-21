package com.evconnect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is a helper for managing database creation and version management.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Define constants for the database name and version.
    public static final String DATABASE_NAME = "ev_connect.db";
    public static final int DATABASE_VERSION = 2;

    // Define constants for the table and column names to avoid typos.
    public static final String TABLE_USER = "user";
    public static final String COL_NIC = "nic";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_ROLE = "role";
    public static final String COL_PASSWORD = "password";
    public static final String COL_STATUS = "status";

    // Booking Table
    public static final String TABLE_BOOKING = "booking";
    public static final String COL_BOOKING_ID = "id";
    public static final String COL_BOOKING_CUSTOMER_NIC = "customerNic";
    public static final String COL_BOOKING_CHARGER_ID = "chargerId";
    public static final String COL_BOOKING_SLOT_ID = "slotId";
    public static final String COL_BOOKING_CREATED_AT = "createdAt";
    public static final String COL_BOOKING_UPDATED_AT = "updatedAt";
    public static final String COL_BOOKING_STATUS = "status";

    // Define the SQL query to create the user table.
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COL_NIC + " TEXT PRIMARY KEY, " +
                    COL_NAME + " TEXT, " +
                    COL_EMAIL + " TEXT, " +
                    COL_PHONE + " TEXT, " +
                    COL_ROLE + " TEXT, " +
                    COL_PASSWORD + " TEXT, " +
                    // Status column with a check constraint to ensure values are either 'ACTIVE' or 'DEACTIVATED'.
                    // 'ACTIVE' is the default value for new users.
                    COL_STATUS + " TEXT CHECK(" + COL_STATUS + " IN ('ACTIVE','DEACTIVATED')) DEFAULT 'ACTIVE')";

    // Define the SQL query to create the booking table.
    private static final String CREATE_TABLE_BOOKING =
            "CREATE TABLE " + TABLE_BOOKING + " (" +
                    COL_BOOKING_ID + " TEXT PRIMARY KEY, " +
                    COL_BOOKING_CUSTOMER_NIC + " TEXT, " +
                    COL_BOOKING_CHARGER_ID + " TEXT, " +
                    COL_BOOKING_SLOT_ID + " TEXT, " +
                    COL_BOOKING_CREATED_AT + " TEXT, " +
                    COL_BOOKING_UPDATED_AT + " TEXT, " +
                    COL_BOOKING_STATUS + " TEXT)";

    /**
     * Constructor for DBHelper.
     *
     * @param context The context of the application.
     */
    public DBHelper(Context context) {
        // Call the superclass constructor.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method is called when the database is created for the first time.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement to create the user table.
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BOOKING);
    }

    /**
     * This method is called when the database needs to be upgraded.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if it exists.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Recreate the database with the new schema.
        onCreate(db);
    }
}