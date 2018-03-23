package com.garuda.hcmobile.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.adapter.DrawerMenuAdapter;
import com.garuda.hcmobile.adapter.ViewHolder;
import com.garuda.hcmobile.font.RobotoTextView;
import com.garuda.hcmobile.fragment.HCNewsFragment;
import com.garuda.hcmobile.fragment.SelfClockInOutFragment;
import com.garuda.hcmobile.fragment.SelfClockInOutFragment2;
import com.garuda.hcmobile.fragment.SlipFATAFragment;
import com.garuda.hcmobile.fragment.SlipGajiFragment;
import com.garuda.hcmobile.fragment.TMSEmpFragment;
import com.garuda.hcmobile.fragment.TMSSupFragment;
import com.garuda.hcmobile.model.MenuModel;
import com.garuda.hcmobile.model.User;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;
import com.garuda.hcmobile.util.GCMRegistrationIntentService;
import com.garuda.hcmobile.util.ImageUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class HCMobHomeLeftMenusActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerMenuAdapter mDrawerMenuAdapter;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private DialogUniversalInfoUtils dialog;
    private List<MenuModel> menuModels;
    private boolean mShouldFinish = false;
    private Handler mHandler;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcmob_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HC-Mobile");
        //downloadLaunchData();

        dialog = new DialogUniversalInfoUtils(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_menu);
        mTitle = mDrawerTitle = getTitle();
        mDrawerList = (ListView) findViewById(R.id.list_view);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        View headerView = getLayoutInflater().inflate(
                R.layout.activity_hcmob_topnav, mDrawerList, false);
        setupProfile(headerView);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        ImageView iv = (ImageView) headerView.findViewById(R.id.image);
        String url=((MyApplication)getApplication()).getUser().getPhoto();
        //Toast.makeText(getApplicationContext(), "Photo : "+url, Toast.LENGTH_LONG).show();
        ImageUtil.displayRoundImage(iv,((MyApplication)getApplication()).getUser().getPhoto(), null);
        mDrawerList.addHeaderView(headerView);// Add header before adapter (for
        // pre-KitKat)
        menuModels=((MyApplication)getApplication()).getMenuModels();
        mDrawerMenuAdapter = new DrawerMenuAdapter(this,menuModels,((MyApplication)getApplication()));
        mDrawerList.setAdapter(mDrawerMenuAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        int color = getResources().getColor(R.color.color_ga_menus);
        color = Color.argb(0xff, Color.red(color), Color.green(color),
                Color.blue(color));
        mDrawerList.setBackgroundColor(color);
        mDrawerList.getLayoutParams().width = (int) getResources()
                .getDimension(R.dimen.drawer_width_social);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.dxxx__op, R.string.dxxx__cp) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mHandler = new Handler();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            int position = 0;
            selectItem(position, (int)menuModels.get(position).getId());
            mDrawerLayout.openDrawer(mDrawerList);
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success
                    String token = intent.getStringExtra("token");
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                }
            }
        };

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if(view.getTag()!=null) {
                mDrawerLayout.closeDrawer(mDrawerList);
                selectItem(position - 1, (int) menuModels.get(position - 1).getId());
            }
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupProfile(View headerView){
        MyApplication myApplication = ((MyApplication)getApplication());
        User user=myApplication.getUser();
        TextView homeLeftMenuNama = (RobotoTextView) headerView.findViewById(R.id.home_left_menu_nama);
        homeLeftMenuNama.setTypeface(myApplication.getTypeface());
        homeLeftMenuNama.setText(user.getNama());
        TextView homeLeftMenuNopeg = (RobotoTextView) headerView.findViewById(R.id.home_left_menu_nopeg);
        homeLeftMenuNopeg.setTypeface(myApplication.getTypeface());
        homeLeftMenuNopeg.setText(user.getNopeg());
        TextView homeLeftMenuPos = (RobotoTextView) headerView.findViewById(R.id.home_left_menu_pos);
        homeLeftMenuPos.setTypeface(myApplication.getTypeface());
        homeLeftMenuPos.setText(user.getPosition_name());
        TextView homeLeftMenuUnit = (RobotoTextView) headerView.findViewById(R.id.home_left_menu_unit);
        homeLeftMenuUnit.setTypeface(myApplication.getTypeface());
        homeLeftMenuUnit.setText(user.getUnit_stext() + " (" + user.getUnit_short() + ")");
    }

    private void selectItem(int position, int modCode) {
        Fragment fragment = getFragmentByDrawerTag(modCode);
        commitFragment(fragment);

        mDrawerList.setItemChecked(position+1, true);
        setTitle(menuModels.get(position).getText());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private Fragment getFragmentByDrawerTag(int modCode) {
        Fragment fragment;
        if (modCode == MenuModel.MODCODE_HCNEWS) {
            fragment = HCNewsFragment.newInstance();
        } else if (modCode == MenuModel.MODCODE_TMS_EMP) {
            fragment = TMSEmpFragment.newInstance();
        }else if (modCode == MenuModel.MODCODE_TMS_SUP) {
            fragment = TMSSupFragment.newInstance();
        }else if (modCode == MenuModel.MODCODE_TMS_CLOCKINOUT) {
            fragment = SelfClockInOutFragment2.newInstance();
        }else if (modCode == MenuModel.MODCODE_SLIP_GAJI) {
            fragment = SlipGajiFragment.newInstance();
        }else if (modCode == MenuModel.MODCODE_SLIP_FATA) {
            fragment =  SlipFATAFragment.newInstance();
        } else {
            fragment = new Fragment();
        }
        mShouldFinish = false;
        return fragment;
    }
    public void commitFragment(Fragment fragment) {
        // Using Handler class to avoid lagging while
        // committing fragment in same time as closing
        // navigation drawer
        mHandler.post(new CommitFragmentRunnable(fragment));
    }

    private class CommitFragmentRunnable implements Runnable {

        private Fragment fragment;

        public CommitFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_menu, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
