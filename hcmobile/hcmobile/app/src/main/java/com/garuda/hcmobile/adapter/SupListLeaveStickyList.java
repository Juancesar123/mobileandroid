package com.garuda.hcmobile.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.fragment.BaseContainerFragment;
import com.garuda.hcmobile.fragment.FormLeaveFragment;
import com.garuda.hcmobile.fragment.TMSEmpFragment;
import com.garuda.hcmobile.fragment.TMSSupFragment;
import com.garuda.hcmobile.model.AEmp;
import com.garuda.hcmobile.model.ALeaveData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.LeaveCancelRequest;
import com.garuda.hcmobile.model.LeaveDataRequest;
import com.garuda.hcmobile.model.LeaveDataResponse;
import com.garuda.hcmobile.model.LeaveQuota;
import com.garuda.hcmobile.model.LeaveResponseRequest;
import com.garuda.hcmobile.util.DialogUniversalWNoteUtils;
import com.garuda.hcmobile.util.DialogUniversalWarningUtils;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class SupListLeaveStickyList extends ArrayAdapter<String>
        implements StickyListHeadersAdapter, OnClickListener {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<ALeaveData> aDataList;
    private TMSSupFragment parentFragment;
    private String year;
    private LeaveQuota leaveQuota;
    private LeaveQuota csQuota;
    private BaseContainerFragment baseContainerFragment;
    private DialogUniversalWNoteUtils dialog;
    private String selectedEmp;
    private int selectedSpinner;
    private List<AEmp> aEmps;

    public SupListLeaveStickyList(final Context context, final TMSSupFragment parentFragment, BaseContainerFragment baseContainerFragment, List<AEmp> aEmps) {
        mContext = context;
        this.parentFragment = parentFragment;
        this.aEmps=aEmps;
        this.baseContainerFragment = baseContainerFragment;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Date date = new Date();
        year = new SimpleDateFormat("yyyy").format(date);
        add("Empty Row for initial subordinat header" );
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
                    R.layout.list_sup_item_sticky_leave, parent, false);
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
            holder.rejectIcon = (TextView) convertView
                    .findViewById(R.id.list_icon_reject);
            holder.approveIcon = (TextView) convertView
                    .findViewById(R.id.list_icon_approve);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(aDataList!=null && aDataList.size()>0) {
            convertView.setVisibility(View.VISIBLE);
            ALeaveData leaveData = aDataList.get(position);
            if (leaveData.getStatus().equals("4") == false) {
                holder.rejectIcon.setVisibility(View.GONE);
                holder.rejectIcon.setEnabled(false);
                holder.approveIcon.setVisibility(View.GONE);
                holder.approveIcon.setEnabled(false);
            }else{
                holder.rejectIcon.setVisibility(View.VISIBLE);
                holder.rejectIcon.setEnabled(true);
                holder.approveIcon.setVisibility(View.VISIBLE);
                holder.approveIcon.setEnabled(true);
            }
            holder.leaveType.setText(leaveData.getJenis());
            holder.leavePeriod.setText(leaveData.getStart_date() + " - " + leaveData.getEnd_date());
            holder.leaveReason.setText(leaveData.getText());
            holder.leaveStatus.setText(leaveData.getSstatus());
            holder.leaveSupNote.setText(leaveData.getSup_reason());
            holder.rejectIcon.setOnClickListener(this);
            holder.rejectIcon.setTag(leaveData);
            holder.approveIcon.setOnClickListener(this);
            holder.approveIcon.setTag(leaveData);
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
        public/* Material */TextView rejectIcon;
        public/* Material */TextView approveIcon;

    }

    private static class HeaderViewHolder {
        public/* Roboto */ TextView date;
        public/* Roboto */ TextView left;
        public/* Roboto */ TextView right;
        public/* Roboto */ TextView quotaLeave;
        public/* Roboto */ TextView quotaCS;
        public/* Roboto */SearchableSpinner actSubordinat;
    }

    @Override
    public View getHeaderView(final int position, final View convertView,
                              final ViewGroup parent) {
        View view = convertView;
        final HeaderViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_header_supleave_data, parent, false);
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
            holder.actSubordinat=(SearchableSpinner) view.findViewById(R.id.act_emp);
            android.widget.ArrayAdapter<String> adapterSpinner = new android.widget.ArrayAdapter<String>(parentFragment.getActivity().getApplicationContext(),
                    android.R.layout.simple_spinner_item, parentFragment.getArrayEmps());
            holder.actSubordinat.setAdapter(adapterSpinner);
            holder.actSubordinat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    AEmp aEmp=parentFragment.getaEmps().get(position);
                    if(aEmp.getNopeg().equals(selectedEmp)==false) {
                        selectedEmp = aEmp.getNopeg();
                        selectedSpinner=position;
                        loadLeaveData(selectedEmp, year);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }


        if(holder.actSubordinat.getSelectedItem()!=null && aDataList==null && (selectedEmp==null || selectedEmp.equals(""))){
            holder.date.setText(year);
            AEmp aEmp=aEmps.get(holder.actSubordinat.getSelectedItemPosition());
            selectedEmp=aEmp.getNopeg();
            selectedSpinner=0;
            loadLeaveData(selectedEmp,year);
        }else{
            if(selectedEmp!=null && selectedEmp.equals("")==false){
                for(int i=0;i<aEmps.size();i++){
                    if(aEmps.get(i).getNopeg().equals(selectedEmp)){
                        selectedSpinner=i;
                        break;
                    }
                }
            }
            holder.actSubordinat.setSelection(selectedSpinner);
            if((holder.date.getText().equals("some") || holder.date.getText().equals("") )&& aDataList==null ){
                holder.date.setText(year);
                loadLeaveData(selectedEmp,year);
            }
        }

        if(aDataList!=null ) {
            if(csQuota==null){
                csQuota=new LeaveQuota();
            }
            if(leaveQuota==null){
                leaveQuota=new LeaveQuota();
            }
            holder.date.setText(year);
            holder.quotaCS.setText(csQuota.toString());
            holder.quotaLeave.setText(leaveQuota.toString());
            holder.left.setOnClickListener(this);
            holder.right.setOnClickListener(this);
        }



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
                loadLeaveData(selectedEmp,"" + iyear);
                break;
            case R.id.list_item_header_right:
                Toast.makeText(mContext, "Right: ",
                        Toast.LENGTH_SHORT).show();
                iyear = Integer.parseInt(year);
                iyear++;
                loadLeaveData(selectedEmp,"" + iyear);
                break;
            case R.id.list_icon_reject:
                ALeaveData leaveData = (ALeaveData) v.getTag();
                dialog = new DialogUniversalWNoteUtils(this.parentFragment.getActivity());
                dialog.showDialog("Reject, please fill note",DialogUniversalWNoteUtils.STATUS_REJECT);
                TextView tvCancel = dialog.getCancelButton();
                TextView tvOK = dialog.getOkButton();
                tvOK.setTag((int)leaveData.getLeave_id());
                tvCancel.setOnClickListener(this);
                tvOK.setOnClickListener(this);
                break;
            case R.id.list_icon_approve:
                ALeaveData leaveDataApprove = (ALeaveData) v.getTag();
                dialog = new DialogUniversalWNoteUtils(this.parentFragment.getActivity());
                dialog.showDialog("Approval, please fill note",DialogUniversalWNoteUtils.STATUS_APPROVE);
                TextView tvCancelApprove = dialog.getCancelButton();
                TextView tvOKApprove = dialog.getOkButton();
                tvOKApprove.setTag((int)leaveDataApprove.getLeave_id());
                tvCancelApprove.setOnClickListener(this);
                tvOKApprove.setOnClickListener(this);

            break;
            case R.id.dialog_wnote_ok:
                int id = (Integer) v.getTag();
                if(dialog.getmDialogComment().getText().toString().equals("")==false) {
                    dialog.dismissDialog();
                    responseLeave(selectedEmp, id, dialog.getStatus(), dialog.getmDialogComment().getText().toString());
                }else{
                    Toast.makeText(mContext, "Please Fill Note ",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dialog_wnote_cancel:
                dialog.dismissDialog();
                break;

        }
    }

    private void responseLeave(final String subordinat,final int leave_id,final int iType,final String note){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        String messageProgressDialog="Rejecting Leave...";
        if(iType==DialogUniversalWNoteUtils.STATUS_APPROVE){
            messageProgressDialog="Approving Leave...";
        }
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", messageProgressDialog);
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().leaveResponseRequest(new LeaveResponseRequest(device_type, device_id, subordinat, leave_id,iType,note, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if (commonResponse.getStatus() == 1) {
                                    loadLeaveData(subordinat,year);
                                } else {
                                    parentFragment.getDialog().showDialog("Response Leave Error, " + commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getDialog().showDialog("Response Leave Error, " + error.getMessage());
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

    private void loadLeaveData(final String subordinat,final String syear) {
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
                RestClient.getRestAPI().getSubsLeaveData(new LeaveDataRequest(device_type, device_id, subordinat, syear, dateTime),
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