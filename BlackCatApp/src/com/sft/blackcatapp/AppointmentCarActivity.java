package com.sft.blackcatapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.pull.LoadMoreView;
import cn.sft.pull.LoadMoreView.LoadMoreListener;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.adapter.AppointmentDetailStudentHoriListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.SubjectStatu;
import com.sft.common.Config.UserType;
import com.sft.dialog.CustomDialog;
import com.sft.event.AppointmentSuccessEvent;
import com.sft.fragment.AppointmentWeekFragment;
import com.sft.listener.OnDateClickListener;
import com.sft.listener.SameTimeStudentOnItemClickListener;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.view.WeekViewPager;
import com.sft.viewutil.ScrollTimeLayout;
import com.sft.viewutil.ScrollTimeLayout.OnTimeLayoutSelectedListener;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.AppointmentDay;
import com.sft.vo.CoachCourseV2VO;
import com.sft.vo.CoachVO;
import com.sft.vo.commentvo.CommentUser;

import de.greenrobot.event.EventBus;

/**
 * 预约学车
 * 
 * @author Administrator
 * 
 */
public class AppointmentCarActivity extends BaseActivity implements
		LoadMoreListener, SameTimeStudentOnItemClickListener {

	private static final String coachCourse = "coachCourse";
	private static final String appointmentCourse = "appointmentCourse";
	private static final String sameTimeStudent = "sameTimeStudent";
	private static final String getmyfirstcoach = "getmyfirstcoach";

	private RelativeLayout noCaochErrorRl;
	private ImageView noCaochErrorIv;
	private TextView noCaochErroTv;

	private WeekViewPager viewPager;
	private ScrollTimeLayout timeLayout;
	private float aspect = 360 / 225f;
	// 用户选择的日期
	private String selectDate;
	// 用户选择的教练
	private CoachVO selectCoach;
	// 教练课程列表
	private List<CoachCourseV2VO> courseList = new ArrayList<CoachCourseV2VO>();
	private TextView learnProgress1Tv;
	private TextView learnProgress2Tv;
	// private TextView learnProgress3Tv;
	// 同时段学员
	private LoadMoreView studentListView;
	private AppointmentDetailStudentHoriListAdapter sameTimeStudentAdapter;
	private List<CommentUser> userList = new ArrayList<CommentUser>();
	private int studentPage = 1;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_appointment_car);
		setTitleText(R.string.appointment_car);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.appointment_list);
		initViews();
		initData();
		// resizeLayout();
		setListener();
		EventBus.getDefault().register(this);
	}

	private void initViews() {
		viewPager = (WeekViewPager) findViewById(R.id.appointment_car_weekview);
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View paramView, Object paramObject) {

				return paramObject == paramView;
			}

			@Override
			public int getCount() {
				return 2;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);

			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				AppointmentWeekFragment fragment = new AppointmentWeekFragment(
						position);
				// LogUtil.print("position---------" + position);
				container.addView(fragment.rootview);
				return fragment.rootview;
			}
		});
		viewPager.setOnDateClickListener(new OnDateClickListener() {

			@Override
			public void onDateClick(AppointmentDay day, boolean clickbale) {
				if (clickbale) {
					// LogUtil.print("点击了" + day);
					selectDate = day.year + "-" + day.month + "-" + day.day;
					// LogUtil.print(selectDate);
					obtainCaochCourse(coachId);
				} else {
					// LogUtil.print("木有点击了" + day);
				}
			}
		});

		noCaochErrorRl = (RelativeLayout) findViewById(R.id.error_rl);
		noCaochErrorIv = (ImageView) findViewById(R.id.error_iv);
		noCaochErroTv = (TextView) findViewById(R.id.error_tv);
		hasAppointment = (LinearLayout) findViewById(R.id.appointment_car_ll);
		belowLayout = (RelativeLayout) findViewById(R.id.appointment_car_below_rl);
		noCaochErrorIv.setBackgroundResource(R.drawable.app_error_robot);
		noCaochErroTv.setText(CommonUtil.getString(this,
				R.string.no_appointment_coach_error_info));

		notimeTv = (TextView) findViewById(R.id.appointment_car_no_time_tv);
		notimeTv.setVisibility(View.GONE);
		noCaochErrorRl.setVisibility(View.GONE);
		hasAppointment.setVisibility(View.VISIBLE);
		belowLayout.setVisibility(View.VISIBLE);
		//
		timeLayout = (ScrollTimeLayout) findViewById(R.id.appointment_car_time);
		appointCommit = (Button) findViewById(R.id.appointment_car_commit_btn);

		learnProgress1Tv = (TextView) findViewById(R.id.appointment_car_select_course1_tv);
		learnProgress2Tv = (TextView) findViewById(R.id.appointment_car_select_course2_tv);
		// learnProgress3Tv = (TextView)
		// findViewById(R.id.appointment_car_select_course3_tv);
		setAppointmentTimeInfo();

		//
		studentListView = (LoadMoreView) findViewById(R.id.appointment_detail_horizon_listview);
		studentListView.setPullLoadMoreEnable(true);
		studentListView.setHorizontal();

		coachName = (TextView) findViewById(R.id.appointment_car_coach_name_tv);
		coachPic = (SelectableRoundedImageView) findViewById(R.id.appointment_car_coach_iv);
		coachPic.setScaleType(ScaleType.CENTER_CROP);
		coachPic.setImageResource(R.drawable.login_head);
		coachPic.setOval(true);

		changeCoChTv = (TextView) findViewById(R.id.appointment_car_change_coach_tv);

	}

	private void setAppointmentTimeInfo() {
		finishTime = 0;
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
		String subjectName = app.userVO.getSubject().getName();
		// if (appointTime != 0) {
		// learnProgress2Tv.setText("第" + (finishTime + 1) + "-"
		// + (appointTime + finishTime) + "课时 ");
		// } else {
		// learnProgress2Tv.setText("第" + (finishTime + 1) + "课时 ");
		// }
		learnProgress1Tv.setText(subjectName);

		// learnProgress3Tv.setText("完成" + finishTime + "课时");
	}

	private void resizeLayout() {
		ScrollView layout = (ScrollView) findViewById(R.id.appointment_car_time_layout);
		LinearLayout.LayoutParams timeLayoutParams = (LinearLayout.LayoutParams) layout
				.getLayoutParams();
		timeLayoutParams.height = (int) (screenWidth / aspect);
	}

	private void setListener() {
		timeLayout
				.setOnTimeLayoutSelectedListener(new MyOnTimeLayoutSelectedListener());
		appointCommit.setOnClickListener(this);
		studentListView.setLoadMoreListener(this);
		changeCoChTv.setOnClickListener(this);
		coachPic.setOnClickListener(this);
	}

	private void initData() {
		// 获取今天
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		selectDate = format.format(new Date());
		selectCoach = (CoachVO) getIntent().getSerializableExtra("coachVO");
		// 获取最近一次预约过的教练
		List<CoachVO> appointmentCoach = Util.getAppointmentCoach(this);
		if (selectCoach == null) {
			// 获取最近预约的教练
			if (appointmentCoach != null && appointmentCoach.size() != 0) {
				selectCoach = appointmentCoach.get(appointmentCoach.size() - 1);
			}
		}
		for (int i = 0; i < appointmentCoach.size(); i++) {
			LogUtil.print("appointmentCoach---"
					+ appointmentCoach.get(i).getName());
		}
		if (selectCoach != null) {
			coachName.setText("教练" + selectCoach.getName());
			RelativeLayout.LayoutParams headParam = (RelativeLayout.LayoutParams) coachPic
					.getLayoutParams();
			String url = selectCoach.getHeadportrait().getOriginalpic();
			if (TextUtils.isEmpty(url)) {
				coachPic.setBackgroundResource(R.drawable.login_head);
			} else {
				BitmapManager.INSTANCE.loadBitmap2(url, coachPic,
						headParam.width, headParam.height);
			}
			// LogUtil.print("要预约的教练---" + selectCoach.getName());
			coachId = selectCoach.getCoachid();
			obtainCaochCourse(coachId);

		} else {
			obtainMyFirstCoach();
			// noCaochErrorRl.setVisibility(View.VISIBLE);
			// hasAppointment.setVisibility(View.GONE);
			// belowLayout.setVisibility(View.GONE);
		}
	}

	private void obtainCaochCourse(String coachId) {
		if (!TextUtils.isEmpty(selectDate)) {
			// 先清空上个教练的课程
			courseList.clear();
			LogUtil.print(coachId + "----" + selectDate + "selectDate");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("coachid", coachId);
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("date", selectDate);
			LogUtil.print("coachid--" + coachId + "---userid--"
					+ app.userVO.getUserid());
			LogUtil.print("1111111----===" + selectDate);
			HttpSendUtils.httpGetSend(coachCourse, this, Config.IP
					+ "api/v1/courseinfo/getcoursebycoachv2", paramMap);
		}
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
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
		case R.id.base_right_tv:
			// intent = new Intent(this, AppointmentMoreCoachActivity.class);
			finish();
			break;
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.appointment_car_change_coach_tv:
			// 更多教练
			intent = new Intent(this, AppointmentMoreCoachActivity.class);
			break;

		case R.id.appointment_car_coach_iv:
			intent = new Intent(this, CoachDetailActivity.class);
			intent.putExtra("coach", selectCoach);
			startActivity(intent);
			break;
		default:
			break;
		}
		if (intent != null) {
			startActivityForResult(intent, v.getId());
		}
	}

	private void obtainMyFirstCoach() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("subjectid", app.userVO.getSubject().getSubjectid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils
				.httpGetSend(getmyfirstcoach, this, Config.IP
						+ "api/v1/userinfo/getmyfirstcoach", paramMap, 10000,
						headerMap);
	}

	private void appointmentCar() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		paramsMap.put("coachid", selectCoach.getCoachid());
		paramsMap.put("is_shuttle",
				selectCoach.getIs_shuttle().equals("true") ? "1" : "0");
		paramsMap.put("address", "");

		String courselist = "";
		List<CoachCourseV2VO> selectCourseList = timeLayout
				.getSelectCourseList();
		int length = selectCourseList.size();
		for (int i = 0; i < length; i++) {
			courselist += selectCourseList.get(i).getCoursedata().get_id();
			courselist += ",";
		}
		courselist = courselist.substring(0, courselist.length() - 1);
		paramsMap.put("courselist", courselist);

		paramsMap.put("begintime", selectDate
				+ " "
				+ selectCourseList.get(0).getCoursedata().getCoursetime()
						.getBegintime());
		paramsMap.put("endtime",
				selectDate
						+ " "
						+ selectCourseList.get(length - 1).getCoursedata()
								.getCoursetime().getEndtime());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(appointmentCourse, this, Config.IP
				+ "api/v1/courseinfo/userreservationcourse", paramsMap, 10000,
				headerMap);
	}

	private void obtainSameTimeStudent(int page) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("coachid", selectCoach.getCoachid());
		headerMap.put("begintime", beginTimeStemp + "");
		headerMap.put("endtime", endTimeStemp + "");
		HttpSendUtils.httpGetSend(sameTimeStudent, this, Config.IP
				+ "api/v1/courseinfo/sametimestudentsv2", headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		LogUtil.print("callBack-->" + jsonString);

		try {
			if (type.equals(coachCourse)) {
				if (!TextUtils.isEmpty(msg)) {

					// 清空时间表
					timeLayout.clearData();
					// ZProgressHUD.getInstance(this).show();
					// ZProgressHUD.getInstance(this).dismissWithFailure(msg,
					// 1000);
					notimeTv.setVisibility(View.VISIBLE);
					notimeTv.setText(msg);
					timeLayout.setVisibility(View.GONE);
					LogUtil.print(ZProgressHUD.getInstance(this).isShowing()
							+ "callBack--1111>" + jsonString);
					return true;
				} else {
					notimeTv.setVisibility(View.GONE);
					timeLayout.setVisibility(View.VISIBLE);

				}
				timeLayout.clearData();
				courseList.clear();

				if (dataArray != null) {
					noCaochErrorRl.setVisibility(View.GONE);
					hasAppointment.setVisibility(View.VISIBLE);
					belowLayout.setVisibility(View.VISIBLE);
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						CoachCourseV2VO coachCourseVO = JSONUtil.toJavaBean(
								CoachCourseV2VO.class,
								dataArray.getJSONObject(i));
						courseList.add(coachCourseVO);
					}
				}

				for (int i = 0; i < courseList.size(); i++) {

				}
				LogUtil.print("1111111----===" + courseList.size());
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
					// EventBus.getDefault().post(new
					// AppointmentSuccessEvent());
					// Intent intent = new Intent();
					// setResult(RESULT_OK, intent);

					// new MyHandler(1500) {
					// @Override
					// public void run() {
					// dialog.dismiss();
					// finish();
					// };
					// };
				}
			} else if (sameTimeStudent.equals(type)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length == 0) {
						userList.clear();
					}
					if (length > 0)
						studentPage++;
					for (int i = 0; i < length; i++) {
						CommentUser commentUser = JSONUtil.toJavaBean(
								CommentUser.class, dataArray.getJSONObject(i)
										.getJSONObject("userid"));
						if (!commentUser.get_id()
								.equals(app.userVO.getUserid())) {
							if (!userList.contains(commentUser)) {
								userList.add(commentUser);
								System.out.println(userList.size());
							}
						}
					}
				}
				if (sameTimeStudentAdapter == null) {
					sameTimeStudentAdapter = new AppointmentDetailStudentHoriListAdapter(
							this, userList);
				} else {
					sameTimeStudentAdapter.setData(userList);
				}
				studentListView.setAdapter(sameTimeStudentAdapter);
				studentListView.setLoadMoreCompleted();
			} else if (getmyfirstcoach.equals(type)) {
				if (data != null) {
					CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class, data);
					if (coachVO != null) {
						selectCoach = coachVO;
						coachName.setText("教练" + selectCoach.getName());
						RelativeLayout.LayoutParams headParam = (RelativeLayout.LayoutParams) coachPic
								.getLayoutParams();
						String url = selectCoach.getHeadportrait()
								.getOriginalpic();
						if (TextUtils.isEmpty(url)) {
							coachPic.setBackgroundResource(R.drawable.login_head);
						} else {
							BitmapManager.INSTANCE.loadBitmap2(url, coachPic,
									headParam.width, headParam.height);
						}
						coachId = selectCoach.getCoachid();
						obtainCaochCourse(coachId);
					}
				}
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
		// super.doException(type, e, code);
		ZProgressHUD.getInstance(this).dismiss();
		noCaochErrorRl.setVisibility(View.VISIBLE);
		hasAppointment.setVisibility(View.GONE);
		belowLayout.setVisibility(View.GONE);
		noCaochErrorIv.setBackgroundResource(R.drawable.app_no_wifi);
		noCaochErroTv.setText(CommonUtil.getString(this, R.string.no_wifi));
	}

	@Override
	public void doTimeOut(String type) {
		if (sameTimeStudent.equals(type))
			studentListView.setLoadMoreCompleted();
		ZProgressHUD.getInstance(this).dismiss();
		noCaochErrorRl.setVisibility(View.VISIBLE);
		hasAppointment.setVisibility(View.GONE);
		belowLayout.setVisibility(View.GONE);
		noCaochErrorIv.setBackgroundResource(R.drawable.app_no_wifi);
		noCaochErroTv.setText(CommonUtil.getString(this, R.string.no_wifi));
	}

	int selectBeginTime = 24;
	int selectEndTime = 0;

	private long beginTimeStemp;
	private long endTimeStemp;

	private String coachId;
	private LinearLayout hasAppointment;
	private Button appointCommit;
	private int finishTime;
	private TextView coachName;
	private SelectableRoundedImageView coachPic;
	private RelativeLayout belowLayout;
	private TextView notimeTv;
	private TextView changeCoChTv;

	class MyOnTimeLayoutSelectedListener implements
			OnTimeLayoutSelectedListener {

		@Override
		public void TimeLayoutSelectedListener(boolean selected,
				boolean canAppointOtherCoach, CoachCourseV2VO coachCourse) {

			if (canAppointOtherCoach) {
				Intent intent = new Intent(AppointmentCarActivity.this,
						AppointmentMoreCoachActivity.class);
				intent.putExtra("coachCourse", coachCourse);
				intent.putExtra("selectDate", selectDate);
				startActivityForResult(intent,
						R.id.appointment_car_change_coach_tv);
				// obtainUsefulcoachTimely(coachCourseVO.getTimeid());
				LogUtil.print("----可约其他教练");
				return;
			}
			if (timeLayout.getSelectCourseList() == null) {
				return;
			}
			LogUtil.print("========" + timeLayout.getSelectCourseList().size());
			if (timeLayout.getSelectCourseList().size() == 1) {
				learnProgress2Tv.setText("第" + (finishTime + 1) + "课时 ");
			} else if (timeLayout.getSelectCourseList().size() == 0) {
				learnProgress2Tv.setText("");
			} else {
				learnProgress2Tv
						.setText("第"
								+ (finishTime + 1)
								+ "-"
								+ (timeLayout.getSelectCourseList().size() + finishTime)
								+ "课时 ");
			}

			// learnProgressTv.setText(subjectName + " 第" + (finishTime + 1)
			// + endtime + "课时  完成" + finishTime + "课时");
			// if (!selected) {
			// return;
			// }
			selectEndTime = 0;
			selectBeginTime = 24;
			for (CoachCourseV2VO coachCourseVO : timeLayout
					.getSelectCourseList()) {
				String begintime = coachCourseVO.getCoursedata()
						.getCoursetime().getBegintime();
				String endtime = coachCourseVO.getCoursedata().getCoursetime()
						.getEndtime();
				if (selectBeginTime > Integer.parseInt(begintime.split(":")[0])) {
					selectBeginTime = Integer.parseInt(begintime.split(":")[0]);
				}

				if (selectEndTime < Integer.parseInt(endtime.split(":")[0])) {
					selectEndTime = Integer.parseInt(endtime.split(":")[0]);
				}

				// 转换成时间戳
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date begindate = null;
				Date enddate = null;
				if (selectDate != null) {
					try {
						begindate = simpleDateFormat.parse(selectDate + " "
								+ selectBeginTime + ":00:00");
						enddate = simpleDateFormat.parse(selectDate + " "
								+ selectEndTime + ":00:00");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				beginTimeStemp = begindate.getTime() / 1000;
				endTimeStemp = enddate.getTime() / 1000;
			}
			obtainSameTimeStudent(studentPage);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == R.id.appointment_car_change_coach_tv) {
				// 获取更多教练
				CoachVO coach = (CoachVO) data.getSerializableExtra("coach");
				//
				// for (int i = 0; i < coachList.size(); i++) {
				// if (coachList.get(i).getCoachid()
				// .equals(coach.getCoachid())) {
				// index = i;
				// break;
				// }
				// }
				// if (index >= 0) {
				// // 包含相同元素
				// coachList.remove(index);
				// }
				// coachList.add(0, coach);
				// adapter = new AppointmentCarCoachHoriListAdapter(this,
				// coachList, (int) (screenWidth - 75 * screenDensity));
				// adapter.setSelected(0);
				// coachListView.setAdapter(adapter);
				if (coach != null) {

					selectCoach = coach;
					coachId = selectCoach.getCoachid();
					coachName.setText("教练" + selectCoach.getName());
					RelativeLayout.LayoutParams headParam = (RelativeLayout.LayoutParams) coachPic
							.getLayoutParams();
					String url = selectCoach.getHeadportrait().getOriginalpic();
					if (TextUtils.isEmpty(url)) {
						coachPic.setBackgroundResource(R.drawable.login_head);
					} else {
						BitmapManager.INSTANCE.loadBitmap2(url, coachPic,
								headParam.width, headParam.height);
					}
					timeLayout.clearData();
					obtainCaochCourse(selectCoach.getCoachid());
				}
			}
		}
	}

	@Override
	public void onLoadMore() {
		obtainSameTimeStudent(studentPage);
	}

	@Override
	public void onSameTimeStudentItemClick(int position) {

		// Intent intent = new Intent(this, StudentInfoActivity.class);
		// intent.putExtra("studentId", sameTimeStudentAdapter.getItem(position)
		// .get_id());
		// startActivity(intent);

		// 进入聊天界面
		CommentUser commentUser = sameTimeStudentAdapter.getItem(position);
		String chatId = commentUser.get_id();
		Intent intent = null;
		if (!TextUtils.isEmpty(chatId)) {
			intent = new Intent(this, ChatActivity.class);
			intent.putExtra("chatId", chatId);
			intent.putExtra("chatName", commentUser.getName());
			intent.putExtra("chatUrl", commentUser.getHeadportrait()
					.getOriginalpic());
			intent.putExtra("userTypeNoAnswer", UserType.USER.getValue());
			startActivity(intent);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("无法获取对方信息");
		}
	}

	public void onEvent(AppointmentSuccessEvent event) {
		this.finish();
	}
}
