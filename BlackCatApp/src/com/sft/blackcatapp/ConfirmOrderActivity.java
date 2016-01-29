package com.sft.blackcatapp;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ConfirmOrderActivity extends Activity implements OnClickListener {

	private TextView tv_coupon_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_order);

		initView();
		Listenner();
	}

	private void initView() {
		tv_coupon_show = (TextView) findViewById(R.id.tv_coupon_show);

	}

	private void Listenner() {
		tv_coupon_show.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_coupon_show:

			break;

		default:
			break;
		}
	}

}
