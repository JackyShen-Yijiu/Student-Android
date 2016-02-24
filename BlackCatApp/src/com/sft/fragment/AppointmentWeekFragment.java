package com.sft.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.sft.common.BlackCatApplication;
import com.sft.view.WeekView;

public class AppointmentWeekFragment {

	public LinearLayout rootview;

	public AppointmentWeekFragment(int pagePosition) {
		rootview = new LinearLayout(BlackCatApplication.getInstance());
		rootview.setBackgroundColor(Color.parseColor("#eeeeee"));
		LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		rootview.setLayoutParams(params);
		WeekView weekView = new WeekView(BlackCatApplication.getInstance());
		weekView.initMonthAdapter(pagePosition);
		rootview.addView(weekView, params);
	}

}
