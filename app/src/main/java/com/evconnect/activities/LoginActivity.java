package com.evconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evconnect.R;
import com.evconnect.db.UserDao;
import com.evconnect.models.LoginRequest;
import com.evconnect.models.LoginResponse;
import com.evconnect.network.ApiClient;
import com.evconnect.network.ApiService;
import com.evconnect.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// This class handles the user login functionality.
public class LoginActivity extends AppCompatActivity {

    // Declare UI elements as member variables.
    EditText etNic, etPassword;
    Button btnLogin, btnGoToRegister;
    // Declare an instance of the data access object for user operations.
    UserDao userDao;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_login layout file.
        setContentView(R.layout.activity_login);
        tokenManager = new TokenManager(this);

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
            String nic = etNic.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Check if NIC or password is empty
            if (nic.isEmpty()) {
                etNic.setError("NIC is required");
                etNic.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                etPassword.requestFocus();
                return;
            }

            // Call the loginUser method from UserDao to validate the credentials.
            if (userDao.loginUser(nic, password)) {
                // Display a success message if login is successful.
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                // start the dashboard activity upon successful login.
//                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            } else {
                checkServerLogin(nic, password);
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

    private void checkServerLogin(String nic, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        LoginRequest request = new LoginRequest(nic, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    // Save token locally (SharedPreferences, etc.)
                    tokenManager.saveToken(token);

                    Toast.makeText(LoginActivity.this, "Login Successful (Server)!", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
