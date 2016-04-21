package com.sft.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.sft.blackcatapp.NewLoginActivity;
import com.sft.common.BlackCatApplication;

public class BaseUtils {

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		LogUtil.print("count--->" + listAdapter.getCount());
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			int h = listItem.getMeasuredHeight();
			LogUtil.print(i + "item-->" + h);
			totalHeight += h;
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 随便看看
	 */
	public static boolean justSay(Activity a) {
		// if(app.)
		BlackCatApplication app = BlackCatApplication.getInstance();
		if (!app.isLogin) {
			Intent intent = new Intent(a, NewLoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			a.startActivity(intent);
			a.finish();
			return true;
		}
		return false;
	}

	/**
	 * 去登录页面
	 * 
	 * @param a
	 */
	public static void toLogin(Activity a) {
		Intent intent = new Intent(a, NewLoginActivity.class);
		a.startActivity(intent);
	}

	/**
	 * SD卡是否已经安装
	 * 
	 * @return
	 */
	public static boolean isMounted() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡根路径
	 * 
	 * @return
	 */
	public static String getAppSdRootPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + Constants.SD_ROOT_PATH + File.separator;
	}

	/**
	 * 获取MD5值
	 * 
	 * @param string
	 * @return
	 */
	public static String getMD5code(String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			byte b[] = md.digest();
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				int i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 转化时间
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatTime(long timestamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		return simpleDateFormat.format(timestamp);
	}

	/**
	 * 转化时间
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatTime2(long timestamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss",
				Locale.getDefault());
		return simpleDateFormat.format(timestamp);
	}

	/**
	 * 转化时间
	 * 
	 * @param timeString
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String formatTime(String timeString) {
		return formatTime(parseTime(timeString));
	}

	public static String formatToFormatTime(long timestamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy.MM.dd HH:mm:ss", Locale.getDefault());
		return simpleDateFormat.format(timestamp);
	}

	public static String formatToFormatTime2(long timestamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日",
				Locale.getDefault());
		return simpleDateFormat.format(timestamp);
	}

	public static String formatToFormatTimeForPinned(String timeString) {
		long timestamp = parseTime(timeString);
		if (timestamp == 0) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy.MM.dd  E", Locale.getDefault());
		return simpleDateFormat.format(timestamp);
	}

	/**
	 * 转化时间 "yyyy-MM-dd HH:mm:ss" 为时间戳
	 * 
	 * @param timeString
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static long parseTime(String timeString) {
		long timestamp = 0;
		if (TextUtils.isEmpty(timeString))
			return timestamp;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date date = simpleDateFormat.parse(timeString);
			timestamp = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	/**
	 * 判断是否在2G|3G状态下
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetTypeMobile(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager == null) {
			return false;
		}
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network == null) {
			return false;
		}
		int netType = network.getType();
		if (ConnectivityManager.TYPE_MOBILE == netType) {
			return true;
		}
		return false;
	}

	/**
	 * 返回网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetType(Context context) {
		int type = -1;
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager == null) {
			return type;
		}
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network == null) {
			return type;
		}
		return network.getType();
	}

	/**
	 * 是否连接WIFI网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context == null) {
			return false;
		}
		return ConnectivityManager.TYPE_WIFI == getNetType(context);
	}

	/**
	 * 判断是否连接wify
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifyConnected(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager == null) {
			return false;
		}
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network == null) {
			return false;
		}
		int netType = network.getType();
		if (ConnectivityManager.TYPE_WIFI == netType) {
			return true;
		}
		return false;
	}

	/**
	 * 复制File
	 * 
	 * @param sFile
	 *            源file
	 * @param dFile
	 *            目标file
	 * @return
	 */
	public static boolean copyFile(File sFile, File dFile) {
		if (sFile == null || dFile == null || !sFile.exists()) {
			return false;
		}
		if (!dFile.exists()) {
			dFile.mkdirs();
		}
		if (dFile.exists()) {
			dFile.delete();
		}
		try {
			dFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] buffer = new byte[1024];
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sFile);
			outputStream = new FileOutputStream(dFile);
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			buffer = null;
		}
		return false;
	}

	/**
	 * 获取String 详情
	 * 
	 * @param context
	 * @param newUrl
	 * @return
	 */
	public static String getStringResultByDir(Context context, String newUrl,
			String dirPath) {

		if (TextUtils.isEmpty(newUrl) || TextUtils.isEmpty(dirPath)) {
			return null;
		}
		String fileName = BaseUtils.getMD5code(newUrl);
		if (TextUtils.isEmpty(fileName)) {
			return null;
		}
		if (isMounted()) {
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dirPath, fileName);
			FileInputStream inputStream = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				inputStream = new FileInputStream(file);
				byte[] data = readStream(inputStream);
				if (data != null) {
					String result = new String(data);
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean saveStringResultByDir(Context context, String url,
			String result, String dirPath) {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(result)
				|| TextUtils.isEmpty(dirPath)) {
			return false;
		}
		String fileName = BaseUtils.getMD5code(url);
		if (TextUtils.isEmpty(fileName)) {
			return false;
		}
		if (isMounted()) {
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dirPath, fileName);
			FileOutputStream fileOutputStream = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(result.getBytes());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.flush();
						fileOutputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, boolean needRecycle) {
		if (bmp == null) {
			return null;
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 65, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String getVersionName(Context ctx) {
		String res = "";
		if (ctx == null)
			return res;
		PackageInfo packageInfo = null;
		try {
			packageInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			res = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static int getVersionCode(Context context) {// 获取版本号(内部识别号)

		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取屏幕密度
	 * 
	 * @param context
	 * @return
	 */
	public static float getDensity(Activity context) {
		// 获取屏幕密度
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
		return dm.density;
	}

	/**
	 * 读取流中的数据
	 * 
	 * @param inStream
	 */
	public static byte[] readStream(InputStream inStream) {
		if (inStream == null) {
			return null;
		}
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null) {
					outStream.flush();
					outStream.close();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		buffer = null;
		return outStream.toByteArray();
	}

	/**
	 * 收起软键盘并设置提示文字
	 */
	public static void closeSoftInputMethod(Activity activity, View view) {
		if (activity == null || view == null) {
			return;
		}
		try {
			((InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void toggleSoftInput(Activity activity, View view) {
		if (activity == null || view == null) {
			return;
		}
		try {
			((InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.toggleSoftInputFromWindow(view.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		if (context == null) {
			return null;
		}
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	public static void hideSoftKeyboard(Activity activity) {
		if (activity == null) {
			return;
		}
		InputMethodManager inputmgr = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null) {
			inputmgr.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	public static int getScreenWidth(Context context) {
		if (context == null) {
			return 0;
		}
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int w = displayMetrics.widthPixels;
		if (w <= 0) {
			w = 720;
		}
		return w;
	}

	public static int getScreenHeight(Context context) {
		if (context == null) {
			return 0;
		}
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int w = displayMetrics.heightPixels;
		if (w <= 0) {
			w = 1280;
		}
		return w;
	}

	/*
	 * 必须在子线程里执行
	 */
	// public static void cleanupDataWhenLogout() {
	// PrefUtils.clearValuesByKey(new String[]{
	// PrefConstants.KEY_LAST_REFRESH_TIME,
	// PrefConstants.KEY_WORK_LIST});
	// }

	public static void callSomebody(Activity act, String phonenum) {
		Intent intent = new Intent();
		// 系统默认的action，用来打开默认的电话界面
		intent.setAction(Intent.ACTION_DIAL);
		// 需要拨打的号码
		intent.setData(Uri.parse("tel:" + phonenum));
		try {
			act.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 给APP打分
	 */
	// public static void rateAppInMarket(Activity activity) {
	// if (activity == null) {
	// return;
	// }
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// intent.setData(Uri.parse("market://details?id="
	// + CarCoachApplication.getInstance().getPackageName()));
	// try {
	// activity.startActivity(intent);
	// activity.overridePendingTransition(0, 0);
	// } catch (ActivityNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static String getTokenID() {
	// TelephonyManager tm = (TelephonyManager) CarCoachApplication
	// .getInstance().getSystemService(Context.TELEPHONY_SERVICE);
	//
	// String imei = tm.getDeviceId();
	//
	// String AndroidID = android.provider.Settings.System.getString(
	// CarCoachApplication.getInstance().getContentResolver(),
	// "android_id");
	//
	// String serialNo = DeviceUtils.getDeviceSerial();
	//
	// return BaseUtils.getMD5code(imei + AndroidID + serialNo);
	// }

	/**
	 * 字符串进行转义，jpush推下来的数据，会对特殊字符进行转义，这里在解析前，需要还原数据
	 * 
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		String newStr = outBuffer.toString().replace("\"{", "{");
		newStr = newStr.replace("}\"", "}");
		return newStr;
	}

	static public class TimeOfDay {
		public int hourOfDay;
		public int minutes;
	}

	public static String getTime(TimeOfDay time) {
		Time tme = new Time(time.hourOfDay, time.minutes, 0);// seconds by
																// default set
																// to zero
		Format formatter;
		formatter = new SimpleDateFormat("a hh:mm");
		return formatter.format(tme);
		// }
		//
		// public static String getTime(SetTimeEvent time) {
		// Time tme = new Time(time.hourOfDay, time.minutes, 0);// seconds by
		// // default set
		// // to zero
		// Format formatter;
		// formatter = new SimpleDateFormat("a hh:mm");
		// return formatter.format(tme);
		// }
		//
		// public static String getDate(SetDateEvent event) {
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.YEAR, event.year);
		// cal.set(Calendar.DAY_OF_MONTH, event.day);
		// cal.set(Calendar.MONTH, event.month);
		// String format = new SimpleDateFormat("yyyy年MM月dd日").format(cal
		// .getTime());
		// return format;
		// }
		//
		// public static long getTimestamp(SetDateEvent event, SetTimeEvent
		// event2) {
		// if (event == null) {
		// return 0;
		// }
		// LogUtil.print("time-->"+event.year+"month--->"+event.month+"day-->"+event.day);
		// LogUtil.print("time--hour>"+event2.hourOfDay+"minutes--->"+event2.minutes);
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.YEAR, event.year);
		// cal.set(Calendar.DAY_OF_MONTH, event.day);
		// cal.set(Calendar.MONTH, event.month);
		// if (event2 != null) {
		// cal.set(Calendar.HOUR_OF_DAY, event2.hourOfDay);
		// cal.set(Calendar.MINUTE, event2.minutes);
		// }
		// return cal.getTimeInMillis();
	}
}
