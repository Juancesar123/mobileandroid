package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class SlipFATAMonth {
    @SerializedName("text")
    private String text;
    @SerializedName("dbulan")
    private String dbulan;
    @SerializedName("ada")
    private String ada;
    @SerializedName("nopeg")
    private String nopeg;

    public SlipFATAMonth(String text, String dbulan, String ada, String nopeg) {
        this.text = text;
        this.dbulan = dbulan;
        this.ada = ada;
        this.nopeg = nopeg;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDbulan() {
        return dbulan;
    }

    public void setDbulan(String dbulan) {
        this.dbulan = dbulan;
    }

    public String getAda() {
        return ada;
    }

    public void setAda(String ada) {
        this.ada = ada;
    }

    public String getNopeg() {
        return nopeg;
    }

    public void setNopeg(String nopeg) {
        this.nopeg = nopeg;
    }

    @Override
    public String toString() {
        return "SlipFATAMonth{" +
                "text='" + text + '\'' +
                ", dbulan='" + dbulan + '\'' +
                ", ada='" + ada + '\'' +
                ", nopeg='" + nopeg + '\'' +
                '}';
    }
}
