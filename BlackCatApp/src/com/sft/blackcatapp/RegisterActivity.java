package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.jzjf.app.R;
import com.sft.api.UserLogin;
import com.sft.common.Config;
import com.sft.listener.EMLoginListener;
import com.sft.util.CommonUtil;
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
	// 发送验证码按钮
	private Button sendCodeBtn;
	// 密码输入框
	private EditText passwordEt;
	// 注册按钮
	private Button registerBtn;
	// 用户协议
	private CheckBox rb_check;

	private ImageView show_password;
	boolean isClick = true;

	private ImageView delet_phone;

	// 获取验证码
	private final static String obtainCode = "obtainCode";
	// 注册
	private final static String register = "register";
	// 验证验证码
	private final static String smscode = "smscode";

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

		phoneEt = (EditText) findViewById(R.id.register_phone_et);
		codeEt = (EditText) findViewById(R.id.register_authcode_et);
		passwordEt = (EditText) findViewById(R.id.register_password_et);
		sendCodeBtn = (Button) findViewById(R.id.register_code_btn);
		registerBtn = (Button) findViewById(R.id.register_register_btn);
		rb_check = (CheckBox) findViewById(R.id.rb_check);
		show_password = (ImageView) findViewById(R.id.show_password);

		delet_phone = (ImageView) findViewById(R.id.delet_phone);

		delet_phone.setVisibility(View.GONE);
	}

	private void setListener() {
		sendCodeBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		show_password.setOnClickListener(this);
		// phoneEt.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// if (TextUtils.isEmpty(s)) {
		// delet_phone.setVisibility(View.GONE);
		// } else {
		// delet_phone.setVisibility(View.VISIBLE);
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		//
		// }
		// });
		delet_phone.setOnClickListener(this);
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
		case R.id.delet_phone:
			phoneEt.setText("");
			break;
		case R.id.register_code_btn:
			obtainCode();
			break;
		case R.id.register_register_btn:
			// 注册
			register();
			// if (TextUtils.isEmpty(checkInput())) {
			// obtainVerificationSmscode();
			// } else {
			// ZProgressHUD.getInstance(RegisterActivity.this).show();
			// ZProgressHUD.getInstance(RegisterActivity.this)
			// .dismissWithFailure(checkInput());
			// }
			break;
		case R.id.show_password:
			if (isClick) {
				passwordEt.setText(passwordEt.getText());
				passwordEt
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
				show_password.setImageResource(R.drawable.password_btn_display);
			} else {
				passwordEt.setText(passwordEt.getText());
				passwordEt.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				show_password.setImageResource(R.drawable.password_btn_hide);

			}
			isClick = !isClick;

			if (isClick) {
			}
			break;

		}
	}

	private String checkInput() {
		String phone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			return "手机号不能为空";
		} else if (!CommonUtil.isMobile(phone)) {
			return "手机号格式不正确";
		} else if (phone.length() != 11) {
			return "请输入正确的手机号";
		}
		String code = codeEt.getText().toString();
		if (TextUtils.isEmpty(code)) {
			return "验证码不能为空";
		}
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			return "密码不能为空";
		}

		if (rb_check.isChecked() != true) {
			return "请选择用户协议";
		}
		return null;
	}

	private void register() {
		String checkResult = checkInput();
		if (checkResult == null) {
			// 注册
			registerBtn.setEnabled(true);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("mobile", phoneEt.getText().toString());
			paramMap.put("smscode", codeEt.getText().toString());
			paramMap.put("usertype", "1");
			paramMap.put("password", util.MD5(passwordEt.getText().toString()));
			paramMap.put("referrerCode", "");
			HttpSendUtils.httpPostSend("register", this, Config.IP
					+ "api/v1/userinfo/signup", paramMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
	}

	private void obtainCode() {
		String phone = phoneEt.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			if (CommonUtil.isMobile(phone)) {
				HttpSendUtils.httpGetSend("obtainCode", this, Config.IP
						+ "api/v1/code/" + phone);
			} else {
				ZProgressHUD.getInstance(RegisterActivity.this).show();
				ZProgressHUD.getInstance(RegisterActivity.this)
						.dismissWithFailure("请输入正确的手机号");
			}
		} else {
			ZProgressHUD.getInstance(RegisterActivity.this).show();
			ZProgressHUD.getInstance(RegisterActivity.this).dismissWithFailure(
					"请输入手机号");
		}
	}

	private void obtainVerificationSmscode() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", phoneEt.getText().toString());
		paramMap.put("code", codeEt.getText().toString());
		HttpSendUtils.httpGetSend(smscode, this, Config.IP
				+ "api/v1/Verificationsmscode", paramMap);
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

				@Override
				public void run() {
					if (time-- > 0) {
						sendCodeBtn.setText("剩余(" + time + "s)");
						sendCodeBtn
								.setBackgroundResource(R.drawable.button_rounded_corners_gray);
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
		} else if (type.equals(smscode)) {
			if (dataString != null) {
				Intent intent = new Intent(this, RegisterNextActivity.class);
				intent.putExtra("phone", phoneEt.getText().toString());
				startActivity(intent);
			}
		} else if (type.equals(register)) {
			try {
				if (data != null && result.equals("1")) {
					app.userVO = JSONUtil.toJavaBean(UserVO.class, data);

					util.saveParam(Config.LAST_LOGIN_PHONE,
							app.userVO.getTelephone());
					util.saveParam(Config.LAST_LOGIN_ACCOUNT, phoneEt.getText()
							.toString());
					util.saveParam(Config.LAST_LOGIN_PASSWORD, passwordEt
							.getText().toString());

					new UserLogin(this).userLogin(app.userVO.getUserid(),
							util.MD5(passwordEt.getText().toString()),
							app.userVO.getNickname());
				}
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

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(RegisterActivity.this,
					MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		};
	};

	@Override
	public void loginResult(boolean result, int code, String message) {
		if (result) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					app.isLogin = true;
					ZProgressHUD.getInstance(RegisterActivity.this).show();
					ZProgressHUD.getInstance(RegisterActivity.this)
							.dismissWithSuccess("注册成功");
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(1500);
								myHandler.sendMessage(myHandler.obtainMessage());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}

			});

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
