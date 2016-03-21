package com.sft.blackcatapp;

import android.os.Bundle;

import com.jzjf.app.R;

public class CaptureActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_about_us);
	}
}
