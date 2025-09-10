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

    EditText etNic, etName, etEmail, etPhone, etPassword;
    Button btnRegister, btnGoToLogin;

    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNic = findViewById(R.id.etNic);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        userDao = new UserDao(this);

        btnRegister.setOnClickListener(v -> {
            String nic = etNic.getText().toString();
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();

            if (nic.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "NIC & Password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = userDao.registerUser(nic, name, email, phone, password);
            if (inserted) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}