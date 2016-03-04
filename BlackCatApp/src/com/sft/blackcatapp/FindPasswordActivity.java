package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

/**
 * 找回密码界面
 * 
 * @author Administrator
 * 
 */
public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {

	// 获取验证码
	private static final String changepassword = "changepassword";
	// 密码输入框
	private EditText passwordEt;
	// 下一步按钮
	private Button nextBtn;
	private ImageView show_password;

	private String phone;

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
		phone = getIntent().getStringExtra("phone");
		show_password = (ImageView) findViewById(R.id.show_password);
		nextBtn = (Button) findViewById(R.id.findpass_finish_btn);
		passwordEt = (EditText) findViewById(R.id.findpass_password_et);

	}

	private void setListener() {
		nextBtn.setOnClickListener(this);

		passwordEt.setOnClickListener(this);

		show_password.setOnClickListener(this);
	}

	private void changePassword() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("password", util.MD5(passwordEt.getText().toString()));
		paramMap.put("usertype", "1");
		paramMap.put("mobile", phone);

		HttpSendUtils.httpPostSend(changepassword, this, Config.IP
				+ "api/v1/userinfo/updatepwd", paramMap);
	}

	private String checkInput() {
		String password = passwordEt.getText().toString();
		if (TextUtils.isEmpty(password)) {
			return "密码不能为空";
		}
		return null;
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		if (type.equals(changepassword)) {
			if (dataString != null && result.equals("1")) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("修改成功");
				new MyHandler(1000) {
					@Override
					public void run() {
						finish();
					}
				};

			}
			return true;
		}
		return true;
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

}
