package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.sft.baseactivity.util.HttpSendUtils;

/**
 * 修改签名
 * 
 * @author Administrator
 * 
 */
public class ChangeSignActivity extends BaseActivity {

	private EditText et;
	private Button btn;

	private static final String changeSign = "changeSign";
	private static final String changeName = "changeName";
	private static final String changeNickName = "changeNickName";

	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_callback);
		initView();
		setListener();
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		btn = (Button) findViewById(R.id.callback_btn);
		et = (EditText) findViewById(R.id.callback_et);

		type = getIntent().getStringExtra("type");

		if (type.equals("sign")) {
			setTitleText(R.string.personal_sign);
			et.setHint(setHint(R.string.simple_sign));
		} else if (type.equals("name")) {
			setTitleText(R.string.name);
			et.setHint(setHint(R.string.name));
		} else {
			setTitleText(R.string.nickname);
			et.setHint(setHint(R.string.nickname));
		}
	}

	private void setListener() {
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.callback_btn:
			if (type.equals("sign")) {
				changeSign();
			} else if (type.equals("name")) {
				changeName();
			} else {
				changeNickName();
			}
			break;
		}
	}

	private void changeSign() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("signature", content);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeSign, this, Config.IP + "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("签名不能为空");
		}
	}

	private void changeName() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("name", content);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeName, this, Config.IP + "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("签名不能为空");
		}
	}

	private void changeNickName() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("nickname", content);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeNickName, this, Config.IP + "api/v1/userinfo/updateuserinfo", paramMap,
					10000, headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("签名不能为空");
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(changeSign)) {
			if (dataString != null) {
				app.userVO.setSignature(et.getText().toString());
				finish();
			}
		} else if (type.equals(changeName)) {
			if (dataString != null) {
				app.userVO.setName(et.getText().toString());
				finish();
			}
		} else if (type.equals(changeNickName)) {
			if (dataString != null) {
				app.userVO.setNickname(et.getText().toString());
				finish();
			}
		}
		return true;
	}
}
