package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.LearnProgressVO;

public class StudyFragment extends BaseFragment implements OnClickListener {

	private final String learnProgress = "learnProgress";
	private View line;
	private TextView subjectOne;
	private TextView subjectTwo;
	private TextView subjectThree;
	private TextView subjectFour;
	private ViewPager viewPager;

	private ArrayList<Fragment> fragments;
	private int line_width;
	private LearnProgressVO learnProgressVO;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_study, container,
				false);
		initViews(rootView);
		setListener();
		if (app.isLogin) {
			obtainLearnProgress();
		}
		return rootView;
	}

	private void obtainLearnProgress() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(learnProgress, this, Config.IP
				+ "api/v1/userinfo/getmyprogress", paramsMap, 10000, headerMap);
	}

	private void initViews(View rootView) {
		subjectOne = (TextView) rootView.findViewById(R.id.study_subject_one);
		subjectTwo = (TextView) rootView.findViewById(R.id.study_subject_two);
		subjectThree = (TextView) rootView
				.findViewById(R.id.study_subject_three);
		subjectFour = (TextView) rootView.findViewById(R.id.study_subject_four);
		line = rootView.findViewById(R.id.line);
		// ViewPropertyAnimator.animate(subjectOne).scaleX(1.2f).setDuration(0);
		// ViewPropertyAnimator.animate(subjectTwo).scaleY(1.2f).setDuration(0);
		// ViewPropertyAnimator.animate(subjectThree).scaleY(1.2f).setDuration(0);
		// ViewPropertyAnimator.animate(subjectFour).scaleY(1.2f).setDuration(0);

		fragments = new ArrayList<Fragment>();
		fragments.add(new SubjectOneFragment());
		fragments.add(new SubjectTwoFragment());
		fragments.add(new SubjectThreeFragment());
		fragments.add(new SubjectFourFragment());
		line_width = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth()
				/ fragments.size();
		line.getLayoutParams().width = line_width;
		line.requestLayout();

		viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(new FragmentStatePagerAdapter(
				getChildFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				changeState(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				float tagerX = arg0 * line_width + arg2 / fragments.size();
				ViewPropertyAnimator.animate(line).translationX(tagerX)
						.setDuration(0);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void setListener() {
		subjectOne.setOnClickListener(this);
		subjectTwo.setOnClickListener(this);
		subjectThree.setOnClickListener(this);
		subjectFour.setOnClickListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.study_subject_one:
			viewPager.setCurrentItem(0);
			break;
		case R.id.study_subject_two:
			viewPager.setCurrentItem(1);
			break;
		case R.id.study_subject_three:
			viewPager.setCurrentItem(2);
			break;
		case R.id.study_subject_four:
			viewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}

	private void changeState(int position) {
		subjectOne.setTextColor(getResources().getColor(
				R.color.study_text_normal));
		subjectTwo.setTextColor(getResources().getColor(
				R.color.study_text_normal));
		subjectThree.setTextColor(getResources().getColor(
				R.color.study_text_normal));
		subjectFour.setTextColor(getResources().getColor(
				R.color.study_text_normal));
		switch (position) {
		case 0:
			subjectOne.setTextColor(getResources().getColor(
					R.color.study_text_selected));

			// ViewPropertyAnimator.animate(subjectOne).scaleX(1.2f)
			// .setDuration(200);
			// ViewPropertyAnimator.animate(subjectOne).scaleY(1.2f)
			// .setDuration(200);
			// ViewPropertyAnimator.animate(subjectTwo).scaleX(1.0f)
			// .setDuration(200);
			// ViewPropertyAnimator.animate(subjectTwo).scaleY(1.0f)
			// .setDuration(200);
			break;
		case 1:
			subjectTwo.setTextColor(getResources().getColor(
					R.color.study_text_selected));
			break;
		case 2:
			subjectThree.setTextColor(getResources().getColor(
					R.color.study_text_selected));
			break;
		case 3:
			subjectFour.setTextColor(getResources().getColor(
					R.color.study_text_selected));
			break;

		default:
			break;
		}

	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(learnProgress)) {
				if (data != null) {
					learnProgressVO = JSONUtil.parseJsonToBean(data.toString(),
							LearnProgressVO.class);
					if (null != learnProgressVO) {
						LogUtil.print(learnProgressVO.getSubjectthree()
								.getTotalcourse() + "====");
						refreshView();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void refreshView() {
		if (null != learnProgressVO) {
			((SubjectOneFragment) fragments.get(0))
					.setLearnProgressInfo(learnProgressVO.getSubjectone());
			((SubjectTwoFragment) fragments.get(1))
					.setLearnProgressInfo(learnProgressVO.getSubjecttwo());
			((SubjectThreeFragment) fragments.get(2))
					.setLearnProgressInfo(learnProgressVO.getSubjectthree());
			((SubjectFourFragment) fragments.get(3))
					.setLearnProgressInfo(learnProgressVO.getSubjectfour());
		}
	}

}
