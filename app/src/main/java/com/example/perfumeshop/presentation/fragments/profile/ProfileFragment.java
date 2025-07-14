package com.example.perfumeshop.presentation.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.perfumeshop.ApiConfig;
import com.example.perfumeshop.R;
import com.example.perfumeshop.data.api.ApiClient;
import com.example.perfumeshop.data.api.TokenManager;
import com.example.perfumeshop.data.api.ProfileApiService;
import com.example.perfumeshop.data.models.entities.User;
import com.example.perfumeshop.presentation.activities.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private TextView tvUserName, tvUsername, tvUserEmail, tvUserPhone;
    private ImageView ivEditProfile;
    private LinearLayout llPrivacySecurity, llPaymentHistory, llOrderHistory;
    private LinearLayout llLogout;
    private ProfileApiService profileApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        setupClickListeners();

        // Initialize ProfileApiService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        profileApiService = retrofit.create(ProfileApiService.class);

        // Load user data from API
        fetchUserProfile();

        return view;
    }

    private void initViews(View view) {
        // Profile header
        profileImage = view.findViewById(R.id.profile_image);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUsername = view.findViewById(R.id.tv_username);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        ivEditProfile = view.findViewById(R.id.iv_edit_profile);
        tvUserPhone = view.findViewById(R.id.tv_user_phone);

        // Menu options
        llPrivacySecurity = view.findViewById(R.id.ll_privacy_security);
        llOrderHistory = view.findViewById(R.id.ll_order_history);
        llPaymentHistory = view.findViewById(R.id.ll_payment_history);

        // Logout
        llLogout = view.findViewById(R.id.ll_logout);
    }

    private void setupClickListeners() {
        ivEditProfile.setOnClickListener(v -> {
            // Navigate to edit profile fragment
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, editProfileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        llPrivacySecurity.setOnClickListener(v -> {
            // Handle privacy & security click
            Toast.makeText(getContext(), "Privacy & Security clicked", Toast.LENGTH_SHORT).show();
            // Navigate to privacy settings
        });

        llOrderHistory.setOnClickListener(v -> {
            // Navigate to order history fragment
            OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, orderHistoryFragment)
                    .addToBackStack(null)
                    .commit();
        });

        llPaymentHistory.setOnClickListener(v -> {
            // Handle payment history click
            Toast.makeText(getContext(), "Payment History clicked", Toast.LENGTH_SHORT).show();
            // Navigate to payment history
        });

        profileImage.setOnClickListener(v -> {
            // Handle profile image click - maybe open image picker
            Toast.makeText(getContext(), "Profile Image clicked", Toast.LENGTH_SHORT).show();
        });

        llLogout.setOnClickListener(v -> {
            // Handle logout
            logoutUser();
        });
    }

    private void fetchUserProfile() {
        if (getContext() == null) return;

        String token = TokenManager.getAccessToken(requireContext());
        if (token == null) {
            Toast.makeText(getContext(), "Authentication token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String authHeader = ApiConfig.BEARER + token;

        profileApiService.getUserProfile(authHeader).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    updateUI(user);
                } else {
                    Toast.makeText(getContext(), "Failed to load profile: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(User user) {
        if (user == null) return;

        // Use getters instead of direct field access
        tvUserName.setText(user.getFullName());
        tvUsername.setText("@" + user.getUsername());
        tvUserEmail.setText(user.getEmail());
        tvUserPhone.setText(user.getPhone());

    }

    private void logoutUser() {
        // Show confirmation dialog
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Clear auth data using TokenManager
                    TokenManager.clearAuthData(requireContext());

                    // Show success message
                    Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to login/auth activity
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}