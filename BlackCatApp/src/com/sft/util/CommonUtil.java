package com.sft.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class CommonUtil {

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取颜色
	 */
	public static int getColor(Context context, int colorId) {
		return context.getResources().getColor(colorId);
	}

	/**
	 * 获取字符串
	 */
	public static String getString(Context context, int stringId) {
		return context.getResources().getString(stringId);
	}
}
