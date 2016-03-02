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

import com.jzjf.app.R;
import com.sft.adapter.SchoolDetailCoachHoriListAdapter;
import com.sft.common.Config;
import com.sft.listener.AdapterRefreshListener;
import com.sft.listener.OnTabActivityResultListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.CoachVO;

/**
 * 我喜欢的教练
 * 
 * @author Administrator
 * 
 */
public class FavouriteCoachActivity extends BaseActivity implements
		OnItemClickListener, AdapterRefreshListener,
		OnTabActivityResultListener {
	private static final String coach = "coach";
	private XListView listView;
	private RelativeLayout layout;
	private SchoolDetailCoachHoriListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_select_coach_distance);
		initView();
		initData();
		obtainFavouriteCoach();
	}

	private void obtainFavouriteCoach() {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(coach, this, Config.IP
				+ "api/v1/userinfo/favoritecoach", null, 10000, headerMap);
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

		layout.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		findViewById(R.id.select_coach_distance_devider_im).getLayoutParams().height = (int) (screenHeight * 0.2f);
		((TextView) findViewById(R.id.select_coach_distance_no_tv))
				.setText(R.string.no_favourite_coach);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(coach)) {
				if (dataArray != null) {
					List<CoachVO> list = new ArrayList<CoachVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						list.add(coachVO);
					}
					app.favouriteCoach = list;
					if (list.size() > 0) {
						layout.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
						adapter = new SchoolDetailCoachHoriListAdapter(this,
								app.favouriteCoach);
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
		Intent intent = new Intent(this, CoachDetailActivity.class);
		intent.putExtra("coach", adapter.getItem(position - 1));
		getParent().startActivityForResult(intent,
				R.layout.activity_select_coach_distance);
	}

	@Override
	public void onDataChanged() {
		if (adapter != null) {
			adapter.setData(app.favouriteCoach);
			adapter.notifyDataSetChanged();
		}
		if (app.favouriteCoach.size() == 0) {
			layout.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.VISIBLE);
			layout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		CoachVO coach = (CoachVO) data.getSerializableExtra("coach");
		if (coach != null) {
			adapter.setSelected(adapter.getData().indexOf(coach));
			adapter.notifyDataSetChanged();

			Util.updateEnrollCoach(this, coach);

		}
	};

}
