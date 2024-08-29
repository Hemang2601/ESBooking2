package com.example.esbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class CompletedServicesActivity extends AppCompatActivity {

    private String userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_services);

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        // Initialize the back arrow button and set the onClick listener
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAppointments = new Intent(CompletedServicesActivity.this, AppointmentsActivity.class);
                intentToAppointments.putExtra("user_id", userId);
                intentToAppointments.putExtra("username", username);
                startActivity(intentToAppointments);
                finish(); // Close this activity and return to the AppointmentsActivity
            }
        });

        // Initialize RecyclerView and other components as needed
    }
}
