package com.example.perfumeshop.data.api;

import android.content.Context;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.data.models.request.LoginRequest;
import com.example.perfumeshop.data.models.request.RegisterRequest;
import com.example.perfumeshop.data.models.response.ApiResponse;
import com.example.perfumeshop.data.models.response.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;
    private Context context;

    public AuthRepository(Context context) {
        this.context = context;
        this.apiService = ApiClient.getApiService();
    }

    // Login interface
    public interface AuthCallback {
        void onSuccess(AuthResponse authResponse);
        void onError(String errorMessage);
        void onLoading(boolean isLoading);
    }

    // Login method
    public void login(String email, String password, AuthCallback callback) {
        callback.onLoading(true);

        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<AuthResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                callback.onLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    // Save authentication data
                    TokenManager.saveAuthData(context, authResponse);

                    callback.onSuccess(authResponse);
                } else {
                    String errorMessage = "Login failed";
                    if (response.code() == 401) {
                        errorMessage = "Invalid email or password";
                    } else if (response.code() >= 500) {
                        errorMessage = "Server error, please try again later";
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onLoading(false);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    // Add this method to the AuthRepository class
    public void register(String fullName, String username, String email, String password, String phone, int yob, boolean gender, AuthCallback callback) {
        callback.onLoading(true);

        RegisterRequest registerRequest = new RegisterRequest(fullName, username, email, password, phone, yob, gender);

        Call<ApiResponse<String>> call = apiService.register(registerRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                callback.onLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    // Registration successful, now login the user
                    login(email, password, callback);
                } else {
                    String errorMessage = "Registration failed";
                    if (response.code() == 400) {
                        errorMessage = "Email already exists";
                    } else if (response.code() >= 500) {
                        errorMessage = "Server error, please try again later";
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                callback.onLoading(false);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Logout method
    public void logout(AuthCallback callback) {
        String token = TokenManager.getAccessToken(context);
        if (token == null) {
            callback.onError("No valid token found");
            return;
        }

        callback.onLoading(true);

        Call<ApiResponse<String>> call = apiService.logout(ApiConfig.BEARER + token);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                callback.onLoading(false);

                // Clear local data regardless of server response
                TokenManager.clearAuthData(context);

                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    // Still consider it success since local data is cleared
                    callback.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                callback.onLoading(false);

                // Clear local data even on network failure
                TokenManager.clearAuthData(context);
                callback.onSuccess(null);
            }
        });
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return TokenManager.isLoggedIn(context);
    }

    // Get current user
    public User getCurrentUser() {
        return TokenManager.getCurrentUser(context);
    }
}