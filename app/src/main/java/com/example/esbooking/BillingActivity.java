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

public class BillingActivity extends AppCompatActivity {

    private static final String TAG = "BillingActivity";
    private String userId;
    private String username;
    private RecyclerView billingRecyclerView;
    private BillingAdapter billingAdapter;
    private List<BillingResponse> billingList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        Log.d(TAG, "onCreate: Activity started");

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        Log.d(TAG, "onCreate: Retrieved user_id = " + userId + ", username = " + username);

        // Initialize the back arrow button and set the onClick listener
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        if (backArrowButton != null) {
            backArrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Back arrow clicked");
                    Intent intentToMain = new Intent(BillingActivity.this, MainActivity.class);
                    intentToMain.putExtra("user_id", userId);
                    intentToMain.putExtra("username", username);
                    startActivity(intentToMain);
                    finish();
                }
            });
        } else {
            Log.e(TAG, "onCreate: Back arrow button not found");
        }

        // Initialize RecyclerView for billing data
        billingRecyclerView = findViewById(R.id.billingRecyclerView);
        if (billingRecyclerView != null) {
            billingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Initialize the billing list and adapter
            billingList = new ArrayList<>();
            billingAdapter = new BillingAdapter(this, billingList);
            billingRecyclerView.setAdapter(billingAdapter);

            // Load billing data from the API
            loadBillingData();
        } else {
            Log.e(TAG, "onCreate: RecyclerView not found");
        }
    }

    private void loadBillingData() {
        // Initialize the ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progress_dialog_message2));
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d(TAG, "loadBillingData: API call started");

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<BillingResponseWrapper> call = apiService.getBillingData(userId);

        call.enqueue(new Callback<BillingResponseWrapper>() {
            @Override
            public void onResponse(Call<BillingResponseWrapper> call, Response<BillingResponseWrapper> response) {
                // Dismiss the progress dialog
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: Progress dialog dismissed");
                }

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: API call successful");

                    BillingResponseWrapper apiResponse = response.body();

                    if (apiResponse != null) {
                        Log.d(TAG, "onResponse: API response not null");
                        Log.d(TAG, "onResponse: Success = " + apiResponse.isSuccess());
                        Log.d(TAG, "onResponse: Data size = " + (apiResponse.getData() != null ? apiResponse.getData().size() : "0"));

                        if (apiResponse.isSuccess() && apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                            // Show RecyclerView and hide "Data Not Found" card
                            View recyclerView = findViewById(R.id.billingRecyclerView);
                            View dataNotFoundCard = findViewById(R.id.dataNotFoundCard);

                            if (recyclerView != null && dataNotFoundCard != null) {
                                recyclerView.setVisibility(View.VISIBLE);
                                dataNotFoundCard.setVisibility(View.GONE);

                                // Update the billing list with the response data
                                billingList.clear();
                                billingList.addAll(apiResponse.getData());
                                billingAdapter.notifyDataSetChanged();

                                Log.d(TAG, "onResponse: Billing data updated");
                            } else {
                                Log.e(TAG, "onResponse: RecyclerView or Data Not Found card not found");
                            }
                        } else {
                            // Hide RecyclerView and show "Data Not Found" card
                            View recyclerView = findViewById(R.id.billingRecyclerView);
                            View dataNotFoundCard = findViewById(R.id.dataNotFoundCard);

                            if (recyclerView != null && dataNotFoundCard != null) {
                                recyclerView.setVisibility(View.GONE);
                                dataNotFoundCard.setVisibility(View.VISIBLE);

                                Log.d(TAG, "onResponse: No data found");
                            } else {
                                Log.e(TAG, "onResponse: RecyclerView or Data Not Found card not found");
                            }
                        }
                    } else {
                        Log.e(TAG, "onResponse: API response body is null");
                    }
                } else {
                    Log.e(TAG, "onResponse: Failed to load billing data. Response Code: " + response.code());
                    Log.e(TAG, "onResponse: Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BillingResponseWrapper> call, Throwable t) {
                // Dismiss the progress dialog
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: Progress dialog dismissed");
                }

                Log.e(TAG, "onFailure: Error fetching billing data", t);

                // Show an AlertDialog suggesting the user restart the app
                new androidx.appcompat.app.AlertDialog.Builder(BillingActivity.this)
                        .setTitle("Error")
                        .setMessage("A problem occurred with the API or network. Please restart the application.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            if (restartIntent != null) {
                                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(restartIntent);
                                finish();
                            } else {
                                Log.e(TAG, "onFailure: Restart intent is null");
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }
}
