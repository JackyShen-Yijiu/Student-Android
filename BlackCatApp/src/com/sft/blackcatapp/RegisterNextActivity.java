package com.sft.blackcatapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
public class RegisterNextActivity extends BaseActivity {

	// 获取验证码间隔时间(秒)
	private final static int codeTime = 60;
	private MyHandler codeHandler;
	// 手机号输入框
	private EditText phoneEt;
	// 验证码输入框
	private EditText codeEt;
	// 发送验证码按钮
	private Button sendCodeBtn;
	// 注册按钮
	private Button registerBtn;
	// 用户协议

	private ImageView delet_phone;

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

		phoneEt = (EditText) findViewById(R.id.register_phone_et);
		codeEt = (EditText) findViewById(R.id.register_authcode_et);
		sendCodeBtn = (Button) findViewById(R.id.register_code_btn);
		registerBtn = (Button) findViewById(R.id.register_register_btn);

		delet_phone = (ImageView) findViewById(R.id.delet_phone);
		delet_phone.setVisibility(View.GONE);
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
			// 注册下一步

			break;

		}
	}

	private void obtainCode() {
		String phone = phoneEt.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			if (CommonUtil.isMobile(phone)) {
				HttpSendUtils.httpGetSend("obtainCode", this, Config.IP
						+ "api/v1/code/" + phone);
			} else {
				ZProgressHUD.getInstance(RegisterNextActivity.this).show();
				ZProgressHUD.getInstance(RegisterNextActivity.this)
						.dismissWithFailure("请输入正确的手机号");
			}
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

}
