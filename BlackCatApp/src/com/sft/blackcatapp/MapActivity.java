package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 驾校位置界面
 * 
 * @author Administrator
 * 
 */
public class MapActivity extends BaseActivity implements
		OnMapDoubleClickListener {

	// ********************百度地图********************
	private MapView mapView = null;
	private BaiduMap mBaiduMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_map);
		initView();
		initMap();
	}

	protected void onResume() {
		super.onResume();
		register(getClass().getName());
	};

	private void initView() {
		Intent intent = getIntent();
		setTitleText(intent.getStringExtra("title"));

		mapView = (MapView) findViewById(R.id.map_bmapView);
	}

	private void initMap() {
		// 获取地图控件引用
		mapView = (MapView) findViewById(R.id.map_bmapView);
		mBaiduMap = mapView.getMap();
		mBaiduMap.setOnMapDoubleClickListener(this);
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// 地图类型：普通地图
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().zoom(18f).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		try {
			double latitude = Double.parseDouble(getIntent().getStringExtra(
					"latitude"));
			double longtitude = Double.parseDouble(getIntent().getStringExtra(
					"longtitude"));
			LatLng point = new LatLng(latitude, longtitude);
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.position_bkground);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option = new MarkerOptions().position(point)
					.anchor(0.5f, 0).icon(bitmap).perspective(true);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(option);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
			mBaiduMap.animateMapStatus(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void onMapDoubleClick(LatLng arg0) {

	}
}
