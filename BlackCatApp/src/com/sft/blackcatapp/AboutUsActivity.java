package com.sft.blackcatapp;

import com.sft.util.DownLoadService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 关于我们
 * 
 * @author Administrator
 * 
 */
public class AboutUsActivity extends BaseActivity {

	// 版本
	private TextView versionTv;
	// 服务协议
	private TextView protocalTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_about_us);
		initView();
		setListener();
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
			builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					startService(new Intent(AboutUsActivity.this, DownLoadService.class).putExtra("url",
							app.versionVO.getDownloadUrl()));
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			});

			builder.create().show();
		}
	}

	private boolean isMyServiceRunning() {
		util.print(DownLoadService.class.getName());
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (DownLoadService.class.getName().equals(service.service.getClassName())) {
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

	private void initView() {
		setTitleText(R.string.about_us);

		versionTv = (TextView) findViewById(R.id.aboutus_version_tv);
		protocalTv = (TextView) findViewById(R.id.aboutus_protocal_tv);

		findViewById(R.id.aboutus_im).getLayoutParams().height = (int) (screenHeight * 0.23f);

		versionTv.setText("V" + util.getAppVersion());

	}

	private void setListener() {
		protocalTv.setOnClickListener(this);
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
		case R.id.aboutus_protocal_tv:
			Intent intent = new Intent(this, TermsActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void forOperResult(Intent intent) {
		if (intent.getBooleanExtra("newVersion", false)) {
			Intent service = new Intent(this, DownLoadService.class);
			service.putExtra("url", app.versionVO.getDownloadUrl());
			startActivity(service);
		}
	}
}
