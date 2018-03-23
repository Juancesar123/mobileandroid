package com.garuda.hcmobile.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.garuda.hcmobile.R;

public class DialogUniversalWNoteUtils {

	private Activity activity;
	private Dialog mDialog;
	private int status=2;
	public final static int STATUS_REJECT=2;
	public final static int STATUS_APPROVE=3;
	private TextView mDialogTitle;
	private TextView mDialogOKButton;
	private TextView mDialogCancelButton;
	private EditText mDialogComment;

	public DialogUniversalWNoteUtils(Activity activity) {
		this.activity = activity;
	}

	public int getStatus() {
		return status;
	}

	public void showDialog(String message,int status) {
		if (mDialog == null) {
			mDialog = new Dialog(activity,
					R.style.CustomDialogTheme);
		}
		this.status=status;
		mDialog.setContentView(R.layout.dialog_universal_wnote);
		mDialog.setCancelable(true);
		mDialog.show();

		mDialogTitle = (TextView) mDialog
				.findViewById(R.id.dialog_wnote_title);
		mDialogOKButton = (TextView) mDialog
				.findViewById(R.id.dialog_wnote_ok);
		mDialogCancelButton = (TextView) mDialog
				.findViewById(R.id.dialog_wnote_cancel);
		mDialogTitle.setText(message);
		mDialogComment = (EditText)mDialog.findViewById(R.id.dialog_wnote_comment);

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
