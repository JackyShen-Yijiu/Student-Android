package com.sft.adapter;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.listener.OnDateClickListener;
import com.sft.util.CommonUtil;
import com.sft.util.LogUtil;
import com.sft.vo.AppointmentDay;

public class WeekAdapter extends ArrayAdapter<Integer> {

	private List<AppointmentDay> data;
	private Context mContext;
	private TextView lastClickView = null;
	private int lastIitem;

	public WeekAdapter(Context context, int resource, List<AppointmentDay> data) {
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
			// holder.rest = (TextView) convertView.findViewById(R.id.rest);
			holder.solar = (TextView) convertView.findViewById(R.id.solar);
			// holder.hasOrder = (ImageView) convertView
			// .findViewById(R.id.has_order);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.solar.setBackgroundDrawable(null);

		holder.solar.setTextColor(CommonUtil.getColor(mContext,
				R.color.text_color_dark));
		// holder.hasOrder.setVisibility(View.INVISIBLE);
		// holder.rest.setVisibility(View.INVISIBLE);
		final AppointmentDay appointmentDay = data.get(position);
		// final Integer item = appointmentDay.day;
		// final Integer month = appointmentDay.month;
		Calendar calendar = Calendar.getInstance();
		final int today = calendar.get(Calendar.DAY_OF_MONTH);
		final int thisMonth = calendar.get(Calendar.MONTH + 1);
		if (appointmentDay.day == today) {
			holder.solar.setTextColor(CommonUtil.getColor(mContext,
					R.color.new_app_main_color));
			// holder.hasOrder.setVisibility(View.VISIBLE);
			LogUtil.print("今天");
		} else if ((thisMonth == appointmentDay.month)
				&& (appointmentDay.day < today)) {
			holder.solar.setTextColor(CommonUtil.getColor(mContext,
					R.color.text_color_light));
			// holder.rest.setVisibility(View.VISIBLE);
		} else {
			holder.solar.setTextColor(CommonUtil.getColor(mContext,
					R.color.text_color_dark));
		}
		holder.solar.setText(appointmentDay.day + "");
		// LogUtil.print(appointmentDay.year + "-" + appointmentDay.month + "-"
		// + appointmentDay.day);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (OnDateClickListener.instance != null) {
					if ((thisMonth == appointmentDay.month)
							&& (appointmentDay.day < today)) {
						OnDateClickListener.instance.onDateClick(
								appointmentDay, false);
					} else {
						TextView textView = (TextView) ((RelativeLayout) v)
								.getChildAt(0);
						if (lastClickView != null) {
							LogUtil.print(lastClickView.getText().toString()
									+ "========");
							// if (lastClickView == textView) {
							// return;
							// }
							if (lastIitem == today) {
								lastClickView.setBackgroundDrawable(null);
								lastClickView.setTextColor(CommonUtil.getColor(
										mContext, R.color.new_app_main_color));
							} else {
								lastClickView.setBackgroundDrawable(null);
								lastClickView.setTextColor(CommonUtil.getColor(
										mContext, R.color.text_color_dark));
							}

						}
						textView.setBackgroundResource(R.drawable.day_selected);

						textView.setTextColor(CommonUtil.getColor(mContext,
								R.color.white));
						lastClickView = textView;
						lastIitem = appointmentDay.day;
						OnDateClickListener.instance.onDateClick(
								appointmentDay, true);

					}
				}

			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView solar;
		// private ImageView hasOrder;
	}

}
