package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.sft.common.Config;
import com.sft.util.CommonUtil;

/**
 * 找回密码界面
 * 
 * @author Administrator
 * 
 */
public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {

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
	private TextView tv_hint_code;
	private TextView tv_hint_phone;
	private TextView tv_hint_pasword;
	private ImageView show_password;
	private ImageView delet_iv;

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

		show_password = (ImageView) findViewById(R.id.show_password);
		delet_iv = (ImageView) findViewById(R.id.delet_iv);

		sendCodeBtn = (Button) findViewById(R.id.findpass_code_btn);
		nextBtn = (Button) findViewById(R.id.findpass_finish_btn);
		phoneEt = (EditText) findViewById(R.id.findpass_phone_et);
		codeEt = (EditText) findViewById(R.id.findpass_code_et);
		passwordEt = (EditText) findViewById(R.id.findpass_password_et);

		tv_hint_phone = (TextView) findViewById(R.id.tv_hint_phone);
		tv_hint_code = (TextView) findViewById(R.id.tv_hint_code);
		tv_hint_pasword = (TextView) findViewById(R.id.tv_hint_pasword);

		String phone = getIntent().getStringExtra("phone");
		if (phone != null) {
			phoneEt.setText(phone);
		}
	}

	private void setListener() {
		sendCodeBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		phoneEt.setOnClickListener(this);
		codeEt.setOnClickListener(this);
		passwordEt.setOnClickListener(this);

		show_password.setOnClickListener(this);
		delet_iv.setOnClickListener(this);
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
				// ZProgressHUD.getInstance(this).show();
				// ZProgressHUD.getInstance(this).dismissWithFailure("请填写正确的手机号");
				tv_hint_phone.setVisibility(View.VISIBLE);
			}
		} else {
			// ZProgressHUD.getInstance(this).show();
			// ZProgressHUD.getInstance(this).dismissWithFailure("请填写手机号");
			tv_hint_phone.setVisibility(View.VISIBLE);
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
			tv_hint_phone.setVisibility(View.VISIBLE);
			tv_hint_phone.setText("手机号不能为空");
			return "手机号不能为空";
		} else if (!CommonUtil.isMobile(phone)) {
			tv_hint_phone.setVisibility(View.VISIBLE);
			tv_hint_phone.setText("手机号格式不正确");
			return "手机号格式不正确";
		} else if (phone.length() != 11) {
			tv_hint_phone.setVisibility(View.VISIBLE);
			tv_hint_phone.setText("请输入正确的手机号");
			return "请输入正确的手机号";
		}
		String code = codeEt.getText().toString();
		if (TextUtils.isEmpty(code)) {
			tv_hint_code.setVisibility(View.VISIBLE);
			tv_hint_code.setText("验证码不能为空");
			return "验证码不能为空";
		}
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			tv_hint_pasword.setVisibility(View.VISIBLE);
			tv_hint_pasword.setText("密码不能为空");
			return "密码不能为空";
		}
		return null;
	}

	private String checkInputs() {
		String phone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			tv_hint_phone.setVisibility(View.VISIBLE);
			tv_hint_phone.setText("手机号不能为空");
			return "手机号不能为空";
		} else if (!CommonUtil.isMobile(phone)) {
			tv_hint_phone.setVisibility(View.VISIBLE);
			tv_hint_phone.setText("手机号格式不正确");
			return "手机号格式不正确";
		} else if (phone.length() != 11) {
			tv_hint_phone.setVisibility(View.VISIBLE);
			tv_hint_phone.setText("请输入正确的手机号");
			return "请输入正确的手机号";
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
			if (dataString != null && result.equals("1")) {
				// ZProgressHUD.getInstance(this).show();
				// ZProgressHUD.getInstance(this).dismissWithSuccess("修改成功");
				new MyHandler(1000) {
					@Override
					public void run() {
						finish();
					}
				};
			} else if (msg.contains("验证码错误")) {
				tv_hint_code.setVisibility(View.VISIBLE);
				tv_hint_code.setText("验证码错误，请重新发送");
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
		case R.id.delet_iv:
			phoneEt.setText("");
			break;
		case R.id.show_password:
			passwordEt
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			break;
		case R.id.findpass_code_btn:
			String result1 = checkInputs();
			if (result1 == null) {
				obtainCode();
			} else {
				// ZProgressHUD.getInstance(this).show();
				// ZProgressHUD.getInstance(this).dismissWithFailure(result1);
			}

			break;
		case R.id.findpass_finish_btn:
			String result = checkInput();
			if (result == null) {
				changePassword();
			} else {
				// ZProgressHUD.getInstance(this).show();
				// ZProgressHUD.getInstance(this).dismissWithFailure(result);
			}
			break;
		case R.id.findpass_phone_et:
			tv_hint_phone.setVisibility(View.GONE);
			break;
		case R.id.findpass_code_et:
			tv_hint_code.setVisibility(View.GONE);
			break;
		case R.id.findpass_password_et:
			tv_hint_pasword.setVisibility(View.GONE);
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
