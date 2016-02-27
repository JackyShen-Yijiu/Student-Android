package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.vo.PayOrderVO;
import com.sft.vo.SuccessVO;
import com.sft.vo.UserBaseStateVO;

/**
 * 报名订单
 * 
 * @author sun 2016-2-25 下午5:05:18
 * 
 */
public class OrderApplyAct extends BaseActivity {

	private ImageView img;

	private TextView tvTitle, tvOrderName, tvPay1, tvPayMoney, tvTime, tvState;
	
	private Button btn1,btn2;
	
	private String[] states = {"支付成功","支付失败"};
	
	private PayOrderVO pay;
	/**线上支付 线下支付*/
	private UserBaseStateVO baseStateVO;
	
	private LinearLayout llTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_order);
		initView();
//		request();
//		getApplyState();
		obtainApplySuccessInfo();
	}

	private void initView() {
		llTop = (LinearLayout) findViewById(R.id.item_order_top_ll);
		tvTitle = (TextView) findViewById(R.id.item_order_title);
		tvOrderName = (TextView) findViewById(R.id.item_order_left1);
		tvPay1 = (TextView) findViewById(R.id.item_order_right10);
		tvPayMoney = (TextView) findViewById(R.id.item_order_right11);
		tvTime = (TextView) findViewById(R.id.item_order_left2);
		tvState = (TextView) findViewById(R.id.item_order_right2);
		btn1 = (Button) findViewById(R.id.item_order_button1);
		btn2 = (Button) findViewById(R.id.item_order_button2);
		btn1.setText("立即支付");
		btn2.setText("重新报名");
		
		llTop.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		
	}
	
	private void setData(PayOrderVO pay) {
		
//		tvOrderName.setText(pay.applyclasstypeinfo.name);
//		tvPayMoney.setText("实付款:");
//		tvPay1.setText("￥"+pay.paymoney);
//		tvTime.setText("报名时间:"+UTC2LOC.instance.getDate(pay.creattime, "yyyy-MM-dd HH:mm:ss"));
		
		
		if(pay.userpaystate.equals("2")){
			tvState.setText(states[0]);
			btn1.setVisibility(View.GONE);
			btn2.setVisibility(View.GONE);
		}else {//支付失败
			tvState.setText(states[1]);
			btn1.setVisibility(View.VISIBLE);
			btn2.setVisibility(View.VISIBLE);
		}
		
		tvTitle.setText(pay.applyschoolinfo.getName());
		if(null!=baseStateVO){
			if(baseStateVO.paytype.equals("1")){//线下支付
				tvTitle.setText(pay.applyschoolinfo.getName()+"(线下)");
			}else{
				tvTitle.setText(pay.applyschoolinfo.getName()+"(线上)");
			}
		}
	

	}
	
	/**
	 * 线下 。线上
	 * @param baseStateVO
	 */
	private void setDataOffLine(UserBaseStateVO baseStateVO){
		if(baseStateVO.paytype.equals("1")){//线下支付
			if(null!=pay){//显示线下支付
				tvTitle.setText(pay.applyschoolinfo.getName()+"(线下)");
			}
			
			if(baseStateVO.getApplystate().equals("2")){//申请成功
				tvState.setText("报名成功");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.GONE);
			}else{//未验证，可以重新报名
				tvState.setText("未验证");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.VISIBLE);
				btn1.setText("重新报名");
			}
		}else{//线上支付
			if(null!=pay){//显示线下支付
				tvTitle.setText(pay.applyschoolinfo.getName()+"(线上)");
			}
		}
	}
	
	private void setOffLine(SuccessVO successVO){
//		successVO.applyschoolinfo.name
//		successVO.applyclasstypeinfo.name;  //课程名称
//		successVO.applyclasstypeinfo.price;
//		successVO.applytime;// 报名时间
//		successVO.applystate //申请状态
//		successVO.applyclasstypeinfo.paytypestatus;//支付状态
//		successVO.applyclasstypeinfo.paytype  1 线下支付  2线上支付
		
		
		tvOrderName.setText(successVO.applyclasstypeinfo.name);
		tvPayMoney.setText("实付款:");
		tvPay1.setText("￥"+successVO.applyclasstypeinfo.price);
		tvTime.setText("报名时间:"+successVO.applytime);//UTC2LOC.instance.getDate(pay.creattime, "yyyy-MM-dd HH:mm:ss")
		LogUtil.print("paytype---->" + successVO.paytype);
		if(successVO.paytype.equals("1")){//线下支付
			tvTitle.setText(successVO.applyschoolinfo.name+"(线下)");
		}else{//线上支付
			tvTitle.setText(successVO.applyschoolinfo.name +"(线上)");
		}
		
		if(successVO.paytypestatus==20){//申请成功
			tvState.setText("报名成功");
			btn2.setVisibility(View.GONE);
			btn1.setVisibility(View.GONE);
		}else if(successVO.paytypestatus == 0){//未支付
			tvState.setText("未支付");
			btn2.setVisibility(View.GONE);
			btn1.setVisibility(View.VISIBLE);
			btn1.setText("重新报名");
		}else if(successVO.paytypestatus == 30){//支付失败
			tvState.setText("支付失败");
			btn2.setVisibility(View.GONE);
			btn1.setVisibility(View.VISIBLE);
			btn1.setText("重新报名");
		}
		
	}
	
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.item_order_button1://立即支付,右面
			if(successVO.paytype.equals("1")){//线下支付，重新报名
				reEnroll();
			}else{//立即支付
				
			}
			break;
		case R.id.item_order_button2://重新报名
			reEnroll();
			break;
		case R.id.item_order_top_ll://订单详情
			if(successVO!=null){
				if(successVO.paytype.equals("1")){//线下
					
					startActivity(new Intent(OrderApplyAct.this,EnrollSuccessActivity.class));
				}else{//线上支付
					Intent i = new Intent(OrderApplyAct.this,OrderDetailActivity.class);
					i.putExtra("bean", successVO);
					startActivity(i);
				}
				
			}
			
			
			break;
		}
		super.onClick(v);
	}
	/**
	 * 重新报名
	 */
	private void reEnroll(){
		setResult(9);
		finish();
	}

	private void request() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("orderstate", "-1");// 订单的状态 // 0 订单生成 2 支付成功 3 支付失败 4 订单取消
										// -1 全部(未支付的订单)

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("notPay", this, Config.IP
				+ "api/v1/userinfo/getmypayorder", paramMap, 10000, headerMap);
	}
	/**
	 * 订单状态
	 */
	private void getApplyState(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("state", this, Config.IP
				+ "api/v1/userinfo/getmyapplystate", paramsMap, 10000,
				headerMap);
	}
	
	private void obtainApplySuccessInfo() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("applySchoolInfor", this, Config.IP
				+ "api/v1/userinfo/getapplyschoolinfo", paramMap, 10000,
				headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals("notPay")) {// 未支付的订单

				int length = dataArray.length();
				List<PayOrderVO> payList = new ArrayList<PayOrderVO>();
				for (int i = 0; i < length; i++) {
					PayOrderVO pay = JSONUtil.toJavaBean(PayOrderVO.class,
							dataArray.getJSONObject(i));
					if(!pay.userpaystate.equals("4")){//不是取消
						payList.add(pay);
					}
				}
				if(payList.size()>0){
					pay = payList.get(0);
					setData(pay);
				}
				
				LogUtil.print("order--size-->"+payList.size());
				
				
			}else if(type.equals("state")){
				if (data != null) {
					UserBaseStateVO baseStateVO = JSONUtil.toJavaBean(
							UserBaseStateVO.class, data);
					if (!baseStateVO.getApplystate().equals(
							app.userVO.getApplystate())) {
						this.baseStateVO = baseStateVO;
						
						app.userVO.setApplystate(baseStateVO.getApplystate());
						setDataOffLine(baseStateVO);
						
					}
				}
			}else if(type.equals("applySchoolInfor")){//报名信息，，目的 获取线下报名的内容
				LogUtil.print("applySchoolinfor-->"+jsonString);
				if (data != null) {
					successVO = JSONUtil.toJavaBean(SuccessVO.class,
							data);
					setOffLine(successVO);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return super.doCallBack(type, jsonString);
	}

	SuccessVO successVO ;

}
