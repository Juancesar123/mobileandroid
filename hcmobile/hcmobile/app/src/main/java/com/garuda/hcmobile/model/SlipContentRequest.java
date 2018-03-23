package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class SlipContentRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("key")
    private String key;
    @SerializedName("spc_param")
    private String spcParam;

    public SlipContentRequest(String device_type, String device_id, String nopeg, String key, String spcParam, String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.key = key;
        this.nopeg=nopeg;
        this.dateTime = dateTime;
        this.spcParam=spcParam;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
