package com.example.perfumeshop.data.api.AuthUseCase;

import com.example.perfumeshop.data.api.AuthRepository;

public class LogoutUseCase {
    private AuthRepository authRepository;

    public LogoutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(AuthRepository.AuthCallback callback) {
        authRepository.logout(callback);
    }
}
