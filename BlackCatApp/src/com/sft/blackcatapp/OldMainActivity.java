package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.common.Config.SubjectStatu;
import com.sft.listener.OnTabActivityResultListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachVO;
import com.sft.vo.HeadLineNewsVO;
import com.sft.vo.QuestionVO;
import com.sft.vo.SchoolVO;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.InfinitePagerAdapter;
import cn.sft.infinitescrollviewpager.InfiniteViewPager;
import cn.sft.infinitescrollviewpager.PageChangeListener;
import cn.sft.infinitescrollviewpager.PageClickListener;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("deprecation")
public class OldMainActivity extends BaseActivity implements PageChangeListener, BitMapURLExcepteionListner {
	//
	private static final String subjectContent = "subjectContent";
	private static final String headlineNews = "headlineNews";
	private static final String coach = "coach";
	private static final String school = "school";
	private static final String questionaddress = "questionaddress";
	//
	private MapView mapView;
	//
	private BaiduMap mBaiduMap;
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
	private InfiniteViewPager topViewPager;
	/**
	 * 广告内容
	 */
	private List<HeadLineNewsVO> adList;
	/**
	 * 广告默认图片
	 */
	private ImageView defaultImage;

	private int viewPagerHeight;
	private RelativeLayout adLayout;
	//
	private ViewPager viewPager;
	//
	private LocalActivityManager activityManager = null;
	//
	private RadioGroup radioGroup;

	private RadioButton leftBlank1Btn, leftBlank2Btn, rightBlank1Btn, rightBlank2Btn;
	private RadioButton enrollBtn, course1Btn, course2Btn, course3Btn, course4Btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_main);
		initView();
		initData(savedInstanceState);
		setListener();
		obtainHeadLineNews();

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
			JPushInterface.setAlias(this, app.userVO.getUserid(), new MyTagAliasCallback());
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

	private void obtainSubjectContent() {
		HttpSendUtils.httpGetSend(subjectContent, this, Config.IP + "api/v1/trainingcontent");
	}

	private void obtainQuestionAddress() {
		HttpSendUtils.httpGetSend(questionaddress, this, Config.IP + "api/v1/info/examquestion");
	}

	private void obtainFavouriteSchool() {
		if (app.isLogin) {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(school, this, Config.IP + "api/v1/userinfo/favoriteschool", null, 10000,
					headerMap);
		}
	}

	private void obtainFavouriteCoach() {
		if (app.isLogin) {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(coach, this, Config.IP + "api/v1/userinfo/favoritecoach", null, 10000, headerMap);
		}
	}

	private void obtainHeadLineNews() {
		HttpSendUtils.httpGetSend(headlineNews, this, Config.IP + "api/v1/info/headlinenews");
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleBarVisible(View.GONE);
		showTitlebarBtn(0);

		mapView = (MapView) findViewById(R.id.main_bmapView);
		mBaiduMap = mapView.getMap();
		viewPager = (ViewPager) findViewById(R.id.main_viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.main_radiobtn);
		adLayout = (RelativeLayout) findViewById(R.id.main_top_headpic_im);
		topViewPager = (InfiniteViewPager) findViewById(R.id.main_top_viewpager);
		defaultImage = (ImageView) findViewById(R.id.main_top_defaultimage);
		dotLayout = (LinearLayout) findViewById(R.id.main_top_dotlayout);

		leftBlank1Btn = (RadioButton) findViewById(R.id.main_leftblank1_btn);
		leftBlank2Btn = (RadioButton) findViewById(R.id.main_leftblank2_btn);
		rightBlank1Btn = (RadioButton) findViewById(R.id.main_rightblank1_btn);
		rightBlank2Btn = (RadioButton) findViewById(R.id.main_rightblank2_btn);
		enrollBtn = (RadioButton) findViewById(R.id.main_enroll_btn);
		course1Btn = (RadioButton) findViewById(R.id.main_course1_btn);
		course2Btn = (RadioButton) findViewById(R.id.main_course2_btn);
		course3Btn = (RadioButton) findViewById(R.id.main_course3_btn);
		course4Btn = (RadioButton) findViewById(R.id.main_course4_btn);

		leftBlank1Btn.setEnabled(false);
		leftBlank2Btn.setEnabled(false);
		rightBlank1Btn.setEnabled(false);
		rightBlank2Btn.setEnabled(false);

		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) adLayout.getLayoutParams();
		headParams.width = screenWidth;
		int height = (int) ((screenWidth - 16 * screenDensity) / 3 + (screenWidth - 12 * screenDensity) * 2 / 3
				+ statusbarHeight);
		height += (63 * screenDensity);

		headParams.height = screenHeight - height;
		viewPagerHeight = headParams.height;
		setViewPager();
	}

	private void setListener() {
		topViewPager.setPageChangeListener(this);
		radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());

		if (app.isLogin) {
			if (app.userVO.getApplystate().equals(EnrollResult.SUBJECT_NONE.getValue())) {
				radioGroup.check(R.id.main_enroll_btn);
			} else {
				String subjectId = app.userVO.getSubject().getSubjectid();
				if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
					radioGroup.check(R.id.main_course2_btn);
				} else if (subjectId.equals(SubjectStatu.SUBJECT_ONE.getValue())) {
					radioGroup.check(R.id.main_course1_btn);
				} else if (subjectId.equals(SubjectStatu.SUBJECT_THREE.getValue())) {
					radioGroup.check(R.id.main_course3_btn);
				} else if (subjectId.equals(SubjectStatu.SUBJECT_FOUR.getValue())) {
					radioGroup.check(R.id.main_course4_btn);
				} else {
					radioGroup.check(R.id.main_course1_btn);
				}
			}
		} else {
			radioGroup.check(R.id.main_enroll_btn);
		}
	}

	private void initData(Bundle savedInstanceState) {

		// 如果用户没有报名，读取用户报名填写过的信息
		if (app.userVO != null && app.userVO.getApplystate().equals(EnrollResult.SUBJECT_NONE.getValue())) {
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
		listViews.add(activityManager.startActivity("SubjectEnrollActivity", i1).getDecorView());
		listViews.add(activityManager.startActivity("SubjectOneActivity", i2).getDecorView());
		listViews.add(activityManager.startActivity("SubjectTwoActivity", i3).getDecorView());
		listViews.add(activityManager.startActivity("SubjectThreeActivity", i4).getDecorView());
		listViews.add(activityManager.startActivity("SubjectFourActivity", i5).getDecorView());

		viewPager.setAdapter(new MyPageAdapter(listViews));

		app.curCity = util.readParam(Config.USER_CITY);
		initMyLocation();
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(headlineNews)) {
				if (dataArray != null) {
					int length = dataArray.length();
					adList = new ArrayList<HeadLineNewsVO>();
					if (length > 0) {
						adImageUrl = new String[length];
					}
					for (int i = 0; i < length; i++) {
						HeadLineNewsVO headLineNewsVO = (HeadLineNewsVO) JSONUtil.toJavaBean(HeadLineNewsVO.class,
								dataArray.getJSONObject(i));
						adList.add(headLineNewsVO);
						adImageUrl[i] = headLineNewsVO.getHeadportrait().getOriginalpic();
					}
					if (length > 0) {
						setViewPager();
					}
				}
			} else if (type.equals(coach)) {
				if (dataArray != null) {
					List<CoachVO> list = new ArrayList<CoachVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = (CoachVO) JSONUtil.toJavaBean(CoachVO.class, dataArray.getJSONObject(i));
						list.add(coachVO);
					}
					app.favouriteCoach = list;
				}
			} else if (type.equals(school)) {
				if (dataArray != null) {
					List<SchoolVO> list = new ArrayList<SchoolVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						SchoolVO schoolVO = (SchoolVO) JSONUtil.toJavaBean(SchoolVO.class, dataArray.getJSONObject(i));
						list.add(schoolVO);
					}
					app.favouriteSchool = list;
				}
			} else if (type.equals(questionaddress)) {
				try {
					if (data != null) {
						QuestionVO questionVO = (QuestionVO) JSONUtil.toJavaBean(QuestionVO.class, data);
						app.questionVO = questionVO;
					}
				} catch (Exception e) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure("题库地址数据错误");
					e.printStackTrace();
				}
			} else if (type.equals(subjectContent)) {
				try {
					if (data != null) {
						String two = data.getString("subjecttwo");
						two = two.replace("[", "").replace("]", "").replace("\"", "");
						app.subjectTwoContent = Arrays.asList(two.split(","));

						String three = data.getString("subjectthree");
						three = three.replace("[", "").replace("]", "");
						app.subjectThreeContent = Arrays.asList(three.split(","));
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

	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			radioGroup.setOnCheckedChangeListener(null);
			switch (position) {
			case 0:
				radioGroup.check(R.id.main_enroll_btn);
				break;
			case 1:
				radioGroup.check(R.id.main_course1_btn);
				break;
			case 2:
				radioGroup.check(R.id.main_course2_btn);
				break;
			case 3:
				radioGroup.check(R.id.main_course3_btn);
				break;
			case 4:
				radioGroup.check(R.id.main_course4_btn);
				break;
			}
			setRadioButtonCenter(position);
			radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void setRadioButtonCenter(int index) {
		leftBlank1Btn.setVisibility(View.GONE);
		leftBlank2Btn.setVisibility(View.GONE);
		enrollBtn.setVisibility(View.GONE);
		course1Btn.setVisibility(View.GONE);
		course2Btn.setVisibility(View.GONE);
		course3Btn.setVisibility(View.GONE);
		course4Btn.setVisibility(View.GONE);
		rightBlank1Btn.setVisibility(View.GONE);
		rightBlank2Btn.setVisibility(View.GONE);
		switch (index) {
		case 0:
			leftBlank1Btn.setVisibility(View.VISIBLE);
			leftBlank2Btn.setVisibility(View.VISIBLE);
			enrollBtn.setVisibility(View.VISIBLE);
			course1Btn.setVisibility(View.VISIBLE);
			course2Btn.setVisibility(View.VISIBLE);
			break;
		case 1:
			leftBlank2Btn.setVisibility(View.VISIBLE);
			enrollBtn.setVisibility(View.VISIBLE);
			course1Btn.setVisibility(View.VISIBLE);
			course2Btn.setVisibility(View.VISIBLE);
			course3Btn.setVisibility(View.VISIBLE);
			break;
		case 2:
			enrollBtn.setVisibility(View.VISIBLE);
			course1Btn.setVisibility(View.VISIBLE);
			course2Btn.setVisibility(View.VISIBLE);
			course3Btn.setVisibility(View.VISIBLE);
			course4Btn.setVisibility(View.VISIBLE);
			break;
		case 3:
			course1Btn.setVisibility(View.VISIBLE);
			course2Btn.setVisibility(View.VISIBLE);
			course3Btn.setVisibility(View.VISIBLE);
			course4Btn.setVisibility(View.VISIBLE);
			rightBlank1Btn.setVisibility(View.VISIBLE);
			break;
		case 4:
			course2Btn.setVisibility(View.VISIBLE);
			course3Btn.setVisibility(View.VISIBLE);
			course4Btn.setVisibility(View.VISIBLE);
			rightBlank1Btn.setVisibility(View.VISIBLE);
			rightBlank2Btn.setVisibility(View.VISIBLE);
			break;
		}
	}

	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			viewPager.setOnPageChangeListener(null);
			switch (checkedId) {
			case R.id.main_enroll_btn:
				viewPager.setCurrentItem(0);
				setRadioButtonCenter(0);
				break;
			case R.id.main_course1_btn:
				viewPager.setCurrentItem(1);
				setRadioButtonCenter(1);
				break;
			case R.id.main_course2_btn:
				viewPager.setCurrentItem(2);
				setRadioButtonCenter(2);
				break;
			case R.id.main_course3_btn:
				viewPager.setCurrentItem(3);
				setRadioButtonCenter(3);
				break;
			case R.id.main_course4_btn:
				viewPager.setCurrentItem(4);
				setRadioButtonCenter(4);
				break;
			}
			viewPager.setOnPageChangeListener(new MyPageChangeListener());
		}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		if (resultCode == R.id.base_right_tv) {
			return;
		}
		String activityName = data.getStringExtra("activityName");
		if (activityName.contains(".")) {
			activityName = activityName.substring(activityName.lastIndexOf(".") + 1);
		}
		util.print("name=" + activityName);
		// 获取当前活动的Activity实例
		Activity subActivity = activityManager.getActivity(activityName);
		// 判断是否实现返回值接口
		if (subActivity instanceof OnTabActivityResultListener) {
			// 获取返回值接口实例
			OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
			// 转发请求到子Activity
			listener.onTabActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onPageChanged(int position) {
		if (imageViews != null) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position].setBackgroundColor(Color.parseColor("#21b8c6"));
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (position != i) {
					imageViews[i].setBackgroundColor(Color.parseColor("#eeeeee"));
				}
			}
		}
	}

	private void setViewPager() {
		InfinitePagerAdapter adapter = null;
		int length = 0;
		if (adImageUrl != null && adImageUrl.length > 0) {
			adapter = new InfinitePagerAdapter(this, adImageUrl, screenWidth, viewPagerHeight);
			length = adImageUrl.length;
		} else {
			adapter = new InfinitePagerAdapter(this, new int[] { R.drawable.defaultimage });
			length = 1;
			defaultImage.setVisibility(View.GONE);
		}
		adapter.setPageClickListener(new MyPageClickListener());
		adapter.setURLErrorListener(this);
		topViewPager.setAdapter(adapter);

		imageViews = new ImageView[length];
		ImageView imageView = null;
		dotLayout.removeAllViews();
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams((int) (8 * screenDensity),
				(int) (4 * screenDensity));
		dotLayout.addView(new TextView(this), textParams);
		// 添加小圆点的图片
		for (int i = 0; i < length; i++) {
			imageView = new ImageView(this);
			// 设置小圆点imageview的参数
			imageView.setLayoutParams(new LayoutParams((int) (16 * screenDensity), (int) (4 * screenDensity)));// 创建一个宽高均为20
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
			// String url =
			// adList.get(position).getHeadportrait().getOriginalpic();
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
	public void onURlError(Exception e) {

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

	@Override
	protected void onPause() {
		mapView.onPause();
		stopLocation();
		super.onPause();
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
	public void forOperResult(Intent intent) {
		boolean isRequestLogin = intent.getBooleanExtra("isRequestLogin", false);
		if (isRequestLogin) {
			finish();
		}
		boolean isVertify = intent.getBooleanExtra("isVertify", false);
		if (isVertify) {
			radioGroup.check(R.id.main_course2_btn);
		}
		boolean isEnrollSuccess = intent.getBooleanExtra("isEnrollSuccess", false);
		if (isEnrollSuccess) {
			app.userVO.setApplystate(EnrollResult.SUBJECT_ENROLLING.getValue());
			radioGroup.check(R.id.main_course1_btn);
		}
	}

	@Override
	protected void onDestroy() {
		app.isLogin = false;
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
		}
		return super.onKeyDown(keyCode, event);
	}
}
