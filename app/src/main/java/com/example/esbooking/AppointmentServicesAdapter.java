package com.example.esbooking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

public class AppointmentServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_SERVICE = 0;
    private static final int ITEM_VIEW_TYPE_FOOTER = 1;

    private List<AppointmentService> appointmentServiceList;
    private List<AppointmentService> selectedServices = new ArrayList<>();

    public AppointmentServicesAdapter(List<AppointmentService> appointmentServiceList) {
        this.appointmentServiceList = appointmentServiceList;
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
                } else {
                    selectedServices.remove(appointmentService);
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
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }


    private void handleAddAppointment(FooterViewHolder holder) {
        if (holder.termsCheckbox.isChecked()) {
            // Handle adding appointment
            // You can also get the selected date and time here if needed
            String selectedDate = holder.selectDateButton.getText().toString();
            String selectedTime = holder.selectTimeButton.getText().toString();

            // Add appointment logic here (e.g., save to database, send network request)

            Toast.makeText(holder.itemView.getContext(), "Appointment added for " + selectedDate + " at " + selectedTime, Toast.LENGTH_SHORT).show();
        } else {
            // Show a message to accept terms and conditions
            Toast.makeText(holder.itemView.getContext(), "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
        }
    }

    public class AppointmentServiceViewHolder extends RecyclerView.ViewHolder {
        CheckBox serviceCheckbox;
        TextView serviceName;
        TextView serviceDescription;
        TextView servicePrice;

        public AppointmentServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceCheckbox = itemView.findViewById(R.id.serviceCheckbox);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        Button selectDateButton;
        Button selectTimeButton;
        CheckBox termsCheckbox;
        Button addAppointmentButton;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            selectDateButton = itemView.findViewById(R.id.selectDateButton);
            selectTimeButton = itemView.findViewById(R.id.selectTimeButton);
            termsCheckbox = itemView.findViewById(R.id.termsCheckbox);
            addAppointmentButton = itemView.findViewById(R.id.addAppointmentButton);
        }
    }
}
