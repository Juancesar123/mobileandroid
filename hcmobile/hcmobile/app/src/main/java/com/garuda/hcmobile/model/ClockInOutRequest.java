package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;


public class ClockInOutRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("loc_long")
    private double loc_long;
    @SerializedName("loc_latt")
    private double loc_latt;
    @SerializedName("loc_time")
    private String loc_time;
    @SerializedName("area")
    private String area;

    public ClockInOutRequest(String device_type, String device_id, String nopeg,
             double loc_long, double loc_latt, String loc_time, String area,
             String dateTime) {
        this.device_type = device_type;
        this.device_id=device_id;
        this.nopeg = nopeg;
        this.dateTime = dateTime;
        this.loc_long=loc_long;
        this.loc_latt=loc_latt;
        this.loc_time=loc_time;
        this.area=area;
    }

    public double getLoc_long() {
        return loc_long;
    }

    public void setLoc_long(double loc_long) {
        this.loc_long = loc_long;
    }

    public double getLoc_latt() {
        return loc_latt;
    }

    public void setLoc_latt(double loc_latt) {
        this.loc_latt = loc_latt;
    }

    public String getLoc_time() {
        return loc_time;
    }

    public void setLoc_time(String loc_time) {
        this.loc_time = loc_time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


}
