package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class HCNewsResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private List<NewsModel> data;
    @SerializedName("message")
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<NewsModel>  getData() {
        return data;
    }

    public void setData(List<NewsModel>  data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
