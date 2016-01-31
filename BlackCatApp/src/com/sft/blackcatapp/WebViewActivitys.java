package com.sft.blackcatapp;

import java.util.List;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sft.vo.ActivitiesVO;

public class WebViewActivitys extends BaseActivity implements OnClickListener {
	private WebView webView;
	private List<ActivitiesVO> mData;
	private ActivitiesVO activitiesVO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);

		webView = (WebView) findViewById(R.id.activitys_webview);
		webView.setOnClickListener(this);
		initListener();
	}

	private void initListener() {
		String activityUrl = getIntent().getStringExtra("url");
		WebSettings webSettings = webView.getSettings();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl(activityUrl);
	}
}
