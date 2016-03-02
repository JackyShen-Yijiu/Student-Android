package com.sft.blackcatapp.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzjf.app.R;

public class MainScreenTab extends RelativeLayout {

	TextView mTextView;
	View mRedPointView;

	// 不会调用
	public MainScreenTab(Context context) {
		super(context);
		initView();
	}

	public MainScreenTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		inflate(getContext(), R.layout.layout_tab_indicator_internal, this);
		mTextView = (TextView) findViewById(R.id.tab_indicator_title);
		mRedPointView = findViewById(R.id.tab_indicator_notifaciton_redpoint);
	}
}
