package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.alipay.PayResult;
import com.sft.alipay.PayUtils;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.MyOrderVO;
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

	private SelectableRoundedImageView img;

	private TextView tvTitle, tvOrderName, tvPay1, tvPayMoney, tvTime, tvState;

	private TextView btn1, btn2;

	private String[] states = { "支付成功", "支付失败" };

	private PayOrderVO pay;
	/** 线上支付 线下支付 */
	private UserBaseStateVO baseStateVO;

	private LinearLayout llTop;

	private LinearLayout ll;

	private RelativeLayout errorRl;
	private TextView errorTv;

	private LinearLayout ll_main;

	private TextView tvNotPay;

	private MyOrderVO myOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.act_order_apply);
		initView();
		request();
		requestMyOrder();
		// getApplyState();
		obtainApplySuccessInfo();
	}

	private void initView() {

		setTitleText("报名信息");
		llTop = (LinearLayout) findViewById(R.id.item_order_top_ll);
		ll = (LinearLayout) findViewById(R.id.item_order);
		errorRl = (RelativeLayout) findViewById(R.id.error_rl);
		errorTv = (TextView) findViewById(R.id.error_tv);

		img = (SelectableRoundedImageView) findViewById(R.id.item_order_img);

		// img.setScaleType(ScaleType.CENTER_CROP);
		// img.setImageResource(R.drawable.default_small_pic);
		// img.setOval(true);
		ll_main = (LinearLayout) findViewById(R.id.ll_main);
		tvTitle = (TextView) findViewById(R.id.item_order_title);
		tvOrderName = (TextView) findViewById(R.id.item_order_left1);
		tvPay1 = (TextView) findViewById(R.id.item_order_right10);
		tvPayMoney = (TextView) findViewById(R.id.item_order_right11);
		tvTime = (TextView) findViewById(R.id.item_order_left2);
		tvState = (TextView) findViewById(R.id.item_order_right2);
		btn1 = (TextView) findViewById(R.id.item_order_button1);
		btn2 = (TextView) findViewById(R.id.item_order_button2);

		tvNotPay = (TextView) findViewById(R.id.act_order_apply_tv);

		btn1.setText("立即支付");
		btn2.setText(R.string.cancel_order);

		llTop.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);

		if (errorTv != null) {
			errorTv.setText(R.string.no_order_apply);
		}

	}

	private void setOffLine(SuccessVO successVO) {
		LogUtil.print("app--->" + app + "user::apply--》线下》" + successVO.paytypestatus);
		
		tvOrderName.setText(successVO.applyclasstypeinfo.name);
		tvPayMoney.setText("实付款:");
		tvPay1.setText("￥" + successVO.applyclasstypeinfo.price);
		tvTime.setText("报名时间:" + successVO.applytime);// UTC2LOC.instance.getDate(pay.creattime,
														// "yyyy-MM-dd HH:mm:ss")
		LogUtil.print("paytypestatus---->" + successVO.paytypestatus);
		tvNotPay.setVisibility(View.VISIBLE);
		if (successVO.paytype.equals("1")) {// 线下支付
			tvTitle.setText(successVO.applyschoolinfo.name + "(线下)");
			
			if (successVO.paytypestatus == 20) {// 申请成功
				tvState.setText("报名成功");
				tvNotPay.setText("订单已支付");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.GONE);
			} else if (successVO.paytypestatus == 0) {// 未支付
				tvState.setText("未支付");
				tvNotPay.setText("订单未支付");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.VISIBLE);
				// btn2.setText(R.string.cancel_order);
				btn1.setText(R.string.cancel_order);
//				ll.setOnClickListener(null);
			} else if (successVO.paytypestatus == 30) {// 支付失败
				tvState.setText("支付失败");
				tvNotPay.setText("订单支付失败");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.VISIBLE);
				// btn2.setText(R.string.cancel_order);
				btn1.setText(R.string.cancel_order);
//				ll.setOnClickListener(null);
			}

		} else {// 线上支付
			tvTitle.setText(successVO.applyschoolinfo.name + "(线上)");
			if (successVO.paytypestatus == 20) {// 申请成功
				tvState.setText("报名成功");
				tvNotPay.setText("订单已支付");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.GONE);
			} else if (successVO.paytypestatus == 0) {// 未支付
				tvState.setText("未支付");
				tvNotPay.setText("订单未支付");
				btn2.setVisibility(View.VISIBLE);
				btn1.setVisibility(View.VISIBLE);
				btn2.setText(R.string.cancel_order);
				btn1.setText("立即支付");
				ll.setOnClickListener(null);
			} else if (successVO.paytypestatus == 30) {// 支付失败
				tvState.setText("支付失败");
				tvNotPay.setText("订单支付失败");
				btn2.setVisibility(View.VISIBLE);
				btn1.setVisibility(View.VISIBLE);
				btn2.setText(R.string.cancel_order);
				btn1.setText("立即支付");
				ll.setOnClickListener(null);
			}
		}

		if (!TextUtils.isEmpty(successVO.schoollogoimg)) {
			LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) img
					.getLayoutParams();
			BitmapManager.INSTANCE.loadBitmap2(successVO.schoollogoimg, img,
					headParams.width, headParams.height);
		}

	}

	private void setMyOrder(MyOrderVO bean) {
		tvOrderName.setText(bean.applyclasstypeinfo.getName());
		tvPayMoney.setText("实付款:");
		tvPay1.setText("￥" + bean.applyclasstypeinfo.getPrice());
		tvTime.setText("报名时间:" + bean.applytime);

		if (bean.paytype == 1) {// 线下支付
			tvTitle.setText(bean.applyschoolinfo.getName() + "(线下)");
			if (bean.paytypestatus == 20) {// 申请成功
				tvState.setText("报名成功");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.GONE);
			} else if (bean.paytypestatus == 0) {// 未支付
				tvState.setText("未支付");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.VISIBLE);
				// btn2.setText(R.string.cancel_order);
				btn1.setText(R.string.cancel_order);
			} else if (bean.paytypestatus == 30) {// 支付失败
				tvState.setText("支付失败");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.VISIBLE);
				// btn2.setText(R.string.cancel_order);
				btn1.setText(R.string.cancel_order);
			}

		} else {// 线上支付
			tvTitle.setText(bean.applyschoolinfo.getName() + "(线上)");
			if (bean.paytypestatus == 20) {// 申请成功
				tvState.setText("报名成功");
				btn2.setVisibility(View.GONE);
				btn1.setVisibility(View.GONE);
			} else if (bean.paytypestatus == 0) {// 未支付
				tvState.setText("未支付");
				btn2.setVisibility(View.VISIBLE);
				btn1.setVisibility(View.VISIBLE);
				btn2.setText(R.string.cancel_order);
				btn1.setText("立即支付");
			} else if (bean.paytypestatus == 30) {// 支付失败
				tvState.setText("支付失败");
				btn2.setVisibility(View.VISIBLE);
				btn1.setVisibility(View.VISIBLE);
				btn2.setText(R.string.cancel_order);
				btn1.setText("立即支付");
			}
		}

		if (!TextUtils.isEmpty(bean.schoollogoimg)) {
			LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) img
					.getLayoutParams();
			BitmapManager.INSTANCE.loadBitmap2(bean.schoollogoimg, img,
					headParams.width, headParams.height);
		}
	}
	
	// 支付状态: 未支付成功: 不可以点击
	// 支付状态: 支付成功： 可以点击进入详情

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(1);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			setResult(1);
			finish();
			break;
		case R.id.item_order_button1:// 立即支付,右面
//			Toast("pay"+successVO.paytype);
			if (successVO.paytype.equals("1")) {// 线下支付，重新报名
				reEnroll();
			} else {// 立即支付

				if (pay != null) {
					Intent i = new Intent(OrderApplyAct.this,
							NewConfirmOrderActivity.class);
					i.putExtra("repay", true);
					i.putExtra("bean", pay);
					startActivity(i);

					String orderId = pay._id;
					String productName = pay.applyclasstypeinfo.name;
					String productDetail = pay.applyschoolinfo.getName();
					// + pay.applyclasstypeinfo.name
					// + pay.applyclasstypeinfo.price;

					String paymoney = pay.paymoney;
					PayUtils pay = new PayUtils();
					pay.setTradeNo(orderId);
					// paymoney = "0.01";
					// LogUtil.print("id::>"+orderId+"name:"+productName+"detail"+productDetail+"Money::>>"+paymoney);
					// pay.pay(this,mHandler,productName,productDetail,paymoney);
				}

			}
			break;
		case R.id.item_order_button2:// 重新报名
			reEnroll();
			break;
		case R.id.item_order_top_ll:// 订单详情
			if (successVO != null) {
				if (successVO.paytype.equals("1")) {// 线下
					Intent i = new Intent(OrderApplyAct.this,
							EnrollSuccessActivity.class);
					i.putExtra("isOnline", false);
					startActivity(i);
				} else {// 线上支付
					Intent i = new Intent(OrderApplyAct.this,
							EnrollSuccessActivity.class);
					i.putExtra("isOnline", true);
					i.putExtra("bean", successVO);
					i.putExtra("pay", pay);
					startActivity(i);
				}

			}

			break;
		}
		super.onClick(v);
	}

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// ConfirmOrderActivity.this.setResult(9,getIntent());
			// ConfirmOrderActivity.this.finish();

			LogUtil.print("msg;::::>>>" + msg.obj);
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
					app.userVO
							.setApplystate(EnrollResult.SUBJECT_ENROLL_SUCCESS
									.getValue());
					Toast.makeText(OrderApplyAct.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					btn1.setVisibility(View.GONE);
					btn2.setVisibility(View.GONE);
					// OrderApplyAct.this.setResult(9,getIntent());
					// OrderApplyAct.this.finish();

					// reLogin();
				} else {
					app.userVO.setApplystate(EnrollResult.SUBJECT_NONE
							.getValue());

					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(OrderApplyAct.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(OrderApplyAct.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(OrderApplyAct.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * 重新报名
	 */
	private void reEnroll() {
		obtainApplyFailInfo();
	}

	private void obtainApplyFailInfo() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils
				.httpGetSend("cancleorder", this, Config.IP
						+ "api/v1/userinfo/usercancelorder", paramMap, 10000,
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
				ll_main.setVisibility(View.VISIBLE);
				int length = dataArray.length();
				List<PayOrderVO> payList = new ArrayList<PayOrderVO>();
				for (int i = 0; i < length; i++) {
					PayOrderVO pay = JSONUtil.toJavaBean(PayOrderVO.class,
							dataArray.getJSONObject(i));
					if (!pay.userpaystate.equals("4")) {// 不是取消
						payList.add(pay);
					}
				}
				if (payList.size() > 0) {
					pay = payList.get(0);
					LogUtil.print("pay---->" + pay.applyschoolinfo.getAddress()
							+ "");
					tvPay1.setText("￥" + pay.paymoney);
					// setData(pay);
				} else {
					errorRl.setVisibility(View.VISIBLE);
				}
				//
				// LogUtil.print("order--size-->"+payList.size());

			} else if (type.equals("state")) {
				// }
			} else if (type.equals("applySchoolInfor")) {// 报名信息，，目的 获取线下报名的内容
				ll_main.setVisibility(View.VISIBLE);
				LogUtil.print("applySchoolinfor-->" + jsonString);
				if (data != null) {
					successVO = JSONUtil.toJavaBean(SuccessVO.class, data);
					setOffLine(successVO);
				}
				if (result.equals("0")) {// 没有数据
					errorRl.setVisibility(View.VISIBLE);
					errorTv.setText(msg);
					ll.setVisibility(View.GONE);
					tvNotPay.setVisibility(View.GONE);
				} else {
					tvNotPay.setVisibility(View.VISIBLE);
					errorRl.setVisibility(View.GONE);
					ll.setVisibility(View.VISIBLE);
				}
			} else if (type.equals("cancleorder")) {
				MainActivity.TARGET_TAB = MainActivity.TAB_APPLY;
				setResult(9);
				finish();
			} else if (type.equals("myorder")) {// 我的订单 详细
				if (data != null) {
					myOrder = JSONUtil.toJavaBean(MyOrderVO.class, data);
					setMyOrder(myOrder);
				}
				if (result.equals("0")) {// 没有数据
					errorRl.setVisibility(View.VISIBLE);
					errorTv.setText(msg);
					ll.setVisibility(View.GONE);
					tvNotPay.setVisibility(View.GONE);
				} else {
					tvNotPay.setVisibility(View.VISIBLE);
					errorRl.setVisibility(View.GONE);
					ll.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.doCallBack(type, jsonString);
	}

	SuccessVO successVO;

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
	 * 获取我的订单
	 */
	private void requestMyOrder() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("myorder", this, Config.IP
				+ "api/v1/userinfo/getmyorder", paramMap, 10000, headerMap);
	}

	/**
	 * get /user 订单状态
	 */
	private void getApplyState() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("state", this, Config.IP
				+ "api/v1/userinfo/getmyapplystate", paramsMap, 10000,
				headerMap);
	}

	// private void setData(PayOrderVO pay) {
	//
	// // tvOrderName.setText(pay.applyclasstypeinfo.name);
	// // tvPayMoney.setText("实付款:");
	// // tvPay1.setText("￥"+pay.paymoney);
	// // tvTime.setText("报名时间:"+UTC2LOC.instance.getDate(pay.creattime,
	// "yyyy-MM-dd HH:mm:ss"));
	//
	//
	// if(pay.userpaystate.equals("2")){
	// tvState.setText(states[0]);
	// btn1.setVisibility(View.GONE);
	// btn2.setVisibility(View.GONE);
	// }else {//支付失败
	// tvState.setText(states[1]);
	// btn1.setVisibility(View.VISIBLE);
	// btn2.setVisibility(View.VISIBLE);
	// }
	//
	// tvTitle.setText(pay.applyschoolinfo.getName());
	// if(null!=baseStateVO){
	// if(baseStateVO.paytype.equals("1")){//线下支付
	// tvTitle.setText(pay.applyschoolinfo.getName()+"(线下)");
	// }else{
	// tvTitle.setText(pay.applyschoolinfo.getName()+"(线上)");
	// }
	// }
	// LogUtil.print("iamge---》"+pay.applyschoolinfo.getLogoimg().getOriginalpic());
	// LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) img
	// .getLayoutParams();
	// BitmapManager.INSTANCE.loadBitmap2(pay.applyschoolinfo.getLogoimg().getOriginalpic(),
	// img,
	// headParams.width, headParams.height);
	//
	//
	// }

	/**
	 * 线下 。线上
	 * 
	 * @param baseStateVO
	 */
	// private void setDataOffLine(UserBaseStateVO baseStateVO){
	// if(baseStateVO.paytype.equals("1")){//线下支付
	// if(null!=pay){//显示线下支付
	// tvTitle.setText(pay.applyschoolinfo.getName()+"(线下)");
	// }
	//
	// if(baseStateVO.getApplystate().equals("2")){//申请成功
	// tvState.setText("报名成功");
	// btn2.setVisibility(View.GONE);
	// btn1.setVisibility(View.GONE);
	// }else{//未验证，可以重新报名
	// tvState.setText("未验证");
	// btn2.setVisibility(View.GONE);
	// btn1.setVisibility(View.VISIBLE);
	// btn1.setText(R.string.cancel_order);
	// }
	// }else{//线上支付
	// if(null!=pay){//显示线下支付
	// tvTitle.setText(pay.applyschoolinfo.getName()+"(线上)");
	// }
	// }
	//
	// //
	// LogUtil.print("iamge---》"+pay.applyschoolinfo.getLogoimg().getOriginalpic());
	// // LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) img
	// // .getLayoutParams();
	// //
	// BitmapManager.INSTANCE.loadBitmap2(baseStateVO.getApplyinfo().applyschoolinfo.getLogoimg().getOriginalpic(),
	// img,
	// // headParams.width, headParams.height);
	// }

}
