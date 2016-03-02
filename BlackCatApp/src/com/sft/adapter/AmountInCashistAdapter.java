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
import com.sft.util.UTC2LOC;
import com.sft.vo.MoneyListVO;

@SuppressLint("InflateParams")
public class AmountInCashistAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MoneyListVO> mData;

	public AmountInCashistAdapter(Context context, List<MoneyListVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	public void setData(List<MoneyListVO> mData) {
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
			holder.date = (TextView) convertView
					.findViewById(R.id.mywallet_item_date_tv);
			holder.name = (TextView) convertView
					.findViewById(R.id.mywallet_item_name_tv);
			holder.income = (TextView) convertView
					.findViewById(R.id.mywallet_item_income_tv);
			holder.title = (TextView) convertView
					.findViewById(R.id.my_wallet_title_tv);
			holder.name.setVisibility(View.GONE);
			holder.income.setTextSize(18);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(mData.get(position).getType());
		holder.date.setText(UTC2LOC.instance.getDate(mData.get(position)
				.getCreatetime(), "yyyy/MM/dd"));
		holder.income.setText(mData.get(position).getIncome());
		return convertView;
	}

	private class ViewHolder {
		public TextView date;
		public TextView income;
		public TextView name;
		public TextView title;
	}
}
