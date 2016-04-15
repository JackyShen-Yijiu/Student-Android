package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.event.ProductExchangeSuccessEvent;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductBuySuccessVO;
import com.sft.vo.ProductVO;

import de.greenrobot.event.EventBus;

/**
 * 订单信息
 * 
 */
public class ProductOrderActivity extends BaseActivity {

	private static final String buy = "buy";
	// 兑换券
	private final static String myCoinCertificate = "myCoinCertificate";
	//

	int selectColor = Color.parseColor("#f2f2f2");
	int normalColor = Color.parseColor("#ffffff");

	private TextView prodectBelowPrice;
	private Button productBuyBtn;

	// 兑换券列表
	private List<MyCuponVO> myCuponList;
	private MyCuponVO myCupon;
	private ProductVO productVO;
	private ImageView productPic;
	private TextView productNameTv;
	private TextView timeTv;
	private TextView productPriceTv;
	private TextView productNumTv;
	private ImageView productNumSubIv;
	private ImageView productNumAddIv;
	private TextView exchangeAddrTv;
	private int productNum = 1;
	private double productPrice = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_product_order);
		initView();
		initData();
		setListener();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		setTitleText("兑换详情");

		productPic = (ImageView) findViewById(R.id.product_detail_pic);
		productNameTv = (TextView) findViewById(R.id.product_detail_name_tv);
		productPriceTv = (TextView) findViewById(R.id.product_detail_price_tv);
		timeTv = (TextView) findViewById(R.id.product_detail_time_tv);

		productNumAddIv = (ImageView) findViewById(R.id.product_detail_num_add_iv);
		productNumSubIv = (ImageView) findViewById(R.id.product_detail_num_sub_iv);
		productNumTv = (TextView) findViewById(R.id.product_detail_num_tv);

		exchangeAddrTv = (TextView) findViewById(R.id.prodduct_detail_exchange_addr_tv);

		prodectBelowPrice = (TextView) findViewById(R.id.product_order_product_price);
		productBuyBtn = (Button) findViewById(R.id.product_order_buy_btn);
	}

	private void initData() {
		productVO = (ProductVO) getIntent().getSerializableExtra("product");
		// MyCuponVO myCupon = (MyCuponVO) getIntent().getSerializableExtra(
		// "myCupon");

		productNameTv.setText(productVO.getProductname());
		productPriceTv.setText(productVO.getProductprice());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		timeTv.setText(format.format(new Date()));

		exchangeAddrTv.setText(productVO.getAddress());
		LinearLayout.LayoutParams headParam = (LinearLayout.LayoutParams) productPic
				.getLayoutParams();
		if (TextUtils.isEmpty(productVO.getProductimg())) {
			productPic.setBackgroundResource(R.drawable.defaultimage);
		} else {

			BitmapManager.INSTANCE.loadBitmap2(productVO.getProductimg(),
					productPic, headParam.width, headParam.height);
		}
		boolean isCupon = getIntent().getBooleanExtra("isCupon", false);
		prodectBelowPrice.setText(productVO.getProductprice() + "");
		productPrice = Double.parseDouble(productVO.getProductprice());
		try {
			LogUtil.print(productVO.getProductprice() + "------" + app.currency);
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

		productNumAddIv.setOnClickListener(this);
		productNumSubIv.setOnClickListener(this);
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
			// if (!CommonUtil.isMobile(phoneEt.getText().toString().trim())) {
			// addressseePhoneHint.setVisibility(View.VISIBLE);
			// LogUtil.print("==========pp"
			// + phoneEt.getText().toString().trim().length());
			// return;
			// }
			// MyCuponVO myCuponVO = (MyCuponVO)
			// getIntent().getSerializableExtra(
			// "myCupon");

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("usertype", "1");
			paramsMap.put("userid", app.userVO.getUserid());
			paramsMap.put("productid", productVO.getProductid());
			paramsMap.put("buycount", productNum + "");
			if (TextUtils.isEmpty(app.userVO.getName())) {

				paramsMap.put("name", "");
			} else {
				paramsMap.put("name", app.userVO.getName());
			}
			if (TextUtils.isEmpty(app.userVO.getMobile())) {

				paramsMap.put("mobile", "");
			} else {
				paramsMap.put("mobile", app.userVO.getMobile());

			}
			if (TextUtils.isEmpty(app.userVO.getAddress())) {

				paramsMap.put("address", "");
			} else {

				paramsMap.put("address", app.userVO.getAddress());
			}
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

		case R.id.product_detail_num_sub_iv:
			// 数量减少
			LogUtil.print((productVO.getProductcount() - productVO
					.getBuycount())
					+ "pppp---==="
					+ productVO.getProductcount());
			if (Integer.parseInt(productNumTv.getText().toString()) <= 1) {
				productNumSubIv.setEnabled(false);
				productNumSubIv
						.setImageResource(R.drawable.quantity_subtract_off);
			} else {
				productNumSubIv.setEnabled(true);
				productNumSubIv
						.setImageResource(R.drawable.quantity_subtract_on);
				productNum--;
				productNumTv.setText(productNum + "");
				productPrice = productPrice
						- Double.parseDouble(productVO.getProductprice());
				prodectBelowPrice.setText((int) productPrice + "");
			}
			if (Integer.parseInt(productNumTv.getText().toString()) <= 1) {
				productNumSubIv.setEnabled(false);
				productNumSubIv
						.setImageResource(R.drawable.quantity_subtract_off);
			}
			if (Integer.parseInt(productNumTv.getText().toString()) >= (productVO
					.getProductcount() - productVO.getBuycount())) {
				productNumAddIv.setEnabled(false);
				productNumAddIv.setImageResource(R.drawable.quantity_add_off);
			} else {
				productNumAddIv.setEnabled(true);
				productNumAddIv.setImageResource(R.drawable.quantity_add_on);
			}
			break;
		case R.id.product_detail_num_add_iv:
			// 数量增加
			if (Integer.parseInt(productNumTv.getText().toString()) >= (productVO
					.getProductcount() - productVO.getBuycount())) {
				productNumAddIv.setEnabled(false);
				productNumAddIv.setImageResource(R.drawable.quantity_add_off);
			} else {
				productNumAddIv.setEnabled(true);
				productNumAddIv.setImageResource(R.drawable.quantity_add_on);
				productNum++;
				productPrice = productPrice
						+ Double.parseDouble(productVO.getProductprice());
				prodectBelowPrice.setText((int) productPrice + "");
				productNumTv.setText(productNum + "");
			}

			if (Integer.parseInt(productNumTv.getText().toString()) <= 1) {
				productNumSubIv.setEnabled(false);
				productNumSubIv
						.setImageResource(R.drawable.quantity_subtract_off);
			} else {
				productNumSubIv.setEnabled(true);
				productNumSubIv
						.setImageResource(R.drawable.quantity_subtract_on);
			}
			break;
		}
	}

	private String checkOrder() {
		// String name = addressseeNameEt.getText().toString();
		// if (TextUtils.isEmpty(name)) {
		// return "请输入姓名";
		// }
		// String phone = phoneEt.getText().toString();
		// if (TextUtils.isEmpty(phone)) {
		// return "请输入电话";
		// }
		// String address = addressEt.getText().toString();
		// if (TextUtils.isEmpty(address)) {
		// return "请输入地址";
		// }
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
				intent.putExtra("productVO", productVO);
				if (productBuySuccessVO != null) {
					// 将兑换所花费的积分传给下一个界面
					intent.putExtra("money", productPrice);
					intent.putExtra("orderscanaduiturl",
							productBuySuccessVO.getOrderscanaduiturl());
				}
				startActivity(intent);
				finish();
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

	public void onEvent(ProductExchangeSuccessEvent event) {
		this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
