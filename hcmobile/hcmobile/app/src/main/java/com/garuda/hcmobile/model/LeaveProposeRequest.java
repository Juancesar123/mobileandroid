package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class LeaveProposeRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("subty")
    private String subty;
    @SerializedName("begda")
    private String begda;
    @SerializedName("endda")
    private String endda;
    @SerializedName("reason")
    private String reason;

    public LeaveProposeRequest(String device_type, String device_id, String nopeg, String subty,String begda,String endda,String reason, String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.nopeg=nopeg;
        this.subty= subty;
        this.begda=begda;
        this.endda=endda;
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
