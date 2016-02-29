package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sft.common.Config;
import com.sft.util.CommonUtil;

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
		Intent intent = null;
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;

		case R.id.learn_car_cheats_backing:
			intent = new Intent(this, YiBuIntroduceActivity.class);
			intent.putExtra("url", Config.CHEATS_BACKING);
			intent.putExtra("cheatname",
					CommonUtil.getString(this, R.string.cheats_backing));
			break;
		case R.id.learn_car_cheats_parking:
			intent = new Intent(this, YiBuIntroduceActivity.class);
			intent.putExtra("url", Config.CHEATS_PARKING);
			intent.putExtra("cheatname",
					CommonUtil.getString(this, R.string.cheats_parking));
			break;
		case R.id.learn_car_cheats_start:
			intent = new Intent(this, YiBuIntroduceActivity.class);
			intent.putExtra("url", Config.CHEATS_START);
			intent.putExtra("cheatname",
					CommonUtil.getString(this, R.string.cheats_start));
			break;
		case R.id.learn_car_cheats_turn_corner:
			intent = new Intent(this, YiBuIntroduceActivity.class);
			intent.putExtra("url", Config.CHEATS_TURN_CORNER);
			intent.putExtra("cheatname",
					CommonUtil.getString(this, R.string.cheats_turn_corner));
			break;
		case R.id.learn_car_cheats_s_curve:
			intent = new Intent(this, YiBuIntroduceActivity.class);
			intent.putExtra("url", Config.CHEATS_S_CURVE);
			intent.putExtra("cheatname",
					CommonUtil.getString(this, R.string.cheats_s_curve));
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}
}
