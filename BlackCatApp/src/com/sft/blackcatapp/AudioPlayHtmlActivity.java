package com.sft.blackcatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jzjf.app.R;
import com.sft.vo.VideoVO;

/**
 * 试题
 * 
 * @author Administrator
 * 
 */
public class AudioPlayHtmlActivity extends BaseActivity {

	private WebView webView;
	private VideoVO videoVO;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_question);
		webView = (WebView) findViewById(R.id.question_webview);
		progressBar = (ProgressBar) findViewById(R.id.question_progress);
		findViewById(R.id.base_left_btn11).setVisibility(View.GONE);
		initData();
	}

	@Override
	protected void onResume() {
		webView.onResume();
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		videoVO = (VideoVO) getIntent().getSerializableExtra("video");
		setTitleText(videoVO.getName());
		WebSettings webSettings = webView.getSettings();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
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
		String url = videoVO.getVideourl();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
