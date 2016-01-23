package com.sft.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.vo.MyAppointmentVO;

public class TodayAppointmentAdapter extends BaseAdapter {

	private List<MyAppointmentVO> myList;
	private Context mContext;

	public TodayAppointmentAdapter(List<MyAppointmentVO> myList,
			Context mContext) {
		super();
		this.myList = myList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public MyAppointmentVO getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		AppointmentHolder holder = null;
		if (convertView == null) {
			holder = new AppointmentHolder();
			convertView = View.inflate(mContext,
					R.layout.today_appointment_list_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.today_appointment_name);
			holder.time = (TextView) convertView
					.findViewById(R.id.today_appointment_time);
			holder.course = (TextView) convertView
					.findViewById(R.id.today_appointment_course);
			holder.isSign = (TextView) convertView
					.findViewById(R.id.today_appointment_sign);

			convertView.setTag(holder);
		} else {
			holder = (AppointmentHolder) convertView.getTag();
		}

		MyAppointmentVO myAppointmentVO = myList.get(position);

		if (myAppointmentVO != null) {
			if (myAppointmentVO.getCoachid() != null) {

				holder.name.setText(myAppointmentVO.getCoachid().getName());
			}

			String beginTime = UTC2LOC.instance.getDate(
					myAppointmentVO.getBegintime(), "HH:mm");
			String endTime = UTC2LOC.instance.getDate(
					myAppointmentVO.getEndtime(), "HH:mm");

			LogUtil.print("beginTime" + beginTime + "---endTime" + endTime);
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			try {
				long diffBeginTime = format.parse(beginTime).getTime()
						- new Date().getTime();
				long diffEndTime = format.parse(endTime).getTime()
						- new Date().getTime();
				if (diffBeginTime / 1000 / 60 > 15) {
					holder.isSign.setText("不可签到");
				} else if (diffEndTime / 1000 / 60 > 0) {
					holder.isSign.setText("不可签到");
				} else {
					holder.isSign.setText("可签到");
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			holder.time.setText(beginTime + "-" + endTime);
			holder.course.setText(myAppointmentVO.getCourseprocessdesc());
		}
		return convertView;
	}

	class AppointmentHolder {
		TextView name;
		TextView time;
		TextView course;
		TextView isSign;
	}

}
