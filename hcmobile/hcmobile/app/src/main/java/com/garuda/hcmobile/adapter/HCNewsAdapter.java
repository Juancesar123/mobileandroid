package com.garuda.hcmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.garuda.hcmobile.R;
import com.garuda.hcmobile.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class HCNewsAdapter extends ArrayAdapter<NewsModel>
		implements OnClickListener {

	private LayoutInflater mInflater;

	public static ArrayList<NewsModel> getDummyContent(){
		ArrayList<NewsModel> list = new ArrayList<>();

		list.add(new NewsModel(1, "Welcome To HC-Mobile", "Currently we dont have any news","2016-09-30 14:32"));

		return list;
	}
	public HCNewsAdapter(Context context, List<NewsModel> items) {
		super(context, 0, items);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.list_item_news_cards, parent, false);
			holder = new ViewHolder();

			holder.title = (TextView) convertView
					.findViewById(R.id.list_item_news_title);
			holder.content = (WebView) convertView
					.findViewById(R.id.webView);
			holder.content.getSettings().setJavaScriptEnabled(true);
			holder.footer = (TextView) convertView
					.findViewById(R.id.list_item_news_footer);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		NewsModel item = getItem(position);
		holder.title.setText(item.getmTitle());
		holder.content.loadData(item.getmText(),"text/html",null);
		holder.footer.setText(item.getmFooter());
		//holder.text.setText(R.string.lorem_ipsum_short);
		//holder.like.setTag(position);
		//holder.follow.setTag(position);

		return convertView;
	}

	private static class ViewHolder {
		public TextView title;
		public WebView content;
		public TextView footer;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int possition = (Integer) v.getTag();
		//switch (v.getId()) {
		//
		//}
	}
}
