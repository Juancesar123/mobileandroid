package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class AOvertimeData {
    @SerializedName("ot_id")
    private int ot_id;
    @SerializedName("data_id")
    private int data_id;
    @SerializedName("hari")
    private String hari;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("nopeg")
    private String nopeg;
    @SerializedName("nama")
    private String nama;
    @SerializedName("plan_in")
    private String plan_in;
    @SerializedName("plan_out")
    private String plan_out;
    @SerializedName("approve_in")
    private String approve_in;
    @SerializedName("approve_out")
    private String approve_out;
    @SerializedName("pay_in")
    private String pay_in;
    @SerializedName("pay_out")
    private String pay_out;
    @SerializedName("spkl")
    private String spkl;
    @SerializedName("clock_in")
    private String clock_in;
    @SerializedName("clock_out")
    private String clock_out;
    @SerializedName("sap_in")
    private String sap_in;
    @SerializedName("sap_out")
    private String sap_out;
    @SerializedName("sstatus")
    private String sstatus;
    @SerializedName("status")
    private String status;
    @SerializedName("to_sap")
    private String to_sap;
    @SerializedName("tgl_emp_data")
    private String tgl_emp_data;


    public AOvertimeData(int ot_id, int data_id, String hari, String tanggal, String nopeg, String nama, String plan_in, String plan_out, String approve_in, String approve_out, String pay_in, String pay_out, String spkl, String clock_in, String clock_out, String sap_in, String sap_out, String sstatus, String status, String to_sap, String tgl_emp_data) {
        this.ot_id = ot_id;
        this.data_id = data_id;
        this.hari = hari;
        this.tanggal = tanggal;
        this.nopeg = nopeg;
        this.nama = nama;
        this.plan_in = plan_in;
        this.plan_out = plan_out;
        this.approve_in = approve_in;
        this.approve_out = approve_out;
        this.pay_in = pay_in;
        this.pay_out = pay_out;
        this.spkl = spkl;
        this.clock_in = clock_in;
        this.clock_out = clock_out;
        this.sap_in = sap_in;
        this.sap_out = sap_out;
        this.sstatus = sstatus;
        this.status = status;
        this.to_sap = to_sap;
        this.tgl_emp_data = tgl_emp_data;
    }

    public String getPay_in() {
        return pay_in;
    }

    public void setPay_in(String pay_in) {
        this.pay_in = pay_in;
    }

    public String getApprove_in() {
        return approve_in;
    }

    public void setApprove_in(String approve_in) {
        this.approve_in = approve_in;
    }

    public int getOt_id() {
        return ot_id;
    }

    public void setOt_id(int ot_id) {
        this.ot_id = ot_id;
    }

    public int getData_id() {
        return data_id;
    }

    public void setData_id(int data_id) {
        this.data_id = data_id;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public String getPlan_in() {
        return plan_in;
    }

    public void setPlan_in(String plan_in) {
        this.plan_in = plan_in;
    }

    public String getPlan_out() {
        return plan_out;
    }

    public void setPlan_out(String plan_out) {
        this.plan_out = plan_out;
    }

    public String getApprove_out() {
        return approve_out;
    }

    public void setApprove_out(String approve_out) {
        this.approve_out = approve_out;
    }

    public String getPay_out() {
        return pay_out;
    }

    public void setPay_out(String pay_out) {
        this.pay_out = pay_out;
    }

    public String getSpkl() {
        return spkl;
    }

    public void setSpkl(String spkl) {
        this.spkl = spkl;
    }

    public String getClock_in() {
        return clock_in;
    }

    public void setClock_in(String clock_in) {
        this.clock_in = clock_in;
    }

    public String getClock_out() {
        return clock_out;
    }

    public void setClock_out(String clock_out) {
        this.clock_out = clock_out;
    }

    public String getSap_in() {
        return sap_in;
    }

    public void setSap_in(String sap_in) {
        this.sap_in = sap_in;
    }

    public String getSap_out() {
        return sap_out;
    }

    public void setSap_out(String sap_out) {
        this.sap_out = sap_out;
    }

    public String getSstatus() {
        return sstatus;
    }

    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTo_sap() {
        return to_sap;
    }

    public void setTo_sap(String to_sap) {
        this.to_sap = to_sap;
    }

    public String getTgl_emp_data() {
        return tgl_emp_data;
    }

    public void setTgl_emp_data(String tgl_emp_data) {
        this.tgl_emp_data = tgl_emp_data;
    }

    @Override
    public String toString() {
        return "AOvertimeData{" +
                "ot_id=" + ot_id +
                ", data_id=" + data_id +
                ", hari='" + hari + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", nopeg='" + nopeg + '\'' +
                ", nama='" + nama + '\'' +
                ", plan_in='" + plan_in + '\'' +
                ", plan_out='" + plan_out + '\'' +
                ", approve_in='" + approve_in + '\'' +
                ", approve_out='" + approve_out + '\'' +
                ", pay_in='" + pay_in + '\'' +
                ", pay_out='" + pay_out + '\'' +
                ", spkl='" + spkl + '\'' +
                ", clock_in='" + clock_in + '\'' +
                ", clock_out='" + clock_out + '\'' +
                ", sap_in='" + sap_in + '\'' +
                ", sap_out='" + sap_out + '\'' +
                ", sstatus='" + sstatus + '\'' +
                ", status='" + status + '\'' +
                ", to_sap='" + to_sap + '\'' +
                ", tgl_emp_data='" + tgl_emp_data + '\'' +
                '}';
    }
}
