package com.example.esbooking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_SERVICE = 0;
    private static final int ITEM_VIEW_TYPE_FOOTER = 1;
    private static final String TAG = "AppointmentServicesAdapter";

    private List<AppointmentService> appointmentServiceList;
    private List<AppointmentService> selectedServices = new ArrayList<>();
    private ApiService apiService;
    private String userId; // Add this field



    public AppointmentServicesAdapter(List<AppointmentService> appointmentServiceList, String userId) {
        this.appointmentServiceList = appointmentServiceList;
        this.userId = userId; // Initialize userId
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Log.d(TAG, "ApiService initialized");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_SERVICE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
            return new AppointmentServiceViewHolder(view);
        } else { // ITEM_VIEW_TYPE_FOOTER
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_card, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_VIEW_TYPE_SERVICE) {
            // Bind data for service item
            AppointmentServiceViewHolder serviceHolder = (AppointmentServiceViewHolder) holder;
            AppointmentService appointmentService = appointmentServiceList.get(position);

            serviceHolder.serviceName.setText(appointmentService.getServiceName());
            serviceHolder.serviceDescription.setText(appointmentService.getDescription());
            serviceHolder.servicePrice.setText("₹" + appointmentService.getPrice());

            serviceHolder.serviceCheckbox.setOnCheckedChangeListener(null);
            serviceHolder.serviceCheckbox.setChecked(selectedServices.contains(appointmentService));

            serviceHolder.serviceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedServices.add(appointmentService);
                    Log.d(TAG, "Service selected: " + appointmentService.getServiceName());
                } else {
                    selectedServices.remove(appointmentService);
                    Log.d(TAG, "Service deselected: " + appointmentService.getServiceName());
                }
            });
        } else { // FooterViewHolder
            FooterViewHolder footerHolder = (FooterViewHolder) holder;

            footerHolder.selectDateButton.setOnClickListener(v -> showDatePicker(footerHolder));
            footerHolder.selectTimeButton.setOnClickListener(v -> showCustomHourPicker(footerHolder));
            footerHolder.addAppointmentButton.setOnClickListener(v -> handleAddAppointment(footerHolder));
        }
    }

    @Override
    public int getItemCount() {
        return appointmentServiceList.size() + 1; // Add 1 for the footer view
    }

    @Override
    public int getItemViewType(int position) {
        return position < appointmentServiceList.size() ? ITEM_VIEW_TYPE_SERVICE : ITEM_VIEW_TYPE_FOOTER;
    }

    public List<AppointmentService> getSelectedServices() {
        return selectedServices;
    }

    private void showDatePicker(FooterViewHolder holder) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                holder.itemView.getContext(),
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                    holder.selectDateButton.setText(date);
                    Log.d(TAG, "Selected date: " + date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showCustomHourPicker(FooterViewHolder holder) {
        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
        View view = inflater.inflate(R.layout.dialog_hour_picker, null);
        NumberPicker hourPicker = view.findViewById(R.id.hourPicker);

        // Define time range from 9 AM to 7 PM
        int startHour = 9; // 9 AM
        int endHour = 19; // 7 PM
        int hourRange = endHour - startHour + 1;

        // Set min and max values
        hourPicker.setMinValue(startHour);
        hourPicker.setMaxValue(endHour);

        // Set displayed values to be in the format of hours with AM/PM
        String[] hours = new String[hourRange];
        for (int i = 0; i < hourRange; i++) {
            int hour = startHour + i;
            String formattedHour = hour < 12 ? String.format("%02d:00 AM", hour) :
                    hour == 12 ? "12:00 PM" :
                            String.format("%02d:00 PM", hour - 12);
            hours[i] = formattedHour;
        }
        hourPicker.setDisplayedValues(hours);

        // Set initial value to current hour, converted to the 12-hour format if necessary
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (currentHour >= startHour && currentHour <= endHour) {
            hourPicker.setValue(currentHour);
        } else {
            hourPicker.setValue(startHour); // Default to 9 AM if current hour is out of range
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Select Hour")
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedHour = hourPicker.getValue();
                    String time = selectedHour < 12 ? String.format("%02d:00 AM", selectedHour) :
                            selectedHour == 12 ? "12:00 PM" :
                                    String.format("%02d:00 PM", selectedHour - 12);
                    holder.selectTimeButton.setText(time);
                    Log.d(TAG, "Selected time: " + time);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void handleAddAppointment(FooterViewHolder holder) {
        if (holder.termsCheckbox.isChecked()) {
            // Handle adding appointment
            String selectedDate = holder.selectDateButton.getText().toString();
            String selectedTime = holder.selectTimeButton.getText().toString();

            if (selectedServices.isEmpty()) {
                Toast.makeText(holder.itemView.getContext(), "Please select at least one service", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No services selected");
                return;
            }

            // Retrieve userId from SharedPreferences
            SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("user_id", null);
            String username = sharedPreferences.getString("username", null);

            if (userId == null) {
                Toast.makeText(holder.itemView.getContext(), "User ID is missing", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "User ID is missing");
                return;
            }

            // Collect selected service IDs into a comma-separated string
            StringBuilder serviceIdsBuilder = new StringBuilder();
            for (AppointmentService service : selectedServices) {
                if (serviceIdsBuilder.length() > 0) {
                    serviceIdsBuilder.append(",");
                }
                serviceIdsBuilder.append(service.getServiceId());
            }
            String serviceIdsString = serviceIdsBuilder.toString();

            // Log the appointment details before sending
            Log.d(TAG, "User id : " + userId);
            Log.d(TAG, "Adding appointments with details:");
            Log.d(TAG, "Selected Date: " + selectedDate);
            Log.d(TAG, "Selected Time: " + selectedTime);
            Log.d(TAG, "Selected Service IDs: " + serviceIdsString);

            // Create a single appointment request for all services
            AppointmentRequest appointmentRequest = new AppointmentRequest(
                    userId,
                    serviceIdsString, // Passing the comma-separated string
                    selectedDate,
                    selectedTime
            );

            // Log appointment request details
            Log.d(TAG, "Appointment request: " + appointmentRequest.toString());

            // Make API call to book appointment
            Call<AppointmentResponse> call = apiService.bookAppointment(appointmentRequest);
            call.enqueue(new Callback<AppointmentResponse>() {
                @Override
                public void onResponse(Call<AppointmentResponse> call, Response<AppointmentResponse> response) {
                    if (response.isSuccessful()) {
                        AppointmentResponse appointmentResponse = response.body();
                        if (appointmentResponse != null && appointmentResponse.isSuccess()) {
                            // Successful booking response
                            Toast.makeText(holder.itemView.getContext(), "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Appointment successfully booked. Response: " + appointmentResponse.toString());

                            // Start AppointmentsActivity with user_id and username
                            Intent intent = new Intent(holder.itemView.getContext(), AppointmentsActivity.class);
                            intent.putExtra("user_id", userId);
                            intent.putExtra("username", username);

                            // Log the user_id and username for debugging purposes
                            Log.d("AppointmentsActivity", "Starting AppointmentsActivity with user_id: " + userId);
                            Log.d("AppointmentsActivity", "Starting AppointmentsActivity with username: " + username);

                            holder.itemView.getContext().startActivity(intent);

                        } else {
                            // Failure in booking appointment
                            Toast.makeText(holder.itemView.getContext(), appointmentResponse != null ? appointmentResponse.getMessage() : "Failed to book appointment. No details provided.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Failed to book appointment. Response: " + (appointmentResponse != null ? appointmentResponse.getMessage() : "No response message"));
                        }
                    } else {
                        // API error
                        Toast.makeText(holder.itemView.getContext(), "An error occurred while processing your request: " + response.message(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "API error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<AppointmentResponse> call, Throwable t) {
                    holder.availableServicesRecyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(holder.itemView.getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Network error: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(holder.itemView.getContext(), "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Terms and conditions not agreed");
        }
    }


    static class AppointmentServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView serviceDescription;
        TextView servicePrice;
        CheckBox serviceCheckbox;

        AppointmentServiceViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceCheckbox = itemView.findViewById(R.id.serviceCheckbox);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        Button selectDateButton;
        Button selectTimeButton;
        Button addAppointmentButton;
        CheckBox termsCheckbox;

        RecyclerView availableServicesRecyclerView;


        FooterViewHolder(View itemView) {
            super(itemView);
            selectDateButton = itemView.findViewById(R.id.selectDateButton);
            selectTimeButton = itemView.findViewById(R.id.selectTimeButton);
            addAppointmentButton = itemView.findViewById(R.id.addAppointmentButton);
            termsCheckbox = itemView.findViewById(R.id.termsCheckbox);

            availableServicesRecyclerView = itemView.findViewById(R.id.availableServicesRecyclerView);
        }
    }
}
