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

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

/**
 * 修改绑定手机
 * 
 * @author Administrator
 * 
 */
public class ChangePhoneActivity extends BaseActivity {

	// 获取验证码
	private final static String obtainCode = "obtainCode";
	private final static String changePhone = "changePhone";
	// 获取验证码间隔时间(秒)
	private final static int codeTime = 60;
	private MyHandler codeHandler;

	private EditText phoneEt, codeEt;

	private Button sendCodeBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_change_phone);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.change_tie_phone);

		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.save);

		sendCodeBtn = (Button) findViewById(R.id.change_phone_code_btn);
		phoneEt = (EditText) findViewById(R.id.change_phone_phone_et);
		codeEt = (EditText) findViewById(R.id.change_phone_code_et);

		phoneEt.setHint(setHint(R.string.new_phonenumber));
		codeEt.setHint(setHint(R.string.auth_code));

		phoneEt.setText(app.userVO.getMobile());
	}

	private void setListener() {
		sendCodeBtn.setOnClickListener(this);
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
				ZProgressHUD.getInstance(this).dismissWithFailure("请输入正确的手机号");
			}
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("手机号为空");
		}
	}

	private void changePhone() {
		String result = checkInput();
		if (result == null) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("smscode", codeEt.getText().toString());
			paramMap.put("mobile", phoneEt.getText().toString());

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changePhone, this, Config.IP
					+ "api/v1/userinfo/updatemobile", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(result);
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
								.setBackgroundResource(R.drawable.change_phone_btn_bkground);
					}
				}
			};
		} else if (type.equals(changePhone)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("修改成功");
				app.userVO.setTelephone(phoneEt.getText().toString());
				util.saveParam(Config.LAST_LOGIN_ACCOUNT, phoneEt.getText()
						.toString());
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
		case R.id.change_phone_code_btn:
			obtainCode();
			break;
		case R.id.base_right_tv:
			changePhone();
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
