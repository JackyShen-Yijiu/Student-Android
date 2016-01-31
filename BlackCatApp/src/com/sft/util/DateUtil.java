package com.sft.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pengdonghua on 2016/1/20.
 */
public class DateUtil {

	/**
	 * 
	 * @param time
	 *            2015-12-15T15:59:43.308Z
	 * @return 2015-12-15 15:59
	 */
	public static String parseTime(String time) {
		// 2015-12-15T15:59:43.308Z
		SimpleDateFormat f = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		if (null == time) {
			return time;
		}
		try {
			Date d = f.parse(time);
			return f1.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
			return time;
		}

	}

}
