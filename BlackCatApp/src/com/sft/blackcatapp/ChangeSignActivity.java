package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

/**
 * 修改签名
 * 
 * @author Administrator
 * 
 */
public class ChangeSignActivity extends BaseActivity {

	private EditText et;// 定义一个文本输入框
	private Button btn;

	private static final String changeSign = "changeSign";
	private static final String changeName = "changeName";
	private static final String changeNickName = "changeNickName";

	private String type;
	private CharSequence temp;
	private TextView tv;

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
		tv = (TextView) findViewById(R.id.text_dialog);
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

	final int MAX_LENGTH = 20;
	int Rest_Length = MAX_LENGTH;

	private void setListener() {
		btn.setOnClickListener(this);

		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (Rest_Length > 0) {
					Rest_Length = MAX_LENGTH - et.getText().length();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tv.setText("您还能输入" + Rest_Length + "个字");

			}

			@Override
			public void afterTextChanged(Editable s) {
				tv.setText("您还能输入" + Rest_Length + "个字");
			}

		});

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
			HttpSendUtils.httpPostSend(changeSign, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
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
			HttpSendUtils.httpPostSend(changeName, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
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
			HttpSendUtils.httpPostSend(changeNickName, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
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
