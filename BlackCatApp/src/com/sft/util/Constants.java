package com.sft.util;

public class Constants {
	public static final float IMAGE_CACHE_HEAP_PERCENTAGE = 1f / 8f;

	/**
	 * SD卡缓存目录
	 */
	public static final String SD_APP_DIR = BaseUtils.getAppSdRootPath();
	public static final String SD_ROOT_PATH = "CarCoach";
	public static final String SD_FILE_PATH = "file";

	// public static final boolean DEBUG = BuildConfig.DEBUG;
	public static final boolean DEBUG = true;

	public static String DEVICE_TOKEN = "";

	public static final String FROM = "from";
	public static final String DETAIL = "detail";
	public static final String DATA = "data";
	public static final String ID = "id";

	public final static int USR_TYPE_COACH = 2;

	public final static int LIST_LEN = 20;

	public final static int ONCE_24H = 24;

	public final static int HANDLE_TYPE_CANCEL = 4;
}
