package com.garuda.hcmobile;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RestClient {
    private static Api restAPI;
    //private static String ROOT =  "https://developer.okvalet.com/public/";
    //private static String ROOT =  "http://developer.okvalet.com/public/";
    //private static String ROOT =  "http://api.ridehandman.com/public/";
    //public static String ROOT =  "http://192.168.9.67/hc_mobile/public/";
    public static String DIR_ROOT= "http://hconline.garuda-indonesia.com/svc/hc_mobile/public";
    public static String DIR_ROOT_SSL= "https://hconline.garuda-indonesia.com/svc/hc_mobile/public";
    //    public static String DIR_ROOT="https://hconline.garuda-indonesia.com/hc_mobile/public";
    public static String ROOT =  RestClient.DIR_ROOT+"/index.php";
    public static String ROOT_SSL =  RestClient.DIR_ROOT_SSL+"/index.php";
    //private static String ROOT =  "http://172.25.143.228/hc_mobile/public/";
    private RestClient(){}

    public static Api getRestAPI(){
        return restAPI;
    }

//    private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
//            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//
//        // Loading CAs from an InputStream
//        CertificateFactory cf = null;
//        cf = CertificateFactory.getInstance("X.509");
//
//        Certificate ca;
//        // I'm using Java7. If you used Java6 close it manually with finally.
//        try (InputStream cert = context.getResources().openRawResource(R.raw.your_certificate)) {
//            ca = cf.generateCertificate(cert);
//        }
//
//        // Creating a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore   = KeyStore.getInstance(keyStoreType);
//        keyStore.load(null, null);
//        keyStore.setCertificateEntry("ca", ca);
//
//        // Creating a TrustManager that trusts the CAs in our KeyStore.
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);
//
//        // Creating an SSLSocketFactory that uses our TrustManager
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, tmf.getTrustManagers(), null);
//
//        return sslContext;
//    }

    public static void createAdapter(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        OkHttpClient okHttpClient = new OkHttpClient();

        // loading CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream cert = context.getResources().openRawResource(R.raw.ga);
        Certificate ca;
        try {
            ca = cf.generateCertificate(cert);
        } finally { cert.close(); }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

        // creating a RestAdapter using the custom client
        RestAdapter builder = new RestAdapter.Builder()
                .setEndpoint(ROOT_SSL)
                .setClient(new OkClient(okHttpClient))
                .build();
        restAPI = builder.create(Api.class);
    }

    public static void setupRestClient(){
        RestAdapter builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        restAPI = builder.create(Api.class);
    }
}