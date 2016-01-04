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

import com.sft.adapter.CoachListAdapter;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.CoachVO;

/**
 * 我的教练
 * 
 * @author Administrator
 * 
 */
public class MyCoachActivity extends BaseActivity implements
		OnItemClickListener {
	private static final String myCoach = "myCoach";
	//
	private XListView coachListView;
	//
	private RelativeLayout noCoachLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_my_coach);
		initView();
		obtainMyCoach();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.my_coach);

		coachListView = (XListView) findViewById(R.id.my_coach_listview);
		noCoachLayout = (RelativeLayout) findViewById(R.id.my_coach_layout);

		findViewById(R.id.my_coach_devider_im).getLayoutParams().height = (int) (screenHeight * 0.2f);
		((TextView) findViewById(R.id.my_coach_no_tv))
				.setText(R.string.no_coach);

		coachListView.setPullLoadEnable(false);
		coachListView.setPullRefreshEnable(false);
		coachListView.setOnItemClickListener(this);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, CoachDetailActivity.class);
		intent.putExtra("coach", (CoachVO) parent.getAdapter()
				.getItem(position));
		startActivity(intent);
	}

	private void obtainMyCoach() {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myCoach, this, Config.IP
				+ "api/v1/userinfo/getmycoachlist", null, 10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(myCoach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if(length>0){
						coachListView.setVisibility(View.VISIBLE);
						noCoachLayout.setVisibility(View.GONE);
						List<CoachVO> list = new ArrayList<CoachVO>();
						for (int i = 0; i < length; i++) {
							CoachVO coachVO = (CoachVO) JSONUtil.toJavaBean(
									CoachVO.class, dataArray.getJSONObject(i));
							list.add(coachVO);
						}
						CoachListAdapter adapter = new CoachListAdapter(this,
								list);
						coachListView.setAdapter(adapter);
					}
				}
			}
		} catch (Exception e) {

		}
		return true;
	}
}
