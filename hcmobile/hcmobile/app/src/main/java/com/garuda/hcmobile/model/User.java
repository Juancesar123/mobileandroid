package com.garuda.hcmobile.model;


import android.util.Log;

public class User {
    private long id;
    private String user;
    private String password;
    private String nopeg;
    private String email;
    private String photo;
    private String position_name;
    private String unit_short;
    private String unit_stext;
    private String position_key;
    private String org_unit;
    private String nama;

    public User(){}

    public User(long id, String user, String password, String nopeg, String email) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.nopeg = nopeg;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNopeg() {
        return nopeg;
    }

    public void setNopeg(String nopeg) {
        this.nopeg = nopeg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        this.photo = photo.replace("https","http");
        //Log.e("photo url", photo);
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", nopeg='" + nopeg + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                ", position_name='" + position_name + '\'' +
                ", unit_short='" + unit_short + '\'' +
                ", unit_stext='" + unit_stext + '\'' +
                ", position_key='" + position_key + '\'' +
                ", org_unit='" + org_unit + '\'' +
                ", nama='" + nama + '\'' +
                '}';
    }
}
