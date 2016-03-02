package com.sft.blackcatapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.jzjf.app.R;

public class ActivitiesActivity extends Activity implements OnClickListener {

	private String url = null;
	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;
	private ImageView deleteIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activities);
		setFinishOnTouchOutside(false);
		// obtainActivities();
		initView();
		initData();
	}

	private void initData() {

		url = getIntent().getStringExtra("url");
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 页面加载完成时，回调
				progress.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});

		settings = webview.getSettings();
		settings.setUseWideViewPort(false);
		settings.setJavaScriptEnabled(true);

		settings.setBuiltInZoomControls(false);
		settings.setSupportZoom(false);
		settings.setDisplayZoomControls(false);

		webview.loadUrl(url);
	}

	private void initView() {
		webview = (WebView) findViewById(R.id.activities_webview);
		progress = (ProgressBar) findViewById(R.id.activities_progress);
		deleteIv = (ImageView) findViewById(R.id.activities_delete);
		deleteIv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.activities_delete) {
			this.finish();
		}
	}

}
