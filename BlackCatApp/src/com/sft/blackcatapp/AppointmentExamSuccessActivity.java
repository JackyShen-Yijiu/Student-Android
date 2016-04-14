package com.sft.blackcatapp;

import java.util.Calendar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyExamInfoVO;

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
		MyExamInfoVO examInfoVO = (MyExamInfoVO) getIntent()
				.getSerializableExtra("examInfoVO");

		if (examInfoVO != null) {
			if (examInfoVO.getExaminationstate().equals(
					Config.MyExamInfo.EXAMINATIONING.getValue())) {
				// 申请中
				titleTv.setText(CommonUtil.getString(this,
						R.string.congratulations_appointment_exam_finish));
				statusTv.setText(CommonUtil.getString(this,
						R.string.appointmenting));
				timeTv.setText(CommonUtil.getString(this,
						R.string.appointment_exam_tip));
				LogUtil.print("====---"
						+ app.userVO.getApplyschoolinfo().getMobile());
				if (app.userVO.getApplyschoolinfo() != null
						&& !TextUtils.isEmpty(app.userVO.getApplyschoolinfo()
								.getMobile())) {
					addressTv.setText("如有疑问请拨打客服："
							+ app.userVO.getApplyschoolinfo().getMobile());
				} else {
					addressTv.setText("");
				}
			} else if (examInfoVO.getExaminationstate().equals(
					Config.MyExamInfo.EXAMINATION_FINISH.getValue())) {
				// 已安排
				titleTv.setText(CommonUtil.getString(this,
						R.string.congratulations_appointment_exam_success));
				statusTv.setText(CommonUtil.getString(this,
						R.string.appointment_success));
				timeTv.setText("考试时间  "
						+ UTC2LOC.instance.getDate(
								examInfoVO.getExaminationdate(), "yyyy/MM/dd"));
				addressTv.setText("考试地点  " + examInfoVO.getExamaddress());
			}
		} else {
			if (app.userVO.getApplyschoolinfo() != null
					&& !TextUtils.isEmpty(app.userVO.getApplyschoolinfo()
							.getMobile())) {
				addressTv.setText("如有疑问请拨打客服："
						+ app.userVO.getApplyschoolinfo().getMobile());
			} else {
				addressTv.setText("");
			}
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
