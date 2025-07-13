package com.example.perfumeshop.data.api;

import android.content.Context;

import com.example.perfumeshop.data.models.response.OrderHistoryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {
    private final ApiService apiService;
    private final Context context;

    public OrderService(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public interface OrderHistoryCallback {
        void onSuccess(OrderHistoryResponse response);
        void onFailure(String errorMessage);
    }

    public void getOrderHistory(OrderHistoryCallback callback) {
        String token = TokenManager.getAccessToken(context);
        if (token == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        String authHeader = "Bearer " + token;
        apiService.getOrderHistory(authHeader).enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch order history");
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}