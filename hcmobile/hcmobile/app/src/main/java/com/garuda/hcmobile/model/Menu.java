package com.garuda.hcmobile.model;


import com.google.gson.annotations.SerializedName;

public class Menu {
//    @SerializedName("id")
    private long id;
//    @SerializedName("code")
    private int code;
//    @SerializedName("name")
    private String name;
//    @SerializedName("icon")
    private int icon;
//    @SerializedName("status")
    private int status;

    public Menu(){}

    public Menu(long id, int code, String name, int icon, int status) {
        this.id = id;
        this.code= code;
        this.name = name;
        this.icon = icon;
        this.status= status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
