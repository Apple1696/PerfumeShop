package com.example.perfumeshop.data.api.AuthUseCase;

import com.example.perfumeshop.data.api.AuthRepository;

public class LoginUseCase {
    private AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthRepository.AuthCallback callback) {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            callback.onError("Email is required");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            callback.onError("Password is required");
            return;
        }

        if (!isValidEmail(email)) {
            callback.onError("Please enter a valid email address");
            return;
        }

        // Proceed with login
        authRepository.login(email.trim(), password, callback);
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}