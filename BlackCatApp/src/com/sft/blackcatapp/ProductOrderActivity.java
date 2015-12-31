package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductBuySuccessVO;
import com.sft.vo.ProductVO;

/**
 * 订单信息
 * 
 */
public class ProductOrderActivity extends BaseActivity implements
		OnFocusChangeListener {

	private static final String buy = "buy";
	// 姓名
	private EditText nameEt, phoneEt, addressEt;
	//
	private LinearLayout nameLayout, phoneLayout, addressLayout;
	//
	private TextView currencyTv;
	private Button buyBtn;

	int selectColor = Color.parseColor("#f2f2f2");
	int normalColor = Color.parseColor("#ffffff");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_product_order);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		setTitleText("订单确认");

		nameEt = (EditText) findViewById(R.id.product_order_nameet);
		phoneEt = (EditText) findViewById(R.id.product_order_phoneet);
		addressEt = (EditText) findViewById(R.id.product_order_addresset);

		nameLayout = (LinearLayout) findViewById(R.id.product_namelayout);
		phoneLayout = (LinearLayout) findViewById(R.id.product_phonelayout);
		addressLayout = (LinearLayout) findViewById(R.id.product_addresslayout);

		currencyTv = (TextView) findViewById(R.id.product_order_currentcy_tv);
		buyBtn = (Button) findViewById(R.id.product_order_buy_btn);

		currencyTv.setText(app.currency);
	}

	private void setListener() {
		nameEt.setOnFocusChangeListener(this);
		phoneEt.setOnFocusChangeListener(this);
		addressEt.setOnFocusChangeListener(this);

		buyBtn.setOnClickListener(this);
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

			String result = checkOrder();
			if (result != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure(result);
				return;
			}
			ProductVO productVO = (ProductVO) getIntent().getSerializableExtra(
					"product");
			MyCuponVO myCuponVO = (MyCuponVO) getIntent().getSerializableExtra(
					"myCupon");

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("usertype", "1");
			paramsMap.put("userid", app.userVO.getUserid());
			paramsMap.put("productid", productVO.getProductid());
			paramsMap.put("name", nameEt.getText().toString());
			paramsMap.put("mobile", phoneEt.getText().toString());
			paramsMap.put("address", addressEt.getText().toString());
			if (myCuponVO != null) {

				paramsMap.put("couponid", myCuponVO.get_id());
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
		String name = nameEt.getText().toString();
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

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		nameLayout.setBackgroundColor(normalColor);
		phoneLayout.setBackgroundColor(normalColor);
		addressLayout.setBackgroundColor(normalColor);
		switch (v.getId()) {
		case R.id.product_order_nameet:
			nameLayout.setBackgroundColor(selectColor);
			break;
		case R.id.product_order_phoneet:
			phoneLayout.setBackgroundColor(selectColor);
			break;
		case R.id.product_order_addresset:
			addressLayout.setBackgroundColor(selectColor);
			break;
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		ProductBuySuccessVO productBuySuccessVO = null;
		if (type.equals(buy)) {
			try {
				JSONObject extra = jsonObject.getJSONObject("extra");
				productBuySuccessVO = JSONUtil.toJavaBean(
						ProductBuySuccessVO.class, extra);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(this, ProductOrderSuccessActivity.class);
			if (productBuySuccessVO != null) {
				intent.putExtra("finishorderurl",
						productBuySuccessVO.getFinishorderurl());
			}
			startActivity(intent);
		}
		return true;
	}
}
