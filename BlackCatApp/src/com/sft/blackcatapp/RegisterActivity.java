package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
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
	// 密码输入框
	private EditText passwordEt;
	// 确认密码输入框
	// private EditText conpassEt;
	// 邀请码输入框
	private EditText invitationEt;
	// 发送验证码按钮
	private Button sendCodeBtn;
	// 注册按钮
	private Button registerBtn;
	// 用户协议
	private TextView protocol;

	private TextView tv_hint_phone;
	private TextView tv_hint_code;
	private TextView tv_hint_pasword;
	private TextView tv_hint_invite;
	private TextView tv_hint_deal;
	private CheckBox rb_check;
	private ImageView delet_phone;
	private ImageView delet_init;
	private ImageView show_password;

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
		rb_check = (CheckBox) findViewById(R.id.rb_check);
		phoneEt = (EditText) findViewById(R.id.register_phone_et);
		codeEt = (EditText) findViewById(R.id.register_authcode_et);
		passwordEt = (EditText) findViewById(R.id.register_password_et);
		// conpassEt = (EditText) findViewById(R.id.register_conpass_et);
		invitationEt = (EditText) findViewById(R.id.register_invitationcode_et);
		sendCodeBtn = (Button) findViewById(R.id.register_code_btn);
		registerBtn = (Button) findViewById(R.id.register_register_btn);

		tv_hint_phone = (TextView) findViewById(R.id.tv_hint_phone);
		tv_hint_code = (TextView) findViewById(R.id.tv_hint_code);
		tv_hint_pasword = (TextView) findViewById(R.id.tv_hint_pasword);
		tv_hint_invite = (TextView) findViewById(R.id.tv_hint_invite);
		tv_hint_deal = (TextView) findViewById(R.id.tv_hint_deal);

		delet_phone = (ImageView) findViewById(R.id.delet_phone);
		delet_init = (ImageView) findViewById(R.id.delet_init);
		show_password = (ImageView) findViewById(R.id.show_password);
	}

	private void setListener() {
		sendCodeBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		protocol.setOnClickListener(this);

		phoneEt.setOnClickListener(this);
		codeEt.setOnClickListener(this);
		passwordEt.setOnClickListener(this);
		// conpassEt.setOnClickListener(this);
		invitationEt.setOnClickListener(this);

		delet_phone.setOnClickListener(this);
		delet_init.setOnClickListener(this);
		show_password.setOnClickListener(this);
	}

	boolean isClick = true;

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
		case R.id.delet_phone:
			phoneEt.setText("");
			break;
		case R.id.delet_init:
			invitationEt.setText("");
			break;
		case R.id.show_password:
			if (isClick) {
				passwordEt
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
			} else {
				passwordEt.setText(passwordEt.getText());
				passwordEt.setTransformationMethod(PasswordTransformationMethod
						.getInstance());

			}
			isClick = !isClick;

			if (isClick) {
			}
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

		case R.id.register_phone_et:
			tv_hint_phone.setVisibility(View.GONE);
			break;
		case R.id.register_authcode_et:
			tv_hint_code.setVisibility(View.GONE);
			break;
		case R.id.register_password_et:
			tv_hint_pasword.setVisibility(View.GONE);
			break;

		case R.id.register_invitationcode_et:
			tv_hint_deal.setVisibility(View.GONE);
			break;
		}
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
			paramMap.put("referrerCode", invitationEt.getText().toString());
			HttpSendUtils.httpPostSend("register", this, Config.IP
					+ "api/v1/userinfo/signup", paramMap);
		} else {
			// ZProgressHUD.getInstance(this).show();
			// ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
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

		// String invite = invitationEt.getText().toString();
		// if (!CommonUtil.isMobile(invite)) {
		// tv_hint_invite.setVisibility(View.VISIBLE);
		// tv_hint_invite.setText("手机号格式不正确");
		// return "手机号格式不正确";
		// } else if (invite.length() != 11) {
		// tv_hint_invite.setVisibility(View.VISIBLE);
		// tv_hint_invite.setText("请输入正确的手机号");
		// return "请输入正确的手机号";
		// }
		if (rb_check.isChecked() != true) {
			tv_hint_deal.setVisibility(View.VISIBLE);
			tv_hint_deal.setText("请选择用户协议");
			return "请选择用户协议";
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
				// ZProgressHUD.getInstance(this).show();
				// ZProgressHUD.getInstance(this).dismissWithFailure("请输入正确的手机号");
				tv_hint_phone.setVisibility(View.VISIBLE);
			}
		} else {
			// ZProgressHUD.getInstance(this).show();
			// ZProgressHUD.getInstance(this).dismissWithFailure("手机号为空");
			tv_hint_phone.setVisibility(View.VISIBLE);
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

				@Override
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
				if (data != null && result.equals("1")) {
					app.userVO = JSONUtil.toJavaBean(UserVO.class, data);
				} else if (msg.contains("验证码错误，请重新发送")) {
					tv_hint_code.setVisibility(View.VISIBLE);
					tv_hint_code.setText("验证码错误，请重新发送");
					return true;
				} else if (msg.contains("用户已存在请直接登录")) {
					tv_hint_phone.setVisibility(View.VISIBLE);
					tv_hint_phone.setText("用户已存在请直接登录");
				}
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
				// ZProgressHUD.getInstance(this).show();
				// ZProgressHUD.getInstance(this).dismissWithFailure("用户数据解析错误");

				tv_hint_phone.setVisibility(View.VISIBLE);
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
							.dismissWithFailure("注册成功");
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
