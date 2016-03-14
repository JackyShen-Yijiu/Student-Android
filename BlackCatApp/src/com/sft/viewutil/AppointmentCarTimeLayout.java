package com.sft.viewutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.util.CommonUtil;
import com.sft.vo.CoachCourseV2VO;

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
	private CoachCourseV2VO coachCourseVO;
	private ImageView labelIv;
	private RelativeLayout backgroundRl;
	private Context mContext;

	public interface TimeLayoutSelectedChangeListener {
		public void onTimeLayoutSelectedChange(AppointmentCarTimeLayout layout,
				CoachCourseV2VO coachCourseVO, boolean selected);
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

	public void setVaule(CoachCourseV2VO coachCourseVO) {
		this.coachCourseVO = coachCourseVO;
		if (coachCourseVO == null) {
			ck.setOnCheckedChangeListener(null);
			ck.setChecked(false);
			ck.setOnCheckedChangeListener(this);
			startTimeTv.setText("暂无");
			endTimeTv.setText("暂无");
			countTv.setText("剩余0个名额");
			setOver(true, other);
			return;
		}
		String beginTime = coachCourseVO.getBegintime();
		beginTime = beginTime.substring(0, beginTime.lastIndexOf(":"));
		startTimeTv.setText(beginTime);
		String endTime = coachCourseVO.getEndtime();
		endTime = endTime.substring(0, endTime.lastIndexOf(":"));
		endTimeTv.setText(endTime);

		// if (BlackCatApplication.getInstance().userVO == null) {
		// return;
		// }

		// 休息
		if (coachCourseVO.getIs_rest() == 0) {
			labelIv.setBackgroundResource(R.drawable.appointment_car_rest);

			// 是否有其他教练可约
			if (coachCourseVO.getCoachcount() > 0) {
				countTv.setText("有" + coachCourseVO.getCoachcount() + "个教练可预约");
				countTv.setTextColor(Color.parseColor("#5B8EFB"));
			} else {
				backgroundRl.setBackgroundColor(Color.parseColor("#efefef"));
				ck.setEnabled(false);
			}
		}
		// 已过时
		if (coachCourseVO.getIs_outofdate() == 0) {
			backgroundRl.setBackgroundColor(Color.parseColor("#efefef"));
			ck.setEnabled(false);
		}
		// 已预约
		if (coachCourseVO.getIs_reservation() == 1) {
			ck.setEnabled(false);
			if (coachCourseVO.getReservationcoachname().equals(
					coachCourseVO.getCoursedata().getCoachname())) {
				// 该教练
				countTv.setText("已约该教练");
				countTv.setTextColor(CommonUtil.getColor(mContext,
						R.color.new_app_main_color));
				labelIv.setBackgroundResource(R.drawable.appointment_car_reservation_this);
			} else {
				countTv.setText("已约" + coachCourseVO.getReservationcoachname()
						+ "教练");
				countTv.setTextColor(Color.parseColor("#5B8EFB"));
				labelIv.setBackgroundResource(R.drawable.appointment_car_reservation_other);
			}
		} else {
			if (coachCourseVO.getIs_outofdate() == 1
					&& coachCourseVO.getIs_rest() == 1
					&& coachCourseVO.getCoursedata() != null) {
				// 该教练已约满
				if (coachCourseVO.getCoursedata().getCoursestudentcount() == coachCourseVO
						.getCoursedata().getSelectedstudentcount()) {
					labelIv.setBackgroundResource(R.drawable.appointment_car_full);
					// 是否有其他教练可约
					if (coachCourseVO.getCoachcount() > 0) {
						countTv.setText("有" + coachCourseVO.getCoachcount()
								+ "个教练可预约");
						countTv.setTextColor(Color.parseColor("#5B8EFB"));
					} else {

						backgroundRl.setBackgroundColor(Color
								.parseColor("#efefef"));
						ck.setEnabled(false);
					}
				} else {
					countTv.setText("剩余"
							+ (coachCourseVO.getCoursedata()
									.getCoursestudentcount() - coachCourseVO
									.getCoursedata().getSelectedstudentcount())
							+ "个名额");
				}
			}
		}

		// if (Arrays.asList(coachCourseVO.getCoursedata().getCourseuser())
		// .contains(BlackCatApplication.getInstance().userVO.getUserid()))
		// {
		// // 此预约是否已经预约
		// countTv.setText("您已预约");
		// setOver(true, has);
		// } else {
		// try {
		// if (beginTime.length() == 4)
		// beginTime = "0" + beginTime;
		// Date date = new Date();
		// SimpleDateFormat format = new SimpleDateFormat(
		// "yyyy-MM-dd HH:mm");
		// long courseTime = format.parse(
		// UTC2LOC.instance.getDate(
		// coachCourseVO.getCoursebegintime(),
		// "yyyy-MM-dd") + " " + beginTime).getTime();
		// long curTime = date.getTime();
		// if (curTime > courseTime) {
		// // 此预约是否已过当前时间
		// countTv.setText("已过时");
		// setOver(true, timeout);
		// } else {
		// int totalCount = Integer.parseInt(coachCourseVO
		// .getCoursestudentcount());
		// int selectCount = Integer.parseInt(coachCourseVO
		// .getSelectedstudentcount());
		// int sub = totalCount - selectCount;
		// countTv.setText("剩余" + sub + "个名额");
		// setOver(sub == 0 ? true : false, other);
		// }
		//
		// } catch (Exception e) {
		// // 没有数据的时候 课时列表显示
		// startTimeTv.setText("暂无");
		// endTimeTv.setText("暂无(请联系其他教练)");
		// countTv.setText("剩余0个名额");
		// setOver(true, other);
		// e.printStackTrace();
		// }

	}

	private void init(Context context) {
		this.mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.appointmentcar_time_layout, null);

		startTimeTv = (TextView) view
				.findViewById(R.id.appointment_car_starttime_tv);
		endTimeTv = (TextView) view
				.findViewById(R.id.appointment_car_endtime_tv);
		countTv = (TextView) view.findViewById(R.id.appointment_car_count_tv);
		countTv.setText("");
		endTv = (TextView) view.findViewById(R.id.appointment_car_end_tv);
		ck = (CheckBox) view.findViewById(R.id.appointment_car_ck);

		labelIv = (ImageView) view.findViewById(R.id.appointment_car_label_iv);
		labelIv.setBackgroundDrawable(null);
		backgroundRl = (RelativeLayout) view
				.findViewById(R.id.appointment_car_bg_rl);
		backgroundRl.setBackgroundColor(Color.WHITE);
		ck.setOnCheckedChangeListener(this);

		addView(view);
	}

	private void setTextColor(int style, int reason) {
		if (style == over) {
			if (reason == has) {
				startTimeTv.setTextColor(getResources().getColorStateList(
						R.color.new_app_main_color));
				endTimeTv.setTextColor(getResources().getColorStateList(
						R.color.new_app_main_color));
				endTv.setTextColor(getResources().getColorStateList(
						R.color.new_app_main_color));
				countTv.setTextColor(getResources().getColor(
						R.color.new_app_main_color));
			} else {
				startTimeTv.setTextColor(Color.parseColor("#cccccc"));
				endTimeTv.setTextColor(Color.parseColor("#cccccc"));
				countTv.setTextColor(Color.parseColor("#cccccc"));
				endTv.setTextColor(Color.parseColor("#cccccc"));
			}
		} else if (style == selected) {
			startTimeTv.setTextColor(getResources().getColorStateList(
					R.color.white));
			endTimeTv.setTextColor(getResources().getColorStateList(
					R.color.white));
			endTv.setTextColor(getResources().getColorStateList(R.color.white));
			countTv.setTextColor(getResources()
					.getColorStateList(R.color.white));
			backgroundRl.setBackgroundColor(CommonUtil.getColor(mContext,
					R.color.new_app_main_color));
		} else if (style == noSelected) {
			startTimeTv.setTextColor(getResources().getColorStateList(
					R.color.new_text_color_dark));
			endTimeTv.setTextColor(getResources().getColorStateList(
					R.color.new_text_color_light));
			endTv.setTextColor(getResources().getColorStateList(
					R.color.new_text_color_light));
			countTv.setTextColor(getResources().getColorStateList(
					R.color.new_text_color_light));
			backgroundRl.setBackgroundColor(CommonUtil.getColor(mContext,
					R.color.white));
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
