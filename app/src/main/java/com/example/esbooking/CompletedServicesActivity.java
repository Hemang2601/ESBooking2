package com.example.esbooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
    private ProgressDialog progressDialog; // Progress dialog variable

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
        // Initialize the ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progress_dialog_message1)); // Make sure to add this string to your strings.xml
        progressDialog.setCancelable(false);
        progressDialog.show(); // Show the progress dialog

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<CompletedServicesResponseWrapper> call = apiService.getCompletedServices(userId);

        Log.d(TAG, "API call to get completed services started");

        call.enqueue(new Callback<CompletedServicesResponseWrapper>() {
            @Override
            public void onResponse(Call<CompletedServicesResponseWrapper> call, Response<CompletedServicesResponseWrapper> response) {
                Log.d(TAG, "API response received");

                // Dismiss the progress dialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful() && response.body() != null) {
                    CompletedServicesResponseWrapper apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                        // Show RecyclerView and hide "Data Not Found" card
                        findViewById(R.id.completedServicesRecyclerView).setVisibility(View.VISIBLE);
                        findViewById(R.id.dataNotFoundCard).setVisibility(View.GONE);

                        // Update the completed service list with the response data
                        completedServiceList.clear();
                        completedServiceList.addAll(apiResponse.getData());
                        completedServicesAdapter.notifyDataSetChanged();
                    } else {
                        // Hide RecyclerView and show "Data Not Found" card
                        findViewById(R.id.completedServicesRecyclerView).setVisibility(View.GONE);
                        findViewById(R.id.dataNotFoundCard).setVisibility(View.VISIBLE);
                    }
                } else {
                    // Log the error code and message
                    Log.e(TAG, "Failed to load completed services. Response Code: " + response.code());

                }
            }

            @Override
            public void onFailure(Call<CompletedServicesResponseWrapper> call, Throwable t) {
                // Dismiss the progress dialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Log.e(TAG, "Error fetching completed services", t);

                // Show an AlertDialog suggesting the user restart the app
                new androidx.appcompat.app.AlertDialog.Builder(CompletedServicesActivity.this)
                        .setTitle("Error")
                        .setMessage("A problem occurred with the API or network. Please restart the application.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Restart the app by launching the main activity
                            Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(restartIntent);
                            finish(); // Close the current activity
                        })
                        .setCancelable(false) // Prevents the user from dismissing the dialog without pressing the OK button
                        .show();
            }

        });
    }
}
