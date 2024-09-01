package com.example.esbooking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompletedServicesAdapter extends RecyclerView.Adapter<CompletedServicesAdapter.ViewHolder> {

    private static final String TAG = "CompletedServicesAdapter"; // Updated TAG
    private List<CompletedServicesResponse> completedServices;
    private Context context;

    public CompletedServicesAdapter(Context context, List<CompletedServicesResponse> completedServices) {
        this.context = context;
        this.completedServices = completedServices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompletedServicesResponse service = completedServices.get(position);

        // Log the details for debugging
        Log.d(TAG, "Binding data for position " + position);
        Log.d(TAG, "Service Name: " + (service.getServiceNames() != null ? service.getServiceNames() : "Not Available"));
        Log.d(TAG, "Status: " + (service.getStatus() != null ? service.getStatus() : "Not Available"));
        Log.d(TAG, "Appointment Date: " + (service.getAppointmentDate() != null ? service.getAppointmentDate() : "Not Available"));
        Log.d(TAG, "Appointment Time: " + (service.getAppointmentTime() != null ? service.getAppointmentTime() : "Not Available"));

        holder.noLabelTextView.setText("No: " + (position + 1));
        holder.serviceNameTextView.setText("Service: " + (service.getServiceNames() != null ? service.getServiceNames() : "No Service Name"));
        holder.serviceStatusTextView.setText("Status: " + (service.getStatus() != null ? service.getStatus() : "No Status"));
        holder.appointmentDateTextView.setText("Date: " + (service.getAppointmentDate() != null ? service.getAppointmentDate() : "No Date"));
        holder.appointmentTimeTextView.setText("Time: " + (service.getAppointmentTime() != null ? service.getAppointmentTime() : "No Time"));
    }

    @Override
    public int getItemCount() {
        return completedServices != null ? completedServices.size() : 0; // Check if list is not null
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noLabelTextView;
        TextView serviceNameTextView;
        TextView serviceStatusTextView;
        TextView appointmentDateTextView;
        TextView appointmentTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noLabelTextView = itemView.findViewById(R.id.noLabelTextView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            serviceStatusTextView = itemView.findViewById(R.id.serviceStatusTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            appointmentTimeTextView = itemView.findViewById(R.id.appointmentTimeTextView);
        }
    }
}
