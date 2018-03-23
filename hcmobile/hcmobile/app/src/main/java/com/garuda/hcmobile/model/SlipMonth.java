package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class SlipMonth {
    @SerializedName("text")
    private String text;
    @SerializedName("seqno")
    private String seqno;
    @SerializedName("paydate")
    private String paydate;
    @SerializedName("variant")
    private String variant;

    public SlipMonth(String text,String seqno,String paydate,String variant){
        this.text=text;
        this.seqno=seqno;
        this.paydate=paydate;
        this.variant=variant;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        return "SlipMonth{" +
                "text='" + text + '\'' +
                ", seqno='" + seqno + '\'' +
                ", paydate='" + paydate + '\'' +
                ", variant='" + variant + '\'' +
                '}';
    }
}
