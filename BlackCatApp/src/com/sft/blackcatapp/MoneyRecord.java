package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;

import com.jzjf.app.R;

public class MoneyRecord extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.money_record);
		setTitleText(R.string.money_record);
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
