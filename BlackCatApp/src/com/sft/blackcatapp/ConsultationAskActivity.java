package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

public class ConsultationAskActivity extends BaseActivity {

	private final String saveuserconsult = "saveuserconsult";
	private EditText askEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_consultation_ask);
		setTitleText(R.string.i_to_ask);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.finish);

		initView();
	}

	private void initView() {
		askEt = (EditText) findViewById(R.id.consultation_ask_et);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			obtainSaveUserConsult();
			break;
		}
	}

	private void obtainSaveUserConsult() {
		if (TextUtils.isEmpty(askEt.getText().toString().trim())) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("问题不能为空");
			return;
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("name", app.userVO.getName());
		paramMap.put("mobile", app.userVO.getMobile());
		paramMap.put("licensetype", app.userVO.getCarmodel().getCode());
		paramMap.put("content", askEt.getText().toString().trim());
		HttpSendUtils.httpPostSend(saveuserconsult, this, Config.IP
				+ "api/v1/saveuserconsult", paramMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		if (type.equals(saveuserconsult)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("提问成功");
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
}
