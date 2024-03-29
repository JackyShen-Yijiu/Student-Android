package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.adapter.OpenCityAdapter;
import com.sft.adapter.SearchCoachListAdapter;
import com.sft.api.ApiHttpClient;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.view.RefreshLayout;
import com.sft.view.RefreshLayout.OnLoadListener;
import com.sft.vo.CoachVO;
import com.sft.vo.OpenCityVO;

public class SearchCoachActivity extends BaseActivity implements
		OnRefreshListener, OnItemClickListener, OnLoadListener {

	public static final String from_searchCoach_enroll = "from_searchCoach_enroll";
	private final static String openCity = "openCity";
	private List<OpenCityVO> openCityList;
	private Context mContext;
	private String currCity = null;
	private EditText searchCoach;
	private TextView carSelect;
	private TextView distanceSelect;
	private TextView commentSelect;
	private ImageView arrow3;
	private ImageView arrow2;
	private ImageView arrow1;

	private ListView coachListView;
	private RefreshLayout swipeLayout;
	private PopupWindow popupWindow;
	private PopupWindow openCityPopupWindow;

	private boolean isCarSelected = false;

	private String cityname;
	private String licensetype;
	private String coachname;
	private String ordertype;

	private int index = 1; // 分页
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;
	private boolean isSearchCoach = false;

	private CoachVO selectCoach;
	private List<CoachVO> coachList = new ArrayList<CoachVO>();
	private SearchCoachListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_school);

		mContext = this;
		setRightText("定位中");
		currCity = app.curCity;
		initView();
		initData();
		setListener();
		cityname = "";
		licensetype = "";
		coachname = "";
		ordertype = "0";
		obtainCaoch();
	}

	private void initView() {
		setTitleText(R.string.search_coach);

		swipeLayout = (RefreshLayout) findViewById(R.id.enroll_school_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		coachListView = (ListView) findViewById(R.id.enroll_select_school_listview);

		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		if (currCity != null) {
			currCity = currCity.replace("市", "");
			setRightText(currCity);

		}

		View headerView = View.inflate(mContext, R.layout.search_coach_header,
				null);

		coachListView.addHeaderView(headerView);
		searchCoach = (EditText) headerView
				.findViewById(R.id.search_coach_search_et);

//		carSelect = (TextView) headerView
//				.findViewById(R.id.search_coach_car_select_tv);
//		distanceSelect = (TextView) headerView
//				.findViewById(R.id.search_coach_distance_select_tv);
//		commentSelect = (TextView) headerView
//				.findViewById(R.id.search_coach_comment_select_tv);
//
//		arrow1 = (ImageView) headerView
//				.findViewById(R.id.search_coach_arrow1_iv);
//		arrow2 = (ImageView) headerView
//				.findViewById(R.id.search_coach_arrow2_iv);
//		arrow3 = (ImageView) headerView
//				.findViewById(R.id.search_coach_arrow3_iv);

	}

	private void initData() {
		searchCoach.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		searchCoach.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) searchCoach.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(SearchCoachActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 实现搜索
					LogUtil.print("搜索");
					coachname = searchCoach.getText().toString().trim();
					searchcoach(true);
					return true;
				}
				return false;
			}

		});
	}

	private void obtainCaoch() {
		RequestParams paramMap = new RequestParams();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "10000");
		paramMap.put("cityname", cityname);
		paramMap.put("licensetype", licensetype);
		paramMap.put("coachname", coachname);
		paramMap.put("ordertype", ordertype);
		paramMap.put("index", index + "");
		paramMap.put("count", "10");

		ApiHttpClient.get("searchcoach", paramMap, handler);
	}

	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte) {
			String value = parseJson(paramArrayOfByte);
			if (!TextUtils.isEmpty(msg)) {
				// 加载失败，弹出失败对话框
				toast.setText(msg);
			} else {
				processSuccess(value);

			}
		}

		@Override
		public void onFailure(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte, Throwable paramThrowable) {

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
		if (value != null) {
			LogUtil.print(value);
			try {
				List<CoachVO> coachList = (List<CoachVO>) JSONUtil
						.parseJsonToList(value, new TypeToken<List<CoachVO>>() {
						}.getType());
				int selectIndex = -1;
				for (int i = 0; i < coachList.size(); i++) {
					CoachVO coachVO = coachList.get(i);
					if (selectCoach != null) {
						if (selectCoach.getCoachid().equals(
								coachVO.getCoachid())) {
							selectIndex = i;
						}
					}
				}
				LogUtil.print("coachList==" + coachList.size());
				setData(coachList, selectIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setData(final List<CoachVO> coach, final int selectIndex) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isSearchCoach) {
					if (index == 1 && coach.size() == 0) {
						toast.setText("没有搜索到您要找的教练");
						return;
					}
				}
				if (index == 1) {
					coachList.clear();
					if (!isRefreshing) {
						coachList.addAll(coach);
						adapter = new SearchCoachListAdapter(mContext,
								coachList);
						coachListView.setAdapter(adapter);
					} else {
						coachList.addAll(coach);
						adapter.notifyDataSetChanged();
					}
					if (coach.size() == 0) {
						toast.setText("该选项下没有数据");
					}
				} else {
					if (coach.size() == 0) {
						toast.setText("没有更多数据了");
					} else {
						coachList.addAll(coach);
						LogUtil.print("onItemClick===" + coachList.size());
						// if (selectIndex >= 0) {
						// // 将已选择的教练放在第一位
						// coachList.add(0, coachList.get(selectIndex));
						// coachList.remove(selectIndex + 1);
						// }
						//
						if (selectIndex >= 0) {
							adapter.setSelected(0);
						}
						adapter.notifyDataSetChanged();

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
		});

	}

	private void searchcoach(boolean isSearch) {

		if (isSearch) {
			index = 1;
		}
		isSearchCoach = true;
		obtainCaoch();
	}

	private void setListener() {
		coachListView.setOnItemClickListener(this);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);

		carSelect.setOnClickListener(this);
		distanceSelect.setOnClickListener(this);
		commentSelect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			setResult(v.getId(), getIntent());
			finish();
			break;
		case R.id.base_right_tv:
			obtainOpenCity();
			break;

//		case R.id.search_coach_car_select_tv:
//
//			showPopupWindow(carSelect);
//			break;
//		case R.id.search_coach_distance_select_tv:
//			index = 1;
//			ordertype = "1";
//			coachname = "";
//			obtainCaoch();
//			setSelectState(2);
//			break;
//		case R.id.search_coach_comment_select_tv:
//			setSelectState(3);
//			index = 1;
//			ordertype = "2";
//			coachname = "";
//			obtainCaoch();
//			break;

		case R.id.pop_window_one:
			setSelectState(1);
			isCarSelected = true;
			index = 1;
			licensetype = "1";
			coachname = "";
			ordertype = "";
			obtainCaoch();
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		case R.id.pop_window_two:
			setSelectState(1);
			isCarSelected = true;
			index = 1;
			licensetype = "2";
			coachname = "";
			ordertype = "";
			obtainCaoch();
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

		carSelect.setTextColor(getResources().getColor(
				R.color.default_text_color));
		commentSelect.setTextColor(getResources().getColor(
				R.color.default_text_color));
		distanceSelect.setTextColor(getResources().getColor(
				R.color.default_text_color));
		arrow1.setImageResource(R.drawable.arrow_below);
		arrow2.setImageResource(R.drawable.arrow_below);
		arrow3.setImageResource(R.drawable.arrow_below);
		switch (position) {
		case 1:
			carSelect.setTextColor(getResources().getColor(
					R.color.app_main_color));
			arrow1.setImageResource(R.drawable.arrow_below_selector);
			break;
		case 2:
			if (isCarSelected) {
				carSelect.setTextColor(getResources().getColor(
						R.color.app_main_color));
				arrow1.setImageResource(R.drawable.arrow_below_selector);
			}
			distanceSelect.setTextColor(getResources().getColor(
					R.color.app_main_color));
			arrow2.setImageResource(R.drawable.arrow_below_selector);
			break;
		case 3:
			if (isCarSelected) {
				carSelect.setTextColor(getResources().getColor(
						R.color.app_main_color));
				arrow1.setImageResource(R.drawable.arrow_below_selector);
			}
			commentSelect.setTextColor(getResources().getColor(
					R.color.app_main_color));
			arrow3.setImageResource(R.drawable.arrow_below_selector);
			break;

		}
	}

	private void showPopupWindow(View parent) {
		if (popupWindow == null) {
			View view = View.inflate(mContext, R.layout.pop_window, null);

			TextView c1Car = (TextView) view.findViewById(R.id.pop_window_one);
			c1Car.setText(R.string.c1_automatic_gear_car);
			TextView c2Car = (TextView) view.findViewById(R.id.pop_window_two);
			c2Car.setText(R.string.c2_manual_gear_car);
			c1Car.setOnClickListener(this);
			c2Car.setOnClickListener(this);

			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.showAsDropDown(parent);
	}

	private void showOpenCityPopupWindow(View parent) {
		if (openCityPopupWindow == null) {
			LinearLayout popWindowLayout = (LinearLayout) View.inflate(
					mContext, R.layout.pop_window, null);
			popWindowLayout.removeAllViews();
			// LinearLayout popWindowLayout = new LinearLayout(mContext);
			popWindowLayout.setOrientation(LinearLayout.VERTICAL);
			ListView OpenCityListView = new ListView(mContext);
			OpenCityListView.setDividerHeight(0);
			OpenCityListView.setCacheColorHint(android.R.color.transparent);
			OpenCityListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					OpenCityVO selectCity = openCityList.get(position);
					System.out.println(selectCity.getName());
					cityname = selectCity.getName().replace("市", "");
					licensetype = "";
					coachname = "";
					ordertype = "0";
					index = 1;
					obtainCaoch();
					openCityPopupWindow.dismiss();
					openCityPopupWindow = null;
				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			popWindowLayout.addView(OpenCityListView, param);
			OpenCityAdapter openCityAdapter = new OpenCityAdapter(mContext,
					openCityList);
			OpenCityListView.setAdapter(openCityAdapter);

			openCityPopupWindow = new PopupWindow(popWindowLayout, 130,
					LayoutParams.WRAP_CONTENT);
		}
		openCityPopupWindow.setFocusable(true);
		openCityPopupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		openCityPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		openCityPopupWindow.showAsDropDown(parent);
	}

	@Override
	public void onRefresh() {
		isRefreshing = true;
		index = 1;
		obtainCaoch();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LogUtil.print("position===" + position);
		LogUtil.print("index===" + index);

		Intent intent = new Intent(this, CoachDetailActivity.class);
		CoachVO coachVO = adapter.getItem(position - 1);
		intent.putExtra("coach", coachVO);

		intent.putExtra(from_searchCoach_enroll, true);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onLoad() {
		isLoadingMore = true;
		index++;
		obtainCaoch();
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(openCity)) {
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
					showOpenCityPopupWindow(rightTV);
				}
			}
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				return;
			}
			boolean isFromEnroll = data.getBooleanExtra(
					from_searchCoach_enroll, false);
			if (isFromEnroll) {
				data.setClass(this, ApplyActivity.class);
				data.putExtra(from_searchCoach_enroll, isFromEnroll);
				startActivity(data);
				finish();
			}
		}
	}
}
