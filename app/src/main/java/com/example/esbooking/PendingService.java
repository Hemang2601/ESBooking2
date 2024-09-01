package com.example.esbooking;

import java.util.List;

public class PendingService {
    private int id;
    private List<String> service_ids;
    private String appointment_date;
    private String appointment_time;
    private String status;
    private String service_names;

    public PendingService(int id, List<String> service_ids, String appointment_date, String appointment_time, String status, String service_names) {
        this.id = id;
        this.service_ids = service_ids;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
        this.service_names = service_names;
    }

    public int getId() {
        return id;
    }

    public List<String> getServiceIds() {
        return service_ids;
    }

    public String getAppointmentDate() {
        return appointment_date;
    }

    public String getAppointmentTime() {
        return appointment_time;
    }

    public String getStatus() {
        return status;
    }

    public String getServiceNames() {
        return service_names;
    }

    // Optionally add setters if needed
}
