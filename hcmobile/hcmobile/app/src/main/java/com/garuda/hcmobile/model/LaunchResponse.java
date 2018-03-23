package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class LaunchResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Adata data;
    @SerializedName("menu")
    private List<AMenu> menu;
    @SerializedName("img")
    private String img;

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

    public Adata getData() {
        return data;
    }

    public void setDatas(Adata data) {
        this.data = data;
    }

    public List<AMenu> getMenu() {
        return menu;
    }

    public void setMenu(List<AMenu> menu) {
        this.menu = menu;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
