package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.InfinitePagerAdapter;
import cn.sft.infinitescrollviewpager.InfiniteViewPager;
import cn.sft.infinitescrollviewpager.MyHandler;
import cn.sft.infinitescrollviewpager.PageChangeListener;
import cn.sft.infinitescrollviewpager.PageClickListener;

import com.sft.adapter.SchoolListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.view.RefreshLayout;
import com.sft.view.RefreshLayout.OnLoadListener;
import com.sft.vo.HeadLineNewsVO;
import com.sft.vo.SchoolVO;

/**
 * 选择驾校界面
 * 
 * @author Administrator
 * 
 */
public class EnrollSchoolActivity extends BaseActivity implements
		OnItemClickListener, OnSelectConfirmListener,
		BitMapURLExcepteionListner, PageChangeListener, OnRefreshListener,
		OnLoadListener {

	private final static String nearBySchool = "nearBySchool";
	private static final String headlineNews = "headlineNews";
	private static final String schoolBySearch = "schoolBySearch";
	// 学校列表
	private ListView schoolListView;

	private SchoolVO selectSchool;
	private List<SchoolVO> schoolList = new ArrayList<SchoolVO>();
	//
	private SchoolListAdapter adapter;
	// 当前选择的学校
	private SchoolVO school;

	private Context mContext;

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
	private EditText searchSchool;
	private RefreshLayout swipeLayout;

	//

	private int index = 1; // 分页
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_school);
		mContext = this;
		initView();
		initData();
		setListener();
		obtainHeadLineNews();
		obtainNearBySchool();
	}

	private void initData() {
		searchSchool.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		searchSchool.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) searchSchool.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(EnrollSchoolActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 实现搜索
					LogUtil.print("搜索");
					schoolname = searchSchool.getText().toString().trim();
					searchSchool();
					return true;
				}
				return false;
			}

		});
	}

	private String cityname;
	private String licensetype;
	private String schoolname;
	private String ordertype;

	private void searchSchool() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "10000");
		// paramMap.put("index", index + "");
		// paramMap.put("cityname", cityname);
		// paramMap.put("licensetype", licensetype);
		// paramMap.put("ordertype", ordertype);
		paramMap.put("schoolname", schoolname);
		HttpSendUtils.httpGetSend(schoolBySearch, this, Config.IP
				+ "api/v1/searchschool", paramMap);
	}

	// 获取头部轮播图图片
	private void obtainHeadLineNews() {
		HttpSendUtils.httpGetSend(headlineNews, this, Config.IP
				+ "api/v1/info/headlinenews");

	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.select_school);

		swipeLayout = (RefreshLayout) findViewById(R.id.enroll_school_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		schoolListView = (ListView) findViewById(R.id.enroll_select_school_listview);
		// schoolListView.setPullRefreshEnable(false);
		// schoolListView.setPullLoadEnable(false);

		selectSchool = (SchoolVO) getIntent().getSerializableExtra("school");

		if (app.userVO != null
				&& app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
			setText(0, R.string.finish);
		}

		View headerView = View.inflate(mContext, R.layout.enroll_school_header,
				null);

		schoolListView.addHeaderView(headerView);
		adLayout = (RelativeLayout) headerView
				.findViewById(R.id.enroll_school_top_headpic_im);
		topViewPager = (InfiniteViewPager) headerView
				.findViewById(R.id.enroll_school_top_viewpager);
		dotLayout = (LinearLayout) headerView
				.findViewById(R.id.enroll_school_top_dotlayout);
		defaultImage = (ImageView) headerView
				.findViewById(R.id.enroll_school_top_defaultimage);
		searchSchool = (EditText) headerView
				.findViewById(R.id.enroll_school_search_et);

		searchSchool.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) adLayout
				.getLayoutParams();
		headParams.width = screenWidth;
		int height = (int) ((screenWidth - 16 * screenDensity) / 3
				+ (screenWidth - 12 * screenDensity) * 2 / 3 + statusbarHeight);
		height += (63 * screenDensity);

		headParams.height = screenHeight - height;
		viewPagerHeight = headParams.height;
		setViewPager();

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
			defaultImage.setVisibility(View.GONE);
		}
		adapter.setPageClickListener(new MyPageClickListener());
		adapter.setURLErrorListener(this);
		topViewPager.setAdapter(adapter);

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
					(int) (6 * screenDensity), (int) (6 * screenDensity)));// 创建一个宽高均为20
			// 的布局
			// 将小圆点layout添加到数组中
			imageView
					.setBackgroundResource(R.drawable.enroll_school_dot_selector);
			imageViews[i] = imageView;

			// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
			if (i == 0) {
				imageView.setEnabled(true);
			} else {
				imageView.setEnabled(false);
			}
			// 将imageviews添加到小圆点视图组
			dotLayout.addView(imageViews[i]);
			dotLayout.addView(new TextView(this), textParams);
		}
	}

	private class MyPageClickListener implements PageClickListener {

		@Override
		public void onPageClick(int position) {
			try {
				if (adList != null && adList.size() > position) {
					String url = adList.get(position).getHeadportrait()
							.getOriginalpic();
					if (!TextUtils.isEmpty(url)) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri content_url = Uri.parse(url);
						intent.setData(content_url);
						startActivity(intent);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	private void setData(List<SchoolVO> school, int selectIndex) {

		if (index == 1) {
			if (!isRefreshing) {
				schoolList.addAll(school);
				adapter = new SchoolListAdapter(this, schoolList);
				schoolListView.setAdapter(adapter);
			}
			schoolList.clear();
		}

		if (school.size() == 0 && index != 1) {
			toast.setText("没有更多数据了");
		} else {

			schoolList.addAll(school);
			if (selectIndex >= 0) {
				// 将已选择的驾校放在第一位
				schoolList.add(0, schoolList.get(selectIndex));
				schoolList.remove(selectIndex + 1);
			}
			adapter.notifyDataSetChanged();
			LogUtil.print("aaaaaaaaa" + schoolList.size());
			if (selectIndex >= 0) {
				adapter.setSelected(0);
			}
		}
		if (isRefreshing) {
			swipeLayout.setRefreshing(false);
			isRefreshing = false;
		}
		if (isLoadingMore) {
			swipeLayout.setLoading(false);
			isLoadingMore = false;
		}
	}

	private void obtainNearBySchool() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "10000");
		paramMap.put("index", index + "");
		paramMap.put("count", "10");
		HttpSendUtils.httpGetSend(nearBySchool, this, Config.IP
				+ "api/v1/searchschool", paramMap);
	}

	private void setListener() {
		schoolListView.setOnItemClickListener(this);
		topViewPager.setPageChangeListener(this);
		// searchSchool.seton
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			if (adapter == null || adapter.getIndex() < 0) {
				finish();
				break;
			}

			school = adapter.getItem(adapter.getIndex());
			String checkResult = Util.isConfilctEnroll(school);
			if (checkResult == null) {
				setResult(v.getId(), new Intent().putExtra("school", school));
				finish();
			} else if (checkResult.length() == 0) {
				app.selectEnrollSchool = school;
				Util.updateEnrollSchool(this, school, false);
				setResult(v.getId(), new Intent().putExtra("school", school));
				finish();
			} else {
				// 提示
				EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
						this, checkResult);
				dialog.show();
			}

			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, SchoolDetailActivity.class);
		SchoolVO schoolVO = adapter.getItem(position - 1);
		intent.putExtra("school", schoolVO);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, final int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				SchoolVO school = (SchoolVO) data
						.getSerializableExtra("school");
				if (app.userVO != null
						&& app.userVO.getApplystate().equals(
								EnrollResult.SUBJECT_NONE.getValue())
						&& school != null) {
					int position = adapter.getData().indexOf(school);
					adapter.setSelected(position);
					adapter.notifyDataSetChanged();
				}
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(resultCode, data);
					finish();
				}
			};
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (type.equals(nearBySchool)) {
				if (dataArray != null) {
					try {
						int selectIndex = -1;
						int length = dataArray.length();
						List<SchoolVO> schoolList = new ArrayList<SchoolVO>();
						for (int i = 0; i < length; i++) {
							SchoolVO schoolVO;
							schoolVO = JSONUtil.toJavaBean(SchoolVO.class,
									dataArray.getJSONObject(i));
							if (selectSchool != null) {
								if (selectSchool.getSchoolid().equals(
										schoolVO.getSchoolid())) {
									selectIndex = i;
								}
							}
							schoolList.add(schoolVO);
						}

						setData(schoolList, selectIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (type.equals(headlineNews)) {
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
			} else {
				if (type.equals(schoolBySearch)) {
					if (dataArray != null) {
						try {
							int selectIndex = -1;
							int length = dataArray.length();
							List<SchoolVO> schoolList = new ArrayList<SchoolVO>();
							for (int i = 0; i < length; i++) {
								SchoolVO schoolVO;
								schoolVO = JSONUtil.toJavaBean(SchoolVO.class,
										dataArray.getJSONObject(i));
								if (selectSchool != null) {
									if (selectSchool.getSchoolid().equals(
											schoolVO.getSchoolid())) {
										selectIndex = i;
									}
								}
								schoolList.add(schoolVO);
							}

							this.schoolList.clear();
							setData(schoolList, selectIndex);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
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
			setResult(R.id.base_right_tv,
					new Intent().putExtra("school", school));
			finish();
		}
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public void onPageChanged(int position) {
		if (imageViews != null) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position].setEnabled(true);
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (position != i) {
					imageViews[i].setEnabled(false);
				}
			}
		}
	}

	// 上啦刷新
	@Override
	public void onRefresh() {

		index = 1;

		isRefreshing = true;
		obtainNearBySchool();

	}

	// 下拉加载
	@Override
	public void onLoad() {
		index++;
		isLoadingMore = true;
		obtainNearBySchool();
	}

}
