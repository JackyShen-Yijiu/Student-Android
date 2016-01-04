package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.api.UserLogin;
import com.sft.common.Config;
import com.sft.listener.EMLoginListener;
import com.sft.util.DownLoadService;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.SharedPreferencesUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.UserVO;
import com.sft.vo.VersionVO;
import com.umeng.analytics.AnalyticsConfig;

public class WelcomeActivity extends BaseActivity implements EMLoginListener {

	private final static String login = "login";
	private static final String version = "version";
	private static final String qiniutoken = "qiniutoken";

	public static String IS_APP_FIRST_OPEN = "is_app_first_open";
	private MyHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initView();
		initData();
	}

	private void initView() {
		// ImageView image = (ImageView) findViewById(R.id.welcome_image);
		// RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
		// image
		// .getLayoutParams();
		// params.width = (int) (screenWidth * 5 / 12f);
		// params.height = (int) (params.width * 243 / 509f);
		//
		// ImageView devider = (ImageView) findViewById(R.id.welcom_devider);
		// RelativeLayout.LayoutParams deviderparams =
		// (RelativeLayout.LayoutParams) devider
		// .getLayoutParams();
		// deviderparams.height = (int) (screenHeight * 41 / 128f);
	}

	private void initData() {

		app.userVO = null;
		app.isLogin = false;

		boolean isFirstOpen = SharedPreferencesUtil.getBoolean(
				getApplicationContext(), IS_APP_FIRST_OPEN, true);
		if (isFirstOpen) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this,
							GuideActivity.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
				}
			}, 1000);
			// startActivity(new Intent(WelcomeActivity.this,
			// GuideActivity.class));
		} else {

			String lastLoginPhone = util.readParam(Config.LAST_LOGIN_ACCOUNT);
			String password = util.readParam(Config.LAST_LOGIN_PASSWORD);
			if (!TextUtils.isEmpty(lastLoginPhone)
					&& !TextUtils.isEmpty(password)) {
				AnalyticsConfig.setAppkey(this, Config.UMENG_APPKEY);
				AnalyticsConfig.setChannel(Config.UMENG_CHANNELID);
				login(lastLoginPhone, password);
			} else {
				handler = new MyHandler(2000) {
					@Override
					public void run() {
						Intent intent = new Intent(WelcomeActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					}
				};
			}
		}

	}

	private void login(String phone, String password) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", phone);
		paramMap.put("usertype", "1");
		paramMap.put("password", util.MD5(password));
		HttpSendUtils.httpPostSend(login, this, Config.IP
				+ "api/v1/userinfo/userlogin", paramMap);
	}

	private void obtainVersionInfo() {
		HttpSendUtils.httpGetSend(version, this, Config.IP
				+ "api/v1/appversion/1");
	}

	private void obtainQiNiuToken() {
		HttpSendUtils.httpGetSend(qiniutoken, this, Config.IP
				+ "api/v1/info/qiniuuptoken");
	}

	@Override
	public void doTimeOut(String type) {
		super.doTimeOut(type);
		Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void doException(String type, Exception e, int code) {
		super.doException(type, e, code);
		new MyHandler(2000) {
			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		};
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (handler != null) {
			handler.cancle();
		}
		if (type.equals(login)) {
			try {
				if (data != null) {
					app.userVO = JSONUtil.toJavaBean(UserVO.class, data);
					util.saveParam(Config.LAST_LOGIN_PHONE,
							app.userVO.getTelephone());
					obtainVersionInfo();
				} else {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure("数据格式错误");
					new MyHandler(1000) {
						@Override
						public void run() {
							finish();
						}
					};
				}
			} catch (Exception e) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("用户数据解析错误");
				e.printStackTrace();
				new MyHandler(1000) {
					@Override
					public void run() {
						finish();
					}
				};
			}
		} else if (type.equals(version)) {
			try {
				VersionVO versionVO = JSONUtil
						.toJavaBean(VersionVO.class, data);
				app.versionVO = versionVO;
				obtainQiNiuToken();
			} catch (Exception e) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("版本数据解析错误");
				e.printStackTrace();
				new MyHandler(1000) {
					@Override
					public void run() {
						finish();
					}
				};
			}
		} else if (type.equals(qiniutoken)) {
			if (dataString != null) {
				app.qiniuToken = dataString;

				new UserLogin(this).userLogin(app.userVO.getUserid(),
						util.MD5(util.readParam(Config.LAST_LOGIN_PASSWORD)),
						app.userVO.getNickname());
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private boolean isMyServiceRunning() {
		util.print(DownLoadService.class.getName());
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (DownLoadService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void loginResult(boolean result, int code, String message) {
		if (result) {
			if (isMyServiceRunning()) {
				app.isLogin = true;
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				showDialog(this);
			}
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ZProgressHUD.getInstance(WelcomeActivity.this).show();
					ZProgressHUD.getInstance(WelcomeActivity.this)
							.dismissWithFailure("初始化聊天失败");
				}
			});
			new MyHandler(1000) {
				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}
			};
		}
	}

	private void showDialog(final Context context) {

		// 是否需要更新
		String curVersion = util.getAppVersion().replace("v", "")
				.replace("V", "").replace(".", "");
		String newVersion = app.versionVO.getVersionCode().replace("v", "")
				.replace("V", "").replace(".", "");

		LogUtil.print("new:::" + newVersion);
		LogUtil.print("old:::" + curVersion);
		try {
			if (Integer.parseInt(newVersion) > Integer.parseInt(curVersion)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("发现新版本");
				builder.setMessage(getString(R.string.app_name) + "有新版本啦！");
				builder.setPositiveButton("立即更新",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								startService(new Intent(WelcomeActivity.this,
										DownLoadService.class).putExtra("url",
										app.versionVO.getDownloadUrl()));
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("以后再说",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});
				Dialog dialog = builder.create();
				dialog.show();
				dialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						app.isLogin = true;
						Intent intent = new Intent(context, MainActivity.class);
						startActivity(intent);
						finish();
					}
				});
			} else {
				app.isLogin = true;
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
			app.isLogin = true;
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}

}
