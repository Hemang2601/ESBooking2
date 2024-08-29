package com.example.esbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class BookNewServiceActivity extends AppCompatActivity {

    private String userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_service);

        // Initialize the back arrow button and set the onClick listener
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity
                finish();
            }
        });
        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");
    }
}
