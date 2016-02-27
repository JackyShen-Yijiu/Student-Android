package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;

public class LearnCarCheatsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_learn_car_cheats);
		initView();
	}

	private void initView() {
		setTitleText(R.string.learn_car_cheats);

		findViewById(R.id.learn_car_cheats_backing).setOnClickListener(this);
		findViewById(R.id.learn_car_cheats_parking).setOnClickListener(this);
		findViewById(R.id.learn_car_cheats_start).setOnClickListener(this);
		findViewById(R.id.learn_car_cheats_turn_corner)
				.setOnClickListener(this);
		findViewById(R.id.learn_car_cheats_s_curve).setOnClickListener(this);
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

		case R.id.learn_car_cheats_backing:
			break;
		case R.id.learn_car_cheats_parking:
			break;
		case R.id.learn_car_cheats_start:
			break;
		case R.id.learn_car_cheats_turn_corner:
			break;
		case R.id.learn_car_cheats_s_curve:
			break;
		}
	}
}
