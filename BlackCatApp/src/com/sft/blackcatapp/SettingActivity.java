package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.easemob.chat.EMChatManager;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.CommonUtil;
import com.sft.viewutil.ZProgressHUD;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_setting);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.setting);

		appointmentCk = (CheckBox) findViewById(R.id.setting_appointment_ck);
		messageCk = (CheckBox) findViewById(R.id.setting_newmessage_ck);

		helpTv = (TextView) findViewById(R.id.setting_hlep);
		aboutUsTv = (TextView) findViewById(R.id.setting_aboutus_tv);
		callbackTv = (TextView) findViewById(R.id.setting_callback_tv);
		gradeTv = (TextView) findViewById(R.id.setting_grade_tv);

		logoutBtn = (Button) findViewById(R.id.person_center_logout_btn);
		if (!app.isLogin) {
			NoLoginDialog dialog = new NoLoginDialog(SettingActivity.this);
			dialog.show();
			return;
		}
		if (app.userVO.getUsersetting().getNewmessagereminder().equals("true")) {
			messageCk.setChecked(true);
		} else {
			messageCk.setChecked(false);
		}
		if (app.userVO.getUsersetting().getReservationreminder().equals("true")) {
			appointmentCk.setChecked(true);
		} else {
			appointmentCk.setChecked(false);
		}
	}

	private void setListener() {
		aboutUsTv.setOnClickListener(this);
		// rateTv.setOnClickListener(this);
		callbackTv.setOnClickListener(this);
		helpTv.setOnClickListener(this);
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
				Intent intent = new Intent(SettingActivity.this,
						LoginActivity.class);
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
