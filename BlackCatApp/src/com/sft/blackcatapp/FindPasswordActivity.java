package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

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
 * 找回密码界面
 * 
 * @author Administrator
 * 
 */
public class FindPasswordActivity extends BaseActivity {

	// 获取验证码
	private final static String obtainCode = "obtainCode";
	private static final String changepassword = "changepassword";
	// 手机号输入框
	private EditText phoneEt;
	// 验证码输入框
	private EditText codeEt;
	// 密码输入框
	private EditText passwordEt;
	// 发送验证码按钮
	private Button sendCodeBtn;
	// 下一步按钮
	private Button nextBtn;
	// 获取验证码间隔时间(秒)
	private final static int codeTime = 60;
	private MyHandler codeHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_find_password);
		initView();
		setListener();
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.find_password);

		sendCodeBtn = (Button) findViewById(R.id.findpass_code_btn);
		nextBtn = (Button) findViewById(R.id.findpass_finish_btn);
		phoneEt = (EditText) findViewById(R.id.findpass_phone_et);
		codeEt = (EditText) findViewById(R.id.findpass_code_et);
		passwordEt = (EditText) findViewById(R.id.findpass_password_et);

		phoneEt.setHint(setHint(R.string.phonenumber));
		codeEt.setHint(setHint(R.string.auth_code));
		passwordEt.setHint(R.string.new_password);

		String phone = getIntent().getStringExtra("phone");
		if (phone != null) {
			phoneEt.setText(phone);
		}
	}

	private void setListener() {
		sendCodeBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	private void obtainCode() {
		String phone = phoneEt.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			if (phone.length() == 11) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("mobile", phone);
				HttpSendUtils.httpGetSend(obtainCode, this, Config.IP
						+ "api/v1/code/" + phone);
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("请填写正确的手机号");
			}
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("请填写手机号");
		}
	}

	private void changePassword() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("smscode", codeEt.getText().toString());
		paramMap.put("password", util.MD5(passwordEt.getText().toString()));
		paramMap.put("mobile", phoneEt.getText().toString());
		paramMap.put("usertype", "1");

		HttpSendUtils.httpPostSend(changepassword, this, Config.IP
				+ "api/v1/userinfo/updatepwd", paramMap);
	}

	private String checkInput() {
		String phone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			return "手机号为空";
		}
		if (phone.length() != 11) {
			return "请输入正确的手机号";
		}
		String code = codeEt.getText().toString();
		if (TextUtils.isEmpty(code)) {
			return "验证码为空";
		}
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			return "密码为空";
		}
		return null;
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
		} else if (type.equals(changepassword)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("修改成功");
				new MyHandler(1000) {
					@Override
					public void run() {
						finish();
					}
				};
			}
		}
		return true;
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
		case R.id.findpass_code_btn:
			obtainCode();
			break;
		case R.id.findpass_finish_btn:
			String result = checkInput();
			if (result == null) {
				changePassword();
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure(result);
			}
			break;
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
