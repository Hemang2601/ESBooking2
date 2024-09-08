package com.example.esbooking;

public class BillingResponse {
    private int appointment_id;
    private String amount;
    private String payment_status;
    private String service_names;
    private String appointment_date;
    private String appointment_time;

    // Getters and setters
    public int getAppointmentId() { return appointment_id; }
    public void setAppointmentId(int appointment_id) { this.appointment_id = appointment_id; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getPaymentStatus() { return payment_status; }
    public void setPaymentStatus(String payment_status) { this.payment_status = payment_status; }

    public String getServiceNames() { return service_names; }
    public void setServiceNames(String service_names) { this.service_names = service_names; }

    public String getAppointmentDate() { return appointment_date; }
    public void setAppointmentDate(String appointment_date) { this.appointment_date = appointment_date; }

    public String getAppointmentTime() { return appointment_time; }
    public void setAppointmentTime(String appointment_time) { this.appointment_time = appointment_time; }
}
