package com.example.perfumeshop.data.api.interceptors;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.data.api.TokenManager;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Add common headers
        Request.Builder builder = originalRequest.newBuilder()
                .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                .addHeader("Accept", ApiConfig.CONTENT_TYPE);

        // Add authorization header if token exists
        String token = TokenManager.getAccessToken();
        if (token != null && !token.isEmpty()) {
            builder.addHeader(ApiConfig.AUTHORIZATION, ApiConfig.BEARER + token);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}