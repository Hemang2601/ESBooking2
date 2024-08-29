package com.example.esbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillingViewHolder> {

    private final List<BillingItem> billingList;

    public BillingAdapter(List<BillingItem> billingList) {
        this.billingList = billingList;
    }

    @NonNull
    @Override
    public BillingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_billing, parent, false);
        return new BillingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        BillingItem item = billingList.get(position);
        holder.serviceName.setText(item.getServiceName());
        holder.amount.setText(item.getAmount());
        holder.status.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return billingList.size();
    }

    public static class BillingViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceName;
        public TextView amount;
        public TextView status;

        public BillingViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
        }
    }
}
