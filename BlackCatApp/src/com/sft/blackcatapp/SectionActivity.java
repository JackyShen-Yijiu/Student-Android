package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;

import com.jzjf.app.R;

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
		}
	}

}
