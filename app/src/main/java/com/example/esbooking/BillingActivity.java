package com.example.esbooking;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BillingActivity extends AppCompatActivity {

    private RecyclerView billingRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        // Initialize the back arrow button and set the onClick listener
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity
                finish();
            }
        });

        // Initialize RecyclerView
        billingRecyclerView = findViewById(R.id.billingRecyclerView);
        billingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView with an adapter
        // Replace with your own adapter and data
        BillingAdapter billingAdapter = new BillingAdapter(getBillingData());
        billingRecyclerView.setAdapter(billingAdapter);
    }

    // Method to provide demo billing data
    private List<BillingItem> getBillingData() {
        List<BillingItem> billingData = new ArrayList<>();
        // Add demo data here
        billingData.add(new BillingItem("Service 1", "$100", "Paid"));
        billingData.add(new BillingItem("Service 2", "$150", "Not Paid"));
        // Add more items as needed
        return billingData;
    }
}
