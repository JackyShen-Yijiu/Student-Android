package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;

public class HelpGuideActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.setting_help_guide);
		setTitleText(R.string.help_guide);
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
