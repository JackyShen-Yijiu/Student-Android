package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.CoachListAdapter;
import com.sft.adapter.SchoolDetailCourseFeeAdapter.MyClickListener;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachVO;

/**
 * 更多教练页面
 * 
 * @author Administrator
 * 
 */
public class AppointmentMoreCoachActivity extends BaseActivity implements
		OnItemClickListener, IXListViewListener {

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
		ZProgressHUD.getInstance(this).setMessage("拼命加载中...");
		ZProgressHUD.getInstance(this).show();
		obtainSchoolCoach(moreCoachPage);
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.more_coach);

		coachListView = (XListView) findViewById(R.id.more_caoch_listview);
		layout = (RelativeLayout) findViewById(R.id.more_caoch_no_layout);
		noCoachIv = (ImageView) findViewById(R.id.more_caoch_no_im);
		noCoachTv = (TextView) findViewById(R.id.more_coach_no_tv);
		// layout.setVisibility(View.VISIBLE);

		coachListView.setVisibility(View.GONE);

		((TextView) findViewById(R.id.more_coach_no_tv))
				.setText(R.string.no_favourite_coach);
	}

	private void setListener() {
		coachListView.setPullRefreshEnable(false);
		coachListView.setPullLoadEnable(true);
		coachListView.setOnItemClickListener(this);
		coachListView.setXListViewListener(this);
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
		Intent intent = null;
		intent = new Intent();
		intent.putExtra("coach", adapter.getItem(position - 1));
		setResult(RESULT_OK, intent);
		finish();

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

						if (moreCoachPage == 1) {
							ZProgressHUD.getInstance(
									AppointmentMoreCoachActivity.this)
									.dismiss();
							coachListView.setVisibility(View.VISIBLE);
						}
						moreCoachPage++;
					} else if (length == 0) {
						if (moreCoachPage == 1) {
							coachListView.setVisibility(View.GONE);
							layout.setVisibility(View.VISIBLE);
						} else {

							toast.setText("没有更多数据了");
							coachListView.setPullLoadEnable(false);
						}
					}
					int curLength = coachList.size();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						// if (app.favouriteCoach.contains(coachVO))
						// continue;
						coachList.add(coachVO);
					}
					if (adapter == null) {
						adapter = new CoachListAdapter(this, coachList,
								mListener);
					} else {
						adapter.setData(coachList);
					}
					coachListView.setAdapter(adapter);
					coachListView.setSelection(curLength);
					coachListView.stopLoadMore();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void doException(String type, Exception e, int code) {
		ZProgressHUD.getInstance(this).dismiss();
		layout.setVisibility(View.VISIBLE);
		coachListView.setVisibility(View.GONE);
		noCoachIv.setBackgroundResource(R.drawable.app_no_wifi);
		noCoachTv.setText(CommonUtil.getString(this, R.string.no_wifi));

	}

	@Override
	public void doTimeOut(String type) {
		ZProgressHUD.getInstance(this).dismiss();
		layout.setVisibility(View.VISIBLE);
		coachListView.setVisibility(View.GONE);
		noCoachIv.setBackgroundResource(R.drawable.app_no_wifi);
		noCoachTv.setText(CommonUtil.getString(this, R.string.no_wifi));
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		obtainSchoolCoach(moreCoachPage);
	}

	/**
	 * 实现类，响应按钮点击事件
	 */
	private MyClickListener mListener = new MyClickListener() {
		@Override
		public void myOnClick(int position, View v) {
			Intent intent = new Intent(AppointmentMoreCoachActivity.this,
					CoachDetailActivity.class);
			intent.putExtra("coach", coachList.get(position));
			intent.putExtra("where",
					AppointmentMoreCoachActivity.class.getName());
//			LogUtil.print(AppointmentMoreCoachActivity.class.getName() + "---");
			startActivity(intent);
		}
	};
	private ImageView noCoachIv;
	private TextView noCoachTv;
}
