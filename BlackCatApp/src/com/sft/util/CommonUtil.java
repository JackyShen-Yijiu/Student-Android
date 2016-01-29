package com.sft.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern
				.compile("^((13[0-9])|(14[5,7])|(15[0-9])|(17[0,6-8])|(18[0-9]))\\d{8}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 姓名验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isRightName(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^(([\u4e00-\u9fa5]{2,6})|([a-zA-Z]{2,6}))$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 判断是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	public static boolean isIdCardOk(String input){
		if(input==null){//
			return false;
		}
	
		 //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）  
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");  
        //通过Pattern获得Matcher  
        Matcher idNumMatcher = idNumPattern.matcher(input);  
        //判断用户输入是否为身份证号  
        if(idNumMatcher.matches()){
        	return true;
        }
		return false;
	}
	
}
