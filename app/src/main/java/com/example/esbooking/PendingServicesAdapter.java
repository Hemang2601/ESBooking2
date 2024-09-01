package com.example.esbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendingServicesAdapter extends RecyclerView.Adapter<PendingServicesAdapter.ViewHolder> {

    private List<PendingService> pendingServices;

    public PendingServicesAdapter(List<PendingService> pendingServices) {
        this.pendingServices = pendingServices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PendingService service = pendingServices.get(position);


        holder.noLabelTextView.setText("No: " + (position + 1));
        holder.serviceNameTextView.setText("Service: " + service.getServiceNames());
        holder.serviceStatusTextView.setText("Status: " + service.getStatus());
        holder.appointmentDateTextView.setText("Date: " + service.getAppointmentDate());
        holder.appointmentTimeTextView.setText("Time: " + service.getAppointmentTime());
    }

    @Override
    public int getItemCount() {
        return pendingServices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noLabelTextView;
        TextView serviceNameTextView;
        TextView serviceStatusTextView;
        TextView appointmentDateTextView;
        TextView appointmentTimeTextView;

        Button cancelButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noLabelTextView = itemView.findViewById(R.id.noLabelTextView); // Ensure this ID matches your XML
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            serviceStatusTextView = itemView.findViewById(R.id.serviceStatusTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            appointmentTimeTextView = itemView.findViewById(R.id.appointmentTimeTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}
