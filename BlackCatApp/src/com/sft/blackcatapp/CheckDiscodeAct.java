package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.UserVO;

import android.content.Intent;
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
		case R.id.act_check_discode_btn:
//			Toast("click");
			if(check()){
				request(phone,et.getText().toString());
			}
			break;
		case R.id.base_left_btn:
			finish();
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
//		LogUtil.print("request--->"+code);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", mobile);
		paramMap.put("couponcode", code);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(CODE, this, Config.IP
				+ "api/v1/system/verifyactivitycoupon", paramMap, 10000,
				headerMap);
	}
	//json={"type":1,"msg":"","data":{"_id":"56adf1e3ec71a91094d1da8a","couponmoney":800,
//	"couponcode":"13121646597","mobile":"13121646597","state":1,"endtime":"2016-08-17T06:04:52.005Z",
//	"createtime":"2016-01-31T11:13:10.383Z"}} type= code


	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		super.doCallBack(type, jsonString);
		if(type.equals(CODE)){
//			Toast(result+"doCallBack"+jsonString);
			if(result.equals("1")){//成功，获取其中的数据
				Toast("验证成功");
//				data
				// 保存数据
				try {
					DisCode bean = JSONUtil.toJavaBean(DisCode.class, data);
					Intent i = new Intent(CheckDiscodeAct.this,ConfirmOrderActivity.class);
					i.putExtra("id", bean._id);
					i.putExtra("money", bean.couponmoney);
					i.putExtra("name", bean.couponcode);
					setResult(3, i);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 干掉界面
				
			}else{
				if(msg!=null)
					Toast(msg);
			}
		}
		return false;
	}

	@Override
	public void doException(String type, Exception e, int code) {
//		Toast("doException"+type);
		super.doException(type, e, code);
	}
	
	class DisCode{
		public String _id;
		public String couponmoney;
		public String couponcode;
		public String mobile;
		public String state;
		public String endtime;
		public String createtime;
	}
	
	
}
