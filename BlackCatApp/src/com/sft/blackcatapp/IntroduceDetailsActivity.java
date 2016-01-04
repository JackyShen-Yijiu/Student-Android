package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.sft.blackcatapp.R;

public class IntroduceDetailsActivity extends BaseActivity {

	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.introduce_detail);
		initView();
		initData();
	}

	private void initView() {
		webview = (WebView) findViewById(R.id.introduce_detail_webview);
		progress = (ProgressBar) findViewById(R.id.introduce_detail_progress);
	}

	private void initData() {
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

		String url = getIntent().getStringExtra("url");

		webview.loadUrl(url);
	}
}
