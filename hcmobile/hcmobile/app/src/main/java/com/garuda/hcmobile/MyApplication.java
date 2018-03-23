package com.garuda.hcmobile;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.garuda.hcmobile.datasource.MenuDAO;
import com.garuda.hcmobile.datasource.UserDAO;
import com.garuda.hcmobile.model.Menu;
import com.garuda.hcmobile.model.MenuModel;
import com.garuda.hcmobile.model.User;
import com.garuda.hcmobile.util.MyLifecycleHandler;
import com.garuda.hcmobile.view.component.FontsOverride;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private User user;
    private List<Menu> menus;
    private String token="";
    private String device_id="";
    private String device_type="";
    private UserDAO userDAO = null;

    private String slipKey="";
    private String slipFATAKey="";

    private Typeface typeface=null;

    public String getSlipFATAKey() {
        return slipFATAKey;
    }

    public void setSlipFATAKey(String slipFATAKey) {
        this.slipFATAKey = slipFATAKey;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userDAO = new UserDAO(this);
        //Log.i("halo","DISINI");
        try {
            RestClient.createAdapter(this.getBaseContext());
        }catch(Exception ex){
            ex.printStackTrace();
         //   RestClient.setupRestClient();
        }
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }
    public static boolean isRooted() {
        return findBinary("su") || checkRootMethod1() || checkRootMethod3() || checkRootMethod2();
    }
    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }
    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su" };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if ( new File( where + binaryName ).exists() ) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
    public Typeface getTypeface(){
        if(typeface==null){
            typeface = Typeface.createFromAsset(this.getAssets(), "fonts/tfforever-demi.ttf");
        }
        return typeface;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice_id() {
        if(device_id.equals("")){
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                if(telephonyManager.getDeviceId()!=null) {
                    device_id = telephonyManager.getDeviceId();
                }else{
                    device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                }

            }catch (SecurityException secEx){
                device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return device_id;
    }

    public String getSlipKey(){
        return this.slipKey;
    }
    public void setSlipKey(String slipKey){
        this.slipKey=slipKey;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_type() {
        if(device_type.equals("")){
            device_type=UserDAO.getAppUsername();
        }
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public User getUser() {
        if(user==null){
            user=userDAO.getUser();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Menu> getMenus() {
        if(menus==null){
            menus=new MenuDAO(this).getMenus();
        }

        return menus;
    }
    public String getMenusCode(){
        String sRet="";
        for(int i=0;i<menus.size();i++){
            Menu menu = menus.get(i);
            if(i>0){
                sRet+=",";
            }
            sRet+=menu.getCode();
        }
        return sRet;
    }

    public List<MenuModel> getMenuModels(){
        List<Menu> menus = this.getMenus();
        List<MenuModel> menuModels = new ArrayList<MenuModel>();
        for(int i=0;i<menus.size();i++){
            Menu menu = menus.get(i);
            MenuModel menuModel = new MenuModel(menu.getCode(),menu.getName(),menu.getIcon(),menu.getStatus());
            //Log.i("menu "+i,menuModel.toString());
            menuModels.add(menuModel);
        }
        return menuModels;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
