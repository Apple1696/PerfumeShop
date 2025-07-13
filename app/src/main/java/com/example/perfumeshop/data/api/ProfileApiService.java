package com.example.perfumeshop.data.api;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.data.models.request.UpdateProfileRequest;
import com.example.perfumeshop.data.models.response.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface ProfileApiService {
    @GET(ApiConfig.USER_PROFILE)
    Call<User> getUserProfile(@Header("Authorization") String authToken);

    @PUT(ApiConfig.UPDATE_PROFILE)
    Call<UpdateProfileResponse> updateProfile(
            @Header("Authorization") String authToken,
            @Body UpdateProfileRequest request
    );
}