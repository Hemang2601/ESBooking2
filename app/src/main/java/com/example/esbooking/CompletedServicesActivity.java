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

public class CompletedServicesActivity extends AppCompatActivity {

    private static final String TAG = "CompletedServicesActivity"; // Tag for logging
    private String userId;
    private String username;
    private RecyclerView completedServicesRecyclerView;
    private CompletedServicesAdapter completedServicesAdapter;
    private List<CompletedServicesResponse> completedServiceList;

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

        // Initialize RecyclerView for completed services
        completedServicesRecyclerView = findViewById(R.id.completedServicesRecyclerView);
        completedServicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the completed service list and adapter
        completedServiceList = new ArrayList<>();
        completedServicesAdapter = new CompletedServicesAdapter(this, completedServiceList);
        completedServicesRecyclerView.setAdapter(completedServicesAdapter);

        // Load completed services from the API
        loadCompletedServices();
    }


    private void loadCompletedServices() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<CompletedServicesResponseWrapper> call = apiService.getCompletedServices(userId);

        Log.d(TAG, "API call to get completed services started");

        call.enqueue(new Callback<CompletedServicesResponseWrapper>() {
            @Override
            public void onResponse(Call<CompletedServicesResponseWrapper> call, Response<CompletedServicesResponseWrapper> response) {
                Log.d(TAG, "API response received");

                if (response.isSuccessful() && response.body() != null) {
                    CompletedServicesResponseWrapper apiResponse = response.body();

                    // Log the response data
                    Log.d(TAG, "Response Body: " + apiResponse.getData().toString());

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Update the completed service list with the response data
                        completedServiceList.clear(); // Clear any previous data
                        completedServiceList.addAll(apiResponse.getData());
                        completedServicesAdapter.notifyDataSetChanged();

                        Log.d(TAG, "Completed services loaded successfully, count: " + completedServiceList.size());
                    } else {
                        // Handle unsuccessful response
                        Log.e(TAG, "Error: " + apiResponse.getMessage());
                        Toast.makeText(CompletedServicesActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the error code and message
                    Log.e(TAG, "Failed to load completed services, Response Code: " + response.code());
                    Toast.makeText(CompletedServicesActivity.this, "Failed to load completed services. Response Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompletedServicesResponseWrapper> call, Throwable t) {
                Log.e(TAG, "Error fetching completed services", t);
                Toast.makeText(CompletedServicesActivity.this, "Error fetching completed services. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
