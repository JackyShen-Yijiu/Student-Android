package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import com.jzjf.app.R;

public class HelpProblemActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.setting_help_problem);
		setTitleText(R.string.help_problem);
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
