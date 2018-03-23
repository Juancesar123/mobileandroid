package com.garuda.hcmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garuda.hcmobile.R;
import com.garuda.hcmobile.fragment.SelfClockInOutFragment;
import com.garuda.hcmobile.fragment.SelfClockInOutFragment2;
import com.garuda.hcmobile.model.ALocation;
import com.garuda.hcmobile.model.DummyModel;
import com.garuda.hcmobile.util.ImageUtil;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.ArrayList;
import java.util.Collections;

public class ParallaxSocialAdapter extends BaseAdapter implements Swappable,
		OnClickListener {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<ALocation> mDummyModelList;
	private SelfClockInOutFragment2 mParentFragment;

	public ParallaxSocialAdapter(SelfClockInOutFragment2 parentFragment,Context context,
								 ArrayList<ALocation> dummyModelList,
								 boolean shouldShowDragAndDropIcon) {
		mContext = context;
		mParentFragment=parentFragment;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDummyModelList = dummyModelList;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		return mDummyModelList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDummyModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDummyModelList.get(position).getId();
	}

	@Override
	public void swapItems(int positionOne, int positionTwo) {
		Collections.swap(mDummyModelList, positionOne, positionTwo);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.list_item_parallax_social, parent, false);
			holder = new ViewHolder();
			holder.header=(TextView) convertView.findViewById(R.id.lvis_header);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.layout_header_of_item);
			holder.image = (ImageView) convertView
					.findViewById(R.id.lvis_image);
			holder.share = (TextView) convertView.findViewById(R.id.lvis_checkin);
			holder.image.setOnClickListener(this);
			holder.share.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position != 0)
			holder.layout.setVisibility(View.GONE);
		else
			holder.layout.setVisibility(View.VISIBLE);

		ALocation dm = mDummyModelList.get(position);
		holder.header.setText(dm.getLocation_name());
		ImageUtil.displayImage(holder.image, dm.getImageURL(), null);
		holder.image.setTag(position);
		holder.share.setTag(position);
		return convertView;
	}

	private static class ViewHolder {
		public LinearLayout layout;
		public ImageView image;
		public TextView share;
		public TextView header;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) v.getTag();
		switch (v.getId()) {
		case R.id.lvis_image:
			Toast.makeText(mContext, "Image: " + position, Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.lvis_checkin:
			ALocation dm = mDummyModelList.get(position);
			mParentFragment.sendCheckInLocation(dm.getLocation_name(),dm.getId());
			Toast.makeText(mContext, "Checking in this Building: " + dm.getLocation_name() + " : " +dm.getId(), Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}
}
