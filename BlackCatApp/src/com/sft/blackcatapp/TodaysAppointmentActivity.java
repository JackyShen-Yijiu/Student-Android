package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.TodayAppointmentAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;

public class TodaysAppointmentActivity extends BaseActivity implements
		OnItemClickListener, OnRefreshListener {

	private static final String reservation = "reservation";
	private SwipeRefreshLayout swipeLayout;
	private ListView mListView;
	private TodayAppointmentAdapter adapter;
	private boolean isRefreshing = false;
	private List<MyAppointmentVO> mList = new ArrayList<MyAppointmentVO>();
	private RelativeLayout order_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_today_appointment);
		initView();
		obtainOppointment();
	}

	private void initView() {
		setTitleText(R.string.today_appointment);
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.today_appointment_swipe_container);
		order_ll = (RelativeLayout) findViewById(R.id.order_ll);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mListView = (ListView) findViewById(R.id.today_appointment_listview);
		mListView.setOnItemClickListener(this);
	}

	/**
	 * 获取预约列表
	 */
	private void obtainOppointment() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("subjectid", app.userVO.getSubject().getSubjectid());
		paramMap.put("reservationstate", "3");
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(reservation, this, Config.IP
				+ "api/v1/courseinfo/getmyreservation", paramMap, 10000,
				headerMap);
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (type.equals(reservation))
			super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (type.equals(reservation))
			super.doTimeOut(type);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(reservation)) {
				if (dataArray != null) {
					List<MyAppointmentVO> list = new ArrayList<MyAppointmentVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						MyAppointmentVO appointmentVO = JSONUtil.toJavaBean(
								MyAppointmentVO.class,
								dataArray.getJSONObject(i));
						if (appointmentVO != null) {
							// 只显示今天的预约
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date date = format
									.parse(UTC2LOC.instance.getDate(
											appointmentVO.getBegintime(),
											"yyyy-MM-dd"));
							LogUtil.print(UTC2LOC.instance.getDate(
									appointmentVO.getBegintime(), "yyyy-MM-dd"));
							Date today = new Date();
							if (Util.isSameDate(today, date)) {
								list.add(appointmentVO);
							}
						}
					}

					LogUtil.print(length + "预约个数：--" + list.size());
					if (list.size() <= 0) {
						// ZProgressHUD.getInstance(this).dismissWithSuccess(
						// "您今天没有预约");
						// ZProgressHUD.getInstance(this).show();
						order_ll.setBackgroundResource(R.drawable.order_bg);
					} else {
						if (adapter == null) {
							mList.clear();
							mList.addAll(list);
							adapter = new TodayAppointmentAdapter(mList, this);
							mListView.setAdapter(adapter);
						}
						mList.clear();

						mList.addAll(list);
					}

				}
				if (isRefreshing) {
					swipeLayout.setRefreshing(false);
					isRefreshing = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// if (!onClickSingleView()) {
		// return;
		// }
		LogUtil.print("sssssssssssssssssss");
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		MyAppointmentVO myAppointmentVO = adapter.getItem(position);
		String beginTime = UTC2LOC.instance.getDate(
				myAppointmentVO.getBegintime(), "hh:mm");
		String endTime = UTC2LOC.instance.getDate(myAppointmentVO.getEndtime(),
				"hh:mm");

		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		try {
			long diffBeginTime = UTC2LOC.instance.getDates(
					myAppointmentVO.getBegintime(), "yyyy-MM-dd HH:mm:ss")
					.getTime()
					- new Date().getTime();
			long diffEndTime = UTC2LOC.instance.getDates(
					myAppointmentVO.getEndtime(), "yyyy-MM-dd HH:mm:ss")
					.getTime()
					- new Date().getTime();
			LogUtil.print("diffEndTime--" + diffEndTime);
			if (diffBeginTime / 1000 / 60 > 15) {
				ZProgressHUD.getInstance(this).dismissWithSuccess(
						"请在开课前15分钟内签到");
				ZProgressHUD.getInstance(this).show();
			} else if (diffEndTime / 1000 / 60 < 0) {
				ZProgressHUD.getInstance(this).dismissWithSuccess(
						"您的课程已结束，不能再签到");
				ZProgressHUD.getInstance(this).show();
			} else {

				Intent intent = new Intent(this, QRCodeCreateActivity.class);
				intent.putExtra("myappointment", myAppointmentVO);
				startActivity(intent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		isRefreshing = true;
		obtainOppointment();
	}
}
