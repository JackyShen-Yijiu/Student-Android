package com.sft.blackcatapp;

import java.util.Calendar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.viewutil.ZProgressHUD;

public class AppointmentExamSuccessActivity extends BaseActivity {
	private final String applyexam = "applyexam";
	private String exampractice = "1"; // 是否考场练车 0 否 1 是

	private TextView beginTimeTv;
	private TextView endTimeTv;

	private Calendar beginDate = Calendar.getInstance();
	private Calendar endDate = Calendar.getInstance();
	private TextView titleTv;
	private TextView statusTv;
	private TextView timeTv;
	private TextView addressTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_appointment_exam_success);
		setTitleText(R.string.appointment_exam_success);

		initView();

		initData();
	}

	private void initView() {
		titleTv = (TextView) findViewById(R.id.appointment_exam_success_title_tv);
		statusTv = (TextView) findViewById(R.id.appointment_exam_success_status_tv);
		timeTv = (TextView) findViewById(R.id.appointment_exam_success_time_tv);
		addressTv = (TextView) findViewById(R.id.appointment_exam_success_address_tv);
	}

	private void initData() {
		if (!TextUtils.isEmpty(app.selectEnrollSchool.getPhone())) {
			addressTv.setText("如有疑问请拨打客服：" + app.selectEnrollSchool.getPhone());
		} else {
			addressTv.setText("");
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (!TextUtils.isEmpty(msg)) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithSuccess(msg);
			return true;
		}
		try {

		} catch (Exception e) {
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		}
	}

}
