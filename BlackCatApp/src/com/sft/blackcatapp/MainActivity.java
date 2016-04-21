package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.google.gson.reflect.TypeToken;
import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.adapter.OpenCityAdapter;
import com.sft.api.ApiHttpClient;
import com.sft.blackcatapp.home.view.MainScreenContainer;
import com.sft.blackcatapp.home.view.MainScreenContainer.OnTabLisener;
import com.sft.city.Act_City;
import com.sft.city.City;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.common.Config.SubjectStatu;
import com.sft.dialog.CheckApplyDialog;
import com.sft.dialog.CustomDialog;
import com.sft.dialog.NoCommentDialog;
import com.sft.dialog.NoCommentDialog.ClickListenerInterface;
import com.sft.fragment.MenuFragment.SLMenuListOnItemClickListener;
import com.sft.util.BaseUtils;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.SharedPreferencesUtil;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.ActivitiesVO;
import com.sft.vo.CoachVO;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.OpenCityVO;
import com.sft.vo.QuestionVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.VersionVO;

public class MainActivity extends BaseMainActivity implements
		SLMenuListOnItemClickListener, OnClickListener, OnTabLisener,
		OnCheckedChangeListener, OnRatingBarChangeListener {

	/** 强制评价 */

	private RelativeLayout rlMustComment;

	/** 是否存在未评价 订单 */
	private static boolean notComment = false;

	//
	private static final String subjectContent = "subjectContent";
	private static final String coach = "coach";
	private static final String school = "school";
	private static final String NOT_COMMENT = "nocomment";
	private static final String comment = "comment";
	private static final String version = "version";
	private static final String questionaddress = "questionaddress";
	private static final String TODAY_IS_OPEN_ACTIVITIES = "today_is_open_activities";
	public static final String ISCLICKCONFIRM = "isclickconfirm";
	private final static String openCity = "openCity";
	//
	private MapView mapView;
	//
	private BaiduMap mBaiduMap;

	// private LocalActivityManager activityManager = null;

	private ImageView home_btn;
	// private SlidingMenu mSlidingMenu;
	// /** 左阴影部分 */
	// public ImageView shade_left;
	// /** Item宽度 */
	// /** 右阴影部分 */
	// public ImageView shade_right;
	// /** 请求CODE */
	// public final static int CHANNELREQUEST = 1;
	// /** 调整返回的RESULTCODE */
	// public final static int CHANNELRESULT = 10;

	SimpleDateFormat df;

	/** 目标 页 */
	public static int TARGET_TAB = 0;

	/**
	 * 框架
	 */
	public static final String SELECT_TAB_NAME = "SELECT_TAB_NAME";
	public static final int TAB_APPLY = 1;// 报名
	public static final int TAB_STUDY = 2;// 学习
	public static final int TAB_APPOINTMENT = 3;// 预约
	public static final int TAB_MALL = 4;// 商城
	public static final int TAB_COMMUNITY = 5;// 社区

	private static final String STATE_KEY_INT_SELECTED_TAB = "selected_tab";

	private MainScreenContainer mMainContainer;
	public static final String ACTION_REFRESH_REDPOINT = "ACTION_REFRESH_REDPOINT";// 刷新红点状态
	public static final String ACTION_SHOW_TAB = "ACTION_SHOW_TAB";// 显示tab
	public static final String ACTION_HIDE_TAB = "ACTION_HIDE_TAB";// 隐藏tab
	private final BroadcastReceiver mMainScreenLocalReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}

			String act = intent.getAction();
			if (ACTION_REFRESH_REDPOINT.equals(act)) {
				mMainContainer.refreshTab();
			} else if (ACTION_HIDE_TAB.equals(act)) {
				mMainContainer.hideTabContainer();
			} else if (ACTION_SHOW_TAB.equals(act)) {
				mMainContainer.showTabContainer();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		setContentView(R.layout.frame_content);
		// EventBus.getDefault().register(this);
		init();
		initView();
		initData(savedInstanceState);
		setListener();
		// 气球按钮
		// initBalloon();
		obtainVersionInfo();
		obtainFavouriteSchool();
		obtainFavouriteCoach();
		obtainQuestionAddress();
		obtainSubjectContent();
		LogUtil.print("---ppp===" + this.getPackageName().replace(".", ""));
		// LogUtil.print("app--->" + app + "user::" +
		// app.userVO.getApplystate());
		// if (app.userVO!=null && app.userVO.getApplystate().equals("0")) {
		// // 填写课时信息
		// checkStateDialog();
		if (app.userVO != null && app.userVO.getApplystate().equals("0")) {
			// 只弹出一次进入验证学车进度的判断弹出框
			// if (!SharedPreferencesUtil.getBoolean(this, ISCLICKCONFIRM,
			// false)) {
			// checkStateDialog();
			// }
		} else {
			// 获取未评论列表

		}

		setTag();
		if (app != null && app.isLogin) {
			util.print("userid=" + app.userVO.getUserid());
		}
	}

	// }
	/**
	 * 版本号
	 */
	private void obtainVersionInfo() {
		HttpSendUtils.httpGetSend(version, this, Config.IP
				+ "api/v1/appversion/1");
	}

	/**
	 * 检查学时状态 对话框，
	 */
	private void checkStateDialog() {
		// 没报名
		// LogUtil.print("state---->" + app.userVO.getApplystate());
		if (app.isLogin) {
			if (app.userVO.getApplystate().equals("0")) {
				CheckApplyDialog dialog = new CheckApplyDialog(this);
				dialog.setTextAndImage("是,我已报名", "您是否已经报名学车", "否，我要学车",
						R.drawable.ic_question);
				dialog.setListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						// 保存选择状态，默认是否,
						SharedPreferencesUtil.putBoolean(MainActivity.this,
								ISCLICKCONFIRM, true);
						startActivity(new Intent(MainActivity.this,
								TestingPhoneActivity.class));

					}
				});
				dialog.show();

			}
		}

	}

	/**
	 * 弹出前去报名的对话框
	 */
	private void gotoApplyDialog() {
		final CheckApplyDialog dialog = new CheckApplyDialog(this);
		dialog.setTextAndImage("前去报名", "抱歉，貌似您还没有报名", "再看看",// 您还没有报名，请前去报名
				R.drawable.appointment_time_error);
		dialog.showBottom();
		dialog.setListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mMainContainer.showTab(TAB_APPLY);
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	private int sum = 0;

	private RelativeLayout MainRl;

	private void setTag() {
		if (app.isLogin) {
			// app.userVO.getUserid()
			LogUtil.print("jpush---userId--->" + app.userVO.getUserid());
			JPushInterface.setAliasAndTags(this, app.userVO.getUserid(), null,
					new MyTagAliasCallback());
			// JPushInterface.setAlias(this, app.userVO.getUserid(),
			// new MyTagAliasCallback());
			LogUtil.print("jpush---userId---end>" + app.userVO.getUserid());
		}
	}

	private class MyTagAliasCallback implements TagAliasCallback {

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			LogUtil.print("----------TagAliasCallback============");
			LogUtil.print("jpush--->MainActivity---->" + arg1);
			sum++;
			if (arg0 != 0 && sum < 5) {
				setTag();
			}
		}

	}

	private void initView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
		mapView = (MapView) findViewById(R.id.main_bmapView);
		mBaiduMap = mapView.getMap();
		// set the Behind View
		// setBehindContentView(R.layout.frame_left_menu);

		home_btn = (ImageView) findViewById(R.id.home_btn);
		titleLeftIv = (ImageView) findViewById(R.id.title_left_iv);
		titleTv = (TextView) findViewById(R.id.title_tv);
		rg = (RadioGroup) findViewById(R.id.title_gp);
		rg.setOnCheckedChangeListener(this);
		titleRightIv = (ImageView) findViewById(R.id.title_right_iv);
		titleRightTv = (TextView) findViewById(R.id.title_right_tv);
		titleFarRightIv = (ImageView) findViewById(R.id.title_far_right_iv);

		rlMustComment = (RelativeLayout) findViewById(R.id.main_force_evaluate_rl);
		MainRl = (RelativeLayout) findViewById(R.id.main_rl);
		rlMustComment.setVisibility(View.GONE);
	}

	private void initData(Bundle savedInstanceState) {

		// 如果用户没有报名，读取用户报名填写过的信息
		if (app.userVO != null
				&& app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			app.selectEnrollSchool = Util.getEnrollUserSelectedSchool(this);
			app.selectEnrollCoach = Util.getEnrollUserSelectedCoach(this);
			app.selectEnrollCarStyle = Util.getEnrollUserSelectedCarStyle(this);
			app.selectEnrollClass = Util.getEnrollUserSelectedClass(this);

		}
		refreshUI();
		app.curCity = util.readParam(Config.USER_CITY);
		initMyLocation();
	}

	private void setListener() {
		home_btn.setOnClickListener(this);
		titleLeftIv.setOnClickListener(this);
		titleRightIv.setOnClickListener(this);
		titleRightTv.setOnClickListener(this);
		titleTv.setOnClickListener(this);
		titleFarRightIv.setOnClickListener(this);
	}

	// 左侧菜单条目点击
	@Override
	public void selectItem(int position, String title) {
		Intent intent;
		switch (position) {

		case 0:
			// Toast.makeText(getBaseContext(), "查找教练", 0).show();
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case 1:
			// Toast.makeText(getBaseContext(), "查找教练", 0).show();
			intent = new Intent(this, EnrollSchoolActivity.class);
			startActivity(intent);
			break;
		case 2:
			if (app.isLogin) {
				intent = new Intent(this, MessageActivity.class);
				startActivity(intent);
			} else {
				BaseUtils.toLogin(MainActivity.this);

				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		case 3:
			if (app.isLogin) {
				intent = new Intent(this, MyWalletActivity.class);
				startActivity(intent);
			} else {
				BaseUtils.toLogin(MainActivity.this);

				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		case 4:
			if (app.isLogin) {
				intent = new Intent(this, PersonCenterActivity.class);
				startActivityForResult(intent, position);
			} else {
				BaseUtils.toLogin(MainActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.print(resultCode + "onActivityResult--》" + data);
		switch (currentPage) {
		case TAB_APPLY:
			if (mMainContainer.enrollFragment.type == 0) {// 驾校
				if (resultCode == 9) {// 城市
					City city = (City) data.getSerializableExtra("city");
					app.curCity = city.name;
					mMainContainer.enrollFragment.schoolFragment
							.requestByCity(city.name);
					titleRightTv.setText(app.curCity);
				}
				// mMainContainer.enrollFragment.schoolFragment.onActivityResult(requestCode,
				// resultCode, data);
			} else {// 教练
				if (resultCode == 9) {// 城市
					City city = (City) data.getSerializableExtra("city");
					app.curCity = city.name;
					mMainContainer.enrollFragment.coachFragment
							.requestByCity(city.name);
					titleRightTv.setText(app.curCity);
				}
				// mMainContainer.enrollFragment.coachFragment.onActivityResult(requestCode,
				// resultCode, data);
			}
			// MainScreenTab.
			break;
		}

	}

	private boolean hasAppointment = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_btn:
			drawerLayout.openDrawer(Gravity.START);
			// toggle(); // 动态判断自动关闭或开启SlidingMenu
			break;
		case R.id.title_far_right_iv:
		case R.id.title_right_tv:
			switch (currentPage) {
			case TAB_APPLY:
				// 定位
				LogUtil.print("定位");
				obtainOpenCity();
				startActivityForResult(new Intent(MainActivity.this,
						Act_City.class), 2);
				break;
			case TAB_APPOINTMENT:
				// 换教练
				Intent intent = null;
				LogUtil.print("换教练");
				if (app.isLogin) {
					LogUtil.print("换教练"
							+ app.userVO.getSubject().getSubjectid()
							+ app.userVO.getApplystate());
					if (app.userVO.getApplystate().equals(
							EnrollResult.SUBJECT_NONE.getValue())) {
						// 还没有报名
						gotoApplyDialog();

					} else if (app.userVO.getApplystate().equals(
							EnrollResult.SUBJECT_ENROLLING.getValue())) {
						final CustomDialog dialog = new CustomDialog(this,
								CustomDialog.ENROLLING);
						dialog.show();
						new MyHandler(1000) {
							@Override
							public void run() {
								dialog.dismiss();
							}
						};
					} else {

						if (app.userVO.getSubject().getSubjectid()
								.equals(SubjectStatu.SUBJECT_ONE.getValue())) {
							ZProgressHUD.getInstance(this).show();
							ZProgressHUD.getInstance(this).dismissWithSuccess(
									"待学习科目二再预约");
						} else {
							intent = new Intent(this,
									AppointmentCarActivity.class);
							intent.putExtra("subject", app.userVO.getSubject()
									.getSubjectid());
						}

					}

				} else {
					BaseUtils.toLogin(this);
					// NoLoginDialog dialog = new NoLoginDialog(this);
					// dialog.show();
				}
				if (intent != null) {
					startActivityForResult(intent, TAB_APPOINTMENT);
				}
				break;
			case TAB_COMMUNITY:

				break;
			case TAB_MALL:

				break;
			case TAB_STUDY:

				break;

			default:
				break;
			}

			break;
		case R.id.title_left_iv:
			// 切换为驾校
			// break;
		case R.id.title_right_iv:
			// // 切换为教练
			// break;
		case R.id.title_tv:
			if (mMainContainer.getCurrentFragment().equals(
					mMainContainer.enrollFragment)) {
				mMainContainer.enrollFragment.switchSchoolOrCoach();
				if (mMainContainer.enrollFragment.type == 0) {
					titleTv.setText(R.string.driving_school);
				} else {
					titleTv.setText(R.string.coach);
				}
			}
			break;

		// 未评价
		case R.id.dialog_comment_commit_btn:// 提交评价
		case R.id.dialog_comment_more_commit_btn:// 提交评价
			int rating = (int) ratingBar.getRating();
			String content = commentEdit.getText().toString();
			LogUtil.print(rating + content);

			float punctual = punctualStar.getRating();
			float attitude = attitudeStar.getRating();
			float ability = abilityStar.getRating();
			float total = totalStar.getRating();
			// comment(String reservationId, String starLevel,
			// String attitudelevel, String timelevel, String abilitylevel,
			// String content) {
			if (TextUtils.isEmpty(content.trim())) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("请输入评论内容");
			} else {
				if (rating == 0) {
					comment(myAppointmentVO.get_id(), total + "",
							attitude + "", punctual + "", ability + "", content);

				} else {
					comment(myAppointmentVO.get_id(), rating + "", attitude
							+ "", punctual + "", ability + "", content);

				}
			}
			break;

		case R.id.dialog_comment_more_btn:
			commentTitleTv.setVisibility(View.GONE);
			ratingBar.setVisibility(View.GONE);
			commentBtns.setVisibility(View.GONE);

			commentStarsLl.setVisibility(View.VISIBLE);
			moreCommitBtn.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取未评论列表
	 */
	private void obtainNotComments() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		LogUtil.print("subject---Id==>"
				+ app.userVO.getSubject().getSubjectid());
		paramMap.put("subjectid", app.userVO.getSubject().getSubjectid());// 订单的状态
																			// //
																			// 0
																			// 订单生成
																			// 2
																			// 支付成功
																			// 3
																			// 支付失败
																			// 4
																			// 订单取消
																			// -1
																			// 全部(未支付的订单)

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(NOT_COMMENT, this, Config.IP
				+ "api/v1/courseinfo/getmyuncommentreservation", paramMap,
				10000, headerMap);
	}

	private void obtainOpenCity() {
		HttpSendUtils.httpGetSend(openCity, this, Config.IP
				+ "api/v1/getopencity");
	}

	private void obtainSubjectContent() {
		HttpSendUtils.httpGetSend(subjectContent, this, Config.IP
				+ "api/v1/trainingcontent");
	}

	private void obtainQuestionAddress() {
		HttpSendUtils.httpGetSend(questionaddress, this, Config.IP
				+ "api/v1/info/examquestion");
	}

	private void obtainFavouriteSchool() {
		if (app.isLogin) {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(school, this, Config.IP
					+ "api/v1/userinfo/favoriteschool", null, 10000, headerMap);
		}
	}

	private void obtainFavouriteCoach() {
		if (app.isLogin) {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(coach, this, Config.IP
					+ "api/v1/userinfo/favoritecoach", null, 10000, headerMap);
		}
	}

	private void comment(String reservationId, String starLevel,
			String attitudelevel, String timelevel, String abilitylevel,
			String content) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("reservationid", reservationId);
		paramMap.put("starlevel", starLevel);
		paramMap.put("attitudelevel", attitudelevel);
		paramMap.put("timelevel", timelevel);
		paramMap.put("abilitylevel", abilitylevel);
		paramMap.put("commentcontent", content);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(comment, this, Config.IP
				+ "api/v1/courseinfo/usercomment", paramMap, 10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(coach)) {
				if (dataArray != null) {
					List<CoachVO> list = new ArrayList<CoachVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						list.add(coachVO);
					}
					app.favouriteCoach = list;
				}
			} else if (type.equals(school)) {
				if (dataArray != null) {
					List<SchoolVO> list = new ArrayList<SchoolVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						SchoolVO schoolVO = JSONUtil.toJavaBean(SchoolVO.class,
								dataArray.getJSONObject(i));
						list.add(schoolVO);
					}
					app.favouriteSchool = list;
				}
			} else if (type.equals(questionaddress)) {
				try {
					if (data != null) {
						QuestionVO questionVO = JSONUtil.toJavaBean(
								QuestionVO.class, data);
						app.questionVO = questionVO;
					}
				} catch (Exception e) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure(
							"题库地址数据错误");
					e.printStackTrace();
				}
			} else if (type.equals(subjectContent)) {
				try {
					if (data != null) {
						String two = data.getString("subjecttwo");
						two = two.replace("[", "").replace("]", "")
								.replace("\"", "");
						app.subjectTwoContent = Arrays.asList(two.split(","));

						String three = data.getString("subjectthree");
						three = three.replace("[", "").replace("]", "");
						app.subjectThreeContent = Arrays.asList(three
								.split(","));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (type.equals(NOT_COMMENT)) {
				// 未评论 的 预约列表
				notComment = false;
				LogUtil.print("notcomment::::>>" + jsonString);
				if (dataArray != null) {
					int length = dataArray.length();
					if (length == 0) {// 不存在 未评论
						// 获取活动
						df = new SimpleDateFormat("yyyy-MM-dd");
						String todayIsOpen = SharedPreferencesUtil.getString(
								this, TODAY_IS_OPEN_ACTIVITIES, "");
						if (!df.format(new Date()).toString()
								.equals(todayIsOpen)) {
							obtainActivities();
						}
						return true;
					}

					try {
						myAppointmentVO = JSONUtil.toJavaBean(
								MyAppointmentVO.class,
								dataArray.getJSONObject(0));
					} catch (Exception e) {
						e.printStackTrace();

					}
					if (myAppointmentVO == null) {
						return true;
					}
					// 显示强制评价
					if (currentPage == TAB_APPOINTMENT) {// 预约
						LogUtil.print("notcomment::::>>");
						rlMustComment.setVisibility(View.VISIBLE);
						MainRl.setClickable(false);
						rlMustComment.setClickable(true);
					} else {
						rlMustComment.setVisibility(View.GONE);
						MainRl.setClickable(true);
						rlMustComment.setClickable(false);
					}
					notComment = true;
					initNotCommentDialog(myAppointmentVO);

				}

			} else if (type.equals(openCity)) {
				if (dataArray != null) {
					int length = dataArray.length();
					openCityList = new ArrayList<OpenCityVO>();
					for (int i = 0; i < length; i++) {
						OpenCityVO openCityVO = null;
						try {
							openCityVO = JSONUtil.toJavaBean(OpenCityVO.class,
									dataArray.getJSONObject(i));
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (openCityVO != null) {
							openCityList.add(openCityVO);
						}
					}
					if (length > 0) {
						// 添加当前城市到listview的头部
						OpenCityVO curCityVO = new OpenCityVO();
						curCityVO.setName("当前城市");
						openCityList.add(0, curCityVO);
						// showOpenCityPopupWindow(curCityTv);
					}
				}
			} else if (type.equals(comment)) {
				if (dataString != null) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithSuccess("评论成功");
					MainRl.setClickable(true);
					rlMustComment.setVisibility(View.GONE);
				}
			} else if (type.equals(version)) {
				try {
					VersionVO versionVO = JSONUtil.toJavaBean(VersionVO.class,
							data);
					app.versionVO = versionVO;
					update(versionVO);

				} catch (Exception e) {
					ZProgressHUD.getInstance(this).dismiss();
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure(
							"版本数据解析错误");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 更新
	 */
	private void update(final VersionVO vo) {
		if (vo.innerversionCode > BaseUtils.getVersionCode(this)) {// 去更新
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("发现新版本");
			builder.setMessage(getString(R.string.app_name) + "有新版本啦！");
			builder.setPositiveButton("立即更新",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							toDownLoad(vo.getDownloadUrl());
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("以后再说",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					});
			Dialog dialog = builder.create();
			dialog.show();

		} else {// 已经是最新版
			// Toast("已经是最新版！");
		}
	}

	private void toDownLoad(String url) {
		if (url == null) {
			Toast("下载地址错误，请在应用市场更新");
			return;
		}
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		startActivity(intent);
	}

	private Toast mToast;

	private void Toast(String str) {
		if (mToast == null) {
			mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(str);
		mToast.show();
	}

	/** 未评论 */
	NoCommentDialog commentDialog = null;

	private void showOpenCityPopupWindow(View parent) {
		if (openCityPopupWindow == null) {
			LinearLayout popWindowLayout = (LinearLayout) View.inflate(this,
					R.layout.pop_window, null);
			popWindowLayout.removeAllViews();
			// LinearLayout popWindowLayout = new LinearLayout(mContext);
			popWindowLayout.setOrientation(LinearLayout.VERTICAL);
			ListView OpenCityListView = new ListView(this);
			OpenCityListView.setDividerHeight(0);
			OpenCityListView.setSelector(android.R.color.transparent);
			OpenCityListView.setCacheColorHint(android.R.color.transparent);
			OpenCityListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (position == 0) {
						selectCity = util.readParam(Config.USER_CITY);
					} else {
						OpenCityVO selectCityVO = openCityList.get(position);
						selectCity = selectCityVO.getName();
					}
					// 替换城市
					app.curCity = selectCity;
					openCityPopupWindow.dismiss();
					openCityPopupWindow = null;
				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.gravity = Gravity.CENTER;
			param.width = LinearLayout.LayoutParams.MATCH_PARENT;
			popWindowLayout.addView(OpenCityListView, param);
			OpenCityAdapter openCityAdapter = new OpenCityAdapter(this,
					openCityList);
			OpenCityListView.setAdapter(openCityAdapter);

			openCityPopupWindow = new PopupWindow(popWindowLayout,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}
		openCityPopupWindow.setFocusable(true);
		openCityPopupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		openCityPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		openCityPopupWindow.showAsDropDown(parent, 0, 20,
				Gravity.CENTER_HORIZONTAL);
	}

	private LocationClient mLocationClient;
	private List<OpenCityVO> openCityList;
	private PopupWindow openCityPopupWindow;
	private String selectCity = "";

	private void initMyLocation() {
		// 定位初始化
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(new MyLocationListener());
		// 设置定位的相关配置
		LocationClientOption option = new LocationClientOption();
		// option.disableCache(true); // 是否允许缓存
		option.setOpenGps(true); // 开启GPS
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(2000);// 设置发起定位请求的间隔时间为2000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(false);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);

		startLocation();
	}

	/**
	 * 实现定位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null)
				return;
			app.latitude = String.valueOf(location.getLatitude());
			app.longtitude = String.valueOf(location.getLongitude());
			String curCity = location.getAddress().city;
			if (curCity != null) {
				// curCity = curCity.replace("市", "");
				app.curCity = curCity;
				util.saveParam(Config.USER_CITY, curCity);
				stopLocation();
				if (mMainContainer.getCurrentTabType() == TAB_APPLY) {
					titleRightTv.setText(curCity);
				}
				LogUtil.print("城市::::>>>" + curCity);

			}
		}
	}

	private void startLocation() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		if (mLocationClient != null && !mLocationClient.isStarted()) {
			mLocationClient.start();
		}
	}

	private void stopLocation() {
		// 当不需要定位图层时关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		if (mLocationClient != null)
			mLocationClient.stop();
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		stopLocation();
		super.onPause();
	}

	private boolean isOnly = false;

	@Override
	protected void onDestroy() {
		app.isLogin = false;
		super.onDestroy();
	}

	private long firstTime;
	private TextView titleTv;
	private RadioGroup rg;
	private TextView titleRightTv;
	private ImageView titleRightIv;
	private ImageView titleLeftIv;
	// 最右边的imagview
	private ImageView titleFarRightIv;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于800毫秒，则不退出
				Toast.makeText(MainActivity.this, "再按一次退出程序...",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else {
				// System.exit(0);// 否则退出程序
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void obtainActivities() {

		// System.out.println(app.curCity);
		RequestParams params = new RequestParams();
		params.put("cityname", app.curCity);
		ApiHttpClient.get("getactivity", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int paramInt,
							Header[] paramArrayOfHeader, byte[] paramArrayOfByte) {
						String value = parseJson(paramArrayOfByte);
						if (!TextUtils.isEmpty(msg)) {
							// 加载失败，弹出失败对话框
							Toast.makeText(getBaseContext(), msg, 0).show();
						} else {
							processSuccess(value);

						}
					}

					@Override
					public void onFailure(int paramInt,
							Header[] paramArrayOfHeader,
							byte[] paramArrayOfByte, Throwable paramThrowable) {

					}
				});

	}

	protected void processSuccess(String value) {
		if (value != null) {
			LogUtil.print(value);
			try {
				List<ActivitiesVO> activitiesList = (List<ActivitiesVO>) JSONUtil
						.parseJsonToList(value,
								new TypeToken<List<ActivitiesVO>>() {
								}.getType());

				if (activitiesList != null && activitiesList.size() > 0) {

					String contenturl = activitiesList.get(0).getContenturl();

					// 打开活动界面
					if (!TextUtils.isEmpty(contenturl)) {
						Intent intent = new Intent(this,
								ActivitiesActivity.class);
						intent.putExtra("url", contenturl);
						startActivity(intent);
						SharedPreferencesUtil.putString(this,
								TODAY_IS_OPEN_ACTIVITIES, df.format(new Date())
										.toString());

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String parseJson(byte[] responseBody) {
		String value = null;
		JSONObject dataObject = null;
		JSONArray dataArray = null;
		String dataString = null;
		try {

			JSONObject jsonObject = new JSONObject(new String(responseBody));
			result = jsonObject.getString("type");
			msg = jsonObject.getString("msg");
			try {
				dataObject = jsonObject.getJSONObject("data");

			} catch (Exception e2) {
				try {
					dataArray = jsonObject.getJSONArray("data");
				} catch (Exception e3) {
					dataString = jsonObject.getString("data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (dataObject != null) {
			value = dataObject.toString();
		} else if (dataArray != null) {
			value = dataArray.toString();

		} else if (dataString != null) {
			value = dataString;
		}
		return value;
	}

	private void init() {
		mMainContainer = (MainScreenContainer) findViewById(R.id.main_screen_container);
		mMainContainer.setup(getSupportFragmentManager());
		mMainContainer.setOnTabListener(this);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_REFRESH_REDPOINT);
		intentFilter.addAction(ACTION_SHOW_TAB);
		intentFilter.addAction(ACTION_HIDE_TAB);
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mMainScreenLocalReceiver, intentFilter);

		mMainContainer.post(new Runnable() {

			@Override
			public void run() {
				// selectTabByIntent(getIntent());
			}
		});
	}

	private void selectTabByIntent(Intent intent) {
		int tab = 0;
		if (intent != null) {
			tab = intent.getIntExtra(SELECT_TAB_NAME, 0);
		}
		mMainContainer.jumpTab(tab, intent);
	}

	private int currentPage = TAB_APPLY;
	private MyAppointmentVO myAppointmentVO;
	private DrawerLayout drawerLayout;

	@Override
	public void onTabSelected(int index, boolean reClicked) {
		titleLeftIv.setVisibility(View.GONE);
		titleRightIv.setVisibility(View.GONE);
		titleRightTv.setVisibility(View.GONE);
		titleFarRightIv.setVisibility(View.GONE);
		switch (index) {
		case TAB_APPLY:
			currentPage = TAB_APPLY;
			titleLeftIv.setVisibility(View.GONE);
			titleRightIv.setVisibility(View.GONE);
			titleRightTv.setVisibility(View.VISIBLE);
			titleTv.setText("");// CommonUtil.getString(this,
								// R.string.driving_school)
			// util.saveParam(Config.USER_CITY, curCity);
			titleRightTv
					.setText(util.readParam(Config.USER_CITY) == null ? CommonUtil
							.getString(this, R.string.locationing) : util
							.readParam(Config.USER_CITY));
			rg.setVisibility(View.VISIBLE);
			/** 强制评价 */
			rlMustComment.setVisibility(View.GONE);
			break;
		case TAB_STUDY:
			currentPage = TAB_STUDY;
			titleTv.setText(CommonUtil.getString(this,
					R.string.tab_indicator_title_study));
			rg.setVisibility(View.GONE);
			rlMustComment.setVisibility(View.GONE);
			break;
		case TAB_APPOINTMENT:
			// 弹出强制评价
			// showPop();

			currentPage = TAB_APPOINTMENT;

			titleTv.setText(CommonUtil.getString(this,
					R.string.tab_indicator_title_appointment) + "列表");
			// if (app.isLogin) {
			// titleFarRightIv.setVisibility(View.VISIBLE);
			titleRightTv.setVisibility(View.VISIBLE);
			titleRightTv
					.setText(CommonUtil.getString(this, R.string.add_coach));
			rg.setVisibility(View.GONE);

			if (notComment) {
				if (currentPage == TAB_APPOINTMENT) {// 预约
					rlMustComment.setVisibility(View.VISIBLE);
				} else {
					rlMustComment.setVisibility(View.GONE);
				}
			} else {
				rlMustComment.setVisibility(View.GONE);
			}
			break;
		case TAB_MALL:
			currentPage = TAB_MALL;
			titleTv.setText(CommonUtil.getString(this,
					R.string.tab_indicator_title_mall));
			rg.setVisibility(View.GONE);
			rlMustComment.setVisibility(View.GONE);
			break;
		case TAB_COMMUNITY:
			currentPage = TAB_COMMUNITY;
			titleTv.setText(CommonUtil.getString(this,
					R.string.tab_indicator_title_community));
			rg.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	private void refreshView() {
		mMainContainer.refreshTab();
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshView();
		if (MainActivity.TARGET_TAB > 0) {// 跳转到 指定的页面， 隐藏左侧
			mMainContainer.showTab(MainActivity.TARGET_TAB);
			drawerLayout.closeDrawer(Gravity.START);
			// changeMenu();
			TARGET_TAB = -1;
		}
		// MainActivity.TARGET_TAB = 0;
		if (app.userVO != null && !app.userVO.getApplystate().equals("0")) {
			// 获取未评论列表
			obtainNotComments();
		}
	}

	private void refreshUI() {
		// 刷新当前页面
		// 根据状态选择首次进入的页面
		if (app.userVO != null) {
			if (app.userVO.getSubject().getSubjectid()
					.equals(Config.SubjectStatu.SUBJECT_ONE.getValue())
					|| app.userVO
							.getSubject()
							.getSubjectid()
							.equals(Config.SubjectStatu.SUBJECT_FOUR.getValue())) {
				// 进入学习
				mMainContainer.showTab(TAB_STUDY);
			} else if (app.userVO.getSubject().getSubjectid()
					.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())
					|| app.userVO
							.getSubject()
							.getSubjectid()
							.equals(Config.SubjectStatu.SUBJECT_THREE
									.getValue())) {
				// 进入预约
				mMainContainer.showTab(TAB_APPOINTMENT);
				LogUtil.print("app.userVO.getSubject().getSubjectid()--"
						+ app.userVO.getSubject().getSubjectid());
			} else {
				mMainContainer.showTab(TAB_APPLY);

			}
		} else {
			mMainContainer.showTab(TAB_APPLY);

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		selectTabByIntent(intent);
		// refreshCheck();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_KEY_INT_SELECTED_TAB,
				mMainContainer != null ? mMainContainer.getCurrentTabType()
						: MainActivity.TAB_APPLY);
		// outState.putBoolean(Constant.LOGIN_STATE_CONFLICT, isConflict());
	}

	public void showAddCoach() {
		titleFarRightIv.setVisibility(View.GONE);
		titleRightTv.setVisibility(View.VISIBLE);
		titleRightTv.setText(CommonUtil.getString(this, R.string.add_coach));
	}

	class NoCommentDialogClickListener implements ClickListenerInterface {

		@Override
		public void getMoreClick() {
			// // 跳转到评论页面
			// Intent intent = new Intent(MainActivity.this,
			// CommentActivity.class);
			// intent.putExtra("appointmentVO", myAppointmentVO);
			// intent.putExtra("position", getIntent().getIntExtra("position",
			// 0));
			// startActivityForResult(intent, 0);
			// commentDialog.dismiss();
		}

		@Override
		public void commintClick() {
			int rating = (int) commentDialog.getRatingBar().getRating();
			String content = commentDialog.getEditText().getText().toString();
			LogUtil.print(rating + content);

			float punctualStar = commentDialog.getPunctualStar().getRating();
			float attitudeStar = commentDialog.getAttitudeStar().getRating();
			float abilityStar = commentDialog.getAbilityStar().getRating();
			float totalStar = commentDialog.getTotalStar().getRating();
			// comment(String reservationId, String starLevel,
			// String attitudelevel, String timelevel, String abilitylevel,
			// String content) {
			if (TextUtils.isEmpty(content.trim())) {
			} else {
				if (rating == 0) {
					comment(myAppointmentVO.get_id(), totalStar + "",
							attitudeStar + "", punctualStar + "", abilityStar
									+ "", content);

				} else {
					comment(myAppointmentVO.get_id(), rating + "", attitudeStar
							+ "", punctualStar + "", abilityStar + "", content);

				}
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (mMainContainer.getCurrentFragment().equals(
				mMainContainer.enrollFragment)) {
			mMainContainer.enrollFragment.switchSchoolOrCoach();
		}
	}

	/**
	 * 未评价
	 */
	private Button moreBtn, commitBtn;

	private SelectableRoundedImageView coachPicIv;

	private RatingBar ratingBar;

	private TextView wordNumTv;

	private EditText commentEdit;

	private TextView coachNameTv;

	private TextView commentTitleTv;

	private LinearLayout commentStarsLl;

	private RatingBar totalStar;

	private RatingBar abilityStar;

	private RatingBar attitudeStar;

	private RatingBar punctualStar;

	private LinearLayout commentBtns;

	private Button moreCommitBtn;

	/**
	 * 初始化 未评论 对话框
	 * 
	 * @param myAppointmentVO2
	 */
	private void initNotCommentDialog(MyAppointmentVO myAppointmentVO2) {
		commentTitleTv = (TextView) findViewById(R.id.dialog_comment_title_tv);
		coachPicIv = (SelectableRoundedImageView) findViewById(R.id.dialog_comment_coach_im1);
		coachPicIv.setScaleType(ScaleType.CENTER_CROP);
		coachPicIv.setImageResource(R.drawable.login_head);
		coachPicIv.setOval(true);

		coachNameTv = (TextView) findViewById(R.id.dialog_comment_coach_name_tv1);
		ratingBar = (RatingBar) findViewById(R.id.dialog_comment_ratingBar);

		commentStarsLl = (LinearLayout) findViewById(R.id.dialog_comment_stars_ll);
		punctualStar = (RatingBar) findViewById(R.id.dialog_comment_punctual_star);
		attitudeStar = (RatingBar) findViewById(R.id.dialog_comment_attitude_star);
		abilityStar = (RatingBar) findViewById(R.id.dialog_comment_ability_star);
		totalStar = (RatingBar) findViewById(R.id.dialog_comment_total_star);

		wordNumTv = (TextView) findViewById(R.id.dialog_comment_words_num);
		commentEdit = (EditText) findViewById(R.id.dialog_comment_edit_et);

		commentBtns = (LinearLayout) findViewById(R.id.dialog_comment_btns);
		moreBtn = (Button) findViewById(R.id.dialog_comment_more_btn);
		commitBtn = (Button) findViewById(R.id.dialog_comment_commit_btn);

		moreCommitBtn = (Button) findViewById(R.id.dialog_comment_more_commit_btn);
		moreBtn.setOnClickListener(this);
		// commitBtn.setOnClickListener(this);
		// moreBtn.setOnClickListener(new clickListener());
		commitBtn.setOnClickListener(this);
		moreCommitBtn.setOnClickListener(this);
		commentEdit.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				wordNumTv.setText(s.length() + "/40");
				selectionStart = commentEdit.getSelectionStart();
				selectionEnd = commentEdit.getSelectionEnd();
				if (temp.length() > 40) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					commentEdit.setText(s);
					commentEdit.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
		if (myAppointmentVO2 != null) {
			LogUtil.print(coachNameTv.getId() + "-------dfsdgfsbfs"
					+ myAppointmentVO.getCoachid().getName());
			coachNameTv.setText("name:::::::>>>");
			coachNameTv.setText(myAppointmentVO.getCoachid().getName());
			LinearLayout.LayoutParams headpicParams = (LinearLayout.LayoutParams) coachPicIv
					.getLayoutParams();

			if (TextUtils.isEmpty(myAppointmentVO.getCoachid()
					.getHeadportrait().getOriginalpic())) {
				coachPicIv.setBackgroundResource(R.drawable.login_head);
			} else {
				BitmapManager.INSTANCE.loadBitmap2(myAppointmentVO.getCoachid()
						.getHeadportrait().getOriginalpic(), coachPicIv,
						headpicParams.width, headpicParams.height);
			}
			LogUtil.print("dsgr-------dfsdgfsbfs"
					+ myAppointmentVO.getCoachid().getName());
		}

		// 设置更多评价的rating事件
		punctualStar.setOnRatingBarChangeListener(this);
		attitudeStar.setOnRatingBarChangeListener(this);
		abilityStar.setOnRatingBarChangeListener(this);

	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// 获取总评分
		float total = punctualStar.getRating() + attitudeStar.getRating()
				+ abilityStar.getRating();

		totalStar.setRating(total / 3);

	}

}
