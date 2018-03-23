package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MBP on 1/9/15.
 */
public class CommonResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

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
}
