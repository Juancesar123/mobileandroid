package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

public class OvertimeResponseDataRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("ot_id")
    private int ot_id;
    @SerializedName("ot_type")
    private int ot_type;
    @SerializedName("clock_in")
    private String clock_in;
    @SerializedName("clock_out")
    private String clock_out;

    public OvertimeResponseDataRequest(String device_type, String device_id, String nopeg, int ot_id, int ot_type, String clock_in, String clock_out,String dateTime) {
        this.device_type = device_type;
        this.device_id = device_id;
        this.nopeg = nopeg;
        this.dateTime = dateTime;
        this.ot_id = ot_id;
        this.ot_type = ot_type;
        this.clock_in = clock_in;
        this.clock_out = clock_out;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
