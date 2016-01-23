package com.sft.blackcatapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
		OnItemClickListener {

	private static final String reservation = "reservation";
	private ListView mListView;
	private TodayAppointmentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_today_appointment);
		initView();
		obtainOppointment();
	}

	private void initView() {
		setTitleText(R.string.today_appointment);
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
						ZProgressHUD.getInstance(this).dismissWithSuccess(
								"您今天没有预约");
						ZProgressHUD.getInstance(this).show();
					} else {
						adapter = new TodayAppointmentAdapter(list, this);
						mListView.setAdapter(adapter);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
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

		MyAppointmentVO myAppointmentVO = adapter.getItem(position);
		String beginTime = UTC2LOC.instance.getDate(
				myAppointmentVO.getBegintime(), "hh:mm");
		String endTime = UTC2LOC.instance.getDate(myAppointmentVO.getEndtime(),
				"hh:mm");

		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		try {
			long diffBeginTime = format.parse(beginTime).getTime()
					- new Date().getTime();
			long diffEndTime = format.parse(endTime).getTime()
					- new Date().getTime();
			if (diffBeginTime / 1000 / 60 > 15) {
				ZProgressHUD.getInstance(this).dismissWithSuccess(
						"请在开课前15分钟内签到");
				ZProgressHUD.getInstance(this).show();
			} else if (diffEndTime / 1000 / 60 > 0) {
				ZProgressHUD.getInstance(this).dismissWithSuccess(
						"您的课程已结束，不能再签到");
				ZProgressHUD.getInstance(this).show();
			} else {

				Intent intent = new Intent(this, QRCodeCreateActivity.class);
				intent.putExtra("myappointment", myAppointmentVO);
				startActivity(intent);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
