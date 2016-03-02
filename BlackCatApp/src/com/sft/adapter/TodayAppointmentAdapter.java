package com.sft.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
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

			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			SimpleDateFormat format1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				long diffBeginTime = UTC2LOC.instance.getDates(
						myAppointmentVO.getBegintime(), "yyyy-MM-dd HH:mm:ss")
						.getTime()
						- new Date().getTime();
				long diffEndTime = UTC2LOC.instance.getDates(
						myAppointmentVO.getEndtime(), "yyyy-MM-dd HH:mm:ss")
						.getTime()
						- new Date().getTime();
				LogUtil.print("diffBeginTime" + diffBeginTime / 1000 / 60
						+ "---diffEndTime" + diffEndTime / 1000 / 60);
				if (diffBeginTime / 1000 / 60 > 15) {
					LogUtil.print("sss");
					holder.isSign.setText("不可签到");
				} else if (diffEndTime / 1000 / 60 < 0) {
					holder.isSign.setText("不可签到");
				} else {
					holder.isSign.setText("可签到");
				}

			} catch (Exception e) {
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
