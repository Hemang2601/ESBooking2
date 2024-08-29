package com.example.esbooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";  // Use a constant tag for logging

    private String userId;
    private String username;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView userPhoneTextView;
    private TextView userAddressTextView;
    private ProgressBar loadingSpinner;
    private CardView profileCardView;
    private CardView changePasswordCardView;  // Add this field for the change password card
    private Button changePasswordButton;  // Change password button in profile view
    private Button newChangePasswordButton;  // Button in change password card

    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameTextView = findViewById(R.id.userNameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userPhoneTextView = findViewById(R.id.userPhoneTextView);
        userAddressTextView = findViewById(R.id.userAddressTextView);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        profileCardView = findViewById(R.id.profileCardView);
        changePasswordCardView = findViewById(R.id.changePasswordCardView);  // Initialize the change password card
        changePasswordButton = findViewById(R.id.changePasswordButton);  // Initialize the button
        newChangePasswordButton = findViewById(R.id.newchangePasswordButton);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);// Initialize button inside the change password card
        newPasswordEditText = findViewById(R.id.newPasswordEditText);


        // Initially hide the change password card
        changePasswordCardView.setVisibility(CardView.GONE);

        // Log that onCreate has been called
        Log.d(TAG, "onCreate: started");

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        Log.d(TAG, "onCreate: Received userId: " + userId + ", username: " + username);

        // Initialize buttons
        ImageButton homeButton = findViewById(R.id.navigation_home);
        ImageButton appointmentsButton = findViewById(R.id.navigation_appointments);
        ImageButton profileButton = findViewById(R.id.navigation_profile);
        ImageButton contactButton = findViewById(R.id.navigation_contact);

        // Setup navigation listeners
        homeButton.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to HomeActivity");
            navigateTo(BookingDashboardActivity.class);
        });
        appointmentsButton.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to AppointmentsActivity");
            navigateTo(AppointmentsActivity.class);
        });
        contactButton.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to ContactActivity");
            navigateTo(ContactActivity.class);
        });

        // Set up change password button click listener
        changePasswordButton.setOnClickListener(v -> showChangePasswordConfirmationDialog());

        // Set up new change password button click listener
        newChangePasswordButton.setOnClickListener(v -> {
            // Handle the actual password change process here
            Toast.makeText(this, "Password Change Process Initiated", Toast.LENGTH_SHORT).show();
        });

        // Make the API call to fetch user profile
        Log.d(TAG, "fetchUserProfile: Making API call to fetch user profile");
        fetchUserProfile();
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intentToNavigate = new Intent(ProfileActivity.this, activityClass);
        intentToNavigate.putExtra("user_id", userId);
        intentToNavigate.putExtra("username", username);
        Log.d(TAG, "Navigating to: " + activityClass.getSimpleName() + " with userId: " + userId + ", username: " + username);
        startActivity(intentToNavigate);
    }

    private void fetchUserProfile() {
        // Show the ProgressBar and hide the ProfileCardView initially
        loadingSpinner.setVisibility(ProgressBar.VISIBLE);
        profileCardView.setVisibility(CardView.GONE);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Log the user_id that is being sent in the API call
        Log.d(TAG, "fetchUserProfile: Sending user_id: " + userId + " in API call");

        Call<UserProfileResponse> call = apiService.getUserProfile(userId);

        Log.d(TAG, "fetchUserProfile: Enqueueing API call");

        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                // Hide the ProgressBar and show the ProfileCardView
                loadingSpinner.setVisibility(ProgressBar.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse userProfileResponse = response.body();

                    if (userProfileResponse.isSuccess()) {
                        UserProfileResponse.UserProfile profile = userProfileResponse.getData();

                        // Set the data to TextViews
                        userNameTextView.setText(profile.getName());
                        userEmailTextView.setText(profile.getEmail());
                        userPhoneTextView.setText(profile.getPhone());
                        userAddressTextView.setText(profile.getAddress());

                        // Show the ProfileCardView
                        profileCardView.setVisibility(CardView.VISIBLE);

                        // Log profile data
                        Log.d(TAG, "Profile Data - Name:    " + profile.getName());
                        Log.d(TAG, "Profile Data - Email:   " + profile.getEmail());
                        Log.d(TAG, "Profile Data - Phone:   " + profile.getPhone());
                        Log.d(TAG, "Profile Data - Address: " + profile.getAddress());
                    } else {
                        Log.e(TAG, "onResponse: Error - " + userProfileResponse.getMessage());
                        // Optionally show an error message to the user here
                    }
                } else {
                    Log.e(TAG, "onResponse: Response unsuccessful or empty. Response code: " + response.code());
                    // Optionally show an error message to the user here
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // Hide the ProgressBar and show the ProfileCardView (optional)
                loadingSpinner.setVisibility(ProgressBar.GONE);

                Log.e(TAG, "onFailure: API call failed: " + t.getMessage(), t);

                // Show an AlertDialog suggesting the user restart the app
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Error")
                        .setMessage("An unexpected error occurred. Please restart the application.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Optionally restart the app by launching the main activity
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setCancelable(false) // Prevents the user from dismissing the dialog without pressing the OK button
                        .show();
            }

        });
    }

    private void showChangePasswordConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Change Password")
                .setMessage("Are you sure you want to change your password?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Handle the change password action
                    openChangePasswordCard();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void openChangePasswordCard() {
        // Show the change password card view
        profileCardView.setVisibility(CardView.GONE);
        changePasswordCardView.setVisibility(CardView.VISIBLE);

        // Set up the change password button click listener
        Button newChangePasswordButton = findViewById(R.id.newchangePasswordButton);
        EditText newPasswordEditText = findViewById(R.id.newPasswordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        CheckBox agreeTermsCheckBox = findViewById(R.id.agreeTermsCheckBox);
        ProgressBar loadingSpinner = findViewById(R.id.loadingSpinner); // Assuming you have a ProgressBar in your layout

        newChangePasswordButton.setOnClickListener(v -> {
            // Hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            boolean agreeToTerms = agreeTermsCheckBox.isChecked();


            // Validate input fields
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!agreeToTerms) {
                Toast.makeText(this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                return;
            }




            // Make the API call to change the password
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<ChangePasswordResponse> call = apiService.profileChangePassword(userId, newPassword);

            changePasswordCardView.setVisibility(CardView.GONE);
            loadingSpinner.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                    // Hide the loading spinner


                    if (response.isSuccessful() && response.body() != null) {
                        ChangePasswordResponse changePasswordResponse = response.body();

                        if (changePasswordResponse.isSuccess()) {
                            Toast.makeText(ProfileActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();

                                // Show profile card view
                                loadingSpinner.setVisibility(View.GONE);
                                profileCardView.setVisibility(CardView.VISIBLE);

                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to change password: " + changePasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to change password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                    // Hide the loading spinner
                    loadingSpinner.setVisibility(View.GONE);

                    // Show an AlertDialog suggesting the user restart the app
                    new AlertDialog.Builder(ProfileActivity.this)
                            .setTitle("Error")
                            .setMessage("An unexpected error occurred. Please restart the application.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // You could optionally add code here to restart the app, if desired
                                // For example, you could restart the app by launching the main activity:
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            })
                            .setCancelable(false) // Prevents the user from dismissing the dialog without pressing the OK button
                            .show();
                }

            });
        });
    }



}
