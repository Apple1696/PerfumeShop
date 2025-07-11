// ProfileFragment.java
package com.example.perfumeshop.presentation.fragments.profile;

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

import com.example.perfumeshop.R;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import com.example.perfumeshop.data.api.TokenManager;
import com.example.perfumeshop.presentation.activities.LoginActivity; // Assuming you have an AuthActivity

public class ProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private TextView tvUserName, tvUserLocation, tvTotalShipping, tvRating, tvPoints, tvReviews;
    private ImageView ivEditProfile;
    private LinearLayout llPrivacySecurity, llPaymentHistory;
    private TextView tvMoreDetails;
    private LinearLayout llLogout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        setupClickListeners();
        loadUserData();

        return view;
    }

    private void initViews(View view) {
        // Profile header
        profileImage = view.findViewById(R.id.profile_image);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserLocation = view.findViewById(R.id.tv_user_location);
        ivEditProfile = view.findViewById(R.id.iv_edit_profile);

        // Statistics
        tvTotalShipping = view.findViewById(R.id.tv_total_shipping);
        tvRating = view.findViewById(R.id.tv_rating);
        tvPoints = view.findViewById(R.id.tv_points);
        tvReviews = view.findViewById(R.id.tv_reviews);

        // Menu options
        llPrivacySecurity = view.findViewById(R.id.ll_privacy_security);
        llPaymentHistory = view.findViewById(R.id.ll_payment_history);

        //Logout
        llLogout = view.findViewById(R.id.ll_logout);


    }

    private void setupClickListeners() {
        ivEditProfile.setOnClickListener(v -> {
            // Handle edit profile click
            Toast.makeText(getContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show();
            // You can navigate to edit profile fragment/activity here
        });


        llPrivacySecurity.setOnClickListener(v -> {
            // Handle privacy & security click
            Toast.makeText(getContext(), "Privacy & Security clicked", Toast.LENGTH_SHORT).show();
            // Navigate to privacy settings
        });

        llPaymentHistory.setOnClickListener(v -> {
            // Handle notification preference click
            Toast.makeText(getContext(), "Notification Preference clicked", Toast.LENGTH_SHORT).show();
            // Navigate to notification settings
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

    private void loadUserData() {
        // Load user data from your data source (SharedPreferences, Database, API, etc.)
        // For now, using the sample data from the image

        tvUserName.setText("Lewis Mariyati");
        tvUserLocation.setText("Nganjuk, Indonesia");
        tvTotalShipping.setText("125");
        tvRating.setText("5");
        tvPoints.setText("1500");
        tvReviews.setText("25");

        // You can load the profile image here
        // Example: Using Glide or Picasso to load image from URL
        // Glide.with(this).load(userImageUrl).into(profileImage);
    }

    // Method to update statistics (call this when data changes)
    public void updateStatistics(int totalShipping, float rating, int points, int reviews) {
        if (tvTotalShipping != null) tvTotalShipping.setText(String.valueOf(totalShipping));
        if (tvRating != null) tvRating.setText(String.valueOf(rating));
        if (tvPoints != null) tvPoints.setText(String.valueOf(points));
        if (tvReviews != null) tvReviews.setText(String.valueOf(reviews));
    }

    // Method to update user info
    public void updateUserInfo(String name, String location, String imageUrl) {
        if (tvUserName != null) tvUserName.setText(name);
        if (tvUserLocation != null) tvUserLocation.setText(location);

        // Load profile image if you have an image loading library
        // if (profileImage != null && imageUrl != null) {
        //     Glide.with(this).load(imageUrl).into(profileImage);
        // }
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