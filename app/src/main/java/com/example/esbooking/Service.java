package com.example.esbooking;

public class Service {
    private String name;
    private String status;

    public Service(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
