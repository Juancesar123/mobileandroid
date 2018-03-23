package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class OvertimeProposeRequest {

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
    @SerializedName("start_propose")
    private String start_propose;
    @SerializedName("end_propose")
    private String end_propose;
    @SerializedName("reason")
    private String reason;

    public OvertimeProposeRequest(String device_type, String device_id, String nopeg, String date, String start_propose, String end_propose, String reason,String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.nopeg = nopeg;
        this.dateTime = dateTime;
        this.date = date;
        this.start_propose = start_propose;
        this.end_propose = end_propose;
        this.reason = reason;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
