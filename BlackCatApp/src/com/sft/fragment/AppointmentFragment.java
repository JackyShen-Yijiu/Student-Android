package com.sft.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.MyAppointmentListAdapter2;
import com.sft.blackcatapp.AppointmentDetailActivity;
import com.sft.blackcatapp.AppointmentExamActivity;
import com.sft.blackcatapp.SussessOrderActvity;
import com.sft.common.Config;
import com.sft.dialog.NoCommentDialog;
import com.sft.event.AppointmentSuccessEvent;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.AppointmentTempVO;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.UserVO;
import com.sft.vo.uservo.StudentSubject;

import de.greenrobot.event.EventBus;

public class AppointmentFragment extends BaseFragment implements
		OnClickListener, OnItemClickListener, OnRefreshListener {

	private static final String RESERVATION = "reservation";
	private static final String MYPROGRESS = "getmyprogress";

	// private WeekViewPager viewPager;
	private TextView subjectValueTv, subjectTextTv;
	TextView tvLastXueShi;
	//
	private StudentSubject subject = null;

	private TextView tvLeft1, tvRight1, tvLeft2, tvRight2;

	//
	private SwipeRefreshLayout appointmentSwipeLaout;
	//
	private MyAppointmentListAdapter2 adapter;
	private RelativeLayout hasCaochRl;
	private List<MyAppointmentVO> list;
	private RelativeLayout noCaochErrorRl;
	private ImageView noCaochErrorIv;
	private TextView noCaochErroTv;
	// private ListView mListView;

	private ExpandableListView epListView;
	private boolean isRefreshing = false;

	/** 列表0:今日列表 ,1,未来预约 */
	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<MyAppointmentVO>> datas = new HashMap<Integer, List<MyAppointmentVO>>();

	private List<MyAppointmentVO> finishedList = null;
	private NoCommentDialog commentDialog;

	private String subjectId = "0";
	private LinearLayout yuekaoLr;

	public AppointmentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_appointment, null,// container
				false);
		initViews(rootView);
		// getActivity().getWindow().setSoftInputMode(
		//
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
		//
		// | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// commentDialog = new NoCommentDialog(getActivity());
		// commentDialog.show();
		return rootView;
	}

	private void initCurrentProgress(View rootView) {
		// 科目二 科目三
		subjectValueTv = (TextView) rootView
				.findViewById(R.id.my_appointment_subject_value_tv);
		// 已学38 学时 坡道起步
		subjectTextTv = (TextView) rootView
				.findViewById(R.id.my_appointment_subject_text_tv);

		tvLastXueShi = (TextView) rootView
				.findViewById(R.id.learn_progress_last_class);
		yuekaoLr = (LinearLayout) rootView
				.findViewById(R.id.learn_progress_yuekao);
		yuekaoLr.setOnClickListener(this);
		tvLeft1 = (TextView) rootView.findViewById(R.id.my_appoint_studied);
		tvRight1 = (TextView) rootView.findViewById(R.id.my_appoint_really);
		tvRight2 = (TextView) rootView.findViewById(R.id.my_appoint_last);
		tvLeft2 = (TextView) rootView.findViewById(R.id.my_appoint_notsign);

		subjectId = app.userVO.getSubject().getSubjectid();
		if (subjectId.equals(Config.SubjectStatu.SUBJECT_NONE.getValue())) {

			noCaochErrorRl.setVisibility(View.VISIBLE);
			hasCaochRl.setVisibility(View.GONE);
			return;
		} else {
			noCaochErrorRl.setVisibility(View.GONE);
			hasCaochRl.setVisibility(View.VISIBLE);
		}
		initSubject(subjectId, app.userVO);
		LogUtil.print("title--enddd>" + subject);

	}

	/***
	 * 当前 课时 信息
	 */
	private void initSubject(String subjectId, UserVO userVO) {
		subjectValueTv.setText(subjectId);

		if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
			subject = app.userVO.getSubjecttwo();
			subjectValueTv.setText("科目二");
		} else if (subjectId.equals(Config.SubjectStatu.SUBJECT_THREE
				.getValue())) {
			subject = app.userVO.getSubjectthree();
			subjectValueTv.setText("科目三");
		}
		// app.userVO.
		if (null != subject) {

			subjectTextTv.setText("已学" + subject.getFinishcourse() + "课时  "
					+ subject.getProgress());// +subject.getProgress()
			// 规定xx 学时 完成XX学时
			tvLeft1.setText("规定" + subject.officialhours + "学时  完成"
					+ subject.officialfinishhours + "学时");

			// 购买 XX学时 已学XX学时
			tvLeft2.setText("购买" + subject.getTotalcourse() + "课时  已学"
					+ subject.getFinishcourse() + "课时");
			if ((subject.officialhours - subject.officialfinishhours) == 0) {// 可以报考
				yuekaoLr.setBackgroundResource(R.drawable.button_rounded_corners);
				yuekaoLr.setClickable(true);
				tvLastXueShi.setVisibility(View.GONE);
			} else {// 不可以报考
				yuekaoLr.setBackgroundResource(R.drawable.button_rounded_corners_gray);
				yuekaoLr.setClickable(false);
				tvLastXueShi.setVisibility(View.VISIBLE);
				tvLastXueShi.setText("还需"
						+ (subject.officialhours - subject.officialfinishhours)
						+ "学时");
			}

		} else {
			tvLeft1.setVisibility(View.GONE);
			tvLeft2.setVisibility(View.GONE);
			tvRight2.setVisibility(View.GONE);
			tvRight1.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// EventBus.getDefault().unregister(this);
	}

	private void initViews(View rootView) {

		noCaochErrorRl = (RelativeLayout) rootView.findViewById(R.id.error_rl);
		noCaochErrorIv = (ImageView) rootView.findViewById(R.id.error_iv);
		noCaochErroTv = (TextView) rootView.findViewById(R.id.error_tv);
		noCaochErrorIv.setImageResource(R.drawable.appointment_detail_applyconfirm);
		noCaochErroTv.setText(CommonUtil.getString(getActivity(),
				R.string.no_appointment_coach_error_info));
		hasCaochRl = (RelativeLayout) rootView
				.findViewById(R.id.appointment_has_coach_rl);
		appointmentSwipeLaout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_appointment_swipe_container);
		epListView = (ExpandableListView) rootView
				.findViewById(R.id.fragment_appointment_listview_ep);
		epListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				Intent intent = new Intent(getActivity(),
						AppointmentDetailActivity.class);
				intent.putExtra("appointmentDetail", datas.get(arg2).get(arg3));
				startActivity(intent);
				return false;
			}
		});
		epListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 2) {// 已完成列表
					if (finishedList != null) {
						Intent i = new Intent(getActivity(),
								SussessOrderActvity.class);
						AppointmentTempVO vo = new AppointmentTempVO();
						vo.list = finishedList;
						i.putExtra("list", vo);
						startActivity(i);
					} else {

					}
				}
				return false;
			}
		});

		appointmentSwipeLaout.setOnRefreshListener(this);
		appointmentSwipeLaout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		View headerView = View.inflate(getActivity(), R.layout.learn_progress,
				null);
		epListView.addHeaderView(headerView);
		if (app.isLogin) {
			// 科目一和没学习时都没有预约列表
			if (app.userVO.getSubject().getSubjectid()
					.equals(Config.SubjectStatu.SUBJECT_NONE.getValue())
					|| app.userVO.getSubject().getSubjectid()
							.equals(Config.SubjectStatu.SUBJECT_ONE.getValue())) {
				noCaochErrorRl.setVisibility(View.VISIBLE);
				hasCaochRl.setVisibility(View.GONE);
			} else {
				noCaochErrorRl.setVisibility(View.GONE);
				hasCaochRl.setVisibility(View.VISIBLE);
				initCurrentProgress(headerView);
				LogUtil.print("拼命加载中...");
				ZProgressHUD.getInstance(getActivity()).setMessage("拼命加载中...");
				ZProgressHUD.getInstance(getActivity()).show();
				obtainOppointment();

			}
		} else {
			noCaochErrorRl.setVisibility(View.VISIBLE);
			hasCaochRl.setVisibility(View.GONE);
		}

		//
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
		//
		HttpSendUtils.httpGetSend(RESERVATION, this, Config.IP
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
			if (type.equals(RESERVATION)) {
				if (dataArray != null) {
					if (list == null) {
						list = new ArrayList<MyAppointmentVO>();
					} else {
						list.clear();
					}
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					List<MyAppointmentVO> toadyAppointList = new ArrayList<MyAppointmentVO>();
					List<MyAppointmentVO> otherAppointList = new ArrayList<MyAppointmentVO>();
					finishedList = new ArrayList<MyAppointmentVO>();

					int length = dataArray.length();
					if (length == 0) {
						noCaochErrorRl.setVisibility(View.VISIBLE);
						hasCaochRl.setVisibility(View.GONE);
					}
					for (int i = 0; i < length; i++) {
						MyAppointmentVO appointmentVO = JSONUtil.toJavaBean(
								MyAppointmentVO.class,
								dataArray.getJSONObject(i));
						String date = UTC2LOC.instance.getDate(
								appointmentVO.getBegintime(), "yyyy-MM-dd");
						if (CommonUtil.compare_date(format.format(new Date()),
								date) == 0) {
							// 今天
							toadyAppointList.add(appointmentVO);
						} else if (CommonUtil.compare_date(
								format.format(new Date()), date) < 0) {
							// 明天，后天...
							otherAppointList.add(appointmentVO);
						} else {
							// 已完成
							finishedList.add(appointmentVO);
						}
					}
					datas.put(0, toadyAppointList);
					datas.put(1, otherAppointList);

					list.addAll(toadyAppointList);
					list.addAll(otherAppointList);
					list.addAll(finishedList);
					LogUtil.print("预约列表：：---》" + list.size());
					datas.put(2, new ArrayList<MyAppointmentVO>());

					list.addAll(toadyAppointList);
					list.addAll(otherAppointList);
					list.addAll(finishedList);
					LogUtil.print("预约列表：：---今天》" + toadyAppointList.size()
							+ "other--->" + otherAppointList.size());
					ZProgressHUD.getInstance(getActivity()).dismiss();
					if (adapter == null) {
						adapter = new MyAppointmentListAdapter2(getActivity(),
								datas);
						epListView.setAdapter(adapter);
						// mListView.setAdapter(adapter);
						for (int i = 0; i < adapter.getGroupCount(); i++) {
							epListView.expandGroup(i);
						}
						// mListView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}

					if (isRefreshing) {
						appointmentSwipeLaout.setRefreshing(false);
						isRefreshing = false;
					}
				}
			} else if (type.equals(MYPROGRESS)) {
				if (null != data) {

					UserVO userVo = JSONUtil.toJavaBean(UserVO.class, data);
					String subjectId = userVo.getSubject().getSubjectid();
					LogUtil.print("myProgress----jsonString>" + jsonString);
					LogUtil.print("myProgress----subjectId>" + subjectId);

					initSubject(subjectId, userVo);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.print("focus---ed-->>" + epListView.isFocused());
		return true;
	}

	@Override
	public void doException(String type, Exception e, int code) {
		// super.doException(type, e, code);
		// LogUtil.print("");
		ZProgressHUD.getInstance(getActivity()).dismiss();
		noCaochErrorRl.setVisibility(View.VISIBLE);
		hasCaochRl.setVisibility(View.GONE);
		noCaochErrorIv.setBackgroundResource(R.drawable.app_no_wifi);
		noCaochErroTv.setText(CommonUtil.getString(getActivity(),
				R.string.no_wifi));
	}

	@Override
	public void doTimeOut(String type) {
		ZProgressHUD.getInstance(getActivity()).dismiss();
		noCaochErrorRl.setVisibility(View.VISIBLE);
		hasCaochRl.setVisibility(View.GONE);
		noCaochErrorIv.setBackgroundResource(R.drawable.app_no_wifi);
		noCaochErroTv.setText(CommonUtil.getString(getActivity(),
				R.string.no_wifi));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.learn_progress_yuekao:// 约考
			Intent intent = new Intent(getActivity(),
					AppointmentExamActivity.class);
			intent.putExtra("subjectid", subjectId);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		LogUtil.print("预约详情");

		if (position - 1 < 0) {
			return;
		}
		Intent intent = new Intent(getActivity(),
				AppointmentDetailActivity.class);
		intent.putExtra("appointmentDetail", list.get(position - 1));
		getActivity().startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == 0) {
				Toast.makeText(getActivity(), "onActivity-->" + requestCode,
						Toast.LENGTH_SHORT).show();
				obtainOppointment();

			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(AppointmentSuccessEvent event) {
		LogUtil.print("onActivityResult--------巅峰时代-");
		obtainOppointment();
	}

	@Override
	public void onRefresh() {
		isRefreshing = true;
		obtainOppointment();
	}

}
