package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.viewutil.ZProgressHUD;

/**
 * 注册界面
 * 
 * @author Administrator
 * 
 */
public class FindPasswordAct extends BaseActivity {

	// 获取验证码间隔时间(秒)
	private final static int codeTime = 60;
	private MyHandler codeHandler;
	// 手机号输入框
	private EditText phoneEt;
	// 验证码输入框
	private EditText codeEt;

	private EditText etPwd;
	// 发送验证码按钮
	private Button sendCodeBtn;
	// 注册按钮
	private Button registerBtn;
	// 用户协议

	private ImageView delet_phone;

	private ImageView imgShowPwd;

	// 获取验证码
	private final static String obtainCode = "obtainCode";
	// 注册
	private final static String register = "register";
	// 验证验证码
	private final static String SMS_CODE = "smscode";

	private final static String CHANGE_PWD = "changepassword";

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
		setTitleText(R.string.find_password);

		phoneEt = (EditText) findViewById(R.id.register_phone_et);
		codeEt = (EditText) findViewById(R.id.register_authcode_et);
		etPwd = (EditText) findViewById(R.id.register_password_et);

		sendCodeBtn = (Button) findViewById(R.id.register_code_btn);
		registerBtn = (Button) findViewById(R.id.register_register_btn);

		
		imgShowPwd = (ImageView) findViewById(R.id.show_password);
		
//		imgShowPwd = (ImageView) findViewById(R.id.delete_password);
//
//>>>>>>> d7df2de3fa5f56df5827368d6702db63b83e1276
		delet_phone = (ImageView) findViewById(R.id.delet_phone);
		delet_phone.setVisibility(View.GONE);
		findViewById(R.id.act_regist_rl).setVisibility(View.GONE);

		registerBtn.setText(R.string.forget_password);
	}

	private void setListener() {
		sendCodeBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		phoneEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					delet_phone.setVisibility(View.GONE);
				} else {
					delet_phone.setVisibility(View.VISIBLE);
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
		delet_phone.setOnClickListener(this);
	}

	private boolean isClick;

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.show_password:
			if (isClick) {
				imgShowPwd.setImageResource(R.drawable.password_btn_display);
				etPwd.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			} else {
				imgShowPwd.setImageResource(R.drawable.password_btn_hide);
				etPwd.setText(etPwd.getText());
				etPwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());

			}
			isClick = !isClick;

			if (isClick) {
			}
			break;
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
			// 注册下一步
			if (TextUtils.isEmpty(checkInput())) {
				changePassword();
//				obtainVerificationSmscode();
			} else {
				ZProgressHUD.getInstance(FindPasswordAct.this).show();
				ZProgressHUD.getInstance(FindPasswordAct.this)
						.dismissWithFailure(checkInput());
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
		String pwd = etPwd.getText().toString();
		if (TextUtils.isEmpty(code)) {
			return "验证码不能为空";
		} else if (TextUtils.isEmpty(pwd)) {// 密码不能为空
			return "密码不能为空";
		}
		return null;
	}

	private void obtainCode() {
		String phone = phoneEt.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			if (CommonUtil.isMobile(phone)) {
				HttpSendUtils.httpGetSend("obtainCode", this, Config.IP
						+ "api/v1/code/" + phone);
			} else {
				ZProgressHUD.getInstance(FindPasswordAct.this).show();
				ZProgressHUD.getInstance(FindPasswordAct.this)
						.dismissWithFailure("请输入正确的手机号");
			}
		} else {
			ZProgressHUD.getInstance(FindPasswordAct.this).show();
			ZProgressHUD.getInstance(FindPasswordAct.this).dismissWithFailure(
					"请输入手机号");
		}
	}

	private void obtainVerificationSmscode() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", phoneEt.getText().toString());
		paramMap.put("code", codeEt.getText().toString());
		HttpSendUtils.httpGetSend(SMS_CODE, this, Config.IP
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
		} else if (type.equals(SMS_CODE)) {
			if (dataString != null) {

				
				if(result.equals("1")){	//验证成功,请求更改密码


//				if (result.equals("1")) { // 验证成功,请求更改密码
//>>>>>>> d7df2de3fa5f56df5827368d6702db63b83e1276
					changePassword();
				} else {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithSuccess(msg);
				}
				// Intent intent = new Intent(this, FindPasswordActivity.class);
				// intent.putExtra("phone", phoneEt.getText().toString());
				// startActivityForResult(intent, 9);
				// startActivity(intent);
			}
		} else if (type.equals(CHANGE_PWD)) {
			if (dataString != null && result.equals("1")) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("修改成功");
				new MyHandler(200) {
					@Override
					public void run() {
						setResult(9);
						finish();
					}
				};

			}
			return true;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 9://
			finish();
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		if (codeHandler != null) {
			codeHandler.cancle();
		}
		super.onDestroy();
	}

	private void changePassword() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("password", util.MD5(etPwd.getText().toString()));
		paramMap.put("usertype", "1");
		paramMap.put("mobile", phoneEt.getText().toString());
		paramMap.put("smscode", codeEt.getText().toString());
		
		HttpSendUtils.httpPostSend(CHANGE_PWD, this, Config.IP
				+ "api/v1/userinfo/updatepwd", paramMap);
	}
	//
	// private String checkInput() {
	// String password = passwordEt.getText().toString();
	// if (TextUtils.isEmpty(password)) {
	// return "密码不能为空";
	// }
	// return null;
	// }
	//
	// @Override
	// public synchronized boolean doCallBack(String type, Object jsonString) {
	// if (super.doCallBack(type, jsonString)) {
	// return true;
	// }
	//
	// if (type.equals(changepassword)) {
	// }
	// return true;
	// }

}
