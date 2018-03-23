package com.garuda.hcmobile.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.ClockInOutRequest;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.GCMTokenRequest;
import com.garuda.hcmobile.model.RegisterRequest;
import com.garuda.hcmobile.model.RegisterResponse;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.garuda.hcmobile.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 5/29/2016.
 */
public class GCMRegistrationIntentService extends IntentService {
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String TAG = "GCMTOKEN";


    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        SharedPreferences sharedPreferences = getSharedPreferences("GCM", Context.MODE_PRIVATE);//Define shared reference file name
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //Log.w("GCMRegIntentService", "token:" + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);

            String oldToken = sharedPreferences.getString(TAG, "");//Return "" when error or key not exists
            //Only request to save token when token is new
            if(!"".equals(token) && !oldToken.equals(token)) {
                saveTokenToServer(token);
                //Save new token to shared reference
                editor.putString(TAG, token);
                editor.commit();
            } else {
                //Log.w("GCMRegistrationService", "Old token");
            }
        } catch (Exception e) {
            //Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void saveTokenToServer(final String tokenGCM){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type= ((MyApplication) this.getApplication()).getDevice_type();
        final String device_id= ((MyApplication) this.getApplication()).getDevice_id();
        final String token= ((MyApplication) this.getApplication()).getToken();
        final String nopeg= ((MyApplication) this.getApplication()).getUser().getNopeg();
        final String password= ((MyApplication) this.getApplication()).getUser().getPassword();
        final GCMTokenRequest registerRequest = new GCMTokenRequest(device_type,device_id, nopeg, tokenGCM,dateTime);
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().setTokenGCM(new GCMTokenRequest(device_type, device_id, nopeg,tokenGCM, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                //Log.e("setTokenGCM", "status" + commonResponse.getStatus() +" | message : "+commonResponse.getMessage());
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                //Log.e("setTokenGCMError", "error auth " + error.getMessage());
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                //Log.e("AuthonSetTokenGCM", "error auth " + error.getMessage());
            }

        });

    }


}