package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.UserBaseStateVO;

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
			// url = "http://123.57.63.15:8181/liuchengt.html";
			setTitleText(R.string.yibu_procedure);
			Intent intent = new Intent(this, EnrollSchoolActivity.class);
			intent.putExtra("isFromMenu", true);
			startActivityForResult(intent, 1);
			finish();
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

	private void checkUserEnrollState() {
		if (!app.userVO.getApplystate().equals(
				Config.EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("userid", app.userVO.getUserid());
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(checkEnrollState, this, Config.IP
					+ "api/v1/userinfo/getmyapplystate", paramsMap, 10000,
					headerMap);
		} else {
			enroll();
		}
	}

	private static final String checkEnrollState = "checkEnrollState";

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

				checkUserEnrollState();

			}
			break;
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (data != null) {
				UserBaseStateVO baseStateVO = JSONUtil.toJavaBean(
						UserBaseStateVO.class, data);
				if (!baseStateVO.getApplystate().equals(
						app.userVO.getApplystate())) {
					app.userVO.setApplystate(baseStateVO.getApplystate());

				}
				enroll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void enroll() {
		String enrollState = app.userVO.getApplystate();
		if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollState)) {
			Intent intent = new Intent(this, ApplyActivity.class);
			startActivity(intent);
			finish();

		} else if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
				app.userVO.getApplystate())) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithSuccess("审核已通过，不能再重新报名！");
		} else {
			Intent intent = new Intent(this, EnrollSuccessActivity.class);
			startActivity(intent);
			finish();

		}
	}
}
