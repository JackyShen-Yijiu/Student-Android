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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.NoLoginDialog;
import com.sft.fragment.MenuFragment;
import com.sft.fragment.MenuFragment.SLMenuListOnItemClickListener;
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
		setContentView(R.layout.frame_content);
		initView();
		initData(savedInstanceState);
		setListener();

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

		// set the Behind View
		setBehindContentView(R.layout.frame_left_menu);
		home_btn = (ImageView) findViewById(R.id.home_btn);
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();

		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeEnabled(false);
		mSlidingMenu.setBehindScrollScale(0.25f);
		mSlidingMenu.setFadeDegree(0.25f);
		mSlidingMenu.setBackgroundResource(R.drawable.bg);
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
		activityManager = new LocalActivityManager(this, true);
		activityManager.dispatchCreate(savedInstanceState);

		// 加入2个子Activity
		Intent i1 = new Intent(this, SubjectEnrollActivity.class);
		Intent i2 = new Intent(this, SubjectOneActivity.class);
		Intent i3 = new Intent(this, SubjectTwoActivity.class);
		Intent i4 = new Intent(this, SubjectThreeActivity.class);
		Intent i5 = new Intent(this, SubjectFourActivity.class);

		List<View> listViews = new ArrayList<View>(); // 实例化listViews
		listViews.add(activityManager
				.startActivity("SubjectEnrollActivity", i1).getDecorView());
		listViews.add(activityManager.startActivity("SubjectOneActivity", i2)
				.getDecorView());
		listViews.add(activityManager.startActivity("SubjectTwoActivity", i3)
				.getDecorView());
		listViews.add(activityManager.startActivity("SubjectThreeActivity", i4)
				.getDecorView());
		listViews.add(activityManager.startActivity("SubjectFourActivity", i5)
				.getDecorView());

		viewPager.setAdapter(new MyPageAdapter(listViews));

		app.curCity = util.readParam(Config.USER_CITY);
		initMyLocation();
	}

	private void setListener() {
		home_btn.setOnClickListener(this);
	}

	// 左侧菜单条目点击
	@Override
	public void selectItem(int position, String title) {
		Intent intent;
		switch (position) {
		case 0:
			if (app.isLogin) {
				intent = new Intent(this, PersonCenterActivity.class);
				getParent().startActivityForResult(intent, position);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case 1:
			Toast.makeText(getBaseContext(), "查找教练", 0).show();
			// intent = new Intent(this, SecondActivity.class);
			// startActivity(intent);
			break;
		case 2:
			// intent = new Intent(this, SecondActivity.class);
			// startActivity(intent);
			break;
		case 3:
			// intent = new Intent(this, SecondActivity.class);
			// startActivity(intent);
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

	private class MyPageAdapter extends PagerAdapter {

		private List<View> list;

		private MyPageAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object arg2) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	private LocationClient mLocationClient;

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
		super.onPause();
		stopLocation();
	}
}
