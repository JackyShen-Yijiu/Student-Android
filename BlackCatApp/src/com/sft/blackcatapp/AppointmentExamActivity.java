package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.dialog.DateTimeDialog;
import com.sft.util.LogUtil;
import com.sft.viewutil.ZProgressHUD;

public class AppointmentExamActivity extends BaseActivity {
	private final String applyexam = "applyexam";
	private String exampractice = "1"; // 是否考场练车 0 否 1 是

	private TextView beginTimeTv;
	private TextView endTimeTv;

	private Calendar beginDate = Calendar.getInstance();
	private Calendar endDate = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_appointment_exam);
		setTitleText(R.string.driving_school_appointment_exam);

		initView();
		// initDatePicker();

	}

	private void initView() {

		TextView userNameTv = (TextView) findViewById(R.id.appointment_exam_username_tv);
		TextView userPhoneTv = (TextView) findViewById(R.id.appointment_exam_user_phone_tv);
		beginTimeTv = (TextView) findViewById(R.id.appointmet_exam_begin_time_tv);
		endTimeTv = (TextView) findViewById(R.id.appointmet_exam_end_time_tv);

		if (app.userVO != null) {
			userNameTv.setText(app.userVO.getName());
			userPhoneTv.setText(app.userVO.getMobile());
		}

		Button applyBtn = (Button) findViewById(R.id.appointment_exam_apply_btn);
		applyBtn.setOnClickListener(this);
		beginTimeTv.setOnClickListener(this);
		endTimeTv.setOnClickListener(this);
	}

	private void initDatePicker() {
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		// DatePickerUtil.resizePicker(startDatePicker, width);
		// DatePickerUtil.resizePicker(endDatePicker, width);
		// DatePickerUtil.setDatePickerDividerColor(startDatePicker);
		// DatePickerUtil.setDatePickerDividerColor(endDatePicker);

		// Calendar calendar = Calendar.getInstance();
		// startDatePicker.init(calendar.get(Calendar.YEAR),
		// calendar.get(Calendar.MONTH),
		// calendar.get(Calendar.DAY_OF_MONTH),
		// new OnDateChangedListener() {
		//
		// @Override
		// public void onDateChanged(DatePicker view, int year,
		// int monthOfYear, int dayOfMonth) {
		// exambegintime = year + "-" + (monthOfYear + 1) + "-"
		// + dayOfMonth;
		// if (monthOfYear + 1 < 10) {
		// if (dayOfMonth + 1 < 10) {
		// applyBeginTime = "0" + (monthOfYear + 1) + "月0"
		// + dayOfMonth + "日";
		// } else {
		// applyBeginTime = "0" + (monthOfYear + 1) + "月"
		// + dayOfMonth + "日";
		// }
		// } else {
		// applyBeginTime = (monthOfYear + 1) + "月"
		// + dayOfMonth + "日";
		// }
		// // applyBeginTime = monthOfYear + "月" + dayOfMonth +
		// // "日";
		// selectDateTv.setText("约考时间段" + applyBeginTime + "至"
		// + applyEndTime);
		// }
		// });
		// endDatePicker.init(calendar.get(Calendar.YEAR),
		// calendar.get(Calendar.MONTH),
		// calendar.get(Calendar.DAY_OF_MONTH),
		// new OnDateChangedListener() {
		//
		// @Override
		// public void onDateChanged(DatePicker view, int year,
		// int monthOfYear, int dayOfMonth) {
		// // 获取一个日历对象，并初始化为当前选中的时间
		// // 约考时间段12月30日至12月20日
		// examendtime = year + "-" + (monthOfYear + 1) + "-"
		// + dayOfMonth;
		// if (monthOfYear + 1 < 10) {
		// if (dayOfMonth + 1 < 10) {
		// applyEndTime = "0" + (monthOfYear + 1) + "月0"
		// + dayOfMonth + "日";
		// } else {
		// applyEndTime = "0" + (monthOfYear + 1) + "月"
		// + dayOfMonth + "日";
		// }
		// } else {
		// applyEndTime = (monthOfYear + 1) + "月" + dayOfMonth
		// + "日";
		// }
		// selectDateTv.setText("约考时间段" + applyBeginTime + "至"
		// + applyEndTime);
		// }
		// });
	}

	private void obtainApplyExamination() {

		if (check()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String subjectid = getIntent().getStringExtra("subjectid");
			LogUtil.print("subjectid-" + subjectid);
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("exambegintime", format.format(beginDate.getTime()));
			paramsMap.put("examendtime", format.format(endDate.getTime()));
			paramsMap.put("exammobile", app.userVO.getMobile());
			paramsMap.put("examname", app.userVO.getName());
			paramsMap.put("exampractice", exampractice);
			paramsMap.put("subjectid", subjectid);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			// LogUtil.print(paramsMap.toString());
			HttpSendUtils.httpPostSend(applyexam, this, Config.IP
					+ "api/v1/userinfo/applyexamination", paramsMap, 10000,
					headerMap);
		}
	}

	private boolean check() {
		if (beginTimeTv.getText().toString().contains("起始")) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("请选择起始日期");
			return false;
		}
		if (endTimeTv.getText().toString().contains("最后")) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("请选择最后日期");
			return false;
		}

		if (beginDate.after(endDate)) {

			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("开始时间不能大于结束时间");
			return false;
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
					startActivity(new Intent(this,
							AppointmentExamSuccessActivity.class));
					// ZProgressHUD.getInstance(this).show();
					// ZProgressHUD.getInstance(this).dismissWithSuccess("约考成功");
					// new MyHandler(1000) {
					// @Override
					// public void run() {
					// finish();
					// }
					// };
				}
			}

		} catch (Exception e) {
		}
		return true;
	}

	private int flag = 1; // 1---起始日期 2---最后日期

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.appointment_exam_apply_btn:
			obtainApplyExamination();
			break;
		case R.id.appointmet_exam_begin_time_tv:

			// 弹出dialog
			flag = 1;
			beginTimeTv.setText(beginDate.get(Calendar.YEAR) + "-"
					+ (beginDate.get(Calendar.MONTH) + 1) + "-"
					+ beginDate.get(Calendar.DAY_OF_MONTH));
			openDialog("起始日期", beginDate);
			break;
		case R.id.appointmet_exam_end_time_tv:
			flag = 2;
			endTimeTv.setText(endDate.get(Calendar.YEAR) + "-"
					+ (endDate.get(Calendar.MONTH) + 1) + "-"
					+ endDate.get(Calendar.DAY_OF_MONTH));
			openDialog("最后日期", endDate);
			break;
		}
	}

	private void openDialog(String tag, Calendar date) {
		DateTimeDialog dateDialog = new DateTimeDialog(this);
		dateDialog.setData(tag, date, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				if (flag == 1) {
					// 起始日期
					beginDate.set(year, monthOfYear, dayOfMonth);
					beginTimeTv.setText(year + "-" + (monthOfYear + 1) + "-"
							+ dayOfMonth);
				} else {
					// 最后日期
					endDate.set(year, monthOfYear, dayOfMonth);
					endTimeTv.setText(year + "-" + (monthOfYear + 1) + "-"
							+ dayOfMonth);
				}

			}
		});
		dateDialog.show();
	}
}
