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
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.jzjf.app.R;
import com.sft.api.UserLogin;
import com.sft.common.Config;
import com.sft.listener.EMLoginListener;
import com.sft.util.CommonUtil;
import com.sft.util.DownLoadService;
import com.sft.util.JSONUtil;
import com.sft.viewutil.EditTextUtils;
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
public class NewLoginActivity extends BaseActivity implements EMLoginListener {
	// 获取验证码间隔时间(秒)
	private final static int codeTime = 60;
	private MyHandler codeHandler;
	// 登录按钮
	private Button loginBtn;
	// 随便看看按钮
	private TextView lookAroundBtn;
	// 手机号输入框
	private EditText phontEt;
	// 密码输入框
	private EditText passwordEt;

	private ImageView delet_iv;
	// 发送验证码
	private Button sendCodeBtn;
	private TextView login_register_tv;

	private final static String obtainCode = "obtainCode";
	// 验证验证码
	private final static String smscode = "smscode";

	// 用户登录
	private final static String login = "login";
	private static final String version = "version";
	private static final String qiniutoken = "qiniutoken";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		setContentView(R.layout.new_activity_login);
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
		lookAroundBtn = (TextView) findViewById(R.id.login_lookaround_btn);
		phontEt = (EditText) findViewById(R.id.login_phone_et);
		sendCodeBtn = (Button) findViewById(R.id.register_code_btn);
		passwordEt = (EditText) findViewById(R.id.login_passwd_et);
		login_register_tv = (TextView) findViewById(R.id.login_register_tv);
		// phontEt.setHint(setHint(R.string.phonenumber));
		// passwordEt.setHint(setHint(R.string.password));

		delet_iv = (ImageView) findViewById(R.id.delet_iv);
		app.isLogin = false;

		EditTextUtils.setEditTextHint(phontEt, getString(R.string.login_phone),
				14);
		EditTextUtils.setEditTextHint(passwordEt,
				getString(R.string.login_code), 14);

	}

	private void setListener() {
		loginBtn.setOnClickListener(this);
		passwordEt.setOnClickListener(this);
		phontEt.setOnClickListener(this);
		sendCodeBtn.setOnClickListener(this);
		login_register_tv.setOnClickListener(this);
		lookAroundBtn.setOnClickListener(this);
		delet_iv.setOnClickListener(this);
	}

	boolean isClick = true;

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.delet_iv:
			phontEt.setText("");
			break;
		case R.id.login_login_btn:
			login();
			break;
		case R.id.login_lookaround_btn:
			finish();// 随便看看
			intent = new Intent(this, MainActivity.class);
			break;
		case R.id.register_code_btn:
			obtainCode();
			break;

		case R.id.login_register_tv:
			Intent i = new Intent(this, LoginActivity.class);
			startActivityForResult(i, v.getId());
			break;

		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	private void obtainCode() {
		String phone = phontEt.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			if (CommonUtil.isMobile(phone)) {
				HttpSendUtils.httpGetSend("obtainCode", this, Config.IP
						+ "api/v1/code/" + phone);
			} else {
				ZProgressHUD.getInstance(NewLoginActivity.this).show();
				ZProgressHUD.getInstance(NewLoginActivity.this)
						.dismissWithFailure("请输入正确的手机号");
			}
		} else {
			ZProgressHUD.getInstance(NewLoginActivity.this).show();
			ZProgressHUD.getInstance(NewLoginActivity.this).dismissWithFailure(
					"请输入手机号");
		}
	}

	private void login() {
		loginBtn.setEnabled(true);
		lookAroundBtn.setEnabled(false);
		String checkResult = checkLoginInfo();
		if (checkResult == null) {
			// ZProgressHUD.getInstance(this).setMessage("正在登录...");
			// ZProgressHUD.getInstance(this).show();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("mobile", phontEt.getText().toString());
			paramMap.put("usertype", "1");
			// paramMap.put("password", app.userVO.getPassword());
			paramMap.put("smscode", passwordEt.getText().toString());
			HttpSendUtils.httpPostSend(login, this, Config.IP
					+ "api/v1/userinfo/studentloginbycode", paramMap);
		} else {
			loginBtn.setEnabled(true);
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
			checkLoginInfo();
		}
	}

	private String checkLoginInfo() {
		String mobile = phontEt.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			return "用户名为空";
		}
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			return "验证码为空";
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
		ZProgressHUD.getInstance(this).dismiss();
	}

	@Override
	public void doException(String type, Exception e, int code) {
		super.doException(type, e, code);
		loginBtn.setEnabled(true);
		lookAroundBtn.setEnabled(true);
		ZProgressHUD.getInstance(this).dismiss();
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			// if (type.equals(login)) {
			// loginBtn.setEnabled(true);
			// lookAroundBtn.setEnabled(true);
			// }
			return true;
		}
		if (!type.equals(obtainCode)) {
			if (!ZProgressHUD.getInstance(this).isShowing()) {
				ZProgressHUD.getInstance(this).setMessage("正在登录...");
				ZProgressHUD.getInstance(this).show();
			}
		}
		if (type.equals(obtainCode)) {
			codeHandler = new MyHandler(true, 1000) {
				private int time = codeTime;

				@Override
				public void run() {
					if (time-- > 0) {
						sendCodeBtn.setText("剩余(" + time + "s)");
						sendCodeBtn
								.setBackgroundResource(R.drawable.button_rounded_corners_gray);
						;
						sendCodeBtn.setEnabled(false);
					} else {
						codeHandler.cancle();
						sendCodeBtn.setEnabled(true);
						sendCodeBtn.setText(R.string.more_send_auth_code);
						sendCodeBtn
								.setBackgroundResource(R.drawable.button_rounded_corners);
					}
				}
			};
		} else if (type.equals(login)) {
			try {

				if (data != null && result.equals("1")) {
					app.userVO = JSONUtil.toJavaBean(UserVO.class, data);
					app.isLogin = true;
					obtainVersionInfo();
					util.saveParam(Config.LAST_LOGIN_PASSWORD,
							app.userVO.getPassword());
					util.saveParam(Config.LAST_LOGIN_MESSAGE,
							jsonString.toString());
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
						app.userVO.getPassword(), app.userVO.getNickname());
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
			// util.saveParam(Config.LAST_LOGIN_PASSWORD, passwordEt.getText()
			// .toString());
			loginBtn.setEnabled(true);
			// lookAroundBtn.setEnabled(true);
			ZProgressHUD.getInstance(this).dismiss();
			if (isMyServiceRunning()) {
				ZProgressHUD.getInstance(this).dismiss();
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
					ZProgressHUD.getInstance(NewLoginActivity.this).show();
					ZProgressHUD.getInstance(NewLoginActivity.this)
							.dismissWithFailure("初始化聊天失败");
					Intent intent = new Intent(NewLoginActivity.this,
							MainActivity.class);
					startActivity(intent);
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
								startService(new Intent(NewLoginActivity.this,
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

	@Override
	protected void onPause() {
		exitAnimition();
		super.onPause();
	}

	private void exitAnimition() {
		overridePendingTransition(R.anim.alpha_in,
				R.anim.option_leave_from_bottom);
	}

	@Override
	protected void onDestroy() {
		if (codeHandler != null) {
			codeHandler.cancle();
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		if (requestCode == R.id.login_register_tv) {// 注册返回
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 点击空白处 收起键盘
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			// if (isShouldHideInput(v, ev)) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}

			// }
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域上下范围之内的
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

}
