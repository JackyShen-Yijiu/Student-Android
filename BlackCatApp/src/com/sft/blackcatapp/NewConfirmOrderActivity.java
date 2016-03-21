package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.alipay.PayResult;
import com.sft.alipay.PayUtils;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.MyCodeVO;
import com.sft.vo.PayOrderVO;
import com.sft.vo.UserVO;
import com.sft.weixinpay.WeixinPay;
import com.tencent.mm.sdk.modelpay.PayReq;

public class NewConfirmOrderActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	private TextView tvGoodPrice;// tv_coupon_show
	/*** 最终支付金额 */
	private TextView tvReallyPay;
	/** 负数 */
	private TextView tvDiscodeBottom;
	/** 选择不同的支付模式 */
	private RadioButton rbAlipay, rbWeixinpay, rbXianXiapay;

	private ClassVO classe;
	/**
	 * 填写的 phone 号码
	 */
	private String phone;

	/** 本应价格 */
	private String price;
	/** 所有折扣后的价格 */
	private String paymoney;
	/** 折扣后， 不包含优惠券 */
	private String onSalePrice;

	private WeixinPay weixinPay;
	/** 0：支付成功 -1 等 失败 -2 用户取消 */
	public static int weixinPayState = 1;

	private final static String WEIXIN_PAY_INFOR = "getweixin_infor";

	private final static String CONFIRM_PAY_ORDER = "confirmpayorder";

	/**
	 * 商品价格:price 实付:paymoney (最后付款 -- 折扣券) 应付：onsaleprice（打折后的）
	 * 
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addView(R.layout.confirm_order_act);
		setBg(getResources().getColor(R.color.white));
		initView();
		Listenner();
	}

	String schoolName;
	/** 商品名字 一步驾校快班 价格， */
	String productName = "";
	String productDetail = "";

	private CoachVO coachVO = null;

	boolean repay = false;
	private MyCodeVO code;
	private ImageView iv_school;
	private TextView tv_school;
	private TextView tv_adress;
	private TextView tv_coach;
	private TextView tv_class;
	private String adress;
	private TextView discode;
	/** 渠道 */
	private EditText etFrom;
	private RelativeLayout rl_saoyisao;

	private void initView() {

		rl_saoyisao = (RelativeLayout) findViewById(R.id.rl_saoyisao);

		iv_school = (ImageView) findViewById(R.id.iv_school);
		tv_school = (TextView) findViewById(R.id.tv_school);
		tv_adress = (TextView) findViewById(R.id.tv_adress);
		tv_coach = (TextView) findViewById(R.id.tv_coach);
		tv_class = (TextView) findViewById(R.id.tv_class);
		discode = (TextView) findViewById(R.id.confirm_order_discode);
		etFrom = (EditText) findViewById(R.id.tv_inivit);

		weixinPay = new WeixinPay(NewConfirmOrderActivity.this);
		repay = getIntent().getBooleanExtra("repay", false);

		// tvGoodName = (TextView) findViewById(R.id.confirm_order_good_name);
		tvGoodPrice = (TextView) findViewById(R.id.confirm_order_price);
		tvReallyPay = (TextView) findViewById(R.id.confirm_order_money_pay);

		tvDiscodeBottom = (TextView) findViewById(R.id.confirm_order_discode);

		rbAlipay = (RadioButton) findViewById(R.id.rb_zhifubao);
		rbWeixinpay = (RadioButton) findViewById(R.id.rb_weixing);
		rbXianXiapay = (RadioButton) findViewById(R.id.rb_xianxia);

		if (repay) {// 重新支付
			doRepay();
		} else {
			// 请求订单 状态

			code = (MyCodeVO) getIntent().getSerializableExtra("code");
			classe = (ClassVO) getIntent().getSerializableExtra("class");

			tv_class.setText(classe.getClassname());
			if (null != code)
				discode.setText(code.getYcode() + "");
			schoolName = getIntent().getStringExtra("schoolName");
			phone = getIntent().getStringExtra("phone");
			// 教练信息
			coachVO = (CoachVO) getIntent().getSerializableExtra("coach");
			if (null == coachVO) {// 随机匹配
				tv_coach.setText("报考教练:智能匹配");
			} else {
				tv_coach.setText("报考教练:" + coachVO.getName());

			}

			if (null != classe) {
				price = classe.getPrice();
				onSalePrice = classe.getOnsaleprice();

				productName = classe.getClassname();
				productDetail = classe.getClassname() + onSalePrice;

				// tvReallyPay.setText(onSalePrice + "元");
				tvReallyPay.setText(price + "元(" + schoolName + ")");
			}

			doRepay();

		}

	}

	private void doRepay() {
		PayOrderVO bean = (PayOrderVO) getIntent().getSerializableExtra("bean");
		coupCode = bean.couponcode;
		couponId = bean.activitycoupon;
		orderId = bean._id;
		price = bean.applyclasstypeinfo.price;
		onSalePrice = bean.applyclasstypeinfo.onsaleprice;
		paymoney = bean.paymoney;
		productName = bean.applyclasstypeinfo.name;
		productDetail = bean.applyclasstypeinfo.name + onSalePrice;
		tvDiscodeBottom.setText("" + bean.discountmoney + "元");
		phone = app.userVO.getMobile();

		schoolName = bean.applyschoolinfo.getName();
		adress = bean.applyschoolinfo.getAddress();

		RelativeLayout.LayoutParams headpicParam = (android.widget.RelativeLayout.LayoutParams) iv_school
				.getLayoutParams();
		String url = bean.applyschoolinfo.getLogoimg().getOriginalpic();
		LogUtil.print("Image-->" + url);
		if (TextUtils.isEmpty(url)) {
			iv_school.setImageResource(R.drawable.default_image);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, iv_school,
					headpicParam.width, headpicParam.height);
		}
		tv_school.setText(schoolName);
		tv_adress.setText(adress);
		tv_class.setText(productName);

		//
		// bean.applyschoolinfo.getLogoimg().getThumbnailpic()

		tvGoodPrice.setText(bean.applyclasstypeinfo.price + "元");
		// tvGoodName.setText(bean.applyclasstypeinfo.name + "元");

		tvReallyPay.setText(bean.applyclasstypeinfo.price + "元("
				+ bean.applyschoolinfo.getName() + ")");
	}

	private void Listenner() {
		// tv_coupon_show.setOnClickListener(this);
		rbAlipay.setOnCheckedChangeListener(this);
		rbWeixinpay.setOnCheckedChangeListener(this);
		rbXianXiapay.setOnCheckedChangeListener(this);
		rl_saoyisao.setOnClickListener(this);
	}

	// private String en
	private String couponId = "";
	private String coupCode = "";

	private String money = "";
	private String payName;
	private String payDetail;
	private String orderId;

	private void request(String coupCode, String couponId, String payOrderId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("couponcode", coupCode);
		paramMap.put("couponid", couponId);
		paramMap.put("payoderid", payOrderId);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend("sendPay", this, Config.IP
				+ "api/v1/userinfo/usercouponforpay", paramMap, 10000,
				headerMap);
	}

	/**
	 * 获取微信支付 的订单信息
	 * 
	 * @param userId
	 * @param payId
	 */
	private void requestWeiXinPayInfor(String userId, String payId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", userId);
		// paramMap.put("couponcode", coupCode);
		// paramMap.put("couponid", couponId);
		paramMap.put("payoderid", payId);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(WEIXIN_PAY_INFOR, this, Config.IP
				+ "api/v1/payinfo/getprepayinfo", paramMap, 10000, headerMap);
	}

	/**
	 * 确认支付
	 * 
	 * @param code
	 */
	private void requestConfirmPayOrder(int payType, String code) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("paytype", String.valueOf(payType));// 支付方式 0 线下支付 1 支付宝 2
															// 微信
		paramMap.put("bcode", code);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils
				.httpPostSend(CONFIRM_PAY_ORDER, this, Config.IP
						+ "api/v1/userinfo/confirmpayorder", paramMap, 10000,
						headerMap);
	}

	/**
	 * 获取位完成的订单 详情
	 */
	private void requestNotFinshOrder() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend("notPay", this, Config.IP
				+ "api/v1/userinfo/usercouponforpay", paramMap, 10000,
				headerMap);
	}

	@Override
	protected void onResume() {
		if (weixinPayState == 0) {// 支付成功
			LogUtil.print("onResume---支付成功>");
			app.userVO.setPayState(20);
			toEnrollSuccess(true);
		} else if (weixinPayState == -1 || weixinPayState == -2) {// 支付失败,取消支付
			toEnrollSuccess(true);
			LogUtil.print("onResume---支付失败>");
			app.userVO.setPayState(30);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		weixinPayState = 1;
		super.onPause();
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString))
			return true;
		LogUtil.print("doCall" + jsonString);
		if (type.equals("sendPay")) {
			PayUtils pay = new PayUtils();
			pay.setTradeNo(orderId);
			pay.pay(this, mHandler, productName, productDetail, paymoney);
		} else if (type.equals("notPay")) {

		} else if (type.equals("reLogin")) {// 重新登录获取信息
			try {
				app.userVO = JSONUtil.toJavaBean(UserVO.class, data);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (type.equals(WEIXIN_PAY_INFOR)) {// 获取微信 支付订单信息
			PayReq pay = weixinPay.parseJson(data);
			weixinPay.pay(pay);
		} else if (type.equals(CONFIRM_PAY_ORDER)) {// 确认订单，启用第三方 支付，或者线下支付

			if (rbAlipay.isChecked()) {// 支付宝
				if ((coupCode == null || coupCode.length() == 0)) {// 直接支付
					PayUtils pay = new PayUtils();
					pay.setTradeNo(orderId);
					pay.pay(this, mHandler, productName, productDetail,
							paymoney);
				} else {
					request(coupCode, couponId, orderId);
				}
			} else if (rbWeixinpay.isChecked()) {// 微信支付
				requestWeiXinPayInfor(app.userVO.getUserid(), orderId);
			} else {// 线下支付
				toEnrollSuccess(false);
			}

		}
		// getIntent().getParcelableArrayListExtra(name)

		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_order_discode_rl:
			Intent intent = new Intent(NewConfirmOrderActivity.this,
					CheckDiscodeAct.class);
			intent.putExtra("phone", phone);
			startActivityForResult(intent, 3);
			break;
		case R.id.base_left_btn:
			cancelDialog();
			// setResult(1);
			// finish();
			break;
		// 扫描
		case R.id.rl_saoyisao:
			Intent intent1 = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent1, 1);
			break;
		case R.id.act_pay_now:// 立即支付
			int payType = 0;
			if (rbAlipay.isChecked()) {// 支付宝
				payType = 1;
			} else if (rbWeixinpay.isChecked()) {// 微信支付
				payType = 2;
			} else {
				payType = 0;
			}
			requestConfirmPayOrder(payType, etFrom.getText().toString());
			break;
		default:
			break;
		}
	}

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		@Override
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
					app.userVO.setPayState(20);
					app.userVO
							.setApplystate(EnrollResult.SUBJECT_ENROLL_SUCCESS
									.getValue());
					Toast.makeText(NewConfirmOrderActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					// ConfirmOrderActivity.this.setResult(9, getIntent());
					// ConfirmOrderActivity.this.finish();
					reLogin();
				} else {
					app.userVO.setApplystate(EnrollResult.SUBJECT_NONE
							.getValue());
					app.userVO.setPayState(30);
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(NewConfirmOrderActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(NewConfirmOrderActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				app.userVO.setPayState(30);
				app.userVO.setApplystate(EnrollResult.SUBJECT_NONE.getValue());
				Toast.makeText(NewConfirmOrderActivity.this,
						"检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
			toEnrollSuccess(true);
		};
	};

	/**
	 * 跳转到报名成功页面
	 */
	private void toEnrollSuccess(boolean isOnline) {
		Intent i = new Intent(NewConfirmOrderActivity.this,
				EnrollSuccessActivity.class);
		i.putExtra("isOnline", isOnline);
		startActivity(i);
		// 结束之前的页面
		setResult(9, new Intent());
		finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && data != null) {

			// data.getStringExtra("money");
			couponId = data.getStringExtra("id");
			coupCode = data.getStringExtra("name");
			// tv_coupon_show.setText(coupCode);
			// tvYcode.setText("￥-"+data.getStringExtra("money"));
			String m = data.getStringExtra("money");
			tvReallyPay.setText(price + "元(" + schoolName + ")");

			// 最终付款价格
			paymoney = getPrice(onSalePrice, m) + "";
			tvReallyPay.setText(paymoney + "元");
			tvDiscodeBottom.setText("一步现金可抵扣" + m + "元");

		}

		if (requestCode == 1 && data != null) {

			etFrom.setText(data.getStringExtra("ma"));
		}

		LogUtil.print("class---》》" + data);

	}

	private void reLogin() {
		String lastLoginPhone = util.readParam(Config.LAST_LOGIN_ACCOUNT);
		String password = util.readParam(Config.LAST_LOGIN_PASSWORD);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", lastLoginPhone);
		paramMap.put("usertype", "1");
		paramMap.put("password", password);
		HttpSendUtils.httpPostSend("reLogin", this, Config.IP
				+ "api/v1/userinfo/userlogin", paramMap);
	}

	private int getPrice(String a1, String a2) {
		int a = Integer.parseInt(a1) - Integer.parseInt(a2);
		// 支付
		money = a + "";
		return a;
	}

	/**
	 * 获取订单名字
	 * 
	 * @return
	 */
	private String getOrderName() {
		if (classe != null)
			return classe.getClassname() + "￥" + classe.getOnsaleprice();
		return null;
	}

	/**
	 * 获取订单详情
	 * 
	 * @return
	 */
	private String getOrderDescription() {
		if (classe != null)
			return classe.getClassname() + "￥" + classe.getOnsaleprice();
		return null;
	}

	/**
	 * 取消对话框
	 */
	private void cancelDialog() {
		final PopupWindow pop = new PopupWindow(this);
		pop.setHeight(LayoutParams.MATCH_PARENT);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		View view = View.inflate(this, R.layout.pop_back, null);
		view.setFocusable(true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pop.dismiss();
			}
		});
		pop.setContentView(view);
		view.findViewById(R.id.pay_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 跳转

						pop.dismiss();
						setResult(9, new Intent());
						finish();
						// 退出支付流程,干掉之前的
					}
				});
		view.findViewById(R.id.pay_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						pop.dismiss();
					}
				});
		pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 返回
			cancelDialog();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		LogUtil.print("isChecked----" + isChecked);
		if (buttonView.getId() == R.id.rb_weixing) {
			if (isChecked) {
				rbAlipay.setChecked(false);
				rbXianXiapay.setChecked(false);
			}
		} else if (buttonView.getId() == R.id.rb_xianxia) {
			if (isChecked) {
				rbWeixinpay.setChecked(false);
				rbAlipay.setChecked(false);
			}
		} else if (buttonView.getId() == R.id.rb_zhifubao) {
			if (isChecked) {
				rbWeixinpay.setChecked(false);
				rbXianXiapay.setChecked(false);
			}
		}

	}
}
