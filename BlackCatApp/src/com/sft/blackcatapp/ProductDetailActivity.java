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
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.event.ProductExchangeSuccessEvent;
import com.sft.util.LogUtil;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductVO;

import de.greenrobot.event.EventBus;

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

	private TextView productName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_product_detail);
		showTitlebarBtn(3);
		setBtnBkground(R.drawable.base_left_btn_bkground, 0);
		webView = (WebView) findViewById(R.id.product_detail_webview);
		// currencyTv = (TextView)
		// findViewById(R.id.product_detail_currentcy_tv);
		buyBtn = (Button) findViewById(R.id.product_detail_buy_btn);
		productName = (TextView) findViewById(R.id.product_detail_product_name);
		buyBtn.setOnClickListener(this);
		initData();

		EventBus.getDefault().register(this);
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
		webView.setVerticalScrollBarEnabled(false);

		webView.setHorizontalScrollBarEnabled(false);

		WebSettings webSettings = webView.getSettings();

		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		webSettings.setJavaScriptEnabled(true);

		webView.loadUrl(url);

		if (!isCupon) {

			productName.setText("" + productVO.getProductprice() + "积分");
			try {
				LogUtil.print(productVO.getProductprice() + "------"
						+ app.currency);
				if (Long.parseLong(app.currency) >= Long.parseLong(productVO
						.getProductprice())) {
					buyBtn.setEnabled(true);
					buyBtn.setTextColor(Color.parseColor("#ffffff"));
					buyBtn.setBackgroundResource(R.drawable.button_rounded_corners);
				} else {
					buyBtn.setEnabled(false);
					buyBtn.setTextColor(Color.parseColor("#ffffff"));
					buyBtn.setBackgroundResource(R.drawable.button_rounded_corners_gray);
				}
			} catch (Exception e) {
				e.printStackTrace();
				buyBtn.setEnabled(false);
				buyBtn.setTextColor(Color.parseColor("#ffffff"));
				buyBtn.setBackgroundResource(R.drawable.button_rounded_corners_gray);
			}
		} else {
			buyBtn.setText("立即兑换");
			productName.setText("需要消费兑换券1张");
			if (myCupon != null) {

				if ("1".equals(myCupon.getState())) {
					buyBtn.setEnabled(true);
					buyBtn.setTextColor(Color.parseColor("#ffffff"));
					buyBtn.setBackgroundResource(R.drawable.button_rounded_corners);
				} else {
					buyBtn.setEnabled(false);
					buyBtn.setTextColor(Color.parseColor("#ffffff"));
					buyBtn.setBackgroundResource(R.drawable.button_rounded_corners_gray);
				}
			} else {
				buyBtn.setEnabled(false);
				buyBtn.setTextColor(Color.parseColor("#ffffff"));
				buyBtn.setBackgroundResource(R.drawable.button_rounded_corners_gray);
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
			intent.putExtra("isCupon",
					getIntent().getBooleanExtra("isCupon", false));
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
		// webView.goBack();
		// return true;
		// }
		return super.onKeyDown(keyCode, event);
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
