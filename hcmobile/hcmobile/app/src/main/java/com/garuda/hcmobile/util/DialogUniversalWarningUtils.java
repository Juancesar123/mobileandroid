package com.garuda.hcmobile.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


import com.garuda.hcmobile.R;

import org.w3c.dom.Text;

public class DialogUniversalWarningUtils {

	private Activity activity;
	private Dialog mDialog;

	private TextView mDialogText;
	private TextView mDialogOKButton;
	private TextView mDialogCancelButton;
	private int sel=2;

	public DialogUniversalWarningUtils(
			Activity activity) {
		this.activity = activity;
	}

	public void showDialog(String message) {
		if (mDialog == null) {
			mDialog = new Dialog(activity,
					R.style.CustomDialogTheme);
		}
		mDialog.setContentView(R.layout.dialog_universal_warning);
		mDialog.setCancelable(true);
		mDialog.show();

		mDialogText = (TextView) mDialog
				.findViewById(R.id.dialog_universal_warning_text);
		mDialogOKButton = (TextView) mDialog
				.findViewById(R.id.dialog_universal_warning_ok);
		mDialogCancelButton = (TextView) mDialog
				.findViewById(R.id.dialog_universal_warning_cancel);
		mDialogText.setText(message);

//		initDialogButtons();
	}

	public TextView getOkButton(){
		return mDialogOKButton;
	}

	public TextView getCancelButton(){
		return mDialogCancelButton;
	}

	private void initDialogButtons() {

		mDialogOKButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mDialog.dismiss();
			}
		});

		mDialogCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mDialog.dismiss();
			}
		});
	}

	public void dismissDialog() {
		mDialog.dismiss();
	}
}
