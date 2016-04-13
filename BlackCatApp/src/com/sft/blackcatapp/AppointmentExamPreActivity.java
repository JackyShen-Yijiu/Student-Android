package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jzjf.app.R;

/**
 * 我要约考
 * 
 * @author Administrator
 * 
 */
public class AppointmentExamPreActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_appointment_exam_one);
		initView();
		initData();
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.school_appintment_exam:
			Intent intent = new Intent(this, AppointmentExamActivity.class);
			intent.putExtra("subjectid", getIntent()
					.getStringExtra("subjectid"));
			startActivity(intent);
			break;
		}
	}

	private void initView() {
		setTitleText(R.string.i_want_to_appointment_exam);
		findViewById(R.id.school_appintment_exam).setOnClickListener(this);
	}

}
