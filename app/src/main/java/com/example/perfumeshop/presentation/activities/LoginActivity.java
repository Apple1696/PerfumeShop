package com.example.perfumeshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.perfumeshop.data.api.AuthRepository;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.presentation.viewmodels.AuthViewModel;
import com.example.perfumeshop.R;
public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupViewModel();
        setupObservers();
        setupClickListeners();

        // Check if user is already logged in
        checkLoginState();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupViewModel() {
        AuthRepository authRepository = new AuthRepository(this);
        authViewModel = new AuthViewModel(authRepository);
    }

    private void setupObservers() {
        // Login success observer
        authViewModel.getIsLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn) {
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Error observer
        authViewModel.getErrorMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        // Loading observer
        authViewModel.getIsLoading().observe(this, isLoading -> {
            // Show/hide loading indicator
            if (isLoading) {
                // Show progress bar or loading dialog
            } else {
                // Hide progress bar or loading dialog
            }
        });
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        // Add this to the setupClickListeners method in LoginActivity.java
        TextView textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {
        // Clear previous errors
        editTextEmail.setError(null);
        editTextPassword.setError(null);

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for valid password
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            focusView = editTextPassword;
            cancel = true;
        }

        // Check for valid email
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError("Please enter a valid email address");
            focusView = editTextEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Perform login
            authViewModel.login(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void checkLoginState() {
        AuthRepository authRepository = new AuthRepository(this);
        if (authRepository.isLoggedIn()) {
            // User is already logged in, navigate to main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}
