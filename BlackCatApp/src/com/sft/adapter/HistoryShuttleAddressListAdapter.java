package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzjf.app.R;

@SuppressLint("InflateParams")
public class HistoryShuttleAddressListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<String> mData;

	public HistoryShuttleAddressListAdapter(Context context, List<String> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.history_shuttle_list_item,
					null);
			holder = new ViewHolder();
			holder.address = (TextView) convertView
					.findViewById(R.id.history_shuttle_item_add_tv);
			holder.devider = (ImageView) convertView
					.findViewById(R.id.history_shuttle_item_devider_im);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.address.setText(mData.get(position));

		if (position == mData.size() - 1) {
			holder.devider.setVisibility(View.GONE);
		} else {
			holder.devider.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private class ViewHolder {
		public TextView address;
		public ImageView devider;
	}
}
