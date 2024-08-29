package com.example.esbooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity"; // Log tag

    private TextInputLayout emailInputLayout;
    private EditText emailEditText;
    private TextInputLayout usernameInputLayout;
    private EditText usernameEditText;
    private TextInputLayout passwordInputLayout;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView loginLink;
    private CardView registerCardView;
    private ProgressBar spinnerLoader; // Add this line

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize views
        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        usernameInputLayout = findViewById(R.id.usernameInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        registerCardView = findViewById(R.id.registerCardView);
        spinnerLoader = findViewById(R.id.spinnerLoader); // Initialize ProgressBar

        // Set onClickListener for registerButton
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from EditText fields
                String email = emailEditText.getText().toString();
                String username = usernameEditText.getText().toString().toUpperCase();
                String password = passwordEditText.getText().toString();

                // Check if any field is empty
                if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    // Show a Toast indicating that all fields are required
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return; // Abort registration if any field is empty
                }

                // Check if the email format is correct
                if (!isValidEmail(email)) {
                    // Show a Toast indicating that the email format is incorrect
                    Toast.makeText(RegisterActivity.this, "Invalid email ", Toast.LENGTH_SHORT).show();
                    return; // Abort registration if the email format is incorrect
                }

                // Hide the CardView and show the ProgressBar
                registerCardView.setVisibility(View.GONE);
                spinnerLoader.setVisibility(View.VISIBLE);

                // Create a User object
                User user = new User(email, username, password);

                // Log the data being sent
                Log.d(TAG, "Registering user with data: " + user.toString());

                // Make the API call using Retrofit
                registerUser(user);
            }
        });

        // Set onClickListener for loginLink
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login activity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Optional: Close the current activity
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void registerUser(User user) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<ResponseBody> call = apiService.registerUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Hide the ProgressBar and show the CardView
                spinnerLoader.setVisibility(View.GONE);
                registerCardView.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Registration response: " + responseBody);

                        // Check if the response indicates that email already exists
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            // Registration successful, show success alert
                            showCustomAlertDialog("Registration Successful", "You have successfully registered.");
                        } else {
                            // Registration failed due to email already existing, show Toast
                            String message = jsonResponse.getString("message");
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                            // Redirect to the MainActivity after a short delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();  // Close the current activity
                                }
                            }, 000); // 2 seconds delay for user to read the Toast
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Registration failed for other reasons, handle the response accordingly
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Registration failed. Error response: " + errorBody);
                        // Handle error response as needed
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Hide the ProgressBar and show the CardView
                spinnerLoader.setVisibility(View.GONE);
                registerCardView.setVisibility(View.VISIBLE);

                // Handle failure, such as network issues
                Log.e(TAG, "Registration failed. Network error: " + t.getMessage());
            }
        });
    }


    private void showCustomAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setView(customView);

        // Set a transparent background
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Set a positive button
        Button okButton = customView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                alertDialog.dismiss();

                // Redirect to MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Optional: Close the current activity
            }
        });

        // Create and show the AlertDialog with fade-in animation
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        // Navigate to the login activity
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
