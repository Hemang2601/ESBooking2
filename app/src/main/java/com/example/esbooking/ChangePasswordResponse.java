package com.example.esbooking;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // Constructor
    public ChangePasswordResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getter for success
    public boolean isSuccess() {
        return success;
    }

    // Setter for success
    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }
}
