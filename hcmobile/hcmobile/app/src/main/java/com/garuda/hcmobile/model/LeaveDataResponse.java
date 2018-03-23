package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class LeaveDataResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ALeaveData> data;
    @SerializedName("leave")
    private LeaveQuota leave;
    @SerializedName("cs")
    private LeaveQuota cs;

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

    public List<ALeaveData> getData() {
        return data;
    }

    public void setData(List<ALeaveData> data) {
        this.data = data;
    }

    public LeaveQuota getCs() {
        return cs;
    }

    public void setCs(LeaveQuota cs) {
        this.cs = cs;
    }

    public LeaveQuota getLeave() {
        return leave;
    }

    public void setLeave(LeaveQuota leave) {
        this.leave = leave;
    }
}
