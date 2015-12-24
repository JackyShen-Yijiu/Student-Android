package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sft.dialog.NoLoginDialog;

public class YiBuIntroduceActivity extends BaseActivity {

	private String url = null;
	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_yibu_introduce);
		initView();
		initData();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		showTitlebarBtn(1);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.enroll);

		// button_sus = (Button) findViewById(R.id.button_sus);

		webview = (WebView) findViewById(R.id.yibu_introduce_webview);
		progress = (ProgressBar) findViewById(R.id.yibu_introduce_progress);
	}

	private void setListener() {
		// button_sus.setOnClickListener(this);

	}

	private void initData() {
		int typeId = getIntent().getIntExtra("typeId", 0);
		switch (typeId) {
		case R.id.introduce_superiority:
			url = "http://123.57.63.15:8181/youshi.html";
			setTitleText(R.string.yibu_superiority);
			break;
		case R.id.introduce_favourable_class:
			url = "http://123.57.63.15:8181/3.html";
			setTitleText(R.string.yibu_favourable_class);

			break;
		case R.id.introduce_procedure:
			url = "http://123.57.63.15:8181/liuchengt.html";
			setTitleText(R.string.yibu_procedure);
			break;

		}

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

		webview.loadUrl(url);
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
			// 报名
			if (!app.isLogin) {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			} else {

				Intent intent = new Intent(this, EnrollActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		}
	}
}
