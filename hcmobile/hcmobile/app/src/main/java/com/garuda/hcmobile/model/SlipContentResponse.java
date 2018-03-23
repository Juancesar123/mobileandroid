package com.garuda.hcmobile.model;

import android.util.Log;

import com.garuda.hcmobile.util.AESCrypt;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by MBP on 1/9/15.
 */
public class SlipContentResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

    private String encryptedContent;



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEncryptedContent(String seckey){
        try {
            AESCrypt crypt=new AESCrypt(seckey);
            encryptedContent= crypt.decrypt(this.data);
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());

        }

        return encryptedContent;
    }

}
