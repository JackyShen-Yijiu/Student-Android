package com.sft.blackcatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.sft.blackcatapp.R;
import com.sft.util.LogUtil;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductVO;

/**
 * 商品详情
 * 
 * @author Administrator
 * 
 */
public class ProductDetailActivity extends BaseActivity {

	private WebView webView;

	// private TextView currencyTv;
	private Button buyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_product_detail);
		webView = (WebView) findViewById(R.id.product_detail_webview);
		// currencyTv = (TextView)
		// findViewById(R.id.product_detail_currentcy_tv);
		buyBtn = (Button) findViewById(R.id.product_detail_buy_btn);

		buyBtn.setOnClickListener(this);
		initData();
	}

	@Override
	protected void onResume() {
		webView.onResume();
		super.onResume();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		// currencyTv.setText(app.currency);
		ProductVO productVO = (ProductVO) getIntent().getSerializableExtra(
				"product");
		MyCuponVO myCupon = (MyCuponVO) getIntent().getSerializableExtra(
				"myCupon");
		boolean isCupon = getIntent().getBooleanExtra("isCupon", false);
		String url = productVO.getDetailurl();
		setTitleText("商品详情");
		WebSettings webSettings = webView.getSettings();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		webSettings.setJavaScriptEnabled(true);

		webView.loadUrl(url);

		if (!isCupon) {

			try {
				LogUtil.print(productVO.getProductprice() + "------"
						+ app.currency);
				if (Long.parseLong(app.currency) >= Long.parseLong(productVO
						.getProductprice())) {
					buyBtn.setEnabled(true);
					buyBtn.setTextColor(Color.parseColor("#ffffff"));
				} else {
					buyBtn.setEnabled(false);
					buyBtn.setTextColor(Color.parseColor("#999999"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				buyBtn.setEnabled(false);
				buyBtn.setTextColor(Color.parseColor("#999999"));
			}
		} else {
			buyBtn.setText("立即兑换");
			if (myCupon != null) {

				if ("1".equals(myCupon.getState())) {
					buyBtn.setEnabled(true);
					buyBtn.setTextColor(Color.parseColor("#ffffff"));
				} else {
					buyBtn.setEnabled(false);
					buyBtn.setTextColor(Color.parseColor("#999999"));
				}
			} else {
				buyBtn.setEnabled(false);
				buyBtn.setTextColor(Color.parseColor("#999999"));

			}
		}
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
		case R.id.product_detail_buy_btn:
			Intent intent = new Intent(this, ProductOrderActivity.class);
			intent.putExtra("product",
					getIntent().getSerializableExtra("product"));
			intent.putExtra("myCupon",
					getIntent().getSerializableExtra("myCupon"));
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
