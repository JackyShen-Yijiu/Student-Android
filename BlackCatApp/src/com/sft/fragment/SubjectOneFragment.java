package com.sft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.viewutil.StudyItemLayout;
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
	private TextView officalClass;
	// 模拟考试次数
	private TextView testTimes;
	// 学习进度
	private ProgressBar studyProgressBar;

	// 学习进度信息
	private SubjectForOneVO subject;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subject_one,
				container, false);
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
		switch (v.getId()) {
		case R.id.question_banks:

			break;
		case R.id.simulation_test:

			break;
		case R.id.my_error_data:

			break;
		case R.id.make_an_appointment:

			break;
		case R.id.communication:

			break;

		default:
			break;
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
		testTimes.setText("模拟考试" + subject.getFinishcourse() + "次");
		officalClass.setText("官方学时" + subject.getOfficialhours());
	}
}
