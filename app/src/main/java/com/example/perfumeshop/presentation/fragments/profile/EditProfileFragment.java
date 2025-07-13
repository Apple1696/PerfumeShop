package com.example.perfumeshop.presentation.fragments.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.perfumeshop.R;
import com.example.perfumeshop.data.api.ApiClient;
import com.example.perfumeshop.data.api.ProfileService;
import com.example.perfumeshop.data.api.TokenManager;
import com.example.perfumeshop.data.models.entities.User;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private TextInputEditText etFullName, etUsername, etEmail, etPhone, etYob;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnSave;
    private Toolbar toolbar;

    private ProfileService profileService;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize the service
        profileService = new ProfileService(ApiClient.getProfileApiService(), requireContext());

        initViews(view);
        setupListeners();
        fetchUserProfile();

        return view;
    }

    private void initViews(View view) {
        profileImage = view.findViewById(R.id.profile_image);
        etFullName = view.findViewById(R.id.et_full_name);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        etYob = view.findViewById(R.id.et_yob);
        rgGender = view.findViewById(R.id.rg_gender);
        rbMale = view.findViewById(R.id.rb_male);
        rbFemale = view.findViewById(R.id.rb_female);
        btnSave = view.findViewById(R.id.btn_save);
        toolbar = view.findViewById(R.id.toolbar);
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(v -> validateAndSaveProfile());
    }

    private void fetchUserProfile() {
        profileService.getUserProfile(new ProfileService.ProfileCallback() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                populateUserData(user);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private void populateUserData(User user) {
        etFullName.setText(user.getFullName());
        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());

        if (user.getYob() > 0) {
            etYob.setText(String.valueOf(user.getYob()));
        }

        // Set gender radio button
        if (user.isGender()) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }
    }

    private void validateAndSaveProfile() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String yobStr = etYob.getText().toString().trim();
        boolean isMale = rbMale.isChecked();

        // Validation
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            return;
        }

        if (TextUtils.isEmpty(yobStr)) {
            etYob.setError("Year of birth is required");
            return;
        }

        int yob;
        try {
            yob = Integer.parseInt(yobStr);
            if (yob < 1900 || yob > 2024) {
                etYob.setError("Please enter a valid year");
                return;
            }
        } catch (NumberFormatException e) {
            etYob.setError("Please enter a valid year");
            return;
        }

        // All validation passed, update profile
        updateProfile(fullName, yob, isMale, phone);
    }

    private void updateProfile(String fullName, int yob, boolean gender, String phone) {
        btnSave.setEnabled(false);
        btnSave.setText("Saving...");

        profileService.updateProfile(fullName, yob, gender, phone, new ProfileService.ProfileCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

                // Return to profile fragment
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle different error scenarios
                if (errorMessage.contains("401")) {
                    Toast.makeText(getContext(), "Unauthorized. Please log in again.", Toast.LENGTH_SHORT).show();
                    TokenManager.clearAuthData(requireContext());
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else if (errorMessage.contains("400")) {
                    Toast.makeText(getContext(), "Invalid input. Please check your data.", Toast.LENGTH_SHORT).show();
                } else if (errorMessage.contains("500")) {
                    Toast.makeText(getContext(), "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Network error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }

                btnSave.setEnabled(true);
                btnSave.setText("Save Changes");
            }
        });
    }
}