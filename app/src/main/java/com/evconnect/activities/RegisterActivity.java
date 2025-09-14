package com.evconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evconnect.R;
import com.evconnect.db.UserDao;

public class RegisterActivity extends AppCompatActivity {

    // Declare UI elements for user input and actions.
    EditText etNic, etName, etEmail, etPhone, etPassword;
    Button btnRegister, btnGoToLogin;

    // Declare an instance of the data access object (DAO) for database operations.
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity from the XML file.
        setContentView(R.layout.activity_register);

        // Initialize UI components by finding their corresponding views in the layout.
        etNic = findViewById(R.id.etNic);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        // Instantiate the UserDao to handle database-related user operations.
        userDao = new UserDao(this);

        // Set an OnClickListener for the register button.
        btnRegister.setOnClickListener(v -> {
            // Retrieve the text entered by the user from each input field.
            String nic = etNic.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (nic.isEmpty()) {
                etNic.setError("NIC is required");
                etNic.requestFocus();
                return;
            }
            if (name.isEmpty()) {
                etName.setError("Name is required");
                etName.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                etEmail.requestFocus();
                return;
            }
            if (phone.isEmpty()) {
                etPhone.setError("Phone is required");
                etPhone.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                etPassword.requestFocus();
                return;
            }

            // Call the registerUser method in the DAO to save the new user.
            boolean inserted = userDao.registerUser(nic, name, email, phone, password);
            if (inserted) {
                // Display a success message if the user was created successfully.
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                // Navigate the user to the LoginActivity.
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                // Finish the current activity so the user can't go back to the registration screen.
                finish();
            } else {
                // Display a message if the user already exists in the database.
                Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set an OnClickListener for the "Go to Login" button.
        btnGoToLogin.setOnClickListener(v -> {
            // Start the LoginActivity.
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            // Finish the current activity.
            finish();
        });
    }
}