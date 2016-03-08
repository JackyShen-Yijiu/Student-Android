package com.sft.fragment;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.blackcatapp.ApplyActivity;
import com.sft.blackcatapp.AppointmentCarActivity;
import com.sft.blackcatapp.CourseActivity;
import com.sft.blackcatapp.MyAppointmentActivity;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.common.Config.SubjectStatu;
import com.sft.util.BaseUtils;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.UserBaseStateVO;

/**
 * 科目三
 * 
 * @author yyhui
 * 
 */
public class OldSubjectThreeFragment extends BaseFragment implements
		OnClickListener {

	private static final String checkEnrollState_my = "checkEnrollStatemy";
	private static final String checkEnrollState_car = "checkEnrollStatecar";
	//

	private View view;
	private Context mContext;
	private ImageView appointmentList;
	private ImageView courseware;
	private ImageView appointment;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.main_view_four, container, false);
		mContext = getActivity();

		initView();
		initListener();
		return view;
	}

	private void initView() {
		appointmentList = (ImageView) view
				.findViewById(R.id.subject_three_appointment_list_iv);
		courseware = (ImageView) view
				.findViewById(R.id.subject_three_courseware_iv);
		appointment = (ImageView) view
				.findViewById(R.id.subject_three_appointment_iv);
	}

	private void initListener() {
		appointmentList.setOnClickListener(this);
		courseware.setOnClickListener(this);
		appointment.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		if (!app.isLogin) {
			BaseUtils.toLogin(getActivity());
			// NoLoginDialog dialog = new NoLoginDialog(mContext);
			// dialog.show();
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.subject_three_appointment_list_iv:
			if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_NONE.getValue())) {
				intent = new Intent(mContext, ApplyActivity.class);
			} else {
				checkUserEnrollState(checkEnrollState_my);
			}
			break;
		case R.id.subject_three_courseware_iv:
			intent = new Intent(mContext, CourseActivity.class);
			intent.putExtra("subjectid", "3");
			intent.putExtra("title", "科目三课件");
			break;
		case R.id.subject_three_appointment_iv:
			if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_NONE.getValue())) {
				intent = new Intent(mContext, ApplyActivity.class);
			} else {
				checkUserEnrollState(checkEnrollState_car);
			}
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void checkUserEnrollState(String type) {
		if (app.userVO.getApplystate().equals(
				Config.EnrollResult.SUBJECT_ENROLLING.getValue())) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("userid", app.userVO.getUserid());
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(type, this, Config.IP
					+ "api/v1/userinfo/getmyapplystate", paramsMap, 10000,
					headerMap);
		} else {
			runIntent(type);
		}
	}

	private void runIntent(String type) {
		if (app.userVO.getApplystate().equals(
				EnrollResult.SUBJECT_ENROLLING.getValue())) {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithFailure(
					"您已报名，请等待驾校审核");
			return;
		}
		String subjectId = app.userVO.getSubject().getSubjectid();
		if (type.equals(checkEnrollState_my)) {
			if (subjectId.equals(SubjectStatu.SUBJECT_THREE.getValue())
					|| subjectId.equals(SubjectStatu.SUBJECT_FOUR.getValue())) {
				Intent intent = new Intent(mContext,
						MyAppointmentActivity.class);
				intent.putExtra("subject", "3");
				startActivity(intent);
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithSuccess(
						"待学习科目三再预约");
			}
		} else if (type.equals(checkEnrollState_car)) {
			if (app.userVO.getSubject().getSubjectid()
					.equals(SubjectStatu.SUBJECT_THREE.getValue())) {
				Intent intent = new Intent(mContext,
						AppointmentCarActivity.class);
				intent.putExtra("subject", "3");
				startActivity(intent);
			} else if (subjectId.equals(SubjectStatu.SUBJECT_FOUR.getValue())) {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithSuccess(
						"已学完科目三，无需预约");
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithSuccess(
						"待学习科目三再预约");
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
					UserBaseStateVO baseStateVO = JSONUtil.toJavaBean(
							UserBaseStateVO.class, data);
					if (!baseStateVO.getApplystate().equals(
							app.userVO.getApplystate())) {
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
