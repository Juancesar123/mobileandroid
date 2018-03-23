package com.garuda.hcmobile.model;

public class MenuModel {

	final public static int MODCODE_HCNEWS = 3;
	final public static int MODCODE_TMS_EMP = 6;
	final public static int MODCODE_TMS_SUP = 9;
	final public static int MODCODE_TMS_CLOCKINOUT = 10;
	final public static int MODCODE_SLIP_GAJI= 11;
	final public static int MODCODE_SLIP_FATA= 12;

	private long mId;
	private String mText;
	private int mIconRes;
	private int mstatus;

	public MenuModel() {
	}

	public MenuModel(long id, String text, int iconRes, int status) {
		mId = id;
		mstatus= status;
		mText = text;
		mIconRes = iconRes;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public int getStatus() {
		return mstatus;
	}

	public void setStatus(int status) {
		mstatus= status;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public int getIconRes() {
		return mIconRes;
	}

	public void setIconRes(int iconRes) {
		mIconRes = iconRes;
	}

	@Override
	public String toString() {
		return "MenuModel{" +
				"mId=" + mId +
				", mText='" + mText + '\'' +
				", mIconRes=" + mIconRes +
				", mstatus=" + mstatus +
				'}';
	}
}
