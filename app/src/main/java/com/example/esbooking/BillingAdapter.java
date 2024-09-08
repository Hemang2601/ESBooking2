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

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {

    private static final String TAG = "BillingAdapter";
    private List<BillingResponse> billingList;
    private Context context;

    public BillingAdapter(Context context, List<BillingResponse> billingList) {
        this.context = context;
        this.billingList = billingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillingResponse billing = billingList.get(position);

        // Log the details for debugging
        Log.d(TAG, "Binding data for position: " + position);
        Log.d(TAG, "Service Names: " + (billing.getServiceNames() != null ? billing.getServiceNames() : "Not Available"));
        Log.d(TAG, "Amount: " + (billing.getAmount() != null ? billing.getAmount() : "Not Available"));
        Log.d(TAG, "Payment Status: " + (billing.getPaymentStatus() != null ? billing.getPaymentStatus() : "Not Available"));
        Log.d(TAG, "Appointment Date: " + (billing.getAppointmentDate() != null ? billing.getAppointmentDate() : "Not Available"));
        Log.d(TAG, "Appointment Time: " + (billing.getAppointmentTime() != null ? billing.getAppointmentTime() : "Not Available"));

        // Setting values in the ViewHolder
        if (holder.noLabelTextView != null) {
            holder.noLabelTextView.setText("No: " + (position + 1));
        } else {
            Log.e(TAG, "No Label TextView is null");
        }

        if (holder.serviceNameTextView != null) {
            holder.serviceNameTextView.setText("Service: " + (billing.getServiceNames() != null ? billing.getServiceNames() : "No Service Name"));
        } else {
            Log.e(TAG, "Service Name TextView is null");
        }

        if (holder.amountTextView != null) {
            holder.amountTextView.setText("Amount: " + (billing.getAmount() != null ? billing.getAmount() : "No Amount"));
        } else {
            Log.e(TAG, "Amount TextView is null");
        }

        if (holder.paymentStatusTextView != null) {
            holder.paymentStatusTextView.setText("Status: " + (billing.getPaymentStatus() != null ? billing.getPaymentStatus() : "No Status"));
        } else {
            Log.e(TAG, "Payment Status TextView is null");
        }

        if (holder.appointmentDateTextView != null) {
            holder.appointmentDateTextView.setText("Date: " + (billing.getAppointmentDate() != null ? billing.getAppointmentDate() : "No Date"));
        } else {
            Log.e(TAG, "Appointment Date TextView is null");
        }

        if (holder.appointmentTimeTextView != null) {
            holder.appointmentTimeTextView.setText("Time: " + (billing.getAppointmentTime() != null ? billing.getAppointmentTime() : "No Time"));
        } else {
            Log.e(TAG, "Appointment Time TextView is null");
        }
    }

    @Override
    public int getItemCount() {
        return billingList != null ? billingList.size() : 0; // Return 0 if list is null
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noLabelTextView;
        TextView serviceNameTextView;
        TextView amountTextView;
        TextView paymentStatusTextView;
        TextView appointmentDateTextView; // New field
        TextView appointmentTimeTextView; // New field

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noLabelTextView = itemView.findViewById(R.id.noLabelTextView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            paymentStatusTextView = itemView.findViewById(R.id.paymentStatusTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView); // Initialize new field
            appointmentTimeTextView = itemView.findViewById(R.id.appointmentTimeTextView); // Initialize new field
        }
    }
}
