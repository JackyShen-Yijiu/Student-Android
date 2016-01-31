package com.sft.blackcatapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sft.vo.ClassVO;

public class ConfirmOrderActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_coupon_show, tvGoodName, tvGoodPrice, tvYcode;

	private TextView tvShoudPay, tvReallyPay;

	private ClassVO classe;

	/**
	 * 填写的 phone 号码
	 */
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addView(R.layout.activity_confirm_order);
		setBg(getResources().getColor(R.color.white));
		
		initView();
		Listenner();
	}

	private void initView() {
		classe = (ClassVO) getIntent().getSerializableExtra("class");
		String schoolName = getIntent().getStringExtra("schoolName");
		phone = getIntent().getStringExtra("phone");

		tv_coupon_show = (TextView) findViewById(R.id.tv_coupon_show);
		tvGoodName = (TextView) findViewById(R.id.tv_goods_show);
		tvGoodPrice = (TextView) findViewById(R.id.tv_goods_money_show);
		// tvDiscode = (TextView) findViewById(R.id.tv_goods_show);
		tvYcode = (TextView) findViewById(R.id.tv_discount_show);
		tvShoudPay = (TextView) findViewById(R.id.textView_money_should);
		tvReallyPay = (TextView) findViewById(R.id.textView_money_cale);

		tvReallyPay.setText(classe.getOnsaleprice() + "元");
		tvShoudPay.setText(classe.getPrice() + "元(" + schoolName + ")");

	}

	private void Listenner() {
		tv_coupon_show.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_coupon_show:
			Intent intent = new Intent(ConfirmOrderActivity.this,
					CheckDiscodeAct.class);
			intent.putExtra("phone", phone);
			startActivityForResult(intent, 3);
			break;
		case R.id.base_left_btn:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && data != null) {
			data.getStringExtra("name");
			data.getStringExtra("money");
			data.getStringExtra("id");
			tv_coupon_show.setText(data.getStringExtra("name"));
		}

	}

}
