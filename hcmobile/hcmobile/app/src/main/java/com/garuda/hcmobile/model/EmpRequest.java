package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;


public class EmpRequest {

    @SerializedName("device_type")
    private String device_type;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("modules")
    private String modules;
    @SerializedName("position_key")
    private String position_key;
    @SerializedName("org_unit")
    private String org_unit;

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getPosition_key() {
        return position_key;
    }

    public void setPosition_key(String position_key) {
        this.position_key = position_key;
    }

    public String getOrg_unit() {
        return org_unit;
    }

    public void setOrg_unit(String org_unit) {
        this.org_unit = org_unit;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }


    public EmpRequest(String device_type, String device_id, String nopeg, String modules, String position_key, String org_unit, String dateTime) {
        this.device_type = device_type;
        this.device_id=device_id;
        this.nopeg = nopeg;
        this.dateTime = dateTime;
        this.modules=modules;
        this.position_key=position_key;
        this.org_unit=org_unit;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getNopeg() {
        return nopeg;
    }

    public void setNopeg(String nopeg) {
        this.nopeg = nopeg;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


}
