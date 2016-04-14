package com.sft.blackcatapp;

import java.util.ArrayList;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jzjf.app.R;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MyOrderActivity extends BaseActivity {

	private TextView integralTv;
	private TextView coinTv;
	private TextView applyTv;
	private View line;
	private ViewPager viewPager;
	private ArrayList<View> listViews;
	private int line_width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_my_order);
		setTitleText("我的订单");
		initView();
		initData(savedInstanceState);
		setListener();
	}

	private void initView() {

		integralTv = (TextView) findViewById(R.id.order_integral);
		coinTv = (TextView) findViewById(R.id.order_coin);
		applyTv = (TextView) findViewById(R.id.order_apply);
		line = findViewById(R.id.line);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void initData(Bundle savedInstanceState) {

		/*
		 * 在一个Activity的一部分中显示其他Activity”要用到LocalActivityManagerity
		 * 作用体现在manager获取View：manager.startActivity(String,
		 * Intent).getDecorView()
		 */
		LocalActivityManager activityManager = new LocalActivityManager(this,
				true);
		activityManager.dispatchCreate(savedInstanceState);

		// 加入2个子Activity
		Intent i1 = new Intent(this, OrderExchangeGoodAct.class);
		i1.putExtra("hasActionBar", false);
		Intent i2 = new Intent(this, RecordActivity.class);
		i2.putExtra("hasActionBar", false);
		Intent i3 = new Intent(this, OrderApplyAct.class);
		i3.putExtra("hasActionBar", false);

		listViews = new ArrayList<View>();
		listViews.add(activityManager.startActivity("integralOrder", i1)
				.getDecorView());

		listViews.add(activityManager.startActivity("coinOrder", i2)
				.getDecorView());

		listViews.add(activityManager.startActivity("applyOrder", i3)
				.getDecorView());

		// fragments = new ArrayList<Fragment>();
		// fragments.add(new IntegralMallFragment());
		// fragments.add(new SubjectTwoFragment());

		line_width = this.getWindowManager().getDefaultDisplay().getWidth()
				/ listViews.size();
		line.getLayoutParams().width = line_width;
		line.requestLayout();

		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup view, int position, Object arg2) {
				ViewPager pViewPager = ((ViewPager) view);
				pViewPager.removeView(listViews.get(position));
			}

			@Override
			public void finishUpdate(View arg0) {
			}

			@Override
			public int getCount() {
				return listViews.size();
			}

			@Override
			public Object instantiateItem(ViewGroup view, int position) {
				ViewPager pViewPager = ((ViewPager) view);
				pViewPager.addView(listViews.get(position));
				return listViews.get(position);
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
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				changeState(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				float tagerX = arg0 * line_width + arg2 / listViews.size();
				ViewPropertyAnimator.animate(line).translationX(tagerX)
						.setDuration(0);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void setListener() {
		integralTv.setOnClickListener(this);
		coinTv.setOnClickListener(this);
		applyTv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_integral:
			viewPager.setCurrentItem(0);
			break;
		case R.id.order_coin:
			viewPager.setCurrentItem(1);
			break;
		case R.id.order_apply:
			viewPager.setCurrentItem(2);
			break;
		case R.id.base_left_btn:
			finish();
			break;
		default:
			break;
		}
	}

	private void changeState(int position) {
		integralTv.setTextColor(getResources().getColor(
				R.color.study_text_normal));
		coinTv.setTextColor(getResources().getColor(R.color.study_text_normal));
		applyTv.setTextColor(getResources().getColor(R.color.study_text_normal));
		switch (position) {
		case 0:
			integralTv.setTextColor(getResources().getColor(
					R.color.study_text_selected));

			break;
		case 1:
			coinTv.setTextColor(getResources().getColor(
					R.color.study_text_selected));
		case 2:
			applyTv.setTextColor(getResources().getColor(
					R.color.study_text_selected));

		default:
			break;
		}

	}
}
