package com.example.esbooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BookingDashboardActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_USER_LOGGED_IN = "userLoggedIn";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    private View userInfoCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_dashboard);

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("user_id");
        String username = intent.getStringExtra("username");

        // Set up UI elements
        TextView firstNameTextView = findViewById(R.id.firstNameTextView);

        firstNameTextView.setText(username);


        // Find the profile image view and the user info card view
        ImageView profileImageView = findViewById(R.id.profileImageView);
        userInfoCardView = findViewById(R.id.userInfoCardView);

        // Set an onClickListener for the profile image to toggle visibility of the card
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleUserInfoCardVisibility();
            }
        });

        // Set up navigation buttons
        ImageButton homeButton = findViewById(R.id.navigation_home);
        ImageButton appointmentsButton = findViewById(R.id.navigation_appointments);
        ImageButton profileButton = findViewById(R.id.navigation_profile);
        ImageButton contactButton = findViewById(R.id.navigation_contact);

        // Set up click listeners for navigation buttons
        appointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AppointmentsActivity
                Intent appointmentsIntent = new Intent(BookingDashboardActivity.this, AppointmentsActivity.class);
                startActivity(appointmentsIntent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(BookingDashboardActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ContactActivity
                Intent contactIntent = new Intent(BookingDashboardActivity.this, ContactActivity.class);
                startActivity(contactIntent);
            }
        });

        // Set up logout button
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        // Handle back press with OnBackPressedCallback to avoid window leaks
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void toggleUserInfoCardVisibility() {
        if (userInfoCardView.getVisibility() == View.VISIBLE) {
            userInfoCardView.setVisibility(View.GONE);
        } else {
            userInfoCardView.setVisibility(View.VISIBLE);
        }
    }

    private void showLogoutDialog() {
        if (!isFinishing() && !isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    performLogout();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void showExitConfirmationDialog() {
        if (!isFinishing() && !isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();  // Close all activities in the task
                    System.exit(0);    // Forcefully exit the app
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }


    private void performLogout() {
        // Clear user data and login status from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_LOGGED_IN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.apply();  // Commit the changes

        // Redirect to MainActivity and clear all activities
        Intent intent = new Intent(BookingDashboardActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();  // Finish the current activity
    }
}
