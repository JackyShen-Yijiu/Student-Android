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
import com.sft.blackcatapp.CourseActivity;
import com.sft.blackcatapp.R;
import com.sft.blackcatapp.YiBuIntroduceActivity;
import com.sft.common.Config;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.CommonUtil;
import com.sft.util.LogUtil;
import com.sft.viewutil.StudyItemLayout;
import com.sft.vo.SubjectForOneVO;

public class SubjectThreeFragment extends BaseFragment implements
		OnClickListener {

	// 官方课时
	private TextView officalClass;
	// 规定学时
	private TextView ruleClass;
	// 已完成
	private TextView finishedClass;
	// 学习进度
	private ProgressBar studyProgressBar;

	// 交流
	private StudyItemLayout communication;
	// 我要约考
	private StudyItemLayout appointment;
	// 学车秘籍
	private StudyItemLayout learnCarCheats;
	// 课件
	private StudyItemLayout courseWare;

	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subject_three,
				container, false);
		mContext = getActivity();
		initViews(rootView);
		setListener();
		return rootView;
	}

	private void initViews(View rootView) {
		LogUtil.print("initViews888");
		finishedClass = (TextView) rootView
				.findViewById(R.id.study_finished_class);
		ruleClass = (TextView) rootView.findViewById(R.id.study_rule_class);
		officalClass = (TextView) rootView
				.findViewById(R.id.study_offical_class);
		studyProgressBar = (ProgressBar) rootView
				.findViewById(R.id.study_progressbar);

		courseWare = (StudyItemLayout) rootView.findViewById(R.id.courseware);
		learnCarCheats = (StudyItemLayout) rootView
				.findViewById(R.id.learn_car_cheats);
		appointment = (StudyItemLayout) rootView
				.findViewById(R.id.make_an_appointment);
		communication = (StudyItemLayout) rootView
				.findViewById(R.id.communication);
	}

	private void setListener() {
		appointment.setOnClickListener(this);
		communication.setOnClickListener(this);
		learnCarCheats.setOnClickListener(this);
		courseWare.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.courseware:
			intent = new Intent(mContext, CourseActivity.class);
			intent.putExtra("subjectid", "3");
			intent.putExtra("title", "科目三");
			break;
		case R.id.learn_car_cheats:
			intent = new Intent(mContext, YiBuIntroduceActivity.class);
			intent.putExtra("url", Config.THREE_EXAM_FORMULA);
			intent.putExtra("cheatname",
					CommonUtil.getString(mContext, R.string.three_exam_formula));
			break;
		case R.id.make_an_appointment:
			if (app.isLogin) {
				intent = new Intent(mContext, AppointmentExamActivity.class);
				intent.putExtra("subjectid", "3");

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
			mContext.startActivity(intent);
		}
	}

	public void setLearnProgressInfo(SubjectForOneVO subject) {
		if (studyProgressBar == null) {
			LogUtil.print("111111");
		}
		if (subject == null) {
			LogUtil.print("1111122221");
		}
		studyProgressBar.setMax(subject.getTotalcourse());
		studyProgressBar.setProgress(subject.getFinishcourse());
		finishedClass.setText("已完成" + subject.getFinishcourse());
		ruleClass.setText("规定课时" + subject.getTotalcourse());
		officalClass.setText("官方学时" + subject.getOfficialhours());

	}

}
