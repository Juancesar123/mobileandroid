package com.garuda.hcmobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.garuda.hcmobile.MyApplication;
import com.garuda.hcmobile.R;
import com.garuda.hcmobile.model.MenuModel;
import com.garuda.hcmobile.util.DummyContent;

import java.util.List;

public class DrawerMenuAdapter extends BaseAdapter {

	private List<MenuModel> mDrawerItems;
	private LayoutInflater mInflater;
	private Context context;
	private MyApplication myApplication;
	public DrawerMenuAdapter(Context context, List<MenuModel> menuModels,MyApplication myApplication) {
		this.context=context;
		this.myApplication=myApplication;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDrawerItems = menuModels;
	}

	public List<MenuModel> getmDrawerItems(){
		return mDrawerItems;
	}

	@Override
	public int getCount() {
		return mDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDrawerItems.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.list_view_item_navigation_drawer_social, parent,
					false);
			holder = new ViewHolder();
			holder.icon = (TextView) convertView
					.findViewById(R.id.icon_social_navigation_item);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.title.setTypeface(myApplication.getTypeface());
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MenuModel item = mDrawerItems.get(position);
		holder.icon.setText(R.string.material_icon_clock);
		holder.title.setText(item.getText());

		return convertView;
	}
}
