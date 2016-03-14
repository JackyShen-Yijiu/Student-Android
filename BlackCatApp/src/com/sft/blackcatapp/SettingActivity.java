package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.easemob.chat.EMChatManager;
import com.jzjf.app.R;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.util.BaseUtils;
import com.sft.util.CommonUtil;
import com.sft.util.DownLoadService;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.VersionVO;

/**
 * 设置界面
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private static final String changeTip = "changeTip";
	private CheckBox appointmentCk, messageCk;

	private TextView aboutUsTv, rateTv, callbackTv;
	private TextView helpTv;
	private Button logoutBtn;
	private TextView gradeTv;
	private TextView tv_code;
	private RelativeLayout setting_update;

	private static final String version = "version";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_setting);
		initView();
		setListener();
		obtainVersionInfo();
	}

	// tv_code.setText(app.versionVO.getVersionCode());

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.setting);

		appointmentCk = (CheckBox) findViewById(R.id.setting_appointment_ck);
		messageCk = (CheckBox) findViewById(R.id.setting_newmessage_ck);

		tv_code = (TextView) findViewById(R.id.tv_code);

		setting_update = (RelativeLayout) findViewById(R.id.setting_update);

		helpTv = (TextView) findViewById(R.id.setting_hlep);
		aboutUsTv = (TextView) findViewById(R.id.setting_aboutus_tv);
		callbackTv = (TextView) findViewById(R.id.setting_callback_tv);
		gradeTv = (TextView) findViewById(R.id.setting_grade_tv);

		logoutBtn = (Button) findViewById(R.id.person_center_logout_btn);
		if (!app.isLogin) {
			BaseUtils.toLogin(SettingActivity.this);
			// NoLoginDialog dialog = new NoLoginDialog(SettingActivity.this);
			// dialog.show();
			return;
		}
		if (app.userVO.getUsersetting() != null) {
			if (app.userVO.getUsersetting().getNewmessagereminder()
					.equals("true")) {
				messageCk.setChecked(true);
			} else {
				messageCk.setChecked(false);
			}
			if (app.userVO.getUsersetting().getReservationreminder()
					.equals("true")) {
				appointmentCk.setChecked(true);
			} else {
				appointmentCk.setChecked(false);
			}
		}

	}

	private void setListener() {
		aboutUsTv.setOnClickListener(this);
		// rateTv.setOnClickListener(this);
		callbackTv.setOnClickListener(this);
		helpTv.setOnClickListener(this);
		setting_update.setOnClickListener(this);
		appointmentCk.setOnCheckedChangeListener(this);
		messageCk.setOnCheckedChangeListener(this);
		gradeTv.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		paramsMap.put("usertype", "1");
		paramsMap.put("reservationreminder", appointmentCk.isChecked() ? "1"
				: "0");
		paramsMap.put("newmessagereminder", messageCk.isChecked() ? "1" : "0");
		paramsMap.put("classremind", "1");

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());

		HttpSendUtils.httpPostSend(changeTip, this, Config.IP
				+ "api/v1/userinfo/personalsetting", paramsMap, 10000,
				headerMap);

	}

	private void obtainVersionInfo() {
		HttpSendUtils.httpGetSend(version, this, Config.IP
				+ "api/v1/appversion/1");
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;

		case R.id.setting_update:
			showDialog();
			break;
		case R.id.person_center_logout_btn:
			ZProgressHUD.getInstance(this).setMessage("正在退出登录...");
			ZProgressHUD.getInstance(this).show();
			EMChatManager.getInstance().logout(null);
			setTag();
			LoginOut();
			break;
		case R.id.setting_hlep:
			intent = new Intent(this, SettingHelp.class);
			break;
		case R.id.setting_aboutus_tv:
			if (!CommonUtil.isNetworkConnected(this)) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("网络异常");
			} else {
				intent = new Intent(this, AboutUsActivity.class);
			}
			break;
		case R.id.setting_callback_tv:
			intent = new Intent(this, CallBackActivity.class);
			break;
		case R.id.setting_grade_tv:
			rateAppInMarket(this);
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void showDialog() {
		if (getIntent().getBooleanExtra("update", false)) {

			// 如果正在下载，return
			if (isMyServiceRunning()) {
				return;
			}
			String curVersion = util.getAppVersion().replace("v", "")
					.replace("V", "").replace(".", "");
			String newVersion = app.versionVO.getVersionCode().replace("v", "")
					.replace("V", "").replace(".", "");
			try {
				if (newVersion != curVersion) {

					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("发现新版本");
					builder.setMessage(getString(R.string.app_name) + "有新版本啦！");
					builder.setPositiveButton("立即更新",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									startService(new Intent(
											SettingActivity.this,
											DownLoadService.class).putExtra(
											"url",
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	private void setTag() {
		if (app.isLogin) {
			JPushInterface.setAlias(this, "", new MyTagAliasCallback());
		}
	}

	private int sum = 0;

	private void LoginOut() {
		new MyHandler(1000) {
			@Override
			public void run() {
				ZProgressHUD.getInstance(SettingActivity.this).dismiss();
				util.saveParam(Config.LAST_LOGIN_PASSWORD, "");
				util.saveParam(Config.LAST_LOGIN_ACCOUNT, "");
				Intent intent = new Intent(SettingActivity.this,
						NewLoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		};
	}

	private class MyTagAliasCallback implements TagAliasCallback {

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			sum++;
			if (arg0 != 0 && sum < 5) {
				setTag();
			} else {
				ZProgressHUD.getInstance(SettingActivity.this).dismiss();
				util.saveParam(Config.LAST_LOGIN_PASSWORD, "");
				Intent intent = new Intent(SettingActivity.this,
						LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}

	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(changeTip)) {
			if (dataString != null) {
				app.userVO.getUsersetting().setReservationreminder(
						appointmentCk.isChecked() ? "true" : "false");
				app.userVO.getUsersetting().setNewmessagereminder(
						messageCk.isChecked() ? "true" : "false");
			}
		} else if (type.equals(version)) {

			try {
				VersionVO versionVO = JSONUtil
						.toJavaBean(VersionVO.class, data);
				app.versionVO = versionVO;

				tv_code.setText(app.versionVO.getVersionCode());

			} catch (Exception e) {
				ZProgressHUD.getInstance(this).dismiss();
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("版本数据解析错误");
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void rateAppInMarket(Activity activity) {
		if (activity == null) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id="
				+ BlackCatApplication.getInstance().getPackageName()));
		try {
			activity.startActivity(intent);
			activity.overridePendingTransition(0, 0);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
}
