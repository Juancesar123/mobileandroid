package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class LeaveQuota {
    @SerializedName("KVERB")
    private int KVERB;
    @SerializedName("ANZHL")
    private int ANZHL;

    public int getKVERB() {
        return KVERB;
    }

    public void setKVERB(int KVERB) {
        this.KVERB = KVERB;
    }

    public int getANZHL() {
        return ANZHL;
    }

    public void setANZHL(int ANZHL) {
        this.ANZHL = ANZHL;
    }

    @Override
    public String toString() {
        return KVERB +" / " + ANZHL ;
    }
}
