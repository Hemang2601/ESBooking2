package com.example.esbooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private String userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        ImageButton homeButton = findViewById(R.id.navigation_home);
        ImageButton appointmentsButton = findViewById(R.id.navigation_appointments);
        ImageButton profileButton = findViewById(R.id.navigation_profile);
        ImageButton contactButton = findViewById(R.id.navigation_contact);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ProfileActivity", "Navigating to Home");
                Intent intentToHome = new Intent(ProfileActivity.this, BookingDashboardActivity.class);
                intentToHome.putExtra("user_id", userId);
                intentToHome.putExtra("username", username);
                startActivity(intentToHome);
            }
        });

        appointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ProfileActivity", "Navigating to Appointments");
                Intent intentToAppointments = new Intent(ProfileActivity.this, AppointmentsActivity.class);
                intentToAppointments.putExtra("user_id", userId);
                intentToAppointments.putExtra("username", username);
                startActivity(intentToAppointments);
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ProfileActivity", "Navigating to Contact");
                Intent intentToContact = new Intent(ProfileActivity.this, ContactActivity.class);
                intentToContact.putExtra("user_id", userId);
                intentToContact.putExtra("username", username);
                startActivity(intentToContact);
            }
        });
    }
}
