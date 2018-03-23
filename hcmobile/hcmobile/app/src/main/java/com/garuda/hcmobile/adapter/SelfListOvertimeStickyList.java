package com.garuda.hcmobile.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.fragment.BaseContainerFragment;
import com.garuda.hcmobile.fragment.FormManAttFragment;
import com.garuda.hcmobile.fragment.FormOvertimeFragment;
import com.garuda.hcmobile.fragment.TMSEmpFragment;
import com.garuda.hcmobile.model.AOvertimeData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.OvertimeDataRequest;
import com.garuda.hcmobile.model.OvertimeDataResponse;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class SelfListOvertimeStickyList extends ArrayAdapter<String>
        implements StickyListHeadersAdapter, OnClickListener {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<AOvertimeData> aTimeDataList;
    private String headerMonth;
    private TMSEmpFragment parentFragment;
    private String year;
    private String month;
    private BaseContainerFragment baseContainerFragment;

    public SelfListOvertimeStickyList(final Context context, final TMSEmpFragment tmsEmpFragment, BaseContainerFragment baseContainerFragment, List<AOvertimeData> aTimeDataList, String year, String month) {
        mContext = context;
        this.baseContainerFragment = baseContainerFragment;
        this.parentFragment = tmsEmpFragment;
        this.aTimeDataList = aTimeDataList;
        this.year = year;
        this.month = month;
        this.headerMonth = month + "." + year;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (aTimeDataList.size() == 0) {
            add("Row number ");
        }
        for (int i = 0; i < aTimeDataList.size(); i++) {
            add("Row number ");
        }
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_item_sticky_overtime, parent, false);
            holder = new ViewHolder();

            holder.headerTimeData = (TextView) convertView
                    .findViewById(R.id.list_item_date);
            holder.plan = (TextView) convertView
                    .findViewById(R.id.list_item_plan);
            holder.reason = (TextView) convertView
                    .findViewById(R.id.list_item_reason);
            holder.approve = (TextView) convertView
                    .findViewById(R.id.list_item_approve);
            holder.pay = (TextView) convertView
                    .findViewById(R.id.list_item_pay);
            holder.status = (TextView) convertView
                    .findViewById(R.id.list_item_status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(aTimeDataList.size()>0) {
            AOvertimeData overtimeData = aTimeDataList.get(position);
            holder.headerTimeData.setText(overtimeData.getHari() + " - " + overtimeData.getTanggal());
            holder.plan.setText(overtimeData.getPlan_in() + " - " + overtimeData.getPlan_out());
            holder.approve.setText(overtimeData.getApprove_in() + " - " + overtimeData.getApprove_out());
            holder.pay.setText(overtimeData.getPay_in() + " - " + overtimeData.getPay_out());
            holder.reason.setText(overtimeData.getSpkl());
            String status = overtimeData.getSstatus();
            if (overtimeData.getTo_sap().equals("1")) {
                status += " | Already Uploaded";
            }
            holder.status.setText(status);
        }else{
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        public/* Roboto */ TextView headerTimeData;
        public/* Roboto */ TextView plan;
        public/* Roboto */ TextView reason;
        public/* Roboto */ TextView approve;
        public/* Roboto */ TextView pay;
        public/* Roboto */ TextView status;
    }

    private static class HeaderViewHolder {
        public/* Roboto */ TextView date;
        public/* Roboto */ TextView left;
        public/* Roboto */ TextView right;
        public/* Roboto */ TextView btnCreate;
    }

    @Override
    public View getHeaderView(final int position, final View convertView,
                              final ViewGroup parent) {
        View view = convertView;
        final HeaderViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_header_selfovertime, parent, false);
            holder = new HeaderViewHolder();
            holder.date = (TextView) view
                    .findViewById(R.id.list_header_social_date);
            holder.left = (TextView) view
                    .findViewById(R.id.list_item_header_left);
            holder.right = (TextView) view
                    .findViewById(R.id.list_item_header_right);
            view.setTag(holder);
            holder.btnCreate = (TextView) view
                    .findViewById(R.id.list_item_header_create);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }

        holder.date.setText(headerMonth);
        holder.left.setOnClickListener(this);
        holder.right.setOnClickListener(this);
        holder.btnCreate.setOnClickListener(this);
        return view;
    }

    @Override
    public long getHeaderId(final int position) {
        return position / 31;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int imonth = 0;
        switch (v.getId()) {
            case R.id.list_item_header_left:
                Toast.makeText(mContext, "Left: ",
                        Toast.LENGTH_SHORT).show();
                imonth = Integer.parseInt(month);
                imonth--;
                if (imonth <= 0) {
                    parentFragment.getDialog().showDialog("Maximum January or Desember on Current Year");
                } else {
                    String nMonth = "";
                    if (imonth <= 9) {
                        nMonth = "0" + imonth;
                    } else {
                        nMonth = "" + imonth;
                    }
                    loadTimeData(year, nMonth);
                }
                break;
            case R.id.list_item_header_right:
                Toast.makeText(mContext, "Right: ",
                        Toast.LENGTH_SHORT).show();
                imonth = Integer.parseInt(month);
                imonth++;
                //Log.i("month", "" + imonth);
                //Log.i("year", year);
                if (imonth > 12) {
                    parentFragment.getDialog().showDialog("Maximum January or Desember on Current Year");
                } else {
                    String nMonth = "";
                    if (imonth <= 9) {
                        nMonth = "0" + imonth;
                    } else {
                        nMonth = "" + imonth;
                    }
                    loadTimeData(year, nMonth);
                }
                break;
            case R.id.list_item_header_create:
                Toast.makeText(mContext, "Create Overtime: ",
                        Toast.LENGTH_SHORT).show();

                FormOvertimeFragment fragment = new FormOvertimeFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                baseContainerFragment.replaceFragment(fragment, true);
                break;
        }
    }

    private void loadTimeData(final String syear, final String smonth) {
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Load Time Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getListOvertimeRequest(new OvertimeDataRequest(device_type, device_id, nopeg, syear, smonth, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<OvertimeDataResponse>() {
                            @Override
                            public void success(final OvertimeDataResponse overtimeDataResponse, Response response) {
                                progressDialog.dismiss();
                                if (overtimeDataResponse.getStatus() == 1) {
                                    clear();
                                    aTimeDataList = overtimeDataResponse.getData();
                                    for (int i = 0; i < aTimeDataList.size(); i++) {
                                        add("Row number ");
                                    }
                                    if(aTimeDataList.size()==0){
                                        add("Row number ");
                                    }
                                    year = syear;
                                    month = smonth;
                                    headerMonth = smonth + "." + syear;
                                    //holder.date.setText(headerMonth);

//									Date date = new Date();
//									String monthNowFull = new SimpleDateFormat("MM").format(date);
//									String monthNow2Digit = new SimpleDateFormat("MM").format(date);
//									String yearNow4Figit = new SimpleDateFormat("yyyy").format(date);
//									StickyListHeadersListView listView = (StickyListHeadersListView) rootView.findViewById(R.id.activity_stickylistheaders_listview);
//									listView.setFitsSystemWindows(true);
//									AlphaInAnimationAdapter animationAdapter;
//									List<ATimeData> aTimeDataList = timeDataResponse.getData();
//									MyStickyListHeadersTimeDataAdapter adapterSocial = new MyStickyListHeadersTimeDataAdapter(
//											activity, timeDataResponse.getData(), monthNowFull + " " + yearNow4Figit);
//									animationAdapter = new AlphaInAnimationAdapter(adapterSocial);
//									StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(
//											animationAdapter);
//									stickyListHeadersAdapterDecorator
//											.setListViewWrapper(new StickyListHeadersListViewWrapper(
//													listView));
//									assert animationAdapter.getViewAnimator() != null;
//									animationAdapter.getViewAnimator().setInitialDelayMillis(500);
//									assert stickyListHeadersAdapterDecorator.getViewAnimator() != null;
//									stickyListHeadersAdapterDecorator.getViewAnimator()
//											.setInitialDelayMillis(500);
//									listView.setAdapter(stickyListHeadersAdapterDecorator);
                                } else {
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
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });
    }
}