package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.blackcatapp.R;
import com.sft.listener.AdapterRefreshListener;

/**
 * 我的喜欢
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("deprecation")
public class MyFavouriteActiviy extends BaseActivity {

	//
	private ViewPager viewPager;
	//
	private LocalActivityManager activityManager = null;
	//
	private RadioGroup radioGroup;

	//
	private RadioButton schoolBtn, coachBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_select_coach);
		initView();
		initData(savedInstanceState);
		setListener();
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.my_favourite);

		viewPager = (ViewPager) findViewById(R.id.selectcoach_viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.selectcoach_radiogroup);
		schoolBtn = (RadioButton) findViewById(R.id.selectcoach_grade_btn);
		coachBtn = (RadioButton) findViewById(R.id.selectcoach_distance_btn);
		schoolBtn.setText("驾校");
		coachBtn.setText("教练");
	}

	private void setListener() {
		radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	private void initData(Bundle savedInstanceState) {
		/*
		 * 在一个Activity的一部分中显示其他Activity”要用到LocalActivityManagerity
		 * 作用体现在manager获取View：manager.startActivity(String,
		 * Intent).getDecorView()
		 */
		activityManager = new LocalActivityManager(this, true);
		activityManager.dispatchCreate(savedInstanceState);

		// 加入2个子Activity
		Intent i1 = new Intent(this, FavouriteCoachActivity.class);
		Intent i2 = new Intent(this, FavouriteSchoolActivity.class);

		List<View> listViews = new ArrayList<View>(); // 实例化listViews
		listViews.add(activityManager.startActivity("FavouriteCoachActivity",
				i1).getDecorView());
		listViews.add(activityManager.startActivity("FavouriteSchoolActivity",
				i2).getDecorView());

		viewPager.setAdapter(new MyPageAdapter(listViews));
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
		case R.id.base_right_btn:
			break;
		}
	}

	@Override
	public void forOperResult(Intent intent) {
		util.print("forOperResult");
		boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
		if (isRefresh) {
			String activityName = intent.getStringExtra("activityName");
			if (activityName.contains(".")) {
				activityName = activityName.substring(activityName
						.lastIndexOf(".") + 1);
			}
			// 获取当前活动的Activity实例
			Activity subActivity = activityManager.getActivity(activityName);
			// 判断是否实现返回值接口
			if (subActivity instanceof AdapterRefreshListener) {
				// 获取返回值接口实例
				AdapterRefreshListener listener = (AdapterRefreshListener) subActivity;
				// 转发请求到子Activity
				listener.onDataChanged();
			}
			return;
		}
		if (intent.getSerializableExtra("coach") != null) {
			Intent intent2 = new Intent(this, CoachDetailActivity.class);
			intent2.putExtra("coach", intent.getSerializableExtra("coach"));
			startActivity(intent2);
		} else if (intent.getSerializableExtra("school") != null) {
			Intent intent2 = new Intent(this, SchoolDetailActivity.class);
			intent2.putExtra("school", intent.getSerializableExtra("school"));
			startActivity(intent2);
		}
	}

	private void setTabBkground(int index) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				// int currentItem = viewPager.getCurrentItem();
				// String activityName = "";
				// if (currentItem == 0) {
				// activityName = "FavouriteCoachActivity";
				// } else if (currentItem == 1) {
				// activityName = "FavouriteSchoolActivity";
				// }
				// Activity subActivity = activityManager
				// .getActivity(activityName);
				// if (subActivity instanceof OnTabActivityResultListener) {
				// // 获取返回值接口实例
				// OnTabActivityResultListener listener =
				// (OnTabActivityResultListener) subActivity;
				// // 转发请求到子Activity
				// listener.onTabActivityResult(requestCode, resultCode, data);
				// }
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(RESULT_OK, data);
					finish();
				}
			};
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
			setTabBkground(position);
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
				setTabBkground(0);
			} else {
				viewPager.setCurrentItem(1);
				setTabBkground(1);
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

}
