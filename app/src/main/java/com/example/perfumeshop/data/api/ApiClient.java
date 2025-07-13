package com.example.perfumeshop.data.api;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.data.api.interceptors.AuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static Retrofit retrofit;
    private static final String TAG = "ApiClient";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Create OkHttpClient with interceptors
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(ApiConfig.TIMEOUT_CONNECT, TimeUnit.SECONDS)
                    .readTimeout(ApiConfig.TIMEOUT_READ, TimeUnit.SECONDS)
                    .writeTimeout(ApiConfig.TIMEOUT_WRITE, TimeUnit.SECONDS)
                    .addInterceptor(new AuthInterceptor());

            // Add logging interceptor for debugging
            // Instead of using BuildConfig.DEBUG, we'll just add logging for now
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConfig.BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
    public static ProfileApiService getProfileApiService() {
        return getRetrofitInstance().create(ProfileApiService.class);
    }
}