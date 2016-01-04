package com.sft.viewutil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.common.BlackCatApplication;
import com.sft.util.UTC2LOC;
import com.sft.vo.CoachCourseVO;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class AppointmentCarTimeLayout extends LinearLayout implements
		OnCheckedChangeListener {

	public static final int selected = 1;
	public static final int noSelected = 2;
	public static final int over = 0;
	// over 对应多种
	public static final int timeout = 0;
	public static final int has = 1;
	public static final int other = 2;

	public TextView startTimeTv, endTimeTv, countTv, endTv;

	public CheckBox ck;

	private TimeLayoutSelectedChangeListener selectedListener;
	private CoachCourseVO coachCourseVO;

	public interface TimeLayoutSelectedChangeListener {
		public void onTimeLayoutSelectedChange(AppointmentCarTimeLayout layout,
				CoachCourseVO coachCourseVO, boolean selected);
	}

	public void setSelectedChangeListener(
			TimeLayoutSelectedChangeListener selectedListener) {
		this.selectedListener = selectedListener;
	}

	public AppointmentCarTimeLayout(Context context) {
		super(context);
		init(context);
	}

	public AppointmentCarTimeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AppointmentCarTimeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void setOver(boolean isOver, int type) {
		ck.setEnabled(!isOver);
		if (isOver) {
			setTextColor(over, type);
		} else {
			setTextColor(noSelected, type);
		}
	}

	public void setVaule(CoachCourseVO coachCourseVO) {
		this.coachCourseVO = coachCourseVO;
		if (coachCourseVO == null) {
			ck.setOnCheckedChangeListener(null);
			ck.setChecked(false);
			ck.setOnCheckedChangeListener(this);
			startTimeTv.setText("");
			endTimeTv.setText("");
			countTv.setText("剩余0个名额");
			setOver(true, other);
			return;
		}
		String beginTime = coachCourseVO.getCoursetime().getBegintime();
		beginTime = beginTime.substring(0, beginTime.lastIndexOf(":"));
		startTimeTv.setText(beginTime);
		String endTime = coachCourseVO.getCoursetime().getEndtime();
		endTime = endTime.substring(0, endTime.lastIndexOf(":"));
		endTimeTv.setText(endTime);

		if (Arrays.asList(coachCourseVO.getCourseuser()).contains(
				BlackCatApplication.getInstance().userVO.getUserid())) {
			// 此预约是否已经预约
			countTv.setText("您已预约");
			setOver(true, has);
		} else {
			try {
				if (beginTime.length() == 4)
					beginTime = "0" + beginTime;
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				long courseTime = format.parse(
						UTC2LOC.instance.getDate(
								coachCourseVO.getCoursebegintime(),
								"yyyy-MM-dd") + " " + beginTime).getTime();
				long curTime = date.getTime();
				if (curTime > courseTime) {
					// 此预约是否已过当前时间
					countTv.setText("已过时");
					setOver(true, timeout);
				} else {
					int totalCount = Integer.parseInt(coachCourseVO
							.getCoursestudentcount());
					int selectCount = Integer.parseInt(coachCourseVO
							.getSelectedstudentcount());
					int sub = totalCount - selectCount;
					countTv.setText("剩余" + sub + "个名额");
					setOver(sub == 0 ? true : false, other);
				}

			} catch (Exception e) {
				countTv.setText("剩余0个名额");
				setOver(true, other);
				e.printStackTrace();
			}
		}

	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.appointmentcar_time_layout, null);

		startTimeTv = (TextView) view
				.findViewById(R.id.appointment_car_starttime_tv);
		endTimeTv = (TextView) view
				.findViewById(R.id.appointment_car_endtime_tv);
		countTv = (TextView) view.findViewById(R.id.appointment_car_count_tv);
		endTv = (TextView) view.findViewById(R.id.appointment_car_end_tv);
		ck = (CheckBox) view.findViewById(R.id.appointment_car_ck);

		ck.setOnCheckedChangeListener(this);

		addView(view);
	}

	private void setTextColor(int style, int reason) {
		if (style == over) {
			if (reason == has) {
				startTimeTv.setTextColor(Color.parseColor("#333333"));
				endTimeTv.setTextColor(Color.parseColor("#333333"));
				endTv.setTextColor(Color.parseColor("#333333"));
				countTv.setTextColor(getResources().getColor(
						R.color.app_main_color));
			} else {
				startTimeTv.setTextColor(Color.parseColor("#cccccc"));
				endTimeTv.setTextColor(Color.parseColor("#cccccc"));
				countTv.setTextColor(Color.parseColor("#cccccc"));
				endTv.setTextColor(Color.parseColor("#cccccc"));
			}
		} else if (style == selected) {
			startTimeTv.setTextColor(getResources().getColorStateList(
					R.color.app_main_color));
			endTimeTv.setTextColor(getResources().getColorStateList(
					R.color.app_main_color));
			endTv.setTextColor(getResources().getColorStateList(
					R.color.app_main_color));
			countTv.setTextColor(getResources().getColorStateList(
					R.color.app_main_color));
		} else if (style == noSelected) {
			startTimeTv.setTextColor(Color.parseColor("#333333"));
			endTimeTv.setTextColor(Color.parseColor("#333333"));
			endTv.setTextColor(Color.parseColor("#333333"));
			countTv.setTextColor(Color.parseColor("#999999"));
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			setTextColor(selected, other);
		} else {
			setTextColor(noSelected, other);
		}
		selectedListener.onTimeLayoutSelectedChange(this, coachCourseVO,
				isChecked);
	}

	public void setCheckBoxState(boolean isChecked) {
		ck.setChecked(isChecked);
		setTextColor(noSelected, other);
	}
}
