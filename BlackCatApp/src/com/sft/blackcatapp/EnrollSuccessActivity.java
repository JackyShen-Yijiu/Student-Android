package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.SuccessVO;

/**
 * 报名成功提示界面
 * 
 * @author Administrator
 * 
 */
public class EnrollSuccessActivity extends BaseActivity {

	private static final String applySuccess = "applySuccess";
	private Button button_sus;
	private TextView tv_qrcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.apply_commit);
		initView();

		setListener();
		obtainApplySuccessInfo();
	}

	private void obtainApplySuccessInfo() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(applySuccess, this, Config.IP
				+ "api/v1/userinfo/getapplyschoolinfo", paramMap, 10000,
				headerMap);
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		showTitlebarBtn(0);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.finish);
		setTitleText(R.string.enroll);
		button_sus = (Button) findViewById(R.id.button_sus);
		qrcode = (ImageView) findViewById(R.id.apply_commit_qrcode);
		tv_qrcode = (TextView) findViewById(R.id.tv_qrcode);
	}

	private void setListener() {
		button_sus.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
		case R.id.base_right_tv:
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
			finish();
			break;
		case R.id.button_sus:
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
			finish();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
		}
		return super.onKeyDown(keyCode, event);
	}

	private ImageView qrcode;

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (type.equals(applySuccess)) {
				if (data != null) {
					SuccessVO successVO = JSONUtil.toJavaBean(SuccessVO.class,
							data);
					if (successVO != null) {
						setQrCode(successVO.scanauditurl);
						tv_qrcode.setText(successVO.userid);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 设置二维码
	private void setQrCode(String scanauditurl) {
		LinearLayout.LayoutParams headParam = (LayoutParams) qrcode
				.getLayoutParams();
		if (!TextUtils.isEmpty(scanauditurl)) {
			String url = Config.IP + "api/v1/create_qrcode?text="
					+ scanauditurl + "&size=10";
			BitmapManager.INSTANCE.loadBitmap2(url, qrcode, headParam.width,
					headParam.height);
		} else {
			qrcode.setImageResource(R.drawable.default_small_pic);
		}
	}

}
