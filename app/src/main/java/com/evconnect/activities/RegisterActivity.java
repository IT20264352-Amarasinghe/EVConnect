package com.evconnect.activities;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evconnect.R;
import com.evconnect.db.UserDao;
import com.evconnect.models.ErrorResponse;
import com.evconnect.models.RegistrationRequest;
import com.evconnect.models.RegistrationResponse;
import com.evconnect.network.ApiClient;
import com.evconnect.network.ApiService;
import com.evconnect.utils.ApiErrorUtils;
import com.google.gson.Gson;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    // Declare UI elements for user input and actions.
    EditText etNic, etName, etEmail, etPhone, etPassword;
    Button btnRegister, btnGoToLogin;

    // Declare an instance of the data access object (DAO) for database operations.
    UserDao userDao;

    // NIC pattern: exactly 9 digits followed by 'V' (case-insensitive) OR exactly 10 digits (0-9)
    private static final Pattern NIC_PATTERN =
            Pattern.compile("^[0-9]{9}[vV]$|^[0-9]{10}$");

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

            // Validate NIC format (10 digits OR 9 digits + V/v)
            if (!NIC_PATTERN.matcher(nic).matches()) {
                etNic.setError("NIC must be 10 digits OR 9 digits ending with 'V'");
                etNic.requestFocus();
                return;
            }

            // Validate Email format (using Android's built-in Patterns utility)
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Enter a valid email address");
                etEmail.requestFocus();
                return;
            }

            // Validate Phone number (must be exactly 10 digits)
            if (phone.length() != 10 || !android.text.TextUtils.isDigitsOnly(phone)) {
                etPhone.setError("Phone number must be exactly 10 digits");
                etPhone.requestFocus();
                return;
            }

            setRegisterLoading(true);
            // Call backend
            ApiService apiService = ApiClient.getClient(RegisterActivity.this).create(ApiService.class);
            RegistrationRequest request = new RegistrationRequest(nic, name, email, phone, "customer" ,password);

            apiService.register(request).enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                    setRegisterLoading(false);
                    if (response.isSuccessful() && response.body() != null) {
                        RegistrationResponse registeredUser = response.body();
                        // Save user locally
                        boolean inserted = userDao.registerUser(
                                registeredUser.getNic(),
                                registeredUser.getName(),
                                registeredUser.getEmail(),
                                registeredUser.getPhone(),
                                registeredUser.getRole(),
                                registeredUser.getPassword()
                        );

                        if (inserted) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Local data sync failed", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    } else if (!response.isSuccessful()) {
                        try {
                            String errorMessage = ApiErrorUtils.getErrorMessage(response);
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            Log.e("RegisterActivity", "RegisterActivity Error: " + errorMessage);
                        } catch (Exception e) {
                            Log.e("RegisterActivity", "RegisterActivity Error: " + e);
                            Toast.makeText(RegisterActivity.this, "Registration Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    setRegisterLoading(false);
                    Log.e("RegisterActivity", "Error: " + t);
                    Toast.makeText(RegisterActivity.this, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Set an OnClickListener for the "Go to Login" button.
        btnGoToLogin.setOnClickListener(v -> {
            // Start the LoginActivity.
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            // Finish the current activity.
            finish();
        });
    }

    private void setRegisterLoading(boolean isLoading) {
        if (isLoading) {
            btnRegister.setEnabled(false);
            btnRegister.setText("");
            findViewById(R.id.progressRegister).setVisibility(View.VISIBLE);
        } else {
            btnRegister.setEnabled(true);
            btnRegister.setText("Register");
            findViewById(R.id.progressRegister).setVisibility(View.GONE);
        }
    }
}