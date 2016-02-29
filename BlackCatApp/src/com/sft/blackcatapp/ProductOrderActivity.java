package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductBuySuccessVO;
import com.sft.vo.ProductVO;

/**
 * 订单信息
 * 
 */
public class ProductOrderActivity extends BaseActivity {

	private static final String buy = "buy";
	// 兑换券
	private final static String myCoinCertificate = "myCoinCertificate";
	// 姓名
	private EditText addressseeNameEt, phoneEt, addressEt;
	//

	int selectColor = Color.parseColor("#f2f2f2");
	int normalColor = Color.parseColor("#ffffff");
	private TextView addressseePhoneHint;
	private TextView addressMode;
	private TextView productPrice;
	private TextView productPriceName;
	private TextView productName;
	private ProductVO productVO;
	private LinearLayout addressLl;
	private TextView prodectBelowPrice;
	private Button productBuyBtn;

	// 兑换券列表
	private List<MyCuponVO> myCuponList;
	private MyCuponVO myCupon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_product_order);
		initView();
		initData();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		setTitleText("兑换详情");

		productName = (TextView) findViewById(R.id.product_order_product_name_tv);
		productPriceName = (TextView) findViewById(R.id.product_order_price_name_tv);
		productPrice = (TextView) findViewById(R.id.product_order_price_tv);
		addressMode = (TextView) findViewById(R.id.product_order_address_mode_tv);

		addressEt = (EditText) findViewById(R.id.product_order_address_et);
		addressseeNameEt = (EditText) findViewById(R.id.product_order_addresssee_name_et);
		addressseePhoneHint = (TextView) findViewById(R.id.product_order_addresssee_phone_hint_tv);
		phoneEt = (EditText) findViewById(R.id.product_order_addresssee_phone_et);

		addressLl = (LinearLayout) findViewById(R.id.product_order_address_ll);

		prodectBelowPrice = (TextView) findViewById(R.id.product_order_product_price);
		productBuyBtn = (Button) findViewById(R.id.product_order_buy_btn);
	}

	private void initData() {
		productVO = (ProductVO) getIntent().getSerializableExtra("product");
		// MyCuponVO myCupon = (MyCuponVO) getIntent().getSerializableExtra(
		// "myCupon");
		boolean isCupon = getIntent().getBooleanExtra("isCupon", false);
		addressseePhoneHint.setVisibility(View.INVISIBLE);
		productName.setText(productVO.getProductname());
		if (isCupon) {
			// 兑换商品
			productPriceName.setText("兑换方式");
			productPrice.setText("1张报名兑换券");
			addressMode.setText("到店自取");
			addressLl.setVisibility(View.GONE);
			prodectBelowPrice.setText("需支付兑换券一张");

			if (myCupon != null) {

				if ("1".equals(myCupon.getState())) {
					productBuyBtn.setEnabled(true);
					productBuyBtn.setTextColor(Color.parseColor("#ffffff"));
				} else {
					productBuyBtn.setEnabled(false);
					productBuyBtn.setTextColor(Color.parseColor("#999999"));
				}
			} else {
				productBuyBtn.setEnabled(false);
				productBuyBtn.setTextColor(Color.parseColor("#999999"));

			}

			// 获取兑换券
			obtainCoinCertificate();
		} else {
			// 积分商品
			productPriceName.setText("兑换积分");
			productPrice.setText("¥" + productVO.getProductprice() + "YB");
			addressMode.setText("快递  免邮");
			addressLl.setVisibility(View.VISIBLE);
			prodectBelowPrice.setText("需支付" + productVO.getProductprice()
					+ "YB");

			try {
				LogUtil.print(productVO.getProductprice() + "------"
						+ app.currency);
				if (Long.parseLong(app.currency) >= Long.parseLong(productVO
						.getProductprice())) {
					productBuyBtn.setEnabled(true);
					productBuyBtn.setTextColor(Color.parseColor("#ffffff"));
				} else {
					productBuyBtn.setEnabled(false);
					productBuyBtn.setTextColor(Color.parseColor("#999999"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				productBuyBtn.setEnabled(false);
				productBuyBtn.setTextColor(Color.parseColor("#999999"));
			}
		}

		// 个人信息
		if (app.userVO != null) {
			addressseeNameEt.setText(app.userVO.getName());
			addressEt.setText(app.userVO.getAddress());
			phoneEt.setText(app.userVO.getMobile());
		}
	}

	// 获取兑换券
	private void obtainCoinCertificate() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		// paramsMap.put("userid", "562cb02e93d4ca260b40e544");

		Map<String, String> headerMap = new HashMap<String, String>();
		// headerMap
		// .put("authorization",
		// "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI1NjJjYjAyZTkzZDRjYTI2MGI0MGU1NDQiLCJ0aW1lc3RhbXAiOiIyMDE1LTEwLTI1VDEwOjQwOjUwLjI1MVoiLCJhdWQiOiJibGFja2NhdGUiLCJpYXQiOjE0NDU3Njk2NTB9.ooSYJ5zJ7ZIsaVwK0o0UuFGMS_xJQhSNcBNEtNAB25w");
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myCoinCertificate, this, Config.IP
				+ "api/v1/userinfo/getmycupon", paramsMap, 10000, headerMap);
	}

	private void setListener() {
		// addressseeNameEt.setOnFocusChangeListener(this);
		// phoneEt.setOnFocusChangeListener(this);
		// addressEt.setOnFocusChangeListener(this);

		productBuyBtn.setOnClickListener(this);
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
		case R.id.product_order_buy_btn:

			// String result = checkOrder();
			// if (result != null) {
			// ZProgressHUD.getInstance(this).show();
			// ZProgressHUD.getInstance(this).dismissWithFailure(result);
			// return;
			// }
			if (!CommonUtil.isMobile(phoneEt.getText().toString().trim())) {
				addressseePhoneHint.setVisibility(View.VISIBLE);
				LogUtil.print("==========pp"
						+ phoneEt.getText().toString().trim().length());
				return;
			}
			ProductVO productVO = (ProductVO) getIntent().getSerializableExtra(
					"product");
			// MyCuponVO myCuponVO = (MyCuponVO)
			// getIntent().getSerializableExtra(
			// "myCupon");

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("usertype", "1");
			paramsMap.put("userid", app.userVO.getUserid());
			paramsMap.put("productid", productVO.getProductid());
			paramsMap.put("name", addressseeNameEt.getText().toString());
			paramsMap.put("mobile", phoneEt.getText().toString());
			paramsMap.put("address", addressEt.getText().toString());
			if (myCupon != null) {

				paramsMap.put("couponid", myCupon.get_id());
			}

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());

			HttpSendUtils
					.httpPostSend(buy, this, Config.IP
							+ "api/v1/userinfo/buyproduct", paramsMap, 10000,
							headerMap);
			break;
		}
	}

	private String checkOrder() {
		String name = addressseeNameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			return "请输入姓名";
		}
		String phone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			return "请输入电话";
		}
		String address = addressEt.getText().toString();
		if (TextUtils.isEmpty(address)) {
			return "请输入地址";
		}
		return null;
	}

	// @Override
	// public void onFocusChange(View v, boolean hasFocus) {
	// nameLayout.setBackgroundColor(normalColor);
	// phoneLayout.setBackgroundColor(normalColor);
	// addressLayout.setBackgroundColor(normalColor);
	// switch (v.getId()) {
	// // case R.id.product_order_nameet:
	// // nameLayout.setBackgroundColor(selectColor);
	// // break;
	// // case R.id.product_order_phoneet:
	// // phoneLayout.setBackgroundColor(selectColor);
	// // break;
	// // case R.id.product_order_addresset:
	// // addressLayout.setBackgroundColor(selectColor);
	// // break;
	// }
	// }

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(buy)) {
				ProductBuySuccessVO productBuySuccessVO = null;
				JSONObject extra = jsonObject.getJSONObject("extra");
				productBuySuccessVO = JSONUtil.toJavaBean(
						ProductBuySuccessVO.class, extra);

				Intent intent = new Intent(this,
						ProductOrderSuccessActivity.class);
				if (productBuySuccessVO != null) {
					intent.putExtra("finishorderurl",
							productBuySuccessVO.getFinishorderurl());
				}
				startActivity(intent);
			} else if (type.equals(myCoinCertificate)) {
				if (dataArray != null) {
					int length = dataArray.length();
					myCuponList = new ArrayList<MyCuponVO>();
					for (int i = 0; i < length; i++) {

						MyCuponVO myCuponVO = JSONUtil.toJavaBean(
								MyCuponVO.class, dataArray.getJSONObject(i));
						myCuponList.add(myCuponVO);

					}
					myCupon = myCuponList.get(0);
					if (myCupon != null) {

						if ("1".equals(myCupon.getState())) {
							productBuyBtn.setEnabled(true);
							productBuyBtn.setTextColor(Color
									.parseColor("#ffffff"));
						} else {
							productBuyBtn.setEnabled(false);
							productBuyBtn.setTextColor(Color
									.parseColor("#999999"));
						}
					} else {
						productBuyBtn.setEnabled(false);
						productBuyBtn.setTextColor(Color.parseColor("#999999"));

					}
					// app.coupons = length;
					// if (cupontAdapter == null) {
					// cupontAdapter = new CupontAdapter(this, myCuponList,
					// producttype);
					// } else {
					// cupontAdapter.setData(myCuponList);
					// }
					// incomeList.setAdapter(cupontAdapter);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
