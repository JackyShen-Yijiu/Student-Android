package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import com.sft.adapter.CarStyleListAdapter;
import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.CarModelVO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import cn.sft.baseactivity.util.HttpSendUtils;

/**
 * 选择车型界面
 * 
 * @author Administrator
 * 
 */
public class EnrollCarStyleActivity extends BaseActivity {

	// 车型list
	private ListView carList;

	private CarStyleListAdapter adapter;
	//
	private final static String carStyle = "carStyle";

	private CarModelVO selectCarModelVO;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_carstyle);
		initView();
		setListener();
		obtainEnrollCarStyle();
	};

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.finish);
		setTitleText(R.string.enroll_carstyle);

		carList = (ListView) findViewById(R.id.select_carstyle_listview);
		selectCarModelVO = (CarModelVO) getIntent().getSerializableExtra("carStyle");
	}

	private void setListener() {

	}

	private void obtainEnrollCarStyle() {
		HttpSendUtils.httpGetSend(carStyle, this, Config.IP + "api/v1/info/carmodel");
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
		case R.id.base_right_tv:
			if (adapter != null) {
				CarModelVO carModel = (CarModelVO) adapter.getItem(adapter.getIndex());
				if (app.selectEnrollCarStyle == null) {
					app.selectEnrollCarStyle = carModel;
					Util.updateEnrollCarStyle(this, carModel);
				}
				if (!app.selectEnrollCarStyle.getModelsid().equals(carModel.getModelsid())) {
					app.selectEnrollCarStyle = carModel;
					Util.updateEnrollCarStyle(this, carModel);
				}
				Intent intent = new Intent();
				intent.putExtra("activityName", SubjectEnrollActivity.class.getName());
				intent.putExtra("carStyle", carModel);
				setResult(RESULT_OK, intent);
			}
			finish();
			break;
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (type.equals(carStyle)) {
				if (dataArray != null) {
					int selectIndex = -1;
					int length = dataArray.length();
					List<CarModelVO> list = new ArrayList<CarModelVO>();
					for (int i = 0; i < length; i++) {
						CarModelVO carStyleVO = (CarModelVO) JSONUtil.toJavaBean(CarModelVO.class,
								dataArray.getJSONObject(i));
						if (selectCarModelVO != null) {
							if (carStyleVO.getModelsid().equals(selectCarModelVO.getModelsid())) {
								selectIndex = i;
							}
						}
						list.add(carStyleVO);
					}
					adapter = new CarStyleListAdapter(this, list);
					adapter.setSelected(selectIndex);
					carList.setAdapter(adapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
