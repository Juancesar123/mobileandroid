package com.garuda.hcmobile.model;

import com.garuda.hcmobile.Api;
import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.RestClient;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class ALocation {
    @SerializedName("id")
    private int id;
    @SerializedName("location_name")
    private String location_name;
    @SerializedName("point_x")
    private double point_x;
    @SerializedName("point_y")
    private double point_y;
    @SerializedName("location_photo")
    private String location_photo;
    @SerializedName("meter")
    private int meter;

    public String getImageURL(){
        return RestClient.DIR_ROOT_SSL+"/loc/"+getLocation_photo();
//        return
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public double getPoint_x() {
        return point_x;
    }

    public void setPoint_x(double point_x) {
        this.point_x = point_x;
    }

    public double getPoint_y() {
        return point_y;
    }

    public void setPoint_y(double point_y) {
        this.point_y = point_y;
    }

    public String getLocation_photo() {
        return location_photo;
    }

    public void setLocation_photo(String location_photo) {
        this.location_photo = location_photo;
    }

    public int getMeter() {
        return meter;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }
}
