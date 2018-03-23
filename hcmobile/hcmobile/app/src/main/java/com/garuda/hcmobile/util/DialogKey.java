package com.garuda.hcmobile.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.garuda.hcmobile.R;

public class DialogKey {

	private Activity activity;
	private Dialog mDialog;
	private TextView mDialogTitle;
	private TextView mDialogOKButton;
	private TextView mDialogCancelButton;
	private EditText mDialogComment;

	public DialogKey(Activity activity) {
		this.activity = activity;
	}


	public void showDialog(String message) {
		if (mDialog == null) {
			mDialog = new Dialog(activity,
					R.style.CustomDialogTheme);
		}
		mDialog.setContentView(R.layout.dialog_key);
		mDialog.setCancelable(true);
		mDialog.show();

		mDialogTitle = (TextView) mDialog
				.findViewById(R.id.dialog_title);
		mDialogOKButton = (TextView) mDialog
				.findViewById(R.id.dialog_ok);
		mDialogCancelButton = (TextView) mDialog
				.findViewById(R.id.dialog_cancel);
		mDialogTitle.setText(message);
		mDialogComment = (EditText)mDialog.findViewById(R.id.dialog_fkey);

//		initDialogButtons();
	}

	public EditText getmDialogComment() {
		return mDialogComment;
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
