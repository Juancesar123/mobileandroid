package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class ATimeData {
    @SerializedName("data_id")
    private long data_id;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("hari")
    private String hari;
    @SerializedName("dws")
    private String dws;
    @SerializedName("jam_masuk")
    private String jam_masuk;
    @SerializedName("jam_keluar")
    private String jam_keluar;
    @SerializedName("clock_in")
    private String clock_in;
    @SerializedName("clock_out")
    private String clock_out;
    @SerializedName("isSubstitutions")
    private int isSubstitutions;
    @SerializedName("isValidate")
    private int isValidate;
    @SerializedName("isOvertime")
    private int isOvertime;
    @SerializedName("isAbsAtt")
    private int isAbsAtt;
    @SerializedName("tmh_keterangan")
    private String tmh_keterangan;

    public ATimeData(long data_id, String tanggal, String hari, String dws, String jam_masuk, String jam_keluar, String clock_in, String clock_out, int isSubstitutions, int isValidate, int isOvertime, int isAbsAtt, String tmh_keterangan) {
        this.data_id = data_id;
        this.tanggal = tanggal;
        this.hari = hari;
        this.dws = dws;
        this.jam_masuk = jam_masuk;
        this.jam_keluar = jam_keluar;
        this.clock_in = clock_in;
        this.clock_out = clock_out;
        this.isSubstitutions = isSubstitutions;
        this.isValidate = isValidate;
        this.isOvertime = isOvertime;
        this.isAbsAtt = isAbsAtt;
        this.tmh_keterangan = tmh_keterangan;
    }

    public long getData_id() {
        return data_id;
    }

    public void setData_id(long data_id) {
        this.data_id = data_id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getDws() {
        return dws;
    }

    public void setDws(String dws) {
        this.dws = dws;
    }

    public String getJam_masuk() {
        if(jam_masuk==null || jam_masuk.equals("NULL")){
            return "N/A";
        }
        return jam_masuk;
    }

    public void setJam_masuk(String jam_masuk) {
        this.jam_masuk = jam_masuk;
    }

    public String getJam_keluar() {
        if(jam_keluar==null || jam_keluar.equals("NULL")){
            return "N/A";
        }
        return jam_keluar;
    }

    public void setJam_keluar(String jam_keluar) {
        this.jam_keluar = jam_keluar;
    }

    public String getClock_in() {
        if(clock_in==null || clock_in.equals("NULL")){
            return "N/A";
        }
        return clock_in;
    }

    public void setClock_in(String clock_in) {
        this.clock_in = clock_in;
    }

    public String getClock_out() {
        if(clock_out==null || clock_out.equals("NULL")){
            return "N/A";
        }
        return clock_out;
    }

    public void setClock_out(String clock_out) {
        this.clock_out = clock_out;
    }

    public int getIsSubstitutions() {
        return isSubstitutions;
    }

    public void setIsSubstitutions(int isSubstitutions) {
        this.isSubstitutions = isSubstitutions;
    }

    public int getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(int isValidate) {
        this.isValidate = isValidate;
    }

    public int getIsOvertime() {
        return isOvertime;
    }

    public void setIsOvertime(int isOvertime) {
        this.isOvertime = isOvertime;
    }

    public int getIsAbsAtt() {
        return isAbsAtt;
    }

    public void setIsAbsAtt(int isAbsAtt) {
        this.isAbsAtt = isAbsAtt;
    }

    public String getTmh_keterangan() {
        if(tmh_keterangan==null || tmh_keterangan.equals("NULL")){
            return "-";
        }
        return tmh_keterangan;
    }

    public void setTmh_keterangan(String tmh_keterangan) {
        this.tmh_keterangan = tmh_keterangan;
    }
}
