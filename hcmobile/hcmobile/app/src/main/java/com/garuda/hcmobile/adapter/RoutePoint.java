package com.garuda.hcmobile.adapter;

import android.util.Log;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by user on 9/28/2016.
 */
public class RoutePoint {

    String url_1="https://hconline.garuda-indonesia.com/svc/hc_mobile/public/index.php";
    String url_2="http://intra.garuda-indonesia.com/hcm/hc_mobile/public/index.php";

    public boolean isConnectedToServer(String url, int timeout) {
        return false;
//        try{
//            URL myUrl = new URL(url);
//            URLConnection connection = myUrl.openConnection();
//            connection.setConnectTimeout(timeout);
//            connection.connect();
//            return true;
//        } catch (Exception e) {
//            Log.i("url "+url,e.getMessage());
//            // Handle your exceptions
//            return false;
//        }
    }

    public String getUrlPointing(){
//        if(this.isConnectedToServer(this.url_1,1000)){
//            return this.url_1;
//        }
//        if(this.isConnectedToServer(this.url_2,1000)){
//            return this.url_2;
//        }
        return "";
    }
}
