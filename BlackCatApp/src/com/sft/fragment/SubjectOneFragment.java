package com.sft.fragment;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.blackcatapp.AppointmentExamPreActivity;
import com.sft.blackcatapp.AppointmentExamSuccessActivity;
import com.sft.blackcatapp.ExerciseOrderAct;
import com.sft.blackcatapp.QuestionActivity;
import com.sft.blackcatapp.SectionActivity;
import com.sft.common.Config;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.BaseUtils;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.viewutil.StudyItemLayout;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyExamInfoVO;
import com.sft.vo.SubjectForOneVO;

public class SubjectOneFragment extends BaseFragment implements OnClickListener {
	private static final String MYEXAMINFO = "myexaminfo";
	// 交流
	private StudyItemLayout communication;
	// 我要约考
	private StudyItemLayout appointment;
	// m我的错题
	private StudyItemLayout errorData;
	// 模拟考试
	private StudyItemLayout simulation;
	// 题库
	private StudyItemLayout questionBank;
	// 官方课时
	// private TextView officalClass;
	// 模拟考试次数
	// private TextView testTimes;
	// 学习进度
	private ProgressBar studyProgressBar;

	// 学习进度信息
	private SubjectForOneVO subject;

	private Context mContext;
	private StudyItemLayout schoolReport;
	private TextView studyProgressTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subject_one,
				container, false);
		mContext = getActivity();
		initViews(rootView);
		setListener();
		return rootView;
	}

	private void initViews(View rootView) {
		studyProgressTv = (TextView) rootView
				.findViewById(R.id.study_progress_tv);
		studyProgressBar = (ProgressBar) rootView
				.findViewById(R.id.study_progressbar);

		questionBank = (StudyItemLayout) rootView
				.findViewById(R.id.question_banks);
		simulation = (StudyItemLayout) rootView
				.findViewById(R.id.simulation_test);
		errorData = (StudyItemLayout) rootView.findViewById(R.id.my_error_data);
		appointment = (StudyItemLayout) rootView
				.findViewById(R.id.make_an_appointment);
		communication = (StudyItemLayout) rootView
				.findViewById(R.id.communication);
		schoolReport = (StudyItemLayout) rootView
				.findViewById(R.id.my_school_report);
	}

	private void setListener() {
		questionBank.setOnClickListener(this);
		simulation.setOnClickListener(this);
		errorData.setOnClickListener(this);
		appointment.setOnClickListener(this);
		schoolReport.setOnClickListener(this);
		communication.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!CommonUtil.isNetworkConnected(mContext)) {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithFailure("网络异常");
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.question_banks:

			// 题库
			if (app.questionVO != null) {

				intent = new Intent(mContext, SectionActivity.class);
				intent.putExtra("subjectid", 1);
				// ExerciseOrderAct
				// intent = new Intent(mContext, QuestionActivity.class);
				// intent.putExtra("url", app.questionVO.getSubjectone()
				// .getQuestionlisturl());

			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.simulation_test:
			// 模拟考试
			intent = new Intent(mContext, ExerciseOrderAct.class);
			intent.putExtra("flag", 1);
			intent.putExtra("subjectid", 1);
//			kemu = getIntent().getIntExtra("subjectid",1);
			//练习模式
			
//			if (app.questionVO != null) {
//				intent = new Intent(mContext, QuestionActivity.class);
//				intent.putExtra("url", app.questionVO.getSubjectone()
//						.getQuestiontesturl());
//			} else {
//				ZProgressHUD.getInstance(mContext).show();
//				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
//			}
			break;
		case R.id.my_error_data:
			// 我的错题
			if (app.isLogin) {
				intent = new Intent(mContext, ExerciseOrderAct.class);
				intent.putExtra("flag", 2);
				intent.putExtra("subjectid", 1);
				if (app.questionVO != null) {
//					intent = new Intent(mContext, QuestionActivity.class);
//					intent.putExtra("url", app.questionVO.getSubjectone()
//							.getQuestionerrorurl());
				} else {
					ZProgressHUD.getInstance(mContext).show();
					ZProgressHUD.getInstance(mContext).dismissWithFailure(
							"暂无题库");
				}
			} else {

				BaseUtils.toLogin(getActivity());
				// NoLoginDialog dialog = new NoLoginDialog(mContext);
				// dialog.show();
			}
			break;
		case R.id.make_an_appointment:
			if (app.isLogin) {
				obtainMyExaminfo();
				// intent = new Intent(mContext,
				// AppointmentExamPreActivity.class);
				// intent.putExtra("subjectid", "1");

			} else {
				BaseUtils.toLogin(getActivity());
//				NoLoginDialog dialog = new NoLoginDialog(getActivity());
//				dialog.show();
			}

			break;

		case R.id.my_school_report:
			// 成绩单
			if (app.isLogin) {
				if (app.questionVO != null) {
					intent = new Intent(mContext, QuestionActivity.class);
					intent.putExtra("url", app.questionVO.getSubjectone()
							.getKemusichengjidanurl());
				} else {
					ZProgressHUD.getInstance(mContext).show();
					ZProgressHUD.getInstance(mContext).dismissWithFailure(
							"暂无成绩单");
				}
			} else {
				BaseUtils.toLogin(getActivity());
//				NoLoginDialog dialog = new NoLoginDialog(mContext);
//				dialog.show();
			}
			break;

		case R.id.communication:

			break;

		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	// 获取我的预考信息
	private void obtainMyExaminfo() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("subjectid", "1");
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		//
		HttpSendUtils.httpGetSend(MYEXAMINFO, this, Config.IP
				+ "api/v1/userinfo/getmyexaminfo", paramMap, 10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(MYEXAMINFO)) {
				//
				if (null != data) {
					MyExamInfoVO examInfoVO = JSONUtil.toJavaBean(
							MyExamInfoVO.class, data);
					// 跳转到相应的页面
					Intent intent = null;
					if (examInfoVO.getExaminationstate().equals(
							Config.MyExamInfo.EXAMINATION_NONE.getValue())) {
						// 未申请
						intent = new Intent(getActivity(),
								AppointmentExamPreActivity.class);
						intent.putExtra("subjectid", "1");
						startActivity(intent);
					} else {
						//
						intent = new Intent(getActivity(),
								AppointmentExamSuccessActivity.class);
						intent.putExtra("examInfoVO", examInfoVO);
						startActivity(intent);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void setLearnProgressInfo(SubjectForOneVO subject) {

		if (null != subject) {
			this.subject = subject;
			refreshUI();
		}
	}

	private void refreshUI() {
		studyProgressBar.setMax(3);
		studyProgressBar.setProgress(0);
		studyProgressTv.setText("学习进度   " + "0" + "/3");
		// testTimes.setText("模拟考试" + subject.getFinishcourse() + "次");
		// officalClass.setText("官方学时" + subject.getOfficialhours());
	}
}
