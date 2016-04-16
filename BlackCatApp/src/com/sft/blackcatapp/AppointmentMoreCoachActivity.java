package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.sft.vo.CoachCourseV2VO;
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
	private static final String usefulcoachtimely = "usefulcoachtimely";
	private static final String usefulcoach = "usefulcoach";
	private XListView coachListView;
	private RelativeLayout layout;
	// 更多教练的页数
	private int moreCoachPage = 1;
	//
	private List<CoachVO> coachList = new ArrayList<CoachVO>();
	//
	private CoachListAdapter adapter;
	private String schoolId;

	private int usefulCoachIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_more_coach);
		initView();
		setListener();
		ZProgressHUD.getInstance(this).setMessage("拼命加载中...");
		ZProgressHUD.getInstance(this).show();
		coachCourse = (CoachCourseV2VO) getIntent().getSerializableExtra(
				"coachCourse");
		selectDate = getIntent().getStringExtra("selectDate");
		isFromApply = getIntent().getBooleanExtra("isFromApply", false);
		boolean isSchoolAllCoach = getIntent().getBooleanExtra(
				"isSchoolAllCoach", false);

		// 标题
		setTitleText(R.string.more_coach);
		if (isFromApply) {
			schoolId = getIntent().getStringExtra("schoolId");
			setTitleText(R.string.select_coach);
			headerRl.setVisibility(View.VISIBLE);
			LogUtil.print("xxxx-adfsf-" + schoolId);
			obtainSchoolCoach(moreCoachPage);
		} else {
			headerRl.setVisibility(View.GONE);
			if (null != coachCourse && (!TextUtils.isEmpty(selectDate))) {
				// 获取当前时间段可以预约的教练
				obtainUsefulcoachTimely();
			} else {
				if (isSchoolAllCoach) {
					schoolId = app.userVO.getApplyschoolinfo().getId();
					obtainSchoolCoach(moreCoachPage);
				} else {
					schoolId = app.userVO.getApplyschoolinfo().getId();
					obtainUsefulcoach();
				}

			}
		}

		if ((getIntent().getBooleanExtra("isOnClickToDetail", false))) {

			setTitleText(R.string.coach_list);
		}
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {

		headerRl = (RelativeLayout) findViewById(R.id.more_caoch_header);
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
		headerRl.setOnClickListener(this);
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
		case R.id.more_caoch_header:
			Intent intent = null;
			intent = new Intent();
			// intent.putExtra("coach", null);
			setResult(RESULT_OK, intent);
			finish();
			break;
		}

	}

	private void obtainSchoolCoach(int index) {
		LogUtil.print("xxxx--" + schoolId);
		HttpSendUtils.httpGetSend(schoolCoach, this, Config.IP
				+ "api/v1/getschoolcoach/" + schoolId + "/" + index);
	}

	// 获取当前时间段可以预约的教练
	private void obtainUsefulcoachTimely() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("coursedate", selectDate);
		paramMap.put("timeid", coachCourse.getTimeid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(usefulcoachtimely, this,
				Config.IP + "api/v1/userinfo/getusefulcoachtimely/index/"
						+ moreCoachPage, paramMap, 10000, headerMap);
		LogUtil.print("moreCoachPage---" + moreCoachPage);
	}

	// 获取我当前可以预约的教练
	private void obtainUsefulcoach() {
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("index", usefulCoachIndex + "");
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(usefulcoach, this, Config.IP
				+ "api/v1/userinfo/getusefulcoach/index/" + usefulCoachIndex,
				paramMap, 10000, headerMap);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if ((getIntent().getBooleanExtra("isOnClickToDetail", false))) {
			// 查看详情
			Intent intent2 = new Intent(this, CoachDetailActivity.class);
			intent2.putExtra("coach", adapter.getItem(position - 1));
			startActivity(intent2);
		} else {
			Intent intent = null;
			intent = new Intent();
			intent.putExtra("coach", adapter.getItem(position - 1));
			setResult(RESULT_OK, intent);
			finish();
		}

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
							coachList.clear();
						}
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
					coachListView.stopRefresh();
				}
			} else if (type.equals(usefulcoachtimely)) {
				if (dataArray != null) {
					int length = dataArray.length();
					LogUtil.print("length---" + length);
					if (length > 0) {

						if (moreCoachPage == 1) {
							ZProgressHUD.getInstance(
									AppointmentMoreCoachActivity.this)
									.dismiss();
							coachListView.setVisibility(View.VISIBLE);
							coachList.clear();
						}
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
					coachListView.stopRefresh();
				}
			} else if (type.equals(usefulcoach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					LogUtil.print("length---" + length);
					if (length > 0) {

						if (moreCoachPage == 1) {
							ZProgressHUD.getInstance(
									AppointmentMoreCoachActivity.this)
									.dismiss();
							coachListView.setVisibility(View.VISIBLE);
							coachList.clear();
						}
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
					coachListView.stopRefresh();
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
		moreCoachPage = 1;
		usefulCoachIndex = 1;
		if (isFromApply) {
			obtainSchoolCoach(moreCoachPage);
		} else {
			if (null != coachCourse && (!TextUtils.isEmpty(selectDate))) {
				// 获取当前时间段可以预约的教练
				obtainUsefulcoachTimely();
			} else {
				schoolId = app.userVO.getApplyschoolinfo().getId();
				obtainUsefulcoach();
			}
		}

	}

	@Override
	public void onLoadMore() {
		moreCoachPage++;
		usefulCoachIndex++;

		if (isFromApply) {
			obtainSchoolCoach(moreCoachPage);
		} else {
			if (null != coachCourse && (!TextUtils.isEmpty(selectDate))) {
				// 获取当前时间段可以预约的教练
				obtainUsefulcoachTimely();
			} else {
				schoolId = app.userVO.getApplyschoolinfo().getId();
				obtainUsefulcoach();
			}
		}
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
			// LogUtil.print(AppointmentMoreCoachActivity.class.getName() +
			// "---");
			startActivity(intent);
		}
	};
	private ImageView noCoachIv;
	private TextView noCoachTv;
	private CoachCourseV2VO coachCourse;
	private String selectDate;
	private RelativeLayout headerRl;
	private boolean isFromApply;
}
