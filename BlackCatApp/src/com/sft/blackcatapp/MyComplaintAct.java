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
import com.sft.adapter.MyComplaintAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.ComplaintListVO;

/**
 * 我的投诉
 * 
 * @author sun 2016-3-1 下午8:50:48
 * 
 */
public class MyComplaintAct extends BaseActivity {

	private ListView lv;

	private MyComplaintAdapter adapter;

	private List<ComplaintListVO> list = new ArrayList<ComplaintListVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.act_mycomplains);
		initView();
		request();
	}

	private void initView() {
		setTitleText(R.string.myapply1);

		lv = (ListView) findViewById(R.id.act_mycomplains_lv);
		adapter = new MyComplaintAdapter(this, list);
		lv.setAdapter(adapter);
	}

	private void request() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("complaint", this, Config.IP
				+ "api/v1/courseinfo/getmycomplaint", paramsMap, 10000,
				headerMap);
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

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals("complaint")) {
			int length = dataArray.length();
			List<ComplaintListVO> payList = new ArrayList<ComplaintListVO>();
			for (int i = 0; i < length; i++) {
				ComplaintListVO pay;
				try {
					pay = JSONUtil.toJavaBean(ComplaintListVO.class,
							dataArray.getJSONObject(i));
					payList.add(pay);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			list = payList;
			adapter.notifyDataSetChanged();
		}

		return true;
	}

}
