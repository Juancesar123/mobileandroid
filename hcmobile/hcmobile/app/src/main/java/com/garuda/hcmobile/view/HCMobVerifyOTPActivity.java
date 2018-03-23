package com.garuda.hcmobile.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import com.garuda.hcmobile.datasource.MenuDAO;
import com.garuda.hcmobile.datasource.UserDAO;
import com.garuda.hcmobile.model.AMenu;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.EmpRequest;
import com.garuda.hcmobile.model.LaunchResponse;
import com.garuda.hcmobile.model.RegisterRequest;
import com.garuda.hcmobile.model.RegisterResponse;
import com.garuda.hcmobile.model.User;
import com.garuda.hcmobile.model.VerifiedResponse;
import com.garuda.hcmobile.model.VerifyRequest;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;
import com.garuda.hcmobile.util.HCMobile;
import com.garuda.hcmobile.view.component.FloatLabeledEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HCMobVerifyOTPActivity extends Activity implements OnClickListener {

    public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.garuda.hcmobile.HCMobVerifyOTPActivity";
    public static final String REGISTER_TRAVEL = "HCMobVerifyOTPActivity";
    private FloatLabeledEditText etOTPNumber = null;
    private TextView verify = null;
    private TextView requestOTP = null;
    private DialogUniversalInfoUtils dialog;
    private UserDAO userDAO;
    private MenuDAO menuDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hcmob_verifyotp);
        verify = (TextView) findViewById(R.id.verify);
        requestOTP = (TextView) findViewById(R.id.requestOTP);
        etOTPNumber = (FloatLabeledEditText) findViewById(R.id.otp_number);

        dialog = new DialogUniversalInfoUtils(this);


        verify.setOnClickListener(this);
        requestOTP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
            if (tv.getText().toString().equals(getResources().getString(R.string.verify))) {
                if (etOTPNumber.getText().toString().equals("")) {
                    etOTPNumber.requestFocus();
                    dialog.showDialog("Please fill OTP Number");
                } else if (etOTPNumber.getText().toString().length() != 6) {
                    etOTPNumber.requestFocus();
                    dialog.showDialog("Please fill 6 Number OTP we sent to your email.check your lastest email");
                } else {
                    tv.setEnabled(false);
                    ProgressDialog progressDialog = ProgressDialog.show(this, "", "Submitting your Verification..");
                    verify(etOTPNumber.getText().toString(), progressDialog);
                }
            } else if (tv.getText().toString().equals(getResources().getString(R.string.requestotp))) {
                tv.setEnabled(false);
                ProgressDialog progressDialog = ProgressDialog.show(this, "", "Requesting OTP..");
                requestOTP(progressDialog);

            }
        }
    }

    private void requestOTP(final ProgressDialog progressDialog) {
        try {
            final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
            final String username = UserDAO.getAppUsername();
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            final String imeiValue = telephonyManager.getDeviceId();
            final JSONObject imei = new JSONObject();
            imei.put(HCMobile.IMEI, imeiValue);
            Intent intent=getIntent();
            String nopeg = intent.getStringExtra(HCMobile.USER_NOPEG);
            String email = intent.getStringExtra(HCMobile.USER_EMAIL);
            String phoneNumber = intent.getStringExtra(HCMobile.USER_MSISDN);
            String gbdat = intent.getStringExtra(HCMobile.USER_DOB);
            final RegisterRequest registerRequest = new RegisterRequest(username,imeiValue, nopeg, phoneNumber, email, gbdat,dateTime);
            RestClient.getRestAPI().requestOTP(registerRequest,
                    new Callback<RegisterResponse>() {

                        @Override
                        public void success(RegisterResponse registerResponse, Response response) {

                            progressDialog.dismiss();
                            int status = registerResponse.getStatus();
                            switch (status) {
                                case 0:
                                    progressDialog.dismiss();
                                    //Log.i("requestOTP", "fail req OTP , " + registerResponse.getMessage());
                                    dialog.showDialog("Request OTP Failed");
                                    break;

                                case 1:
                                    progressDialog.dismiss();
                                    //Log.i("requestOTP", "success req OTP");
                                    dialog.showDialog("Resend OTP Success, Please Check your email");
                                    break;
                                case 99:
                                    progressDialog.dismiss();
                                    //Log.i("requestOTP", "fail req OTP , " + registerResponse.getMessage());
                                    dialog.showDialog("Request OTP Failed");
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            progressDialog.dismiss();
                            dialog.showDialog("Fail request OTP, " + error.getMessage());

                            //Log.e("requestOTP_retrofit", error.getMessage());
                            //setOTPOnClickListener(otpFields);

                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
            //Log.e("json", e.getMessage());
        }
    }

    private void verify(final String otp, final ProgressDialog progressDialog) {
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type= ((MyApplication) this.getApplication()).getDevice_type();
        final String device_id= ((MyApplication) this.getApplication()).getDevice_id();
        Intent intent =getIntent();
        User user = ((MyApplication) this.getApplication()).getUser();
        final VerifyRequest verifyRequest = new VerifyRequest(device_type, device_id, otp,user.getEmail(),user.getNopeg(), dateTime);
        //Log.i("BEFORE VERIFY OTP","SENDING");
        RestClient.getRestAPI().verifiedOTP(verifyRequest, new Callback<VerifiedResponse>() {
            @Override
            public void success(VerifiedResponse verifiedResponse, Response response) {
                //register parser
                //Log.i("BEFORE VERIFY OTP","ARRIVE");
                int status = verifiedResponse.getStatus();
                switch (status) {
                    case 0:
                        progressDialog.dismiss();
                        dialog.showDialog("Fail Verify , " + verifiedResponse.getMessage());
                        verify.setEnabled(true);
                        break;

                    case 1:
                        //Log.i("BEFORE VERIFY OTP","SUCCESS");
                        progressDialog.dismiss();
                        //progressDialog.setMessage("Authenticated, Loading your Profiles.");
                        updateUser(verifiedResponse);
                        break;
                    case 99:
                        progressDialog.dismiss();
                        dialog.showDialog("Fail Verify , " + verifiedResponse.getMessage());
                        verify.setEnabled(true);
                        break;
                    default:
                        progressDialog.dismiss();
                        break;
                }

            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                dialog.showDialog("Fail Verify , " + error.getMessage());
                //Log.e("verify", error.getMessage());
                verify.setEnabled(true);
            }
        });

    }

    private void updateUser(VerifiedResponse verifiedResponse) {
        Intent intent = getIntent();
        User user=((MyApplication)getApplication()).getUser();
        if(userDAO==null){
            userDAO = new UserDAO(this);
        }
        if(menuDao==null){
            menuDao=new MenuDAO(this);
        }
        userDAO.updatePassword(user.getId(),verifiedResponse.getPassword());
        //String nopeg = intent.getStringExtra(HCMobile.USER_NOPEG);
        //String email = intent.getStringExtra(HCMobile.USER_EMAIL);
        //User user = userDAO.insertUser("user", verifiedResponse.getPassword(), nopeg, email);
        //Log.i("USER STATUS",user.toString());
        userDAO.updateProfile(user.getId(),
                RestClient.DIR_ROOT_SSL+"/" + verifiedResponse.getImg(),
                verifiedResponse.getData().getPosition_key(),
                verifiedResponse.getData().getOrg_unit(),
                verifiedResponse.getData().getPosition_name(),
                verifiedResponse.getData().getUnit_short(),
                verifiedResponse.getData().getUnit_stext(),
                verifiedResponse.getData().getNama());
        ((MyApplication) getApplication()).setUser(userDAO.getUser());
        //Log.i("MENU COUNT",verifiedResponse.getMenu().size()+" menu");
        for (int i=0; i<verifiedResponse.getMenu().size(); i++) {
            AMenu amenu = verifiedResponse.getMenu().get(i);
            menuDao.insertMenu(amenu.getModule_code(),amenu.getModule_text(),amenu.getMod_icon(),amenu.getMod_status());
        }
        ((MyApplication) getApplication()).setMenus(menuDao.getMenus());
        goDashboard();
    }

    private void goDashboard() {
        Intent intent = new Intent(this, HCMobHomeLeftMenusActivity.class);
        startActivity(intent);
    }

    private void downloadLaunchData() {

        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getApplication()).getUser().getPassword();
        final User user = userDAO.getUser();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Load your profile...");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getLaunchData(new EmpRequest(device_type, device_id, nopeg,"","","", dateTime),
                        authenticateResponse.getToken(),
                        new Callback<LaunchResponse>() {
                            @Override
                            public void success(final LaunchResponse launchResponse, Response response) {
                                //Log.i("INIT_DATA", "OK");
                                if(launchResponse.getStatus()==1){
                                    userDAO.updateProfile(user.getId(),
                                            RestClient.ROOT_SSL + launchResponse.getImg(),
                                            launchResponse.getData().getPosition_key(),
                                            launchResponse.getData().getOrg_unit(),
                                            launchResponse.getData().getPosition_name(),
                                            launchResponse.getData().getUnit_short(),
                                            launchResponse.getData().getUnit_stext(),
                                            launchResponse.getData().getNama());
                                    ((MyApplication) getApplication()).setUser(userDAO.getUser());
                                    for (int i=0; i<launchResponse.getMenu().size(); i++) {
                                        AMenu amenu = launchResponse.getMenu().get(i);
                                        menuDao.insertMenu(amenu.getModule_code(),amenu.getModule_text(),amenu.getMod_icon(),amenu.getMod_status());
                                    }
                                    ((MyApplication) getApplication()).setMenus(menuDao.getMenus());
                                    progressDialog.dismiss();
                                    goDashboard();
                                }else{
                                    progressDialog.dismiss();
                                    dialog.showDialog("Get Launch Data Error, " + launchResponse.getMessage());
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                dialog.showDialog("Get Launch Data Error, " + error.getMessage());
                                //Log.e("GetLaunchDataError", "error auth " + error.getMessage());
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.showDialog("Auth Error, " + error.getMessage());
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });

    }
}
