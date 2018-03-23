package com.garuda.hcmobile.util;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by user on 5/12/2016.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        Intent intent = new Intent(this,GCMRegistrationIntentService.class);
        startService(intent);
    }
}
