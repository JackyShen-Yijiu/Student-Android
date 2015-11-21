package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.common.Config.SubjectStatu;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.UserBaseStateVO;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

/**
 * 科目三
 * 
 * @author Administrator
 * 
 */
public class SubjectThreeActivity extends BaseActivity {

	private static final String checkEnrollState_my = "checkEnrollStatemy";
	private static final String checkEnrollState_car = "checkEnrollStatecar";
	//
	private LinearLayout twoLayout;
	//
	private LinearLayout threeLayout;
	//
	private RelativeLayout approintmentBtn;
	private RelativeLayout approintmentCarBtn, courseBtn, walletBtn, messageBtn, myBtn;

	private TextView appointmentText, appointmentCarText, courseText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_subject_two);
		initView();
		resizeLayout();
		setListener();
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleBarVisible(View.GONE);
		showTitlebarBtn(0);
		setTitleText(R.string.school);

		twoLayout = (LinearLayout) findViewById(R.id.main_two_layout);
		threeLayout = (LinearLayout) findViewById(R.id.main_three_layout);

		approintmentBtn = (RelativeLayout) findViewById(R.id.main_appointment_layout);
		approintmentCarBtn = (RelativeLayout) findViewById(R.id.main_appointment_car_layout);
		courseBtn = (RelativeLayout) findViewById(R.id.main_appointment_course_layout);
		walletBtn = (RelativeLayout) findViewById(R.id.main_wallet_layout);
		messageBtn = (RelativeLayout) findViewById(R.id.main_message_layout);
		myBtn = (RelativeLayout) findViewById(R.id.main_my_layout);

		appointmentText = (TextView) findViewById(R.id.main_appointment_tv);
		appointmentCarText = (TextView) findViewById(R.id.main_appointment_car_tv);
		courseText = (TextView) findViewById(R.id.main_appointment_course_tv);

		
		ImageView imageAppointment = (ImageView) findViewById(R.id.main_appointment_im);
		RelativeLayout.LayoutParams paramsAppointment = (RelativeLayout.LayoutParams) imageAppointment
				.getLayoutParams();
		paramsAppointment.width = (int) (screenDensity * 112);
		paramsAppointment.height = (int) (screenDensity * 136);

		ImageView imageAppointmentCar = (ImageView) findViewById(R.id.main_appointment_car_im);
		RelativeLayout.LayoutParams paramsAppointmentCar = (RelativeLayout.LayoutParams) imageAppointmentCar
				.getLayoutParams();
		paramsAppointmentCar.width = (int) (screenDensity * 67);
		paramsAppointmentCar.height = (int) (screenDensity * 63);

		ImageView imageCourse = (ImageView) findViewById(R.id.main_appointment_course_im);
		RelativeLayout.LayoutParams paramsCourse = (RelativeLayout.LayoutParams) imageCourse.getLayoutParams();
		paramsCourse.width = (int) (screenDensity * 86);
		paramsCourse.height = (int) (screenDensity * 53);
		
		appointmentText.setText("科三预约列表");
		appointmentCarText.setText("我要预约");
		courseText.setText("科三课件");
		
		findViewById(R.id.main_appointment_layout).setBackgroundColor(Color.parseColor("#ff6633"));
		findViewById(R.id.main_appointment_car_layout).setBackgroundColor(Color.parseColor("#ff9900"));
		findViewById(R.id.main_appointment_course_layout).setBackgroundColor(Color.parseColor("#bd1f4a"));
		findViewById(R.id.main_wallet_layout).setBackgroundColor(Color.parseColor("#01a300"));
		findViewById(R.id.main_message_layout).setBackgroundColor(Color.parseColor("#0094a6"));
		findViewById(R.id.main_my_layout).setBackgroundColor(Color.parseColor("#2d8aef"));

	}

	private void resizeLayout() {
		LinearLayout.LayoutParams threeLayoutParams = (LinearLayout.LayoutParams) threeLayout.getLayoutParams();
		threeLayoutParams.height = (int) ((screenWidth - 12 * screenDensity) / 3);
		LinearLayout.LayoutParams twoLayoutParams = (LinearLayout.LayoutParams) twoLayout.getLayoutParams();
		twoLayoutParams.height = (int) ((screenWidth - 11 * screenDensity) * 2 / 3);
	}

	private void setListener() {
		approintmentBtn.setOnClickListener(this);
		approintmentCarBtn.setOnClickListener(this);
		courseBtn.setOnClickListener(this);
		walletBtn.setOnClickListener(this);
		messageBtn.setOnClickListener(this);
		myBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		if (!app.isLogin) {
			NoLoginDialog dialog = new NoLoginDialog(this);
			dialog.show();
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.main_appointment_layout:
			if (app.userVO.getApplystate().equals(EnrollResult.SUBJECT_NONE.getValue())) {
				intent = new Intent(this, EnrollVerifyActivity.class);
			} else {
				checkUserEnrollState(checkEnrollState_my);
			}
			break;
		case R.id.main_appointment_course_layout:
			intent = new Intent(this, CourseActivity.class);
			intent.putExtra("subjectid", "3");
			intent.putExtra("title", "科目三课件");
			break;
		case R.id.main_appointment_car_layout:
			if (app.userVO.getApplystate().equals(EnrollResult.SUBJECT_NONE.getValue())) {
				intent = new Intent(this, EnrollVerifyActivity.class);
			} else {
				checkUserEnrollState(checkEnrollState_car);
			}
			break;
		case R.id.main_wallet_layout:
			intent = new Intent(this, MyWalletActivity.class);
			break;
		case R.id.main_message_layout:
			intent = new Intent(this, MessageActivity.class);
			break;
		case R.id.main_my_layout:
			intent = new Intent(this, PersonCenterActivity.class);
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void checkUserEnrollState(String type) {
		if (app.userVO.getApplystate().equals(Config.EnrollResult.SUBJECT_ENROLLING.getValue())) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("userid", app.userVO.getUserid());
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(type, this, Config.IP + "api/v1/userinfo/getmyapplystate", paramsMap, 10000,
					headerMap);
		} else {
			runIntent(type);
		}
	}

	private void runIntent(String type) {
		if (app.userVO.getApplystate().equals(EnrollResult.SUBJECT_ENROLLING.getValue())) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("您已报名，请等待驾校审核");
			return;
		}
		String subjectId = app.userVO.getSubject().getSubjectid();
		if (type.equals(checkEnrollState_my)) {
			if (subjectId.equals(SubjectStatu.SUBJECT_THREE.getValue())
					|| subjectId.equals(SubjectStatu.SUBJECT_FOUR.getValue())) {
				Intent intent = new Intent(this, MyAppointmentActivity.class);
				intent.putExtra("subject", "3");
				startActivity(intent);
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("待学习科目三再预约");
			}
		} else if (type.equals(checkEnrollState_car)) {
			if (app.userVO.getSubject().getSubjectid().equals(SubjectStatu.SUBJECT_THREE.getValue())) {
				Intent intent = new Intent(this, AppointmentCarActivity.class);
				intent.putExtra("subject", "3");
				startActivity(intent);
			} else if (subjectId.equals(SubjectStatu.SUBJECT_FOUR.getValue())) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("已学完科目三，无需预约");
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("待学习科目三再预约");
			}
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.contains("checkEnrollState")) {
				if (data != null) {
					UserBaseStateVO baseStateVO = (UserBaseStateVO) JSONUtil.toJavaBean(UserBaseStateVO.class, data);
					if (!baseStateVO.getApplystate().equals(app.userVO.getApplystate())) {
						app.userVO.setApplystate(baseStateVO.getApplystate());
					}
					runIntent(type);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
