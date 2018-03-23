package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 9/30/2016.
 */
public class NewsModel {

    @SerializedName("id")
    private long mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("news_text")
    private String mText;
    @SerializedName("footer")
    private String mFooter;

    public NewsModel() {
    }

    public NewsModel(long id, String title, String text, String  footer) {
        mId = id;
        mTitle = title;
        mText = text;
        mFooter = footer;
    }

    public long getId(){
        return mId;
    }
    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmFooter() {
        return mFooter;
    }

    public void setmFooter(String mFooter) {
        this.mFooter = mFooter;
    }

    @Override
    public String toString() {
        return mText;
    }
}
