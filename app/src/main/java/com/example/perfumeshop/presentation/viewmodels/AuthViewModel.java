package com.example.perfumeshop.presentation.viewmodels;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.perfumeshop.data.api.AuthRepository;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.data.models.response.AuthResponse;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        isLoading.setValue(false);
        isLoggedIn.setValue(authRepository.isLoggedIn());
        currentUser.setValue(authRepository.getCurrentUser());
    }

    // Login method
    public void login(String email, String password) {
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
                isLoggedIn.setValue(true);
                currentUser.setValue(authResponse.getUser());
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }

            @Override
            public void onLoading(boolean loading) {
                isLoading.setValue(loading);
            }
        });
    }

    // Register method
    public void register(String fullName, String username, String email, String password, String phone, int yob, boolean gender) {
        authRepository.register(fullName, username, email, password, phone, yob, gender, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
                isLoggedIn.setValue(true);
                currentUser.setValue(authResponse.getUser());
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }

            @Override
            public void onLoading(boolean loading) {
                isLoading.setValue(loading);
            }
        });
    }

    // Logout method
    public void logout() {
        authRepository.logout(new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
                isLoggedIn.setValue(false);
                currentUser.setValue(null);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }

            @Override
            public void onLoading(boolean loading) {
                isLoading.setValue(loading);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearError() {
        errorMessage.setValue(null);
    }

    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
}