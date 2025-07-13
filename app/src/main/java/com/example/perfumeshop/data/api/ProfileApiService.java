package com.example.perfumeshop.data.api;

import com.example.perfumeshop.data.models.entities.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ProfileApiService {
    @GET("user/profile")
    Call<User> getUserProfile(@Header("Authorization") String authToken);
}