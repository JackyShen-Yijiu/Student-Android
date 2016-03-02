package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.util.DatePickerUtil;
import com.sft.util.LogUtil;
import com.sft.viewutil.ZProgressHUD;

public class AppointmentExamActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private final String applyexam = "applyexam";
	private DatePicker startDatePicker;
	private DatePicker endDatePicker;
	private EditText nameEt;
	private EditText phoneEt;
	private TextView phoneErrorTv;
	private RadioGroup testDriveRg;
	private TextView selectDateTv;
	private String exambegintime;
	private String examendtime;
	private String exampractice = "1"; // 是否考场练车 0 否 1 是

	private String applyBeginTime;
	private String applyEndTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_appointment_exam);
		setTitleText(R.string.driving_school_appointment_exam);

		initView();
		initData();
		initDatePicker();

	}

	private void initView() {

		nameEt = (EditText) findViewById(R.id.appointment_exam_name_edit_et);
		phoneEt = (EditText) findViewById(R.id.appointment_exam_phone_edit_et);
		phoneErrorTv = (TextView) findViewById(R.id.appointment_exam_phone_error_hint);

		startDatePicker = (DatePicker) findViewById(R.id.appointment_exam_start_datepicker);
		endDatePicker = (DatePicker) findViewById(R.id.appointment_exam_end_datepicker);

		testDriveRg = (RadioGroup) findViewById(R.id.appointment_exam_test_drive_rg);
		testDriveRg.setOnCheckedChangeListener(this);

		selectDateTv = (TextView) findViewById(R.id.appointment_exam_select_date_tv);
		Button applyBtn = (Button) findViewById(R.id.appointment_exam_apply_btn);
		applyBtn.setOnClickListener(this);
	}

	private void initData() {

		if (app.userVO != null) {
			nameEt.setText(app.userVO.getName());
			phoneEt.setText(app.userVO.getMobile());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat applyformat = new SimpleDateFormat("MM月dd日");
		Date date = new Date();
		exambegintime = format.format(date);
		examendtime = format.format(date);
		applyBeginTime = applyformat.format(date);
		applyEndTime = applyformat.format(date);
		selectDateTv.setText("约考时间段" + applyBeginTime + "至" + applyEndTime);
	}

	private void initDatePicker() {
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		DatePickerUtil.resizePicker(startDatePicker, width);
		DatePickerUtil.resizePicker(endDatePicker, width);
		DatePickerUtil.setDatePickerDividerColor(startDatePicker);
		DatePickerUtil.setDatePickerDividerColor(endDatePicker);

		Calendar calendar = Calendar.getInstance();
		startDatePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						exambegintime = year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth;
						if (monthOfYear + 1 < 10) {
							if (dayOfMonth + 1 < 10) {
								applyBeginTime = "0" + (monthOfYear + 1) + "月0"
										+ dayOfMonth + "日";
							} else {
								applyBeginTime = "0" + (monthOfYear + 1) + "月"
										+ dayOfMonth + "日";
							}
						} else {
							applyBeginTime = (monthOfYear + 1) + "月"
									+ dayOfMonth + "日";
						}
						// applyBeginTime = monthOfYear + "月" + dayOfMonth +
						// "日";
						selectDateTv.setText("约考时间段" + applyBeginTime + "至"
								+ applyEndTime);
					}
				});
		endDatePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// 获取一个日历对象，并初始化为当前选中的时间
						// 约考时间段12月30日至12月20日
						examendtime = year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth;
						if (monthOfYear + 1 < 10) {
							if (dayOfMonth + 1 < 10) {
								applyEndTime = "0" + (monthOfYear + 1) + "月0"
										+ dayOfMonth + "日";
							} else {
								applyEndTime = "0" + (monthOfYear + 1) + "月"
										+ dayOfMonth + "日";
							}
						} else {
							applyEndTime = (monthOfYear + 1) + "月" + dayOfMonth
									+ "日";
						}
						selectDateTv.setText("约考时间段" + applyBeginTime + "至"
								+ applyEndTime);
					}
				});
	}

	private void obtainApplyExamination() {
		if (checkInfo()) {

			String subjectid = getIntent().getStringExtra("subjectid");
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("exambegintime", exambegintime);
			paramsMap.put("examendtime", examendtime);
			paramsMap.put("exammobile", phoneEt.getText().toString());
			paramsMap.put("examname", nameEt.getText().toString());
			paramsMap.put("exampractice", exampractice);
			paramsMap.put("subjectid", subjectid);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
//			LogUtil.print(paramsMap.toString());
			HttpSendUtils.httpPostSend(applyexam, this, Config.IP
					+ "api/v1/userinfo/applyexamination", paramsMap, 10000,
					headerMap);
		}
	}

	private boolean checkInfo() {
		String phone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			phoneErrorTv.setVisibility(View.VISIBLE);
			return false;
		} else if (!CommonUtil.isMobile(phone)) {
			phoneErrorTv.setVisibility(View.VISIBLE);
			return false;
		} else {
			phoneErrorTv.setVisibility(View.INVISIBLE);
		}

		return true;
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
			if (type.equals(applyexam)) {
				if (dataString != null) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithSuccess("约考成功");
					new MyHandler(1000) {
						@Override
						public void run() {
							finish();
						}
					};
				}
			}

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
		case R.id.appointment_exam_apply_btn:
			obtainApplyExamination();
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkId) {

		switch (checkId) {
		case R.id.appointment_exam_test_drive_rb:
			exampractice = "1";
			break;

		case R.id.appointment_exam_no_test_drive_rb:
			exampractice = "0";
			break;
		}
	}
}
