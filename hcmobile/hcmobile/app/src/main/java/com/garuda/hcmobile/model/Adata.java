package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/19/2016.
 */
public class Adata {

    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("nama")
    private String nama;
    @SerializedName("ee_grp")
    private String ee_grp;
    @SerializedName("position_key")
    private String position_key;
    @SerializedName("position_name")
    private String position_name;
    @SerializedName("job_key")
    private String job_key;
    @SerializedName("job_name")
    private String job_name;
    @SerializedName("org_unit")
    private String org_unit;
    @SerializedName("unit_short")
    private String unit_short;
    @SerializedName("unit_stext")
    private String unit_stext;
    @SerializedName("gender")
    private String gender;
    @SerializedName("ee_subgrp")
    private String ee_subgrp;
    @SerializedName("tax_id")
    private String tax_id;
    @SerializedName("email")
    private String email;



    public String getNopeg() {
        return nopeg;
    }

    public void setNopeg(String nopeg) {
        this.nopeg = nopeg;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEe_grp() {
        return ee_grp;
    }

    public void setEe_grp(String ee_grp) {
        this.ee_grp = ee_grp;
    }

    public String getPosition_key() {
        return position_key;
    }

    public void setPosition_key(String position_key) {
        this.position_key = position_key;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public String getJob_key() {
        return job_key;
    }

    public void setJob_key(String job_key) {
        this.job_key = job_key;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getOrg_unit() {
        return org_unit;
    }

    public void setOrg_unit(String org_unit) {
        this.org_unit = org_unit;
    }

    public String getUnit_short() {
        return unit_short;
    }

    public void setUnit_short(String unit_short) {
        this.unit_short = unit_short;
    }

    public String getUnit_stext() {
        return unit_stext;
    }

    public void setUnit_stext(String unit_stext) {
        this.unit_stext = unit_stext;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEe_subgrp() {
        return ee_subgrp;
    }

    public void setEe_subgrp(String ee_subgrp) {
        this.ee_subgrp = ee_subgrp;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
