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
import com.garuda.hcmobile.fragment.FormManAttFragment;
import com.garuda.hcmobile.fragment.TMSEmpFragment;
import com.garuda.hcmobile.fragment.TMSSupFragment;
import com.garuda.hcmobile.model.AEmp;
import com.garuda.hcmobile.model.ALeaveData;
import com.garuda.hcmobile.model.AManAttData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.ManAttCancelRequest;
import com.garuda.hcmobile.model.ManAttDataRequest;
import com.garuda.hcmobile.model.ManAttDataResponse;
import com.garuda.hcmobile.model.ManAttResponseRequest;
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

public class SupListManAttStickyList extends ArrayAdapter<String>
        implements StickyListHeadersAdapter, OnClickListener {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<AEmp> aEmps;
    private List<AManAttData> aDataList;
    private TMSSupFragment parentFragment;
    private String year;
    private BaseContainerFragment baseContainerFragment;
    private DialogUniversalWNoteUtils dialog;
    private String selectedEmp;
    private int selectedSpinner;

    public SupListManAttStickyList(final Context context, final TMSSupFragment parentFragment, BaseContainerFragment baseContainerFragment, List<AEmp> aEmps) {
        mContext = context;
        this.parentFragment = parentFragment;
        this.aEmps= aEmps;
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
                    R.layout.list_sup_item_sticky_manatt_data, parent, false);
            holder = new ViewHolder();

            holder.txtDate = (TextView) convertView
                    .findViewById(R.id.list_item_date);
            holder.txtReason = (TextView) convertView
                    .findViewById(R.id.list_item_reason);
            holder.txtStatus = (TextView) convertView
                    .findViewById(R.id.list_item_status);
            holder.txtSupNote = (TextView) convertView
                    .findViewById(R.id.list_item_sup_note);
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
            AManAttData manAttData = aDataList.get(position);
            if (manAttData.getStatus().equals("2") == false) {
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
            holder.txtDate.setText(manAttData.getTanggal());
            holder.txtReason.setText(manAttData.getKeterangan());
            holder.txtStatus.setText(manAttData.getSstatus());
            holder.txtSupNote.setText(manAttData.getSup_reason());
            holder.rejectIcon.setOnClickListener(this);
            holder.rejectIcon.setTag(manAttData);
            holder.approveIcon.setOnClickListener(this);
            holder.approveIcon.setTag(manAttData);
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
        public/* Material */TextView rejectIcon;
        public/* Material */TextView approveIcon;

    }

    private static class HeaderViewHolder {
        public/* Roboto */ TextView date;
        public/* Roboto */ TextView left;
        public/* Roboto */ TextView right;
        public/* Roboto */SearchableSpinner actSubordinat;
    }

    @Override
    public View getHeaderView(final int position, final View convertView,
                              final ViewGroup parent) {
        View view = convertView;
        final HeaderViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_header_supmanatt_data, parent, false);
            holder = new HeaderViewHolder();
            holder.date = (TextView) view
                    .findViewById(R.id.list_header_social_date);
            holder.left = (TextView) view
                    .findViewById(R.id.list_item_header_left);
            holder.right = (TextView) view
                    .findViewById(R.id.list_item_header_right);
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
                        loadManAttData(selectedEmp, year);
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
            loadManAttData(selectedEmp,year);
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
                loadManAttData(selectedEmp,year);
            }
        }
        holder.date.setText(year);
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
                loadManAttData(selectedEmp,"" + iyear);
                break;
            case R.id.list_item_header_right:
                Toast.makeText(mContext, "Right: ",
                        Toast.LENGTH_SHORT).show();
                iyear = Integer.parseInt(year);
                iyear++;
                loadManAttData(selectedEmp,"" + iyear);
                break;
            case R.id.list_item_header_create:
                Toast.makeText(mContext, "CREATE ManAtt: ",
                        Toast.LENGTH_SHORT).show();
                FormManAttFragment fragment = new FormManAttFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                baseContainerFragment.replaceFragment(fragment, true);
                break;
            case R.id.list_icon_reject:
                AManAttData aManAttData = (AManAttData) v.getTag();
                dialog = new DialogUniversalWNoteUtils(this.parentFragment.getActivity());
                dialog.showDialog("Reject, please fill note",DialogUniversalWNoteUtils.STATUS_REJECT);
                TextView tvCancel = dialog.getCancelButton();
                TextView tvOK = dialog.getOkButton();
                tvOK.setTag((int)aManAttData.getVal_id());
                tvCancel.setOnClickListener(this);
                tvOK.setOnClickListener(this);
                break;
            case R.id.list_icon_approve:
                AManAttData aManAttDataA = (AManAttData) v.getTag();
                dialog = new DialogUniversalWNoteUtils(this.parentFragment.getActivity());
                dialog.showDialog("Approval, please fill note",DialogUniversalWNoteUtils.STATUS_APPROVE);
                TextView tvCancelApprove = dialog.getCancelButton();
                TextView tvOKApprove = dialog.getOkButton();
                tvOKApprove.setTag((int)aManAttDataA.getVal_id());
                tvCancelApprove.setOnClickListener(this);
                tvOKApprove.setOnClickListener(this);

                break;
            case R.id.dialog_wnote_ok:
                int id = (Integer) v.getTag();
                if(dialog.getmDialogComment().getText().toString().equals("")==false) {
                    dialog.dismissDialog();
                    responseManAtt(selectedEmp, id, dialog.getStatus(), dialog.getmDialogComment().getText().toString());
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

    private void responseManAtt(final String subordinat,final int val_id,final int itype,final String comment){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        String messageProgressDialog="Rejecting Manual Attendance...";
        int xtype=0;
        if(itype==DialogUniversalWNoteUtils.STATUS_APPROVE){
            messageProgressDialog="Approving Manual Attendance...";
            xtype=1;
        }
        final int iType=xtype;
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", messageProgressDialog);
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().manattResponseRequest(new ManAttResponseRequest(device_type, device_id, subordinat, val_id, iType,comment,dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if (commonResponse.getStatus() == 1) {
                                    loadManAttData(subordinat,year);
                                } else {
                                    parentFragment.getDialog().showDialog("cancel ManAtt Error, " + commonResponse.getMessage());
                                    loadManAttData(subordinat,year);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getDialog().showDialog("cancel ManAtt Error, " + error.getMessage());
                                //Log.e("GetLaunchDataError", "error auth " + error.getMessage());
                                loadManAttData(subordinat,year);
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                parentFragment.getDialog().showDialog("Auth Error, " + error.getMessage());
                //Log.e("AuthActivity", "error auth " + error.getMessage());
                loadManAttData(subordinat,year);
            }
        });
    }

    private void loadManAttData(final String subordinat,final String syear) {
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
                RestClient.getRestAPI().getSubsManAttData(new ManAttDataRequest(device_type, device_id, subordinat, syear, dateTime),
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