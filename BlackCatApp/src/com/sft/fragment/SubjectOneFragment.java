package com.sft.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.blackcatapp.AppointmentExamActivity;
import com.sft.blackcatapp.ExerciseOrderAct;
import com.sft.blackcatapp.QuestionActivity;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.BaseUtils;
import com.sft.util.CommonUtil;
import com.sft.viewutil.StudyItemLayout;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.SubjectForOneVO;

public class SubjectOneFragment extends BaseFragment implements OnClickListener {

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
				
				intent = new Intent(mContext, ExerciseOrderAct.class);
//				intent = new Intent(mContext, QuestionActivity.class);
//				intent.putExtra("url", app.questionVO.getSubjectone()
//						.getQuestionlisturl());
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.simulation_test:
			// 模拟考试
			if (app.questionVO != null) {
				intent = new Intent(mContext, QuestionActivity.class);
				intent.putExtra("url", app.questionVO.getSubjectone()
						.getQuestiontesturl());
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.my_error_data:
			// 我的错题
			if (app.isLogin) {
				if (app.questionVO != null) {
					intent = new Intent(mContext, QuestionActivity.class);
					intent.putExtra("url", app.questionVO.getSubjectone()
							.getQuestionerrorurl());
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
				intent = new Intent(mContext, AppointmentExamActivity.class);
				intent.putExtra("subjectid", "1");

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

	public void setLearnProgressInfo(SubjectForOneVO subject) {

		if (null != subject) {
			this.subject = subject;
			refreshUI();
		}
	}

	private void refreshUI() {
		studyProgressBar.setMax(subject.getTotalcourse());
		studyProgressBar.setProgress(subject.getFinishcourse());
		studyProgressTv.setText("学习进度   " + subject.getFinishcourse() + "/"
				+ subject.getOfficialhours());
		// testTimes.setText("模拟考试" + subject.getFinishcourse() + "次");
		// officalClass.setText("官方学时" + subject.getOfficialhours());
	}
}
