package com.garuda.hcmobile.model;

import android.util.Base64;
import android.util.Log;

import com.garuda.hcmobile.util.AESCrypt;
import com.garuda.hcmobile.util.AesCbcWithIntegrity;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by MBP on 1/9/15.
 */
public class SlipMonthResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

    private List<SlipMonth> slipMonths;
    private List<SlipFATAMonth> slipFATAMonths;
    private List<String> slipMonthsString;
    private List<String> slipFATAMonthsString;
    private SecretKeySpec keyspec;
    private IvParameterSpec ivspec;
    private Cipher cipher;


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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
/*
    public List<SlipMonth> getSlipMonth(String keys){
        AesCbcWithIntegrity.SecretKeys aesKeys = null;
        try {
            aesKeys = AesCbcWithIntegrity.keys(keys);
        } catch (InvalidKeyException e) {

        }
        return getSlipMonth(aesKeys);
    }*/

    public List<String> getSlipMonthString(String seckey){
        try {
            if(slipMonths==null){
                slipMonths = getSlipMonth(seckey);
            }
            if(slipMonthsString==null) {
                slipMonthsString = new ArrayList<String>();
                for (int i = 0; i < slipMonths.size(); i++) {
                    SlipMonth slipMonth = slipMonths.get(i);
                    slipMonthsString.add(slipMonth.getText());
                }
            }

        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }
        return slipMonthsString;
    }

    public List<SlipMonth> getSlipMonth(String seckey){
        if(slipMonths!=null){
            return slipMonths;
        }
        try {
            slipMonths = new ArrayList<SlipMonth>();
            AESCrypt crypt=new AESCrypt(seckey);
            String jsonEncode=crypt.decrypt(this.data);
            JSONArray jr = new JSONArray(jsonEncode);
            for(int i=0;i<jr.length();i++){
                JSONObject obj=jr.getJSONObject(i);
                String text= jr.getJSONObject(i).getString("text");
                String seqno= jr.getJSONObject(i).getString("seqno");
                String paydate= jr.getJSONObject(i).getString("paydate");
                String variant= jr.getJSONObject(i).getString("variant");
                SlipMonth slipMonth = new SlipMonth(text,seqno,paydate,variant);
                slipMonths.add(slipMonth);
            }
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }
        return slipMonths;
    }

    public List<String> getSlipFATAMonthString(String seckey){
        try {
            if(slipFATAMonths==null){
                slipFATAMonths = getSlipFATAMonth(seckey);
            }
            if(slipMonthsString==null) {
                slipMonthsString = new ArrayList<String>();
                for (int i = 0; i < slipFATAMonths.size(); i++) {
                    SlipFATAMonth slipMonth = slipFATAMonths.get(i);
                    slipMonthsString.add(slipMonth.getText());
                }
            }

        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }
        return slipMonthsString;
    }


    public List<SlipFATAMonth> getSlipFATAMonth(String seckey){
        if(slipFATAMonths!=null){
            return slipFATAMonths;
        }
        try {
            slipFATAMonths = new ArrayList<SlipFATAMonth>();
            AESCrypt crypt=new AESCrypt(seckey);
            String jsonEncode=crypt.decrypt(this.data);
            JSONArray jr = new JSONArray(jsonEncode);
            for(int i=0;i<jr.length();i++){
                JSONObject obj=jr.getJSONObject(i);
                String text= jr.getJSONObject(i).getString("text");
                String dbulan= jr.getJSONObject(i).getString("dbulan");
                String ada= jr.getJSONObject(i).getString("ada");
                String nopeg= jr.getJSONObject(i).getString("nopeg");
                SlipFATAMonth slipFATAMonth = new SlipFATAMonth(text,dbulan,ada,nopeg);
                slipFATAMonths.add(slipFATAMonth);
            }
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
        }
        return slipFATAMonths;
    }

}
