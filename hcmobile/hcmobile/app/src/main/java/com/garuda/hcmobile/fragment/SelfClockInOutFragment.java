package com.garuda.hcmobile.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.font.RobotoTextView;
import com.garuda.hcmobile.model.AMenu;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.ClockInOutRequest;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.EmpRequest;
import com.garuda.hcmobile.model.LaunchResponse;
import com.garuda.hcmobile.model.LocationCheckResponse;
import com.garuda.hcmobile.model.User;
import com.garuda.hcmobile.util.CustomTextView;
import com.garuda.hcmobile.util.GPSTracker;
import com.garuda.hcmobile.view.FloatLabeledEditText;
import com.garuda.hcmobile.view.HCMobHomeLeftMenusActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 5/19/2016.
 */
public class SelfClockInOutFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private RobotoTextView tvBtnClockInOutCheck=null;
    private RobotoTextView tvBtnClockInOutSubmit=null;
    private FloatLabeledEditText etPlace=null;
    private FloatLabeledEditText etTime=null;
    private String serverTime="";

    private Dialog dialog;
    private CustomTextView textView;
    private Button btnDialog;
    //Location location;
    LocationManager locationManager;
    public static SelfClockInOutFragment newInstance() {
        SelfClockInOutFragment f = new SelfClockInOutFragment();
        return f;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_hcmob_selfclockinout,
                container, false);
        tvBtnClockInOutCheck= (RobotoTextView) rootView.findViewById(R.id.btnClockInCheck);
        tvBtnClockInOutSubmit= (RobotoTextView) rootView.findViewById(R.id.btnClockInSubmit);
        etPlace= (FloatLabeledEditText) rootView.findViewById(R.id.clockinout_place);
        etTime= (FloatLabeledEditText) rootView.findViewById(R.id.clockinout_time);
        tvBtnClockInOutSubmit.setEnabled(false);
        etPlace.setEnabled(false);
        etTime.setEnabled(false);
        //locationManager = (LocationManager) this.getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        textView = (CustomTextView) dialog.findViewById(R.id.text);
        btnDialog = (Button) dialog.findViewById(R.id.button);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDialog.setText("OK");
        initCheckLocation();
        //tvBtnClockInOutCheck.setOnClickListener(this);
        //tvBtnClockInOutSubmit.setOnClickListener(this);
        return rootView;
    }

    private void initCheckLocation(){
        final Location location = checkLocationAvailability();
        if (location == null) {
            textView.setText("Can not load your Location");
            dialog.show();

        }
        final String loc_time = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String loc_area = "initial checking";
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                //Log.i("USER",((MyApplication)getApplication()).getUser().toString());
                RestClient.getRestAPI().checkLocationClockInOut(new ClockInOutRequest(device_type, device_id, nopeg, location.getLongitude(), location.getLatitude(), loc_time, loc_area, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<LocationCheckResponse>() {
                            @Override
                            public void success(final LocationCheckResponse commonResponse, Response response) {
                                if (commonResponse.getStatus() == 1) {
                                    Toast.makeText(rootView.getContext(), "Checking Location Success " , Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(rootView.getContext(), "Checking Location Error " , Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(rootView.getContext(), "Checking Location Error, " + error.getMessage(), Toast.LENGTH_LONG).show();
                                //Log.e("GetLaunchDataError", "error auth " + error.getMessage());
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(rootView.getContext(), "Auth Error, " + error.getMessage(), Toast.LENGTH_LONG).show();
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });
    }

    private Location checkLocationAvailability() {
        tvBtnClockInOutSubmit.setEnabled(false);
        LocationManager service = (LocationManager) rootView.getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean locationEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!locationEnabled) {
            Toast.makeText(rootView.getContext(), "Please Enabled your GPS or Location", Toast.LENGTH_SHORT).show();
        }

        Location lastKnownLocation = new GPSTracker(this.getActivity()).getLocation();
        if(lastKnownLocation==null){
            Toast.makeText(rootView.getContext(), "Location Not Available", Toast.LENGTH_SHORT).show();
        }else {
            long curr_time = Calendar.getInstance().getTimeInMillis();
            long loc_time = lastKnownLocation.getTime();
            if(Math.abs(curr_time-loc_time)>300000){
                Toast.makeText(rootView.getContext(), "Your Last Updated Location Expired" , Toast.LENGTH_SHORT).show();
            }
            if(lastKnownLocation.getAccuracy()>250){
                Toast.makeText(rootView.getContext(), "Your Location Is Not Accurate (Max 250m)" , Toast.LENGTH_SHORT).show();
            }
                return lastKnownLocation;
        }
        return null;
    }

    private double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f/Math.PI);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)* Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }


    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            if (tv.getText().toString().equals(getResources().getString(R.string.clockinout_submit))) {

            } else if (tv.getText().toString().equals(getResources().getString(R.string.clockinout_check))) {
                checkLocationAvailability();
            }
        }
    }
}
