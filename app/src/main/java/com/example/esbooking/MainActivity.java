package com.example.esbooking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout;
    private EditText emailEditText;
    private TextInputLayout passwordInputLayout;
    private EditText passwordEditText;
    private TextView forgotPasswordTextView;
    private Button loginButton;
    private TextView registerTextView;
    private CardView cardView;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_USER_LOGGED_IN = "userLoggedIn";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initializeViews();

        // Check if the user is already logged in
        if (isUserLoggedIn()) {
            redirectToDashboard();
        }

        // Set onClickListener for loginButton
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInput(email, password)) {
                loginUser(email, password);
            } else {
                Toast.makeText(MainActivity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initializeViews() {
        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        cardView = findViewById(R.id.cardView);
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }
        // Basic email validation (can be enhanced further)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setLoggedInStatus(boolean isLoggedIn, String userId, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_USER_LOGGED_IN, isLoggedIn);
        if (isLoggedIn) {
            editor.putString(KEY_USER_ID, userId);
            editor.putString(KEY_USERNAME, username);
        }
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_USER_LOGGED_IN, false);
    }

    private void redirectToDashboard() {
        Intent intent = new Intent(MainActivity.this, BookingDashboardActivity.class);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userId = prefs.getString(KEY_USER_ID, "");
        String username = prefs.getString(KEY_USERNAME, "");
        intent.putExtra("user_id", userId);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    private void loginUser(String email, String password) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(new LoginRequest(email, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                handleLoginResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginError", "Error occurred: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginResponse(Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = response.body();
            if (loginResponse != null && loginResponse.isSuccess()) {
                Log.d("LoginResponse", "Response: " + new Gson().toJson(loginResponse));
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                setLoggedInStatus(true, String.valueOf(loginResponse.getUserId()), loginResponse.getUsername());
                redirectToDashboard();
            } else {
                handleUnsuccessfulLogin(loginResponse);
            }
        } else {
            Log.e("LoginResponse", "Unsuccessful Response Code: " + response.code());
            Toast.makeText(MainActivity.this, "Login Failed: Unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUnsuccessfulLogin(LoginResponse loginResponse) {
        if (loginResponse != null) {
            String message = loginResponse.getMessage();
            Toast.makeText(MainActivity.this, "Login Failed: " + (message != null && !message.isEmpty() ? message : "Unknown error"), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Login Failed: Unknown error", Toast.LENGTH_SHORT).show();
        }
    }
}
