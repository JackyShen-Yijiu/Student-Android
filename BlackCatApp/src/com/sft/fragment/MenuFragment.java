package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.adapter.MyAdapter;
import com.sft.blackcatapp.R;
import com.sft.common.BlackCatApplication;

public class MenuFragment extends Fragment implements OnItemClickListener {
	private ListView listView;
	private ArrayList<HashMap<String, String>> mMenuTitles;
	private MyAdapter myAdapter;
	private SLMenuListOnItemClickListener mCallback;
	private int[] icons = new int[] { R.drawable.home, R.drawable.search_coach,
			R.drawable.messagelist, R.drawable.signin, R.drawable.market,
			R.drawable.me };
	private ImageView personIcon;

	BlackCatApplication app;

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
		View rootView = inflater.inflate(R.layout.fragment_menu, null);
		initView(rootView);
		setData();
		setListener();
		return rootView;
	}

	private void initView(View rootView) {
		listView = (ListView) rootView.findViewById(R.id.left_menu);
		personIcon = (ImageView) rootView
				.findViewById(R.id.fragment_menu_headpic_im);
		listView.setCacheColorHint(android.R.color.transparent);
		listView.setDividerHeight(0);
		listView.setSelector(R.drawable.drawer_list_item_selector);
		RelativeLayout.LayoutParams headpicParam = (android.widget.RelativeLayout.LayoutParams) personIcon
				.getLayoutParams();

		String url = app.userVO.getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			personIcon.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, personIcon,
					headpicParam.width, headpicParam.height);
		}

	}

	private void setData() {
		mMenuTitles = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("item", "首页");
		mMenuTitles.add(map);
		map = new HashMap<String, String>();
		map.put("item", "查找教练");
		mMenuTitles.add(map);
		map = new HashMap<String, String>();
		map.put("item", "消息");
		mMenuTitles.add(map);
		map = new HashMap<String, String>();
		map.put("item", "签到");
		mMenuTitles.add(map);
		map = new HashMap<String, String>();
		map.put("item", "商城");
		mMenuTitles.add(map);
		map = new HashMap<String, String>();
		map.put("item", "我");
		mMenuTitles.add(map);
		myAdapter = new MyAdapter(getActivity(), mMenuTitles, icons);
		listView.setAdapter(myAdapter);
	}

	private void setListener() {
		listView.setOnItemClickListener(this);
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
}
