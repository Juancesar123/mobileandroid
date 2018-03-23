package com.garuda.hcmobile.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.adapter.MyStickyListHeadersTimeDataAdapter;
import com.garuda.hcmobile.model.ATimeData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonRequest;
import com.garuda.hcmobile.model.TMSAuth;
import com.garuda.hcmobile.model.TMSInitResponse;
import com.garuda.hcmobile.model.TimeDataRequest;
import com.garuda.hcmobile.model.TimeDataResponse;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;
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
public class TMSEmpFragment extends Fragment implements DialogInterface.OnClickListener {

    private View rootView;
    public FragmentTabHost mTabHost;
    private DialogUniversalInfoUtils dialog;
    TabWidget widget;
    HorizontalScrollView hs;

    public static TMSEmpFragment newInstance() {
        return new TMSEmpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tabs,
                container, false);

        mTabHost = (FragmentTabHost) rootView
                .findViewById(android.R.id.tabhost);

        widget = (TabWidget) rootView.findViewById(android.R.id.tabs);
        hs = (HorizontalScrollView) rootView
                .findViewById(R.id.horizontalScrollView);


        mTabHost.setup(getActivity(), getChildFragmentManager(),android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("empTimeData").setIndicator("Time Data"),
                SelfTimeDataContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("empSelfMantAtt").setIndicator("Manual Attendance"),
                SelfManAttContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("empSelfLeave").setIndicator("Leave"),
                SelfLeaveContainerFragment.class, null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                //Log.i("mTabHost","onTabChange");
                setTabColor(mTabHost);
            }
        });
        int color_ga_menus = getResources().getColor(R.color.color_ga_menus);
        int color_ga_selected_menu = getResources().getColor(R.color.color_ga_selected_menu);
        color_ga_menus = Color.argb(0xff, Color.red(color_ga_menus), Color.green(color_ga_menus),
                Color.blue(color_ga_menus));
        color_ga_selected_menu = Color.argb(0xff, Color.red(color_ga_selected_menu), Color.green(color_ga_selected_menu),
                Color.blue(color_ga_selected_menu));
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(color_ga_selected_menu);
        this.checkTMSAUth(this.getActivity(), this);
        return rootView;
    }

    //Change The Backgournd Color of Tabs
    public void setTabColor(TabHost tabhost) {
        int color_ga_menus = getResources().getColor(R.color.color_ga_menus);
        int color_ga_selected_menu = getResources().getColor(R.color.color_ga_selected_menu);
        color_ga_menus = Color.argb(0xff, Color.red(color_ga_menus), Color.green(color_ga_menus),
                Color.blue(color_ga_menus));
        color_ga_selected_menu = Color.argb(0xff, Color.red(color_ga_selected_menu), Color.green(color_ga_selected_menu),
                Color.blue(color_ga_selected_menu));

        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(color_ga_menus); //unselected
        if(tabhost.getCurrentTab()==0)
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(color_ga_selected_menu); //1st tab selected
        else
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(color_ga_selected_menu); //2nd tab selected
    }

    private void checkTMSAUth(final Activity activity, final TMSEmpFragment parentFragment) {
        final MyApplication myApplication = (MyApplication)this.getActivity().getApplication();
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = myApplication.getDevice_type();
        final String device_id = myApplication.getDevice_id();
        final String nopeg = myApplication.getUser().getNopeg();
        final String password = myApplication.getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "TMS Check....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getTMSInitData(new CommonRequest(device_type, device_id, nopeg, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<TMSInitResponse>() {
                            @Override
                            public void success(final TMSInitResponse tmsInitResponse, Response response) {
                                progressDialog.dismiss();

                                Toast.makeText(getActivity(), tmsInitResponse.getStatus()+" | "+tmsInitResponse.getData().getCan_espkl(), Toast.LENGTH_LONG).show();
                                if (tmsInitResponse.getStatus() == 1) {
                                    if (tmsInitResponse.getData().getCan_espkl() == 1) {
                                        mTabHost.addTab(mTabHost.newTabSpec("empSelfOvertime").setIndicator("Overtime"),
                                                SelfOvertimeContainerFragment.class, null);
                                    }if (tmsInitResponse.getData().getCan_clock_inout() == 1) {
                                        mTabHost.addTab(mTabHost.newTabSpec("empSelfClockInOut").setIndicator("SelfClockInOut"),
                                                SelfClockInOutFragment.class, null);
                                    }

                                    for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                                        final TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
                                                .findViewById(android.R.id.title);
                                        tv.setTypeface(myApplication.getTypeface());
                                        if (tv == null)
                                            continue;
                                        else
                                            tv.setTextColor(0xFFFFFFFF);
                                    }
//                                    if (tmsInitResponse.getData().getCan_leave() == 1) {
//                                    }
                                } else {
                                    parentFragment.getDialog().showDialog("fail to retrieve auth");
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

    public DialogUniversalInfoUtils getDialog() {
        if (dialog == null) {
            dialog = new DialogUniversalInfoUtils(this.getActivity());
        }
        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public class MotherActivity extends FragmentActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TMSEmpFragment fragmenttab = new TMSEmpFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragmenttab).commit();


        }
    }
}
