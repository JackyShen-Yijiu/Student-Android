package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sft.viewutil.StudyItemLayout;

public class SettingHelp extends BaseActivity implements OnClickListener {

	private StudyItemLayout help_guide, help_problem, help_exam, help_newcomer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.setting_help);

		initView();
	}

	private void initView() {
		help_guide = (StudyItemLayout) findViewById(R.id.help_guide);
		help_exam = (StudyItemLayout) findViewById(R.id.help_exam);
		help_newcomer = (StudyItemLayout) findViewById(R.id.help_newcomer);
		help_problem = (StudyItemLayout) findViewById(R.id.help_problem);

		help_guide.setOnClickListener(this);
		help_exam.setOnClickListener(this);
		help_newcomer.setOnClickListener(this);
		help_problem.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.help_guide:
			break;
		case R.id.help_exam:
			break;
		case R.id.help_newcomer:
			break;
		case R.id.help_problem:
			break;
		}
	}
}