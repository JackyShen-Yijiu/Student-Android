package com.sft.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sft.blackcatapp.QuestionActivity;
import com.jzjf.app.R;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.CommonUtil;
import com.sft.viewutil.ZProgressHUD;

public class OldSubjectFourFragment extends BaseFragment implements
		OnClickListener {

	private View view;
	private Context mContext;
	private ImageView courseware;
	private ImageView errorData;
	private ImageView questionBank;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_view_five, container, false);
		mContext = getActivity();

		initView();
		initListener();
		return view;
	}

	private void initView() {
		courseware = (ImageView) view
				.findViewById(R.id.subject_four_courseware_iv);
		errorData = (ImageView) view
				.findViewById(R.id.subject_four_error_data_iv);
		questionBank = (ImageView) view
				.findViewById(R.id.subject_four_question_bank_iv);
	}

	private void initListener() {
		courseware.setOnClickListener(this);
		errorData.setOnClickListener(this);
		questionBank.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
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
		case R.id.subject_four_question_bank_iv:
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
		case R.id.subject_four_error_data_iv:
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
		case R.id.subject_four_courseware_iv:
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
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

}
