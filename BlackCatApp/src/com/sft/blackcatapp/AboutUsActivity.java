package com.sft.blackcatapp;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sft.common.Config;
import com.sft.util.DownLoadService;
import com.sft.viewutil.ZProgressHUD;

/**
 * 关于我们
 * 
 * @author Administrator
 * 
 */
public class AboutUsActivity extends BaseActivity {

	private String url = null;
	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_about_us);
		initView();
		initData();
		showDialog();
	}

	private void showDialog() {
		if (getIntent().getBooleanExtra("update", false)) {

			// 如果正在下载，return
			if (isMyServiceRunning()) {
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("发现新版本");
			builder.setMessage(getString(R.string.app_name) + "有新版本啦！");
			builder.setPositiveButton("立即更新",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							startService(new Intent(AboutUsActivity.this,
									DownLoadService.class).putExtra("url",
									app.versionVO.getDownloadUrl()));
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("以后再说",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					});

			builder.create().show();
		}
	}

	private boolean isMyServiceRunning() {
		util.print(DownLoadService.class.getName());
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (DownLoadService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initData() {

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 页面加载完成时，回调
				progress.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
				ZProgressHUD.getInstance(getBaseContext()).show();
				ZProgressHUD.getInstance(getBaseContext()).dismissWithFailure(
						"网络异常");
			}
		});

		settings = webview.getSettings();
		settings.setUseWideViewPort(false);
		settings.setJavaScriptEnabled(true);

		settings.setBuiltInZoomControls(false);
		settings.setSupportZoom(false);
		settings.setDisplayZoomControls(false);

		// String curVersion = util.getAppVersion().replace("v", "")
		// .replace("V", "").replace(".", "");
		webview.loadUrl(Config.VERSION
				+ util.getAppVersion());
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

	private void initView() {
		setTitleText(R.string.about_us);

		webview = (WebView) findViewById(R.id.yibu_introduce_webview);
		progress = (ProgressBar) findViewById(R.id.yibu_introduce_progress);

	}

}
