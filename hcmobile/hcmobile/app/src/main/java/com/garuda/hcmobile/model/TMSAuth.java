package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MBP on 1/9/15.
 */
public class TMSAuth {
    @SerializedName("isLakhar")
    private int isLakhar=0;
    @SerializedName("isMan")
    private int isMan=0;
    @SerializedName("can_espkl")
    private int can_espkl=0;
    @SerializedName("can_leave")
    private int can_leave=0;
    @SerializedName("can_clock_inout")
    private int can_clock_inout=1;

    public int getIsLakhar() {
        return isLakhar;
    }

    public void setIsLakhar(int isLakhar) {
        this.isLakhar = isLakhar;
    }

    public int getIsMan() {
        return isMan;
    }

    public void setIsMan(int isMan) {
        this.isMan = isMan;
    }

    public int getCan_leave() {
        return can_leave;
    }

    public void setCan_leave(int can_leave) {
        this.can_leave = can_leave;
    }

    public int getCan_espkl() {
        return can_espkl;
    }

    public void setCan_espkl(int can_espkl) {
        this.can_espkl = can_espkl;
    }

    public int getCan_clock_inout() {
        return can_clock_inout;
    }

    public void setCan_clock_inout(int can_clock_inout) {
        this.can_clock_inout = can_clock_inout;
    }
}
