package com.example.perfumeshop.data.api;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.data.models.request.ChatRequest;
import com.example.perfumeshop.data.models.response.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatbotApiService {
    @POST(ApiConfig.CHATBOT_CHAT)
    Call<ChatResponse> sendMessage(@Body ChatRequest chatRequest);
}
