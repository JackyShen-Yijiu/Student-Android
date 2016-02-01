package com.sft.blackcatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sft.fragment.CoachsFragment1;
import com.sft.fragment.SchoolsFragment;
import com.sft.util.LogUtil;

/**
 * 选择驾校界面
 * 
 * @author Administrator
 * 
 */
public class EnrollSchoolActivity1 extends FragmentActivity implements
		OnClickListener {

	/*** 选择驾校 */
	int selected;

	SchoolsFragment schoolFragment = null;

	CoachsFragment1 coachFragment = null;
	/** 查找驾校 */
	private int type = 0;

	public EditText etSearch;

	private TextView tvTitle;
	private TextView tvRight;

	private boolean isClassSelected = false;
	private boolean isSearchSchool = false;
	private TextView classSelect;
	private TextView distanceSelect;
	private TextView commentSelect;
	private TextView priceSelect;
	private ImageView arrow1, arrow2, arrow3, arrow4;

	//

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		selected = getIntent().getIntExtra("select", 0);
		LogUtil.print("selected----" + selected);
		setContentView(R.layout.act_enrollschool_container);
		initView(type);
		initData();

		if (selected == 1) {// 隐藏找教练
			tvRight.setVisibility(View.GONE);
		} else {
			tvRight.setVisibility(View.VISIBLE);
		}
	}

	private void initView(int flag) {

		tvTitle = ((TextView) findViewById(R.id.base_title_tv));

		etSearch = (EditText) findViewById(R.id.enroll_school_search_et);
		tvRight = ((TextView) findViewById(R.id.base_right_tv));
		tvRight.setClickable(true);
		tvRight.setOnClickListener(this);
		classSelect = (TextView) findViewById(R.id.enroll_school_class_select_tv);
		distanceSelect = (TextView) findViewById(R.id.enroll_school_distance_select_tv);
		commentSelect = (TextView) findViewById(R.id.enroll_school_comment_select_tv);
		priceSelect = (TextView) findViewById(R.id.enroll_school_price_select_tv);
		arrow1 = (ImageView) findViewById(R.id.enroll_school_arrow1_iv);
		arrow2 = (ImageView) findViewById(R.id.enroll_school_arrow2_iv);
		arrow3 = (ImageView) findViewById(R.id.enroll_school_arrow3_iv);
		arrow4 = (ImageView) findViewById(R.id.enroll_school_arrow4_iv);

		ImageButton imgLeft = (ImageButton) findViewById(R.id.base_left_btn);
		imgLeft.setBackgroundResource(R.drawable.base_left_btn_bkground);
		imgLeft.setOnClickListener(this);

		// setTitleText(R.string.select_school);
		// setRightText("定位中");

		FragmentTransaction tran = getSupportFragmentManager()
				.beginTransaction();
		if (flag == 0) {// 驾校
			if (schoolFragment == null)
				schoolFragment = SchoolsFragment.getInstance(selected);
			tran.add(R.id.fl_container, schoolFragment);
			tvRight.setText("找教练");
			tvTitle.setText(R.string.select_school);
		} else {// 教练
			if (coachFragment == null)
				coachFragment = CoachsFragment1.getInstance();
			tran.add(R.id.fl_container, coachFragment);
			tvRight.setText("找驾校");
			tvTitle.setText(R.string.search_coach);
		}

		tran.commitAllowingStateLoss();
	}

	private void initData() {
		etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		etSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) etSearch.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(EnrollSchoolActivity1.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 实现搜索
					LogUtil.print("搜索");
					String coachname = etSearch.getText().toString().trim();
					searchcoach(coachname);
					return true;
				}
				return false;
			}

		});
	}

	/**
	 * 切换 教练/驾校
	 */
	private void change() {
		FragmentTransaction tran = getSupportFragmentManager()
				.beginTransaction();

		if (type == 0) {// 切换到教练
			type = 1;
			if (coachFragment == null)
				coachFragment = CoachsFragment1.getInstance();
			// if(coachFragment.isAdded())
			// tran.show(coachFragment);
			tran.replace(R.id.fl_container, coachFragment);
			tvRight.setText("找驾校");
			tvTitle.setText(R.string.search_coach);
		} else {// 切换到驾校
			if (schoolFragment == null)
				schoolFragment = SchoolsFragment.getInstance(selected);
			type = 0;
			tran.replace(R.id.fl_container, schoolFragment);
			tvRight.setText("找教练");
			tvTitle.setText(R.string.select_school);
		}
		tran.commitAllowingStateLoss();
	}

	private void searchcoach(String name) {
		if (type == 0) {
			schoolFragment.schoolname = name;
			schoolFragment.searchSchool(true);
		} else if (type == 1) {
			coachFragment.coachname = name;
			coachFragment.searchcoach(true);
		}
	}

	@Override
	protected void onResume() {
		// register(getClass().getName());
		super.onResume();
	};

	@Override
	public void onClick(View v) {
		// if (!onClickSingleView()) {
		// return;
		// }
		int id = v.getId();
		switch (id) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			change();
			break;
		case R.id.enroll_school_class_select_tv:// 班型选择
			showPopupWindow(v);
			break;
		case R.id.enroll_school_distance_select_tv:// 距离最近
			setSelectState(2);
			onClickFragment(id);
			break;
		case R.id.enroll_school_comment_select_tv:// 评分最高
			setSelectState(3);
			onClickFragment(id);
			break;
		case R.id.enroll_school_price_select_tv:// 价格最低
			setSelectState(4);
			onClickFragment(id);
			break;
		case R.id.pop_window_one:
			setSelectState(1);
			onClickFragment(id);
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		case R.id.pop_window_two:
			setSelectState(1);
			onClickFragment(id);
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		}
	}

	private void onClickFragment(int id) {
		if (type == 0) {// 驾校
			schoolFragment.order(id);
		} else {
			coachFragment.order(id);
		}

	}

	private PopupWindow popupWindow;

	/**
	 * 班型选择
	 * 
	 * @param parent
	 */
	private void showPopupWindow(View parent) {
		if (popupWindow == null) {
			View view = View.inflate(this, R.layout.pop_window, null);

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
		// WindowManager windowManager = (WindowManager)
		// getSystemService(Context.WINDOW_SERVICE);
		// int xPos = -popupWindow.getWidth() / 2
		// + getCustomTitle().getCenter().getWidth() / 2;

		popupWindow.showAsDropDown(parent);
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

	@Override
	protected void onActivityResult(int requestCode, final int resultCode,
			final Intent data) {
		if (type == 0) {
			schoolFragment.onActivityResult(requestCode, resultCode, data);
		} else {
			coachFragment.onActivityResult(requestCode, resultCode, data);
		}

	}

}
