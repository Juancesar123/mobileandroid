package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/19/2016.
 */
public class AEmp {

    @SerializedName("gabungan")
    private String gabungan;
    @SerializedName("nopeg_maintain")
    private String nopeg;
    @SerializedName("nama")
    private String nama;

    public String getGabungan() {
        return gabungan;
    }

    public void setGabungan(String gabungan) {
        this.gabungan = gabungan;
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

    @Override
    public String toString() {
        return "AEmp{" +
                "gabungan='" + gabungan + '\'' +
                ", nopeg='" + nopeg + '\'' +
                ", nama='" + nama + '\'' +
                '}';
    }
}
