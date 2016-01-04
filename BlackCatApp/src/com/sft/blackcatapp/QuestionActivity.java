package com.sft.blackcatapp;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sft.blackcatapp.R;

/**
 * 试题
 * 
 * @author Administrator
 * 
 */
public class QuestionActivity extends BaseActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_question);
		webView = (WebView) findViewById(R.id.question_webview);
		// setTitleBarVisible(View.GONE);
		showTitlebarBtn(1);
		setTitleText("");
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		WebSettings webSettings = webView.getSettings();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginState(PluginState.ON);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setDefaultTextEncodingName("UTF-8");
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);

		webView.setWebChromeClient(new WebChromeClient());
		String url = getIntent().getStringExtra("url");
		if (app.isLogin) {
			url += ("?userid=" + app.userVO.getUserid());
		}
		webView.loadUrl(url);
	}

	@Override
	protected void onPause() {
		webView.loadUrl("javascript:save()");
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.base_left_btn)
			finish();
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
