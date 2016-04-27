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
import com.sft.util.LogUtil;
import com.sft.viewutil.StudyItemLayout;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyExamInfoVO;
import com.sft.vo.SubjectForOneVO;

public class SubjectFourFragment extends BaseFragment implements
		OnClickListener {
	private static final String MYEXAMINFO = "myexaminfo";
	private static final String MYSCORE = "myscore";
	// 交流
	private StudyItemLayout communication;
	// 我要约考
	private StudyItemLayout appointment;
	// 我的错题
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
	private Context mContext;
	private StudyItemLayout schoolReport;
	private TextView studyProgressTv;
	private int studyNUmber = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subject_four,
				container, false);
		mContext = getActivity();
		initViews(rootView);
		setListener();
		obtainMyScore();
		return rootView;
	}

	private void initViews(View rootView) {
		// testTimes = (TextView) rootView.findViewById(R.id.study_test_times);
		// officalClass = (TextView) rootView
		// .findViewById(R.id.study_offical_class);
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
		schoolReport = (StudyItemLayout) rootView
				.findViewById(R.id.my_school_report);
		communication = (StudyItemLayout) rootView
				.findViewById(R.id.communication);
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
		if (!app.isLogin) {
			BaseUtils.toLogin(getActivity());
			// NoLoginDialog dialog = new NoLoginDialog(mContext);
			// dialog.show();
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.question_banks:
			// 题库
			if (app.questionVO != null) {
				intent = new Intent(mContext, SectionActivity.class);
				intent.putExtra("subjectid", 4);
				intent.putExtra("flag", 0);
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.simulation_test:
			intent = new Intent(mContext, ExerciseOrderAct.class);
			intent.putExtra("subjectid", 4);
			intent.putExtra("flag", 1);
			// 模拟考试
			// if (app.questionVO != null) {
			// intent = new Intent(mContext, QuestionActivity.class);
			// intent.putExtra("url", app.questionVO.getSubjectfour()
			// .getQuestiontesturl());
			// } else {
			// ZProgressHUD.getInstance(mContext).show();
			// ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			// }
			break;
		case R.id.my_error_data:
			// 我的错题
			if (app.questionVO != null) {
				intent = new Intent(mContext, ExerciseOrderAct.class);
				intent.putExtra("subjectid", 4);
				intent.putExtra("flag", 2);
				// intent = new Intent(mContext, QuestionActivity.class);
				// intent.putExtra("url", app.questionVO.getSubjectfour()
				// .getQuestionerrorurl());
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.make_an_appointment:
			if (app.isLogin) {
				obtainMyExaminfo();
				// intent = new Intent(mContext,
				// AppointmentExamPreActivity.class);
				// intent.putExtra("subjectid", "4");

			} else {
				NoLoginDialog dialog = new NoLoginDialog(getActivity());
				dialog.show();
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
				NoLoginDialog dialog = new NoLoginDialog(mContext);
				dialog.show();
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
		paramMap.put("subjectid", "4");
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		//
		HttpSendUtils.httpGetSend(MYEXAMINFO, this, Config.IP
				+ "api/v1/userinfo/getmyexaminfo", paramMap, 10000, headerMap);
	}

	// 获取我的考试成绩
	private void obtainMyScore() {
		if (app.userVO == null && app.userVO.getUserid() == null) {
			return;
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("subjectid", "4");
		paramMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		//
		HttpSendUtils.httpGetSend(MYSCORE, this, Config.IP
				+ "api/v1/userinfo/getmyscore", paramMap, 10000, headerMap);

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
						intent.putExtra("subjectid", "4");
						startActivity(intent);
					} else {
						//
						intent = new Intent(getActivity(),
								AppointmentExamSuccessActivity.class);
						intent.putExtra("examInfoVO", examInfoVO);
						startActivity(intent);
					}
				}
			} else if (type.equals(MYSCORE)) {
				//
				if (null != dataArray) {
					studyNUmber = dataArray.length();
					LogUtil.print("studyNUmber---" + studyNUmber);
					setLearnProgressInfo(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public void doError(String type, String msg) {
		if(type.equals(MYSCORE)){
			return;
		}
		super.doError(type, msg);
	}

	public void setLearnProgressInfo(SubjectForOneVO subject) {
		studyProgressBar.setMax(3);
		studyProgressBar.setProgress(studyNUmber);
		studyProgressTv.setText("学习进度   " + studyNUmber + "/3");
		// testTimes.setText("模拟考试" + subject.getFinishcourse() + "次");
		// officalClass.setText("官方学时" + subject.getOfficialhours());

	}
}
