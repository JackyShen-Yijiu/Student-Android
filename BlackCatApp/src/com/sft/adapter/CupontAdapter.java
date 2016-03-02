package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.util.UTC2LOC;
import com.sft.vo.MyCuponVO;

public class CupontAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MyCuponVO> mData;
	private String producttype;

	public CupontAdapter(Context context, List<MyCuponVO> mData,
			String producttype) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.producttype = producttype;
	}

	public void setData(List<MyCuponVO> mData) {
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
			holder.income = (TextView) convertView
					.findViewById(R.id.mywallet_item_income_tv);
			holder.title = (TextView) convertView
					.findViewById(R.id.my_wallet_title_tv);
			convertView.findViewById(R.id.mywallet_item_name_tv).setVisibility(
					View.VISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(mData.get(position).getState());
		holder.income.setText(mData.get(position).get_id());
		holder.date.setText(UTC2LOC.instance.getDate(mData.get(position)
				.getCreatetime(), "yyyy/MM/dd"));
		// holder.income.setText(mData.get(position).getAmount());
		return convertView;
	}

	private class ViewHolder {
		public TextView date;
		public TextView title;
		public TextView income;
	}
}
