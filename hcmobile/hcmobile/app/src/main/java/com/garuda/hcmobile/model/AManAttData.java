package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class AManAttData {
    @SerializedName("val_id")
    private long val_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("nama")
    private String nama;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("status")
    private String status;
    @SerializedName("superior")
    private String superior;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("sstatus")
    private String sstatus;
    @SerializedName("sup_reason")
    private String sup_reason;

    public AManAttData(long val_id, String nopeg, String nama, String keterangan, String status, String superior, String tanggal, String sstatus, String sup_reason) {
        this.val_id = val_id;
        this.nopeg = nopeg;
        this.nama = nama;
        this.keterangan = keterangan;
        this.status = status;
        this.superior = superior;
        this.tanggal = tanggal;
        this.sstatus = sstatus;
        this.sup_reason = sup_reason;
    }

    public long getVal_id() {
        return val_id;
    }

    public void setVal_id(long val_id) {
        this.val_id = val_id;
    }

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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getSstatus() {
        return sstatus;
    }

    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    public String getSup_reason() {
        if(sup_reason==null || sup_reason.equals("")){
            return "";
        }
        return sup_reason;
    }

    public void setSup_reason(String sup_reason) {
        this.sup_reason = sup_reason;
    }
}
