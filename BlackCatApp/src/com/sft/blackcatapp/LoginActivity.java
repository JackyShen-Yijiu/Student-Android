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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.api.UserLogin;
import com.sft.common.Config;
import com.sft.listener.EMLoginListener;
import com.sft.util.DownLoadService;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.UserVO;
import com.sft.vo.VersionVO;
import com.umeng.analytics.AnalyticsConfig;

/**
 * 登录界面
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends BaseActivity implements EMLoginListener {

	// 登录按钮
	private Button loginBtn;
	// 随便看看按钮
	private Button lookAroundBtn;
	// 手机号输入框
	private EditText phontEt;
	// 密码输入框
	private EditText passwordEt;
	// 忘记密码
	private TextView forgetPassTv;
	// 注册账号
	private TextView registerAccountTv;

	// 用户登录
	private final static String login = "login";
	private static final String version = "version";
	private static final String qiniutoken = "qiniutoken";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_login);
		initView();
		setListener();
		String lastLoginPhone = util.readParam(Config.LAST_LOGIN_ACCOUNT);
		if (!TextUtils.isEmpty(lastLoginPhone)) {
			phontEt.setText(lastLoginPhone);
		}

		app.userVO = null;
		app.isLogin = false;

		AnalyticsConfig.setAppkey(this, Config.UMENG_APPKEY);
		AnalyticsConfig.setChannel(Config.UMENG_CHANNELID);
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
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		setTitleBarVisible(View.GONE);

		loginBtn = (Button) findViewById(R.id.login_login_btn);
		lookAroundBtn = (Button) findViewById(R.id.login_lookaround_btn);
		phontEt = (EditText) findViewById(R.id.login_phone_et);
		passwordEt = (EditText) findViewById(R.id.login_passwd_et);
		forgetPassTv = (TextView) findViewById(R.id.login_forget_tv);
		registerAccountTv = (TextView) findViewById(R.id.login_register_tv);

		phontEt.setHint(setHint(R.string.phonenumber));
		passwordEt.setHint(setHint(R.string.password));

		app.isLogin = false;
	}

	private void setListener() {
		loginBtn.setOnClickListener(this);
		lookAroundBtn.setOnClickListener(this);
		forgetPassTv.setOnClickListener(this);
		registerAccountTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.login_login_btn:
			login();
			break;
		case R.id.login_lookaround_btn:
			finish();
			intent = new Intent(this, MainActivity.class);
			break;
		case R.id.login_forget_tv:
			intent = new Intent(this, FindPasswordActivity.class);
			String phone = phontEt.getText().toString();
			if (!TextUtils.isEmpty(phone)) {
				intent.putExtra("phone", phone);
			}
			break;
		case R.id.login_register_tv:
			intent = new Intent(this, RegisterActivity.class);
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void login() {
		loginBtn.setEnabled(false);
		lookAroundBtn.setEnabled(false);
		String checkResult = checkLoginInfo();
		if (checkResult == null) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("mobile", phontEt.getText().toString());
			paramMap.put("usertype", "1");
			paramMap.put("password", util.MD5(passwordEt.getText().toString()));
			HttpSendUtils.httpPostSend(login, this, Config.IP
					+ "api/v1/userinfo/userlogin", paramMap);
		} else {
			loginBtn.setEnabled(true);
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
	}

	private String checkLoginInfo() {
		String mobile = phontEt.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			return "用户名为空";
		}
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			return "密码为空";
		}
		return null;
	}

	@Override
	public void doTimeOut(String type) {
		super.doTimeOut(type);
		if (type.equals(login)) {
			loginBtn.setEnabled(true);
			lookAroundBtn.setEnabled(true);
		}
	}

	@Override
	public void doException(String type, Exception e, int code) {
		super.doException(type, e, code);
		loginBtn.setEnabled(true);
		lookAroundBtn.setEnabled(true);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			if (type.equals(login)) {
				loginBtn.setEnabled(true);
				lookAroundBtn.setEnabled(true);
			}
			return true;
		}

		if (type.equals(login)) {
			try {
				loginBtn.setEnabled(true);
				lookAroundBtn.setEnabled(true);
				if (data != null) {
					app.userVO = JSONUtil.toJavaBean(UserVO.class, data);
					obtainVersionInfo();
				} else {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure("数据格式错误");
				}
			} catch (Exception e) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("用户数据解析错误");
				e.printStackTrace();
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
			}
		} else if (type.equals(qiniutoken)) {
			if (dataString != null) {
				app.qiniuToken = dataString;
				new UserLogin(this).userLogin(app.userVO.getUserid(),
						util.MD5(passwordEt.getText().toString()),
						app.userVO.getNickname());
			}
		}
		return true;
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
			util.saveParam(Config.LAST_LOGIN_PHONE, app.userVO.getTelephone());
			util.saveParam(Config.LAST_LOGIN_ACCOUNT, phontEt.getText()
					.toString());
			util.saveParam(Config.LAST_LOGIN_PASSWORD, passwordEt.getText()
					.toString());

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
					ZProgressHUD.getInstance(LoginActivity.this).show();
					ZProgressHUD.getInstance(LoginActivity.this)
							.dismissWithFailure("初始化聊天失败");
				}
			});
		}
	}

	private void showDialog(final Context context) {

		// 是否需要更新
		String curVersion = util.getAppVersion().replace("v", "")
				.replace("V", "").replace(".", "");
		String newVersion = app.versionVO.getVersionCode().replace("v", "")
				.replace("V", "").replace(".", "");

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
								startService(new Intent(LoginActivity.this,
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
