package com.example.perfumeshop;

import android.app.Application;

import com.example.perfumeshop.data.api.TokenManager;

public class PerfumeShopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize TokenManager with application context
        TokenManager.init(this);
    }
}