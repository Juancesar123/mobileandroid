package com.garuda.hcmobile.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifiedResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("password")
    private String password;
    @SerializedName("data")
    private Adata data;
    @SerializedName("menu")
    private List<AMenu> menu;
    @SerializedName("img")
    private String img;


    public VerifiedResponse(int status, String message, String password) {
        this.status = status;
        this.message = message;
        this.password = password;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
}
