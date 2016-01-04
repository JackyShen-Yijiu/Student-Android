package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sft.adapter.HomePageAdapter;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.NoLoginDialog;
import com.sft.fragment.IntroducesFragment;
import com.sft.fragment.MenuFragment;
import com.sft.fragment.MenuFragment.SLMenuListOnItemClickListener;
import com.sft.fragment.SubjectFourFragment;
import com.sft.fragment.SubjectOneFragment;
import com.sft.fragment.SubjectThreeFragment;
import com.sft.fragment.SubjectTwoFragment;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachVO;
import com.sft.vo.QuestionVO;
import com.sft.vo.SchoolVO;

public class MainActivity extends BaseMainActivity implements
		SLMenuListOnItemClickListener, OnClickListener {

	//
	private static final String subjectContent = "subjectContent";
	private static final String coach = "coach";
	private static final String school = "school";
	private static final String questionaddress = "questionaddress";
	//
	private MapView mapView;
	//
	private BaiduMap mBaiduMap;

	private LocalActivityManager activityManager = null;

	private ImageView home_btn;
	private SlidingMenu mSlidingMenu;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** Item宽度 */
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;
	private HomePageAdapter mHomePageAdapter;
	private ViewPager viewPager;

	@SuppressLint("NewApi")
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
		initView();
		initData(savedInstanceState);
		setListener();
		// 气球按钮
		// initBalloon();
		obtainFavouriteSchool();
		obtainFavouriteCoach();
		obtainQuestionAddress();
		obtainSubjectContent();
		setTag();
		if (app != null && app.isLogin) {
			util.print("userid=" + app.userVO.getUserid());
		}

	}

	private int sum = 0;
	private ImageView bottomProgress;

	private void setTag() {
		if (app.isLogin) {
			JPushInterface.setAlias(this, app.userVO.getUserid(),
					new MyTagAliasCallback());
		}
	}

	private class MyTagAliasCallback implements TagAliasCallback {

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			sum++;
			if (arg0 != 0 && sum < 5) {
				setTag();
			}
		}

	}

	private void initView() {

		mapView = (MapView) findViewById(R.id.main_bmapView);
		mBaiduMap = mapView.getMap();
		viewPager = (ViewPager) findViewById(R.id.main_content_vp);
		carImageView = (ImageView) findViewById(R.id.main_car_iv);

		bottomProgress = (ImageView) findViewById(R.id.main_bottom_progress_iv);
		introduce = (TextView) findViewById(R.id.main_yibu_introduce_tv);
		subjectOne = (TextView) findViewById(R.id.main_subject_one_tv);
		subjectTwo = (TextView) findViewById(R.id.main_subject_two_tv);
		subjectThree = (TextView) findViewById(R.id.main_subject_three_tv);
		subjectFour = (TextView) findViewById(R.id.main_subject_four_tv);

		// set the Behind View
		setBehindContentView(R.layout.frame_left_menu);
		home_btn = (ImageView) findViewById(R.id.home_btn);
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();

		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeEnabled(false);
		mSlidingMenu.setBehindScrollScale(0.25f);
		mSlidingMenu.setFadeDegree(0.25f);
		mSlidingMenu.setBackgroundResource(R.drawable.left_bg);
		mSlidingMenu
				.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
					@Override
					public void transformCanvas(Canvas canvas, float percentOpen) {
						float scale = (float) (percentOpen * 0.25 + 0.75);
						canvas.scale(scale, scale, -canvas.getWidth() / 2,
								canvas.getHeight() / 2);
					}
				});

		mSlidingMenu
				.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
					@Override
					public void transformCanvas(Canvas canvas, float percentOpen) {
						float scale = (float) (1 - percentOpen * 0.25);
						canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
					}
				});

		// 设置 SlidingMenu 内容
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.left_menu, new MenuFragment());
		fragmentTransaction.commit();
	}

	public void changeMenu() {
		mSlidingMenu.toggle();
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
		/*
		 * 在一个Activity的一部分中显示其他Activity”要用到LocalActivityManagerity
		 * 作用体现在manager获取View：manager.startActivity(String,
		 * Intent).getDecorView()
		 */
		// activityManager = new LocalActivityManager(this, true);
		// activityManager.dispatchCreate(savedInstanceState);
		//
		// // 加入2个子Activity
		// Intent i1 = new Intent(this, SubjectEnrollActivity.class);
		// Intent i2 = new Intent(this, SubjectOneActivity.class);
		// Intent i3 = new Intent(this, SubjectTwoActivity.class);
		// Intent i4 = new Intent(this, SubjectThreeActivity.class);
		// Intent i5 = new Intent(this, SubjectFourActivity.class);
		//
		// List<View> listViews = new ArrayList<View>(); // 实例化listViews
		// listViews.add(activityManager
		// .startActivity("SubjectEnrollActivity", i1).getDecorView());
		// listViews.add(activityManager.startActivity("SubjectOneActivity", i2)
		// .getDecorView());
		// listViews.add(activityManager.startActivity("SubjectTwoActivity", i3)
		// .getDecorView());
		// listViews.add(activityManager.startActivity("SubjectThreeActivity",
		// i4)
		// .getDecorView());
		// listViews.add(activityManager.startActivity("SubjectFourActivity",
		// i5)
		// .getDecorView());
		//
		// viewPager.setAdapter(new MyPageAdapter(listViews));

		mHomePageAdapter = new HomePageAdapter(
				this.getSupportFragmentManager(), getBaseContext());
		mHomePageAdapter.addFragmentClass(IntroducesFragment.class);
		mHomePageAdapter.addFragmentClass(SubjectOneFragment.class);
		mHomePageAdapter.addFragmentClass(SubjectTwoFragment.class);
		mHomePageAdapter.addFragmentClass(SubjectThreeFragment.class);
		mHomePageAdapter.addFragmentClass(SubjectFourFragment.class);
		viewPager.setAdapter(mHomePageAdapter);
		viewPager.setOffscreenPageLimit(4);
		app.curCity = util.readParam(Config.USER_CITY);
		initMyLocation();
	}

	private void setListener() {
		home_btn.setOnClickListener(this);
		viewPager.setOnPageChangeListener(new MainOnPageChangeListener());
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
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case 3:
			if (app.isLogin) {
				intent = new Intent(this, MyWalletActivity.class);
				startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case 4:
			if (app.isLogin) {
				intent = new Intent(this, PersonCenterActivity.class);
				startActivityForResult(intent, position);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// case CHANNELREQUEST:
		// if (resultCode == CHANNELRESULT) {
		// setChangelView();
		// }
		// break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_btn:
			toggle(); // 动态判断自动关闭或开启SlidingMenu
			break;
		default:
			break;
		}
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private LocationClient mLocationClient;
	private ImageView carImageView;
	private TextView introduce;
	private TextView subjectOne;
	private TextView subjectTwo;
	private TextView subjectThree;
	private TextView subjectFour;

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
				curCity = curCity.replace("市", "");
				app.curCity = curCity;
				util.saveParam(Config.USER_CITY, curCity);
				stopLocation();
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

	class MainOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// if (!isOnly) {
			//
			// if (position == 1 && positionOffset > 0) {
			// String enrollState = app.userVO.getApplystate();
			// if (!EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
			// enrollState)) {
			// Intent intent = new Intent(getBaseContext(),
			// ApplyActivity.class);
			// startActivity(intent);
			// isOnly = true;
			// }
			//
			// }
			// }
			int carPointX = (int) ((positionOffset + position) * CommonUtil
					.dip2px(getBaseContext(), 65));
			android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) carImageView
					.getLayoutParams();
			layoutParams.leftMargin = carPointX;
			carImageView.setLayoutParams(layoutParams);
		}

		@Override
		public void onPageSelected(int position) {
			setBottomState(position);

			if (position == 2) {

				if (app.isLogin) {
					String enrollState = BlackCatApplication.app.userVO
							.getApplystate();
					if (EnrollResult.SUBJECT_NONE.getValue()
							.equals(enrollState)) {
						Intent intent = new Intent(getBaseContext(),
								ApplyActivity.class);
						startActivity(intent);
						viewPager.setCurrentItem(position - 1);
					}
				} else {
					Intent intent = new Intent(getBaseContext(),
							LoginActivity.class);
					startActivity(intent);
					finish();
					viewPager.setCurrentItem(position - 1);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private void setBottomState(int postion) {
		introduce.setTextColor(getResources().getColor(
				R.color.bottom_text_unselector));
		introduce.setTextSize(16);
		subjectOne.setTextColor(getResources().getColor(
				R.color.bottom_text_unselector));
		subjectOne.setTextSize(16);
		subjectTwo.setTextColor(getResources().getColor(
				R.color.bottom_text_unselector));
		subjectTwo.setTextSize(16);
		subjectThree.setTextColor(getResources().getColor(
				R.color.bottom_text_unselector));
		subjectThree.setTextSize(16);
		subjectFour.setTextColor(getResources().getColor(
				R.color.bottom_text_unselector));
		subjectFour.setTextSize(16);
		// bottomProgress.setBackgroundDrawable(null);

		switch (postion) {
		case 0:
			introduce.setTextColor(getResources().getColor(
					R.color.bottom_text_selector));
			introduce.setTextSize(18);
			bottomProgress.setImageResource(R.drawable.loding_one);
			break;
		case 1:
			subjectOne.setTextColor(getResources().getColor(
					R.color.bottom_text_selector));
			subjectOne.setTextSize(18);
			bottomProgress.setImageResource(R.drawable.loding_two);
			break;
		case 2:
			subjectTwo.setTextColor(getResources().getColor(
					R.color.bottom_text_selector));
			subjectTwo.setTextSize(18);
			bottomProgress.setImageResource(R.drawable.loding_three);
			break;
		case 3:
			subjectThree.setTextColor(getResources().getColor(
					R.color.bottom_text_selector));
			subjectThree.setTextSize(18);
			bottomProgress.setImageResource(R.drawable.loding_four);
			break;
		case 4:
			subjectFour.setTextColor(getResources().getColor(
					R.color.bottom_text_selector));
			subjectFour.setTextSize(18);
			bottomProgress.setImageResource(R.drawable.loding_five);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		app.isLogin = false;
		super.onDestroy();
	}

	private long firstTime;

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

	// @Override
	// public boolean onKeyUp(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// long secondTime = System.currentTimeMillis();
	// if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于800毫秒，则不退出
	// Toast.makeText(MainActivity.this, "再按一次退出程序...",
	// Toast.LENGTH_SHORT).show();
	// firstTime = secondTime;// 更新firstTime
	// return true;
	// } else {
	// System.exit(0);// 否则退出程序
	// }
	// }
	// return super.onKeyUp(keyCode, event);
	// }

}
