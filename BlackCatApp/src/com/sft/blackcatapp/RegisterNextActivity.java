package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.UserVO;

/**
 * 注册界面
 * 
 * @author Administrator
 * 
 */
public class RegisterNextActivity extends BaseActivity implements
		EMLoginListener {

	private MyHandler codeHandler;
	// 密码输入框
	private EditText passwordEt;
	// 邀请人输入框
	private EditText invitationEt;
	// 注册按钮
	private Button registerBtn;
	// 用户协议

	private ImageView deletePassword;
	private ImageView deleteInvatation;
	private CheckBox rb_check;

	// 注册
	private final static String register = "register";
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addView(R.layout.activity_register_next);
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

		passwordEt = (EditText) findViewById(R.id.register_password_et);
		invitationEt = (EditText) findViewById(R.id.register_invitationcode_et);
		registerBtn = (Button) findViewById(R.id.register_register_btn);

		deletePassword = (ImageView) findViewById(R.id.delete_password);
		deleteInvatation = (ImageView) findViewById(R.id.delet_invitation);
		deletePassword.setVisibility(View.GONE);
		deleteInvatation.setVisibility(View.GONE);

		rb_check = (CheckBox) findViewById(R.id.rb_check);
	}

	private void setListener() {
		registerBtn.setOnClickListener(this);
		passwordEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					deletePassword.setVisibility(View.GONE);
				} else {
					deletePassword.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		invitationEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					deleteInvatation.setVisibility(View.GONE);
				} else {
					deleteInvatation.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		deletePassword.setOnClickListener(this);
		deleteInvatation.setOnClickListener(this);

	}

	private String checkInput() {
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			return "密码不能为空";
		}

		if (rb_check.isChecked() != true) {
			return "请选择用户协议";
		}

		phone = getIntent().getStringExtra("phone");
		if (TextUtils.isEmpty(phone)) {
			return "手机号不能为空";
		}
		return null;
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
		case R.id.delete_password:
			passwordEt.setText("");
			break;
		case R.id.delet_invitation:
			invitationEt.setText("");
			break;
		case R.id.register_register_btn:
			// 注册
			register();
			break;

		}
	}

	private void register() {
		String checkResult = checkInput();
		if (checkResult == null) {
			// 注册
			registerBtn.setEnabled(true);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("mobile", phone);
			paramMap.put("smscode", "");
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

		if (type.equals(register)) {
			try {
				if (data != null && result.equals("1")) {
					app.userVO = JSONUtil.toJavaBean(UserVO.class, data);

					util.saveParam(Config.LAST_LOGIN_PHONE,
							app.userVO.getTelephone());
					util.saveParam(Config.LAST_LOGIN_ACCOUNT, phone);
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
			Intent intent = new Intent(RegisterNextActivity.this,
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
					ZProgressHUD.getInstance(RegisterNextActivity.this).show();
					ZProgressHUD.getInstance(RegisterNextActivity.this)
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
					ZProgressHUD.getInstance(RegisterNextActivity.this).show();
					ZProgressHUD.getInstance(RegisterNextActivity.this)
							.dismissWithFailure("初始化聊天失败");
				}
			});
		}
	}
}
