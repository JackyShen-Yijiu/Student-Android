package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jzjf.app.R;
import com.sft.adapter.SussessAppointmentAdapter;
import com.sft.vo.MyAppointmentVO;

public class SussessOrderActvity extends BaseActivity {

	private ListView Lv;
	private SussessAppointmentAdapter adapter;
	private List<MyAppointmentVO> list = new ArrayList<MyAppointmentVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.fragment_coach_or_school);
		initView();
	}

	private void initView() {
		setTitleText("已完成预约");

		Lv = (ListView) findViewById(R.id.enroll_select_school_listview);
		adapter = new SussessAppointmentAdapter(this, list);
		Lv.setAdapter(adapter);
	}

	/*
	 * private void request() { Map<String, String> paramsMap = new
	 * HashMap<String, String>(); paramsMap.put("userid",
	 * app.userVO.getUserid()); Map<String, String> headerMap = new
	 * HashMap<String, String>(); headerMap.put("authorization",
	 * app.userVO.getToken()); HttpSendUtils.httpGetSend("complaint", this,
	 * Config.IP + "api/v1/userinfo/favoritecoach", null, 10000, headerMap); }
	 */

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
