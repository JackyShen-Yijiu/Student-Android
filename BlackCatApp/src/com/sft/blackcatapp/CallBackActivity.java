package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

/**
 * 反馈
 * 
 * @author Administrator
 * 
 */
public class CallBackActivity extends BaseActivity {

	private EditText et;
	private Button btn;

	private static final String callback = "callback";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_callback);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.callback);

		btn = (Button) findViewById(R.id.callback_btn);
		et = (EditText) findViewById(R.id.callback_et);

		et.setHint(setHint(R.string.opinions_suggestions));
	}

	private void setListener() {
		btn.setOnClickListener(this);
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
		case R.id.callback_btn:
			callBack();
			break;
		}
	}

	private void callBack() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content.trim())) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("feedbackmessage", content);
			ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获取网络的状态信息，有下面三种方式
			NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
			String networkName = networkInfo.getTypeName();
			if (networkName.equalsIgnoreCase("MOBILE")) {
				networkName += networkInfo.getExtraInfo();
			}
			paramMap.put("network", networkName);
			paramMap.put("resolution", screenWidth + "*" + screenHeight);
			paramMap.put("appversion", util.getAppVersion());
			paramMap.put("mobileversion", VERSION.RELEASE);
			HttpSendUtils.httpPostSend(callback, this, Config.IP
					+ "api/v1/userfeedback", paramMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("反馈内容不能为空");
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(callback)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("反馈成功");
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
