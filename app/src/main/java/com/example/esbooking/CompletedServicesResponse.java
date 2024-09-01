package com.example.esbooking;

import java.util.List;

public class CompletedServicesResponse {
    private int id;
    private List<Integer> service_ids;
    private String appointment_date;
    private String appointment_time;
    private String status;
    private String service_names;

    // Getters and setters
    public int getId() { return id; }
    public List<Integer> getServiceIds() { return service_ids; }
    public String getAppointmentDate() { return appointment_date; }
    public String getAppointmentTime() { return appointment_time; }
    public String getStatus() { return status; }
    public String getServiceNames() { return service_names; }
}
