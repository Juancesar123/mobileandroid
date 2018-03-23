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
import com.garuda.hcmobile.model.OvertimeProposeRequest;
import com.garuda.hcmobile.model.OvertimeResponseDataRequest;
import com.garuda.hcmobile.view.component.FloatLabeledEditText;

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
public class FormApprovalOvertimeFragment extends Fragment implements OnClickListener {
    private View rootView;
    private TMSSupFragment parentFragment;
    private TextView tvDMY;
    private TextView tvPlanBegTime;
    private TextView tvPlanEndTime;
    private TextView tvApprovalBegTime;
    private TextView tvApprovalEndTime;
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

    public static FormApprovalOvertimeFragment newInstance() {
        FormApprovalOvertimeFragment f = new FormApprovalOvertimeFragment();
        return f;
    }

    public void setBaseContainerFragment(BaseContainerFragment baseContainerFragment) {
        this.baseContainerFragment = baseContainerFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.fragment_overtime_approve,
                wrapper, true);
        if (aOvertimeData != null) {
            //Log.i("AOVERTIMEDATA", aOvertimeData.toString());
        }
        String category = "";
        if (parentFragment == null) {
            parentFragment = (TMSSupFragment) this.getParentFragment().getParentFragment();
        }
        tvDMY = (TextView) rootView.findViewById(R.id.tvovertime_date);
        tvPlanBegTime = (TextView) rootView.findViewById(R.id.tvovertime_plan_begtime);
        tvPlanEndTime = (TextView) rootView.findViewById(R.id.tvovertime_plan_endtime);
        tvApprovalBegTime = (TextView) rootView.findViewById(R.id.tvovertime_begtime);
        tvApprovalEndTime = (TextView) rootView.findViewById(R.id.tvovertime_endtime);
        tvCancel = (TextView) rootView.findViewById(R.id.rtvbtn_cancel);
        tvApprove = (TextView) rootView.findViewById(R.id.rtvbtn_approve);
        tvReason = (TextView) rootView.findViewById(R.id.tv_reason);

        tvDMY.setText(aOvertimeData.getTanggal());
        tvPlanBegTime.setText(aOvertimeData.getPlan_in());
        tvPlanEndTime.setText(aOvertimeData.getPlan_out());
        tvReason.setText(aOvertimeData.getSpkl());
        tvApprovalBegTime.setText(aOvertimeData.getPlan_in());
        tvApprovalEndTime.setText(aOvertimeData.getPlan_out());
        tvApprovalBegTime.setOnClickListener(this);
        tvApprovalEndTime.setOnClickListener(this);
        tvApprove.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        begCalendar = Calendar.getInstance();
        Date date = stringToDate(aOvertimeData.getTanggal() + " " + aOvertimeData.getPlan_in(), "dd.MM.yyyy HH:ss");
        begCalendar.setTime(date);
        endCalendar = Calendar.getInstance();
        date = stringToDate(aOvertimeData.getTanggal() + " " + aOvertimeData.getPlan_out(), "dd.MM.yyyy HH:ss");
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
                    approveOvertime(aOvertimeData.getOt_id(),tvApprovalBegTime.getText().toString(),tvApprovalEndTime.getText().toString());
//                    overtime_propose(startCalendar.get(Calendar.YEAR)+"-"+char2(startCalendar.get(Calendar.MONTH)+1)+"-"+char2(startCalendar.get(Calendar.DAY_OF_MONTH)),tvBegTime.getText().toString(),tvEndTime.getText().toString(),etReason.getText().toString());
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
            tvApprovalBegTime.setText(char2(selectedHour)+":"+char2(selectedMinute));
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
            tvApprovalEndTime.setText(char2(selectedHour)+":"+char2(selectedMinute));
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

    private void approveOvertime(final int ot_id,final String start_time,final String end_time){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) parentFragment.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) parentFragment.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getActivity(), "", "Approving Overtime...");
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().overtimeResponseRequest(new OvertimeResponseDataRequest(device_type, device_id, nopeg, ot_id,1,start_time,end_time, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if (commonResponse.getStatus() == 1) {
                                    tvApprove.setEnabled(true);
                                    Toast.makeText(getActivity(), "Approve Success.",Toast.LENGTH_SHORT).show();
                                    SupOvertimeFragment fragment = new SupOvertimeFragment();
                                    fragment.setSelectedEmp(aOvertimeData.getNopeg());
                                    fragment.setSelectedDate(selectedDate);
                                    Bundle bundle = new Bundle();
                                    fragment.setArguments(bundle);
                                    ((BaseContainerFragment)getParentFragment()).replaceFragment(fragment, true);
                                } else {
                                    parentFragment.getDialog().showDialog("Approve Overtime Error, " + commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                parentFragment.getDialog().showDialog("Approve Overtime Error, " + error.getMessage());
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
