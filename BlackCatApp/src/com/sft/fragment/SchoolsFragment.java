package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.InfiniteViewPager;
import cn.sft.infinitescrollviewpager.MyHandler;
import cn.sft.infinitescrollviewpager.PageChangeListener;
import cn.sft.infinitescrollviewpager.PageClickListener;

import com.google.gson.reflect.TypeToken;
import com.jzjf.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.adapter.SchoolListAdapter;
import com.sft.api.ApiHttpClient;
import com.sft.blackcatapp.ApplyActivity;
import com.sft.blackcatapp.ConfirmOrderActivity;
import com.sft.blackcatapp.SchoolDetailActivity;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.CheckApplyDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.listener.MOnScrollListener;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.view.RefreshLayout;
import com.sft.view.RefreshLayout.OnLoadListener;
import com.sft.viewutil.EditTextUtils;
import com.sft.vo.HeadLineNewsVO;
import com.sft.vo.OpenCityVO;
import com.sft.vo.PayOrderVO;
import com.sft.vo.SchoolVO;

/**
 * 学校、
 * 
 * @author sun 2016-1-26 下午3:28:01
 * 
 */
public class SchoolsFragment extends BaseFragment implements
		OnItemClickListener, OnSelectConfirmListener,
		BitMapURLExcepteionListner, PageChangeListener, OnRefreshListener,
		OnLoadListener, OnClickListener {

	private String currCity = null;

	// private String cityname = "北京";
	private String licensetype = "1";
	public String schoolname;
	private String ordertype = "0";

	private final static String nearBySchool = "nearBySchool";
	private static final String headlineNews = "headlineNews";
	private final static String openCity = "openCity";
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
	private View headerView;
	private ImageView imgDelete;
	private RefreshLayout swipeLayout;
	// private LinearLayout llSearch;

	//

	private int index = 1; // 分页
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;

	private List<OpenCityVO> openCityList;
	private PopupWindow openCityPopupWindow;

	private boolean scrollFlag;
	/** 上次所在的位置 */
	private int lastId = 0;

	static SchoolsFragment fragment = null;

	private static int type;

	private RelativeLayout errorRl;
	private TextView errorTv;

	public static SchoolsFragment getInstance(int t) {
		if (fragment == null)
			fragment = new SchoolsFragment();
		type = t;

		LogUtil.print("type-onItemClick->" + type);
		return fragment;
	}

	public void setCurrent(SchoolVO selectSchool) {
		selectSchool = this.selectSchool;
	}

	private void initData() {
		// if(null==app.curCity){
		// }else{
		// cityname = app.curCity;
		// }

		ordertype = "" + getArguments().getInt("type", 2);// 评分
		searchSchool.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		searchSchool.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) searchSchool.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getActivity()
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 实现搜索
					LogUtil.print("搜索");
					schoolname = searchSchool.getText().toString().trim();
					searchSchool(true);
					return true;
				}
				return false;
			}

		});
	}

	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte) {
			String value = parseJson(paramArrayOfByte);
			LogUtil.print("nearby--onSuccess->" + value);

			if (!TextUtils.isEmpty(msg)) {
				// 加载失败，弹出失败对话框
				Toast(msg);
			} else {
				processSuccess(value);
			}
		}

		@Override
		public void onFailure(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte, Throwable paramThrowable) {
			LogUtil.print("nearby--onFailure->" + paramInt);

		}
	};

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

	// 搜索成功
	protected void processSuccess(String value) {
		
		
		if (!isSearchSchool) {
			searchSchool.setVisibility(View.GONE);
			imgDelete.setVisibility(View.GONE);
			headerView.setVisibility(View.GONE);
		}
		// headerView.setVisibility(View.GONE);

		// ((EnrollSchoolActivity1)getActivity()).etSearch.setVisibility(View.GONE);
		// searchSchool.setVisibility(View.GONE);
		if (value != null) {
			LogUtil.print(value);
			try {
				@SuppressWarnings("unchecked")
				List<SchoolVO> schoolList = (List<SchoolVO>) JSONUtil
						.parseJsonToList(value,
								new TypeToken<List<SchoolVO>>() {
								}.getType());
				int selectIndex = -1;
				for (int i = 0; i < schoolList.size(); i++) {
					SchoolVO schoolVO = schoolList.get(i);
					if (selectSchool != null) {
						if (selectSchool.getSchoolid().equals(
								schoolVO.getSchoolid())) {
							selectIndex = i;
						}
					}
				}
//				LogUtil.print("processSuccess---->"+schoolList.size());
//				Toast("processSuccess---->"+schoolList.size());
				if (isSearchSchool) {
					setSearchData(schoolList, selectIndex);
				} else {
					setData(schoolList, selectIndex);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isSearchSchool = false;
	private int searchIndex = 1;
	private TextView classSelect;
	private TextView distanceSelect;
	private TextView commentSelect;
	private TextView priceSelect;
	private ImageView arrow1, arrow2, arrow3, arrow4;

	public void searchSchool(boolean isSearch) {
		if (isSearch) {
			searchIndex = 1;
		}
		isSearchSchool = true;
		LogUtil.print(schoolname);
		RequestParams paramMap = new RequestParams();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "10000");
		paramMap.put("schoolname", schoolname);
		paramMap.put("index", searchIndex + "");
		paramMap.put("count", "10");

		ApiHttpClient.get("searchschool", paramMap, handler);
	}

	// 获取头部轮播图图片
	// private void obtainHeadLineNews() {
	// HttpSendUtils.httpGetSend(headlineNews, this, Config.IP
	// + "api/v1/info/headlinenews");
	//
	// }

	// @Override
	// protected void onResume() {
	// register(getClass().getName());
	// super.onResume();
	// };

	private void initViewA(View rootView) {
		// setTitleText(R.string.select_school);
		errorRl = (RelativeLayout) rootView.findViewById(R.id.error_rl);
		errorTv = (TextView) rootView.findViewById(R.id.error_tv);
		swipeLayout = (RefreshLayout) rootView
				.findViewById(R.id.enroll_school_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		headerView = View.inflate(getActivity(), R.layout.enroll_school_header,
				null);

		//

		schoolListView = (ListView) rootView
				.findViewById(R.id.enroll_select_school_listview);

		schoolListView.addHeaderView(headerView);
		adapter = new SchoolListAdapter(getActivity(), schoolList);

		schoolListView.setAdapter(adapter);

		swipeLayout.setChildScroll(new MOnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				LogUtil.print("scrolling--->"
						+ schoolListView.getFirstVisiblePosition());
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
					scrollFlag = false;
					// 判断滚动到底部
					if (schoolListView.getLastVisiblePosition() == (schoolListView
							.getCount() - 1)) {
						// toTopBtn.setVisibility(View.VISIBLE);
					}
					// 判断滚动到顶部
					if (schoolListView.getFirstVisiblePosition() == 0) {
						// toTopBtn.setVisibility(View.GONE);
					}

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
					scrollFlag = true;

					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
					scrollFlag = false;
					break;
				}
			}

			/**
			 * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
			 * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				lastId = firstVisibleItem;
			}

			@Override
			public void downPull() {
				if (lastId == 0) {
					searchSchool.setVisibility(View.VISIBLE);
					imgDelete.setVisibility(View.VISIBLE);
					headerView.setVisibility(View.VISIBLE);
					// ((EnrollSchoolActivity1)getActivity()).etSearch.setVisibility(View.VISIBLE);
					LogUtil.print("scrolling---2222>"
							+ schoolListView.getPivotX());
					// searchSchool.setVisibility(View.VISIBLE);
					// schoolListView.scrollListBy(0);
					LogUtil.print("scrolling---4444>"
							+ schoolListView.getPivotX());
				}

			}
		});

		// searchSchool.

		// schoolListView.setPullRefreshEnable(false);
		// schoolListView.setPullLoadEnable(false);

		// if (app.userVO != null
		// && app.userVO.getApplystate().equals(
		// EnrollResult.SUBJECT_NONE.getValue())) {
		// showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		// setText(0, R.string.finish);
		// } else {
		// showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		// if (currCity != null) {
		// currCity = currCity.replace("市", "");
		// setRightText(currCity);
		//
		// }
		// }

		//
		// // 查找搜索框
		// llSearch = (LinearLayout) headerView
		// .findViewById(R.id.enroll_school_select_ll);
		//
		// adLayout = (RelativeLayout) headerView
		// .findViewById(R.id.enroll_school_top_headpic_im);
		// topViewPager = (InfiniteViewPager) headerView
		// .findViewById(R.id.enroll_school_top_viewpager);
		// dotLayout = (LinearLayout) headerView
		// .findViewById(R.id.enroll_school_top_dotlayout);
		// defaultImage = (ImageView) headerView
		// .findViewById(R.id.enroll_school_top_defaultimage);
		searchSchool = (EditText) headerView
				.findViewById(R.id.enroll_school_search_et);
		imgDelete = (ImageView) headerView.findViewById(R.id.header_delete);
		imgDelete.setOnClickListener(this);
		EditTextUtils.setEditTextHint(searchSchool,
				getString(R.string.search_school), 12);
		//
		// classSelect = (TextView) headerView
		// .findViewById(R.id.enroll_school_class_select_tv);

		// distanceSelect = (TextView)
		// findViewById(R.id.enroll_school_distance_select_tv);
		// commentSelect = (TextView)
		// findViewById(R.id.enroll_school_comment_select_tv);
		// priceSelect = (TextView)
		// findViewById(R.id.enroll_school_price_select_tv);
		// arrow1 = (ImageView) findViewById(R.id.enroll_school_arrow1_iv);
		// arrow2 = (ImageView) findViewById(R.id.enroll_school_arrow2_iv);
		// arrow3 = (ImageView) findViewById(R.id.enroll_school_arrow3_iv);
		// arrow4 = (ImageView) findViewById(R.id.enroll_school_arrow4_iv);
		//
		// searchSchool.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		// RelativeLayout.LayoutParams headParams =
		// (RelativeLayout.LayoutParams) adLayout
		// .getLayoutParams();
		// headParams.width = screenWidth;
		// int height = (int) ((screenWidth - 16 * screenDensity) / 3
		// + (screenWidth - 12 * screenDensity) * 2 / 3 + statusbarHeight);
		// height += (63 * screenDensity);
		//
		// headParams.height = screenHeight - height;
		// viewPagerHeight = headParams.height;
		// setViewPager();

	}

	// private void setViewPager() {
	// InfinitePagerAdapter adapter = null;
	// int length = 0;
	// if (adImageUrl != null && adImageUrl.length > 0) {
	// adapter = new InfinitePagerAdapter(this, adImageUrl, screenWidth,
	// viewPagerHeight);
	// length = adImageUrl.length;
	// } else {
	// adapter = new InfinitePagerAdapter(getActivity(),
	// new int[] { R.drawable.defaultimage });
	// length = 1;
	// defaultImage.setVisibility(View.GONE);
	// }
	// adapter.setPageClickListener(new MyPageClickListener());
	// adapter.setURLErrorListener(this);
	// topViewPager.setAdapter(adapter);
	//
	// imageViews = new ImageView[length];
	// ImageView imageView = null;
	// dotLayout.removeAllViews();
	// LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
	// (int) (8 * screenDensity), (int) (4 * screenDensity));
	// dotLayout.addView(new TextView(this), textParams);
	// // 添加小圆点的图片
	// for (int i = 0; i < length; i++) {
	// imageView = new ImageView(this);
	// // 设置小圆点imageview的参数
	// imageView.setLayoutParams(new LayoutParams(
	// (int) (6 * screenDensity), (int) (6 * screenDensity)));// 创建一个宽高均为20
	// // 的布局
	// // 将小圆点layout添加到数组中
	// imageView
	// .setBackgroundResource(R.drawable.enroll_school_dot_selector);
	// imageViews[i] = imageView;
	//
	// // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
	// if (i == 0) {
	// imageView.setEnabled(true);
	// } else {
	// imageView.setEnabled(false);
	// }
	// // 将imageviews添加到小圆点视图组
	// dotLayout.addView(imageViews[i]);
	// dotLayout.addView(new TextView(this), textParams);
	// }
	// }

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

	private void setData(List<SchoolVO> school, int selectIndex) {

		if (index == 1) {
			schoolList.clear();
			if (!isRefreshing) {
				schoolList.addAll(school);
				// adapter = new SchoolListAdapter(getActivity(), schoolList);
				// schoolListView.setAdapter(adapter);
				adapter.setData(schoolList);
				adapter.notifyDataSetChanged();
				LogUtil.print("nearby---000>" + schoolList.size());
			} else {// ??? 正在刷新
				// schoolList.addAll(school);
				schoolList.clear();
				schoolList = school;
				adapter.setData(schoolList);
				adapter.notifyDataSetChanged();
				LogUtil.print("nearby---111>" + schoolList.size());
				// adapter = new SchoolListAdapter(getActivity(), schoolList);
				// schoolListView.setAdapter(adapter);
			}
			noData(schoolList.size());
		} else {
			if (school.size() == 0) {
				Toast("没有更多数据了");
				// swipeLayout.setLoading(true);
			} else {

				schoolList.addAll(school);

				adapter.notifyDataSetChanged();
				LogUtil.print("nearby---222>" + schoolList.size());
				if (selectIndex >= 0) {
					adapter.setSelected(0);
				}
			}
		}

		// swipeLayout.setl
		// if (selectIndex >= 0) {
		// // 将已选择的驾校放在第一位
		// schoolList.add(0, schoolList.get(selectIndex));
		// schoolList.remove(selectIndex + 1);
		// }
		if (isRefreshing) {
			swipeLayout.setRefreshing(false);
			isRefreshing = false;
		}
		if (isLoadingMore) {
			swipeLayout.setLoading(false);
			isLoadingMore = false;
		}
	}

	private void noData(int size) {
		if (size == 0) {
			errorRl.setVisibility(View.VISIBLE);
			errorTv.setText(R.string.no_school);
		} else {
			errorRl.setVisibility(View.GONE);
		}

	}

	/**
	 * 获取学校根据 城市的不同
	 */
	public void getSchoolByCity(String cityName) {
		// cityname =cityName;
		obtainNearBySchool();
	}
	
	public void setIndex(int i){
		index = i;
	}

	private void obtainNearBySchool() {
		LogUtil.print("nearBySchool-->" + app.curCity);
		RequestParams paramMap = new RequestParams();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "10000");
		paramMap.put("cityname", app.curCity);
		paramMap.put("licensetype", licensetype);
		paramMap.put("schoolname", schoolname);
		paramMap.put("ordertype", ordertype);
		paramMap.put("index", index + "");
		paramMap.put("count", "10");
		// LogUtil.print("Nearby-->"+app.latitude+"long-->"+app.longtitude+"orderType"+ordertype);
		ApiHttpClient.get("searchSchool", paramMap, handler);// "searchschool"
	}

	private void setListener() {
		schoolListView.setOnItemClickListener(this);
		// topViewPager.setPageChangeListener(this);
		// searchSchool.seton
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);

		// classSelect.setOnClickListener(this);
		// distanceSelect.setOnClickListener(this);
		// commentSelect.setOnClickListener(this);
		// priceSelect.setOnClickListener(this);
	}

	private boolean isClassSelected = false;

	@Override
	public void onClick(View v) {
		// if (!onClickSingleView()) {
		// return;
		// }
		switch (v.getId()) {
		case R.id.base_left_btn:
			// finish();
			break;
		case R.id.base_right_tv:
			obtainOpenCity();
			break;

		case R.id.enroll_school_class_select_tv:
			index = 1;

			showPopupWindow(classSelect);
			break;
		case R.id.enroll_school_distance_select_tv:
			index = 1;
			// cityname = currCity;
			schoolname = "";
			ordertype = "1";
			// setSelectState(2);
			obtainNearBySchool();
			break;
		case R.id.enroll_school_comment_select_tv:
			index = 1;
			// cityname = currCity;
			schoolname = "";
			ordertype = "2";
			// setSelectState(3);
			obtainNearBySchool();
			break;
		case R.id.enroll_school_price_select_tv:
			index = 1;
			// cityname = currCity;
			schoolname = "";
			ordertype = "3";
			// setSelectState(4);
			obtainNearBySchool();
			break;

		case R.id.pop_window_one:
			// setSelectState(1);
			isClassSelected = true;
			// cityname = currCity;
			licensetype = "1";
			schoolname = "";
			ordertype = "";
			classSelect.setText(R.string.c1_automatic_gear_car);
			LogUtil.print("====" + licensetype);
			obtainNearBySchool();
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		case R.id.pop_window_two:
			// setSelectState(1);
			classSelect.setText(R.string.c2_manual_gear_car);
			isClassSelected = true;
			// cityname = currCity;
			licensetype = "2";
			schoolname = "";
			LogUtil.print("====" + licensetype);
			ordertype = "";
			obtainNearBySchool();
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		case R.id.header_delete:// 删除 输入框 ，从新刷新请求数据
			if (searchSchool.getText().toString().length() > 0) {
				searchSchool.setText("");
				schoolname = "";
				searchSchool(true);
			}

			break;
		}
	}

	public void order(String cityName) {
		index = 1;
		// cityname = cityName;
		schoolname = "";
		ordertype = "0";
		// setSelectState(2);
		obtainNearBySchool();
	}

	/**
	 * 排序
	 */
	public void order(int flag) {
		switch (flag) {
		case R.id.enroll_school_distance_select_tv:// 距离
			index = 1;
			// cityname = currCity;
			schoolname = "";
			ordertype = "1";
			// setSelectState(2);
			obtainNearBySchool();
			break;
		case R.id.enroll_school_comment_select_tv:// 评价
			index = 1;
			// cityname = currCity;
			schoolname = "";
			ordertype = "2";
			// setSelectState(3);
			obtainNearBySchool();
			break;
		case R.id.enroll_school_price_select_tv:// 价格
			index = 1;
			// cityname = currCity;
			schoolname = "";
			ordertype = "3";
			// setSelectState(4);
			obtainNearBySchool();
			break;

		case R.id.pop_window_one:
			// setSelectState(1);
			isClassSelected = true;
			// cityname = currCity;
			licensetype = "1";
			schoolname = "";
			ordertype = "";
			LogUtil.print("====" + licensetype);
			obtainNearBySchool();
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		case R.id.pop_window_two:
			// setSelectState(1);
			// classSelect.setText(R.string.c2_manual_gear_car);
			isClassSelected = true;
			// cityname = currCity;
			licensetype = "2";
			schoolname = "";
			LogUtil.print("====" + licensetype);
			ordertype = "";
			obtainNearBySchool();
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		case R.id.pop_window_three:
			isClassSelected = true;
			// cityname = currCity;
			licensetype = "3";
			schoolname = "";
			// setSelectState(1);
			ordertype = "";
			obtainNearBySchool();
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		}

	}

	private void obtainOpenCity() {
		HttpSendUtils.httpGetSend(openCity, this, Config.IP
				+ "api/v1/getopencity");
	}

	private void setSelectState(int position) {

		classSelect.setTextColor(getResources().getColor(
				R.color.default_text_color));
		priceSelect.setTextColor(getResources().getColor(
				R.color.default_text_color));
		commentSelect.setTextColor(getResources().getColor(
				R.color.default_text_color));
		distanceSelect.setTextColor(getResources().getColor(
				R.color.default_text_color));
		arrow1.setImageResource(R.drawable.arrow_below);
		arrow2.setImageResource(R.drawable.arrow_below);
		arrow3.setImageResource(R.drawable.arrow_below);
		arrow4.setImageResource(R.drawable.arrow_below);
		switch (position) {
		case 1:
			classSelect.setTextColor(getResources().getColor(
					R.color.app_main_color));
			arrow1.setImageResource(R.drawable.arrow_below_selector);
			break;
		case 2:
			if (isClassSelected) {
				classSelect.setTextColor(getResources().getColor(
						R.color.app_main_color));
				arrow1.setImageResource(R.drawable.arrow_below_selector);
			}
			distanceSelect.setTextColor(getResources().getColor(
					R.color.app_main_color));
			arrow2.setImageResource(R.drawable.arrow_below_selector);
			break;
		case 3:
			if (isClassSelected) {
				classSelect.setTextColor(getResources().getColor(
						R.color.app_main_color));
				arrow1.setImageResource(R.drawable.arrow_below_selector);
			}
			commentSelect.setTextColor(getResources().getColor(
					R.color.app_main_color));
			arrow3.setImageResource(R.drawable.arrow_below_selector);
			break;
		case 4:
			if (isClassSelected) {
				classSelect.setTextColor(getResources().getColor(
						R.color.app_main_color));
				arrow1.setImageResource(R.drawable.arrow_below_selector);
			}
			priceSelect.setTextColor(getResources().getColor(
					R.color.app_main_color));
			arrow4.setImageResource(R.drawable.arrow_below_selector);
			break;

		}
	}

	private PopupWindow popupWindow;

	private boolean isFromMenu;

	private void showPopupWindow(View parent) {
		if (popupWindow == null) {
			View view = View.inflate(mContext, R.layout.pop_window, null);

			TextView c1Car = (TextView) view.findViewById(R.id.pop_window_one);
			c1Car.setText(R.string.c1_automatic_gear_car);
			TextView c2Car = (TextView) view.findViewById(R.id.pop_window_two);
			c2Car.setText(R.string.c2_manual_gear_car);
			TextView other = (TextView) view
					.findViewById(R.id.pop_window_three);
			other.setText(R.string.other);
			other.setOnClickListener(this);
			c1Car.setOnClickListener(this);
			c2Car.setOnClickListener(this);

			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// WindowManager windowManager = (WindowManager)
		// getSystemService(Context.WINDOW_SERVICE);
		// int xPos = -popupWindow.getWidth() / 2
		// + getCustomTitle().getCenter().getWidth() / 2;

		popupWindow.showAsDropDown(parent);

	}

	// private void showOpenCityPopupWindow(View parent) {
	// if (openCityPopupWindow == null) {
	// LinearLayout popWindowLayout = (LinearLayout) View.inflate(
	// mContext, R.layout.pop_window, null);
	// popWindowLayout.removeAllViews();
	// // LinearLayout popWindowLayout = new LinearLayout(mContext);
	// popWindowLayout.setOrientation(LinearLayout.VERTICAL);
	// ListView OpenCityListView = new ListView(mContext);
	// OpenCityListView.setDividerHeight(0);
	// OpenCityListView.setCacheColorHint(android.R.color.transparent);
	// OpenCityListView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// OpenCityVO selectCity = openCityList.get(position);
	// System.out.println(selectCity.getName());
	// cityname = selectCity.getName();
	// licensetype = "";
	// schoolname = "";
	// ordertype = "";
	// index = 1;
	// obtainNearBySchool();
	// openCityPopupWindow.dismiss();
	// openCityPopupWindow = null;
	// }
	// });
	// LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// popWindowLayout.addView(OpenCityListView, param);
	// OpenCityAdapter openCityAdapter = new OpenCityAdapter(mContext,
	// openCityList);
	// OpenCityListView.setAdapter(openCityAdapter);
	//
	// openCityPopupWindow = new PopupWindow(popWindowLayout, 130,
	// LayoutParams.WRAP_CONTENT);
	// }
	// openCityPopupWindow.setFocusable(true);
	// openCityPopupWindow.setOutsideTouchable(true);
	// // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
	// openCityPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	//
	// openCityPopupWindow.showAsDropDown(parent);
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			return;
		}
		LogUtil.print("onItemClick---" + type);
		if (type == 0) {
			Intent intent = new Intent(getActivity(),
					SchoolDetailActivity.class);
			SchoolVO schoolVO = adapter.getItem(position - 1);
			intent.putExtra("school", schoolVO);
			LogUtil.print("list---schoolll--->" + schoolVO.getId());
			startActivityForResult(intent, 0);
		} else {// 选择驾校
			Intent i = new Intent();
			SchoolVO schoolVO = adapter.getItem(position - 1);
			LogUtil.print("school-->");
			i.putExtra("school", schoolVO);
			getActivity().setResult(3, i);
			getActivity().finish();
		}

	}

	@Override
	public void onActivityResult(int requestCode, final int resultCode,
			final Intent data) {
//		Toast("onactviityResult-->" + resultCode);
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
			LogUtil.print("schoolsss-->");
			if (isFromMenu) {
				data.setClass(getActivity(), ApplyActivity.class);
				data.putExtra("isFromMenu", isFromMenu);
				startActivity(data);
			}
			new MyHandler(200) {
				@Override
				public void run() {
					getActivity().setResult(resultCode, data);
					getActivity().finish();
				}
			};
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		LogUtil.print(type + "doCallBack--" + jsonString);
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
					// if (length > 0) {
					// setViewPager();
					// }
				}
			} else if (type.equals(openCity)) {
				// if (dataArray != null) {
				// int length = dataArray.length();
				// openCityList = new ArrayList<OpenCityVO>();
				// for (int i = 0; i < length; i++) {
				// OpenCityVO openCityVO = null;
				// try {
				// openCityVO = JSONUtil.toJavaBean(OpenCityVO.class,
				// dataArray.getJSONObject(i));
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// if (openCityVO != null) {
				// openCityList.add(openCityVO);
				// }
				// }
				// if (length > 0) {
				// showOpenCityPopupWindow(rightTV);
				// }
				// }
			} else if (type.equals("notPay")) {// 未支付的订单
				// {"type":1,"msg":"","data":[{"_id":"56af11ce9ba0d4530524b6cb","userpaystate":0,"creattime":
				// "2016-02-01T08:05:34.823Z","payendtime":"2016-02-04T08:05:34.823Z","paychannel":0,
				// "applyschoolinfo":{"id":"562dcc3ccb90f25c3bde40da","name":"一步互联网驾校"},
				// "applyclasstypeinfo":{"id":"56a9ba41fe60f807363001c9","name":"新春特惠班","price":
				// 4980,"onsaleprice":4680},"discountmoney":0,"paymoney":4680,"activitycoupon":"","couponcode":""}]}
				// type= notPay

				int length = dataArray.length();
				List<PayOrderVO> payList = new ArrayList<PayOrderVO>();
				for (int i = 0; i < length; i++) {
					PayOrderVO pay;
					pay = JSONUtil.toJavaBean(PayOrderVO.class,
							dataArray.getJSONObject(i));
					LogUtil.print("state-->" + pay.userpaystate);
					if (pay.userpaystate.equals("0")
							|| pay.userpaystate.equals("3")) {// 订单刚生成，支付失败

						// 存在未支付订单
						// app.userVO.setApplystate(EnrollResult.SUBJECT_NONE
						// .getValue());

						app.isEnrollAgain = true;
						// if (pay.userpaystate.equals("0")
						// || pay.userpaystate.equals("3")) {// 订单刚生成，支付失败
						// LogUtil.print("notFinish---->" + i);
						// if (!hasNotFinishDialog) {
						// HasOrder(pay);
						// hasNotFinishDialog = true;
						// }
						//
						// break;
						// }
						payList.add(pay);
					}

					// if (dataArray != null) {
					// int length = dataArray.length();
					// openCityList = new ArrayList<OpenCityVO>();
					// for (int i = 0; i < length; i++) {
					// OpenCityVO openCityVO = null;
					// try {
					// openCityVO = JSONUtil.toJavaBean(OpenCityVO.class,
					// dataArray.getJSONObject(i));
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					// if (openCityVO != null) {
					// openCityList.add(openCityVO);
					// }
					// }
					// if (length > 0) {
					// showOpenCityPopupWindow(rightTV);
					// }
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/** 是否存在未完成订单 */
	public static boolean hasNotFinishDialog = false;

	private void setSearchData(List<SchoolVO> school, int selectIndex) {
		if (searchIndex == 1) {
			schoolList.clear();
			adapter.notifyDataSetChanged();
			if (school.size() == 0) {
				Toast("没有搜索到您要找的驾校");
				return;
			}
		}
		if (searchIndex != 1 && school.size() == 0) {
			Toast("没有更多数据了");
		}
		if (selectIndex >= 0) {
			// 将已选择的驾校放在第一位
			schoolList.add(0, schoolList.get(selectIndex));
			schoolList.remove(selectIndex + 1);
		}
		schoolList.addAll(school);
		adapter.notifyDataSetChanged();
		if (selectIndex >= 0) {
			adapter.setSelected(0);
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

	@Override
	public void selectConfirm(boolean isConfirm, boolean isFreshAll) {
		if (isConfirm) {
			app.selectEnrollSchool = school;
			Util.updateEnrollSchool(getActivity(), school, isFreshAll);
			if (isFreshAll) {
				app.selectEnrollCoach = Util
						.getEnrollUserSelectedCoach(getActivity());
				app.selectEnrollCarStyle = Util
						.getEnrollUserSelectedCarStyle(getActivity());
				app.selectEnrollClass = Util
						.getEnrollUserSelectedClass(getActivity());
			}
			getActivity().setResult(R.id.base_right_tv,
					new Intent().putExtra("school", school));
			getActivity().finish();
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

		isRefreshing = true;
		if (isSearchSchool) {
			searchIndex = 1;
			searchSchool(false);
		} else {
			index = 1;
			obtainNearBySchool();
		}

	}

	// 下拉加载
	@Override
	public void onLoad() {
		LogUtil.print("more--->" + index);
		isLoadingMore = true;
		if (isSearchSchool) {
			searchIndex++;
			searchSchool(false);
		} else {
			index++;
			obtainNearBySchool();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coach_or_school, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		LogUtil.print("schooll--initView");

		// addView(R.layout.activity_enroll_school);

		// isFromMenu = getIntent().getBooleanExtra("isFromMenu", false);

		currCity = app.curCity;
		isSearchSchool = false;
		// initView();
		initViewA(v);
		initData();
		setListener();
		// obtainHeadLineNews();
		// cityname = "";
		licensetype = "";
		schoolname = "";
		// ordertype = "";
		obtainNearBySchool();
		if (app.isLogin)
			requestNotFinshOrder();
	}

	/**
	 * 获取位完成的订单 详情
	 */
	private void requestNotFinshOrder() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("orderstate", "-1");// 订单的状态 // 0 订单生成 2 支付成功 3 支付失败 4 订单取消
											// -1 全部(未支付的订单)

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("notPay", this, Config.IP
				+ "api/v1/userinfo/getmypayorder", paramMap, 10000, headerMap);

	}

	/**
	 * 是否包含未支付 订单
	 */
	private void HasOrder(final PayOrderVO pay) {
		CheckApplyDialog dialog = new CheckApplyDialog(getActivity());
		dialog.setTextAndImage("立即支付", "您有未完成订单,是否需要立即支付", "重新报名",
				R.drawable.ic_question);
		dialog.setListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 立即支付
				Intent i = new Intent(getActivity(), ConfirmOrderActivity.class);
				i.putExtra("repay", true);// 再次支付
				i.putExtra("bean", pay);
				startActivity(i);
				hasNotFinishDialog = false;
			}
		});
		dialog.show();
	}

}
