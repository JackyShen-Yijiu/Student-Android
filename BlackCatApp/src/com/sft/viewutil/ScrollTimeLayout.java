package com.sft.viewutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sft.dialog.CustomDialog;
import com.sft.util.LogUtil;
import com.sft.viewutil.AppointmentCarTimeLayout.TimeLayoutSelectedChangeListener;
import com.sft.vo.CoachCourseVO;

@SuppressLint("NewApi")
public class ScrollTimeLayout extends LinearLayout implements
		TimeLayoutSelectedChangeListener {

	private Context context;
	private int column = 3;
	private Comparator<Object> courseComp;
	// 用户选择时间段的课程
	private List<CoachCourseVO> selectCourseList = new ArrayList<CoachCourseVO>();
	private int screenWidth;
	private boolean isTimeBlockCon;

	public ScrollTimeLayout(Context context) {
		super(context);
		init(context);
	}

	public ScrollTimeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ScrollTimeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		setLayoutDirection(LinearLayout.VERTICAL);
		courseComp = new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				CoachCourseVO s1 = (CoachCourseVO) o1;
				CoachCourseVO s2 = (CoachCourseVO) o2;
				try {
					int c1 = Integer.parseInt(s1.getCoursetime().getTimeid());
					int c2 = Integer.parseInt(s2.getCoursetime().getTimeid());
					if (c1 < c2)
						return -1;
					else if (c1 == c2)
						return 0;
					else if (c1 > c2)
						return 1;
				} catch (Exception e) {

				}
				return 0;
			}
		};
	}

	public boolean isTimeBlockCon() {
		return isTimeBlockCon;
	}

	public List<CoachCourseVO> getSelectCourseList() {
		return selectCourseList;
	}

	public void setSelectCourseList(List<CoachCourseVO> selectCourseList) {
		this.selectCourseList = selectCourseList;
	}

	public void setColumn(int size) {
		this.column = size;
	}

	public void clearData() {
		removeAllViews();
		selectCourseList = new ArrayList<CoachCourseVO>();
	}

	public void setData(List<CoachCourseVO> list, float aspect) {

		if (list == null || list.size() == 0) {
			return;
		}

		Collections.sort(list, courseComp);
		// 最后一行的个数
		int lastConut = list.size() % column;
		// 行数
		int row = list.size() / column;
		if (lastConut > 0) {
			row++;
		}

		LinearLayout.LayoutParams vertialDeviderParams = new LayoutParams(1,
				LinearLayout.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams horitalDeviderParams = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 1);

		LinearLayout.LayoutParams timeParams = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth
						/ aspect / 3), 1);

		LinearLayout.LayoutParams rowParams = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		rowParams.setLayoutDirection(LinearLayout.HORIZONTAL);

		for (int i = 0; i < row - 1; i++) {
			// 此时每行有column个
			LinearLayout innerLayout = new LinearLayout(context);
			addView(innerLayout, rowParams);

			for (int j = 0; j < column; j++) {
				AppointmentCarTimeLayout timeLayout = new AppointmentCarTimeLayout(
						context);
				timeLayout.setSelectedChangeListener(this);
				// 参数
				timeLayout.setVaule(list.get(i * column + j));
				innerLayout.addView(timeLayout, timeParams);
				if (j < column - 1) {
					ImageView devider = new ImageView(context);
					devider.setBackgroundColor(Color.parseColor("#cccccc"));
					innerLayout.addView(devider, vertialDeviderParams);
				}
			}

			ImageView devider = new ImageView(context);
			devider.setBackgroundColor(Color.parseColor("#cccccc"));
			addView(devider, horitalDeviderParams);

		}

		LinearLayout innerLayout = new LinearLayout(context);
		addView(innerLayout, rowParams);

		lastConut = lastConut == 0 ? column : lastConut;
		for (int i = 0; i < lastConut; i++) {
			AppointmentCarTimeLayout timeLayout = new AppointmentCarTimeLayout(
					context);
			timeLayout.setSelectedChangeListener(this);
			// 参数
			timeLayout.setVaule(list.get((row - 1) * column + i));
			innerLayout.addView(timeLayout, timeParams);
			if (i != column - 1) {
				ImageView devider = new ImageView(context);
				devider.setBackgroundColor(Color.parseColor("#cccccc"));
				innerLayout.addView(devider, vertialDeviderParams);
			}
		}

		if (row == 1) {
			// 加横线
			ImageView devider = new ImageView(context);
			devider.setBackgroundColor(Color.parseColor("#cccccc"));
			addView(devider, horitalDeviderParams);
		}

	}

	@Override
	public void onTimeLayoutSelectedChange(AppointmentCarTimeLayout layout,
			CoachCourseVO coachCourseVO, boolean selected) {
		if (coachCourseVO == null) {
			return;
		}
		if (selected) {
			selectCourseList.add(coachCourseVO);
			LogUtil.print("===========select");
		} else {
			selectCourseList.remove(coachCourseVO);
			LogUtil.print("=====no======select");
		}

		isTimeBlockCon = checkTimeBlockCon();
		if (!isTimeBlockCon) {
			CustomDialog dialog = new CustomDialog(context,
					CustomDialog.APPOINTMENT_TIME_ERROR);
			dialog.show();
			// 选择不连续，给出提示后，就取消该项选择
			layout.setCheckBoxState(false);
			selectCourseList.remove(coachCourseVO);
		}
		onTimeLayoutSelectedListener.TimeLayoutSelectedListener(selected);
	}

	private boolean checkTimeBlockCon() {
		Collections.sort(selectCourseList, courseComp);
		int size = selectCourseList.size() - 1;
		for (int i = 0; i < size; i++) {
			if (Integer.parseInt(selectCourseList.get(i).getCoursetime()
					.getTimeid()) + 1 != Integer.parseInt(selectCourseList
					.get(i + 1).getCoursetime().getTimeid())) {
				return false;
			}
		}
		return true;
	}

	public interface OnTimeLayoutSelectedListener {
		void TimeLayoutSelectedListener(boolean selected);
	}

	private OnTimeLayoutSelectedListener onTimeLayoutSelectedListener;

	public void setOnTimeLayoutSelectedListener(
			OnTimeLayoutSelectedListener onTimeLayoutSelectedListener) {
		this.onTimeLayoutSelectedListener = onTimeLayoutSelectedListener;
	}
}
