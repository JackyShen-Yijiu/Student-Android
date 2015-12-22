package com.sft.blackcatapp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sft.fragment.MenuFragment;
import com.sft.fragment.MenuFragment.SLMenuListOnItemClickListener;
import com.sft.util.CommonUtil;
import com.sft.view.ColumnHorizontalScrollView;

public class MainActivity extends SlidingFragmentActivity implements
		SLMenuListOnItemClickListener, OnClickListener {
	private ImageView home_btn;
	private SlidingMenu mSlidingMenu;
	private RelativeLayout rl_column;
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRadioGroup_content;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	private LinearLayout ll_more_columns;
	private ImageView button_more_columns;
	private ViewPager mViewPager;
	// private ArrayList<ChannelItem> userChannelList = new
	// ArrayList<ChannelItem>();
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_content);
		initView();
		initTopMenu();
		// setChangelView();
		setListener();

	}

	private void initView() {
		// set the Behind View
		setBehindContentView(R.layout.frame_left_menu);
		home_btn = (ImageView) findViewById(R.id.home_btn);
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();

		// mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置左右都可以划出SlidingMenu菜单
		// mSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow);
		//
		// // mSlidingMenu.setShadowWidth(5);
		// // mSlidingMenu.setBehindOffset(100);
		// mSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
		// mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width); // 设置阴影图片的宽度
		// mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset); //
		// SlidingMenu划出时主页面显示的剩余宽度
		// mSlidingMenu.setFadeDegree(0.35f);
		// // 设置SlidingMenu 的手势模式
		// // TOUCHMODE_FULLSCREEN 全屏模式，在整个content页面中，滑动，可以打开SlidingMenu
		// // TOUCHMODE_MARGIN
		// // 边缘模式，在content页面中，如果想打开SlidingMenu,你需要在屏幕边缘滑动才可以打开SlidingMenu
		// // TOUCHMODE_NONE 不能通过手势打开SlidingMenu
		// mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

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

	private void initTopMenu() {

		mScreenWidth = CommonUtil.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		// rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		// mColumnHorizontalScrollView = (ColumnHorizontalScrollView)
		// findViewById(R.id.mColumnHorizontalScrollView);
		//
		// mRadioGroup_content = (LinearLayout)
		// findViewById(R.id.mRadioGroup_content);
		// shade_left = (ImageView) findViewById(R.id.shade_left);
		// shade_right = (ImageView) findViewById(R.id.shade_right);
		// ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		// button_more_columns = (ImageView)
		// findViewById(R.id.button_more_columns);
		// mViewPager = (ViewPager) findViewById(R.id.mViewPager);
	}

	private void setListener() {
		home_btn.setOnClickListener(this);
		// button_more_columns.setOnClickListener(this);
	}

	// /**
	// * 当栏目项发生变化时候调用
	// * */
	// private void setChangelView() {
	// // initColumnData();
	// // initTabColumn();
	// // initFragment();
	// }

	// /** 获取Column栏目 数据 */
	// private void initColumnData() {
	// userChannelList.add(new ChannelItem(1, "推荐", 1, 1));
	// userChannelList.add(new ChannelItem(2, "热点", 2, 1));
	// userChannelList.add(new ChannelItem(3, "杭州", 3, 1));
	// userChannelList.add(new ChannelItem(4, "时尚", 4, 1));
	// userChannelList.add(new ChannelItem(5, "科技", 5, 1));
	// userChannelList.add(new ChannelItem(6, "体育", 6, 1));
	// userChannelList.add(new ChannelItem(7, "军事", 7, 1));
	// userChannelList.add(new ChannelItem(8, "财经", 1, 0));
	// userChannelList.add(new ChannelItem(9, "汽车", 2, 0));
	// userChannelList.add(new ChannelItem(10, "房产", 3, 0));
	// userChannelList.add(new ChannelItem(11, "社会", 4, 0));
	// userChannelList.add(new ChannelItem(12, "情感", 5, 0));
	// }

	// /**
	// * 初始化Column栏目项
	// * */
	// private void initTabColumn() {
	// mRadioGroup_content.removeAllViews();
	// int count = userChannelList.size();
	// mColumnHorizontalScrollView.setParam(this, mScreenWidth,
	// mRadioGroup_content, shade_left, shade_right, ll_more_columns,
	// rl_column);
	// for (int i = 0; i < count; i++) {
	// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	// mItemWidth, LayoutParams.WRAP_CONTENT);
	// params.leftMargin = 5;
	// params.rightMargin = 5;
	// TextView columnTextView = new TextView(this);
	// columnTextView.setTextAppearance(this,
	// R.style.top_category_scroll_view_item_text);
	// columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
	// columnTextView.setGravity(Gravity.CENTER);
	// columnTextView.setPadding(5, 5, 5, 5);
	// columnTextView.setId(i);
	// columnTextView.setText(userChannelList.get(i).getName());
	// columnTextView.setTextColor(getResources().getColorStateList(
	// R.color.top_category_scroll_text_color_day));
	// if (columnSelectIndex == i) {
	// columnTextView.setSelected(true);
	// }
	// columnTextView.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
	// View localView = mRadioGroup_content.getChildAt(i);
	// if (localView != v)
	// localView.setSelected(false);
	// else {
	// localView.setSelected(true);
	// mViewPager.setCurrentItem(i);
	// }
	// }
	// Toast.makeText(getApplicationContext(),
	// userChannelList.get(v.getId()).getName(),
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	// mRadioGroup_content.addView(columnTextView, i, params);
	// }
	// }

	/**
	 * 选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	// /**
	// * 初始化Fragment
	// * */
	// private void initFragment() {
	// fragments.clear();// 清空
	// int count = userChannelList.size();
	// for (int i = 0; i < count; i++) {
	// Bundle data = new Bundle();
	// data.putString("text", userChannelList.get(i).getName());
	// data.putInt("id", userChannelList.get(i).getId());
	// NewsFragment newfragment = new NewsFragment();
	// newfragment.setArguments(data);
	// fragments.add(newfragment);
	// }
	// NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(
	// getSupportFragmentManager(), fragments);
	// mViewPager.setAdapter(mAdapetr);
	// mViewPager.setOnPageChangeListener(pageListener);
	// }

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};

	// 左侧菜单条目点击
	@Override
	public void selectItem(int position, String title) {
		Intent intent;
		switch (position) {
		case 0:
			Toast.makeText(getBaseContext(), "首页", 0).show();
			// intent = new Intent(this, SecondActivity.class);
			// startActivity(intent);
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
		// TODO Auto-generated method stub
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
		// case R.id.button_more_columns:
		// Intent intent_channel = new Intent(this, ChannelActivity.class);
		// startActivityForResult(intent_channel, CHANNELREQUEST);
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_left);
		// break;
		default:
			break;
		}
	}
}
