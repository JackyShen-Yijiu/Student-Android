package com.sft.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.sft.listener.OnDateClickListener;

public class WeekViewPager extends ViewPager {

	public WeekViewPager(Context context) {
		super(context);
	}

	public WeekViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WeekViewPager setOnDateClickListener(
			OnDateClickListener onDateClickListener) {
		OnDateClickListener.instance = onDateClickListener;
		return this;
	}
}
