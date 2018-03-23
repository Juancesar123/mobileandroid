package com.garuda.hcmobile.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.datasource.MenuDAO;
import com.garuda.hcmobile.datasource.UserDAO;
import com.garuda.hcmobile.model.AMenu;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.EmpRequest;
import com.garuda.hcmobile.model.LaunchResponse;
import com.garuda.hcmobile.model.User;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by MBP on 15/8/15.
 */
public class MainActivity extends Activity implements View.OnClickListener {


    private UserDAO userDAO;
    private MenuDAO menuDAO;

    private DialogUniversalInfoUtils dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDAO = new UserDAO(this);
        menuDAO=new MenuDAO(this);
        final User user = userDAO.getUser();
        Intent intent =null;
        if(MyApplication.isRooted()){
            dialog = new DialogUniversalInfoUtils(this);
            dialog.showDialog("Sorry, Application close cause your device has been rooted");
            dialog.getmDialogOKButton().setOnClickListener(this);
        }else if(user==null){
            intent = new Intent(MainActivity.this,HCMobRegisterActivity.class);
            startActivity(intent);
        }else if(user.getPassword()==null || user.getPassword().equals("")){
            intent = new Intent(MainActivity.this,HCMobVerifyOTPActivity.class);
            startActivity(intent);
        }else {
            //authentikasi dulu menu ma profile nya.
            dialog = new DialogUniversalInfoUtils(this);
            ((MyApplication) getApplication()).setUser(userDAO.getUser());
            ((MyApplication) getApplication()).setMenus(menuDAO.getMenus());
            menu_profile_check();

        }
    }

    @Override
    public void onResume() {
        userDAO.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        userDAO.close();
        super.onPause();
    }
    private void menu_profile_check() {

        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getApplication()).getUser().getPassword();
        final String menusCodes=((MyApplication) this.getApplication()).getMenusCode();
        final String position_key=((MyApplication) this.getApplication()).getUser().getPosition_key();
        final String org_unit=((MyApplication) this.getApplication()).getUser().getOrg_unit();
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                //Log.i("USER",((MyApplication)getApplication()).getUser().toString());
                RestClient.getRestAPI().getLaunchData(new EmpRequest(device_type, device_id, nopeg,menusCodes,position_key,org_unit, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<LaunchResponse>() {
                            @Override
                            public void success(final LaunchResponse launchResponse, Response response) {
                                if(launchResponse.getStatus()==1){
                                    if(launchResponse.getData()!=null && launchResponse.getData().getNama()!=null){
                                        //Log.i("u must check","HERE");
                                        User user = ((MyApplication)getApplication()).getUser();
                                        userDAO.updateProfile(user.getId(),
                                                RestClient.DIR_ROOT_SSL+"/"+ launchResponse.getImg(),
                                                launchResponse.getData().getPosition_key(),
                                                launchResponse.getData().getOrg_unit(),
                                                launchResponse.getData().getPosition_name(),
                                                launchResponse.getData().getUnit_short(),
                                                launchResponse.getData().getUnit_stext(),
                                                launchResponse.getData().getNama());
                                        ((MyApplication) getApplication()).setUser(userDAO.getUser());
                                    }
                                    if(launchResponse.getMenu().size()>0){
                                        //Log.i("u must check menu","HERE");
                                        menuDAO.clearMenus();
                                        for (int i=0; i<launchResponse.getMenu().size(); i++) {
                                            AMenu amenu = launchResponse.getMenu().get(i);
                                            menuDAO.insertMenu(amenu.getModule_code(),amenu.getModule_text(),amenu.getMod_icon(),amenu.getMod_status());
                                        }
                                        ((MyApplication) getApplication()).setMenus(menuDAO.getMenus());
                                    }
                                    goDashboard();
                                }else{
                                    dialog.showDialog("Get Launch Data Error, " + launchResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                dialog.showDialog("Get Launch Data Error, " + error.getMessage());
                                Log.e("GetLaunchDataError", "error auth " + error.getMessage());
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
    private void goDashboard() {
        Intent intent = new Intent(this, HCMobHomeLeftMenusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_universal_info_ok:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            break;
        }
    }

}
