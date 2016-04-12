package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.vo.ProductVO;

/**
 * 购买成功
 * 
 */
public class ProductOrderSuccessActivity extends BaseActivity {

	private Button returnBtn;

	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;

	private ProductVO productVO;

	private ImageView productPic;

	private TextView productNameTv;

	private TextView productPriceTv;

	private TextView timeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_order_success);
		initView();
		// setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		setTitleText("兑换优惠券");

		showTitlebarBtn(1);

		productPic = (ImageView) findViewById(R.id.product_order_pic);
		productNameTv = (TextView) findViewById(R.id.product_order_name_tv);
		productPriceTv = (TextView) findViewById(R.id.product_order_price_tv);
		timeTv = (TextView) findViewById(R.id.product_order_time_tv);
		productVO = (ProductVO) getIntent().getSerializableExtra("productVO");

		productNameTv.setText(productVO.getProductname());
		productPriceTv.setText(productVO.getProductprice());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		timeTv.setText(format.format(new Date()));

		// exchangeAddrTv.setText(productVO.getAddress());
		LinearLayout.LayoutParams headParam = (LinearLayout.LayoutParams) productPic
				.getLayoutParams();
		if (TextUtils.isEmpty(productVO.getProductimg())) {
			productPic.setBackgroundResource(R.drawable.defaultimage);
		} else {

			BitmapManager.INSTANCE.loadBitmap2(productVO.getProductimg(),
					productPic, headParam.width, headParam.height);
		}
		// button_sus = (Button) findViewById(R.id.button_sus);

		// webview = (WebView) findViewById(R.id.order_success_webview);
		// progress = (ProgressBar) findViewById(R.id.order_success_progress);
		// // returnBtn = (Button) findViewById(R.id.order_success_btn);
		//
		// webview.setWebViewClient(new WebViewClient() {
		// @Override
		// public void onPageFinished(WebView view, String url) {
		// // 页面加载完成时，回调
		// progress.setVisibility(View.GONE);
		// super.onPageFinished(view, url);
		// }
		// });
		//
		// settings = webview.getSettings();
		// settings.setBuiltInZoomControls(false);
		// settings.setUseWideViewPort(true);
		// settings.setJavaScriptEnabled(true);
		// webview.setHorizontalScrollBarEnabled(false);
		// webview.setVerticalScrollBarEnabled(false);
		// String finishorderurl = getIntent().getStringExtra("finishorderurl");
		// if (finishorderurl != null) {
		//
		// webview.loadUrl(finishorderurl);
		// }
	}

	private void setListener() {
		// returnBtn.setOnClickListener(this);
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
		case R.id.base_right_tv:
			// case R.id.order_success_btn:
			// finish();
			// break;
			Intent intent1 = new Intent(this, MallActivity.class);
			intent1.putExtra("moneytype",
					Config.MoneyType.COIN_CERTIFICATE.getValue());
			startActivity(intent1);
			break;
		}
	}

	@Override
	public void finish() {
		sendBroadcast(new Intent(ProductOrderActivity.class.getName())
				.putExtra("finish", true));
		sendBroadcast(new Intent(ProductDetailActivity.class.getName())
				.putExtra("finish", true));
		super.finish();
	}
}
