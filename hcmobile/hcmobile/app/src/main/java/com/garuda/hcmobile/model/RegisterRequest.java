package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;


public class RegisterRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("msisdn")
    private String msisdn;
    @SerializedName("email")
    private String email;
    @SerializedName("gbdat")
    private String gbdat;
    @SerializedName("date_time")
    private String dateTime;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RegisterRequest(String device_type, String device_id, String nopeg, String msisdn, String email, String gbdat, String dateTime) {
        this.device_type = device_type;
        this.device_id=device_id;
        this.nopeg = nopeg;
        this.msisdn = msisdn;
        this.email=email;
        this.gbdat = gbdat;
        this.dateTime = dateTime;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getNopeg() {
        return nopeg;
    }

    public void setNopeg(String nopeg) {
        this.nopeg = nopeg;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getGbdat() {
        return gbdat;
    }

    public void setGbdat(String gbdat) {
        this.gbdat = gbdat;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


}
