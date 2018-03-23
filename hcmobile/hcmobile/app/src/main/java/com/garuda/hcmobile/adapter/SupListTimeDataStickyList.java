package com.garuda.hcmobile.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.RestClient;
import com.garuda.hcmobile.fragment.SupTimeDataFragment;
import com.garuda.hcmobile.fragment.TMSSupFragment;
import com.garuda.hcmobile.model.AEmp;
import com.garuda.hcmobile.model.ATimeData;
import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.TimeDataRequest;
import com.garuda.hcmobile.model.TimeDataResponse;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class SupListTimeDataStickyList extends ArrayAdapter<String>
		implements StickyListHeadersAdapter,OnClickListener {

	private final Context mContext;
	private LayoutInflater mInflater;
	private List<AEmp> aEmps;
	private List<ATimeData> aTimeDataList;
	private String headerMonth;
	private SupTimeDataFragment parentFragment;
	private String year;
	private String month;
	private String selectedEmp;
	private int selectedSpinner;
	private HeaderViewHolder holderHeader;

	public SupListTimeDataStickyList(final Context context, final SupTimeDataFragment tmsEmpFragment, List<AEmp> aEmps) {
		mContext = context;
		this.parentFragment=tmsEmpFragment;
		this.aEmps = aEmps;
		Date date = new Date();
		month = new SimpleDateFormat("MM").format(date);
		year = new SimpleDateFormat("yyyy").format(date);
		this.headerMonth=month+"."+year;
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
					R.layout.list_item_sticky_time_data, parent, false);
			holder = new ViewHolder();

			holder.headerTimeData= (TextView) convertView
					.findViewById(R.id.list_item_sticky_header_date);
			holder.sched= (TextView) convertView
					.findViewById(R.id.list_item_sticky_header_sched2);
			holder.real= (TextView) convertView
					.findViewById(R.id.list_item_sticky_header_real2);
			holder.ket= (TextView) convertView
					.findViewById(R.id.list_item_sticky_header_ket);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(aTimeDataList!=null && aTimeDataList.size()>0) {
			ATimeData timeData = aTimeDataList.get(position);

			holder.headerTimeData.setText(timeData.getHari() + " - " + timeData.getTanggal());
			holder.sched.setText(timeData.getJam_masuk() + " - " + timeData.getJam_keluar());
			holder.real.setText(timeData.getClock_in() + " - " + timeData.getClock_out());
			holder.ket.setText(timeData.getTmh_keterangan());
		}else{
			convertView.setVisibility(View.GONE);
		}

		return convertView;
	}

	private static class ViewHolder {
		public/* Roboto */ TextView headerTimeData;
		public/* Roboto */TextView sched;
		public/* Roboto */TextView real;
		public/* Roboto */TextView ket;
	}

	private static class HeaderViewHolder {
		public/* Roboto */TextView date;
		public/* Roboto */TextView left;
		public/* Roboto */TextView right;
		public/* Roboto */SearchableSpinner actSubordinat;
		public LinearLayout ll_control;
	}

	@Override
	public View getHeaderView(final int position, final View convertView,
			final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.list_sup_header_time_data, parent, false);
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
						loadTimeData(selectedEmp, year, month);
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
		if(holderHeader.actSubordinat.getSelectedItem()!=null && aTimeDataList==null){
			AEmp aEmp=parentFragment.getTMSSupFragment().getaEmps().get(holderHeader.actSubordinat.getSelectedItemPosition());
			selectedEmp=aEmp.getNopeg();
			selectedSpinner=0;
			loadTimeData(selectedEmp,year,month);
		}else{
			holderHeader.actSubordinat.setSelection(selectedSpinner);
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
		int imonth=0;
		switch (v.getId()) {
		case R.id.list_item_header_left:
			Toast.makeText(mContext, "Left: " ,
					Toast.LENGTH_SHORT).show();
			imonth = Integer.parseInt(month);
			imonth--;
			if(imonth<=0){
				parentFragment.getTMSSupFragment().getDialog().showDialog("Maximum January or Desember on Current Year");
			}else{
				String nMonth="";
				if(imonth<=9){
					nMonth="0"+imonth;
				}else{
					nMonth=""+imonth;
				}
				loadTimeData(selectedEmp,year,nMonth);
			}
			break;
		case R.id.list_item_header_right:
			Toast.makeText(mContext, "Right: " ,
					Toast.LENGTH_SHORT).show();
			imonth = Integer.parseInt(month);
			imonth++;
			//Log.i("month",""+imonth);
			//Log.i("year",year);
			if(imonth>12){
				parentFragment.getTMSSupFragment().getDialog().showDialog("Maximum January or Desember on Current Year");
			}else{
				String nMonth="";
				if(imonth<=9){
					nMonth="0"+imonth;
				}else{
					nMonth=""+imonth;
				}
				loadTimeData(selectedEmp,year,nMonth);
			}
			break;
		}
	}

	private void loadTimeData(final String selectedEmp,final String syear, final String smonth ){
		final String dateTime = new SimpleDateFormat("yyyyMMdd|HHmmss").format(new Date());
		final String device_type = ((MyApplication) parentFragment.getTMSSupFragment().getActivity().getApplication()).getDevice_type();
		final String device_id = ((MyApplication) parentFragment.getTMSSupFragment().getActivity().getApplication()).getDevice_id();
		final String nopeg = ((MyApplication) parentFragment.getTMSSupFragment().getActivity().getApplication()).getUser().getNopeg();
		final String password = ((MyApplication) parentFragment.getTMSSupFragment().getActivity().getApplication()).getUser().getPassword();
		//get Launch Data from Server
		final ProgressDialog progressDialog = ProgressDialog.show(parentFragment.getTMSSupFragment().getActivity(), "", "Load Time Data....");
		RestClient.getRestAPI().authenticate(device_type, device_id, password, nopeg, dateTime, new Callback<AuthenticateResponse>() {
			@Override
			public void success(final AuthenticateResponse authenticateResponse, Response response) {
				RestClient.getRestAPI().getSubTimeData(new TimeDataRequest(device_type, device_id, selectedEmp,syear,smonth, dateTime),
						authenticateResponse.getToken(),
						new Callback<TimeDataResponse>() {
							@Override
							public void success(final TimeDataResponse timeDataResponse, Response response) {
								progressDialog.dismiss();
								if(timeDataResponse.getStatus()==1){
									clear();
									aTimeDataList=timeDataResponse.getData();
									for (int i = 0; i < aTimeDataList.size(); i++) {
										add("Row number " );
									}
									if(aTimeDataList.size()==0){
										add("Row number " );
									}
									year=syear;
									month=smonth;
									headerMonth=smonth+"."+syear;
									//holder.date.setText(headerMonth);

//									Date date = new Date();
//									String monthNowFull = new SimpleDateFormat("MM").format(date);
//									String monthNow2Digit = new SimpleDateFormat("MM").format(date);
//									String yearNow4Figit = new SimpleDateFormat("yyyy").format(date);
//									StickyListHeadersListView listView = (StickyListHeadersListView) rootView.findViewById(R.id.activity_stickylistheaders_listview);
//									listView.setFitsSystemWindows(true);
//									AlphaInAnimationAdapter animationAdapter;
//									List<ATimeData> aTimeDataList = timeDataResponse.getData();
//									MyStickyListHeadersTimeDataAdapter adapterSocial = new MyStickyListHeadersTimeDataAdapter(
//											activity, timeDataResponse.getData(), monthNowFull + " " + yearNow4Figit);
//									animationAdapter = new AlphaInAnimationAdapter(adapterSocial);
//									StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(
//											animationAdapter);
//									stickyListHeadersAdapterDecorator
//											.setListViewWrapper(new StickyListHeadersListViewWrapper(
//													listView));
//									assert animationAdapter.getViewAnimator() != null;
//									animationAdapter.getViewAnimator().setInitialDelayMillis(500);
//									assert stickyListHeadersAdapterDecorator.getViewAnimator() != null;
//									stickyListHeadersAdapterDecorator.getViewAnimator()
//											.setInitialDelayMillis(500);
//									listView.setAdapter(stickyListHeadersAdapterDecorator);
								}else{
									parentFragment.getTMSSupFragment().getDialog().showDialog(timeDataResponse.getMessage());
								}
							}

							@Override
							public void failure(RetrofitError error) {
								progressDialog.dismiss();
								parentFragment.getTMSSupFragment().getDialog().showDialog("Get Time Data Error, " + error.getMessage());
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