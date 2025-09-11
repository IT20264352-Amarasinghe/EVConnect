package com.evconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evconnect.R;
import com.evconnect.db.UserDao;

// This class handles the user login functionality.
public class LoginActivity extends AppCompatActivity {

    // Declare UI elements as member variables.
    EditText etNic, etPassword;
    Button btnLogin, btnGoToRegister;
    // Declare an instance of the data access object for user operations.
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_login layout file.
        setContentView(R.layout.activity_login);

        // Initialize UI elements by finding them by their IDs from the layout file.
        etNic = findViewById(R.id.etNic);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // Create a new instance of UserDao to interact with user data.
        userDao = new UserDao(this);

        // Set an OnClickListener for the login button.
        btnLogin.setOnClickListener(v -> {
            // Get the text from the NIC and password input fields.
            String nic = etNic.getText().toString();
            String password = etPassword.getText().toString();

            // Call the loginUser method from UserDao to validate the credentials.
            if (userDao.loginUser(nic, password)) {
                // Display a success message if login is successful.
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                // start the dashboard activity upon successful login.
//                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            } else {
                // Display a failure message if login fails.
                Toast.makeText(this, "Invalid Credentials or Account Deactivated", Toast.LENGTH_SHORT).show();
            }
        });

        // Set an OnClickListener for the "Go to Register" button.
        btnGoToRegister.setOnClickListener(v -> {
            // Start the RegisterActivity.
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            // Finish the current activity to remove it from the back stack.
            finish();
        });
    }
}
