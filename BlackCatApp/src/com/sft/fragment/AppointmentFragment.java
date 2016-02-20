package com.sft.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.MyAppointmentListAdapter;
import com.sft.blackcatapp.AppointmentDetailActivity;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.listener.OnDateClickListener;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.view.WeekViewPager;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.UserVO;
import com.sft.vo.uservo.StudentSubject;

public class AppointmentFragment extends BaseFragment implements
		OnClickListener, OnItemClickListener {

	private static final String reservation = "reservation";
	private static final String MYPROGRESS = "getmyprogress";

	private WeekViewPager viewPager;
	private TextView subjectValueTv, subjectTextTv;
	//
	private StudentSubject subject = null;

	private TextView tvLeft1, tvRight1, tvLeft2, tvRight2;

	//
	private XListView appointmentList;
	//
	private MyAppointmentListAdapter adapter;
	private RelativeLayout noCaochErrorRl;
	private RelativeLayout hasCaochRl;

	public AppointmentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment, null,// container
				false);
		initViews(rootView);

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
		if (subjectId.equals(Config.SubjectStatu.SUBJECT_NONE.getValue())) {

			noCaochErrorRl.setVisibility(View.VISIBLE);
			hasCaochRl.setVisibility(View.GONE);
			return;
		} else {
			noCaochErrorRl.setVisibility(View.GONE);
			hasCaochRl.setVisibility(View.VISIBLE);
		}
		subjectValueTv.setText(subjectId);

		if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
			subject = app.userVO.getSubjecttwo();
		} else if (subjectId.equals(Config.SubjectStatu.SUBJECT_THREE
				.getValue())) {
			subject = app.userVO.getSubjectthree();
		}
		if (null != subject) {
			tvLeft1.setText("已约学时" + subject.getFinishcourse() + "课时");
			tvLeft2.setText("漏课" + subject.getMissingcourse() + "课时");
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
		viewPager
				.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

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

		noCaochErrorRl = (RelativeLayout) rootView.findViewById(R.id.error_rl);
		hasCaochRl = (RelativeLayout) rootView
				.findViewById(R.id.appointment_has_coach_rl);
		appointmentList = (XListView) rootView
				.findViewById(R.id.my_appointment_listview);
		appointmentList.setPullLoadEnable(false);
		appointmentList.setPullRefreshEnable(false);
		View headerView = View.inflate(getActivity(), R.layout.learn_progress,
				null);
		appointmentList.addHeaderView(headerView);
		appointmentList.setOnItemClickListener(this);
		if (app.isLogin) {
			noCaochErrorRl.setVisibility(View.GONE);
			hasCaochRl.setVisibility(View.VISIBLE);
			initCurrentProgress(headerView);
			ZProgressHUD.getInstance(getActivity()).setMessage("拼命加载中...");
			ZProgressHUD.getInstance(getActivity()).show();
			obtainOppointment();
		} else {
			noCaochErrorRl.setVisibility(View.VISIBLE);
			hasCaochRl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void obtainOppointment() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("subjectid", app.userVO.getSubject().getSubjectid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(reservation, this, Config.IP
				+ "api/v1/courseinfo/getmyreservation", paramMap, 10000,
				headerMap);
		requestStatus();
	}

	/**
	 * 学员预约--查询学生的学习进度
	 */
	private void requestStatus() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(MYPROGRESS, this, Config.IP
				+ "api/v1/userinfo/getmyprogress", paramMap, 10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(reservation)) {
				if (dataArray != null) {
					List<MyAppointmentVO> list = new ArrayList<MyAppointmentVO>();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					List<MyAppointmentVO> toadyAppointList = new ArrayList<MyAppointmentVO>();
					List<MyAppointmentVO> otherAppointList = new ArrayList<MyAppointmentVO>();
					List<MyAppointmentVO> finishedList = new ArrayList<MyAppointmentVO>();

					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						MyAppointmentVO appointmentVO = JSONUtil.toJavaBean(
								MyAppointmentVO.class,
								dataArray.getJSONObject(i));
						String date = UTC2LOC.instance.getDate(
								appointmentVO.getReservationcreatetime(),
								"yyyy-MM-dd");
						if (CommonUtil.compare_date(format.format(new Date()),
								date) == 0) {
							// 今天
							toadyAppointList.add(appointmentVO);
						} else if (CommonUtil.compare_date(
								format.format(new Date()), date) > 0) {
							// 明天，后天...
							otherAppointList.add(appointmentVO);
						} else {
							// 已完成
							finishedList.add(appointmentVO);
						}
					}
					list.addAll(toadyAppointList);
					list.addAll(otherAppointList);
					list.addAll(finishedList);
					ZProgressHUD.getInstance(getActivity()).dismiss();
					adapter = new MyAppointmentListAdapter(getActivity(), list);
					appointmentList.setAdapter(adapter);
				}
			} else if (type.equals(MYPROGRESS)) {
				if (null != data) {

					UserVO userVo = JSONUtil.toJavaBean(UserVO.class, data);
					String subjectId = userVo.getSubject().getSubjectid();
					LogUtil.print("myProgress----jsonString>" + jsonString);
					LogUtil.print("myProgress----subjectId>" + subjectId);
					StudentSubject tempSubject = null;
					// 获取当前学习的 课，科目2 或者科目3
					if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO
							.getValue())) {
						LogUtil.print("myProgress----subjectId22222>");
						tempSubject = userVo.getSubjecttwo();
					} else if (subjectId
							.equals(Config.SubjectStatu.SUBJECT_THREE
									.getValue())) {
						LogUtil.print("myProgress----subjectId33333>");
						tempSubject = userVo.getSubjectthree();
					}

					LogUtil.print("myProgress----333>"
							+ tempSubject.getFinishcourse() + "last:"
							+ tempSubject.getReservation());
					//
					// tempSubject.getTotalcourse() -
					// tempSubject.getFinishcourse();
					tvLeft1.setText("已约学时" + tempSubject.getFinishcourse()
							+ "课时");
					tvLeft2.setText("漏课" + tempSubject.missingcourse + "课时");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		LogUtil.print("预约详情");
		Intent intent = new Intent(getActivity(),
				AppointmentDetailActivity.class);
		getActivity().startActivity(intent);
	}

}
