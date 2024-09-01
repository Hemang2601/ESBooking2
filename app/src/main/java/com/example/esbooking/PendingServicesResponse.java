package com.example.esbooking;

import java.util.List;

public class PendingServicesResponse {
    private boolean success;
    private String message;
    private List<PendingService> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<PendingService> getData() {
        return data;
    }
}
