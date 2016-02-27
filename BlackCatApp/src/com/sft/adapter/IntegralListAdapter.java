package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.vo.walletvo.Integrallist;

public class IntegralListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Integrallist> mData;
	private Context mContext;

	public IntegralListAdapter(Context context, List<Integrallist> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		mContext = context;
	}

	public void setData(List<Integrallist> mData) {
		this.mData = mData;
	}

	public List<Integrallist> getData() {
		return mData;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.integral_list_item, null);
			holder = new ViewHolder();
			holder.type = (TextView) convertView
					.findViewById(R.id.integral_type);
			holder.time = (TextView) convertView
					.findViewById(R.id.integral_time);
			holder.number = (TextView) convertView
					.findViewById(R.id.integral_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.number.setText(mData.get(position).getAmount());
		holder.time.setText(mData.get(position).getCreatetime());
		holder.type.setText(mData.get(position).getType());
		return convertView;
	}

	private class ViewHolder {

		public TextView number;
		public TextView time;
		public TextView type;
	}
}
