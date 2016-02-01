package com.sft.blackcatapp;

import me.maxwin.view.XListView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sft.adapter.SchoolBusRouteAdapter;
import com.sft.vo.SchoolVO;

public class SchoolBusRouteActivity extends BaseActivity implements
		OnClickListener {

	private SchoolVO school;
	private XListView listview;
	private TextView noBus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_bus_route);
		initView();
		initData();

	}

	private void initView() {
		setTitleText(R.string.bus_route);
		listview = (XListView) findViewById(R.id.bus_route_listview);
		listview.setPullRefreshEnable(false);
		listview.setPullLoadEnable(false);
		noBus = (TextView) findViewById(R.id.bus_route_no_tv);
	}

	private void initData() {
		noBus.setVisibility(View.GONE);

		school = (SchoolVO) getIntent().getSerializableExtra("school_route");
		if (school == null && school.getSchoolbusroute() == null
				&& school.getSchoolbusroute().size() == 0) {
			noBus.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		} else {
			SchoolBusRouteAdapter adapter = new SchoolBusRouteAdapter(this,
					school.getSchoolbusroute());
			listview.setAdapter(adapter);
		}
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
