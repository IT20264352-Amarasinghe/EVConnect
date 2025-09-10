package com.evconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evconnect.R;
import com.evconnect.db.UserDao;

public class LoginActivity extends AppCompatActivity {

    EditText etNic, etPassword;
    Button btnLogin, btnGoToRegister;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNic = findViewById(R.id.etNic);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        userDao = new UserDao(this);

        btnLogin.setOnClickListener(v -> {
            String nic = etNic.getText().toString();
            String password = etPassword.getText().toString();

            if (userDao.loginUser(nic, password)) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid Credentials or Account Deactivated", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }
}
