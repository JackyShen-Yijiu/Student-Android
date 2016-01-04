package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.SchoolListAdapter;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.listener.AdapterRefreshListener;
import com.sft.listener.OnTabActivityResultListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.SchoolVO;

/**
 * 我喜欢的教练
 * 
 * @author Administrator
 * 
 */
public class FavouriteSchoolActivity extends BaseActivity implements
		OnItemClickListener, AdapterRefreshListener,
		OnTabActivityResultListener {

	private static final String school = "school";
	private XListView listView;
	private RelativeLayout layout;
	private SchoolListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_select_coach_distance);
		initView();
		initData();
		obtainFavouriteSchool();
	}

	private void obtainFavouriteSchool() {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(school, this, Config.IP
				+ "api/v1/userinfo/favoriteschool", null, 10000, headerMap);
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleBarVisible(View.GONE);
		setTitleText(R.string.distance);

		listView = (XListView) findViewById(R.id.select_coach_distance_listview);
		layout = (RelativeLayout) findViewById(R.id.select_coach_distance_layout);
	}

	private void initData() {
		listView.setPullRefreshEnable(false);
		listView.setPullLoadEnable(false);
		listView.setOnItemClickListener(this);

		findViewById(R.id.select_coach_distance_devider_im).getLayoutParams().height = (int) (screenHeight * 0.2f);
		((TextView) findViewById(R.id.select_coach_distance_no_tv))
				.setText(R.string.no_favourite_school);

		layout.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(school)) {
				if (dataArray != null) {
					List<SchoolVO> list = new ArrayList<SchoolVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						SchoolVO schoolVO = (SchoolVO) JSONUtil.toJavaBean(
								SchoolVO.class, dataArray.getJSONObject(i));
						list.add(schoolVO);
					}
					app.favouriteSchool = list;
					if (list.size() > 0) {
						layout.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
						adapter = new SchoolListAdapter(this, list);
						listView.setAdapter(adapter);
					} else {
						layout.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
					}
				}
			}
		} catch (Exception e) {

		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, SchoolDetailActivity.class);
		intent.putExtra("school", (SchoolVO) adapter.getItem(position - 1));
		getParent().startActivityForResult(intent,
				R.layout.activity_select_coach_distance);
	}

	@Override
	public void onDataChanged() {
		if (adapter != null) {
			adapter.setData(app.favouriteSchool);
			adapter.notifyDataSetChanged();
		}
		if (app.favouriteSchool.size() == 0) {
			layout.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.VISIBLE);
			layout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		SchoolVO school = (SchoolVO) data.getSerializableExtra("school");
		if (school != null) {
			adapter.setSelected(adapter.getData().indexOf(school));
			adapter.notifyDataSetChanged();
			Util.updateEnrollSchool(this, school);
		}
	};

}
