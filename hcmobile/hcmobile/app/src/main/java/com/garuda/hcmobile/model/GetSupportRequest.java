package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kharda on 26-Jan-16.
 */
public class GetSupportRequest {
    @SerializedName("no_hp")
    private String phoneNumber;
    @SerializedName("utype")
    private int utype;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("message")
    private String message;

    public GetSupportRequest(String phoneNumber, int utype, String dateTime, String message) {
        this.phoneNumber = phoneNumber;
        this.utype = utype;
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUtype() {
        return utype;
    }

    public void setUtype(int utype) {
        this.utype = utype;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}