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

import com.jzjf.app.R;
import com.sft.blackcatapp.QuestionActivity;
import com.sft.util.BaseUtils;
import com.sft.util.CommonUtil;
import com.sft.viewutil.ZProgressHUD;

public class OldSubjectOneFragment extends BaseFragment implements
		OnClickListener {

	private View view;
	private ImageView questionBank;
	private ImageView mock;
	private ImageView errorData;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_view_two, container, false);
		mContext = getActivity();
		initView();
		initListener();
		return view;
	}

	private void initView() {
		questionBank = (ImageView) view
				.findViewById(R.id.subject_one_question_bank_iv);
		mock = (ImageView) view.findViewById(R.id.subject_one_mock_iv);
		errorData = (ImageView) view
				.findViewById(R.id.subject_one_error_data_iv);
	}

	private void initListener() {
		questionBank.setOnClickListener(this);
		mock.setOnClickListener(this);
		errorData.setOnClickListener(this);
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
		Intent intent = null;
		switch (v.getId()) {
		case R.id.subject_one_question_bank_iv:
			// 题库
			if (app.questionVO != null) {
				intent = new Intent(mContext, QuestionActivity.class);
				intent.putExtra("url", app.questionVO.getSubjectone()
						.getQuestionlisturl());
			} else {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.subject_one_error_data_iv:
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
		case R.id.subject_one_mock_iv:
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
		}
		if (intent != null) {
			mContext.startActivity(intent);
		}
	}

}
