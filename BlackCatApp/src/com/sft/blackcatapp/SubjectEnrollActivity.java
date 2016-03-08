package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.listener.OnTabActivityResultListener;
import com.sft.util.BaseUtils;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.vo.CoachVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.UserBaseStateVO;

/**
 * 科目一
 * 
 * @author Administrator
 * 
 */
public class SubjectEnrollActivity extends BaseActivity implements
		OnTabActivityResultListener {

	private static final String checkEnrollState = "checkEnrollState";
	//
	private LinearLayout twoLayout;
	//
	private LinearLayout threeLayout;
	//
	private RelativeLayout libraryBtn;
	private RelativeLayout examBtn, myFaultBtn, walletBtn, messageBtn, myBtn;
	private ImageView bottomProgress;
	private ImageView car;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.main_view_one);
		// setTheme(R.style.translucent);
		// this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
		initView();
		// resizeLayout();
		// setListener();
		LogUtil.print("课程报名");
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleBarVisible(View.GONE);
		showTitlebarBtn(0);
		setTitleText(R.string.school);

		// bottomProgress = (ImageView)
		// findViewById(R.id.main_bottom_progress_iv);
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
				if (app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
					intent = new Intent(this, EnrollActivity.class);
					startActivity(intent);
				} else {
					checkUserEnrollState();
				}
			} else {
				BaseUtils.toLogin(SubjectEnrollActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		case R.id.main_wallet_layout:
			if (app.isLogin) {
				intent = new Intent(this, MyWalletActivity.class);
				startActivity(intent);
			} else {
				BaseUtils.toLogin(SubjectEnrollActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		case R.id.main_message_layout:
			if (app.isLogin) {
				intent = new Intent(this, MessageActivity.class);
				startActivity(intent);
			} else {
				BaseUtils.toLogin(SubjectEnrollActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		case R.id.main_my_layout:
			if (app.isLogin) {
				intent = new Intent(this, PersonCenterActivity.class);
				getParent().startActivityForResult(intent, v.getId());
			} else {
				BaseUtils.toLogin(SubjectEnrollActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		}
	}

	private void checkUserEnrollState() {
		if (app.userVO.getApplystate().equals(
				Config.EnrollResult.SUBJECT_ENROLLING.getValue())) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("userid", app.userVO.getUserid());
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(checkEnrollState, this, Config.IP
					+ "api/v1/userinfo/getmyapplystate", paramsMap, 10000,
					headerMap);
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
					UserBaseStateVO baseStateVO = JSONUtil.toJavaBean(
							UserBaseStateVO.class, data);
					if (!baseStateVO.getApplystate().equals(
							app.userVO.getApplystate())) {
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
