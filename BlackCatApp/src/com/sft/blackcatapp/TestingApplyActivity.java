package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 验证报名信息
 * 
 * @author Administrator
 * 
 */
public class TestingApplyActivity extends BaseActivity implements
		OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.testing_apply);
		initView();
		Listener();
	}

	private void initView() {
		setTitleText(R.string.testing_person_center);
	}

	private void Listener() {

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
