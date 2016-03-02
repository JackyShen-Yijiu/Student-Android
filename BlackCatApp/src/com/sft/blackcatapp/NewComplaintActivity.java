package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.common.Config;
import com.sft.util.LogUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachVO;
/**
 * 新 投诉
 * @author sun  2016-3-1 下午8:48:43
 *
 */
public class NewComplaintActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	private static final String complaint = "complaint";

	private EditText contentEt;

	private RadioGroup feedbackusertypeRg;

	private ImageView commitPic2Iv;

	private Button commitBtn;

	private ImageView commitPicIv1;
	private String url1 = null;

	private String url2 = null;

	private RadioGroup feedbacktypeRg;

	private RadioButton feedbacktypeSchoolRb;

	private RadioButton feedbacktypeCoachRb;

	private RadioButton feedbackusertypeAnonymous;

	private RadioButton feedbackusertypeRealName;

	private String feedbackusertype = "0";// //投诉类型 0 匿名投诉 1 实名投诉

	private String feedbacktype = "1"; // // 反馈类型 0 平台反馈 1 投诉教练 2 投诉驾校

	private TextView coachNameTv;

	private TextView phoneTv;

	private TextView complaint_coach_show;

	private TextView chCounterText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.new_activity_applystate);
		initView();
		initData();
		Listener();
		init();
	}

	private void init() {

		contentEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String content = contentEt.getText().toString();
				chCounterText.setText("投诉内容 " + content.length() + "/" + "200");
			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});
	}

	// 初始化数据
	private void initData() {
		if (app != null && app.userVO != null
				&& app.userVO.getApplycoachinfo() != null) {
			coachNameTv.setText(app.userVO.getApplycoachinfo().getName());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.apply_left);
		feedbacktypeRg = (RadioGroup) findViewById(R.id.complaint_feedbacktype_rg);
		feedbacktypeCoachRb = (RadioButton) findViewById(R.id.complaint_feedbacktype_coach);
		feedbacktypeSchoolRb = (RadioButton) findViewById(R.id.complaint_feedbacktype_school);

		feedbackusertypeRg = (RadioGroup) findViewById(R.id.complaint_feedbackusertype);
		feedbackusertypeAnonymous = (RadioButton) findViewById(R.id.complaint_feedbackusertype_anonymous);
		feedbackusertypeRealName = (RadioButton) findViewById(R.id.complaint_feedbackusertype_realname);

		coachNameTv = (TextView) findViewById(R.id.complaint_coach_name_tv);

		complaint_coach_show = (TextView) findViewById(R.id.complaint_coach_show);
		contentEt = (EditText) findViewById(R.id.complaint_content);
		phoneTv = (TextView) findViewById(R.id.complaint_coach_phone_tv);
		chCounterText = (TextView) findViewById(R.id.sdk_status_edit_text);
		commitPicIv1 = (ImageView) findViewById(R.id.complaint_commit_pic1);
		commitPic2Iv = (ImageView) findViewById(R.id.complaint_commit_pic2);
		commitBtn = (Button) findViewById(R.id.button_commit);

		phoneTv.setText(app.userVO.getTelephone());
	}

	private void Listener() {
		feedbacktypeRg.setOnCheckedChangeListener(this);
		feedbackusertypeRg.setOnCheckedChangeListener(this);
		commitPicIv1.setOnClickListener(this);
		commitPic2Iv.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		coachNameTv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.complaint_commit_pic1:
			intent = new Intent(this, NewCropImageActivity.class);
			startActivityForResult(intent, v.getId());
			break;
		case R.id.complaint_commit_pic2:
			intent = new Intent(this, NewCropImageActivity.class);
			startActivityForResult(intent, v.getId());
			break;

		// 返回
		case R.id.base_left_btn:
			finish();
			break;
		// 提交
		case R.id.button_commit:
			complaint();
			break;
		case R.id.complaint_coach_name_tv:
			// 弹出教练列表
			intent = new Intent(this, AppointmentMoreCoachActivity.class);
			startActivityForResult(intent, v.getId());
			break;
		}
	}

	private void complaint() {
		String content = contentEt.getText().toString();
		String becomplainedname = coachNameTv.getText().toString();
		if (!TextUtils.isEmpty(content.trim())) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());

			paramMap.put("feedbackmessage", content);

			ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获取网络的状态信息，有下面三种方式
			NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
			String networkName = networkInfo.getTypeName();
			if (networkName.equalsIgnoreCase("MOBILE")) {
				networkName += networkInfo.getExtraInfo();
			}
			paramMap.put("mobileversion", VERSION.RELEASE);
			paramMap.put("network", networkName);
			paramMap.put("resolution", screenWidth + "*" + screenHeight);
			paramMap.put("appversion", util.getAppVersion());

			paramMap.put("feedbacktype", feedbacktype);
			paramMap.put("name", app.userVO.getName());
			paramMap.put("feedbackusertype", feedbackusertype);
			paramMap.put("mobile", app.userVO.getMobile());
			// 投诉 教练，驾校名

			paramMap.put("becomplainedname", becomplainedname);
			// 类型
			// 没图 一张 两张的
			paramMap.put("piclist", getPicUrl());

			LogUtil.print("111" + paramMap);
			HttpSendUtils.httpPostSend(complaint, this, Config.IP
					+ "api/v1/userfeedback", paramMap);

		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("投诉内容不能为空");
		}
	}

	private String getPicUrl() {
		String url = "";
		if (null != url1 && null != url2) {
			url = url1 + "," + url2;
		} else {
			if (null != url1) {
				url = url1;
			}
			if (null != url2) {
				url = url2;
			}
		}

		return url;
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		if (type.equals(complaint)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("投诉成功");
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
		if (null != data) {

			if (requestCode == R.id.complaint_commit_pic2) {// 第二章
				url2 = data.getStringExtra("url");
				// BitmapManager.INSTANCE.loadBitmap2(url, iv_applystate, 90,
				// 90);
				BitmapManager.INSTANCE.loadBitmap2(url2, commitPic2Iv, 90, 90);
			} else if (requestCode == R.id.complaint_commit_pic1) {// 第1章
				url1 = data.getStringExtra("url");
				BitmapManager.INSTANCE.loadBitmap2(url1, commitPicIv1, 90, 90);
				// BitmapManager.INSTANCE.loadBitmap2(url2, iv_applystates, 90,
				// 90);
			} else if (requestCode == R.id.complaint_coach_name_tv) {
				CoachVO coach = (CoachVO) data.getSerializableExtra("coach");
				if (coach != null) {
					coachNameTv.setText(coach.getName());
					complaint_coach_show
							.setText("投诉 " + coach.getName() + "教练");
				}
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkId) {

		switch (checkId) {
		// 投诉教练
		case R.id.complaint_feedbacktype_coach:
			coachNameTv.setText(app.userVO.getApplycoachinfo().getName());
			coachNameTv.setCompoundDrawables(null, null, null, getResources()
					.getDrawable(R.drawable.person_center_arrow));
			coachNameTv.setEnabled(true);
			complaint_coach_show.setText("投诉 "
					+ app.userVO.getApplycoachinfo().getName() + "教练");
			feedbacktype = "1";
			break;
		// 投诉驾校
		case R.id.complaint_feedbacktype_school:
			coachNameTv.setText(app.userVO.getApplyschoolinfo().getName());
			coachNameTv.setCompoundDrawables(null, null, null, null);
			coachNameTv.setEnabled(false);
			feedbacktype = "2";
			complaint_coach_show.setText("投诉 "
					+ app.userVO.getApplyschoolinfo().getName() + "驾校");
			break;
		// 匿名投诉
		case R.id.complaint_feedbackusertype_anonymous:
			feedbackusertype = "0";
			break;
		// 实名投诉
		case R.id.complaint_feedbackusertype_realname:
			feedbackusertype = "1";
			break;

		default:
			break;
		}
	}
}
