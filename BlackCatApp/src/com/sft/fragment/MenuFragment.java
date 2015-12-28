package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.sft.adapter.MyAdapter;
import com.sft.blackcatapp.EnrollSchoolActivity;
import com.sft.blackcatapp.MainActivity;
import com.sft.blackcatapp.MessageActivity;
import com.sft.blackcatapp.MyWalletActivity;
import com.sft.blackcatapp.PersonCenterActivity;
import com.sft.blackcatapp.R;
import com.sft.common.BlackCatApplication;
import com.sft.dialog.NoLoginDialog;

public class MenuFragment extends Fragment implements OnItemClickListener,
		OnClickListener {
	// private ListView listView;
	private ArrayList<HashMap<String, String>> mMenuTitles;
	private MyAdapter myAdapter;
	private SLMenuListOnItemClickListener mCallback;
	private int[] icons = new int[] { R.drawable.home, R.drawable.search_coach,
			R.drawable.messagelist, R.drawable.signin, R.drawable.market,
			R.drawable.me };
	private SelectableRoundedImageView personIcon;

	BlackCatApplication app;
	private TextView username;
	private TextView drivingSchool;
	private TextView code;
	private Context mContext;

	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (app == null) {
			app = BlackCatApplication.getInstance();
		}
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_menu_new, null);
		initView(rootView);
		// setData();
		setListener();
		return rootView;
	}

	private void initView(View rootView) {
		username = (TextView) rootView
				.findViewById(R.id.fragment_menu_username);
		drivingSchool = (TextView) rootView
				.findViewById(R.id.fragment_menu_driving_school);
		code = (TextView) rootView.findViewById(R.id.fragment_menu_code);
		personIcon = (SelectableRoundedImageView) rootView
				.findViewById(R.id.fragment_menu_headpic_im);
		personIcon.setScaleType(ScaleType.CENTER_CROP);
		personIcon.setImageResource(R.drawable.default_small_pic);
		personIcon.setOval(true);
		// listView.setCacheColorHint(android.R.color.transparent);
		// listView.setDividerHeight(0);
		// listView.setSelector(R.drawable.drawer_list_item_selector);
		RelativeLayout.LayoutParams headpicParam = (android.widget.RelativeLayout.LayoutParams) personIcon
				.getLayoutParams();
		if (app.isLogin) {

			String url = app.userVO.getHeadportrait().getOriginalpic();
			if (TextUtils.isEmpty(url)) {
				personIcon.setImageResource(R.drawable.default_small_pic);
			} else {
				BitmapManager.INSTANCE.loadBitmap2(url, personIcon,
						headpicParam.width, headpicParam.height);
			}
			username.setText(app.userVO.getName());
			drivingSchool.setText(app.userVO.getApplyschoolinfo().getName());
		}

		//
		rootView.findViewById(R.id.fragment_menu_home_btn).setOnClickListener(
				this);
		rootView.findViewById(R.id.fragment_menu_driving_school_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_message_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_mall_btn).setOnClickListener(
				this);
		rootView.findViewById(R.id.fragment_menu_activity_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_signin_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_setting_btn)
				.setOnClickListener(this);

	}

	// private void setData() {
	// mMenuTitles = new ArrayList<HashMap<String, String>>();
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("item", "首页");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "查找驾校");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "消息");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "钱包");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "我");
	// mMenuTitles.add(map);
	// myAdapter = new MyAdapter(getActivity(), mMenuTitles, icons);
	// listView.setAdapter(myAdapter);
	// }

	private void setListener() {
		// listView.setOnItemClickListener(this);
		personIcon.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, String> item = mMenuTitles.get(position);
		if (mCallback != null && item.containsKey("item")) {
			mCallback.selectItem(position, item.get("item"));
		}
	}

	/**
	 * 左侧菜单 点击回调接口
	 * 
	 * @author FX_SKY
	 * 
	 */
	public interface SLMenuListOnItemClickListener {

		public void selectItem(int position, String title);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.fragment_menu_headpic_im:
			if (app.isLogin) {
				intent = new Intent(getActivity(), PersonCenterActivity.class);
				startActivity(intent);

			} else {
				NoLoginDialog dialog = new NoLoginDialog(getActivity());
				dialog.show();
			}
			break;

		case R.id.fragment_menu_home_btn:
			intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			((Activity) mContext).finish();
			break;
		case R.id.fragment_menu_driving_school_btn:
			intent = new Intent(mContext, EnrollSchoolActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.fragment_menu_message_btn:
			if (app.isLogin) {
				intent = new Intent(mContext, MessageActivity.class);
				mContext.startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(mContext);
				dialog.show();
			}
			break;
		case R.id.fragment_menu_mall_btn:
			if (app.isLogin) {
				intent = new Intent(mContext, MyWalletActivity.class);
				mContext.startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(mContext);
				dialog.show();
			}
			break;
		case R.id.fragment_menu_activity_btn:

			break;
		case R.id.fragment_menu_signin_btn:

			break;
		case R.id.fragment_menu_setting_btn:

			break;
		default:
			break;
		}
	}
}
