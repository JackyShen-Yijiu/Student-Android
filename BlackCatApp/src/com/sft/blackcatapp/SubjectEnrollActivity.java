package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.NoLoginDialog;
import com.sft.listener.OnTabActivityResultListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.CoachVO;
import com.sft.vo.SchoolVO;
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
 * 科目一
 * 
 * @author Administrator
 * 
 */
public class SubjectEnrollActivity extends BaseActivity implements OnTabActivityResultListener {

	private static final String checkEnrollState = "checkEnrollState";
	//
	private LinearLayout twoLayout;
	//
	private LinearLayout threeLayout;
	//
	private RelativeLayout libraryBtn;
	private RelativeLayout examBtn, myFaultBtn, walletBtn, messageBtn, myBtn;

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

		libraryBtn = (RelativeLayout) findViewById(R.id.main_appointment_layout);
		examBtn = (RelativeLayout) findViewById(R.id.main_appointment_car_layout);
		myFaultBtn = (RelativeLayout) findViewById(R.id.main_appointment_course_layout);
		walletBtn = (RelativeLayout) findViewById(R.id.main_wallet_layout);
		messageBtn = (RelativeLayout) findViewById(R.id.main_message_layout);
		myBtn = (RelativeLayout) findViewById(R.id.main_my_layout);

		ImageView imageAppointment = (ImageView) findViewById(R.id.main_appointment_im);
		imageAppointment.setBackgroundResource(R.drawable.coach_card);
		RelativeLayout.LayoutParams paramsAppointment = (RelativeLayout.LayoutParams) imageAppointment
				.getLayoutParams();
		paramsAppointment.width = (int) (screenDensity * 123);
		paramsAppointment.height = (int) (screenDensity * 144);
		
		ImageView imageAppointmentCar = (ImageView) findViewById(R.id.main_appointment_car_im);
		imageAppointmentCar.setBackgroundResource(R.drawable.enroll);
		RelativeLayout.LayoutParams paramsAppointmentCar = (RelativeLayout.LayoutParams) imageAppointmentCar
				.getLayoutParams();
		paramsAppointmentCar.width = (int) (screenDensity * 64);
		paramsAppointmentCar.height = (int) (screenDensity * 77);
		
		ImageView imageCourse = (ImageView) findViewById(R.id.main_appointment_course_im);
		imageCourse.setBackgroundResource(R.drawable.school_card);
		RelativeLayout.LayoutParams paramsCourse = (RelativeLayout.LayoutParams) imageCourse
				.getLayoutParams();
		paramsCourse.width = (int) (screenDensity * 62);
		paramsCourse.height = (int) (screenDensity * 81);
		
		((TextView) findViewById(R.id.main_appointment_tv)).setText(R.string.coach_card);
		((TextView) findViewById(R.id.main_appointment_car_tv)).setText(R.string.enroll);
		((TextView) findViewById(R.id.main_appointment_course_tv)).setText(R.string.school_card);
		
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
		threeLayout.requestLayout();
		LinearLayout.LayoutParams twoLayoutParams = (LinearLayout.LayoutParams) twoLayout.getLayoutParams();
		twoLayoutParams.height = (int) ((screenWidth - 11 * screenDensity) * 2 / 3);
		twoLayout.requestLayout();
	}

	private void setListener() {
		libraryBtn.setOnClickListener(this);
		examBtn.setOnClickListener(this);
		myFaultBtn.setOnClickListener(this);
		walletBtn.setOnClickListener(this);
		messageBtn.setOnClickListener(this);
		myBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}

		Intent intent = null;
		switch (v.getId()) {
		case R.id.main_appointment_layout:
			// 教练卡
			intent = new Intent(this, EnrollCoachActivity.class);
			if (app.isLogin) {
				// 将选择的教练放在最前
				CoachVO coach = Util.getEnrollUserSelectedCoach(this);
				if (coach != null) {
					intent.putExtra("coach", coach);
				}
			}
			getParent().startActivityForResult(intent, v.getId());
			break;
		case R.id.main_appointment_course_layout:
			// 驾校卡
			intent = new Intent(this, EnrollSchoolActivity.class);
			if (app.isLogin) {
				// 将选择的学校放在最前
				SchoolVO school = Util.getEnrollUserSelectedSchool(this);
				if (school != null) {
					intent.putExtra("school", school);
				}
			}
			getParent().startActivityForResult(intent, v.getId());
			break;
		case R.id.main_appointment_car_layout:
			// 报名
			if (app.isLogin) {
				if (app.userVO.getApplystate().equals(EnrollResult.SUBJECT_NONE.getValue())) {
					intent = new Intent(this, EnrollActivity.class);
					startActivity(intent);
				} else {
					checkUserEnrollState();
				}
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case R.id.main_wallet_layout:
			if (app.isLogin) {
				intent = new Intent(this, MyWalletActivity.class);
				startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case R.id.main_message_layout:
			if (app.isLogin) {
				intent = new Intent(this, MessageActivity.class);
				startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case R.id.main_my_layout:
			if (app.isLogin) {
				intent = new Intent(this, PersonCenterActivity.class);
				getParent().startActivityForResult(intent, v.getId());
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		}
	}

	private void checkUserEnrollState() {
		if (app.userVO.getApplystate().equals(Config.EnrollResult.SUBJECT_ENROLLING.getValue())) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("userid", app.userVO.getUserid());
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(checkEnrollState, this, Config.IP + "api/v1/userinfo/getmyapplystate", paramsMap,
					10000, headerMap);
		} else {
			runIntent();
		}
	}

	private void runIntent() {
		Intent intent = new Intent(this, EnrollActivity.class);
		startActivity(intent);
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		data.setClass(this, EnrollActivity.class);
		data.putExtra("requestCode", requestCode);
		data.putExtra("resultCode", resultCode);
		data.putExtra("userselect", true);
		startActivity(data);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(checkEnrollState)) {
				if (data != null) {
					UserBaseStateVO baseStateVO = (UserBaseStateVO) JSONUtil.toJavaBean(UserBaseStateVO.class, data);
					if (!baseStateVO.getApplystate().equals(app.userVO.getApplystate())) {
						app.userVO.setApplystate(baseStateVO.getApplystate());
					}
					runIntent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
