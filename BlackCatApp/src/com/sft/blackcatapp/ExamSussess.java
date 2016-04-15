package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jzjf.app.R;

public class ExamSussess extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.exam_sussess);
		setTitleText(R.string.exam_result);
		TextView code = (TextView) findViewById(R.id.exam_code);
		int score = getIntent().getIntExtra("score", 0);
		code.setText("您的成绩"+score+"分");
		
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
