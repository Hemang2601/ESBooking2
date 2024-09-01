package com.example.esbooking;

import java.util.List;

public class ApiResponse {
    private boolean success;
    private String message;
    private List<AppointmentService> data;

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<AppointmentService> getData() { return data; }
    public void setData(List<AppointmentService> data) { this.data = data; }
}
