package com.example.perfumeshop.presentation.activities;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.api.AuthRepository;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.presentation.viewmodels.AuthViewModel;

public class MainActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViewModel();
        setupObservers();

        // Check if user is logged in
        checkLoginState();
    }

    private void initViews() {
        textViewWelcome = findViewById(R.id.textViewWelcome);
    }

    private void setupViewModel() {
        AuthRepository authRepository = new AuthRepository(this);
        authViewModel = new AuthViewModel(authRepository);
        // Initialize the logout button and set its click listener
        findViewById(R.id.buttonLogout).setOnClickListener(view -> {
            authViewModel.logout();
            Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {
        // Current user observer
        authViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    textViewWelcome.setText("Welcome, " + user.getFullName());
                }
            }
        });

        // Login state observer
        authViewModel.getIsLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggedIn) {
                if (!isLoggedIn) {
                    // User logged out, navigate to login
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Loading state observer
        authViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLoginState() {
        AuthRepository authRepository = new AuthRepository(this);
        if (!authRepository.isLoggedIn()) {
            // User is not logged in, navigate to login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            authViewModel.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}