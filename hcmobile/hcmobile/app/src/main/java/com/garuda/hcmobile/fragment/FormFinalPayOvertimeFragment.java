package com.garuda.hcmobile.fragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.AOvertimeData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.OvertimeResponseDataRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 5/19/2016.
 */
public class FormFinalPayOvertimeFragment extends Fragment implements OnClickListener {
    private View rootView;
    private TMSSupFragment parentFragment;
    private TextView tvDMY;
    private TextView tvPlanBegTime;
    private TextView tvPlanEndTime;
    private TextView tvApprovalBegTime;
    private TextView tvApprovalEndTime;
    private TextView tvFinalPayBegTime;
    private TextView tvFinalPayEndTime;
    private TextView tvReason;
    private TextView tvCancel;
    private TextView tvApprove;
    private BaseContainerFragment baseContainerFragment;

    private int begYear;
    private int begMonth;
    private int begDay;
    private int endYear;
    private int endMonth;
    private int endDay;
    private Calendar begCalendar;
    private Calendar endCalendar;
    private Calendar startCalendar;
    private AOvertimeData aOvertimeData;
    private Date selectedDate;

    public static FormFinalPayOvertimeFragment newInstance() {
        FormFinalPayOvertimeFragment f = new FormFinalPayOvertimeFragment();
        return f;
    }

    public void setBaseContainerFragment(BaseContainerFragment baseContainerFragment) {
        this.baseContainerFragment = baseContainerFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.fragment_overtime_finalpay,
                wrapper, true);
        //if (aOvertimeData != null) {
            //Log.i("AOVERTIMEDATA", aOvertimeData.toString());
        //}
        String category = "";
        if (parentFragment == null) {
            parentFragment = (TMSSupFragment) this.getParentFragment().getParentFragment();
        }
        tvDMY = (TextView) rootView.findViewById(R.id.tvovertime_date);
        tvPlanBegTime = (TextView) rootView.findViewById(R.id.tvovertime_plan_begtime);
        tvPlanEndTime = (TextView) rootView.findViewById(R.id.tvovertime_plan_endtime);
        tvApprovalBegTime= (TextView) rootView.findViewById(R.id.tvovertime_approve_begtime);
        tvApprovalEndTime = (TextView) rootView.findViewById(R.id.tvovertime_approve_endtime);
        tvFinalPayBegTime= (TextView) rootView.findViewById(R.id.tvovertime_begtime);
        tvFinalPayEndTime= (TextView) rootView.findViewById(R.id.tvovertime_endtime);
        tvCancel = (TextView) rootView.findViewById(R.id.rtvbtn_cancel);
        tvApprove = (TextView) rootView.findViewById(R.id.rtvbtn_approve);
        tvReason = (TextView) rootView.findViewById(R.id.tv_reason);

        tvDMY.setText(aOvertimeData.getTanggal());
        tvPlanBegTime.setText(aOvertimeData.getPlan_in());
        tvPlanEndTime.setText(aOvertimeData.getPlan_out());
        tvReason.setText(aOvertimeData.getSpkl());
        tvApprovalBegTime.setText(aOvertimeData.getApprove_in());
        tvApprovalEndTime.setText(aOvertimeData.getApprove_out());
        tvFinalPayBegTime.setText(aOvertimeData.getApprove_in());
        tvFinalPayEndTime.setText(aOvertimeData.getApprove_out());
        tvFinalPayBegTime.setOnClickListener(this);
        tvFinalPayEndTime.setOnClickListener(this);
        tvApprove.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        begCalendar = Calendar.getInstance();
        Date date = stringToDate(aOvertimeData.getTanggal() + " " + aOvertimeData.getApprove_in(), "dd.MM.yyyy HH:ss");
        begCalendar.setTime(date);
        endCalendar = Calendar.getInstance();
        date = stringToDate(aOvertimeData.getTanggal() + " " + aOvertimeData.getApprove_out(), "dd.MM.yyyy HH:ss");
        endCalendar.setTime(date);
        return rootView;
    }

    public AOvertimeData getaOvertimeData() {
        return aOvertimeData;
    }

    public Date stringToDate(String val, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(val);
        } catch (ParseException e) {
            Log.i("PARSE ERROR", e.getMessage());
        } catch (Exception e) {
            Log.i("PARSE ERROR", e.getMessage());
        } finally {
            return date;
        }
    }


    public void setaOvertimeData(AOvertimeData aOvertimeData) {
        this.aOvertimeData = aOvertimeData;
        selectedDate = stringToDate(aOvertimeData.getTanggal(), "dd.MM.yyyy");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int iyear = 0;
        switch (v.getId()) {
            case R.id.tvovertime_begtime:
                begShowTimePickerDialog();
                break;
            case R.id.tvovertime_endtime:
                endShowTimePickerDialog();
                break;
            case R.id.rtvbtn_approve:
                if (check_leave_overtime()) {
                    finalpayOvertime(aOvertimeData.getOt_id(),tvFinalPayBegTime.getText().toString(),tvFinalPayEndTime.getText().toString());
                }
                break;
            case R.id.rtvbtn_cancel:
                SupOvertimeFragment fragment = new SupOvertimeFragment();
                fragment.setSelectedEmp(aOvertimeData.getNopeg());
                fragment.setSelectedDate(selectedDate);
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                ((BaseContainerFragment) this.getParentFragment()).replaceFragment(fragment, true);
                break;
        }
    }

    public void begShowTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(parentFragment.getActivity(), begTimePickerListener, begCalendar.get(Calendar.HOUR_OF_DAY), begCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public void endShowTimePickerDialog() {
        if (endCalendar.getTimeInMillis() <= begCalendar.getTimeInMillis()) {
            endCalendar.add(Calendar.MINUTE, 1);
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(parentFragment.getActivity(), endTimePickerListener, endCalendar.get(Calendar.HOUR_OF_DAY), begCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener begTimePickerListener
            = new TimePickerDialog.OnTimeSetListener() {

        // when dialog box is closed, below method will be called.
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            begCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            begCalendar.set(Calendar.MINUTE, selectedMinute);
            tvFinalPayBegTime.setText(char2(selectedHour)+":"+char2(selectedMinute));
        }
    };

    private String char2(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return "" + num;
    }

    private TimePickerDialog.OnTimeSetListener endTimePickerListener
            = new TimePickerDialog.OnTimeSetListener() {

        // when dialog box is closed, below method will be called.
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            endCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            endCalendar.set(Calendar.MINUTE, selectedMinute);
            tvFinalPayEndTime.setText(char2(selectedHour)+":"+char2(selectedMinute));
        }
    };

    private boolean check_leave_overtime() {
        //Log.i("BEG Calendar",""+begCalendar.getTimeInMillis());
        //Log.i("END Calendar",""+endCalendar.getTimeInMillis());
        if (endCalendar.getTimeInMillis() <= begCalendar.getTimeInMillis()) {
            Toast.makeText(getActivity(), "Check Period (Start and End Time) ", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if(etReason.getText().toString().equals("")){
//            Toast.makeText(getActivity(), "Fill Reason",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    private void finalpayOvertime(final int ot_id,final String start_time,final String end_time){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Finalizing Overtime...");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().overtimeResponseRequest(new OvertimeResponseDataRequest(device_type, device_id, nopeg, ot_id,9,start_time,end_time, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if (commonResponse.getStatus() == 1) {
                                    tvApprove.setEnabled(true);
                                    Toast.makeText(getActivity(), "FinalPay Success.",Toast.LENGTH_SHORT).show();
                                    SupOvertimeFragment fragment = new SupOvertimeFragment();
                                    fragment.setSelectedEmp(aOvertimeData.getNopeg());
                                    fragment.setSelectedDate(selectedDate);
                                    Bundle bundle = new Bundle();
                                    fragment.setArguments(bundle);
                                    ((BaseContainerFragment)getParentFragment()).replaceFragment(fragment, true);
                                }else if (commonResponse.getStatus() == 2) {
                                    tvApprove.setEnabled(true);
                                    Toast.makeText(getActivity(), "FinalPay Success, but pending SAP.",Toast.LENGTH_SHORT).show();
                                    SupOvertimeFragment fragment = new SupOvertimeFragment();
                                    fragment.setSelectedEmp(aOvertimeData.getNopeg());
                                    fragment.setSelectedDate(selectedDate);
                                    Bundle bundle = new Bundle();
                                    fragment.setArguments(bundle);
                                    ((BaseContainerFragment)getParentFragment()).replaceFragment(fragment, true);
                                } else {
                                    parentFragment.getDialog().showDialog("FinalPay Overtime Error, " + commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getDialog().showDialog("FinalPay Overtime Error, " + error.getMessage());
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

//    private void loadLeaveData(final String year, final Activity activity){

//    }
}
