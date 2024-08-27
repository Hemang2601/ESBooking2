package com.example.esbooking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class BookNewServiceActivity extends AppCompatActivity {

    private String userId;
    private String username;

    // Array of service EditTexts, quantities, and CheckBoxes
    private EditText[] serviceNameEditTexts = new EditText[10];
    private EditText[] serviceQtyEditTexts = new EditText[10];
    private CheckBox[] serviceCheckBoxes = new CheckBox[10];

    private EditText dateEditText;
    private EditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_new_service);

        // Retrieve user_id and username from the intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        username = intent.getStringExtra("username");

        // Initialize UI elements
        Button bookServiceButton = findViewById(R.id.bookServiceButton);
        Button backToAppointmentsButton = findViewById(R.id.backToAppointmentsButton);
        Button selectDateButton = findViewById(R.id.selectDateButton);
        Button selectTimeButton = findViewById(R.id.selectTimeButton);



        // Initialize service EditTexts, quantities, and CheckBoxes
        for (int i = 0; i < 10; i++) {
            int nameId = getResources().getIdentifier("serviceNameEditText" + (i + 1), "id", getPackageName());
            int qtyId = getResources().getIdentifier("serviceQtyEditText" + (i + 1), "id", getPackageName());
            int checkBoxId = getResources().getIdentifier("serviceCheckBox" + (i + 1), "id", getPackageName());

            serviceNameEditTexts[i] = findViewById(nameId);
            serviceQtyEditTexts[i] = findViewById(qtyId);
            serviceCheckBoxes[i] = findViewById(checkBoxId);
        }

        // Set onClick listeners for the date and time selection buttons
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        bookServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBooking();
            }
        });

        backToAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAppointments = new Intent(BookNewServiceActivity.this, AppointmentsActivity.class);
                intentToAppointments.putExtra("user_id", userId);
                intentToAppointments.putExtra("username", username);
                startActivity(intentToAppointments);
                finish(); // Close this activity and return to the AppointmentsActivity
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    dateEditText.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minute1);
                    timeEditText.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void handleBooking() {
        boolean allFieldsFilled = true;
        StringBuilder bookedServices = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            String serviceName = serviceNameEditTexts[i].getText().toString().trim();
            String serviceQty = serviceQtyEditTexts[i].getText().toString().trim();
            boolean isChecked = serviceCheckBoxes[i].isChecked();

            if (isChecked) {
                if (serviceName.isEmpty() || serviceQty.isEmpty()) {
                    allFieldsFilled = false;
                    break;
                }
                bookedServices.append("Service ").append(i + 1).append(": ").append(serviceName)
                        .append(", Qty: ").append(serviceQty).append("\n");
            }
        }

        if (allFieldsFilled) {
            Log.d("BookNewServiceActivity", "Services booked:\n" + bookedServices.toString());
            Toast.makeText(BookNewServiceActivity.this, "Services booked successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity and return to the previous one
        } else {
            Toast.makeText(BookNewServiceActivity.this, "Please fill all fields for selected services", Toast.LENGTH_SHORT).show();
        }
    }
}
