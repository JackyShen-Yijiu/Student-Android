package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jzjf.app.R;
import com.sft.fragment.ExciseFragment;

/**
 * 章节选择
 * 
 * @author Administrator
 * 
 */
public class SectionActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.section_select);
		setTitleText(R.string.section);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.section_test:
		case R.id.section_test1:
		case R.id.section_test2:
		case R.id.section_test4:
			startActivity(new Intent(SectionActivity.this,ExerciseOrderAct.class));
			break;
		}
	}

}
