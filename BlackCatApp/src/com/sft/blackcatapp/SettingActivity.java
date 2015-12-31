package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;

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

		aboutUsTv = (TextView) findViewById(R.id.setting_aboutus_tv);
		callbackTv = (TextView) findViewById(R.id.setting_callback_tv);

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

		appointmentCk.setOnCheckedChangeListener(this);
		messageCk.setOnCheckedChangeListener(this);
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
		case R.id.setting_aboutus_tv:
			intent = new Intent(this, AboutUsActivity.class);
			break;
		case R.id.setting_callback_tv:
			intent = new Intent(this, CallBackActivity.class);
			break;
		}
		if (intent != null) {
			startActivity(intent);
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
}
