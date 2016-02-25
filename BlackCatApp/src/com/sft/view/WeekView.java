package com.sft.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.sft.adapter.WeekAdapter;
import com.sft.util.WeekDataUtil;
import com.sft.vo.AppointmentDay;

public class WeekView extends GridView {

	private List<AppointmentDay> list;
	private WeekAdapter adapter;

	public WeekView(Context context) {
		super(context);
		this.setNumColumns(7);
		this.setCacheColorHint(0);
	}

	public WeekView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setNumColumns(7);
		this.setCacheColorHint(0);
	}

	public void initMonthAdapter(int pagePosition) {
		if (pagePosition == 0) {
			this.list = WeekDataUtil.getCurrentWeek();
		} else if (pagePosition == 1) {
			this.list = WeekDataUtil.getNextWeek();
		}
		adapter = new WeekAdapter(getContext(), 1, this.list);
		this.setAdapter(adapter);
	}
}
