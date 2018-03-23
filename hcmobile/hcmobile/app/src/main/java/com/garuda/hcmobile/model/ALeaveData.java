package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class ALeaveData {
    @SerializedName("leave_id")
    private long leave_id;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("nama")
    private String nama;
    @SerializedName("jenis")
    private String jenis;
    @SerializedName("subty")
    private String subty;
    @SerializedName("text")
    private String text;
    @SerializedName("img_path")
    private String img_path;
    @SerializedName("status")
    private String status;
    @SerializedName("superior")
    private String superior;
    @SerializedName("start_date")
    private String start_date;
    @SerializedName("end_date")
    private String end_date;
    @SerializedName("sstatus")
    private String sstatus;
    @SerializedName("sup_reason")
    private String sup_reason;

    public ALeaveData(long leave_id, String nopeg, String nama, String jenis, String subty, String text, String img_path, String status, String superior, String start_date, String end_date, String sstatus,String sup_reason) {
        this.leave_id = leave_id;
        this.nopeg = nopeg;
        this.nama = nama;
        this.jenis = jenis;
        this.subty = subty;
        this.text = text;
        this.img_path = img_path;
        this.status = status;
        this.superior = superior;
        this.start_date = start_date;
        this.end_date = end_date;
        this.sstatus = sstatus;
        this.sup_reason = sup_reason;
    }

    public String getSstatus() {
        return sstatus;
    }

    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    public long getLeave_id() {
        return leave_id;
    }

    public void setLeave_id(long leave_id) {
        this.leave_id = leave_id;
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

    public String getJenis() {
        if(jenis==null){
            return "-";
        }
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getSubty() {
        return subty;
    }

    public void setSubty(String subty) {
        this.subty = subty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSup_reason() {
        return sup_reason;
    }

    public void setSup_reason(String sup_reason) {
        this.sup_reason = sup_reason;
    }


    @Override
    public String toString() {
        return "ALeaveData{" +
                "leave_id=" + leave_id +
                ", nopeg='" + nopeg + '\'' +
                ", nama='" + nama + '\'' +
                ", jenis='" + jenis + '\'' +
                ", subty='" + subty + '\'' +
                ", text='" + text + '\'' +
                ", img_path='" + img_path + '\'' +
                ", status='" + status + '\'' +
                ", superior='" + superior + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", sstatus='" + sstatus + '\'' +
                ", sup_reason='" + sup_reason + '\'' +
                '}';
    }
}
