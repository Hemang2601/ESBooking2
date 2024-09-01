package com.example.esbooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingServicesActivity extends AppCompatActivity {

    private static final String TAG = "PendingServicesActivity"; // Tag for logging
    private String userId;
    private String username;
    private RecyclerView pendingServicesRecyclerView;
    private PendingServicesAdapter pendingServicesAdapter;
    private List<PendingService> pendingServiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_services);

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        // Initialize the back arrow button and set the onClick listener
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAppointments = new Intent(PendingServicesActivity.this, AppointmentsActivity.class);
                intentToAppointments.putExtra("user_id", userId);
                intentToAppointments.putExtra("username", username);
                startActivity(intentToAppointments);
                finish(); // Close this activity and return to the AppointmentsActivity
            }
        });

        // Initialize RecyclerView for pending services
        pendingServicesRecyclerView = findViewById(R.id.pendingServicesRecyclerView);
        pendingServicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the pending service list and adapter
        pendingServiceList = new ArrayList<>();
        pendingServicesAdapter = new PendingServicesAdapter(pendingServiceList);
        pendingServicesRecyclerView.setAdapter(pendingServicesAdapter);

        // Load pending services from the API
        loadPendingServices();
    }

    private void loadPendingServices() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<PendingServicesResponse> call = apiService.getPendingServices(userId);

        Log.d(TAG, "API call to get pending services started");

        call.enqueue(new Callback<PendingServicesResponse>() {
            @Override
            public void onResponse(Call<PendingServicesResponse> call, Response<PendingServicesResponse> response) {
                Log.d(TAG, "API response received");

                if (response.isSuccessful() && response.body() != null) {
                    PendingServicesResponse apiResponse = response.body();

                    // Log the response data
                    Log.d(TAG, "Response Body: " + apiResponse.getData().toString());

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Update the pending service list with the response data
                        pendingServiceList.addAll(apiResponse.getData());
                        pendingServicesAdapter.notifyDataSetChanged();

                        Log.d(TAG, "Pending services loaded successfully, count: " + pendingServiceList.size());
                    } else {
                        // Handle unsuccessful response
                        Log.e(TAG, "Error: " + apiResponse.getMessage());
                        Toast.makeText(PendingServicesActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the error code and message
                    Log.e(TAG, "Failed to load pending services, Response Code: " + response.code());
                    Toast.makeText(PendingServicesActivity.this, "Failed to load pending services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PendingServicesResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching pending services", t);
                Toast.makeText(PendingServicesActivity.this, "Error fetching pending services", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
