package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * 报名成功提示界面
 * 
 * @author Administrator
 * 
 */
public class TestingCommit extends BaseActivity {

	private Button button_sus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.testing_commit);
		initView();
		initData();
		setListener();
	}

	private void initData() {

	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		showTitlebarBtn(0);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.finish);
		setTitleText(R.string.enroll);
		button_sus = (Button) findViewById(R.id.button_sus);
	}

	private void setListener() {
		button_sus.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
		case R.id.base_right_tv:
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
			finish();
			break;
		case R.id.button_sus:
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
			finish();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
		}
		return super.onKeyDown(keyCode, event);
	}
}
