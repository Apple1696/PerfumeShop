package com.example.perfumeshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.api.AuthRepository;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.presentation.viewmodels.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextUsername, editTextEmail, editTextPassword, editTextYob;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Button buttonRegister;
    private TextView textViewLogin;
    private ProgressBar progressBar;

    private AuthViewModel authViewModel;
    private EditText editTextPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupViewModel();
        setupObservers();
        setupClickListeners();
    }

    private void initViews() {
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextYob = findViewById(R.id.editTextYob);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);
        progressBar = findViewById(R.id.progressBar);
        editTextPhone = findViewById(R.id.editTextPhone);

    }

    private void setupViewModel() {
        AuthRepository authRepository = new AuthRepository(this);
        authViewModel = new AuthViewModel(authRepository);
    }

    private void setupObservers() {
        // Loading state observer
        authViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonRegister.setEnabled(false);
                } else {
                    progressBar.setVisibility(View.GONE);
                    buttonRegister.setEnabled(true);
                }
            }
        });

        // Error message observer
        authViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    authViewModel.clearError();
                }
            }
        });

        // Registration success observer (same as login success)
        authViewModel.getIsLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggedIn) {
                if (isLoggedIn) {
                    // Navigate to main activity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Current user observer
        authViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Toast.makeText(RegisterActivity.this, "Welcome, " + user.getFullName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupClickListeners() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void attemptRegister() {
        // Clear previous errors
        editTextFullName.setError(null);
        editTextUsername.setError(null);
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextYob.setError(null);

        // Get values
        String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String yobString = editTextYob.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        // Check for valid inputs
        if (TextUtils.isEmpty(fullName)) {
            editTextFullName.setError("Full name is required");
            focusView = editTextFullName;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Username is required");
            focusView = editTextUsername;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError("Phone number is required");
            focusView = editTextPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError("Please enter a valid email address");
            focusView = editTextEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            focusView = editTextPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(yobString)) {
            editTextYob.setError("Year of birth is required");
            focusView = editTextYob;
            cancel = true;
        }

        int yob = 0;
        try {
            yob = Integer.parseInt(yobString);
            if (yob < 1900 || yob > 2023) {
                editTextYob.setError("Please enter a valid year");
                focusView = editTextYob;
                cancel = true;
            }
        } catch (NumberFormatException e) {
            editTextYob.setError("Please enter a valid year");
            focusView = editTextYob;
            cancel = true;
        }

        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if (cancel) {
            if (focusView != null) {
                focusView.requestFocus();
            }
        } else {
            // Get gender value
            boolean gender = radioButtonMale.isChecked(); // true for male, false for female

            // Perform registration
            authViewModel.register(fullName, username, email, password, phone, yob, gender);        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }
}