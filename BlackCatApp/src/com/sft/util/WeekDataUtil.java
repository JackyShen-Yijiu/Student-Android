package com.sft.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekDataUtil {

	public static List<Integer> getCurrentWeek() {
		List<Integer> thisWeek = new ArrayList<Integer>();
		Calendar calendar = Calendar.getInstance();
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		for (int i = weekDay; i >= 1; i--) {
			thisWeek.add(0, calendar.get(Calendar.DAY_OF_MONTH));
			calendar.add(Calendar.DATE, -1);
		}
		calendar.setTime(new Date());
		for (int i = weekDay; i < 7; i++) {
			calendar.add(Calendar.DATE, 1);
			thisWeek.add(thisWeek.size(), calendar.get(Calendar.DAY_OF_MONTH));
		}
		return thisWeek;
	}

	public static List<Integer> getNextWeek() {
		List<Integer> nextWeek = new ArrayList<Integer>();
		Calendar calendar = Calendar.getInstance();

		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, 7 - weekDay + 1);
		for (int i = 0; i < 7; i++) {
			nextWeek.add(calendar.get(Calendar.DAY_OF_MONTH));
			calendar.add(Calendar.DATE, 1);
		}

		return nextWeek;
	}
}
