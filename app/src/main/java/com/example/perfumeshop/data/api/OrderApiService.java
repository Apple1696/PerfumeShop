package com.example.perfumeshop.data.api;

import com.example.perfumeshop.data.models.response.OrderDetailResponse;
import com.example.perfumeshop.data.models.response.OrderHistoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface OrderApiService {
    
    @GET("order/user")
    Call<OrderHistoryResponse> getUserOrders(@Header("Authorization") String authToken);
    
    @GET("order/{orderId}")
    Call<OrderDetailResponse> getOrderDetail(@Header("Authorization") String authToken, 
                                           @Path("orderId") String orderId);
}
