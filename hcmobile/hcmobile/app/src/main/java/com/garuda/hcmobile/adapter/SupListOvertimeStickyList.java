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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.fragment.BaseContainerFragment;
import com.garuda.hcmobile.fragment.FormApprovalOvertimeFragment;
import com.garuda.hcmobile.fragment.FormFinalPayOvertimeFragment;
import com.garuda.hcmobile.fragment.FormOvertimeFragment;
import com.garuda.hcmobile.fragment.SupOvertimeFragment;
import com.garuda.hcmobile.fragment.TMSEmpFragment;
import com.garuda.hcmobile.fragment.TMSSupFragment;
import com.garuda.hcmobile.model.AEmp;
import com.garuda.hcmobile.model.AOvertimeData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.OvertimeDataRequest;
import com.garuda.hcmobile.model.OvertimeDataResponse;
import com.garuda.hcmobile.model.OvertimeResponseDataRequest;
import com.garuda.hcmobile.util.DialogUniversalWarningUtils;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class SupListOvertimeStickyList extends ArrayAdapter<String>
        implements StickyListHeadersAdapter, OnClickListener {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<AEmp> aEmps;
    private List<AOvertimeData> aTimeDataList;
    private SupOvertimeFragment parentFragment;
    private BaseContainerFragment baseContainerFragment;
    private String year;
    private String month;
    private String headerMonth;
    private Date selectedDate;
    private String selectedEmp;
    private int selectedSpinner;
    private HeaderViewHolder holderHeader;
    private DialogUniversalWarningUtils dialog;

    public SupListOvertimeStickyList(final Context context, final SupOvertimeFragment parentFragment, BaseContainerFragment baseContainerFragment, List<AEmp> aEmps) {
        mContext = context;
        this.baseContainerFragment = baseContainerFragment;
        this.parentFragment = parentFragment;
        this.aEmps= aEmps;
        selectedDate = new Date();
        month = new SimpleDateFormat("MM").format(selectedDate);
        year = new SimpleDateFormat("yyyy").format(selectedDate);
        headerMonth=month+"."+year;
        //Log.i("HeaderMonth",headerMonth);
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        add("Empty Row for initial subordinat header" );
    }

    public SupListOvertimeStickyList(final Context context, final SupOvertimeFragment parentFragment, BaseContainerFragment baseContainerFragment, List<AEmp> aEmps,String selectedEmp,Date selectedDate) {
        //Log.i("Selected Date",selectedDate.toString());
        mContext = context;
        this.baseContainerFragment = baseContainerFragment;
        this.parentFragment = parentFragment;
        this.aEmps= aEmps;
        this.selectedEmp=selectedEmp;
        this.selectedDate=selectedDate;
        month = new SimpleDateFormat("MM").format(selectedDate);
        year = new SimpleDateFormat("yyyy").format(selectedDate);
        headerMonth=month+"."+year;
        //Log.i("HeaderMonth",headerMonth);
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    R.layout.list_sup_item_sticky_overtime, parent, false);
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
            holder.btnReject = (TextView) convertView
                    .findViewById(R.id.list_icon_reject);
            holder.btnApprove= (TextView) convertView
                    .findViewById(R.id.list_icon_approve);
            holder.btnPay= (TextView) convertView
                    .findViewById(R.id.list_icon_pay);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(aTimeDataList!=null && aTimeDataList.size()>0) {
            convertView.setVisibility(View.VISIBLE);
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
            if(aTimeDataList.get(position).getStatus().equals("2")){
                holder.btnPay.setVisibility(View.GONE);
                holder.btnReject.setTag(overtimeData);
                holder.btnApprove.setTag(overtimeData);
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.btnApprove.setVisibility(View.VISIBLE);
                holder.btnReject.setOnClickListener(this);
                holder.btnApprove.setOnClickListener(this);
            }else if(aTimeDataList.get(position).getStatus().equals("1") && aTimeDataList.get(position).getTo_sap().equals("1")==false){
                holder.btnReject.setVisibility(View.GONE);
                holder.btnApprove.setVisibility(View.GONE);
                holder.btnPay.setTag(overtimeData);
                holder.btnPay.setOnClickListener(this);
                holder.btnPay.setVisibility(View.VISIBLE);
            }else{
                holder.btnReject.setVisibility(View.GONE);
                holder.btnApprove.setVisibility(View.GONE);
                holder.btnPay.setVisibility(View.GONE);
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
        public/* Roboto */ TextView btnReject;
        public/* Roboto */ TextView btnApprove;
        public/* Roboto */ TextView btnPay;
    }

    private static class HeaderViewHolder {
        public/* Roboto */ TextView date;
        public/* Roboto */ TextView left;
        public/* Roboto */ TextView right;
        public/* Roboto */SearchableSpinner actSubordinat;
        public LinearLayout ll_control;
    }

    @Override
    public View getHeaderView(final int position, final View convertView,
                              final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_header_supovertime, parent, false);
            holderHeader = new HeaderViewHolder();
            holderHeader.date = (TextView) view
                    .findViewById(R.id.list_header_social_date);
            holderHeader.left = (TextView) view
                    .findViewById(R.id.list_item_header_left);
            holderHeader.right = (TextView) view
                    .findViewById(R.id.list_item_header_right);
            holderHeader.actSubordinat=(SearchableSpinner) view.findViewById(R.id.act_emp);
            android.widget.ArrayAdapter<String> adapterSpinner = new android.widget.ArrayAdapter<String>(parentFragment.getTMSSupFragment().getActivity().getApplicationContext(),
                    android.R.layout.simple_spinner_item, parentFragment.getTMSSupFragment().getArrayEmps());
            holderHeader.actSubordinat.setAdapter(adapterSpinner);
            holderHeader.actSubordinat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    AEmp aEmp=parentFragment.getTMSSupFragment().getaEmps().get(position);
                    if(aEmp.getNopeg().equals(selectedEmp)==false) {
                        selectedEmp = aEmp.getNopeg();
                        selectedSpinner=position;
                        loadOverTimeData(selectedEmp, year, month);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            holderHeader.ll_control=(LinearLayout) view.findViewById(R.id.ll_control);
            view.setTag(holderHeader);
        } else {
            holderHeader = (HeaderViewHolder) view.getTag();
        }


        if(holderHeader.actSubordinat.getSelectedItem()!=null && aTimeDataList==null && (selectedEmp==null || selectedEmp.equals(""))){
            holderHeader.date.setText(headerMonth);
            AEmp aEmp=aEmps.get(holderHeader.actSubordinat.getSelectedItemPosition());
            selectedEmp=aEmp.getNopeg();
            selectedSpinner=0;
            loadOverTimeData(selectedEmp,year,month);
        }else{
            if(selectedEmp!=null && selectedEmp.equals("")==false){
                for(int i=0;i<aEmps.size();i++){
                    if(aEmps.get(i).getNopeg().equals(selectedEmp)){
                        selectedSpinner=i;
                        break;
                    }
                }
            }
            holderHeader.actSubordinat.setSelection(selectedSpinner);
            if(holderHeader.date.getText().equals("some") && aTimeDataList==null ){
                holderHeader.date.setText(headerMonth);
                loadOverTimeData(selectedEmp,year,month);
            }
        }

        if(aTimeDataList!=null ) {
            holderHeader.date.setText(headerMonth);
            holderHeader.left.setOnClickListener(this);
            holderHeader.right.setOnClickListener(this);
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
        int imonth = 0;
        Calendar c;
        switch (v.getId()) {
            case R.id.list_item_header_left:
                    c = Calendar.getInstance();
                    c.setTime(selectedDate);
                c.add(Calendar.MONTH,-1);
                    loadOverTimeData(selectedEmp,c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                break;
            case R.id.list_item_header_right:
                    c = Calendar.getInstance();
                    c.setTime(selectedDate);
                c.add(Calendar.MONTH,1);
                    loadOverTimeData(selectedEmp,c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                break;
            case R.id.list_icon_reject :
                //Log.i("OvertimeData",v.getTag().toString());
                AOvertimeData overtimeData = (AOvertimeData) v.getTag();
                //Log.i("OvertimeData",overtimeData.toString());
                dialog = new DialogUniversalWarningUtils(this.parentFragment.getActivity());
                dialog.showDialog("Are you sure want to reject this overtime "+overtimeData.getSpkl()+" ?");
                TextView tvCancel = dialog.getCancelButton();
                TextView tvOK = dialog.getOkButton();
                tvOK.setTag(overtimeData.getOt_id());
                tvCancel.setOnClickListener(this);
                tvOK.setOnClickListener(this);
                break;
            case R.id.list_icon_approve:
                FormApprovalOvertimeFragment fragment = new FormApprovalOvertimeFragment();
                fragment.setaOvertimeData((AOvertimeData)v.getTag());
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                baseContainerFragment.replaceFragment(fragment, true);
                break;
            case R.id.list_icon_pay:
                FormFinalPayOvertimeFragment fragmentFinalPay = new FormFinalPayOvertimeFragment();
                fragmentFinalPay.setaOvertimeData((AOvertimeData)v.getTag());
                Bundle bundleFinalPay = new Bundle();
                fragmentFinalPay.setArguments(bundleFinalPay);
                baseContainerFragment.replaceFragment(fragmentFinalPay, true);
                break;
            case R.id.dialog_universal_warning_ok:
                dialog.dismissDialog();
                int id = (Integer) v.getTag();
                Toast.makeText(mContext, "Rejecting Overtime " +id,
                        Toast.LENGTH_SHORT).show();
                rejectOvertime(id);
                break;
            case R.id.dialog_universal_warning_cancel:
                dialog.dismissDialog();
                Toast.makeText(mContext, "WARNING CANCEL: " ,
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void loadOverTimeData(final String subordinat,final String  syear, final String smonth) {
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Load OverTime Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getSubOvertimeRequest(new OvertimeDataRequest(device_type, device_id, subordinat, syear, smonth, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<OvertimeDataResponse>() {
                            @Override
                            public void success(final OvertimeDataResponse overtimeDataResponse, Response response) {
                                progressDialog.dismiss();
                                if (overtimeDataResponse.getStatus() == 1) {
                                    clear();
                                    aTimeDataList = overtimeDataResponse.getData();
                                    //Log.i("SIZE",""+aTimeDataList.size());
                                    for (int i = 0; i < aTimeDataList.size(); i++) {
                                        add("Row number ");
                                    }
                                    if(aTimeDataList.size()==0){
                                        add("Row number ");
                                    }
                                    year = syear;
                                    month = smonth;
                                    headerMonth = smonth + "." + syear;

                                    Calendar c = Calendar.getInstance();
                                    c.setTime(selectedDate);
                                    c.set(Calendar.YEAR,Integer.valueOf(year));
                                    c.set(Calendar.MONTH,Integer.valueOf(month));
                                    selectedDate=c.getTime();

                                } else {
                                    parentFragment.getTMSSupFragment().getDialog().showDialog(overtimeDataResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getTMSSupFragment().getDialog().showDialog("Get Overtime Data Error, " + error.getMessage());
                                //Log.e("GetOvertimeError", "error auth " + error.getMessage());
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                parentFragment.getTMSSupFragment().getDialog().showDialog("Auth Error, " + error.getMessage());
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });
    }

    private void loadOverTimeData(final String subordinat,final int iyear, final int imonth) {
        String syear = ""+iyear;
        String smonth=""+imonth;
        if(imonth<10)smonth="0"+smonth;
        this.loadOverTimeData(subordinat,syear,smonth);
    }

    private void rejectOvertime(final int ot_id){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Cancel Overtime Data....");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().overtimeResponseRequest(new OvertimeResponseDataRequest(device_type, device_id, nopeg, ot_id,0,"99:99","99:99", dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if (commonResponse.getStatus() == 1) {
                                    loadOverTimeData(selectedEmp,year,month);
                                } else {
                                    parentFragment.getTMSSupFragment().getDialog().showDialog("Reject Overtime Error, " + commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getTMSSupFragment().getDialog().showDialog("Reject Overtime Error, " + error.getMessage());
                                //Log.e("GetLaunchDataError", "error auth " + error.getMessage());
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                parentFragment.getTMSSupFragment().getDialog().showDialog("Auth Error, " + error.getMessage());
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }
        });
    }
}