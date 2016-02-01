package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.adapter.CoachListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.CoachVO;

/**
 * 选择教练界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("InflateParams")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EnrollCoachActivity extends BaseActivity implements
		OnItemClickListener, IXListViewListener, OnSelectConfirmListener {

	private static final String schoolCoach = "schoolCoach";
	private static final String nearbyCoach = "nearbyCoach";
	//
	private ViewPager viewPager;
	//
	private RadioGroup radioGroup;
	// 距离list
	private View distanceView;
	private RelativeLayout distanceLayout;
	private XListView distanceListView;
	// 评分list
	private View gradeView;
	private RelativeLayout gradeLayout;
	private XListView gradeListView;

	private String schoolId = null;

	private List<CoachVO> distanceList = new ArrayList<CoachVO>();
	private List<CoachVO> gradeList = new ArrayList<CoachVO>();

	private int coachIndex = 1;

	private CoachListAdapter distanceAdapter;
	private CoachListAdapter gradeAdapter;

	private CoachVO selectCoach;
	// 用户欲选择的教练
	private CoachVO coach;
	private boolean isfromtestingmsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_select_coach);
		initView();
		initData();
		setListener();
		if (TextUtils.isEmpty(schoolId)) {
			obtainNearByCoach();
			distanceListView.setPullLoadEnable(false);
			gradeListView.setPullLoadEnable(false);
		} else {
			obtainSchoolCoach(coachIndex);
			distanceListView.setPullLoadEnable(true);
			gradeListView.setPullLoadEnable(true);
		}
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.coach_list);

		viewPager = (ViewPager) findViewById(R.id.selectcoach_viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.selectcoach_radiogroup);

		selectCoach = (CoachVO) getIntent().getSerializableExtra("coach");

		if (app.userVO != null
				&& app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
			setText(0, R.string.finish);
		}
	}

	private void setListener() {
		radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		distanceListView.setOnItemClickListener(this);
		gradeListView.setOnItemClickListener(this);
		distanceListView.setXListViewListener(this);
		gradeListView.setXListViewListener(this);
	}

	private void initData() {
		List<View> listViews = new ArrayList<View>(); // 实例化listViews
		distanceView = getLayoutInflater().inflate(
				R.layout.activity_select_coach_distance, null);
		gradeView = getLayoutInflater().inflate(
				R.layout.activity_select_coach_distance, null);

		distanceListView = (XListView) distanceView
				.findViewById(R.id.select_coach_distance_listview);
		gradeListView = (XListView) gradeView
				.findViewById(R.id.select_coach_distance_listview);
		distanceLayout = (RelativeLayout) distanceView
				.findViewById(R.id.select_coach_distance_layout);
		gradeLayout = (RelativeLayout) gradeView
				.findViewById(R.id.select_coach_distance_layout);

		gradeListView.setPullRefreshEnable(false);
		distanceListView.setPullRefreshEnable(false);

		listViews.add(distanceView);
		listViews.add(gradeView);
		viewPager.setAdapter(new MyPageAdapter(listViews));

		schoolId = getIntent().getStringExtra("schoolId");
		isfromtestingmsg = getIntent().getBooleanExtra(
				TestingMsgActivity.ISFROMTESTINGMSG, false);
	}

	private void setData(int curIndex) {
		if (distanceList.size() == 0) {
			gradeLayout.setVisibility(View.VISIBLE);
			distanceLayout.setVisibility(View.VISIBLE);
		} else {

			if (distanceAdapter == null) {
				distanceAdapter = new CoachListAdapter(this,
						sortListByDistance());
			} else {
				distanceAdapter.setData(sortListByDistance());
			}
			if (distanceAdapter.getData().indexOf(selectCoach) == 0)
				distanceAdapter.setSelected(0);
			distanceListView.setAdapter(distanceAdapter);
			distanceListView.setSelection(curIndex);

			if (gradeAdapter == null) {
				gradeAdapter = new CoachListAdapter(this, sortListByRate());
			} else {
				gradeAdapter.setData(sortListByRate());
			}
			if (gradeAdapter.getData().indexOf(selectCoach) == 0)
				gradeAdapter.setSelected(0);
			gradeListView.setAdapter(gradeAdapter);
			gradeListView.setSelection(curIndex);
		}
	}

	private List<CoachVO> sortListByDistance() {
		Comparator comp = new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				CoachVO c1 = (CoachVO) o1;
				CoachVO c2 = (CoachVO) o2;
				try {
					float distance1 = Float.parseFloat(c1.getDistance());
					float distance2 = Float.parseFloat(c2.getDistance());
					if (distance1 < distance2)
						return -1;
					else if (distance1 == distance2)
						return 0;
					else if (distance1 > distance2)
						return 1;
				} catch (Exception e) {
				}
				return 0;
			}
		};
		Collections.sort(distanceList, comp);
		//
		if (selectCoach != null) {
			int selectIndex = distanceList.indexOf(selectCoach);
			if (selectIndex >= 0) {
				distanceList.add(0, distanceList.get(selectIndex));
				distanceList.remove(selectIndex + 1);
			}
		}
		return distanceList;
	}

	private List<CoachVO> sortListByRate() {
		Comparator comp = new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				CoachVO c1 = (CoachVO) o1;
				CoachVO c2 = (CoachVO) o2;
				try {
					float rate1 = Float.parseFloat(c1.getStarlevel());
					float rate2 = Float.parseFloat(c2.getStarlevel());
					if (rate1 < rate2)
						return 1;
					else if (rate1 == rate2)
						return 0;
					else if (rate1 > rate2)
						return -1;
				} catch (Exception e) {
				}
				return -1;
			}
		};
		Collections.sort(gradeList, comp);
		//
		if (selectCoach != null) {
			int selectIndex = gradeList.indexOf(selectCoach);
			if (selectIndex >= 0) {
				gradeList.add(0, gradeList.get(selectIndex));
				gradeList.remove(selectIndex + 1);
			}
		}
		return gradeList;
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
			if (distanceAdapter == null || distanceAdapter.getIndex() < 0) {
				finish();
				break;
			}
			coach = distanceAdapter.getItem(distanceAdapter.getIndex());
			String checkResult = Util.isConfilctEnroll(coach);
			if (checkResult == null) {
				setResult(v.getId(), new Intent().putExtra("coach", coach));
				finish();
			} else if (checkResult.length() == 0) {
				app.selectEnrollCoach = coach;
				Util.updateEnrollCoach(this, coach, false);
				setResult(v.getId(), new Intent().putExtra("coach", coach));
				finish();
			} else if (checkResult.equals("refresh")) {
				app.selectEnrollCoach = coach;
				Util.updateEnrollCoach(this, coach, true);
				setResult(v.getId(), new Intent().putExtra("coach", coach));
				finish();
			} else {
				EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
						this, checkResult);
				dialog.show();
			}

			break;
		}
	}

	private void obtainNearByCoach() {
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("latitude", app.latitude);
		// paramMap.put("longitude", app.longtitude);
		paramMap.put("latitude", "40.096263");
		paramMap.put("longitude", "116.1270");
		paramMap.put("radius", "100000");
		HttpSendUtils.httpGetSend(nearbyCoach, this, Config.IP
				+ "api/v1/userinfo/nearbycoach", paramMap);
	}

	private void obtainSchoolCoach(int index) {
		HttpSendUtils.httpGetSend(schoolCoach, this, Config.IP
				+ "api/v1/getschoolcoach/" + schoolId + "/" + index);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			gradeListView.stopLoadMore();
			distanceListView.stopLoadMore();
			return true;
		}
		try {
			if (type.equals(schoolCoach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0) {
						coachIndex++;
					}
					int curIndex = distanceList.size();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						distanceList.add(coachVO);
						gradeList.add(coachVO);
					}
					setData(curIndex);
					gradeListView.stopLoadMore();
					distanceListView.stopLoadMore();
				}
			} else if (type.equals(nearbyCoach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						distanceList.add(coachVO);
						gradeList.add(coachVO);
					}
					setData(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, final int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				// 用户没有报名点击了回退键
				CoachVO coach = (CoachVO) data.getSerializableExtra("coach");
				if (app.userVO != null
						&& app.userVO.getApplystate().equals(
								EnrollResult.SUBJECT_NONE.getValue())
						&& coach != null) {
					int position = distanceAdapter.getData().indexOf(coach);
					distanceAdapter.setSelected(position);
					distanceAdapter.notifyDataSetChanged();
					position = gradeAdapter.getData().indexOf(coach);
					gradeAdapter.setSelected(position);
					gradeAdapter.notifyDataSetChanged();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CoachVO coachVO = null;
		if (viewPager.getCurrentItem() == 0) {
			coachVO = distanceAdapter.getItem(position - 1);
		} else if (viewPager.getCurrentItem() == 1) {
			coachVO = gradeAdapter.getItem(position - 1);
		}
		if (isfromtestingmsg) {
			Intent i = new Intent();
			i.putExtra("coach", coachVO);
			this.setResult(3, i);
			this.finish();
		} else {
			Intent intent = new Intent(this, CoachDetailActivity.class);
			intent.putExtra("coach", coachVO);
			startActivityForResult(intent, 0);
		}
	}

	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			radioGroup.setOnCheckedChangeListener(null);
			radioGroup.check(position == 0 ? R.id.selectcoach_distance_btn
					: R.id.selectcoach_grade_btn);
			radioGroup
					.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			viewPager.setOnPageChangeListener(null);
			if (checkedId == R.id.selectcoach_distance_btn) {
				viewPager.setCurrentItem(0);
			} else {
				viewPager.setCurrentItem(1);
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
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		obtainSchoolCoach(coachIndex);
	}

	@Override
	public void selectConfirm(boolean isConfirm, boolean isFreshAll) {
		if (isConfirm) {
			app.selectEnrollCoach = coach;
			Util.updateEnrollCoach(this, coach, isFreshAll);
			if (isFreshAll) {
				app.selectEnrollSchool = Util.getEnrollUserSelectedSchool(this);
				app.selectEnrollCarStyle = Util
						.getEnrollUserSelectedCarStyle(this);
				app.selectEnrollClass = Util.getEnrollUserSelectedClass(this);
			}
			setResult(R.id.base_right_tv, new Intent().putExtra("coach", coach));
			finish();
		}
	}

}
