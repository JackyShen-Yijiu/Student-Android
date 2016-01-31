package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * 验证 折扣券
 * @author sun  2016-1-30 上午9:37:07
 *
 */
public class CheckDiscodeAct extends BaseActivity{

	private EditText et;
	
	private final String CODE = "code";
	
	private String phone = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.act_check_discode);
		addView(R.layout.act_check_discode);
		showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN
				);// BaseActivity.SHOW_RIGHT_BTN
		setTitleText("活动兑换码");
		et = (EditText) findViewById(R.id.act_check_discode_et);
		phone = getIntent().getStringExtra("phone");
	}

	public void onClick(View v){
		switch(v.getId()){
		case R.id.act_check_discode_et:
			if(check()){
				request(phone,et.getText().toString());
			}
			break;
		}
	}
	
	private boolean check(){
		if(TextUtils.isEmpty(et.getText().toString())){
			return false;
		}
		return true;
	}
	
	private void request(String mobile,String code){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", mobile);
		paramMap.put("couponcode", code);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(CODE, this, Config.IP
				+ "api/v1/system/verifyactivitycoupon ", paramMap, 10000,
				headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if(type.equals(CODE)){
			if(msg.equals("")){//成功，获取其中的数据
				
			}
		}
		return super.doCallBack(type, jsonString);
	}
	
	
	
}
