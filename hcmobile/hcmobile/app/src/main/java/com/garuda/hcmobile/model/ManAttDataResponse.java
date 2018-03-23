package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class ManAttDataResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<AManAttData> data;

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

    public List<AManAttData> getData() {
        return data;
    }

    public void setData(List<AManAttData> data) {
        this.data = data;
    }
}
