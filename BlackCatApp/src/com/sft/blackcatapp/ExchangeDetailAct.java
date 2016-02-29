package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sft.vo.ExchangeGoodOrderVO;
import com.sft.vo.ExchangeOrderItemVO;

public class ExchangeDetailAct extends BaseActivity{

	private TextView tvGoodName,tvJifen,tvExpress,tvAddress,tvReceiver,tvPhone,
	tvTime,tvType,tvMoney;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.act_exchange_detail);
		initView();
		initData();
	}

	private void initView() {
		tvGoodName = (TextView) findViewById(R.id.act_exchange_detail_product);
		tvJifen = (TextView) findViewById(R.id.act_exchange_detail_jifen);
		tvExpress = (TextView) findViewById(R.id.act_exchange_detail_express);
		tvAddress = (TextView) findViewById(R.id.act_exchange_detail_address);
		tvReceiver = (TextView) findViewById(R.id.act_exchange_detail_receivername);
		tvPhone = (TextView) findViewById(R.id.act_exchange_detail_phone);
		tvTime = (TextView) findViewById(R.id.act_exchange_detail_time);
		tvType = (TextView) findViewById(R.id.act_exchange_detail_type);
		tvMoney = (TextView) findViewById(R.id.act_exchange_detail_yb);
		
		setTitleText("兑换详情");
	}
	
	private void initData(){
		ExchangeOrderItemVO item = (ExchangeOrderItemVO) getIntent().getSerializableExtra("bean");
//		int po = getIntent().getIntExtra("po", 0);
//		ExchangeOrderItemVO item = bean.ordrelist.get(po);
		if(item!=null){
			tvGoodName.setText(item.productname);
			tvJifen.setText("￥"+item.productprice+"YB");
			tvExpress.setText("未知");
			tvAddress.setText(item.merchantaddress);
			tvReceiver.setText(item.receivername);
			tvPhone.setText(item.mobile);
			tvTime.setText(item.createtime);
			tvType.setText("积分兑换？");
			tvMoney.setText(item.productprice+"YB");
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.base_left_btn:
			finish();
			break;
		}
		super.onClick(v);
	}
	
	

	
}
