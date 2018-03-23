package com.garuda.hcmobile.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.adapter.SelfListLeaveStickyList;
import com.garuda.hcmobile.model.ALeaveData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.LeaveDataRequest;
import com.garuda.hcmobile.model.LeaveDataResponse;
import com.garuda.hcmobile.model.LeaveProposeRequest;
import com.garuda.hcmobile.view.component.FloatLabeledEditText;
import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.util.StickyListHeadersListViewWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by user on 5/19/2016.
 */
public class FormLeaveFragment extends Fragment implements OnClickListener {
    private View rootView;
    private TMSEmpFragment parentFragment;
    private TextView tvBegda;
    private TextView tvEndda;
    private TextView tvCancel;
    private TextView tvPropose;
    private FloatLabeledEditText etReason;
    private BaseContainerFragment baseContainerFragment;
    private Spinner spinnerLeaveType;

    private int begYear;
    private int begMonth;
    private int begDay;
    private int endYear;
    private int endMonth;
    private int endDay;
    private Calendar begCalendar;
    private Calendar endCalendar;
    private Calendar startCalendar;


    public static FormLeaveFragment newInstance() {
        FormLeaveFragment f = new FormLeaveFragment();
        return f;
    }

    public void setBaseContainerFragment(BaseContainerFragment baseContainerFragment) {
        this.baseContainerFragment = baseContainerFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.fragment_leave_propose,
                wrapper, true);
        String category = "";
        startCalendar = Calendar.getInstance();
        begCalendar = (Calendar) startCalendar.clone();
        endCalendar = (Calendar) startCalendar.clone();
        begYear = endYear = begCalendar.get(Calendar.YEAR);
        begMonth = endMonth = begCalendar.get(Calendar.MONTH);
        begDay = endDay = begCalendar.get(Calendar.DAY_OF_MONTH);
        if (parentFragment == null) {
            parentFragment = (TMSEmpFragment) this.getParentFragment().getParentFragment();
        }
        tvBegda = (TextView) rootView.findViewById(R.id.tvleave_begda);
        tvEndda = (TextView) rootView.findViewById(R.id.tvleave_endda);
        tvPropose = (TextView) rootView.findViewById(R.id.leave_propose);
        tvCancel = (TextView) rootView.findViewById(R.id.leave_cancel);
        etReason = (FloatLabeledEditText) rootView.findViewById(R.id.leave_reason);
        spinnerLeaveType=(Spinner) rootView.findViewById(R.id.leave_spinner);
        tvBegda.setOnClickListener(this);
        tvEndda.setOnClickListener(this);
        tvPropose.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
//        Log.i("V.ID", "IS " + v.getId());
        int iyear = 0;
        switch (v.getId()) {
            case R.id.tvleave_begda:
                begdaShowDatePickerDialog();
                break;
            case R.id.tvleave_endda:
                enddaShowDatePickerDialog();
                break;
            case R.id.leave_propose:
                if(check_leave_propose()){
                    String[] aLeaveKey = getResources().getStringArray(R.array.leave_key_arrays);
                    leave_propose(aLeaveKey[(int)spinnerLeaveType.getSelectedItemId()],
                            new StringBuilder().append(begYear)
                                    .append("-").append(char2(begMonth + 1)).append("-").append(char2(begDay)).toString(),
                            new StringBuilder().append(endYear)
                                    .append("-").append(char2(endMonth + 1)).append("-").append(char2(endDay)).toString(),
                            etReason.getText().toString());
                }
                break;
            case R.id.leave_cancel:
                SelfLeaveFragment fragment = new SelfLeaveFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                ((BaseContainerFragment) this.getParentFragment()).replaceFragment(fragment, true);
                break;
        }
    }

    public void begdaShowDatePickerDialog() {

        DatePickerDialog begDatePicker = new DatePickerDialog(parentFragment.getActivity(), begDatePickerListener, begYear, begMonth, begDay);
        begDatePicker.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
        //begDatePicker.getDatePicker().setCalendarViewShown(false);
        //begDatePicker.getDatePicker().setSpinnersShown(true);
        begDatePicker.show();
    }

    public void enddaShowDatePickerDialog() {
        DatePickerDialog endDatePicker = new DatePickerDialog(parentFragment.getActivity(), endDatePickerListener, endYear, endMonth, endDay);
        if (startCalendar.getTimeInMillis() == begCalendar.getTimeInMillis()) {
            //Log.i("min START", "" + startCalendar.getTimeInMillis());
            endDatePicker.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
        } else {
            //Log.i("min BEGCALENDAR", "" + begCalendar.getTimeInMillis());
            endDatePicker.getDatePicker().setMinDate(begCalendar.getTimeInMillis());
        }
        endDatePicker.show();
    }

    private DatePickerDialog.OnDateSetListener begDatePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.set(selectedYear, selectedMonth, selectedDay);
            if (calendarNow.getTimeInMillis() < view.getMinDate()) {
                begdaShowDatePickerDialog();
                Toast.makeText(getActivity(), getResources().getString(R.string.createOrder_datePicker_selectFutureDate), Toast.LENGTH_SHORT).show();
                view.updateDate(begCalendar.get(Calendar.YEAR), begCalendar.get(Calendar.MONTH), begCalendar.get(Calendar.DAY_OF_MONTH));
            }

            begYear = selectedYear;
            begMonth = selectedMonth;
            begDay = selectedDay;
            begCalendar.set(selectedYear, selectedMonth, selectedDay);
            tvBegda.setText(new StringBuilder().append(char2(begDay))
                    .append(".").append(char2(begMonth + 1)).append(".").append(begYear));
            if (tvEndda.getText().toString().equals(getResources().getString(R.string.leave_dmy))) {
                endYear = selectedYear;
                endMonth = selectedMonth;
                endDay = selectedDay;
                endCalendar.set(selectedYear, selectedMonth, selectedDay);
                tvEndda.setText(new StringBuilder().append(char2(endDay))
                        .append(".").append(char2(endMonth + 1)).append(".").append(endYear));
            }
        }
    };

    private String char2(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return "" + num;
    }

    private DatePickerDialog.OnDateSetListener endDatePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            Calendar calendarNow = (Calendar) endCalendar.clone();
            calendarNow.set(selectedYear, selectedMonth, selectedDay);
            if (calendarNow.getTimeInMillis() < view.getMinDate()) {
                enddaShowDatePickerDialog();
                Toast.makeText(getActivity(), getResources().getString(R.string.createOrder_datePicker_selectPastDate), Toast.LENGTH_SHORT).show();
                view.updateDate(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));
            }

            endYear = selectedYear;
            endMonth = selectedMonth;
            endDay = selectedDay;
            endCalendar.set(selectedYear, selectedMonth, selectedDay);
            tvEndda.setText(new StringBuilder().append(char2(endDay))
                    .append(".").append(char2(endMonth + 1)).append(".").append(endYear));

            if (tvBegda.getText().toString().equals(getResources().getString(R.string.leave_dmy))) {
                begYear = selectedYear;
                begMonth = selectedMonth;
                begDay = selectedDay;
                begCalendar.set(selectedYear, selectedMonth, selectedDay);
                tvBegda.setText(new StringBuilder().append(char2(begDay))
                        .append(".").append(char2(begMonth + 1)).append(".").append(begYear));
            }
        }
    };

    private boolean check_leave_propose(){

        if(endCalendar.getTimeInMillis() < begCalendar.getTimeInMillis()){
            Toast.makeText(getActivity(), "Check Period (Begin and End Date) ",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etReason.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Fill Reason",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void leave_propose(final String leave_type,final String begda, final String endda,final String reason){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Send Leave Propose....");
        tvPropose.setEnabled(false);
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().proposeLeaveRequest(new LeaveProposeRequest(device_type, device_id, nopeg,leave_type,begda,endda,reason, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if(commonResponse.getStatus()==1){
                                    tvPropose.setEnabled(true);
                                    Toast.makeText(getActivity(), "Propose Success.",Toast.LENGTH_SHORT).show();
                                    SelfLeaveFragment fragment = new SelfLeaveFragment();
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
