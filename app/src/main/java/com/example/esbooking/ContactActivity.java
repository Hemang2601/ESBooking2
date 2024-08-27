package com.example.esbooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    private String userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        ImageButton homeButton = findViewById(R.id.navigation_home);
        ImageButton appointmentsButton = findViewById(R.id.navigation_appointments);
        ImageButton profileButton = findViewById(R.id.navigation_profile);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ContactActivity", "Navigating to Home");

                // Pass user data to BookingDashboardActivity
                Intent intentToHome = new Intent(ContactActivity.this, BookingDashboardActivity.class);
                intentToHome.putExtra("user_id", userId);
                intentToHome.putExtra("username", username);
                startActivity(intentToHome);
            }
        });

        appointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ContactActivity", "Navigating to Appointments");

                // Pass user data to AppointmentsActivity
                Intent intentToAppointments = new Intent(ContactActivity.this, AppointmentsActivity.class);
                intentToAppointments.putExtra("user_id", userId);
                intentToAppointments.putExtra("username", username);
                startActivity(intentToAppointments);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ContactActivity", "Navigating to Profile");

                // Pass user data to ProfileActivity
                Intent intentToProfile = new Intent(ContactActivity.this, ProfileActivity.class);
                intentToProfile.putExtra("user_id", userId);
                intentToProfile.putExtra("username", username);
                startActivity(intentToProfile);
            }
        });
    }
}
