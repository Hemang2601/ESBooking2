package com.example.esbooking;

public class BillingItem {
    private final String serviceName;
    private final String amount;
    private final String status;

    public BillingItem(String serviceName, String amount, String status) {
        this.serviceName = serviceName;
        this.amount = amount;
        this.status = status;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
