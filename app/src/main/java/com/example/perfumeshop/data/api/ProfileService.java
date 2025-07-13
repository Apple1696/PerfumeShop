package com.example.perfumeshop.data.api;

import android.content.Context;

import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.data.models.request.UpdateProfileRequest;
import com.example.perfumeshop.data.models.response.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileService {
    private final ProfileApiService apiService;
    private final Context context;

    public ProfileService(ProfileApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public interface ProfileCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    public void getUserProfile(ProfileCallback callback) {
        String token = TokenManager.getAccessToken(context);
        if (token == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        String authHeader = "Bearer " + token;
        apiService.getUserProfile(authHeader).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch user profile");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void updateProfile(String fullName, int yob, boolean gender, String phone, ProfileCallback callback) {
        String token = TokenManager.getAccessToken(context);
        if (token == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest(fullName, yob, gender, phone);
        String authHeader = "Bearer " + token;

        apiService.updateProfile(authHeader, request).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update locally stored user data
                    User updatedUser = response.body().getUser();
                    callback.onSuccess(updatedUser);
                } else {
                    callback.onFailure("Failed to update profile");
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}