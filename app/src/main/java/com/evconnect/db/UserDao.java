package com.evconnect.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.mindrot.jbcrypt.BCrypt;

/**
 * This class serves as a Data Access Object (DAO) for the 'user' table.
 * It encapsulates all the database operations related to user management,
 * such as registration and login, providing a clean separation of concerns.
 */
public class UserDao {
    // An instance of DBHelper to get a reference to the database.
    private final DBHelper dbHelper;

    /**
     * Constructor for UserDao.
     * @param context The application context, required to initialize DBHelper.
     */
    public UserDao(Context context) {
        // Initialize the DBHelper.
        dbHelper = new DBHelper(context);
    }

    /**
     * Registers a new user in the database.
     * @param nic The user's NIC (National Identity Card) number.
     * @param name The user's full name.
     * @param email The user's email address.
     * @param password The user's password.
     * @return True if the user was successfully inserted, false otherwise (e.g., if NIC already exists).
     */
    public boolean registerUser(String nic, String name, String email, String phone, String role, String password) {
        // Get a writable database instance.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object to store key-value pairs of the user data.
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_NIC, nic);
        values.put(DBHelper.COL_NAME, name);
        values.put(DBHelper.COL_EMAIL, email);
        values.put(DBHelper.COL_PHONE, phone);
        values.put(DBHelper.COL_ROLE, role);
        values.put(DBHelper.COL_PASSWORD, password);
        values.put(DBHelper.COL_STATUS, "ACTIVE"); // Set the initial status to 'ACTIVE'.

        // Insert the new row into the 'user' table.
        // The insert method returns the row ID of the newly inserted row, or -1 if an error occurred.
        long result = db.insert(DBHelper.TABLE_USER, null, values);
        // Close the database connection to free up resources.
        db.close();
        // Return true if the insertion was successful (result is not -1), otherwise false.
        return result != -1;
    }

    /**
     * Validates a user's login credentials.
     * @param nic The user's NIC number.
     * @param password The user's password.
     * @return True if a user with the given NIC and password exists and is active, false otherwise.
     */
    public boolean loginUser(String nic, String password) {
        // Get a readable database instance.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Perform a database query to find a user that matches the provided NIC, password, and active status.
        Cursor cursor = db.query(
                DBHelper.TABLE_USER,
                new String[]{DBHelper.COL_PASSWORD},  // Fetch stored hashed password
                DBHelper.COL_NIC + "=? AND " + DBHelper.COL_STATUS + "=?",
                new String[]{nic, "ACTIVE"},
                null, null, null
        );

        // Check if the cursor contains any rows. If moveToFirst() returns true, a matching user was found.
        boolean loggedIn = false;
        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_PASSWORD));
            loggedIn = BCrypt.checkpw(password, storedHash);
        }
        // Always close the cursor to release its resources.
        cursor.close();
        // Close the database connection.
        db.close();
        // Return the login status.
        return loggedIn;
    }
}