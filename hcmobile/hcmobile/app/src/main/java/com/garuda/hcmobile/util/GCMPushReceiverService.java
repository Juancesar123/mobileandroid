package com.garuda.hcmobile.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.garuda.hcmobile.R;
import com.garuda.hcmobile.view.HCMobHomeLeftMenusActivity;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by user on 5/12/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        String id= data.getString("id");
        sendNotification(Integer.parseInt(id),message,title);
    }

    private void sendNotification(int id,String message,String title){
        Intent intent = new Intent(this, HCMobHomeLeftMenusActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode=0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this,requestCode,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notification)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText("My GCM Message X:X")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,noBuilder.build());
    }

    private void sendNotification(String message){
        this.sendNotification((int)Math.random()*100,message,"HC-Mobile");

    }
}
