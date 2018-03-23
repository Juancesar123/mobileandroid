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
import com.garuda.hcmobile.adapter.SupListLeaveStickyList;
import com.garuda.hcmobile.adapter.SupListManAttStickyList;
import com.garuda.hcmobile.model.AEmp;
import com.garuda.hcmobile.model.AManAttData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonRequest;
import com.garuda.hcmobile.model.ManAttDataRequest;
import com.garuda.hcmobile.model.ManAttDataResponse;
import com.garuda.hcmobile.model.SubordinatResponse;
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
public class SupManAttFragment extends Fragment {
    private View rootView;
    private TMSSupFragment parentFragment;

    public static SupManAttFragment newInstance() {
        SupManAttFragment f = new SupManAttFragment();
        return f;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.activity_stickylistheaders,
                wrapper, true);
        String category = "";
        if(parentFragment==null){
            parentFragment=(TMSSupFragment)this.getParentFragment().getParentFragment();
        }
        if(parentFragment.getaEmps()==null || parentFragment.getaEmps().size()==0) {
            loadSubordinat(this.getActivity());
        }else{
            loadStickyList(this.getActivity(),parentFragment.getaEmps());
        }
//        Date date = new Date();
//        String yearNow4Figit = new SimpleDateFormat("yyyy").format(date);
//
//        loadManAttData(yearNow4Figit,this.getActivity());

        return rootView;
    }

    private void loadStickyList(Activity activity,List<AEmp> aEmpList){
        StickyListHeadersListView listView = (StickyListHeadersListView) rootView.findViewById(R.id.activity_stickylistheaders_listview);
        listView.setFitsSystemWindows(true);
        AlphaInAnimationAdapter animationAdapter;
//                                    SelfListOvertimeStickyList
        SupListManAttStickyList adapterSocial = new SupListManAttStickyList(
                activity,parentFragment,(BaseContainerFragment)getParentFragment(), aEmpList);
        animationAdapter = new AlphaInAnimationAdapter(adapterSocial);
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
    }

    private void loadSubordinat(final Activity activity){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Load Subordinat List....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getSubordinat(new CommonRequest(device_type, device_id, nopeg, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<SubordinatResponse>() {
                            @Override
                            public void success(final SubordinatResponse subordinatResponse, Response response) {
                                progressDialog.dismiss();
                                if(subordinatResponse.getStatus()==1){
//                                    Date date = new Date();
//                                    String monthNowFull = new SimpleDateFormat("MM").format(date);
//                                    String monthNow2Digit = new SimpleDateFormat("MM").format(date);
//                                    String yearNow4Figit = new SimpleDateFormat("yyyy").format(date);
                                    List<AEmp> aEmps = subordinatResponse.getData();
                                    parentFragment.setaEmps(aEmps);
                                    loadStickyList(activity,aEmps);
                                }else{
                                    parentFragment.getDialog().showDialog(subordinatResponse.getMessage());
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
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });

    }

//    private void loadManAttData(final String year, final Activity activity){
//        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
//        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
//        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
//        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
//        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
//        //get Launch Data from Server
//        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Load Man.Att. Data....");
//        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
//            @Override
//            public void success(final AuthenticateResponse authenticateResponse, Response response) {
//                RestClient.getRestAPI().getListManAttRequest(new ManAttDataRequest(device_type, device_id, nopeg,year, dateTime),
//                        authenticateResponse.getToken(),
//                        new Callback<ManAttDataResponse>() {
//                            @Override
//                            public void success(final ManAttDataResponse manAttDataResponse, Response response) {
//                                progressDialog.dismiss();
//                                if(manAttDataResponse.getStatus()==1){
//                                    Date date = new Date();
//                                    String yearNow4Figit = new SimpleDateFormat("yyyy").format(date);
//                                    StickyListHeadersListView listView = (StickyListHeadersListView) rootView.findViewById(R.id.activity_stickylistheaders_listview);
//                                    listView.setFitsSystemWindows(true);
//                                    AlphaInAnimationAdapter animationAdapter;
//                                    List<AManAttData> aDataList = manAttDataResponse.getData();
//                                    SelfListManAttStickyList adapterLeaveList = new SelfListManAttStickyList(
//                                                    activity,parentFragment,(BaseContainerFragment)getParentFragment(), manAttDataResponse.getData(),yearNow4Figit);
//                                    animationAdapter = new AlphaInAnimationAdapter(adapterLeaveList);
//                                    StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(
//                                            animationAdapter);
//                                    stickyListHeadersAdapterDecorator
//                                            .setListViewWrapper(new StickyListHeadersListViewWrapper(
//                                                    listView));
//                                    assert animationAdapter.getViewAnimator() != null;
//                                    animationAdapter.getViewAnimator().setInitialDelayMillis(500);
//                                    assert stickyListHeadersAdapterDecorator.getViewAnimator() != null;
//                                    stickyListHeadersAdapterDecorator.getViewAnimator()
//                                            .setInitialDelayMillis(500);
//                                    listView.setAdapter(stickyListHeadersAdapterDecorator);
//                                }else{
//                                    parentFragment.getDialog().showDialog(manAttDataResponse.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void failure(RetrofitError error) {
//                                progressDialog.dismiss();
//                                parentFragment.getDialog().showDialog("Get Time Data Error, " + error.getMessage());
//                                Log.e("GetLaunchDataError", "error auth " + error.getMessage());
//                            }
//
//                        });
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                parentFragment.getDialog().showDialog("Auth Error, " + error.getMessage());
//                Log.e("AuthActivity", "error auth " + error.getMessage());
//            }
//
//        });
//    }
}
