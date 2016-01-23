package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;

import android.os.Bundle;
import android.view.View;

/**
 * 测试支付
 * @author sun  2016-1-23 下午3:23:52
 *
 */
public class TestPayAct extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pay_test);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
	}
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.button1://请求信的id
			
			break;
		case R.id.button2://发起支付请求，到第三方sdk
			break;
		}
	}

	private void getOrderId() {

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userId", "");
		paramMap.put("content", "1");
		paramMap.put("password", "");
		HttpSendUtils.httpPostSend("1", this, Config.IP
				+ "api/v1/userinfo/userlogin", paramMap);

	}
	
	private void doPay(){
		
	}
	

}
