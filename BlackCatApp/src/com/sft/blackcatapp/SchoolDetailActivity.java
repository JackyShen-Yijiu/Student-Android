package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.InfinitePagerAdapter;
import cn.sft.infinitescrollviewpager.InfiniteViewPager;
import cn.sft.infinitescrollviewpager.MyHandler;
import cn.sft.infinitescrollviewpager.PageChangeListener;
import cn.sft.infinitescrollviewpager.PageClickListener;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.sft.adapter.SchoolDetailCoachHoriListAdapter;
import com.sft.adapter.SchoolDetailCourseFeeAdapter;
import com.sft.adapter.SchoolDetailCourseFeeAdapter.MyClickListener;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.view.WordWrapView;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.HeadLineNewsVO;
import com.sft.vo.SchoolBusRoute;
import com.sft.vo.SchoolVO;
import com.sft.vo.TagsList;

/**
 * 驾校详情界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class SchoolDetailActivity extends BaseActivity implements
		BitMapURLExcepteionListner, PageChangeListener,
		OnCheckedChangeListener, OnSelectConfirmListener, OnItemClickListener,
		android.widget.RadioGroup.OnCheckedChangeListener {

	private static final String school_route = "school_route";
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
	private InfiniteViewPager viewPager;
	/**
	 * 广告内容
	 */
	private List<HeadLineNewsVO> adList;

	private int viewPagerHeight;
	private RelativeLayout adLayout;
	// 驾校信息
	private TextView schoolInfoTv;
	// 驾校简介
	private TextView schoolInstructionTv;
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
	private TextView schoolInMoreTv;
	// 添加删除教练
	private CheckBox addDeleteSchoolCk;
	//
	private TextView noCoahTv;

	//
	private int coachPage = 1;
	//
	private List<CoachVO> coachList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_school_detail);
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

	private String enrollBtnName;

	@Override
	protected void onResume() {
		register(getClass().getName());
		if (app.userVO == null
				|| app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			enrollBtnName = getResources().getString(R.string.enroll);
		} else if (app.userVO.getApplystate().equals(
				EnrollResult.SUBJECT_ENROLLING.getValue())) {
			enrollBtnName = getResources().getString(R.string.verifying);
		} else {
			enrollBtnName = getResources().getString(R.string.appointment);
		}
		super.onResume();
	};

	private void initView() {
		showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN
				| BaseActivity.SHOW_RIGHT_BTN);
		setBtnBkground(R.drawable.base_left_btn_bkground, R.drawable.phone);
		setTitleText(R.string.school_detail);

		sv_container = (ScrollView) findViewById(R.id.school_detail_scrollview);
		adLayout = (RelativeLayout) findViewById(R.id.school_detail_headpic_im);
		viewPager = (InfiniteViewPager) findViewById(R.id.school_detail_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.school_detail_dotlayout);

		coachlistView = (ListView) findViewById(R.id.school_coach_listview);
		courselistView = (ListView) findViewById(R.id.course_fee_listview);
		// horizontalListView = (LoadMoreView)
		// findViewById(R.id.select_coach_horizon_listview);
		// horizontalListView.setPullLoadMoreEnable(true);
		// horizontalListView.setVertical();

		// listview添加底部控件
		View view = View.inflate(this, R.layout.look_school_all_coach, null);
		coachlistView.addFooterView(view);
		schoolInfoTv = (TextView) findViewById(R.id.school_detail_schoolinfo_tv);
		schoolInstructionTv = (TextView) findViewById(R.id.school_detail_schoolinstruction_tv);
		// enrollBtn = (Button) findViewById(R.id.coach_detail_enroll_btn);
		schoolNameTv = (TextView) findViewById(R.id.school_detail_name_tv);
		schoolPriceTv = (TextView) findViewById(R.id.school_detail_price_tv);
		schoolAddressTv = (TextView) findViewById(R.id.school_detail_place_tv);
		schoolRateTv = (TextView) findViewById(R.id.coach_detail_rate_tv);
		workTimeTv = (TextView) findViewById(R.id.school_detail_weektime_tv);
		schoolInTv = (TextView) findViewById(R.id.coach_detail_introduction_tv);
		schoolInMoreTv = (TextView) findViewById(R.id.school_detail_more_tv);

		addDeleteSchoolCk = (CheckBox) findViewById(R.id.school_detail_collection_ck);
		noCoahTv = (TextView) findViewById(R.id.select_coach_horizon_no_tv);
		trainGroundLayout = (LinearLayout) findViewById(R.id.school_detail_train_pic_ll);
		busRoute = (WordWrapView) findViewById(R.id.coach_detail_busroute_intro);
		busRoute.showColor(false);

		radioGroup = (RadioGroup) findViewById(R.id.school_detail_radiogroup);
		coachInfoRb = (RadioButton) findViewById(R.id.school_detail_coach_info_rb);
		courseFeeRb = (RadioButton) findViewById(R.id.school_detail_course_fee_rb);
		busMore = (TextView) findViewById(R.id.school_detail_bus_more_tv);

		noCoahTv.setVisibility(View.GONE);
		coachlistView.setVisibility(View.GONE);
		courselistView.setVisibility(View.VISIBLE);
		schoolInfoTv.getPaint().setFakeBoldText(true);
		schoolInstructionTv.getPaint().setFakeBoldText(true);

		school = (SchoolVO) getIntent().getSerializableExtra("school");

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
		setViewPager();
	}

	private void setListener() {
		schoolAddressTv.setOnClickListener(this);
		// enrollBtn.setOnClickListener(this);
		viewPager.setPageChangeListener(this);
		// horizontalListView.setLoadMoreListener(this);
		addDeleteSchoolCk.setOnCheckedChangeListener(this);
		schoolInMoreTv.setOnClickListener(this);
		coachlistView.setOnItemClickListener(this);
		radioGroup.setOnCheckedChangeListener(this);
		busMore.setOnClickListener(this);
	}

	private void setData() {
		if (school != null) {
			adImageUrl = school.getPictures();
			setViewPager();
			schoolNameTv.setText(school.getName());
			schoolPriceTv.setText(school.getPrice());
			schoolAddressTv.setText(school.getAddress());
			schoolRateTv.setText(school.getPassingrate() + "%");
			workTimeTv.setText(school.getHours());
			schoolInTv.setText(school.getIntroduction());
			showSchoolIntro();

			// 动态添加训练场地的图片
			String[] trainPicStrings = school.getPictures();
			for (int i = 0; i < trainPicStrings.length; i++) {
				ImageView imageView = new ImageView(this);
				LayoutParams params = new LayoutParams(dp2px(90), dp2px(60));
				imageView.setScaleType(ScaleType.CENTER_CROP);
				params.leftMargin = dp2px(15);
				BitmapManager.INSTANCE.loadBitmap2(trainPicStrings[i],
						imageView, dp2px(90), dp2px(60));
				trainGroundLayout.addView(imageView, params);
			}

			// 班车路线
			addBusRoutes();
		}
	}

	private void addBusRoutes() {
		if (busRoute.getChildCount() > 0) {
			return;
		}
		List<TagsList> list = new ArrayList<TagsList>();
		for (SchoolBusRoute route : school.getSchoolbusroute()) {
			TagsList label = new TagsList();
			label.setTagname(route.getRoutename());
			list.add(label);
		}
		busRoute.setData(list);
		busRoute.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			TextView textview = new TextView(this);
			textview.setTextColor(Color.parseColor("#333333"));
			textview.setText(list.get(i).getTagname());
			busRoute.addView(textview);
		}
	}

	private int dp2px(int dp) {
		return (int) (this.getResources().getDisplayMetrics().density * dp + 0.5);
	}

	int minHeight;// 2行时候的高度
	int maxHeight;// schoolInTv总的高度
	private boolean isExtend = false;// 是否展开
	private boolean isRunAnim = false;
	private ScrollView sv_container;
	private ListView coachlistView;
	private ListView courselistView;
	private LinearLayout trainGroundLayout;
	private WordWrapView busRoute;
	private RadioGroup radioGroup;
	private RadioButton courseFeeRb;
	private RadioButton coachInfoRb;
	private TextView busMore;
	private List<CoachVO> twoCoach;
	private List<ClassVO> courseList;

	// 设置驾校简介
	private void showSchoolIntro() {
		schoolInTv.setMaxLines(2);
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

												LogUtil.print("minHeight-"
														+ minHeight
														+ "+maxHeight"
														+ maxHeight);
												schoolInTv.getLayoutParams().height = minHeight;
												schoolInTv.requestLayout();
											}
										});
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

	private void setViewPager() {
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
					obtainHeadLineNews();
					setData();
					obtainEnrollClass();
				}
			} else if (type.equals(coach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0) {
						coachPage++;
						if (coachList == null)
							coachList = new ArrayList<CoachVO>();
						twoCoach = new ArrayList<CoachVO>();
						for (int i = 0; i < length; i++) {
							CoachVO coachVO = JSONUtil.toJavaBean(
									CoachVO.class, dataArray.getJSONObject(i));
							coachList.add(coachVO);
							if (i < 2) {
								twoCoach.add(coachVO);
							}
						}
						adapter = null;
						adapter = new SchoolDetailCoachHoriListAdapter(this,
								twoCoach);
						adapter.setData(twoCoach);
						coachlistView.setAdapter(adapter);

					}
					// horizontalListView.setLoadMoreCompleted();
				}
			} else if (type.equals(headLineNews)) {
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
						setViewPager();
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
							enrollBtnName);
					// courseFeeAdapter.setData(twoCoach);
					courselistView.setAdapter(courseFeeAdapter);
					setListViewHeightBasedOnChildren(coachlistView);
					sv_container.smoothScrollTo(0, 0);
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

					sv_container.scrollBy(0, maxHeight - minHeight);
				}
			});
			animator.addListener(new AppDesAnimListener());
			animator.setDuration(350);
			animator.start();

			// 标记值取反
			isExtend = !isExtend;
			schoolInMoreTv.setText(isExtend ? "收起" : "更多");
			break;

		case R.id.school_detail_bus_more_tv:
			intent = new Intent(this, SchoolBusRouteActivity.class);
			intent.putExtra(school_route, school);
			startActivity(intent);
			break;
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
			new MyHandler(200) {
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
			intent.putExtra("activityName",
					SubjectEnrollActivity.class.getName());

			LogUtil.print("change--scholll-->" + school.getName());
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == adapter.getCount()) {
			// 查看该校全部教练
			Intent intent = new Intent(this, SchoolAllCoachActivity.class);
			startActivity(intent);

		} else {

			Intent intent = new Intent(this, CoachDetailActivity.class);
			CoachVO coachVO = adapter.getItem(arg2);
			intent.putExtra("coach", coachVO);
<<<<<<< HEAD
			intent.putExtra("schoolId", school.getId());
			startActivityForResult(intent, listView.getId());
=======
			startActivityForResult(intent, coachlistView.getId());
>>>>>>> c608d388823ca95f39dbc4b93c4c9ed8eca20816
		}
	}

	// @Override
	// public void onLoadMore() {
	// obtainSchoolCoach(coachPage);
	// }

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == coachInfoRb.getId()) {
			// 教练详情
			if (coachList != null && coachList.size() > 0) {
				noCoahTv.setVisibility(View.GONE);
				coachlistView.setVisibility(View.VISIBLE);
			} else {
				noCoahTv.setVisibility(View.VISIBLE);
				coachlistView.setVisibility(View.GONE);
			}
			courselistView.setVisibility(View.GONE);
		} else if (checkedId == courseFeeRb.getId()) {
			// 课程费用
			courselistView.setVisibility(View.VISIBLE);
			noCoahTv.setVisibility(View.GONE);
			coachlistView.setVisibility(View.GONE);
		}
	}

	/**
	 * 实现类，响应按钮点击事件
	 */
	private MyClickListener mListener = new MyClickListener() {
		@Override
		public void myOnClick(int position, View v) {
			boolean isFromSearchCoach = getIntent().getBooleanExtra(
					SearchCoachActivity.from_searchCoach_enroll, false);
			Intent intent = null;
			if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_NONE.getValue())) {
				String checkResult = Util.isConfilctEnroll(school);
				LogUtil.print("toApply" + checkResult);
				if (checkResult == null) {
					intent = new Intent();
					intent.putExtra("school", school);
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					setResult(RESULT_OK, intent);
					finish();
				} else if (checkResult.length() == 0) {
					app.selectEnrollSchool = school;
					Util.updateEnrollSchool(SchoolDetailActivity.this, school,
							false);
					intent = new Intent();
					intent.putExtra("school", school);
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					// 提示
					EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
							SchoolDetailActivity.this, checkResult);
					dialog.show();
				}
			} else if (app.userVO.getApplystate().equals(
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
	};

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
}
