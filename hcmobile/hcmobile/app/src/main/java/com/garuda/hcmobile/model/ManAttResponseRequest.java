package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class ManAttResponseRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("val_id")
    private int val_id;
    @SerializedName("itype")
    private int itype;
    @SerializedName("note")
    private String note;

    public ManAttResponseRequest(String device_type, String device_id, String nopeg, int val_id, int itype, String note, String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.nopeg=nopeg;
        this.val_id= val_id;
        this.itype=itype;
        this.note=note;
        this.dateTime = dateTime;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
