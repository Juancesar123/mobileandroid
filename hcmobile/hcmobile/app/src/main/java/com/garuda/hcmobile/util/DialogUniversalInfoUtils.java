package com.garuda.hcmobile.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.garuda.hcmobile.R;

public class DialogUniversalInfoUtils {

	private Activity activity;
	private Dialog mDialog;

	private TextView mDialogHeader;
	private TextView mDialogText;
	private TextView mDialogOKButton;
	private ImageView mDialogImage;

	public DialogUniversalInfoUtils(Activity activity) {
		this.activity = activity;
	}
	public void showDialog(){
		this.showDialog("");
	}
	public void showDialog(String message) {
		if (mDialog == null) {
			mDialog = new Dialog(activity, R.style.CustomDialogTheme);
		}
		mDialog.setContentView(R.layout.dialog_universal_info);
		mDialog.setCancelable(true);
		mDialog.show();

		mDialogHeader = (TextView) mDialog.findViewById(R.id.dialog_universal_info_title);
		mDialogText = (TextView) mDialog.findViewById(R.id.dialog_universal_info_text);
		mDialogOKButton = (TextView) mDialog.findViewById(R.id.dialog_universal_info_ok);

		if(message.equals("")==false){
			mDialogText.setText(message);
		}

		
		initDialogButtons();
	}



	private void initDialogButtons() {

		mDialogOKButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mDialog.dismiss();
			}
		});
	}

	public void dismissDialog() {
		mDialog.dismiss();
	}

	public TextView getmDialogOKButton() {
		return mDialogOKButton;
	}

	public void setmDialogOKButton(TextView mDialogOKButton) {
		this.mDialogOKButton = mDialogOKButton;
	}
}
