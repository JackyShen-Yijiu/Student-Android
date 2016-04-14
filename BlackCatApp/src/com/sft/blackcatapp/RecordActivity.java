package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jzjf.app.R;

public class RecordActivity extends BaseActivity {

	private ListView recordlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.record_juan);
		setTitleText(R.string.user_record);
		initView();
	}

	private void initView() {
		recordlist = (ListView) findViewById(R.id.record_list);
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
