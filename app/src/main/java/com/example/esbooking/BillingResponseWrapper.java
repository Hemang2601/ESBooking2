package com.example.esbooking;

import java.util.List;

public class BillingResponseWrapper {

    private boolean success; // Indicates if the API call was successful
    private List<BillingResponse> data; // List of billing responses

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<BillingResponse> getData() {
        return data;
    }

    public void setData(List<BillingResponse> data) {
        this.data = data;
    }
}
