package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.sft.listener.ICallBack;

import com.sft.common.BlackCatApplication;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.QuestionVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.UserVO;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends cn.sft.baseactivity.base.BaseActivity
		implements OnClickListener, ICallBack {

	public static final int SHOW_LEFT_BTN = 1;
	public static final int SHOW_RIGHT_BTN = 2;
	public static final int SHOW_LEFT_TEXT = 1;
	public static final int SHOW_RIGHT_TEXT = 2;

	// 标题栏
	protected LinearLayout titlebarLayout;
	// 标题栏左边按钮
	private ImageButton leftBtn;
	// 标题栏右边按钮
	private ImageButton rightBtn;
	// 标题栏左边文本
	private TextView leftTV;
	// 标题栏右边文本
	protected TextView rightTV;
	// 标题栏标题
	private TextView titleTV;
	// 内容布局
	private LinearLayout contentLayout;
	// action
	public static String action;

	protected BlackCatApplication app;

	protected String result = "";
	protected String msg = "";
	protected JSONObject data = null;
	protected JSONArray dataArray = null;
	protected String dataString = null;
	protected JSONObject jsonObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			if (app == null) {
				app = BlackCatApplication.getInstance();
			}
			app.userVO = (UserVO) savedInstanceState.getSerializable("userVO");
			app.questionVO = (QuestionVO) savedInstanceState
					.getSerializable("questionVO");
			app.subjectTwoContent = savedInstanceState
					.getStringArrayList("subjectTwoContent");
			app.subjectThreeContent = savedInstanceState
					.getStringArrayList("subjectThreeContent");
			app.selectEnrollSchool = (SchoolVO) savedInstanceState
					.getSerializable("selectEnrollSchool");
			app.selectEnrollCoach = (CoachVO) savedInstanceState
					.getSerializable("selectEnrollCoach");
			app.selectEnrollCarStyle = (CarModelVO) savedInstanceState
					.getSerializable("selectEnrollCarStyle");
			app.selectEnrollClass = (ClassVO) savedInstanceState
					.getSerializable("selectEnrollClass");
			app.userVO = (UserVO) savedInstanceState.getSerializable("userVO");
			app.qiniuToken = savedInstanceState.getString("qiniuToken");

			Bundle favouriteCoach = savedInstanceState
					.getBundle("favouriteCoach");
			if (favouriteCoach != null) {
				Set<String> favouriteCoachKey = favouriteCoach.keySet();
				if (app.favouriteCoach == null)
					app.favouriteCoach = new ArrayList<CoachVO>();
				for (String key : favouriteCoachKey) {
					app.favouriteCoach.add((CoachVO) favouriteCoach.get(key));
				}
			}

			Bundle favouriteSchool = savedInstanceState
					.getBundle("favouriteSchool");
			if (favouriteSchool != null) {
				Set<String> favouriteSchoolKey = favouriteSchool.keySet();
				if (app.favouriteSchool == null)
					app.favouriteSchool = new ArrayList<SchoolVO>();
				for (String key : favouriteSchoolKey) {
					app.favouriteSchool
							.add((SchoolVO) favouriteSchool.get(key));
				}
			}

			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.activity_base);

		titlebarLayout = (LinearLayout) findViewById(R.id.base_titlebar_layout);
		leftBtn = (ImageButton) findViewById(R.id.base_left_btn);
		rightBtn = (ImageButton) findViewById(R.id.base_right_btn);
		leftTV = (TextView) findViewById(R.id.base_left_tv);
		rightTV = (TextView) findViewById(R.id.base_right_tv);
		titleTV = (TextView) findViewById(R.id.base_title_tv);
		// 中文字体加粗
		titleTV.getPaint().setFakeBoldText(true);

		contentLayout = (LinearLayout) findViewById(R.id.base_content_layout);

		showTitlebarText(0);
		showTitlebarBtn(SHOW_LEFT_BTN);
		setBtnBkground(R.drawable.base_left_btn_bkground, 0);

		setListener();

		// util.setShowLogPrint(false);
		if (app == null) {
			app = BlackCatApplication.getInstance();
		}
	}

	private void setListener() {
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		leftTV.setOnClickListener(this);
		rightTV.setOnClickListener(this);
	}

	@Override
	public void register(String action) {
		super.register(action);
		BaseActivity.action = action;
	}

	protected void setTitleBarVisible(int index) {
		titlebarLayout.setVisibility(index);
	}

	protected void showTitlebarText(int index) {
		leftTV.setVisibility(View.GONE);
		rightTV.setVisibility(View.GONE);
		switch (index) {
		case 1:
			leftTV.setVisibility(View.VISIBLE);
			break;
		case 2:
			rightTV.setVisibility(View.VISIBLE);
			break;
		case 3:
			leftTV.setVisibility(View.VISIBLE);
			rightTV.setVisibility(View.VISIBLE);
			break;
		}
	}

	protected void setText(String... ids) {
		if (ids == null || ids.length != 2) {
			return;
		}
		leftTV.setText(ids[0]);
		rightTV.setText(ids[1]);
	}

	protected void setText(int... ids) {
		if (ids == null || ids.length != 2) {
			return;
		}
		if (ids[0] > 0)
			leftTV.setText(ids[0]);
		if (ids[1] > 0)
			rightTV.setText(ids[1]);
	}

	protected void setRightText(String name) {
		Drawable cityIcon = getResources()
				.getDrawable(R.drawable.location_city);
		rightTV.setCompoundDrawablesWithIntrinsicBounds(cityIcon, null, null,
				null);
		rightTV.setText(name);
	}

	protected void showTitlebarBtn(int index) {
		leftBtn.setVisibility(View.GONE);
		rightBtn.setVisibility(View.GONE);
		switch (index) {
		case 1:
			leftBtn.setVisibility(View.VISIBLE);
			break;
		case 2:
			rightBtn.setVisibility(View.VISIBLE);
			break;
		case 3:
			leftBtn.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			break;
		}
	}

	protected void setBtnBkground(int... ids) {
		if (ids == null || ids.length != 2) {
			return;
		}
		if (ids[0] > 0)
			leftBtn.setBackgroundResource(ids[0]);
		if (ids[1] > 0)
			rightBtn.setBackgroundResource(ids[1]);
	}

	protected void setTitleText(int stringId) {
		titleTV.setText(getString(stringId));
	}

	protected void setTitleText(String s) {
		titleTV.setText(s);
	}

	protected void addView(int layoutId) {
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LayoutInflater inflater = getLayoutInflater();
		contentLayout.addView(inflater.inflate(layoutId, null), params);
	}

	protected SpannableString setHint(String string) {
		SpannableString name = new SpannableString(string);
		name.setSpan(new AbsoluteSizeSpan(15, true), 0, name.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return name;
	}

	protected SpannableString setHint(int stringId) {
		SpannableString name = new SpannableString(getString(stringId));
		name.setSpan(new AbsoluteSizeSpan(15, true), 0, name.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return name;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		try {
			util.print("json=" + jsonString + " type= " + type);

			jsonObject = new JSONObject(jsonString.toString());
			result = jsonObject.getString("type");
			msg = jsonObject.getString("msg");
			try {
				data = jsonObject.getJSONObject("data");
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

		if (!TextUtils.isEmpty(msg)) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(msg, 2000);
			return true;
		}

		return false;
	}

	@Override
	public void doException(String type, Exception e, int code) {
		util.print("type= " + type + " 异常  code=" + code);
		if (code == 0) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("网络异常", 2000);
		} else if (code == 500) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("服务器异常", 2000);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(
					"type= " + type + " 异常  code=" + code, 2000);
		}
		if (e != null)
			e.printStackTrace();
	}

	@Override
	public void doTimeOut(String type) {
		util.print("type=" + type + " 超时");
		ZProgressHUD.getInstance(this).show();
		ZProgressHUD.getInstance(this).dismissWithFailure(
				"type=" + type + " 超时");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 友盟session统计
		MobclickAgent.onResume(this);

		// 极光统计
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 友盟session统计
		MobclickAgent.onPause(this);
		// 极光统计
		JPushInterface.onPause(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			outState.putSerializable("userVO", app.userVO);
			outState.putSerializable("questionVO", app.questionVO);

			int length = app.favouriteCoach.size();
			Bundle bundle = new Bundle();
			for (int i = 0; i < length; i++) {
				bundle.putSerializable("favouriteCoach" + i,
						app.favouriteCoach.get(i));
			}
			outState.putBundle("favouriteCoach", bundle);

			bundle = new Bundle();
			length = app.favouriteSchool.size();
			for (int i = 0; i < length; i++) {
				bundle.putSerializable("favouriteSchool" + i,
						app.favouriteSchool.get(i));
			}
			outState.putBundle("favouriteSchool", bundle);

			outState.putStringArrayList("subjectTwoContent",
					(ArrayList<String>) app.subjectTwoContent);
			outState.putStringArrayList("subjectThreeContent",
					(ArrayList<String>) app.subjectThreeContent);
			outState.putSerializable("selectEnrollSchool",
					app.selectEnrollSchool);
			outState.putSerializable("selectEnrollCoach", app.selectEnrollCoach);
			outState.putSerializable("selectEnrollCarStyle",
					app.selectEnrollCarStyle);
			outState.putSerializable("selectEnrollClass", app.selectEnrollClass);
			outState.putString("qiniuToken", app.qiniuToken);
		} catch (Exception e) {

		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

}
