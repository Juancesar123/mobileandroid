package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class ManAttProposeRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("date")
    private String date;
    @SerializedName("reason")
    private String reason;

    public ManAttProposeRequest(String device_type, String device_id, String nopeg, String date, String reason, String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.nopeg=nopeg;
        this.date= date;
        this.reason=reason;
        this.dateTime = dateTime;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
