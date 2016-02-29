package com.sft.blackcatapp;

import android.content.Intent;
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

		setTitleText(R.string.setting_help);

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
		Intent intent = null;
		switch (v.getId()) {

		case R.id.base_left_btn:
			finish();
			break;
		// 使用指南
		case R.id.help_guide:
			intent = new Intent(this, HelpGuideActivity.class);
			break;
		// 理论考试
		case R.id.help_exam:
			intent = new Intent(this, HelpExamActivity.class);
			break;
		// 新手上路
		case R.id.help_newcomer:
			intent = new Intent(this, HelpNewcomerActivity.class);
			break;
		// 常见问题
		case R.id.help_problem:
			intent = new Intent(this, HelpProblemActivity.class);
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
}