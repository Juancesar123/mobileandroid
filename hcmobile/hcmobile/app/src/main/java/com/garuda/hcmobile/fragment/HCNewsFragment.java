package com.garuda.hcmobile.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.adapter.HCNewsAdapter;
import com.garuda.hcmobile.adapter.SelfListLeaveStickyList;
import com.garuda.hcmobile.model.ALeaveData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonRequest;
import com.garuda.hcmobile.model.HCNewsResponse;
import com.garuda.hcmobile.model.NewsModel;
import com.garuda.hcmobile.model.TMSInitResponse;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;
import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
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
public class HCNewsFragment extends Fragment implements OnDismissCallback {

    private View rootView;
    private DialogUniversalInfoUtils dialog;
    HorizontalScrollView hs;
    HCNewsAdapter hcNewsAdapter;
    private static final int INITIAL_DELAY_MILLIS = 300;

    public static HCNewsFragment newInstance() {
        return new HCNewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.list_view,
                wrapper, true);


        checkNews(this.getActivity(), this);

        return rootView;

//        rootView = inflater.inflate(R.layout.fragment_tabs,
//                container, false);
//
//        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
//
//        hcNewsAdapter = new HCNewsAdapter(this.getActivity(),HCNewsAdapter.getDummyContent());
//        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
//                new SwipeDismissAdapter(hcNewsAdapter, this));
//        swingBottomInAnimationAdapter.setAbsListView(listView);
//
//        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
//        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
//                INITIAL_DELAY_MILLIS);
//
//        listView.setClipToPadding(false);
//        listView.setDivider(null);
//        Resources r = getResources();
//        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                8, r.getDisplayMetrics());
//        listView.setDividerHeight(px);
//        listView.setFadingEdgeLength(0);
//        listView.setFitsSystemWindows(true);
//        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
//                r.getDisplayMetrics());
//        listView.setPadding(px, px, px, px);
//        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
//        listView.setAdapter(swingBottomInAnimationAdapter);
//
//        //rootView.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        //getSupportActionBar().setTitle("Google cards social");
//
//
//        this.checkNews(this.getActivity(), this);
//        return rootView;
    }


    private void checkNews(final Activity activity, final HCNewsFragment parentFragment) {
        final MyApplication myApplication = (MyApplication) this.getActivity().getApplication();
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = myApplication.getDevice_type();
        final String device_id = myApplication.getDevice_id();
        final String nopeg = myApplication.getUser().getNopeg();
        final String password = myApplication.getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "News Check....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getHCNews(new CommonRequest(device_type, device_id, nopeg, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<HCNewsResponse>() {
                            @Override
                            public void success(final HCNewsResponse hcNewsResponse, Response response) {
                                progressDialog.dismiss();
                                if (hcNewsResponse.getStatus() == 1) {
                                    ListView listView = (ListView) rootView.findViewById(R.id.list_view);
                                    List<NewsModel> aDataList = hcNewsResponse.getData();
                                    hcNewsAdapter = new HCNewsAdapter(parentFragment.getActivity(), aDataList);
                                    SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                                            new SwipeDismissAdapter(hcNewsAdapter, parentFragment));
                                    swingBottomInAnimationAdapter.setAbsListView(listView);

                                    assert swingBottomInAnimationAdapter.getViewAnimator() != null;
                                    swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                                            INITIAL_DELAY_MILLIS);

                                    listView.setClipToPadding(false);
                                    listView.setDivider(null);
                                    Resources r = getResources();
                                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                            8, r.getDisplayMetrics());
                                    listView.setDividerHeight(px);
                                    listView.setFadingEdgeLength(0);
                                    listView.setFitsSystemWindows(true);
                                    px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                                            r.getDisplayMetrics());
                                    listView.setPadding(px, px, px, px);
                                    listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
                                    listView.setAdapter(swingBottomInAnimationAdapter);
                                } else {
                                    parentFragment.getDialog().showDialog("Get News Error, " + hcNewsResponse.getMsg());
                                    onErrorNews(parentFragment);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getDialog().showDialog("Get News Error, " + error.getMessage());
                                //Log.e("GetLaunchDataError", "error auth " + error.getMessage());
                                onErrorNews(parentFragment);
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                parentFragment.getDialog().showDialog("Get News Error, " + error.getMessage());
                //Log.e("NewsActivity", "error auth " + error.getMessage());
                onErrorNews(parentFragment);
            }

        });
    }

    private void onErrorNews(HCNewsFragment parentFragment){
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        List<NewsModel> aDataList = HCNewsAdapter.getDummyContent();
        hcNewsAdapter = new HCNewsAdapter(parentFragment.getActivity(), aDataList);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                new SwipeDismissAdapter(hcNewsAdapter, parentFragment));
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                INITIAL_DELAY_MILLIS);

        listView.setClipToPadding(false);
        listView.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        listView.setDividerHeight(px);
        listView.setFadingEdgeLength(0);
        listView.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        listView.setPadding(px, px, px, px);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setAdapter(swingBottomInAnimationAdapter);
    }

    public DialogUniversalInfoUtils getDialog() {
        if (dialog == null) {
            dialog = new DialogUniversalInfoUtils(this.getActivity());
        }
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView,
                          @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            hcNewsAdapter.remove(hcNewsAdapter.getItem(position));
        }
    }

    public class MotherActivity extends FragmentActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            HCNewsFragment fragmenttab = new HCNewsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragmenttab).commit();


        }
    }
}
