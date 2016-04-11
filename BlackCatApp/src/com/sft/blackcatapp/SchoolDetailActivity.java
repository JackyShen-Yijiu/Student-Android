package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.InfinitePagerAdapter;
import cn.sft.infinitescrollviewpager.MyHandler;
import cn.sft.infinitescrollviewpager.PageChangeListener;
import cn.sft.infinitescrollviewpager.PageClickListener;

import com.jzjf.app.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.sft.adapter.SchoolDetailCoachHoriListAdapter;
import com.sft.adapter.SchoolDetailCourseFeeAdapter;
import com.sft.adapter.SchoolDetailCourseFeeAdapter.MyClickListener;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.dialog.NoLoginDialog;
import com.sft.fragment.MenuFragment;
import com.sft.util.BaseUtils;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.viewutil.MyScrollView;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.HeadLineNewsVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.SuccessVO;

/**
 * 驾校详情界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class SchoolDetailActivity extends BaseActivity implements
		BitMapURLExcepteionListner, PageChangeListener,
		OnCheckedChangeListener, OnSelectConfirmListener,
		MyScrollView.scrollStateListener {

	// private static final String school_route = "school_route";
	private static final String schoolType = "school";
	private static final String classInfo = "classInfo";
	private static final String coach = "coach";
	private static final String addSchool = "addSchool";
	private static final String deleteSchool = "deleteSchool";
	private static final String headLineNews = "headLineNews";

	/**
	 * 广告栏的地址
	 */
	private String[] adImageUrl;
	/**
	 * 包含小圆点的layout
	 */
	private LinearLayout dotLayout;
	/**
	 * 小圆点的集合
	 */
	private ImageView[] imageViews;
	/**
	 * 广告栏
	 */
	private ViewPager viewPager;
	/**
	 * 广告内容
	 */
	private List<HeadLineNewsVO> adList;

	private int viewPagerHeight;
	private RelativeLayout adLayout;
	// 驾校信息
	private TextView schoolInfoTv;
	// 驾校简介
	// private TextView schoolInstructionTv;
	// // 驾校教练列表
	// private LoadMoreView horizontalListView;
	//
	private SchoolDetailCoachHoriListAdapter adapter;
	private SchoolDetailCourseFeeAdapter courseFeeAdapter;
	// 报名按钮
	// private Button enrollBtn;
	// 用户查看的驾校
	private SchoolVO school;
	// 驾校名称
	private TextView schoolNameTv;
	// 驾校价格
	private TextView schoolPriceTv;
	// 驾校地址
	private TextView schoolAddressTv;
	// 通过率
	private TextView schoolRateTv;
	// 营业时间
	private TextView workTimeTv;
	// 驾校简介
	private TextView schoolInTv;
	// 驾校简介更多
	// private TextView schoolInMoreTv;
	// 添加删除教练
	private CheckBox addDeleteSchoolCk;
	//
	// private TextView noCoahTv;

	//
	private int coachPage = 1;
	//
	private List<CoachVO> coachList;
	/*** 2.0 */
	/** 标题 */
	TextView titleTV;

	private View viewTop;
	private View viewTopStatic;
	private View titleLayout;
	private int hegiht;
	/** 暂无训练场 照片 */
	// private TextView tvNoPic;
	private View viewStatus;

	private SuccessVO offlineVO;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			// getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			// getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			// 底部 导航
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_detail2);
		// addView(R.layout.

		hegiht = BaseUtils.getScreenHeight(this);
		initView();
		setListener();
		obtainEnrollSchoolDetail();

	}

	private void obtainSchoolCoach(int coachPage) {
		HttpSendUtils.httpGetSend(coach, this, Config.IP
				+ "api/v1/getschoolcoach/" + school.getSchoolid() + "/"
				+ coachPage);
	}

	private void obtainHeadLineNews() {
		HttpSendUtils.httpGetSend(headLineNews, this, Config.IP
				+ "api/v1/info/headlinenews");
	}

	/**
	 * 线下报名的 状态
	 */
	private void obtainOffLineApplySuccessInfo() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("applySchoolInfor", this, Config.IP
				+ "api/v1/userinfo/getapplyschoolinfo", paramMap, 10000,
				headerMap);
	}

	private String enrollState;
	private TextView schoolInMoreTv;

	@Override
	protected void onResume() {
		register(getClass().getName());
		// LogUtil.print("state——--》"+app.userVO);
		// app.userVO.
		if (app.userVO == null
				|| app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			enrollState = EnrollResult.SUBJECT_NONE.getValue();
		} else if (app.userVO.getApplystate().equals(
				EnrollResult.SUBJECT_ENROLLING.getValue())) {
			enrollState = EnrollResult.SUBJECT_ENROLLING.getValue();
		} else {
			enrollState = EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue();
		}
		if (app.isLogin)
			obtainOffLineApplySuccessInfo();
		super.onResume();
	};

	private void initTitle() {

		viewStatus = findViewById(R.id.act_school_detail_status);

		findViewById(R.id.base_titlebar_layout).setBackgroundResource(
				android.R.color.transparent);

		ImageButton bus = (ImageButton) findViewById(R.id.base_right_btn2);

		ImageButton left = (ImageButton) findViewById(R.id.base_left_btn);
		ImageButton phone = (ImageButton) findViewById(R.id.base_right_btn);
		titleTV = (TextView) findViewById(R.id.base_title_tv);
		// 中文字体加粗
		titleTV.getPaint().setFakeBoldText(true);
		titleTV.setText("");
		titleLayout = findViewById(R.id.base_titlebar_layout_bg);
		bus.setImageResource(R.drawable.bus_white_icon);
		left.setBackgroundResource(R.drawable.base_left_btn_bkground);
		phone.setImageResource(R.drawable.phone_white_icon);
		bus.setVisibility(View.VISIBLE);

		phone.setOnClickListener(this);
		left.setOnClickListener(this);
		bus.setOnClickListener(this);
		titleLayout.setBackgroundResource(android.R.color.transparent);
	}

	private void initView() {
		// showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN
		// | BaseActivity.SHOW_RIGHT_BTN);
		// setBtnBkground(R.drawable.base_left_btn_bkground, R.drawable.phone);
		// setTitleText(R.string.school_detail);
		initTitle();
		alphaIn = AnimationUtils.loadAnimation(SchoolDetailActivity.this,
				R.anim.alpha_in);
		alphaOut = AnimationUtils.loadAnimation(SchoolDetailActivity.this,
				R.anim.alpha_out);
		scaleSmall = AnimationUtils.loadAnimation(SchoolDetailActivity.this,
				R.anim.scale_small);
		scaleBig = AnimationUtils.loadAnimation(SchoolDetailActivity.this,
				R.anim.scale_big);

		sv_container = (MyScrollView) findViewById(R.id.school_detail_scrollview);
		sv_container.setOnStateListener(this);
		adLayout = (RelativeLayout) findViewById(R.id.school_detail_headpic_im);
		viewPager = (ViewPager) findViewById(R.id.school_detail_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.school_detail_dotlayout);
		headpicLayout = findViewById(R.id.school_detail_headpic_layout_bg);
		headpicLayout.setBackgroundResource(R.drawable.bg_top);
		// tvNoPic = (TextView) findViewById(R.id.school_detail_nopic_tv);
		// coachlistView = (ListView) findViewById(R.id.school_coach_listview);
		courselistView = (ListView) findViewById(R.id.course_fee_listview);
		courselistView.setFocusable(false);
		// coachlistView.setFocusable(false);
		// horizontalListView = (LoadMoreView)
		// findViewById(R.id.select_coach_horizon_listview);
		// horizontalListView.setPullLoadMoreEnable(true);
		// horizontalListView.setVertical();

		// listview添加底部控件
		// View view = View.inflate(this, R.layout.look_school_all_coach, null);
		// coachlistView.addFooterView(view);
		schoolInfoTv = (TextView) findViewById(R.id.school_detail_schoolinfo_tv);
		// schoolInstructionTv = (TextView)
		// findViewById(R.id.school_detail_schoolinstruction_tv);
		// enrollBtn = (Button) findViewById(R.id.coach_detail_enroll_btn);
		// 顶部红色背景
		viewTop = findViewById(R.id.school_detail_top_ll);
		viewTopStatic = findViewById(R.id.school_detail_top_static);

		schoolNameTv = (TextView) findViewById(R.id.school_detail_name_tv);
		schoolRatingRar = (RatingBar) findViewById(R.id.school_detail_ratingrar);
		schoolPriceTv = (TextView) findViewById(R.id.school_detail_price_tv);
		schoolAddressTv = (TextView) findViewById(R.id.school_detail_place_tv);
		schoolRateTv = (TextView) findViewById(R.id.school_detail_rate_tv);
		workTimeTv = (TextView) findViewById(R.id.school_detail_time_tv);
		schoolInTv = (TextView) findViewById(R.id.coach_detail_introduction_tv);
		schoolInMoreTv = (TextView) findViewById(R.id.school_detail_more_tv);

		addDeleteSchoolCk = (CheckBox) findViewById(R.id.school_detail_collection_ck);

		coachInfoRl = (RelativeLayout) findViewById(R.id.school_detail_coach_info_rl);
		// radioGroup = (RadioGroup)
		// findViewById(R.id.school_detail_radiogroup);
		// coachInfoRb = (RadioButton)
		// findViewById(R.id.school_detail_coach_info_rb);
		// courseFeeRb = (RadioButton)
		// findViewById(R.id.school_detail_course_fee_rb);

		// coachInfoRbTop = (RadioButton)
		// findViewById(R.id.school_detail_coach_info_rb_top);
		// courseFeeRbTop = (RadioButton)
		// findViewById(R.id.school_detail_course_fee_rb_top);
		//
		// radioGroupTop = (RadioGroup)
		// findViewById(R.id.school_detail_radiogroup_top);
		// busMore = (TextView) findViewById(R.id.school_detail_bus_more_tv);

		// noCoahTv.setVisibility(View.GONE);
		// coachlistView.setVisibility(View.GONE);
		courselistView.setVisibility(View.VISIBLE);
		schoolInfoTv.getPaint().setFakeBoldText(true);
		// schoolInstructionTv.getPaint().setFakeBoldText(true);

		school = (SchoolVO) getIntent().getSerializableExtra("school");

		// noCoahTv.setMinHeight("");

		LogUtil.print("schoolId--id--00>" + school.getId());

		// if (app.userVO == null) {
		// enrollBtn.setVisibility(View.GONE);
		// addDeleteSchoolCk.setEnabled(false);
		// } else {
		// enrollBtn.setVisibility(View.VISIBLE);
		// }

		if (app.favouriteSchool != null) {
			if (app.favouriteSchool.contains(school)) {
				addDeleteSchoolCk.setChecked(true);
			} else {
				addDeleteSchoolCk.setChecked(false);
			}
		}

		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) adLayout
				.getLayoutParams();
		headParams.width = screenWidth;
		headParams.height = (int) (screenWidth * 2 / 3f);
		viewPagerHeight = headParams.height;
		setViewPager(adImageUrl);
	}

	private void setListener() {
		schoolAddressTv.setOnClickListener(this);
		// enrollBtn.setOnClickListener(this);
		// horizontalListView.setLoadMoreListener(this);
		addDeleteSchoolCk.setOnCheckedChangeListener(this);
		schoolInTv.setOnClickListener(this);
		coachInfoRl.setOnClickListener(this);
		schoolInMoreTv.setOnClickListener(this);
		// coachlistView.setOnItemClickListener(this);
		// radioGroup.setOnCheckedChangeListener(this);
		// radioGroupTop.setOnCheckedChangeListener(this);
		// busMore.setOnClickListener(this);
	}

	private void setData() {
		if (school != null) {
			if (adImageUrl == null) {
				adImageUrl = new String[1];
			}
			LogUtil.print("adImageUrl[0]"
					+ school.getLogoimg().getOriginalpic());
			adImageUrl[0] = school.getLogoimg().getOriginalpic();
			setViewPager(adImageUrl);
			schoolNameTv.setText(school.getName());
			LogUtil.print(school.getSchoollevel() + "=====");
			if (!TextUtils.isEmpty(school.getSchoollevel())) {
				schoolRatingRar.setRating(Float.parseFloat(school
						.getSchoollevel()));
			}
			schoolPriceTv.setText(school.getPrice());
			schoolAddressTv.setText(school.getAddress());
			schoolRateTv.setText(school.getPassingrate() + "%");

			if (TextUtils.isEmpty(workTimeTv.getText())) {
				workTimeTv.setText("暂无时间");
			} else {
				workTimeTv.setText(school.getHours());
			}
			schoolInTv.setText(school.getIntroduction());
			showSchoolIntro();

			// // 动态添加训练场地的图片
			// String[] trainPicStrings = school.getPictures();
			// if (trainPicStrings.length == 0) {
			// tvNoPic.setVisibility(View.VISIBLE);
			// trainGroundLayout.setVisibility(View.GONE);
			// } else {
			// tvNoPic.setVisibility(View.GONE);
			// trainGroundLayout.setVisibility(View.VISIBLE);
			// }
			// for (int i = 0; i < trainPicStrings.length; i++) {
			// ImageView imageView = new ImageView(this);
			// LayoutParams params = new LayoutParams(dp2px(90), dp2px(60));
			// imageView.setScaleType(ScaleType.CENTER_CROP);
			// params.leftMargin = dp2px(15);
			// BitmapManager.INSTANCE.loadBitmap2(trainPicStrings[i],
			// imageView, dp2px(90), dp2px(60));
			// trainGroundLayout.addView(imageView, params);
			// }

			// 班车路线
			// addBusRoutes();
		}
	}

	// private void addBusRoutes() {
	// if (busRoute.getChildCount() > 0) {
	// return;
	// }
	// List<TagsList> list = new ArrayList<TagsList>();
	// for (SchoolBusRoute route : school.getSchoolbusroute()) {
	// TagsList label = new TagsList();
	// label.setTagname(route.getRoutename());
	// list.add(label);
	// }
	// busRoute.setData(list);
	// busRoute.removeAllViews();
	// for (int i = 0; i < list.size(); i++) {
	// TextView textview = new TextView(this);
	// textview.setTextColor(Color.parseColor("#333333"));
	// textview.setText(list.get(i).getTagname());
	// busRoute.addView(textview);
	// }
	// }

	private int dp2px(int dp) {
		return (int) (this.getResources().getDisplayMetrics().density * dp + 0.5);
	}

	int minHeight;// 2行时候的高度
	int maxHeight;// schoolInTv总的高度
	private boolean isExtend = false;// 是否展开
	private boolean isRunAnim = false;
	private MyScrollView sv_container;
	// private ListView coachlistView;
	private ListView courselistView;
	// private LinearLayout trainGroundLayout;
	// private WordWrapView busRoute;
	// private RadioGroup radioGroup;
	// private RadioGroup radioGroupTop;
	// private RadioButton courseFeeRb;
	// private RadioButton coachInfoRb;
	// private RadioButton courseFeeRbTop, coachInfoRbTop;
	private TextView busMore;
	private List<CoachVO> twoCoach;
	private List<ClassVO> courseList;

	// 设置驾校简介
	private void showSchoolIntro() {
		schoolInTv.setMaxLines(2);
		schoolInTv.setLines(2);
		schoolInTv.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						schoolInTv.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						minHeight = schoolInTv.getMeasuredHeight();// 得到5行时候的高度

						// 2.让tv_des先显示全部的文本，再获取总高度
						schoolInTv.setMaxLines(Integer.MAX_VALUE);// 显示全部文本
						schoolInTv.getViewTreeObserver()
								.addOnGlobalLayoutListener(
										new OnGlobalLayoutListener() {
											@Override
											public void onGlobalLayout() {
												schoolInTv
														.getViewTreeObserver()
														.removeGlobalOnLayoutListener(
																this);
												// 获取总高度
												maxHeight = schoolInTv
														.getHeight();

												schoolInTv.getLayoutParams().height = minHeight;
												schoolInTv.requestLayout();
											}
										});

						// schoolInTv.setLines(2);
					}
				});
	}

	private void obtainEnrollClass() {
		HttpSendUtils.httpGetSend(classInfo, this, Config.IP
				+ "api/v1/driveschool/schoolclasstype/" + school.getSchoolid());
	}

	private void obtainEnrollSchoolDetail() {
		HttpSendUtils.httpGetSend(schoolType, this, Config.IP
				+ "api/v1/driveschool/getschoolinfo/" + school.getSchoolid());
	}

	private void setViewPager(String[] adImageUrl) {
		InfinitePagerAdapter adapter = null;
		int length = 0;
		if (adImageUrl != null && adImageUrl.length > 0) {
			adapter = new InfinitePagerAdapter(this, adImageUrl, screenWidth,
					viewPagerHeight);
			length = adImageUrl.length;
		} else {
			adapter = new InfinitePagerAdapter(this,
					new int[] { R.drawable.defaultimage });
			length = 1;
		}
		adapter.setPageClickListener(new MyPageClickListener());
		adapter.setURLErrorListener(this);

		viewPager.setAdapter(adapter);

		imageViews = new ImageView[length];
		ImageView imageView = null;
		dotLayout.removeAllViews();
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				(int) (8 * screenDensity), (int) (4 * screenDensity));
		dotLayout.addView(new TextView(this), textParams);
		// 添加小圆点的图片
		for (int i = 0; i < length; i++) {
			imageView = new ImageView(this);
			// 设置小圆点imageview的参数
			imageView.setLayoutParams(new LayoutParams(
					(int) (16 * screenDensity), (int) (4 * screenDensity)));// 创建一个宽高均为20
			// 的布局
			// 将小圆点layout添加到数组中
			imageViews[i] = imageView;

			// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
			if (i == 0) {
				imageViews[i].setBackgroundColor(Color.parseColor("#21b8c6"));
			} else {
				imageViews[i].setBackgroundColor(Color.parseColor("#eeeeee"));
			}
			// 将imageviews添加到小圆点视图组
			dotLayout.addView(imageViews[i]);
			dotLayout.addView(new TextView(this), textParams);
		}
	}

	private class MyPageClickListener implements PageClickListener {

		@Override
		public void onPageClick(int position) {
			// try {
			// if (adList != null && adList.size() > position) {
			// String url = adList.get(position).getHeadportrait()
			// .getOriginalpic();
			// if (!TextUtils.isEmpty(url)) {
			// Intent intent = new Intent();
			// intent.setAction("android.intent.action.VIEW");
			// Uri content_url = Uri.parse(url);
			// intent.setData(content_url);
			// startActivity(intent);
			// }
			// }
			// } catch (Exception e) {
			// }
		}
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (coach.equals(type))
			// horizontalListView.setLoadMoreCompleted();
			super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (coach.equals(type))
			// horizontalListView.setLoadMoreCompleted();
			super.doTimeOut(type);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(schoolType)) {
				if (data != null) {
					school = JSONUtil.toJavaBean(SchoolVO.class, data);
					// obtainHeadLineNews();
					setData();
					obtainEnrollClass();

				}
			}
			// else if (type.equals(coach)) {
			// if (dataArray != null) {
			// int length = dataArray.length();
			// if (length > 0) {
			// coachPage++;
			// if (coachList == null)
			// coachList = new ArrayList<CoachVO>();
			// twoCoach = new ArrayList<CoachVO>();
			// for (int i = 0; i < length; i++) {
			// CoachVO coachVO = JSONUtil.toJavaBean(
			// CoachVO.class, dataArray.getJSONObject(i));
			// coachList.add(coachVO);
			// if (i < 2) {
			// twoCoach.add(coachVO);
			// }
			// }
			// adapter = null;
			// adapter = new SchoolDetailCoachHoriListAdapter(this,
			// twoCoach);
			// adapter.setData(twoCoach);
			// coachlistView.setAdapter(adapter);
			//
			// }
			// // horizontalListView.setLoadMoreCompleted();
			// }
			// }
			else if (type.equals(headLineNews)) {
				if (dataArray != null) {
					int length = dataArray.length();
					adList = new ArrayList<HeadLineNewsVO>();
					if (length > 0) {
						adImageUrl = new String[length];
					}
					for (int i = 0; i < length; i++) {
						HeadLineNewsVO headLineNewsVO = JSONUtil.toJavaBean(
								HeadLineNewsVO.class,
								dataArray.getJSONObject(i));
						adList.add(headLineNewsVO);
						adImageUrl[i] = headLineNewsVO.getHeadportrait()
								.getOriginalpic();
					}
					if (length > 0) {
						setViewPager(adImageUrl);
					}
				}
				obtainSchoolCoach(coachPage);

			} else if (type.equals(addSchool)) {
				if (!app.favouriteSchool.contains(school)) {
					app.favouriteSchool.add(school);
					sendBroadcast(new Intent(MyFavouriteActiviy.class.getName())
							.putExtra("isRefresh", true).putExtra(
									"activityName",
									FavouriteSchoolActivity.class.getName()));
				}
			} else if (type.equals(deleteSchool)) {
				if (app.favouriteSchool.contains(school)) {
					app.favouriteSchool.remove(school);
					sendBroadcast(new Intent(MyFavouriteActiviy.class.getName())
							.putExtra("isRefresh", true).putExtra(
									"activityName",
									FavouriteSchoolActivity.class.getName()));
				}
			} else if (type.equals(classInfo)) {
				if (dataArray != null) {
					int length = dataArray.length();
					courseList = new ArrayList<ClassVO>();
					for (int i = 0; i < length; i++) {
						ClassVO classVO = JSONUtil.toJavaBean(ClassVO.class,
								dataArray.getJSONObject(i));
						courseList.add(classVO);
					}
					LogUtil.print("----0" + courseList.size());
					courseFeeAdapter = null;
					courseFeeAdapter = new SchoolDetailCourseFeeAdapter(
							courseList, SchoolDetailActivity.this, mListener,
							enrollState);
					courseFeeAdapter.setName(school.getName());
					// courseFeeAdapter.setData(twoCoach);
					courselistView.setAdapter(courseFeeAdapter);
					// setListViewHeightBasedOnChildren(coachlistView);
					// sv_container.smoothScrollTo(0, 0);
				}

			} else if (type.equals("applySchoolInfor")) {// 存在未完成订单
				LogUtil.print("applySchoolinfor-->" + jsonString);
				if (data != null) {
					offlineVO = JSONUtil.toJavaBean(SuccessVO.class, data);
					app.userVO.setPayState(offlineVO.paytypestatus);
					// offlineVO.applystate
					// setOffLine(offlineVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.school_detail_coach_info_rl:
			// 进入教练列表

			if (app.isLogin) {
				intent = new Intent(this, AppointmentMoreCoachActivity.class);
				intent.putExtra("isOnClickToDetail", true);
				startActivity(intent);
			} else {
				BaseUtils.toLogin(this);
			}
			break;
		case R.id.base_left_btn:
			setResult(v.getId(), getIntent());
			finish();
			break;
		case R.id.base_right_btn:
			try {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + school.getPhone()));
				startActivity(phoneIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.base_right_btn2:// bus 路线
			Intent i = new Intent(SchoolDetailActivity.this,
					SchoolBusRouteActivity.class);
			i.putExtra("school_route", school);
			i.putExtra(MenuFragment.schoolId, school.getId());
			startActivity(i);

			break;
		// case R.id.coach_detail_enroll_btn:
		// if (app.userVO.getApplystate().equals(
		// EnrollResult.SUBJECT_NONE.getValue())) {
		// String checkResult = Util.isConfilctEnroll(school);
		// LogUtil.print("toApply" + checkResult);
		// if (checkResult == null) {
		// intent = new Intent();
		// intent.putExtra("school", school);
		// intent.putExtra("activityName",
		// SubjectEnrollActivity.class.getName());
		// setResult(RESULT_OK, intent);
		// finish();
		// } else if (checkResult.length() == 0) {
		// app.selectEnrollSchool = school;
		// Util.updateEnrollSchool(this, school, false);
		// intent = new Intent();
		// intent.putExtra("school", school);
		// intent.putExtra("activityName",
		// SubjectEnrollActivity.class.getName());
		// setResult(RESULT_OK, intent);
		// finish();
		// } else {
		// // 提示
		// EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
		// this, checkResult);
		// dialog.show();
		// }
		// } else if (app.userVO.getApplystate().equals(
		// EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {
		// intent = new Intent(this, AppointmentCarActivity.class);
		// startActivity(intent);
		// } else if (app.userVO.getApplystate().equals(
		// EnrollResult.SUBJECT_ENROLLING.getValue())) {
		// ZProgressHUD.getInstance(this).show();
		// ZProgressHUD.getInstance(this)
		// .dismissWithFailure("正在报名中，请等待审核");
		// }
		// break;
		case R.id.school_detail_place_tv:
			intent = new Intent(this, MapActivity.class);
			intent.putExtra("longtitude", school.getLongitude());
			intent.putExtra("latitude", school.getLatitude());
			intent.putExtra("title", school.getName());
			startActivity(intent);
			break;

		case R.id.coach_detail_introduction_tv:
		case R.id.school_detail_more_tv:
			if (isRunAnim) {
				return;
			}
			ValueAnimator animator;
			if (isExtend) {
				// 执行收缩动画
				animator = ValueAnimator.ofInt(maxHeight, minHeight);
			} else {
				// 执行扩展动画
				animator = ValueAnimator.ofInt(minHeight, maxHeight);
			}

			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					int animatedValue = (Integer) animator.getAnimatedValue();
					schoolInTv.getLayoutParams().height = animatedValue;
					schoolInTv.requestLayout();

					// sv_container.scrollBy(0, maxHeight - minHeight);
				}
			});
			animator.addListener(new AppDesAnimListener());
			animator.setDuration(350);
			animator.start();

			// 标记值取反
			isExtend = !isExtend;
			schoolInMoreTv.setText(isExtend ? "收起" : "展开");
			break;

		// case R.id.school_detail_bus_more_tv:
		// intent = new Intent(this, SchoolBusRouteActivity.class);
		// intent.putExtra(school_route, school);
		// startActivity(intent);
		// break;
		}
	}

	class AppDesAnimListener implements AnimatorListener {
		@Override
		public void onAnimationCancel(Animator arg0) {
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			isRunAnim = false;
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
		}

		@Override
		public void onAnimationStart(Animator arg0) {
			isRunAnim = true;
		}

	}

	@Override
	public void onPageChanged(int position) {
		if (imageViews != null) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position].setBackgroundColor(Color
						.parseColor("#21b8c6"));
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (position != i) {
					imageViews[i].setBackgroundColor(Color
							.parseColor("#eeeeee"));
				}
			}
		}
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				return;
			}
			new MyHandler(1) {
				@Override
				public void run() {
					LogUtil.print("ScholllDetail----result>");
					data.putExtra("school", school);
					setResult(RESULT_OK, data);
					finish();
				}
			};
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (!app.isLogin) {
			NoLoginDialog dialog = new NoLoginDialog(SchoolDetailActivity.this);
			dialog.show();
			addDeleteSchoolCk.setChecked(false);
			return;
		}
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());

		String url = Config.IP + "api/v1/userinfo/favoriteschool/"
				+ school.getSchoolid();
		if (isChecked) {
			HttpSendUtils.httpPutSend(addSchool, this, url, null, 10000,
					headerMap);
		} else {
			HttpSendUtils.httpDeleteSend(deleteSchool, this, url, null, 10000,
					headerMap);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(R.id.base_left_btn, getIntent());
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void selectConfirm(boolean isConfirm, boolean isFreshAll) {
		if (isConfirm) {
			app.selectEnrollSchool = school;
			Util.updateEnrollSchool(this, school, isFreshAll);
			if (isFreshAll) {
				app.selectEnrollCoach = Util.getEnrollUserSelectedCoach(this);
				app.selectEnrollCarStyle = Util
						.getEnrollUserSelectedCarStyle(this);
				app.selectEnrollClass = Util.getEnrollUserSelectedClass(this);
			}
			Intent intent = new Intent();
			intent.putExtra("school", school);

			LogUtil.print("change--scholll-->" + school.getName());
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long
	// arg3) {
	// if (arg2 == adapter.getCount()) {
	// // 查看该校全部教练
	// Intent intent = new Intent(this, SchoolAllCoachActivity.class);
	// intent.putExtra("school", school);
	// startActivity(intent);
	//
	// }
	// else {
	//
	// Intent intent = new Intent(this, CoachDetailActivity.class);
	// CoachVO coachVO = adapter.getItem(arg2);
	// intent.putExtra("coach", coachVO);
	// // intent.putExtra("schoolId", school.getId());
	// // startActivityForResult(intent, listView.getId());
	// intent.putExtra("schoolId", school.getId());
	// startActivityForResult(intent, coachlistView.getId());
	// }
	// }

	// @Override
	// public void onLoadMore() {
	// obtainSchoolCoach(coachPage);
	// }

	// @Override
	// public void onCheckedChanged(RadioGroup group, int checkedId) {
	// if (checkedId == coachInfoRb.getId()
	// || checkedId == coachInfoRbTop.getId()) {
	// coachInfoRb.setChecked(true);
	// coachInfoRbTop.setChecked(true);
	// // 教练详情
	// if (coachList != null && coachList.size() > 0) {
	// noCoahTv.setVisibility(View.GONE);
	// coachlistView.setVisibility(View.VISIBLE);
	// } else {
	// noCoahTv.setVisibility(View.VISIBLE);
	// coachlistView.setVisibility(View.GONE);
	// }
	//
	// LogUtil.print("noCoach--->" + coachlistView.getHeight());
	// courselistView.setVisibility(View.GONE);
	// } else if (checkedId == courseFeeRb.getId()
	// || checkedId == courseFeeRbTop.getId()) {
	// courseFeeRb.setChecked(true);
	// courseFeeRbTop.setChecked(true);
	// // 课程费用
	// courselistView.setVisibility(View.VISIBLE);
	// noCoahTv.setVisibility(View.GONE);
	// coachlistView.setVisibility(View.GONE);
	// }
	// }

	/**
	 * 实现类，响应按钮点击事件
	 */
	private MyClickListener mListener = new MyClickListener() {
		@Override
		public void myOnClick(int position, View v) {
			boolean isFromSearchCoach = getIntent().getBooleanExtra(
					SearchCoachActivity.from_searchCoach_enroll, false);
			Intent intent = null;
			if (!app.isLogin) {
				BaseUtils.toLogin(SchoolDetailActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(
				// SchoolDetailActivity.this);
				// dialog.show();
				return;
			}
			if (app.userVO.getPayState() == 0 || app.userVO.getPayState() == 30) {// 未支付，，支付失败
				toPay(position);
			} else {// 支付成功
				if (app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {
					intent = new Intent(SchoolDetailActivity.this,
							AppointmentCarActivity.class);
					startActivity(intent);
				} else if (app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_ENROLLING.getValue())) {
					ZProgressHUD.getInstance(SchoolDetailActivity.this).show();
					ZProgressHUD.getInstance(SchoolDetailActivity.this)
							.dismissWithFailure("正在报名中，请等待审核");
				}
			}

			// if (app.userVO.getApplystate().equals(
			// EnrollResult.SUBJECT_NONE.getValue())) {
			// toPay(position);
			// } else if (app.userVO.getApplystate().equals(
			// EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {
			// intent = new Intent(SchoolDetailActivity.this,
			// AppointmentCarActivity.class);
			// startActivity(intent);
			// } else if (app.userVO.getApplystate().equals(
			// EnrollResult.SUBJECT_ENROLLING.getValue())) {
			// ZProgressHUD.getInstance(SchoolDetailActivity.this).show();
			// ZProgressHUD.getInstance(SchoolDetailActivity.this)
			// .dismissWithFailure("正在报名中，请等待审核");
			// }

		}

	};

	private void toPay(int po) {
		ClassVO classe = courseFeeAdapter.getItem(po);
		LogUtil.print("initdata-->" + app.userVO);
		Intent i = new Intent(SchoolDetailActivity.this, ApplyAct.class);
		i.putExtra("school", school);
		i.putExtra("schoolId", school.getSchoolid());
		i.putExtra("class", classe);
		i.putExtra("from", 0);
		LogUtil.print("schoolId->" + school.getSchoolid() + "id::"
				+ classe.getSchoolinfo().getId());
		i.putExtra(SearchCoachActivity.from_searchCoach_enroll, true);
		// startActivity(i);
		startActivityForResult(i, 9);

	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public void onScrollChanged(int t, int oldt) {
		int[] location = new int[2];
		schoolNameTv.getLocationOnScreen(location);
		int y = location[1];

		int[] location2 = new int[2];
		// radioGroup.getLocationOnScreen(location2);
		int y1 = location2[1];
		// LogUtil.print(viewTop.getVisibility()+"ScrollView---onScrollChangedt::>"+(viewTopStatic.getY()+viewTopStatic.getHeight())+"Name::>>>"+y);

		if (topStatic == 0) {// 初始化
			topStatic = viewTopStatic.getY() + viewTopStatic.getHeight();
			// topTab = radioGroupTop.getY();
		} else if (topStatic < y) {// 刚进入 状态

			if (viewTop.getVisibility() != View.GONE) {
				viewTop.setVisibility(View.GONE);
				titleLayout.setBackgroundResource(android.R.color.transparent);
				addDeleteSchoolCk.setVisibility(View.VISIBLE);
				schoolNameTv.setVisibility(View.VISIBLE);
				titleTV.setText("");
				viewStatus.setVisibility(View.GONE);

				titleLayout.startAnimation(alphaOut);
				viewTop.startAnimation(alphaOut);
				viewStatus.startAnimation(alphaOut);

				addDeleteSchoolCk.startAnimation(scaleBig);
				LogUtil.print(topTab + "yyyyyyy--透明");
				//
			}
			// noCoahTv.setHeight(hegiht - y1);

		} else if (topStatic > y || topStatic == y) {// 已经滑动很多，

			if (viewTop.getVisibility() != View.VISIBLE) {
				viewTop.setVisibility(View.VISIBLE);
				titleLayout.setBackgroundResource(R.color.new_app_main_color);
				addDeleteSchoolCk.setVisibility(View.INVISIBLE);
				schoolNameTv.setVisibility(View.INVISIBLE);
				titleTV.setText(school.getName());
				// viewStatus.setVisibility(View.VISIBLE);

				viewStatus.startAnimation(alphaIn);
				viewTop.startAnimation(alphaIn);
				titleLayout.startAnimation(alphaIn);
				addDeleteSchoolCk.startAnimation(scaleSmall);
				// 设置 noCoach 的高度
			}
			// if (topTab < y1) {// 还没有到 最上面
			// // if (noCoahTv.getVisibility() == View.VISIBLE) {
			// // int height = (hegiht - y1) > noCoahTv.getHeight() ? noCoahTv
			// // .getHeight() : hegiht - y1;
			// // noCoahTv.setHeight(height);
			// // LogUtil.print(height + "yyyyyyy222>>" + y1
			// // + "TextViewHeight::::>>" + noCoahTv.getHeight());
			// //
			// // } else {
			// // noCoahTv.setHeight(hegiht - y1);
			// // }
			//
			// } else {// 已经在最上面了
			// noCoahTv.setHeight((int) (hegiht - topStatic - 180));
			// }
			// LogUtil.print(topTab + "yyyyyyy" + "TextViewHeight::::>>"
			// + noCoahTv.getHeight());
			// 滑动到 课程费用/教练信息
			// if (topTab > y1) {// 显示固定的，
			// if (radioGroupTop.getVisibility() != View.VISIBLE)
			// radioGroupTop.setVisibility(View.VISIBLE);
			// } else {// 隐藏固定的
			// if (radioGroupTop.getVisibility() != View.INVISIBLE)
			// radioGroupTop.setVisibility(View.INVISIBLE);
			// }

		}

		// 处理 没有教练或者没有课程的信息

	}

	private float topStatic = 0;
	private float topTab = 0;

	Animation alphaIn;
	Animation alphaOut;
	Animation scaleBig;
	Animation scaleSmall;
	private RatingBar schoolRatingRar;
	private RelativeLayout coachInfoRl;
	private View headpicLayout;
}
