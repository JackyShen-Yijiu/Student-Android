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
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.sft.api.UserLogin;
import com.sft.common.Config;
import com.sft.listener.EMLoginListener;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.UserVO;

/**
 * 注册界面
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends BaseActivity implements EMLoginListener {

	// 获取验证码间隔时间(秒)
	private final static int codeTime = 60;
	private MyHandler codeHandler;
	// 手机号输入框
	private EditText phoneEt;
	// 验证码输入框
	private EditText codeEt;
	// 密码输入框
	private EditText passwordEt;
	// 确认密码输入框
	private EditText conpassEt;
	// 邀请码输入框
	private EditText invitationEt;
	// 发送验证码按钮
	private Button sendCodeBtn;
	// 注册按钮
	private Button registerBtn;
	// 用户协议
	private TextView protocol;

	// 获取验证码
	private final static String obtainCode = "obtainCode";
	// 注册
	private final static String register = "register";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_register);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.register);

		protocol = (TextView) findViewById(R.id.register_protocol_tv);

		phoneEt = (EditText) findViewById(R.id.register_phone_et);
		codeEt = (EditText) findViewById(R.id.register_authcode_et);
		passwordEt = (EditText) findViewById(R.id.register_password_et);
		conpassEt = (EditText) findViewById(R.id.register_conpass_et);
		invitationEt = (EditText) findViewById(R.id.register_invitationcode_et);
		sendCodeBtn = (Button) findViewById(R.id.register_code_btn);
		registerBtn = (Button) findViewById(R.id.register_register_btn);

		phoneEt.setHint(setHint(R.string.phonenumber));
		codeEt.setHint(setHint(R.string.auth_code));
		passwordEt.setHint(setHint(R.string.password));
		conpassEt.setHint(setHint(R.string.confirm_password));
		invitationEt.setHint(setHint(R.string.invitation_code));
	}

	private void setListener() {
		sendCodeBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		protocol.setOnClickListener(this);
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
		case R.id.register_register_btn:
			register();
			break;
		case R.id.register_code_btn:
			obtainCode();
			break;
		case R.id.register_protocol_tv:
			Intent intent = new Intent(this, TermsActivity.class);
			startActivity(intent);
			break;

		}
	}

	private void register() {
		String checkResult = checkInput();
		if (checkResult == null) {
			// 注册
			registerBtn.setEnabled(false);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("mobile", phoneEt.getText().toString());
			paramMap.put("smscode", codeEt.getText().toString());
			paramMap.put("usertype", "1");
			paramMap.put("password", util.MD5(passwordEt.getText().toString()));
			paramMap.put("referrerCode", invitationEt.getText().toString());
			HttpSendUtils.httpPostSend("register", this, Config.IP
					+ "api/v1/userinfo/signup", paramMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
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
		String conPass = conpassEt.getText().toString();
		if (!conPass.equals(password)) {
			return "两次密码输入不一致";
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
	public void doException(String type, Exception e, int code) {
		if (type.equals(register)) {
			registerBtn.setEnabled(true);
		}
		super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (type.equals(register)) {
			registerBtn.setEnabled(true);
		}
		super.doTimeOut(type);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			if (type.equals(register)) {
				registerBtn.setEnabled(true);

			}
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
		} else if (type.equals(register)) {
			try {
				app.userVO = (UserVO) JSONUtil.toJavaBean(UserVO.class, data);
				util.saveParam(Config.LAST_LOGIN_PHONE,
						app.userVO.getTelephone());
				util.saveParam(Config.LAST_LOGIN_ACCOUNT, phoneEt.getText()
						.toString());
				util.saveParam(Config.LAST_LOGIN_PASSWORD, passwordEt.getText()
						.toString());

				new UserLogin(this).userLogin(app.userVO.getUserid(),
						util.MD5(passwordEt.getText().toString()),
						app.userVO.getNickname());

			} catch (Exception e) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("用户数据解析错误");
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		if (codeHandler != null) {
			codeHandler.cancle();
		}
		super.onDestroy();
	}

	@Override
	public void loginResult(boolean result, int code, String message) {
		if (result) {
			app.isLogin = true;
			ZProgressHUD.getInstance(RegisterActivity.this).show();
			ZProgressHUD.getInstance(RegisterActivity.this).dismissWithFailure(
					"注册成功");
			new MyHandler(1000) {
				private Intent intent;

				@Override
				public void run() {
					intent = new Intent(RegisterActivity.this,
							MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
			};

		} else {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ZProgressHUD.getInstance(RegisterActivity.this).show();
					ZProgressHUD.getInstance(RegisterActivity.this)
							.dismissWithFailure("初始化聊天失败");
				}
			});
		}
	}
}
