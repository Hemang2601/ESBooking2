package com.example.esbooking;

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

public class BookNewServiceActivity extends AppCompatActivity {

    private static final String TAG = "BookNewServiceActivity"; // Tag for logging
    private String userId;
    private String username;
    private RecyclerView availableServicesRecyclerView;
    private AppointmentServicesAdapter appointmentServicesAdapter;
    private List<AppointmentService> appointmentServiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_service);

        // Initialize the back arrow button and set the onClick listener
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Navigate back to the previous activity
            }
        });

        // Retrieve user_id and username from the intent
        userId = getIntent().getStringExtra("user_id");
        username = getIntent().getStringExtra("username");
        Log.d(TAG, "User ID: " + userId);
        Log.d(TAG, "Username: " + username);

        // Initialize the RecyclerView for available services
        availableServicesRecyclerView = findViewById(R.id.availableServicesRecyclerView);
        availableServicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the appointment service list and adapter
        appointmentServiceList = new ArrayList<>();
        appointmentServicesAdapter = new AppointmentServicesAdapter(appointmentServiceList);
        availableServicesRecyclerView.setAdapter(appointmentServicesAdapter);

        // Load services from the API
        loadServicesFromApi();
    }

    private void loadServicesFromApi() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ApiResponse> call = apiService.getServices();  // Updated to Call<ApiResponse>

        Log.d(TAG, "API call to get services started");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d(TAG, "API response received");

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();

                    // Log the response data
                    Log.d(TAG, "Response Body: " + apiResponse.getData().toString());

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Update the appointment service list with the response data
                        appointmentServiceList.addAll(apiResponse.getData());
                        appointmentServicesAdapter.notifyDataSetChanged();

                        Log.d(TAG, "Services loaded successfully, count: " + appointmentServiceList.size());
                    } else {
                        // Handle unsuccessful response
                        Log.e(TAG, "Error: " + apiResponse.getMessage());
                        Toast.makeText(BookNewServiceActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the error code and message
                    Log.e(TAG, "Failed to load services, Response Code: " + response.code());
                    Toast.makeText(BookNewServiceActivity.this, "Failed to load services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching services", t);
                Toast.makeText(BookNewServiceActivity.this, "Error fetching services", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
