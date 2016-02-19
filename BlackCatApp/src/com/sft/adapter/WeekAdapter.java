package com.sft.adapter;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.listener.OnDateClickListener;
import com.sft.util.CommonUtil;
import com.sft.util.LogUtil;

public class WeekAdapter extends ArrayAdapter<Integer> {

	private List<Integer> data;
	private Context mContext;

	public WeekAdapter(Context context, int resource, List<Integer> data) {
		super(context, resource);
		mContext = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.calendar_date, null);
			holder.rest = (TextView) convertView.findViewById(R.id.rest);
			holder.solar = (TextView) convertView.findViewById(R.id.solar);
			holder.hasOrder = (ImageView) convertView
					.findViewById(R.id.has_order);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.hasOrder.setVisibility(View.INVISIBLE);
		holder.rest.setVisibility(View.INVISIBLE);
		final Integer item = data.get(position);
		Calendar calendar = Calendar.getInstance();
		final int today = calendar.get(Calendar.DAY_OF_MONTH);
		if (item == today) {
			holder.solar.setTextColor(CommonUtil.getColor(mContext,
					R.color.new_app_main_color));
			holder.hasOrder.setVisibility(View.VISIBLE);
			LogUtil.print("今天");
		} else if (item < today) {
			holder.solar.setTextColor(CommonUtil.getColor(mContext,
					R.color.text_color_light));
			holder.rest.setVisibility(View.VISIBLE);
		} else {
			holder.solar.setTextColor(CommonUtil.getColor(mContext,
					R.color.text_color_dark));
		}
		holder.solar.setText(item + "");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (OnDateClickListener.instance != null) {
					if (item < today) {

						OnDateClickListener.instance.onDateClick(item, false);
					} else {
						OnDateClickListener.instance.onDateClick(item, true);

					}
				}

			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView solar, rest;
		private ImageView hasOrder;
	}

}
