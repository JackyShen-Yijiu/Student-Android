package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

/**
 * 报名验证
 * 
 * @author Administrator
 * 
 */
public class TestingPhoneActivity extends BaseActivity {
	private MyHandler codeHandler;

	// 获取验证码间隔时间(秒)
	private final static int codeTime = 60;
	// 真实姓名输入框
	private EditText nameEt;
	// 手机号输入框
	private EditText phoneEt;
	// 验证码输入框
	private EditText codeEt;
	// 提交
	private Button commitBtn;
	// 发送验证码按钮
	private Button sendCodeBtn;
	// 获取验证码
	private final static String obtainCode = "obtainCode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.testing_phone);
		initView();
		setListener();
	}

	private void initView() {
		setTitleText(R.string.enroll_vertify);

		phoneEt = (EditText) findViewById(R.id.testing_phone_et);
		nameEt = (EditText) findViewById(R.id.testing_name_et);
		codeEt = (EditText) findViewById(R.id.testing_code_et);

		commitBtn = (Button) findViewById(R.id.button_next);
		sendCodeBtn = (Button) findViewById(R.id.button_send);

	}

	private void setListener() {
		commitBtn.setOnClickListener(this);
		sendCodeBtn.setOnClickListener(this);
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
		case R.id.base_right_btn:
			break;
		case R.id.button_next:
			Intent intentt = new Intent(this, TestingMsgActivity.class);
			startActivity(intentt);
			// finish();
			register();
			break;
		case R.id.button_send:
			obtainCode();
			break;
		}
	}

	private void register() {
		String checkResult = checkInput();
		if (checkResult == null) {
			// 注册
			commitBtn.setEnabled(false);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("mobile", phoneEt.getText().toString());
			paramMap.put("smscode", codeEt.getText().toString());
			paramMap.put("name", nameEt.getText().toString());
			paramMap.put("usertype", "1");
			HttpSendUtils.httpPostSend("register", this, Config.IP
					+ "api/v1/userinfo/signup", paramMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {

			return true;
		}

		if (type.equals(obtainCode)) {
			codeHandler = new MyHandler(true, 1000) {
				private int time = codeTime;

				public void run() {
					if (time-- > 0) {
						sendCodeBtn.setText("剩余(" + time + "s)");
						sendCodeBtn.setBackgroundColor(Color
								.parseColor("#cccccc"));
						sendCodeBtn.setEnabled(false);
					} else {
						codeHandler.cancle();
						sendCodeBtn.setEnabled(true);
						sendCodeBtn.setText(R.string.more_send_auth_code);
						sendCodeBtn
								.setBackgroundResource(R.drawable.btn_bkground);
					}
				}
			};
		}
		return true;
	}

	private String checkInput() {
		String phone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			return "手机号为空";
		}
		if (phone.length() != 11) {
			return "请输入正确的手机号";
		}
		String name = nameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			return "姓名为空";
		}
		String code = codeEt.getText().toString();
		if (TextUtils.isEmpty(code)) {
			return "验证码为空";
		}
		return null;
	}

	private void obtainCode() {
		String phone = phoneEt.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			if (phone.length() == 11) {
				HttpSendUtils.httpGetSend("obtainCode", this, Config.IP
						+ "api/v1/code/" + phone);
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("请输入正确的手机号");
			}
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("手机号为空");

		}
	}

	@Override
	protected void onDestroy() {
		if (codeHandler != null) {
			codeHandler.cancle();
		}
		super.onDestroy();
	}

}
