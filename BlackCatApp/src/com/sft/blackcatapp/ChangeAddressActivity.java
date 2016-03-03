package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

/**
 * 修改常用地址
 * 
 * @author Administrator
 * 
 */
public class ChangeAddressActivity extends BaseActivity {

	private EditText et;

	private static final String changeAddress = "changeAddress";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_callback);
		initView();
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.address);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.save);

		et = (EditText) findViewById(R.id.callback_et);

		et.setHint(setHint(R.string.address));
		et.setText(app.userVO.getAddress());

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

		case R.id.base_right_tv:
			changeAddress();
			break;
		}
	}

	private void changeAddress() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("address", content);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeAddress, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("地址不能为空");
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(changeAddress)) {
			if (dataString != null) {
				app.userVO.setAddress(et.getText().toString());
				finish();
			}
		}
		return true;
	}
}
