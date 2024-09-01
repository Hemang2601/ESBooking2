package com.example.esbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentServicesAdapter extends RecyclerView.Adapter<AppointmentServicesAdapter.AppointmentServiceViewHolder> {

    private List<AppointmentService> appointmentServiceList;
    private List<AppointmentService> selectedServices = new ArrayList<>();

    public AppointmentServicesAdapter(List<AppointmentService> appointmentServiceList) {
        this.appointmentServiceList = appointmentServiceList;
    }

    @NonNull
    @Override
    public AppointmentServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new AppointmentServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentServiceViewHolder holder, int position) {
        // Get the current AppointmentService object
        AppointmentService appointmentService = appointmentServiceList.get(position);

        // Bind the data to the ViewHolder
        holder.serviceName.setText(appointmentService.getServiceName());
        holder.serviceDescription.setText(appointmentService.getDescription());
        holder.servicePrice.setText("₹" + appointmentService.getPrice());

        // Set the CheckBox state
        holder.serviceCheckbox.setOnCheckedChangeListener(null); // Remove any previous listeners
        holder.serviceCheckbox.setChecked(selectedServices.contains(appointmentService));

        // Add or remove the service from selectedServices list based on CheckBox state
        holder.serviceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedServices.add(appointmentService);
            } else {
                selectedServices.remove(appointmentService);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentServiceList.size();
    }

    public List<AppointmentService> getSelectedServices() {
        return selectedServices;
    }

    public class AppointmentServiceViewHolder extends RecyclerView.ViewHolder {
        CheckBox serviceCheckbox;
        TextView serviceName;
        TextView serviceDescription;
        TextView servicePrice;

        public AppointmentServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            serviceCheckbox = itemView.findViewById(R.id.serviceCheckbox);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
        }
    }
}
