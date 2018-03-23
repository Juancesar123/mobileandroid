package com.garuda.hcmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.R;
import com.garuda.hcmobile.model.ATimeData;
import com.garuda.hcmobile.model.DummyModel;
import com.garuda.hcmobile.util.ImageUtil;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MyStickyListHeadersSocialAdapter extends ArrayAdapter<String>
		implements StickyListHeadersAdapter,OnClickListener {

	private final Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<ATimeData> aTimeDataList;

	public MyStickyListHeadersSocialAdapter(final Context context, List<ATimeData> aTimeDataList ) {
		mContext = context;
		aTimeDataList = aTimeDataList;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		for (int i = 0; i < 100; i++) {
//			add("Row number " );
//		}
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
		ATimeData timeData = aTimeDataList.get(position);

		holder.headerTimeData.setText(timeData.getHari()+" - " + timeData.getTanggal());
		holder.sched.setText(timeData.getJam_masuk()+" - "+timeData.getJam_keluar());
		holder.real.setText(timeData.getClock_in()+" - "+timeData.getClock_out());
		holder.ket.setText(timeData.getTmh_keterangan());
		return convertView;
	}

	private static class ViewHolder {
		public/* Roboto */ TextView headerTimeData;
		public/* Roboto */TextView sched;
		public/* Roboto */TextView real;
		public/* Roboto */TextView ket;
	}

	private static class HeaderViewHolder {
		public/* Roboto */TextView day;
		public/* Roboto */TextView date;
	}

	@Override
	public View getHeaderView(final int position, final View convertView,
			final ViewGroup parent) {
		View view = convertView;
		final HeaderViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.list_header_social, parent, false);
			holder = new HeaderViewHolder();
//			holder.day = (TextView) view
//					.findViewById(R.id.list_header_social_day);
			holder.date = (TextView) view
					.findViewById(R.id.list_header_social_date);
			view.setTag(holder);
		} else {
			holder = (HeaderViewHolder) view.getTag();
		}

//		holder.day.setText("Yesterday");
		holder.date.setText("14.6.2015.");
		// holder.name.setText("Header " + getHeaderId(position));

		return view;
	}

	@Override
	public long getHeaderId(final int position) {
		return position / 5;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) v.getTag();
		switch (v.getId()) {
		case R.id.list_item_header_left:
			Toast.makeText(mContext, "User image: " + position,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.list_item_header_right:
			Toast.makeText(mContext, "Like icon: " + position,
					Toast.LENGTH_SHORT).show();
			break;
		}
	}
}