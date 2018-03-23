package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MBP on 1/9/15.
 */
public class TMSInitResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("msg")
    private TMSAuth msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TMSAuth getData() {
        return msg;
    }

    public void setData(TMSAuth data) {
        this.msg = data;
    }
}
