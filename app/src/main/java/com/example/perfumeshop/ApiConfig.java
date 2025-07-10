package com.example.perfumeshop;

public class ApiConfig {
    // Base URL - Update this based on your environment
    public static final String BASE_URL = "http://10.0.2.2:5000/api/";

    // Auth endpoints
    public static final String AUTH_LOGIN = "auth/login";
    public static final String AUTH_REGISTER = "auth/register";
    public static final String AUTH_LOGOUT = "auth/logout";
    public static final String AUTH_REFRESH = "auth/refresh";

    // Timeouts
    public static final int TIMEOUT_CONNECT = 30; // seconds
    public static final int TIMEOUT_READ = 30; // seconds
    public static final int TIMEOUT_WRITE = 30; // seconds

    // Headers
    public static final String CONTENT_TYPE = "application/json";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
}