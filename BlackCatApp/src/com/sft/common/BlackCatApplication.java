package com.sft.common;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.loopj.android.http.AsyncHttpClient;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.sft.api.ApiHttpClient;
import com.sft.blackcatapp.CropImageActivity;
import com.sft.blackcatapp.NewCropImageActivity;
import com.sft.library.DemoHXSDKHelper;
import com.sft.util.LogUtil;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.QuestionVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.UserVO;
import com.sft.vo.VersionVO;
import com.umeng.socialize.PlatformConfig;

//@SuppressLint("SimpleDateFormat")
public class BlackCatApplication extends Application {

	public static BlackCatApplication app;
	public UploadManager uploadManager;
	// 登录的用户信息
	public UserVO userVO;
	// 七牛token
	public String qiniuToken;
	public QuestionVO questionVO;
	public VersionVO versionVO;
	public String latitude = "39.542415";
	public String longtitude = "116.232929";
	// 用户当前所在城市
	public String curCity;
	// 我喜欢的教练
	public List<CoachVO> favouriteCoach;
	// 我喜欢的驾校
	public List<SchoolVO> favouriteSchool;

	public boolean isLogin = false;
	public boolean isEnrollAgain = false;
	// 科目二内容
	public List<String> subjectTwoContent;
	// 科目三内容
	public List<String> subjectThreeContent;
	// 用户报名选择的驾校
	public SchoolVO selectEnrollSchool;
	// 用户报名选择的教练
	public CoachVO selectEnrollCoach;
	// 用户报名选择的车型
	public CarModelVO selectEnrollCarStyle;
	// 用户报名选择的班级
	public ClassVO selectEnrollClass;
	// 我的豆币
	public String currency;
	// 我的兑换劵
	public int coupons;
	// 我的Y码
	public String fcode;
	// 我的现金
	public String money;

	public static BlackCatApplication getInstance() {
		return app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.print("jpush---init");
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		SDKInitializer.initialize(this);

		uploadManager = new UploadManager();
		app = this;

		initHttpClient();
		DemoHXSDKHelper.getInstance().onInit(this);
	}

	private void initHttpClient() {
		// 初始化网络请求
		AsyncHttpClient client = new AsyncHttpClient();
		ApiHttpClient.setHttpClient(client);

	}

	protected String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
		Iterator<RunningAppProcessInfo> i = l.iterator();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	public void uploadPic(File file, String s) {

		uploadManager.put(file, s, qiniuToken, new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo info, JSONObject res) {
				sendBroadcast(new Intent(CropImageActivity.class.getName())
						.putExtra("info", info.toString()).putExtra("res",
								res == null ? "" : res.toString()));
			}
		}, new UploadOptions(null, null, false, new UpProgressHandler() {
			@Override
			public void progress(String key, double percent) {
			}
		}, new UpCancellationSignal() {
			@Override
			public boolean isCancelled() {
				// return true 停止上传
				return false;
			}
		}));
	}

	public void uploadPicTou(File file, String s) {

		uploadManager.put(file, s, qiniuToken, new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo info, JSONObject res) {
				sendBroadcast(new Intent(NewCropImageActivity.class.getName())
						.putExtra("info", info.toString()).putExtra("res",
								res == null ? "" : res.toString()));
			}
		}, new UploadOptions(null, null, false, new UpProgressHandler() {
			@Override
			public void progress(String key, double percent) {
			}
		}, new UpCancellationSignal() {
			@Override
			public boolean isCancelled() {
				// return true 停止上传
				return false;
			}
		}));
	}

	{
		// 微信 appid appsecret
		PlatformConfig.setWeixin("wxb815a53dcb2faf06",
				"2637931343bdd1d1991fcef1b28a187a");
		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("1218454084",
				"2bc6f9bf921f866bc1045c12f3fbebf8");
		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone("1105156397", "CcGth5JpjJmfiLRG");

	}

}
