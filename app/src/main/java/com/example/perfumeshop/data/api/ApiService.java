package com.example.perfumeshop.data.api;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.data.models.request.LoginRequest;
import com.example.perfumeshop.data.models.request.RegisterRequest;
import com.example.perfumeshop.data.models.response.ApiResponse;
import com.example.perfumeshop.data.models.response.AuthResponse;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST(ApiConfig.AUTH_LOGIN)
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST(ApiConfig.AUTH_REGISTER)
    Call<ApiResponse<String>> register(@Body RegisterRequest registerRequest);

    @POST(ApiConfig.AUTH_LOGOUT)
    Call<ApiResponse<String>> logout(@Header("Authorization") String authToken);

    @POST(ApiConfig.AUTH_REFRESH)
    Call<AuthResponse> refreshToken(@Header("Authorization") String refreshToken);

    // Add other endpoints as needed
    @GET("user/profile")
    Call<User> getUserProfile(@Header("Authorization") String authToken);
}