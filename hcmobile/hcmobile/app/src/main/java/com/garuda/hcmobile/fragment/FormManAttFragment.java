package com.garuda.hcmobile.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.LeaveProposeRequest;
import com.garuda.hcmobile.model.ManAttProposeRequest;
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
public class FormManAttFragment extends Fragment implements OnClickListener {
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


    public static FormManAttFragment newInstance() {
        FormManAttFragment f = new FormManAttFragment();
        return f;
    }

    public void setBaseContainerFragment(BaseContainerFragment baseContainerFragment) {
        this.baseContainerFragment = baseContainerFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity());
        rootView = inflater.inflate(R.layout.fragment_manatt_propose,
                wrapper, true);
        String category = "";
        startCalendar = Calendar.getInstance();
        begCalendar = (Calendar) startCalendar.clone();
        begYear = begCalendar.get(Calendar.YEAR);
        begMonth = begCalendar.get(Calendar.MONTH)+1;
        begDay = begCalendar.get(Calendar.DAY_OF_MONTH);
        if (parentFragment == null) {
            parentFragment = (TMSEmpFragment) this.getParentFragment().getParentFragment();
        }
        tvBegda = (TextView) rootView.findViewById(R.id.tvleave_begda);
        tvPropose = (TextView) rootView.findViewById(R.id.leave_propose);
        tvCancel = (TextView) rootView.findViewById(R.id.leave_cancel);
        etReason = (FloatLabeledEditText) rootView.findViewById(R.id.leave_reason);
        tvBegda.setText(char2(begDay)+"."+char2(begMonth)+"."+begYear);
        tvPropose.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int iyear = 0;
        switch (v.getId()) {
            case R.id.leave_propose:
                if(check_propose()){
                    manatt_propose(
                            new StringBuilder().append(begYear)
                                    .append("-").append(char2(begMonth)).append("-").append(char2(begDay)).toString(),
                            etReason.getText().toString());
                }
                break;
            case R.id.leave_cancel:
                SelfManAttFragment fragment = new SelfManAttFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                ((BaseContainerFragment) this.getParentFragment()).replaceFragment(fragment, true);
                break;
        }
    }

    private String char2(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return "" + num;
    }

    private boolean check_propose(){
        if(etReason.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Fill Reason",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void manatt_propose(final String begda,final String reason){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Send ManAtt Propose....");
        tvPropose.setEnabled(false);
        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().proposeManAttRequest(new ManAttProposeRequest(device_type, device_id, nopeg,begda,reason, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<CommonResponse>() {
                            @Override
                            public void success(final CommonResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if(commonResponse.getStatus()==1){
                                    tvPropose.setEnabled(true);
                                    Toast.makeText(getActivity(), "Propose Success.",Toast.LENGTH_SHORT).show();
                                    SelfManAttFragment fragment = new SelfManAttFragment();
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
                                parentFragment.getDialog().showDialog("propose ManAtt Error, " + error.getMessage());
                                //Log.e("propose ManAtt Error", "error auth " + error.getMessage());
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