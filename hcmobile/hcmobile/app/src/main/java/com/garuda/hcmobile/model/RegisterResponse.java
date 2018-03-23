package com.garuda.hcmobile.model;


import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("otp_number")
    private String otp_number;

    public RegisterResponse(int status, String message, String otpNumber) {
        this.status = status;
        this.message = message;
        this.otp_number = otpNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtpNumber() {
        return otp_number;
    }

    public void setOtpNumber(String otpNumber) {
        this.otp_number = otpNumber;
    }
}
