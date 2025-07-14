package com.example.perfumeshop;

public class ApiConfig {
    // Base URL - Update this based on your environment
    public static final String BASE_URL = "http://10.0.2.2:5000/api/";

    // Auth endpoints
    public static final String AUTH_LOGIN = "auth/login";
    public static final String AUTH_REGISTER = "auth/register";
    public static final String AUTH_LOGOUT = "auth/logout";
    public static final String AUTH_REFRESH = "auth/refresh";

    // Chatbot endpoint
    public static final String CHATBOT_CHAT = "chat/message";

    // Perfume endpoints
    public static final String PERFUME_LIST = "perfume";
    public static final String PERFUME_DETAIL = "perfume/{id}"; // Use {id} for dynamic path

    // Brand endpoints
    public static final String BRAND_LIST = "brand";    // Comment endpoints
    public static final String COMMENT_CREATE = "comment/{id}"; // Use {perfumeId}
    
    // Order endpoints
    public static final String ORDER_CREATE = "payment/create-payment";
    public static final String ORDER_HISTORY = "order/user";
    public static final String ORDER_DETAIL = "order/{orderId}";

    // User Profile endpoint
    public static final String USER_PROFILE = "user/profile";
    public static final String UPDATE_PROFILE = "user/profile";

    // Timeouts
    public static final int TIMEOUT_CONNECT = 120; // seconds
    public static final int TIMEOUT_READ = 120; // seconds
    public static final int TIMEOUT_WRITE = 120; // seconds

    // Header
    public static final String CONTENT_TYPE = "application/json";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
}