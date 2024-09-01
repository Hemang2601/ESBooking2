package com.example.esbooking;

public class AppointmentRequest {
    private String userId;
    private String serviceId;
    private String date;
    private String time;

    public AppointmentRequest(String userId, String serviceId, String date, String time) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
