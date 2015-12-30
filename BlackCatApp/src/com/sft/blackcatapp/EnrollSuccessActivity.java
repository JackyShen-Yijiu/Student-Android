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
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.SharedPreferencesUtil;
import com.sft.vo.SuccessVO;
import com.squareup.picasso.Picasso;

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
		HttpSendUtils.httpGetSend(applySuccess, this, Config.IP
				+ "api/v1/userinfo/getapplyschoolinfo", paramMap, 10000,
				headerMap);
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		app.isEnrollAgain = false;
		showTitlebarBtn(1);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.enroll_again);
		setTitleText("");
		button_sus = (Button) findViewById(R.id.button_sus);
		qrcode = (ImageView) findViewById(R.id.apply_commit_qrcode);
		tv_qrcode = (TextView) findViewById(R.id.tv_qrcode);
		carryData = (TextView) findViewById(R.id.apply_commit_carry_data);
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
			finish();
			break;
		case R.id.base_right_tv:
			// sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
			// "isEnrollSuccess", true));
			// finish();
			app.isEnrollAgain = true;
			app.userVO.setApplystate(EnrollResult.SUBJECT_NONE.getValue());
			SharedPreferencesUtil.putString(getBaseContext(),
					Config.USER_ENROLL_INFO, "");
			Intent intent = new Intent(this, ApplyActivity.class);
			startActivity(intent);
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
	private TextView carryData;

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
					if (successVO == null) {
						LogUtil.print("successVO");
					}
					if (successVO != null) {
						// 保存报名信息
						SharedPreferencesUtil.putString(getBaseContext(),
								Config.USER_ENROLL_INFO, data.toString());
						setQrCode(successVO.scanauditurl);
						tv_qrcode.setText(successVO.userid);
						app.userVO.setApplystate(EnrollResult.SUBJECT_ENROLLING
								.getValue());
						if (successVO.applynotes != null) {
							carryData.setText(successVO.applynotes);
						}
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
		if (!TextUtils.isEmpty(scanauditurl)) {
			String url = Config.IP + "api/v1/create_qrcode?text="
					+ scanauditurl + "&size=10";
			System.out.println(url);
			// BitmapManager.INSTANCE.loadBitmap2(url, qrcode, headParam.width,
			// headParam.height);
			Picasso.with(getBaseContext()).load(url).into(qrcode);
		} else {
			qrcode.setImageResource(R.drawable.default_small_pic);
		}
	}

}
