package com.sft.adapter;

import java.util.List;

import com.sft.blackcatapp.R;
import com.sft.util.UTC2LOC;
import com.sft.vo.IncomeVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class IncomeListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<IncomeVO> mData;

	public IncomeListAdapter(Context context, List<IncomeVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	public void setData(List<IncomeVO> mData) {
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
			convertView = mInflater.inflate(R.layout.mywallet_list_item, null);
			holder.date = (TextView) convertView.findViewById(R.id.mywallet_item_date_tv);
			holder.income = (TextView) convertView.findViewById(R.id.mywallet_item_income_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.date.setText(UTC2LOC.instance.getDate(mData.get(position).getCreatetime(), "yyyy/mm/dd"));
		holder.income.setText(mData.get(position).getAmount());
		return convertView;
	}

	private class ViewHolder {
		public TextView date;
		public TextView income;
	}
}
