package com.garuda.hcmobile.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.SlipContentRequest;
import com.garuda.hcmobile.model.SlipContentResponse;
import com.garuda.hcmobile.model.SlipFATAMonth;
import com.garuda.hcmobile.model.SlipKeyDataRequest;
import com.garuda.hcmobile.model.SlipMonth;
import com.garuda.hcmobile.model.SlipMonthDataRequest;
import com.garuda.hcmobile.model.SlipMonthResponse;
import com.garuda.hcmobile.model.SlipYearResponse;
import com.garuda.hcmobile.util.AESCrypt;
import com.garuda.hcmobile.util.DialogKey;
import com.garuda.hcmobile.util.DialogUniversalInfoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 9/6/2017.
 */
public class SlipFATAFragment extends Fragment implements View.OnClickListener  {
    private View rootView;
    public static final String TAG = "Slip FATA";

    private LinearLayout mFiltersLayout;

    private ArrayAdapter<String> adapterYear;
    private ArrayAdapter<String> adapterMonth;
    private DialogKey dialog;

    private DialogUniversalInfoUtils dialogInfo;

    MaterialSpinner spinnerYear;
    MaterialSpinner spinnerMonth;
    List<String> listItems = new ArrayList<>();
    private WebView web;


    public static SlipFATAFragment newInstance() {
        SlipFATAFragment f = new SlipFATAFragment();

        return f;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_slip_gaji,
                container, false);

        web = (WebView) rootView.findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);
        String strKey = ((MyApplication) this.getActivity().getApplication()).getSlipFATAKey();
        if(strKey==null || strKey.equals("")) {
            showDialogKey("Please Insert Your FATA Key");
        }else{
            openYear(strKey);
        }
        //checkingSpecialEmp();
        /*boolean isSpecial = checkingSpecialEmp();
        if(isSpecial){



        showDialogKey();
        for(int i=0;i<ITEMS.length;i++){
            listItems.add(ITEMS[i]);
        }


        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear = (MaterialSpinner) rootView.findViewById(R.id.spinnerYear);
        spinnerYear.setAdapter(adapter);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=-1){
                    String sYear = (String)spinnerYear.getSelectedItem();
                    Toast.makeText(getActivity(), sYear, Toast.LENGTH_SHORT).show();
                    mFiltersLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mFiltersLayout=(LinearLayout) rootView.findViewById(R.id.activity_month);*/
        return rootView;
    }

    public DialogUniversalInfoUtils getDialogInfo() {
        if (dialogInfo == null) {
            dialogInfo = new DialogUniversalInfoUtils(this.getActivity());
        }
        return dialogInfo;
    }

    private void generateSlipYear(){
        //
    }


    public void showDialogKey(String title){
        dialog = new DialogKey(this.getActivity());
        dialog.showDialog(title);
        TextView tvCancel = dialog.getCancelButton();
        TextView tvOK = dialog.getOkButton();
        tvCancel.setOnClickListener(this);
        tvOK.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok:
                if(dialog.getmDialogComment().getText().toString().equals("")==false) {
                    openYear(dialog.getmDialogComment().getText().toString());
                    dialog.dismissDialog();
                }else{
                    Toast.makeText(this.getActivity(), "Please Insert Your Key",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dialog_cancel:
                dialog.dismissDialog();
                break;
        }
    }



    private void openYear(final String key){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        //final String key = ((MyApplication) this.getActivity().getApplication()).getSlipKey();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Checking Key & Retrieving Year List...");

        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getSlipFATAYear(new SlipKeyDataRequest(device_type, device_id, nopeg,key, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<SlipYearResponse>() {
                            @Override
                            public void success(final SlipYearResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if(commonResponse.getStatus()==0){
                                    getDialogInfo().showDialog(commonResponse.getMessage());
                                }else if(commonResponse.getStatus()==1){
                                    ((MyApplication) getActivity().getApplication()).setSlipFATAKey(key);
                                    List<String> listItems = commonResponse.getData();
                                    adapterYear = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listItems);
                                    adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerYear = (MaterialSpinner) rootView.findViewById(R.id.spinnerYear);
                                    spinnerYear.setAdapter(adapterYear);
                                    spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if(position!=-1){
                                                String sYear = (String)spinnerYear.getSelectedItem();
                                                openMonth(sYear);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                    ((LinearLayout) rootView.findViewById(R.id.activity_slip)).setVisibility(View.VISIBLE);
                                }else{
                                    getDialogInfo().showDialog(commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                getDialogInfo().showDialog("Get Slip Auth " + error.getMessage());
                                //Log.e("GetLaunchDataError", "error auth " + error.getMessage());
                            }

                        });
            }

            @Override
            public void failure(RetrofitError error) {
                getDialogInfo().showDialog("Auth Error, " + error.getMessage());
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });
    }

    private void openMonth(final String year){
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        final String key = ((MyApplication) this.getActivity().getApplication()).getSlipFATAKey();
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Retrieving Periode List...");

        RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
            @Override
            public void success(final AuthenticateResponse authenticateResponse, Response response) {
                RestClient.getRestAPI().getSlipFATAMonth(new SlipMonthDataRequest(device_type, device_id, nopeg,key,year, dateTime),
                        authenticateResponse.getToken(),
                        new Callback<SlipMonthResponse>() {
                            @Override
                            public void success(final SlipMonthResponse commonResponse, Response response) {
                                progressDialog.dismiss();
                                if(commonResponse.getStatus()==0){
                                    getDialogInfo().showDialog(commonResponse.getMessage());
                                }else if(commonResponse.getStatus()==1){
                                    final String aesKey=authenticateResponse.getToken().substring(8,24);
                                    try {
                                        List<String> slipMonths=commonResponse.getSlipFATAMonthString(aesKey);
                                        adapterMonth = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, slipMonths);
                                        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerMonth = (MaterialSpinner) rootView.findViewById(R.id.spinnerMonth);
                                        spinnerMonth.setHint("Periode");
                                        spinnerMonth.setAdapter(adapterMonth);
                                        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if(position!=-1){
                                                    String sMonth = (String)spinnerMonth.getSelectedItem();
                                                    String sMonthCheck=(String)spinnerMonth.getItemAtPosition(position);
                                                    if(sMonth.equals(sMonthCheck)){
                                                        List<SlipFATAMonth> slipMonths = commonResponse.getSlipFATAMonth(aesKey);
                                                        SlipFATAMonth slipMonth = slipMonths.get(position);
                                                        if(sMonth.equals(slipMonth.getText())){
                                                            openContent(slipMonth.getDbulan(),slipMonth.getNopeg(),authenticateResponse);

                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                        ((LinearLayout) rootView.findViewById(R.id.activity_month)).setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                        getDialogInfo().showDialog(e.getMessage());
                                    }
                                }else{
                                    getDialogInfo().showDialog(commonResponse.getMessage());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                getDialogInfo().showDialog("Get Slip Auth " + error.getMessage());
                            }
                        });
            }
            @Override
            public void failure(RetrofitError error) {
                getDialogInfo().showDialog("Auth Error, " + error.getMessage());
                //Log.e("AuthActivity", "error auth " + error.getMessage());
            }

        });
    }

    private void openContent(final String dbulan,final String nopegx,AuthenticateResponse response){
        final String aesKey=response.getToken().substring(8,24);
        AESCrypt aesCrypt=null;
        try {
            aesCrypt = new AESCrypt(aesKey);
        } catch (Exception e) {

        }
        final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
        final String device_type = ((MyApplication) this.getActivity().getApplication()).getDevice_type();
        final String device_id = ((MyApplication) this.getActivity().getApplication()).getDevice_id();
        final String nopeg = ((MyApplication) this.getActivity().getApplication()).getUser().getNopeg();
        final String password = ((MyApplication) this.getActivity().getApplication()).getUser().getPassword();
        final String key = ((MyApplication) this.getActivity().getApplication()).getSlipFATAKey();
        String encryptedParam="";
        try {
            encryptedParam=aesCrypt.encrypt(dbulan+"|"+nopegx);
        } catch (Exception e) {

        }
        //get Launch Data from Server
        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "", "Retrieving Slip FATA...");

        RestClient.getRestAPI().getSlipFATAContent(new SlipContentRequest(device_type, device_id, nopeg,key,encryptedParam, dateTime),
            response.getToken(),
            new Callback<SlipContentResponse>() {
            @Override
            public void success(final SlipContentResponse commonResponse, Response response) {
                progressDialog.dismiss();
                if(commonResponse.getStatus()==0){
                    getDialogInfo().showDialog(commonResponse.getMessage());
                }else if(commonResponse.getStatus()==1){
                    String result=commonResponse.getEncryptedContent(aesKey);
                    web.loadUrl("about:blank");
                    web.loadData(result, "text/html; charset=utf-8", "UTF-8");

                }else{
                    getDialogInfo().showDialog(commonResponse.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                getDialogInfo().showDialog("Get Slip Auth " + error.getMessage());
            }
        });
    }



}
