package com.example.esbooking;

import java.util.List;

public class CompletedServicesResponseWrapper {
    private boolean success;
    private String message;
    private List<CompletedServicesResponse> data;

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CompletedServicesResponse> getData() {
        return data;
    }

    public void setData(List<CompletedServicesResponse> data) {
        this.data = data;
    }
}
