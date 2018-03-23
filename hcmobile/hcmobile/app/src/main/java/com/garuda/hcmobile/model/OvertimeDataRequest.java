package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class OvertimeDataRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("year")
    private String year;
    @SerializedName("month")
    private String month;

    public OvertimeDataRequest(String device_type, String device_id, String nopeg, String year, String month, String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.year = year;
        this.month=month;
        this.nopeg=nopeg;
        this.dateTime = dateTime;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}