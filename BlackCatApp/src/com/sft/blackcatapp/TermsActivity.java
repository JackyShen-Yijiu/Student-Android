package com.sft.blackcatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TermsActivity extends BaseActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_question);
		webView = (WebView) findViewById(R.id.question_webview);
		initData();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		setTitleText(R.string.allow_protocol);
		String url = "http://www.ifanying.com/userAgreement.html";
		WebSettings webSettings = webView.getSettings();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl(url);
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
		}
	}

	@Override
	protected void onPause() {
		webView.onPause();
		super.onPause();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
