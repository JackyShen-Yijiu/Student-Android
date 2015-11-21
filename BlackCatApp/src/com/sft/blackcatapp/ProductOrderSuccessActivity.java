package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 购买成功
 *
 */
public class ProductOrderSuccessActivity extends BaseActivity {

	private Button returnBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_order_success);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		setTitleText("交易成功");

		returnBtn = (Button) findViewById(R.id.order_success_btn);
	}

	private void setListener() {
		returnBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.order_success_btn:
			finish();
			break;
		}
	}

	@Override
	public void finish() {
		sendBroadcast(new Intent(ProductOrderActivity.class.getName()).putExtra("finish", true));
		sendBroadcast(new Intent(ProductDetailActivity.class.getName()).putExtra("finish", true));
		super.finish();
	}
}
