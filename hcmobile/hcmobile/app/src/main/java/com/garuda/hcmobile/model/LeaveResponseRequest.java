package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class LeaveResponseRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("leave_id")
    private int leave_id;
    @SerializedName("itype")
    private int itype;
    @SerializedName("note")
    private String note;

    public LeaveResponseRequest(String device_type, String device_id, String nopeg, int leave_id,int itype,String note, String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.nopeg=nopeg;
        this.leave_id= leave_id;
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
