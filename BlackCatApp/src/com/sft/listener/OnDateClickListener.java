package com.sft.listener;

import com.sft.vo.AppointmentDay;

public abstract class OnDateClickListener {
	public static OnDateClickListener instance;

	public abstract void onDateClick(AppointmentDay day, boolean clickbale);
}
