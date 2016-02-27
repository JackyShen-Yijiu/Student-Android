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

import com.sft.listener.AdapterRefreshListener;

@SuppressWarnings("deprecation")
public class WalletActivity extends BaseActivity {

	private RadioButton wallet_integral, wallet_coupons, wallet_money;
	private ViewPager viewPager;
	private RadioGroup radioGroup;

	private LocalActivityManager activityManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_wallet);
		initView();
		initData(savedInstanceState);
		setListener();
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
		Intent i1 = new Intent(this, Walletintegral.class);
		Intent i2 = new Intent(this, Walletcoupons.class);
		Intent i3 = new Intent(this, Walletmoney.class);

		List<View> listViews = new ArrayList<View>(); // 实例化listViews
		listViews.add(activityManager.startActivity("Walletintegral", i1)
				.getDecorView());
		listViews.add(activityManager.startActivity("Walletcoupons", i2)
				.getDecorView());
		listViews.add(activityManager.startActivity("Walletmoney", i3)
				.getDecorView());

		viewPager.setAdapter(new MyPageAdapter(listViews));
	}

	private void initView() {
		setTitleText(R.string.wallet);

		wallet_integral = (RadioButton) findViewById(R.id.wallet_integral_btn);
		wallet_coupons = (RadioButton) findViewById(R.id.wallet_coupons_btn);
		wallet_money = (RadioButton) findViewById(R.id.wallet_money_btn);

		viewPager = (ViewPager) findViewById(R.id.wallet_viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.wallet_radiogroup);

	}

	private void setListener() {
		radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	@Override
	public void forOperResult(Intent intent) {
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

	private void setTabBkground(int index) {

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
