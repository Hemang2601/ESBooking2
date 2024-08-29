package com.example.esbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendingServicesAdapter extends RecyclerView.Adapter<PendingServicesAdapter.ViewHolder> {

    private List<Service> servicesList;

    public PendingServicesAdapter(List<Service> servicesList) {
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = servicesList.get(position);
        holder.serviceNameTextView.setText(service.getName());
        holder.serviceStatusTextView.setText(service.getStatus());
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView;
        TextView serviceStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            serviceStatusTextView = itemView.findViewById(R.id.serviceStatusTextView);
        }
    }
}
