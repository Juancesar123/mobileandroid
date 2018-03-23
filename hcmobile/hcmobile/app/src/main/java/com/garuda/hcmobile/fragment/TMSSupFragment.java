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
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.AEmp;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonRequest;
import com.garuda.hcmobile.model.TMSInitResponse;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 5/19/2016.
 */
public class TMSSupFragment extends Fragment implements DialogInterface.OnClickListener {

    private View rootView;
    public FragmentTabHost mTabHost;
    private DialogUniversalInfoUtils dialog;
    TabWidget widget;
    HorizontalScrollView hs;
    private List<AEmp> aEmps;

    public static TMSSupFragment newInstance() {
        return new TMSSupFragment();
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
        mTabHost.addTab(mTabHost.newTabSpec("supTimeData").setIndicator("Time Data"),
                SupTimeDataContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("supSelfLeave").setIndicator("Leave"),
                SupLeaveContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("supSelfOvertime").setIndicator("Overtime"),
                SupOvertimeContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("supSelfMantAtt").setIndicator("Manual Attendance"),
                SupManAttContainerFragment.class, null);


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
        MyApplication myApplication = ((MyApplication) this.getActivity().getApplication());
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            final TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title);
            tv.setTypeface(myApplication.getTypeface());
            if (tv == null)
                continue;
            else
                tv.setTextColor(0xFFFFFFFF);
        }
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

    public DialogUniversalInfoUtils getDialog() {
        if (dialog == null) {
            dialog = new DialogUniversalInfoUtils(this.getActivity());
        }
        return dialog;
    }

    public List<AEmp> getaEmps() {
        return aEmps;
    }

    public void setaEmps(List<AEmp> aEmps) {
        this.aEmps = aEmps;
    }

    public String[] getArrayEmps(){
        String[] empString=new String[aEmps.size()];
        if(aEmps!=null && aEmps.size()>0) {
            for (int i = 0; i < aEmps.size(); i++) {
                empString[i] = aEmps.get(i).getGabungan();
            }
        }
        return empString;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public class MotherActivity extends FragmentActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TMSSupFragment fragmenttab = new TMSSupFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragmenttab).commit();


        }
    }
}
