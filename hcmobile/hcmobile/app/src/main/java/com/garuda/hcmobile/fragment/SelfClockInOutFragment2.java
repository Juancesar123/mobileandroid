package com.garuda.hcmobile.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.ALocation;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.ClockInOutRequest;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.DummyModel;
import com.garuda.hcmobile.model.LocationCheckResponse;
import com.garuda.hcmobile.util.GPSTracker;
import com.garuda.hcmobile.view.pzv.PullToZoomListViewEx;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.adapter.ParallaxSocialAdapter;
import com.garuda.hcmobile.util.DummyContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 9/6/2017.
 */
public class SelfClockInOutFragment2 extends Fragment implements View.OnClickListener  {
    private View rootView;
    public static final String TAG = "Parallax social";
    private Location startLocation=null;
    ImageView iv;
    private PullToZoomListViewEx listView;
    LocationManager locationManager;
    public static SelfClockInOutFragment2 newInstance() {
        SelfClockInOutFragment2 f = new SelfClockInOutFragment2();
        return f;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_parallax_social,
                container, false);
        initCheckLocation();

        iv = (ImageView) rootView.findViewById(R.id.header_parallax_social_new_image);

        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(rootView.getContext(), "Photo of user",
                        Toast.LENGTH_SHORT).show();
            }
        });

        listView = (PullToZoomListViewEx) rootView.findViewById(R.id.paralax_social_list_view);
        listView.setShowDividers(0);
        listView.setAdapter(new ParallaxSocialAdapter(this,rootView.getContext(), new ArrayList<ALocation>(), false));


        return rootView;

    }

    @Override
    public void onClick(View v) {

    }

    private void initCheckLocation(){
        Location locationTemp = checkLocationAvailability();
        if (locationTemp == null) {
            //Toast.makeText(rootView.getContext(), "Location Not Found," , Toast.LENGTH_LONG).show();
            return;
        }
        final Location location=locationTemp;
        final String loc_time = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String loc_area = "initial checking";
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        final SelfClockInOutFragment2 parentFragment=this;
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
                                    Toast.makeText(rootView.getContext(), "Size Locations :"+commonResponse.getALocation().size() , Toast.LENGTH_LONG).show();
                                    //ArrayList<ALocation> list = new ArrayList<>();
                                    //list.add(new DummyModel(0, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/0.jpg", "Jane Smith", R.string.fontello_heart_empty));
                                    //list.add(new DummyModel(1, "http://pengaja.com/uiapptemplate/newphotos/listviews/googlecards/travel/1.jpg", "Jane Smith", R.string.fontello_heart_empty));
                                    listView.setAdapter(new ParallaxSocialAdapter(parentFragment,rootView.getContext(), commonResponse.getALocation(), false));
                                    startLocation=location;
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


    public void sendCheckInLocation(final String place,final int iPlace){
        final Location location = checkLocationAvailability();
        if (location == null) {
            Toast.makeText(rootView.getContext(), "Location Not Found" , Toast.LENGTH_LONG).show();
            return;
        }
        final String loc_time = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String loc_area = iPlace+"|"+place;
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().setClockInOut(new ClockInOutRequest(device_type, device_id, nopeg, location.getLongitude(), location.getLatitude(), loc_time,loc_area , dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                if (commonResponse.getStatus() == 1) {
                                    Toast.makeText(rootView.getContext(), "Checked on "+place , Toast.LENGTH_LONG).show();
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
        LocationManager service = (LocationManager) rootView.getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean locationEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!locationEnabled) {
            Toast.makeText(rootView.getContext(), "Please Enabled your GPS or Location", Toast.LENGTH_SHORT).show();
            return null;
        }

        Location lastKnownLocation = new GPSTracker(this.getActivity()).getLocation();
        if(lastKnownLocation==null){
            Toast.makeText(rootView.getContext(), "Location Not Available", Toast.LENGTH_SHORT).show();
            return null;
        }else {
            long curr_time = Calendar.getInstance().getTimeInMillis();
            long loc_time = lastKnownLocation.getTime();
            if(Math.abs(curr_time-loc_time)>300000){
                Toast.makeText(rootView.getContext(), "Your Last Updated Location Expired" , Toast.LENGTH_SHORT).show();
                return null;
            }
            if(lastKnownLocation.getAccuracy()>250){
                Toast.makeText(rootView.getContext(), "Your Location Is Not Accurate (Max 250m)" , Toast.LENGTH_SHORT).show();
                return null;
            }
            return lastKnownLocation;
        }
    }
}
