package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.alipay.PayResult;
import com.sft.alipay.PayUtils;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.PayOrderVO;
import com.sft.vo.SuccessVO;

public class OrderDetailActivity extends BaseActivity implements
		OnClickListener {
	private static final String notPay = "notPay";
	private TextView intent_code;
	private TextView indent_school;
	private TextView indent_class;
	private TextView indent_time;
	private TextView gross_money;
	private TextView favorable_money;
	private TextView actual_money;
	private TextView last_money;
	private Button button_commit;
	private TextView tvState;
	// private PayOrderVO payOrderVO;
	private SuccessVO bean;
	private PayOrderVO pay;
	private RelativeLayout rlPay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_indent);
		initView();
		initData();
		request();
	}

	private void initData() {
		bean = (SuccessVO) getIntent().getSerializableExtra("bean");
		pay = (PayOrderVO) getIntent().getSerializableExtra("pay");
		indent_school.setText(bean.applyschoolinfo.name);
		indent_class.setText(bean.applyclasstypeinfo.name);
		indent_time.setText(bean.applytime);

		gross_money.setText("￥" + bean.applyclasstypeinfo.price);

	}

	private void setData(PayOrderVO pay2) {
		intent_code.setText(pay2._id);
		favorable_money
				.setText("-￥" + pay2.discountmoney);
		actual_money.setText("￥" + pay2.paymoney);
		last_money.setText("需支付  " + pay2.paymoney + "元");
		LogUtil.print("money--->"+pay2.paymoney+"折扣"+pay2.discountmoney);
		if(pay2.userpaystate.equals("0")){
			tvState.setText("未支付");
		}else if(pay2.userpaystate.equals("2")){
			tvState.setText("支付成功");
			rlPay.setVisibility(View.GONE);
		}else if(pay2.userpaystate.equals("3")){
			tvState.setText("支付失败");
		}else{
			tvState.setText("订单取消");
		}
		
		
	}
	
	//线下支付： 可以点击，进入详情
	//线下支付： 不可以点击进入
	//未支付：     不可以进入详情，重新支付，进入
	
	// 未支付： 不可以点击进入
	// 已经支付：可以进入


	private void request() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("orderstate", "-1");// 订单的状态 // 0 订单生成 2 支付成功 3 支付失败 4 订单取消
										// -1 全部(未支付的订单)

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("notPay", this, Config.IP
				+ "api/v1/userinfo/getmypayorder", paramMap, 10000, headerMap);
//		Toast("orderDetail--->");
	}

	private void initView() {
		setTitleText(R.string.indent_detail);
		
		rlPay = (RelativeLayout) findViewById(R.id.act_intent_pay_rl);
		tvState = (TextView) findViewById(R.id.tv_intent_state);
		intent_code = (TextView) findViewById(R.id.tv_intent_code);
		indent_school = (TextView) findViewById(R.id.indent_school);
		indent_class = (TextView) findViewById(R.id.indent_class);
		indent_time = (TextView) findViewById(R.id.indent_time);

		gross_money = (TextView) findViewById(R.id.gross_money);
		favorable_money = (TextView) findViewById(R.id.favorable_money);
		actual_money = (TextView) findViewById(R.id.actual_money);
		last_money = (TextView) findViewById(R.id.last_money);

		button_commit = (Button) findViewById(R.id.button_commit);
		button_commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.button_commit://立即支付
			pay();
			break;
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if(super.doCallBack(type, jsonString)){
			return true;
		}
		if (type.equals("notPay")) {// 未支付的订单
			try {
				if(dataArray!=null){
					int length = dataArray.length();
					List<PayOrderVO> payList = new ArrayList<PayOrderVO>();
					for (int i = 0; i < length; i++) {
						PayOrderVO pay;
						pay = JSONUtil.toJavaBean(PayOrderVO.class,
								dataArray.getJSONObject(i));
						if(!pay.userpaystate.equals("4")){//订单 取消
							payList.add(pay);
						}
					}
					if (payList.size() > 0) {
						pay = payList.get(0);
						setData(pay);
					}
					LogUtil.print("order--size-->" + payList.size());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return true;
	}
	
	private void pay(){
		if(pay!=null){
			String orderId = pay._id;
			String productName = pay.applyclasstypeinfo.name;
			String productDetail = pay.applyschoolinfo.getName();
//					+ pay.applyclasstypeinfo.name
//					+ pay.applyclasstypeinfo.price;
			
			String paymoney = pay.paymoney;
			PayUtils pay = new PayUtils();
			pay.setTradeNo(orderId);
//			paymoney = "0.01";
			LogUtil.print("id::>"+orderId+"name:"+productName+"detail"+productDetail+"Money::>>"+paymoney);
			pay.pay(this,mHandler,productName,productDetail,paymoney);
		}
	}
	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
//			ConfirmOrderActivity.this.setResult(9,getIntent());
//			ConfirmOrderActivity.this.finish();
			
			LogUtil.print("msg;::::>>>"+msg.obj);
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					app.userVO.setApplystate(EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue());
					Toast.makeText(OrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					button_commit.setEnabled(false);
//					OrderDetailActivity.this.setResult(9,getIntent());
//					OrderDetailActivity.this.finish();
					
//					reLogin();
				} else {
					app.userVO.setApplystate(EnrollResult.SUBJECT_NONE.getValue());
					
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(OrderDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(OrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(OrderDetailActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	
}
