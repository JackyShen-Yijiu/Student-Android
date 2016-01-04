package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

/**
 * 修改签名
 * 
 * @author Administrator
 * 
 */
public class ChangeSignActivity extends BaseActivity {

	private EditText et;
	private Button btn;
	private TextView mTextView;

	private static final String changeSign = "sign";
	private static final String changeName = "name";
	private static final String changeNickName = "nickname";

	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_callback);
		initView();
		setListener();
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		btn = (Button) findViewById(R.id.callback_btn);
		et = (EditText) findViewById(R.id.callback_et);
		type = getIntent().getStringExtra("type");
		Log.v(type, type);
		mTextView = (TextView) findViewById(R.id.text_dialog);

		if (type.equals(changeSign)) {
			setTitleText(R.string.change_personalized);
		}
		if (type.equals(changeName)) {
			setTitleText(R.string.change_name);
		}
		if (type.equals(changeNickName)) {
			setTitleText(R.string.change_nickname);
		}

	}

	private void setListener() {
		btn.setOnClickListener(this);
		et.addTextChangedListener(mTextWatcher);
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void beforeTextChanged(CharSequence s, int arg1, int arg2,
				int arg3) {
			temp = s;
		}

		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
			mTextView.setText(s);
		}

		@Override
		public void afterTextChanged(Editable s) {
			editStart = et.getSelectionStart();
			editEnd = et.getSelectionEnd();
			if (temp.length() > 20) {
				// Toast.makeText(ChangeSignActivity.this, "你输入的字数已经超过了限制！",
				// Toast.LENGTH_SHORT).show();
				ZProgressHUD.getInstance(ChangeSignActivity.this).show();
				ZProgressHUD.getInstance(ChangeSignActivity.this)
						.dismissWithFailure("你输入的字数已经超过了限制！");
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				et.setText(s);
				et.setSelection(tempSelection);
			}
		}
	};

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
			if (type.equals("sign")) {
				changeSign();
			} else if (type.equals("name")) {
				changeName();
			} else {
				changeNickName();
			}
			break;
		}
	}

	private void changeSign() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("signature", content);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeSign, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("签名不能为空");
		}
	}

	private void changeName() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("name", content);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeName, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("签名不能为空");
		}
	}

	private void changeNickName() {
		String content = et.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("nickname", content);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeNickName, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("签名不能为空");
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(changeSign)) {
			if (dataString != null) {
				app.userVO.setSignature(et.getText().toString());
				finish();
			}
		} else if (type.equals(changeName)) {
			if (dataString != null) {
				app.userVO.setName(et.getText().toString());
				finish();
			}
		} else if (type.equals(changeNickName)) {
			if (dataString != null) {
				app.userVO.setNickname(et.getText().toString());
				finish();
			}
		}
		return true;
	}
}
