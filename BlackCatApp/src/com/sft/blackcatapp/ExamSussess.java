package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;

import com.jzjf.app.R;

public class ExamSussess extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.setting_help_problem);
		setTitleText(R.string.exam_result);
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
