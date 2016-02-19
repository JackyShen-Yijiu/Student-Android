package com.sft.listener;


public abstract class OnDateClickListener {
	public static OnDateClickListener instance;

	public abstract void onDateClick(int day, boolean clickbale);
}
