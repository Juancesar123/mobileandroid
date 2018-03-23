package com.garuda.hcmobile.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.datasource.UserDAO;
import com.garuda.hcmobile.model.RegisterRequest;
import com.garuda.hcmobile.model.RegisterResponse;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;
import com.garuda.hcmobile.util.HCMobile;
import com.garuda.hcmobile.view.component.FloatLabeledEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HCMobRegisterActivity extends Activity implements OnClickListener {

	public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.garuda.hcmobile.HCMobRegisterActivity";
	public static final String REGISTER_TRAVEL = "HCMobRegisterActivity";
	private FloatLabeledEditText etNopeg=null;
	private FloatLabeledEditText etPhone=null;
	private FloatLabeledEditText etEmail=null;
	private FloatLabeledEditText etGBDAT=null;
	private TextView register=null;
	private TextView txtRegister=null;
	private DialogUniversalInfoUtils dialog;
	private Matcher matcher;
	private UserDAO userDAO;
	private static final String DATE_PATTERN =
			"((19|20)\\d\\d)[-](5?[1-9]|6[012])[-](8?[1-9]|[12][0-9]|9[01])";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hcmob_register);
		this.register = (TextView) findViewById(R.id.register);
		this.etNopeg= (FloatLabeledEditText) findViewById(R.id.nopeg);
		this.etPhone= (FloatLabeledEditText) findViewById(R.id.msisdn);
		this.etEmail= (FloatLabeledEditText) findViewById(R.id.email);
		this.etGBDAT= (FloatLabeledEditText) findViewById(R.id.gbdat);
		this.txtRegister= (TextView) findViewById(R.id.txtRegister);

		userDAO = new UserDAO(this);

		dialog = new DialogUniversalInfoUtils(this);
		this.txtRegister.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			TextView tv = (TextView) v;
			Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
			if (etNopeg.getText().toString().equals("") && etEmail.getText().toString().equals("")
					&& etGBDAT.getText().toString().equals("") && etPhone.getText().toString().equals("")) {
				etNopeg.requestFocus();
				dialog.showDialog("Please fill in all fields");
			} else if (etNopeg.getText().toString().equals("")) {
				etNopeg.requestFocus();
				dialog.showDialog("Please fill in your name");
			} else if (etEmail.getText().toString().equals("")) {
				etEmail.requestFocus();
				dialog.showDialog("Please fill in your email");
			} else if (etPhone.getText().toString().equals("")) {
				etPhone.requestFocus();
				dialog.showDialog("Please fill in your phone number");
			}  else if (etGBDAT.getText().toString().equals("")) {
				etGBDAT.requestFocus();
				dialog.showDialog("Please fill in your Birth Day");
			} else if (this.validate(etGBDAT.getText().toString())) {
				etGBDAT.requestFocus();
				dialog.showDialog("Please fill corrent date format your birth date (yyyy-mm-dd)");
			} else if (etNopeg.getText().toString().length()!=6 && (etNopeg.getText().toString().startsWith("5") || etNopeg.getText().toString().startsWith("7"))==false ) {
				etNopeg.requestFocus();
				dialog.showDialog("please fill correct nopeg");
			} else if (!isEmailValid(etEmail.getText().toString())) {
				etEmail.requestFocus();
				dialog.showDialog("Invalid email format.");
			} else {
				tv.setEnabled(false);
				ProgressDialog progressDialog = ProgressDialog.show(this, "", "Submitting your registration..");
				register( etNopeg.getText().toString(),etPhone.getTextString().toString(),etEmail.getText().toString(),etGBDAT.getText().toString(), progressDialog);
			}
		}
	}

	private void register(final String nopeg,final String phoneNumber,final String email,final String gbdat, final ProgressDialog progressDialog) {

//		try {

			final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
			final String device_type= ((MyApplication) this.getApplication()).getDevice_type();
			final String device_id= ((MyApplication) this.getApplication()).getDevice_id();
			final RegisterRequest registerRequest = new RegisterRequest(device_type,device_id, nopeg, phoneNumber, email, gbdat,dateTime);

			RestClient.getRestAPI().register(registerRequest, new Callback<RegisterResponse>() {
				@Override
				public void success(RegisterResponse registerResponse, Response response) {
					//register parser
					int status = registerResponse.getStatus();
					switch (status) {
						case 0:
							progressDialog.dismiss();
							register.setEnabled(true);
							dialog.showDialog("Fail register , " + registerResponse.getMessage());
							break;

						case 1:
							progressDialog.dismiss();
							goVerifyOTP(registerRequest);
							break;
						case 99:
							progressDialog.dismiss();
							register.setEnabled(true);
							dialog.showDialog("Fail register , " + registerResponse.getMessage());
							break;
						default:
							break;
					}

				}

				@Override
				public void failure(RetrofitError error) {
					progressDialog.dismiss();
					dialog.showDialog("Fail register , " + error.getMessage());
					//Log.e("reg", error.getMessage());
					register.setEnabled(true);
				}
			});

	}

	public boolean validate(final String date){
		matcher = Pattern.compile(DATE_PATTERN).matcher(date);

		if(matcher.matches()){

			//Log.e("date validate","matcher.matches") ;
			matcher.reset();

			if(matcher.find()){
				//Log.e("date validate","matcher.matches") ;
				String day = matcher.group(3);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(1));
				//Log.e("date validate",""+year) ;
				//Log.e("date validate",month) ;
				//Log.e("date validate",day) ;

				if (day.equals("31") &&
						(month.equals("4") || month .equals("6") || month.equals("9") ||
								month.equals("11") || month.equals("04") || month .equals("06") ||
								month.equals("09"))) {
					return false; // only 1,3,5,7,8,10,12 has 31 days
				}

				else if (month.equals("2") || month.equals("02")) {
					//leap year
					if(year % 4==0){
						return !(day.equals("30") || day.equals("31"));
					}
					else{
						return !(day.equals("29") || day.equals("30") || day.equals("31"));
					}
				}

				else{
					return true;
				}
			}

			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	public boolean isEmailValid(String email) {
		String regExpn =
				"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
						+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
						+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
						+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		return matcher.matches() && email.endsWith("@garuda-indonesia.com");
	}

	private void goVerifyOTP(RegisterRequest registerRequest) {
		Intent intent = new Intent(this, HCMobVerifyOTPActivity.class);
		intent.putExtra(HCMobile.USER_NOPEG, registerRequest.getNopeg());
		intent.putExtra(HCMobile.USER_DOB, registerRequest.getGbdat());
		intent.putExtra(HCMobile.USER_EMAIL, registerRequest.getEmail());
		intent.putExtra(HCMobile.USER_MSISDN, registerRequest.getMsisdn());
		userDAO.insertUser("user","", registerRequest.getNopeg(), registerRequest.getEmail());
		((MyApplication) getApplication()).setUser(userDAO.getUser());
		startActivity(intent);

	}
}
