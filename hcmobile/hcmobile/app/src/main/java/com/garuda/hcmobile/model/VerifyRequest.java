package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class VerifyRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("otp")
    private String otp;
    @SerializedName("email")
    private String email;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;

    public VerifyRequest(String device_type,String device_id, String otp,String email,String nopeg, String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.otp = otp;
        this.email=email;
        this.nopeg=nopeg;
        this.dateTime = dateTime;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
