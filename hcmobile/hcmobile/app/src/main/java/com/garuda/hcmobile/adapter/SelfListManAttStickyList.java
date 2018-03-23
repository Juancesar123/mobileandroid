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
import com.garuda.hcmobile.fragment.TMSEmpFragment;
import com.garuda.hcmobile.model.ALeaveData;
import com.garuda.hcmobile.model.AManAttData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.LeaveCancelRequest;
import com.garuda.hcmobile.model.LeaveDataRequest;
import com.garuda.hcmobile.model.LeaveDataResponse;
import com.garuda.hcmobile.model.LeaveQuota;
import com.garuda.hcmobile.model.ManAttCancelRequest;
import com.garuda.hcmobile.model.ManAttDataRequest;
import com.garuda.hcmobile.model.ManAttDataResponse;
import com.garuda.hcmobile.util.DialogUniversalWarningUtils;
import com.garuda.hcmobile.fragment.FormManAttFragment;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class SelfListManAttStickyList extends ArrayAdapter<String>
        implements StickyListHeadersAdapter, OnClickListener {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<AManAttData> aDataList;
    private TMSEmpFragment parentFragment;
    private String year;
    private BaseContainerFragment baseContainerFragment;
    private DialogUniversalWarningUtils dialog;

    public SelfListManAttStickyList(final Context context, final TMSEmpFragment parentFragment, BaseContainerFragment baseContainerFragment, List<AManAttData> aDataList, String year) {
        mContext = context;
        this.parentFragment = parentFragment;
        this.aDataList = aDataList;
        this.year = year;
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
                    R.layout.list_item_sticky_manatt_data, parent, false);
            holder = new ViewHolder();

            holder.txtDate = (TextView) convertView
                    .findViewById(R.id.list_item_date);
            holder.txtReason = (TextView) convertView
                    .findViewById(R.id.list_item_reason);
            holder.txtStatus = (TextView) convertView
                    .findViewById(R.id.list_item_status);
            holder.txtSupNote = (TextView) convertView
                    .findViewById(R.id.list_item_sup_note);
            holder.cancelIcon = (TextView) convertView
                    .findViewById(R.id.list_icon_cancel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(aDataList.size()>0) {
            AManAttData manAttData = aDataList.get(position);
            if (manAttData.getStatus().equals("2") == false) {
                holder.cancelIcon.setVisibility(View.GONE);
                holder.cancelIcon.setEnabled(false);
            }
            holder.txtDate.setText(manAttData.getTanggal());
            holder.txtReason.setText(manAttData.getKeterangan());
            holder.txtStatus.setText(manAttData.getSstatus());
            holder.txtSupNote.setText(manAttData.getSup_reason());
            holder.cancelIcon.setTag(manAttData);
            holder.cancelIcon.setOnClickListener(this);
        }else{
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        public/* Roboto */ TextView txtReason;
        public/* Roboto */ TextView txtDate;
        public/* Roboto */ TextView txtStatus;
        public/* Roboto */ TextView txtSupNote;
        public/* Material */TextView cancelIcon;

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
                    R.layout.list_header_selfmanatt_data, parent, false);
            holder = new HeaderViewHolder();
            holder.date = (TextView) view
                    .findViewById(R.id.list_header_social_date);
            holder.left = (TextView) view
                    .findViewById(R.id.list_item_header_left);
            holder.right = (TextView) view
                    .findViewById(R.id.list_item_header_right);
            holder.btnCreate= (TextView) view
                    .findViewById(R.id.list_item_header_create);

            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }
        holder.date.setText(year);
        holder.btnCreate.setOnClickListener(this);
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
                loadManAttData("" + iyear);
                break;
            case R.id.list_item_header_right:
                Toast.makeText(mContext, "Right: ",
                        Toast.LENGTH_SHORT).show();
                iyear = Integer.parseInt(year);
                iyear++;
                loadManAttData("" + iyear);
                break;
            case R.id.list_item_header_create:
                Toast.makeText(mContext, "CREATE ManAtt: ",
                        Toast.LENGTH_SHORT).show();
                FormManAttFragment fragment = new FormManAttFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                baseContainerFragment.replaceFragment(fragment, true);
                break;
            case R.id.list_icon_cancel:
                AManAttData manattData = (AManAttData) v.getTag();
                dialog = new DialogUniversalWarningUtils(this.parentFragment.getActivity());
                dialog.showDialog("Are you sure want to cancel : "+manattData.getKeterangan()+" ?");
                TextView tvCancel = dialog.getCancelButton();
                TextView tvOK = dialog.getOkButton();
                tvOK.setTag((int)manattData.getVal_id());
                tvCancel.setOnClickListener(this);
                tvOK.setOnClickListener(this);
                Toast.makeText(mContext, "CANCEL ManAtt: " + manattData.getVal_id(),
                        Toast.LENGTH_SHORT).show();

                break;
            case R.id.dialog_universal_warning_ok:
                dialog.dismissDialog();
                int id = (Integer) v.getTag();
                Toast.makeText(mContext, "WARNING OK: " ,
                        Toast.LENGTH_SHORT).show();
                cancelManAtt(id);
                break;
            case R.id.dialog_universal_warning_cancel:
                dialog.dismissDialog();
                Toast.makeText(mContext, "WARNING CANCEL: " ,
                        Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void cancelManAtt(final int val_id){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Cancel ManAtt Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().cancelManAttRequest(new ManAttCancelRequest(device_type, device_id, nopeg, val_id, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if (commonResponse.getStatus() == 1) {
                                    loadManAttData(year);
                                } else {
                                    parentFragment.getDialog().showDialog("cancel ManAtt Error, " + commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getDialog().showDialog("cancel ManAtt Error, " + error.getMessage());
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

    private void loadManAttData(final String syear) {
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Load ManAtt Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getListManAttRequest(new ManAttDataRequest(device_type, device_id, nopeg, syear, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<ManAttDataResponse>() {
                            @Override
                            public void success(final ManAttDataResponse manAttDataResponse, Response response) {
                                progressDialog.dismiss();
                                if (manAttDataResponse.getStatus() == 1) {
                                    clear();
                                    aDataList = manAttDataResponse.getData();
                                    for (int i = 0; i < aDataList.size(); i++) {
                                        add("Row number ");
                                    }
                                    if(aDataList.size()==0){
                                        add("Row number ");
                                    }
                                    year = syear;
                                } else {
                                    parentFragment.getDialog().showDialog(manAttDataResponse.getMessage());
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