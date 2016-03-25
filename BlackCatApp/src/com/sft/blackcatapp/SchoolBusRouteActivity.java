package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.SchoolBusAdapter;
import com.sft.adapter.SchoolBusAdapter2;
import com.sft.common.Config;
import com.sft.fragment.MenuFragment;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.SchoolBusRouteNew;
import com.sft.vo.SchoolVO;

/**
 * 班车 路线
 * 
 * @author pengdonghua
 * 
 */
public class SchoolBusRouteActivity extends BaseActivity implements
		OnClickListener {

	private static final String schoolType = "school";
	private SchoolVO school;
	private ListView listview;
	private ExpandableListView lv;
	private ImageView error_iv;
	private TextView errorTvs;

	private SchoolBusAdapter adapter;

	private SchoolBusAdapter2 adapter2;

	private List<SchoolBusRouteNew> list = new ArrayList<SchoolBusRouteNew>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_bus_route);
		initView();
		initData();
//		
	}

	private void initView() {
		setTitleText(R.string.bus_route);
		adapter = new SchoolBusAdapter(this);
		adapter2 = new SchoolBusAdapter2(this, list);
		lv = (ExpandableListView) findViewById(R.id.bus_route_ep_lv);
		lv.setAdapter(adapter);
		listview = (ListView) findViewById(R.id.bus_route_listview);
		listview.setAdapter(adapter2);

		error_iv = (ImageView) findViewById(R.id.error_iv);
		errorRl = (RelativeLayout) findViewById(R.id.error_rl);
		errorTv = (TextView) findViewById(R.id.error_tv);
		errorTvs = (TextView) findViewById(R.id.error_tvs);
	}

	private void initData() {
		school = (SchoolVO) getIntent().getSerializableExtra("school_route");
		String schoolId = getIntent().getStringExtra(MenuFragment.schoolId);
		
		if (!TextUtils.isEmpty(schoolId)) {
			obtainEnrollSchoolDetail(schoolId);
		} else {
			if (school == null || school.getSchoolbusroute() == null
					|| school.getSchoolbusroute().size() == 0) {
				errorRl.setVisibility(View.VISIBLE);
				error_iv.setImageResource(R.drawable.image_banche);
				errorTv.setText("暂时没有班车接送信息");
			} else {
				adapter.setData(school.getSchoolbusroute());
				adapter2.setData(school.getSchoolbusroute());
			}
		}
	}

	private void obtainEnrollSchoolDetail(String schoolId) {
		HttpSendUtils.httpGetSend(schoolType, this, Config.IP
				+ "api/v1/driveschool/getschoolinfo/" + schoolId);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(schoolType)) {
				if (data != null) {
					school = JSONUtil.toJavaBean(SchoolVO.class, data);
				}
				
				if (school == null || school.getSchoolbusroute() == null
						|| school.getSchoolbusroute().size() == 0) {
					errorRl.setVisibility(View.VISIBLE);
					error_iv.setImageResource(R.drawable.image_banche);
					errorTv.setText("暂时没有班车接送信息");
				} else {
					adapter.setData(school.getSchoolbusroute());
					adapter2.setData(school.getSchoolbusroute());
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
		case R.id.base_left_btn:
			finish();
			break;
		}
	}
}
