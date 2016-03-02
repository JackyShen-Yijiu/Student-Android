package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;

@SuppressLint("InflateParams")
public class SuggestAddressListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<String> mData;

	public SuggestAddressListAdapter(Context context, List<String> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	public void setData(List<String> mData) {
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
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.suggest_address_list_item,
					null);
			holder.name = (TextView) convertView
					.findViewById(R.id.shuttle_address_item_address_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(mData.get(position));
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
	}
}
