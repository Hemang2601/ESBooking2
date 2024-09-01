package com.example.esbooking;

public class AppointmentService {
    private String service_id;
    private String service_name;
    private String description;
    private String price;

    // Getters and setters
    public String getServiceId() {
        return service_id;
    }

    public void setServiceId(String service_id) {
        this.service_id = service_id;
    }

    public String getServiceName() {
        return service_name;
    }

    public void setServiceName(String service_name) {
        this.service_name = service_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
