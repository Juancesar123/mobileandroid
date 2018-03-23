package com.garuda.hcmobile.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.LeaveProposeRequest;
import com.garuda.hcmobile.model.OvertimeProposeRequest;
import com.garuda.hcmobile.view.component.FloatLabeledEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 5/19/2016.
 */
public class FormOvertimeFragment extends Fragment implements OnClickListener {
    private View rootView;
    private TMSEmpFragment parentFragment;
    private TextView tvDMY;
    private TextView tvBegTime;
    private TextView tvEndTime;
    private TextView tvCancel;
    private TextView tvPropose;
    private FloatLabeledEditText etReason;
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


    public static FormOvertimeFragment newInstance() {
        FormOvertimeFragment f = new FormOvertimeFragment();
        return f;
    }

    public void setBaseContainerFragment(BaseContainerFragment baseContainerFragment) {
        this.baseContainerFragment = baseContainerFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.fragment_overtime_propose,
                wrapper, true);
        String category = "";
        startCalendar = Calendar.getInstance();
        begCalendar = (Calendar) startCalendar.clone();
        endCalendar = (Calendar) startCalendar.clone();
        if (parentFragment == null) {
            parentFragment = (TMSEmpFragment) this.getParentFragment().getParentFragment();
        }
        tvDMY = (TextView) rootView.findViewById(R.id.tvovertime_date);
        tvBegTime= (TextView) rootView.findViewById(R.id.tvovertime_begtime);
        tvEndTime= (TextView) rootView.findViewById(R.id.tvovertime_endtime);
        tvCancel = (TextView) rootView.findViewById(R.id.rtvbtn_cancel);
        tvPropose = (TextView) rootView.findViewById(R.id.rtvbtn_propose);
        etReason = (FloatLabeledEditText) rootView.findViewById(R.id.leave_reason);
        tvDMY.setText(char2(startCalendar.get(Calendar.DAY_OF_MONTH))+"."+char2(startCalendar.get(Calendar.MONTH)+1)+"."+startCalendar.get(Calendar.YEAR));
        tvBegTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvPropose.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        return rootView;
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
            case R.id.rtvbtn_propose:
                if(check_leave_overtime()){
                    overtime_propose(startCalendar.get(Calendar.YEAR)+"-"+char2(startCalendar.get(Calendar.MONTH)+1)+"-"+char2(startCalendar.get(Calendar.DAY_OF_MONTH)),tvBegTime.getText().toString(),tvEndTime.getText().toString(),etReason.getText().toString());
                }
                break;
            case R.id.rtvbtn_cancel:
                SelfOvertimeFragment fragment = new SelfOvertimeFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                ((BaseContainerFragment) this.getParentFragment()).replaceFragment(fragment, true);
                break;
        }
    }

    public void begShowTimePickerDialog() {
        TimePickerDialog timePickerDialog= new TimePickerDialog(parentFragment.getActivity(), begTimePickerListener, begCalendar.get(Calendar.HOUR_OF_DAY),begCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public void endShowTimePickerDialog() {
        if (endCalendar.getTimeInMillis() <= begCalendar.getTimeInMillis()) {
            endCalendar.add(Calendar.MINUTE,1);
        }
        TimePickerDialog timePickerDialog= new TimePickerDialog(parentFragment.getActivity(), endTimePickerListener, endCalendar.get(Calendar.HOUR_OF_DAY),begCalendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener begTimePickerListener
            = new TimePickerDialog.OnTimeSetListener() {

        // when dialog box is closed, below method will be called.
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            begCalendar.set(Calendar.HOUR_OF_DAY,selectedHour);
            begCalendar.set(Calendar.MINUTE,selectedMinute);
            tvBegTime.setText(char2(selectedHour)+":"+char2(selectedMinute));
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
            endCalendar.set(Calendar.HOUR_OF_DAY,selectedHour);
            endCalendar.set(Calendar.MINUTE,selectedMinute);
            tvEndTime.setText(char2(selectedHour)+":"+char2(selectedMinute));
        }
    };

    private boolean check_leave_overtime(){

        if(endCalendar.getTimeInMillis() <= begCalendar.getTimeInMillis()){
            Toast.makeText(getActivity(), "Check Period (Start and End Time) ",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etReason.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Fill Reason",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void overtime_propose(final String date,final String start_time, final String end_time,final String reason){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Send Overtime Propose....");
        tvPropose.setEnabled(false);
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().proposeOvertimeRequest(new OvertimeProposeRequest(device_type, device_id, nopeg,date,start_time,end_time,reason, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if(commonResponse.getStatus()==1){
                                    tvPropose.setEnabled(true);
                                    Toast.makeText(getActivity(), "Propose Success.",Toast.LENGTH_SHORT).show();
                                    SelfOvertimeFragment fragment = new SelfOvertimeFragment();
                                    Bundle bundle = new Bundle();
                                    fragment.setArguments(bundle);
                                    ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
                                }else{
                                    tvPropose.setEnabled(true);
                                    parentFragment.getDialog().showDialog(commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                tvPropose.setEnabled(true);
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

//    private void loadLeaveData(final String year, final Activity activity){

//    }
}
