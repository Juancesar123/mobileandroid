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
import com.garuda.hcmobile.fragment.FormLeaveFragment;
import com.garuda.hcmobile.fragment.SelfLeaveFragment;
import com.garuda.hcmobile.fragment.SelfTimeDataFragment;
import com.garuda.hcmobile.fragment.TMSEmpFragment;
import com.garuda.hcmobile.model.ALeaveData;
import com.garuda.hcmobile.model.ATimeData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.LeaveCancelRequest;
import com.garuda.hcmobile.model.LeaveDataRequest;
import com.garuda.hcmobile.model.LeaveDataResponse;
import com.garuda.hcmobile.model.LeaveProposeRequest;
import com.garuda.hcmobile.model.LeaveQuota;
import com.garuda.hcmobile.model.TimeDataRequest;
import com.garuda.hcmobile.model.TimeDataResponse;
import com.garuda.hcmobile.util.DialogUniversalWarningUtils;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class SelfListLeaveStickyList extends ArrayAdapter<String>
        implements StickyListHeadersAdapter, OnClickListener {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<ALeaveData> aDataList;
    private TMSEmpFragment parentFragment;
    private String year;
    private LeaveQuota leaveQuota;
    private LeaveQuota csQuota;
    private BaseContainerFragment baseContainerFragment;
    private DialogUniversalWarningUtils dialog;

    public SelfListLeaveStickyList(final Context context, final TMSEmpFragment parentFragment, BaseContainerFragment baseContainerFragment, List<ALeaveData> aDataList, LeaveQuota leaveQuota, LeaveQuota csQuota, String year) {
        mContext = context;
        this.parentFragment = parentFragment;
        this.aDataList = aDataList;
        this.year = year;
        this.leaveQuota = leaveQuota;
        this.csQuota = csQuota;
        this.baseContainerFragment = baseContainerFragment;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(aDataList.size()==0){
            add("Row number ");
        }
        for (int i = 0; i < aDataList.size(); i++) {
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
                    R.layout.list_item_sticky_leave_data, parent, false);
            holder = new ViewHolder();

            holder.leaveType = (TextView) convertView
                    .findViewById(R.id.list_item_leave_type);
            holder.leavePeriod = (TextView) convertView
                    .findViewById(R.id.list_item_sticky_leave_period);
            holder.leaveReason = (TextView) convertView
                    .findViewById(R.id.list_item_sticky_leave_reason);
            holder.leaveStatus = (TextView) convertView
                    .findViewById(R.id.list_item_sticky_leave_status);
            holder.leaveSupNote = (TextView) convertView
                    .findViewById(R.id.list_item_sticky_leave_sup_note);
            holder.cancelIcon = (TextView) convertView
                    .findViewById(R.id.list_icon_cancel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(aDataList.size()>0) {
            ALeaveData leaveData = aDataList.get(position);
            if (leaveData.getStatus().equals("4") == false) {
                holder.cancelIcon.setVisibility(View.GONE);
                holder.cancelIcon.setEnabled(false);
            }
            holder.leaveType.setText(leaveData.getJenis());
            holder.leavePeriod.setText(leaveData.getStart_date() + " - " + leaveData.getEnd_date());
            holder.leaveReason.setText(leaveData.getText());
            holder.leaveStatus.setText(leaveData.getSstatus());
            holder.leaveSupNote.setText(leaveData.getSup_reason());
            holder.cancelIcon.setOnClickListener(this);
            holder.cancelIcon.setTag(leaveData);
        }else{
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        public/* Roboto */ TextView leaveType;
        public/* Roboto */ TextView leavePeriod;
        public/* Roboto */ TextView leaveReason;
        public/* Roboto */ TextView leaveStatus;
        public/* Roboto */ TextView leaveSupNote;
        public/* Material */TextView cancelIcon;

    }

    private static class HeaderViewHolder {
        public/* Roboto */ TextView date;
        public/* Roboto */ TextView left;
        public/* Roboto */ TextView right;
        public/* Roboto */ TextView quotaLeave;
        public/* Roboto */ TextView quotaCS;
        public/* Roboto */ TextView btnLeave;
    }

    @Override
    public View getHeaderView(final int position, final View convertView,
                              final ViewGroup parent) {
        View view = convertView;
        final HeaderViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_header_selfleave_data, parent, false);
            holder = new HeaderViewHolder();
            holder.date = (TextView) view
                    .findViewById(R.id.list_header_social_date);
            holder.left = (TextView) view
                    .findViewById(R.id.list_item_header_left);
            holder.right = (TextView) view
                    .findViewById(R.id.list_item_header_right);
            holder.quotaLeave = (TextView) view
                    .findViewById(R.id.list_item_header_leave_entl);
            holder.quotaCS = (TextView) view
                    .findViewById(R.id.list_item_header_cs_entl);
            holder.btnLeave = (TextView) view
                    .findViewById(R.id.list_item_header_create_leave);

            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }
        if(csQuota==null){
            csQuota=new LeaveQuota();
        }
        if(leaveQuota==null){
            leaveQuota=new LeaveQuota();
        }
        holder.date.setText(year);
        holder.quotaCS.setText(csQuota.toString());
        holder.quotaLeave.setText(leaveQuota.toString());
        holder.btnLeave.setOnClickListener(this);
        holder.left.setOnClickListener(this);
        holder.right.setOnClickListener(this);
        return view;
    }

    @Override
    public long getHeaderId(final int position) {
        return position / 31;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int iyear = 0;
        switch (v.getId()) {
            case R.id.list_item_header_left:
                Toast.makeText(mContext, "Left: ",
                        Toast.LENGTH_SHORT).show();
                iyear = Integer.parseInt(year);
                iyear--;
                loadLeaveData("" + iyear);
                break;
            case R.id.list_item_header_right:
                Toast.makeText(mContext, "Right: ",
                        Toast.LENGTH_SHORT).show();
                iyear = Integer.parseInt(year);
                iyear++;
                loadLeaveData("" + iyear);
                break;
            case R.id.list_item_header_create_leave:
                Toast.makeText(mContext, "CREATE LEAVE: ",
                        Toast.LENGTH_SHORT).show();
                FormLeaveFragment fragment = new FormLeaveFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                baseContainerFragment.replaceFragment(fragment, true);
                break;
            case R.id.list_icon_cancel:
                ALeaveData leaveData = (ALeaveData) v.getTag();
//                Log.i("LEAVE ID",""+leave_id);
                dialog = new DialogUniversalWarningUtils(this.parentFragment.getActivity());
                dialog.showDialog("Are you sure want to cancel this leave  "+leaveData.getText()+" ?");
                TextView tvCancel = dialog.getCancelButton();
                TextView tvOK = dialog.getOkButton();
                tvOK.setTag((int)leaveData.getLeave_id());
                tvCancel.setOnClickListener(this);
                tvOK.setOnClickListener(this);
                Toast.makeText(mContext, "CANCEL LEAVE: " + leaveData.getLeave_id(),
                        Toast.LENGTH_SHORT).show();

                break;
            case R.id.dialog_universal_warning_ok:
                dialog.dismissDialog();
//                Log.i("TAG",v.getTag()+"");
                int id = (Integer) v.getTag();
                Toast.makeText(mContext, "WARNING OK: " ,
                        Toast.LENGTH_SHORT).show();
                cancelLeave(id);
                break;
            case R.id.dialog_universal_warning_cancel:
                dialog.dismissDialog();
                Toast.makeText(mContext, "WARNING CANCEL: " ,
                        Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void cancelLeave(final int leave_id){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Cancel Leave Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().cancelLeaveRequest(new LeaveCancelRequest(device_type, device_id, nopeg, leave_id, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if (commonResponse.getStatus() == 1) {
                                    loadLeaveData(year);
                                } else {
                                    parentFragment.getDialog().showDialog("Get Time Data Error, " + commonResponse.getMessage());
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

    private void loadLeaveData(final String syear) {
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Load Leave Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getListLeaveRequest(new LeaveDataRequest(device_type, device_id, nopeg, syear, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<LeaveDataResponse>() {
                            @Override
                            public void success(final LeaveDataResponse leaveDataResponse, Response response) {
                                progressDialog.dismiss();
                                if (leaveDataResponse.getStatus() == 1) {
                                    clear();
                                    aDataList = leaveDataResponse.getData();
                                    if(aDataList.size()==0){
                                        add("Row number ");
                                    }
                                    for (int i = 0; i < aDataList.size(); i++) {
                                        add("Row number ");
                                    }
                                    year = syear;
                                    csQuota = leaveDataResponse.getCs();
                                    leaveQuota = leaveDataResponse.getLeave();
                                } else {
                                    parentFragment.getDialog().showDialog(leaveDataResponse.getMessage());
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