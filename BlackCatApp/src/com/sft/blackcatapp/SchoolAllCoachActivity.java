package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

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
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachVO;

/**
 * 更多教练页面
 * 
 * @author Administrator
 * 
 */
public class SchoolAllCoachActivity extends BaseActivity implements
		OnItemClickListener {

	private static final String schoolCoach = "schoolCoach";
	private XListView coachListView;
	private RelativeLayout layout;
	// 更多教练的页数
	private int moreCoachPage = 1;
	//
	private List<CoachVO> coachList = new ArrayList<CoachVO>();
	//
	private CoachListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_more_coach);
		initView();
		setListener();
		obtainSchoolCoach(moreCoachPage);
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.coach_list);

		coachListView = (XListView) findViewById(R.id.more_caoch_listview);
		layout = (RelativeLayout) findViewById(R.id.more_caoch_no_layout);

		// layout.setVisibility(View.VISIBLE);
		ZProgressHUD.getInstance(this).setMessage("拼命加载中...");
		ZProgressHUD.getInstance(this).show();
		coachListView.setVisibility(View.GONE);

		findViewById(R.id.more_caoch_devider_im).getLayoutParams().height = (int) (screenHeight * 0.2f);
		((TextView) findViewById(R.id.more_coach_no_tv))
				.setText(R.string.no_favourite_coach);
	}

	private void setListener() {
		coachListView.setPullRefreshEnable(false);
		coachListView.setPullLoadEnable(false);
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

	private void obtainSchoolCoach(int index) {
		HttpSendUtils.httpGetSend(schoolCoach, this, Config.IP
				+ "api/v1/getschoolcoach/"
				+ app.userVO.getApplyschoolinfo().getId() + "/" + index);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, CoachDetailActivity.class);
		intent.putExtra("coach", adapter.getItem(position - 1));
		startActivity(intent);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(schoolCoach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0) {
						moreCoachPage++;
						// layout.setVisibility(View.GONE);
						ZProgressHUD.getInstance(SchoolAllCoachActivity.this)
								.dismiss();
						coachListView.setVisibility(View.VISIBLE);
					}
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						coachList.add(coachVO);
					}
					if (adapter == null) {
						adapter = new CoachListAdapter(this, coachList);
					} else {
						adapter.setData(coachList);
					}
					coachListView.setAdapter(adapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
