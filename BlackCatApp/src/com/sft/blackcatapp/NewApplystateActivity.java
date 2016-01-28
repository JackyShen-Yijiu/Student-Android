package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.common.Config;
import com.sft.util.LogUtil;
import com.sft.viewutil.ZProgressHUD;

public class NewApplystateActivity extends BaseActivity implements
		OnClickListener {

	private RadioButton rb_apply_coach;
	private RadioButton rb_apply_school;
	private RadioGroup rgroup_apply;
	private ImageView iv_applystate;
	private ImageView iv_applystates;
	private Intent intent;
	private EditText ed_apply_name;
	private EditText et;
	private Button button_commit;
	private String feedbacktype;
	private String piclist;
	private String becomplainedname;
	private EditText ed_name;
	private static final String complaint = "complaint";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.new_activity_applystate);
		initView();
		Listener();

	}

	@Override
	protected void onResume() {
		// initData();
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.apply_left);
		ed_apply_name = (EditText) findViewById(R.id.ed_apply_name);

		et = (EditText) findViewById(R.id.et_publish_bulletin_content);
		ed_name = (EditText) findViewById(R.id.ed_apply_name);
		rb_apply_coach = (RadioButton) findViewById(R.id.rb_apply_coach);
		rb_apply_school = (RadioButton) findViewById(R.id.rb_apply_school);
		rgroup_apply = (RadioGroup) findViewById(R.id.rgroup_apply);
		iv_applystate = (ImageView) findViewById(R.id.iv_applystate);
		iv_applystates = (ImageView) findViewById(R.id.iv_applystates);
		button_commit = (Button) findViewById(R.id.button_commit);

	}

	private void Listener() {
		iv_applystate.setOnClickListener(this);
		iv_applystates.setOnClickListener(this);

		rgroup_apply.setOnClickListener(this);
		button_commit.setOnClickListener(this);
		rb_apply_coach.setOnClickListener(this);
		rb_apply_school.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 投诉教练
		case R.id.rb_apply_coach:
			ed_apply_name.setHint("投诉教练名");
			feedbacktype = "1";

			break;
		// 投诉驾校
		case R.id.rb_apply_school:
			ed_apply_name.setHint("投诉驾校名");
			feedbacktype = "2";
			break;
		// 添加投诉图片
		case R.id.iv_applystate:
			intent = new Intent(this, NewCropImageActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.iv_applystates:
			intent = new Intent(this, NewCropImageActivity.class);
			startActivityForResult(intent, 2);
			break;

		// 返回
		case R.id.base_left_btn:
			finish();
			break;
		// 提交
		case R.id.button_commit:
			complaint();
			break;
		}
	}

	private void complaint() {
		String content = et.getText().toString();
		String becomplainedname = ed_name.getText().toString();
		if (!TextUtils.isEmpty(content.trim())) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());

			paramMap.put("feedbackmessage", content);
			paramMap.put("mobile", app.userVO.getMobile());
			paramMap.put("name", app.userVO.getName());
			// 投诉 教练，驾校名

			paramMap.put("becomplainedname", becomplainedname);
			// 类型
			paramMap.put("feedbacktype", feedbacktype);
			// 没图 一张 两张的
			paramMap.put("url", getPicUrl());

			HttpSendUtils.httpPostSend(complaint, this, Config.IP
					+ "api/v1/userfeedback", paramMap);

			LogUtil.print("111" + content + getPicUrl() + becomplainedname);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("评论内容不能为空");
		}
	}

	private String getPicUrl() {
		String url = null;
		if (null == url1 && null == url2) {

		}
		if (null == url1 || null == url2) {
			url = url1 == null ? url2 : url1;
		}
		if (null != url1 && null != url2) {
			url = url1 + "," + url2;
		}
		return url;
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		LogUtil.print("2222" + jsonString);

		if (type.equals(complaint)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("反馈成功");
				new MyHandler(1000) {
					@Override
					public void run() {
						finish();
					}
				};
			}
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && null != data) {// 第二章
			url2 = data.getStringExtra("url");
			// BitmapManager.INSTANCE.loadBitmap2(url, iv_applystate, 90, 90);
			BitmapManager.INSTANCE.loadBitmap2(url2, iv_applystates, 90, 90);
		} else if (requestCode == 1 && null != data) {// 第1章
			url1 = data.getStringExtra("url");
			BitmapManager.INSTANCE.loadBitmap2(url1, iv_applystate, 90, 90);
			// BitmapManager.INSTANCE.loadBitmap2(url2, iv_applystates, 90, 90);
		}
	}

	private String url1 = null;

	private String url2 = null;

}
