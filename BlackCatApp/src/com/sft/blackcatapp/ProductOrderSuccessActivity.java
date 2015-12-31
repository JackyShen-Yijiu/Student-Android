package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sft.common.Config;

/**
 * 购买成功
 * 
 */
public class ProductOrderSuccessActivity extends BaseActivity {

	private Button returnBtn;

	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_order_success);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		setTitleText("兑换优惠券");

		showTitlebarBtn(1);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.go_and_see);

		// button_sus = (Button) findViewById(R.id.button_sus);

		webview = (WebView) findViewById(R.id.order_success_webview);
		progress = (ProgressBar) findViewById(R.id.order_success_progress);
		// returnBtn = (Button) findViewById(R.id.order_success_btn);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 页面加载完成时，回调
				progress.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});

		settings = webview.getSettings();
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);

		String finishorderurl = getIntent().getStringExtra("finishorderurl");
		if (finishorderurl != null) {

			webview.loadUrl(finishorderurl);
		}
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
