package com.sft.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.listener.OnDateClickListener;
import com.sft.util.LogUtil;
import com.sft.view.WeekViewPager;
import com.sft.vo.uservo.StudentSubject;

public class AppointmentFragment extends BaseFragment implements
		OnClickListener {

	private WeekViewPager viewPager;
	private TextView subjectValueTv, subjectTextTv;
	//
	private StudentSubject subject = null;

	private TextView tvLeft1, tvRight1, tvLeft2, tvRight2;

	public AppointmentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment, null,// container
				false);
		initViews(rootView);
		initCurrentProgress(rootView);
		return rootView;
	}

	private void initCurrentProgress(View rootView) {
		subjectValueTv = (TextView) rootView
				.findViewById(R.id.my_appointment_subject_value_tv);
		subjectTextTv = (TextView) rootView
				.findViewById(R.id.my_appointment_subject_text_tv);

		tvLeft1 = (TextView) rootView.findViewById(R.id.my_appoint_studied);
		tvRight1 = (TextView) rootView.findViewById(R.id.my_appoint_really);
		tvRight2 = (TextView) rootView.findViewById(R.id.my_appoint_last);
		tvLeft2 = (TextView) rootView.findViewById(R.id.my_appoint_notsign);

		String subjectId = app.userVO.getSubject().getSubjectid();
		subjectValueTv.setText(subjectId);

		if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
			subject = app.userVO.getSubjecttwo();
		} else if (subjectId.equals(Config.SubjectStatu.SUBJECT_THREE
				.getValue())) {
			subject = app.userVO.getSubjectthree();
		}
		if (null != subject) {
			tvLeft1.setText("已约学时" + subject.getFinishcourse() + "课时");
			tvLeft2.setText("漏课" + subject.getReservation() + "课时");
		} else {
			tvLeft1.setVisibility(View.GONE);
			tvLeft2.setVisibility(View.GONE);
			tvRight2.setVisibility(View.GONE);
			tvRight1.setVisibility(View.GONE);
		}
		if (subject != null) {
			String curProgress = subject.getProgress();
			subjectTextTv.setText(getString(R.string.cur_progress)
					+ curProgress);
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// EventBus.getDefault().unregister(this);
	}

	private void initViews(View rootView) {
		viewPager = (WeekViewPager) rootView.findViewById(R.id.viewPager);
		viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {

			@Override
			public int getCount() {
				return 2;
			}

			@Override
			public Fragment getItem(int position) {
				AppointmentWeekFragment fragment = new AppointmentWeekFragment();
				fragment.setData(position);
				return fragment;
			}
		});
		viewPager.setOnDateClickListener(new OnDateClickListener() {

			@Override
			public void onDateClick(int day, boolean clickbale) {
				if (clickbale) {
					LogUtil.print("点击了" + day);
				} else {
					LogUtil.print("木有点击了" + day);

				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

}
