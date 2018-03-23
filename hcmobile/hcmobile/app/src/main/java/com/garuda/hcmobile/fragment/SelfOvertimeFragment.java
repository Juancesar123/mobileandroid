package com.garuda.hcmobile.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.adapter.SelfListManAttStickyList;
import com.garuda.hcmobile.adapter.SelfListOvertimeStickyList;
import com.garuda.hcmobile.model.AManAttData;
import com.garuda.hcmobile.model.AOvertimeData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.ManAttDataRequest;
import com.garuda.hcmobile.model.ManAttDataResponse;
import com.garuda.hcmobile.model.OvertimeDataRequest;
import com.garuda.hcmobile.model.OvertimeDataResponse;
import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.util.StickyListHeadersListViewWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by user on 5/19/2016.
 */
public class SelfOvertimeFragment extends Fragment {
    private View rootView;
    private TMSEmpFragment parentFragment;

    public static SelfOvertimeFragment newInstance() {
        SelfOvertimeFragment f = new SelfOvertimeFragment();
        return f;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.activity_stickylistheaders,
                wrapper, true);
        //rootView.setBackgroundResource(R.drawable.seat);
        String category = "";

        if(parentFragment==null){
            parentFragment=(TMSEmpFragment)this.getParentFragment().getParentFragment();
        }
        Date date = new Date();
        String monthNowFull = new SimpleDateFormat("MMMMM").format(date);
        String monthNow2Digit = new SimpleDateFormat("MM").format(date);
        String yearNow4Figit = new SimpleDateFormat("yyyy").format(date);

        loadOvertimeData(monthNow2Digit,yearNow4Figit,this.getActivity());

        return rootView;
    }

    private void loadOvertimeData(final String month, final String year, final Activity activity){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Load Overtime Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getListOvertimeRequest(new OvertimeDataRequest(device_type, device_id, nopeg,year,month, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<OvertimeDataResponse>() {
                            @Override
                            public void success(final OvertimeDataResponse overtimeDataResponse, Response response) {
                                progressDialog.dismiss();
                                if(overtimeDataResponse.getStatus()==1){
                                    Date date = new Date();
                                    String monthNow2Digit = new SimpleDateFormat("MM").format(date);
                                    String yearNow4Figit = new SimpleDateFormat("yyyy").format(date);
                                    StickyListHeadersListView listView = (StickyListHeadersListView) rootView.findViewById(R.id.activity_stickylistheaders_listview);
                                    listView.setFitsSystemWindows(true);
//                                    listView.setBackgroundResource(R.drawable.seat);
                                    AlphaInAnimationAdapter animationAdapter;
                                    List<AOvertimeData> aDataList = overtimeDataResponse.getData();
                                    SelfListOvertimeStickyList adapterLeaveList = new SelfListOvertimeStickyList(
                                                    activity,parentFragment,(BaseContainerFragment)getParentFragment(), overtimeDataResponse.getData(),yearNow4Figit,monthNow2Digit);
                                    animationAdapter = new AlphaInAnimationAdapter(adapterLeaveList);
                                    StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(
                                            animationAdapter);
                                    stickyListHeadersAdapterDecorator
                                            .setListViewWrapper(new StickyListHeadersListViewWrapper(
                                                    listView));
                                    assert animationAdapter.getViewAnimator() != null;
                                    animationAdapter.getViewAnimator().setInitialDelayMillis(500);
                                    assert stickyListHeadersAdapterDecorator.getViewAnimator() != null;
                                    stickyListHeadersAdapterDecorator.getViewAnimator()
                                            .setInitialDelayMillis(500);
                                    listView.setAdapter(stickyListHeadersAdapterDecorator);
                                }else{
                                    parentFragment.getDialog().showDialog(overtimeDataResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getDialog().showDialog("Get Time Data Error, " + error.getMessage());
                                //Log.e("GetLaunchDataError", "error auth " + error.getMessage());
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                parentFragment.getDialog().showDialog("Auth Error, " + error.getMessage());
               // Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });
    }
}
