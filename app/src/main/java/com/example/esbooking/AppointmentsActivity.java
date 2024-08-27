package com.example.esbooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AppointmentsActivity extends AppCompatActivity {

    private String userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        ImageButton homeButton = findViewById(R.id.navigation_home);
        ImageButton appointmentsButton = findViewById(R.id.navigation_appointments);
        ImageButton profileButton = findViewById(R.id.navigation_profile);
        ImageButton contactButton = findViewById(R.id.navigation_contact);

        // CardView instances
        CardView bookNewServiceCard = findViewById(R.id.cardBookNewService);
        CardView pendingServicesCard = findViewById(R.id.cardPendingServices);
        CardView completedServicesCard = findViewById(R.id.cardCompletedServices);
        CardView billingCard = findViewById(R.id.cardBilling);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Home");
                Intent intentToHome = new Intent(AppointmentsActivity.this, BookingDashboardActivity.class);
                intentToHome.putExtra("user_id", userId);
                intentToHome.putExtra("username", username);
                startActivity(intentToHome);
            }
        });

        appointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Appointments");
                // The current activity is already AppointmentsActivity
                // You can choose to do nothing or show a message if desired
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Profile");
                Intent intentToProfile = new Intent(AppointmentsActivity.this, ProfileActivity.class);
                intentToProfile.putExtra("user_id", userId);
                intentToProfile.putExtra("username", username);
                startActivity(intentToProfile);
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Contact");
                Intent intentToContact = new Intent(AppointmentsActivity.this, ContactActivity.class);
                intentToContact.putExtra("user_id", userId);
                intentToContact.putExtra("username", username);
                startActivity(intentToContact);
            }
        });

        bookNewServiceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Book New Service");
                Intent intentToBookNewService = new Intent(AppointmentsActivity.this, BookNewServiceActivity.class);
                intentToBookNewService.putExtra("user_id", userId);
                intentToBookNewService.putExtra("username", username);
                startActivity(intentToBookNewService);
            }
        });

        pendingServicesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Pending Services");
                Intent intentToPendingServices = new Intent(AppointmentsActivity.this, PendingServicesActivity.class);
                intentToPendingServices.putExtra("user_id", userId);
                intentToPendingServices.putExtra("username", username);
                startActivity(intentToPendingServices);
            }
        });

        completedServicesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Completed Services");
                Intent intentToCompletedServices = new Intent(AppointmentsActivity.this, CompletedServicesActivity.class);
                intentToCompletedServices.putExtra("user_id", userId);
                intentToCompletedServices.putExtra("username", username);
                startActivity(intentToCompletedServices);
            }
        });

        billingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppointmentsActivity", "Navigating to Billing");
                Intent intentToBilling = new Intent(AppointmentsActivity.this, BillingActivity.class);
                intentToBilling.putExtra("user_id", userId);
                intentToBilling.putExtra("username", username);
                startActivity(intentToBilling);
            }
        });
    }
}
