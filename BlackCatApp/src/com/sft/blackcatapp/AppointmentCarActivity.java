package com.sft.blackcatapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;
import cn.sft.pull.AutoScrollListener;
import cn.sft.pull.LoadMoreView;
import cn.sft.pull.LoadMoreView.LoadMoreListener;
import cn.sft.pull.OnItemClickListener;

import com.sft.adapter.AppointmentCarCoachHoriListAdapter;
import com.sft.adapter.AppointmentDetailStudentHoriListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.SubjectStatu;
import com.sft.dialog.CustomDialog;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.viewutil.ScrollTimeLayout;
import com.sft.viewutil.ScrollTimeLayout.OnTimeLayoutSelectedListener;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachCourseVO;
import com.sft.vo.CoachVO;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.commentvo.CommentUser;
import com.sft.vo.uservo.StudentSubject;

/**
 * 预约学车
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class AppointmentCarActivity extends BaseActivity implements
		OnCheckedChangeListener, OnItemClickListener, AutoScrollListener,
		LoadMoreListener {

	private static final String coachCourse = "coachCourse";
	private static final String appointmentCourse = "appointmentCourse";
	private static final String favouriteCoach = "favouriteCoach";
	private static final String sameTimeStudent = "sameTimeStudent";
	private static final String myCoach = "myCoach";

	// private static final String reservation = "reservation";
	// 日期列表
	private RadioGroup dateGroup;
	// radiobtn;
	private RadioButton firstDateBtn, secondDateBtn, threeDateBtn, fourDateBtn,
			fiveDateBtn, sixDateBtn, sevenDateBtn;

	// 接送地址标题
	private TextView shuttleTitleTv;

	// 接送布局
	private LinearLayout shuttleLayout;
	// 接送地址
	private TextView shuttleAddressTv;
	//
	private TextView curAppointmentTimeTv;
	//
	private TextView confirmAppointmentTimeTv;
	// 我的预约界面教练水平列表
	private LoadMoreView coachListView;
	// 预约学车没有教练提示
	private TextView noCoachTv;
	// 提交按钮
	private Button commitBtn;

	// 同时段学员
	private LoadMoreView studentListView;
	//
	private int studentPage = 1;
	//
	private MyAppointmentVO appointmentVO;
	private AppointmentDetailStudentHoriListAdapter sameTimeStudentAdapter;
	private List<CommentUser> userList = new ArrayList<CommentUser>();
	//
	private AppointmentCarCoachHoriListAdapter adapter;
	// 预约学车教练列表
	private List<CoachVO> coachList = new ArrayList<CoachVO>();
	// 教练课程列表
	private List<CoachCourseVO> courseList = new ArrayList<CoachCourseVO>();
	// 用户选择的教练
	private CoachVO selectCoach;
	// 用户选择的日期
	private String selectDate;

	private ScrollTimeLayout timeLayout;

	private float aspect = 360 / 225f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_appointment_car);
		initView();
		initData();
		initDate();
		resizeLayout();
		setListener();
		// obtainSameTimeStudent(studentPage);
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.appointment_car);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.more_coach);

		timeLayout = (ScrollTimeLayout) findViewById(R.id.appointment_car_time);

		shuttleLayout = (LinearLayout) findViewById(R.id.appointment_car_shuttle_layout);
		shuttleTitleTv = (TextView) findViewById(R.id.appointment_car_address_title_tv);
		shuttleTitleTv.getPaint().setFakeBoldText(true);
		shuttleAddressTv = (TextView) findViewById(R.id.appointment_car_address_content_tv);
		curAppointmentTimeTv = (TextView) findViewById(R.id.appointment_car_appointtime_tv);
		confirmAppointmentTimeTv = (TextView) findViewById(R.id.appointment_car_examtime_tv);
		noCoachTv = (TextView) findViewById(R.id.appointment_car_no_coach_tv);

		coachListView = (LoadMoreView) findViewById(R.id.appointment_car_horizon_listview);
		coachListView.setPullLoadMoreEnable(false);
		coachListView.setHorizontal();

		commitBtn = (Button) findViewById(R.id.appointment_car_commit_btn);

		dateGroup = (RadioGroup) findViewById(R.id.appointment_car_radiogroup);
		firstDateBtn = (RadioButton) findViewById(R.id.appointment_car_first_btn);
		secondDateBtn = (RadioButton) findViewById(R.id.appointment_car_second_btn);
		threeDateBtn = (RadioButton) findViewById(R.id.appointment_car_three_btn);
		fourDateBtn = (RadioButton) findViewById(R.id.appointment_car_four_btn);
		fiveDateBtn = (RadioButton) findViewById(R.id.appointment_car_five_btn);
		sixDateBtn = (RadioButton) findViewById(R.id.appointment_car_six_btn);
		sevenDateBtn = (RadioButton) findViewById(R.id.appointment_car_seven_btn);

		shuttleAddressTv.setText(app.userVO.getAddress());
		setAppointmentTimeInfo();
		shuttleLayout.setVisibility(View.GONE);

		String subjectId = app.userVO.getSubject().getSubjectid();
		StudentSubject subject = null;
		if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
			subject = app.userVO.getSubjecttwo();
		} else if (subjectId.equals(Config.SubjectStatu.SUBJECT_THREE
				.getValue())) {
			subject = app.userVO.getSubjectthree();
		}
		if (subject != null) {
			if (subject.getReservation() + subject.getFinishcourse() >= subject
					.getTotalcourse()) {
				commitBtn.setText(app.userVO.getSubject().getName() + "学时已约满");
			} else {
				commitBtn.setOnClickListener(this);
			}
		}

		studentListView = (LoadMoreView) findViewById(R.id.appointment_detail_horizon_listview);
		studentListView.setPullLoadMoreEnable(true);
		studentListView.setHorizontal();

	}

	private void setAppointmentTimeInfo() {
		int finishTime = 0;
		int appointTime = 0;
		if (app.userVO.getSubject().getSubjectid()
				.equals(SubjectStatu.SUBJECT_TWO.getValue())) {
			finishTime = app.userVO.getSubjecttwo().getFinishcourse();
			appointTime = app.userVO.getSubjecttwo().getReservation();
		} else if (app.userVO.getSubject().getSubjectid()
				.equals(SubjectStatu.SUBJECT_THREE.getValue())) {
			finishTime = app.userVO.getSubjectthree().getFinishcourse();
			appointTime = app.userVO.getSubjectthree().getReservation();
		}
		if (appointTime == 0) {
			curAppointmentTimeTv.setText("当前没有预约");
		} else {
			int endtime = appointTime + finishTime;
			curAppointmentTimeTv.setText("当前预约了"
					+ app.userVO.getSubject().getName() + "第"
					+ (finishTime + 1) + "-" + endtime + "课时");
		}
		confirmAppointmentTimeTv.setText("已确认练车时间为：" + finishTime + "课时");
	}

	private void initData() {
		appointmentVO = (MyAppointmentVO) getIntent().getSerializableExtra(
				"appointment");
		coachListView.setVisibility(View.GONE);
		obtainFavouriteCoach();
		// obtainOppointment();

	}

	private void initDate() {
		String[] dates = new String[7];
		SimpleDateFormat format = new SimpleDateFormat("MM" + "月" + "dd" + "日");
		dates[0] = format.format(new Date());

		Calendar c = Calendar.getInstance();
		for (int i = 1; i < 7; i++) {
			int mCurrentDay = c.get(Calendar.DAY_OF_MONTH);
			c.set(Calendar.DAY_OF_MONTH, mCurrentDay + 1);
			dates[i] = format.format(c.getTime());
		}

		firstDateBtn.setText(dates[0]);
		secondDateBtn.setText(dates[1]);
		threeDateBtn.setText(dates[2]);
		fourDateBtn.setText(dates[3]);
		fiveDateBtn.setText(dates[4]);
		sixDateBtn.setText(dates[5]);
		sevenDateBtn.setText(dates[6]);

		int year = Calendar.getInstance().get(Calendar.YEAR);
		String text = firstDateBtn.getText().toString();
		selectDate = year + "-" + text.replace("月", "-").replace("日", "");
	}

	private void initCoachListData() {
		adapter = new AppointmentCarCoachHoriListAdapter(this, coachList,
				(int) (screenWidth - 75 * screenDensity));
		if (selectCoach == null && coachList.size() > 0) {
			selectCoach = coachList.get(0);
			adapter.setSelected(0);
			if (selectCoach.getIs_shuttle().equals("true")) {
				shuttleLayout.setVisibility(View.VISIBLE);
			} else {
				shuttleLayout.setVisibility(View.GONE);
			}
		}
		coachListView.setAdapter(adapter);
	}

	private void resizeLayout() {
		ScrollView layout = (ScrollView) findViewById(R.id.appointment_car_time_layout);
		LinearLayout.LayoutParams timeLayoutParams = (LinearLayout.LayoutParams) layout
				.getLayoutParams();
		timeLayoutParams.height = (int) (screenWidth / aspect);
	}

	private void setListener() {
		shuttleLayout.setOnClickListener(this);
		dateGroup.setOnCheckedChangeListener(this);
		coachListView.setAutoScrollListener(this);
		studentListView.setLoadMoreListener(this);
		timeLayout
				.setOnTimeLayoutSelectedListener(new MyOnTimeLayoutSelectedListener());
	}

	private void obtainFavouriteCoach() {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(favouriteCoach, this, Config.IP
				+ "api/v1/userinfo/favoritecoach", null, 10000, headerMap);
	}

	// private void obtainOppointment() {
	// Map<String, String> paramMap = new HashMap<String, String>();
	// paramMap.put("userid", app.userVO.getUserid());
	// paramMap.put("subjectid", getIntent().getStringExtra("subject"));
	// Map<String, String> headerMap = new HashMap<String, String>();
	// headerMap.put("authorization", app.userVO.getToken());
	// HttpSendUtils.httpGetSend(reservation, this, Config.IP
	// + "api/v1/courseinfo/getmyreservation", paramMap, 10000,
	// headerMap);
	// }

	private void obtainSameTimeStudent(int page) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("coachid", selectCoach.getCoachid());
		headerMap.put("begintime", beginTimeStemp + "");
		headerMap.put("endtime", endTimeStemp + "");
		HttpSendUtils.httpGetSend(sameTimeStudent, this, Config.IP
				+ "api/v1/courseinfo/sametimestudentsv2", headerMap);
	}

	private void obtainCaochCourse(String coachId) {
		if (!TextUtils.isEmpty(selectDate)) {
			// 先清空上个教练的课程
			courseList.clear();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("coachid", coachId);
			paramMap.put("date", selectDate);
			HttpSendUtils.httpGetSend(coachCourse, this, Config.IP
					+ "api/v1/courseinfo/getcoursebycoach", paramMap);
		}
	}

	private void obtainMyCoach(String coachId) {
		System.out.println(coachId);
		HttpSendUtils.httpGetSend(myCoach, this, Config.IP
				+ "api/v1/userinfo/getuserinfo" + "/2/userid/" + coachId);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			intent = new Intent(this, AppointmentMoreCoachActivity.class);
			break;
		case R.id.appointment_car_shuttle_layout:
			intent = new Intent(this, ShuttleAddressActivity.class);
			break;
		case R.id.appointment_car_commit_btn:
			if (timeLayout.getSelectCourseList().size() == 0) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("选择课程");
			} else if (timeLayout.isTimeBlockCon()) {
				if (selectCoach.getDriveschoolinfo().getId()
						.equals(app.userVO.getApplyschoolinfo().getId())) {
					appointmentCar();
				} else {
					CustomDialog dialog = new CustomDialog(this,
							CustomDialog.ERROR_COACH);
					dialog.show();
				}
			} else {
				CustomDialog dialog = new CustomDialog(this,
						CustomDialog.APPOINTMENT_TIME_ERROR);
				dialog.show();
			}
			break;
		}
		if (intent != null) {
			startActivityForResult(intent, v.getId());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == R.id.appointment_car_shuttle_layout) {
				String address = data.getStringExtra("address");
				shuttleAddressTv.setText(address);
			} else if (requestCode == R.id.base_right_tv) {
				// 获取更多教练
				CoachVO coach = (CoachVO) data.getSerializableExtra("coach");

				coachListView.setVisibility(View.VISIBLE);
				noCoachTv.setVisibility(View.GONE);
				int index = -1;
				for (int i = 0; i < coachList.size(); i++) {
					if (coachList.get(i).getCoachid()
							.equals(coach.getCoachid())) {
						index = i;
						break;
					}
				}
				if (index >= 0) {
					// 包含相同元素
					coachList.remove(index);
				}
				coachList.add(0, coach);
				adapter = new AppointmentCarCoachHoriListAdapter(this,
						coachList, (int) (screenWidth - 75 * screenDensity));
				adapter.setSelected(0);
				coachListView.setAdapter(adapter);
				if (selectCoach == null
						|| !coach.getCoachid().equals(selectCoach.getCoachid())) {
					selectCoach = coach;
					timeLayout.clearData();
					obtainCaochCourse(selectCoach.getCoachid());
				}
			}
		}
	}

	private void appointmentCar() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		paramsMap.put("coachid", selectCoach.getCoachid());
		paramsMap.put("is_shuttle",
				selectCoach.getIs_shuttle().equals("true") ? "1" : "0");
		paramsMap.put("address", shuttleAddressTv.getText().toString());

		String courselist = "";
		List<CoachCourseVO> selectCourseList = timeLayout.getSelectCourseList();
		int length = selectCourseList.size();
		for (int i = 0; i < length; i++) {
			courselist += selectCourseList.get(i).get_id();
			courselist += ",";
		}
		courselist = courselist.substring(0, courselist.length() - 1);
		paramsMap.put("courselist", courselist);

		paramsMap.put("begintime", selectDate + " "
				+ selectCourseList.get(0).getCoursetime().getBegintime());
		paramsMap.put("endtime",
				selectDate
						+ " "
						+ selectCourseList.get(length - 1).getCoursetime()
								.getEndtime());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(appointmentCourse, this, Config.IP
				+ "api/v1/courseinfo/userreservationcourse", paramsMap, 10000,
				headerMap);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int index = 0;
		switch (checkedId) {
		case R.id.appointment_car_first_btn:
			index = 0;
			break;
		case R.id.appointment_car_second_btn:
			index = 1;
			break;
		case R.id.appointment_car_three_btn:
			index = 2;
			break;
		case R.id.appointment_car_four_btn:
			index = 3;
			break;
		case R.id.appointment_car_five_btn:
			index = 4;
			break;
		case R.id.appointment_car_six_btn:
			index = 5;
			break;
		case R.id.appointment_car_seven_btn:
			index = 6;
			break;
		}
		timeLayout.clearData();
		RadioButton btn = (RadioButton) group.getChildAt(index);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String text = btn.getText().toString();
		selectDate = year + "-" + text.replace("月", "-").replace("日", "");
		if (selectCoach != null)
			obtainCaochCourse(selectCoach.getCoachid());
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(myCoach)) {
				if (data != null) {
					CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class, data);
					if (!coachList.contains(coachVO)) {
						coachList.add(coachVO);
					}
				}
				initCoachListData();
				if (coachList.size() > 0) {
					coachListView.setVisibility(View.VISIBLE);
					noCoachTv.setVisibility(View.GONE);
					obtainCaochCourse(selectCoach.getCoachid());
				}
			} else if (type.equals(favouriteCoach)) {
				CoachVO tempCoachVO = (CoachVO) getIntent()
						.getSerializableExtra("coach");
				if (tempCoachVO != null)
					coachList.add(tempCoachVO);
				try {
					List<CoachVO> tempList = Util.getAppointmentCoach(this);
					if (tempList != null && tempList.size() > 0) {
						if (tempList.contains(tempCoachVO)) {
							tempList.remove(tempCoachVO);
						}
						Collections.reverse(tempList);
						coachList.addAll(tempList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (dataArray != null) {
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						if (coachList.contains(coachVO)) {
							continue;
						}
						if (app.userVO.getApplyschoolinfo().getId()
								.equals(coachVO.getDriveschoolinfo().getId())) {
							coachList.add(coachVO);
						}
					}
				}
				// 获取报名时填写的教练
				String coachId = app.userVO.getApplycoachinfo().getId();
				if (!TextUtils.isEmpty(coachId)) {
					obtainMyCoach(coachId);

				} else {
					initCoachListData();
					if (coachList.size() > 0) {
						coachListView.setVisibility(View.VISIBLE);
						noCoachTv.setVisibility(View.GONE);
						obtainCaochCourse(selectCoach.getCoachid());
					}
				}

			} else if (type.equals(coachCourse)) {
				courseList.clear();
				if (dataArray != null) {
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						CoachCourseVO coachCourseVO = JSONUtil
								.toJavaBean(CoachCourseVO.class,
										dataArray.getJSONObject(i));
						courseList.add(coachCourseVO);
					}
				}
				timeLayout.setData(courseList, aspect);
			} else if (type.equals(appointmentCourse)) {
				if (dataString != null) {
					sendBroadcast(new Intent(
							MyAppointmentActivity.class.getName()).putExtra(
							"isRefresh", true));
					final CustomDialog dialog = new CustomDialog(this,
							CustomDialog.APPOINTMENT_TIME_SUCCESS);
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();

					// 预约成功，保存当前的教练，以备下次预约
					Util.saveAppointmentCoach(this, selectCoach);
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					new MyHandler(1500) {
						@Override
						public void run() {
							dialog.dismiss();
							finish();
						};
					};
				}
			} else if (sameTimeStudent.equals(type)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0)
						studentPage++;
					for (int i = 0; i < length; i++) {
						CommentUser commentUser = JSONUtil.toJavaBean(
								CommentUser.class, dataArray.getJSONObject(i)
										.getJSONObject("userid"));
						if (!commentUser.get_id()
								.equals(app.userVO.getUserid()))
							userList.add(commentUser);
					}
				}
				if (sameTimeStudentAdapter == null) {
					sameTimeStudentAdapter = new AppointmentDetailStudentHoriListAdapter(
							getBaseContext(), userList);
				} else {
					sameTimeStudentAdapter.setData(userList);
				}
				studentListView.setAdapter(sameTimeStudentAdapter);
				studentListView.setLoadMoreCompleted();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (sameTimeStudent.equals(type))
			studentListView.setLoadMoreCompleted();
		super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (sameTimeStudent.equals(type))
			studentListView.setLoadMoreCompleted();
		super.doTimeOut(type);
	}

	@Override
	public void onItemClick(int position) {
		util.print("position=" + position);
		adapter.setSelected(position);
		coachListView.scrollToIndex(position);
		coachListView.setAdapter(adapter);
		selectCoach = adapter.getItem(position);
		if (selectCoach.getIs_shuttle().equals("true")) {
			shuttleLayout.setVisibility(View.VISIBLE);
		} else {
			shuttleLayout.setVisibility(View.GONE);
		}
		timeLayout.clearData();
		// 更新课程
		obtainCaochCourse(selectCoach.getCoachid());
	}

	@Override
	public void autoScroll(RecyclerView recyclerView) {
		LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView
				.getLayoutManager();
		int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
		int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
		View firstView = recyclerView
				.findViewHolderForPosition(firstVisibleItem).itemView;
		recyclerView.getChildAt(firstVisibleItem);
		int itemWidth = firstView.getWidth();
		Rect firstRect = new Rect();
		firstView.getHitRect(firstRect);

		int curSelect = adapter.getSelected();
		int scorooToPosition = 0;
		if (lastVisibleItem - firstVisibleItem <= 1) {
			if (firstView.getRight() > itemWidth / 2) {
				scorooToPosition = firstVisibleItem;
			} else {
				scorooToPosition = lastVisibleItem;
			}
			coachListView.scrollToIndex(scorooToPosition);
		} else {
			scorooToPosition = firstVisibleItem + 1;
		}
		if (scorooToPosition != curSelect) {
			onItemClick(scorooToPosition);
		}
	}

	@Override
	public void onLoadMore() {
		obtainSameTimeStudent(studentPage);
	}

	String selectBeginTime = "24:00:00";
	String selectEndTime = "24:00:00";

	private long beginTimeStemp;
	private long endTimeStemp;

	class MyOnTimeLayoutSelectedListener implements
			OnTimeLayoutSelectedListener {

		@Override
		public void TimeLayoutSelectedListener() {
			if (timeLayout.getSelectCourseList() == null
					|| timeLayout.getSelectCourseList().size() == 0) {
				return;
			}
			selectEndTime = "24:00:00";
			selectBeginTime = "24:00:00";
			for (CoachCourseVO coachCourseVO : timeLayout.getSelectCourseList()) {
				String begintime = coachCourseVO.getCoursetime().getBegintime();
				String endtime = coachCourseVO.getCoursetime().getEndtime();
				if (begintime.length() < selectBeginTime.length()) {
					selectBeginTime = begintime;
				} else if (begintime.length() == selectBeginTime.length()) {

					if (begintime.compareTo(selectBeginTime) < 0) {
						selectBeginTime = begintime;
					}
				}

				if (endtime.length() < selectEndTime.length()) {
					selectBeginTime = begintime;
				} else if (endtime.length() == selectEndTime.length()) {

					if (endtime.compareTo(selectEndTime) > 0) {
						selectEndTime = endtime;
					}
				}

				// 转换成时间戳
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date begindate = null;
				Date enddate = null;
				if (selectDate != null) {
					try {
						begindate = simpleDateFormat.parse(selectDate + " "
								+ selectBeginTime);
						enddate = simpleDateFormat.parse(selectDate + " "
								+ selectEndTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				beginTimeStemp = begindate.getTime() / 1000;
				endTimeStemp = enddate.getTime() / 1000;
			}
			LogUtil.print("时间戳" + beginTimeStemp);
			LogUtil.print(selectDate + " " + selectBeginTime);
			obtainSameTimeStudent(studentPage);
		}
	}
}
