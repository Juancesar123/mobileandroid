package com.garuda.hcmobile.util;

import android.util.Log;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.FCMTokenRequest;
import com.garuda.hcmobile.model.GCMTokenRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 12/5/2017.
 */

public class HCMobFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Toast.makeText(getApplicationContext(), "FSM START", Toast.LENGTH_LONG).show();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("info", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        saveTokenToServer(refreshedToken);
    }

    private void saveTokenToServer(final String tokenFCM){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type= ((MyApplication) this.getApplication()).getDevice_type();
        final String device_id= ((MyApplication) this.getApplication()).getDevice_id();
        final String token= ((MyApplication) this.getApplication()).getToken();
        final String nopeg= ((MyApplication) this.getApplication()).getUser().getNopeg();
        final String password= ((MyApplication) this.getApplication()).getUser().getPassword();
        final FCMTokenRequest registerRequest = new FCMTokenRequest(device_type,device_id, nopeg, tokenFCM,dateTime);
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().setTokenFCM(registerRequest,
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
