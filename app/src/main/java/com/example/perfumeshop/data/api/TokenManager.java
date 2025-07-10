package com.example.perfumeshop.data.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.data.models.response.AuthResponse;

public class TokenManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save authentication data
    public static void saveAuthData(Context context, AuthResponse authResponse) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(KEY_ACCESS_TOKEN, authResponse.getToken());
        editor.putString(KEY_USER_ID, authResponse.getUser().get_id());
        editor.putString(KEY_USER_EMAIL, authResponse.getUser().getEmail());
        editor.putString(KEY_USER_NAME, authResponse.getUser().getFullName());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Get access token
    public static String getAccessToken(Context context) {
        return getPreferences(context).getString(KEY_ACCESS_TOKEN, null);
    }

    // Static method for interceptor (requires context to be set)
    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static String getAccessToken() {
        if (appContext == null) return null;
        return getPreferences(appContext).getString(KEY_ACCESS_TOKEN, null);
    }

    // Check if user is logged in
    public static boolean isLoggedIn(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get current user data
    public static User getCurrentUser(Context context) {
        SharedPreferences prefs = getPreferences(context);
        if (!prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            return null;
        }

        User user = new User();
        user.set_id(prefs.getString(KEY_USER_ID, ""));
        user.setEmail(prefs.getString(KEY_USER_EMAIL, ""));
        user.setFullName(prefs.getString(KEY_USER_NAME, ""));
        return user;
    }

    // Clear authentication data
    public static void clearAuthData(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    // Update token
    public static void updateAccessToken(Context context, String newToken) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(KEY_ACCESS_TOKEN, newToken);
        editor.apply();
    }
}
