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

import com.sft.blackcatapp.AppointmentExamActivity;
import com.sft.blackcatapp.QuestionActivity;
import com.sft.blackcatapp.R;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.CommonUtil;
import com.sft.viewutil.StudyItemLayout;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.SubjectForOneVO;

public class SubjectFourFragment extends BaseFragment implements
		OnClickListener {

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
	private TextView officalClass;
	// 模拟考试次数
	private TextView testTimes;
	// 学习进度
	private ProgressBar studyProgressBar;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subject_four,
				container, false);
		mContext = getActivity();
		initViews(rootView);
		setListener();
		return rootView;
	}

	private void initViews(View rootView) {
		testTimes = (TextView) rootView.findViewById(R.id.study_test_times);
		officalClass = (TextView) rootView
				.findViewById(R.id.study_offical_class);
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
	}

	private void setListener() {
		questionBank.setOnClickListener(this);
		simulation.setOnClickListener(this);
		errorData.setOnClickListener(this);
		appointment.setOnClickListener(this);
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
			NoLoginDialog dialog = new NoLoginDialog(mContext);
			dialog.show();
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.question_banks:
			// 题库
			if (app.questionVO != null) {
				intent = new Intent(mContext, QuestionActivity.class);
				intent.putExtra("url", app.questionVO.getSubjectfour()
						.getQuestionlisturl());
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.simulation_test:
			// 模拟考试
			if (app.questionVO != null) {
				intent = new Intent(mContext, QuestionActivity.class);
				intent.putExtra("url", app.questionVO.getSubjectfour()
						.getQuestiontesturl());
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.my_error_data:
			// 我的错题
			if (app.questionVO != null) {
				intent = new Intent(mContext, QuestionActivity.class);
				intent.putExtra("url", app.questionVO.getSubjectfour()
						.getQuestionerrorurl());
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.make_an_appointment:
			if (app.isLogin) {
				intent = new Intent(mContext, AppointmentExamActivity.class);
				intent.putExtra("subjectid", "4");

			} else {
				NoLoginDialog dialog = new NoLoginDialog(getActivity());
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
		studyProgressBar.setMax(subject.getTotalcourse());
		studyProgressBar.setProgress(subject.getFinishcourse());
		testTimes.setText("模拟考试" + subject.getFinishcourse() + "次");
		officalClass.setText("官方学时" + subject.getOfficialhours());

	}
}
