package com.sft.blackcatapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sft.vo.ServerClassList;

public class ConfirmOrderActivity extends Activity implements OnClickListener {

	private TextView tv_coupon_show,tvGoodName,tvGoodPrice,tvYcode;
	
	private TextView tvShoudPay,tvReallyPay;
	
	private ServerClassList classe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_order);

		initView();
		Listenner();
	}

	private void initView() {
		classe = (ServerClassList) getIntent().getSerializableExtra("class");
		String schoolName = getIntent().getStringExtra("schoolName");
		
		tv_coupon_show = (TextView) findViewById(R.id.tv_coupon_show);
		tvGoodName = (TextView) findViewById(R.id.tv_goods_show);
		tvGoodPrice = (TextView) findViewById(R.id.tv_goods_money_show);
//		tvDiscode = (TextView) findViewById(R.id.tv_goods_show);
		tvYcode = (TextView) findViewById(R.id.tv_discount_show);
		tvShoudPay = (TextView) findViewById(R.id.textView_money_should);
		tvReallyPay = (TextView) findViewById(R.id.textView_money_cale);
		
		
		tvReallyPay.setText(classe.getOnsaleprice()+"元");
		tvShoudPay.setText(classe.getPrice()+"元("+schoolName+")");
		
	}

	private void Listenner() {
		tv_coupon_show.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_coupon_show:
			Intent intent = new Intent(ConfirmOrderActivity.this,CheckDiscodeAct.class);
			startActivityForResult(intent, 3);	
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 3 && data!=null){
			data.getStringExtra("name");
			data.getStringExtra("money");
			data.getStringExtra("id");
			tv_coupon_show.setText(data.getStringExtra("name"));
		}
		
		
	}
	
	

}
