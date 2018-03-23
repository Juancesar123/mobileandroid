package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class LocationCheckResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("locations")
    private ArrayList<ALocation> aLocation;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ALocation> getALocation() {
        return aLocation;
    }

    public void setALoction(ArrayList<ALocation> aLocation) {
        this.aLocation= aLocation;
    }


}
