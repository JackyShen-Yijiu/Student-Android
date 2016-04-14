package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jzjf.app.R;

public class IntegralHelpActivity extends BaseActivity {
	private TextView tv_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.integral_help);
		setTitleText(R.string.integra);
		tv_code = (TextView) findViewById(R.id.tv_code);
		tv_code.setText(app.currency);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		}
	}
}
