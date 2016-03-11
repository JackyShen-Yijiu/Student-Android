package com.sft.blackcatapp;

import android.os.Bundle;

import com.jzjf.app.R;

public class SussessOrderActvity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.fragment_coach_or_school);
		initView();
	}

	private void initView() {
		setTitleText("完成订单");
	}
}
