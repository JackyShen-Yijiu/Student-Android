package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;

public class HelpNewcomerActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.setting_help_newcomer);
		setTitleText(R.string.help_newcomer);
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
