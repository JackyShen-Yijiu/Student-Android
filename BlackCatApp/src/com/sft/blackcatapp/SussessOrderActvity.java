package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.SussessAppointmentAdapter;
import com.sft.vo.AppointmentTempVO;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.MyAppointmentVO;

public class SussessOrderActvity extends BaseActivity {
	private static final String complaint = "appointmentVO";
	private ListView Lv;
	private SussessAppointmentAdapter adapter;

	private List<MyAppointmentVO> list = new ArrayList<MyAppointmentVO>();

	private MyAppointmentVO appointmentVO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.sussess_order_act);
		initView();
//		request();
	}

	private void initView() {
		setTitleText("已完成预约");
		AppointmentTempVO vo = (AppointmentTempVO)getIntent().getSerializableExtra("list");
		list = vo.list;

		Lv = (ListView) findViewById(R.id.enroll_select_school_listview);
		adapter = new SussessAppointmentAdapter(this, list);
		Lv.setAdapter(adapter);
	}

	private void request() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("complaint", this, Config.IP
				+ "api/v1/courseinfo/userreservationinfo", paramsMap, 10000,
				headerMap);

	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(complaint)) {
				if (data != null) {
					appointmentVO = JSONUtil.toJavaBean(MyAppointmentVO.class,
							data);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.base_left_btn:
			finish();
			break;
		}
	}

}
