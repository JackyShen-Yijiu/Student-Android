package com.sft.blackcatapp;

import android.os.Bundle;

/**
 * 课件播放
 * @author Administrator
 *
 */
public class CoursePlayActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}
}
