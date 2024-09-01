package com.example.esbooking;

public class AppointmentRequest {
    private String userId;
    private String serviceIds; // Changed to String for comma-separated IDs
    private String date;
    private String time;

    public AppointmentRequest(String userId, String serviceIds, String date, String time) {
        this.userId = userId;
        this.serviceIds = serviceIds;
        this.date = date;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "AppointmentRequest{" +
                "userId='" + userId + '\'' +
                ", serviceIds='" + serviceIds + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
