package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.ExchangeGoodOrderAdapter;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.view.RefreshLayout;
import com.sft.view.RefreshLayout.OnLoadListener;
import com.sft.vo.ExchangeGoodOrderVO;
import com.sft.vo.ExchangeOrderItemVO;

/**
 * 兑换商品订单
 * 
 * @author sun 2016-2-25 下午5:07:55
 * 
 */
public class OrderExchangeGoodAct extends BaseActivity implements
		OnItemClickListener, OnRefreshListener, OnLoadListener {

	private ListView lv;

	private ExchangeGoodOrderAdapter adapter;

	ExchangeGoodOrderVO bean;
	private RelativeLayout errorRl;
	private TextView errorTv;

	private TextView errorTvs;

	private ImageView error_iv;

	private RefreshLayout swipeLayout;
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;
	private int index = 1;
	private List<ExchangeOrderItemVO> orderList = new ArrayList<ExchangeOrderItemVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.fragment_coach_or_school);
		initView();
		request();
	}

	private void initView() {

		boolean hasActionBar = getIntent()
				.getBooleanExtra("hasActionBar", true);
		if (hasActionBar) {
			setTitleBarVisible(View.VISIBLE);
			setTitleText("兑换记录");
		} else {
			setTitleBarVisible(View.GONE);
			setTitleText("我的订单");
		}

		error_iv = (ImageView) findViewById(R.id.error_iv);
		errorRl = (RelativeLayout) findViewById(R.id.error_rl);
		errorTv = (TextView) findViewById(R.id.error_tv);
		errorTvs = (TextView) findViewById(R.id.error_tvs);
		swipeLayout = (RefreshLayout) findViewById(R.id.enroll_school_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setBackgroundColor(getResources().getColor(R.color.white));
		lv = (ListView) findViewById(R.id.enroll_select_school_listview);
		bean = new ExchangeGoodOrderVO();
		adapter = new ExchangeGoodOrderAdapter(this, orderList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

	private void request() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("index", index + "");
		paramMap.put("count", "10");

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("exchangOrder", this, Config.IP
				+ "api/v1/userinfo/getmyorderlist", paramMap, 10000, headerMap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		}
		super.onClick(v);
	}

	@Override
	public void doException(String type, Exception e, int code) {
		super.doException(type, e, code);
		if (isRefreshing) {
			swipeLayout.setRefreshing(false);
			isRefreshing = false;
		}
		if (isLoadingMore) {
			swipeLayout.setLoading(false);
			isLoadingMore = false;
		}
		errorRl.setVisibility(View.VISIBLE);
		swipeLayout.setVisibility(View.GONE);

		error_iv.setBackgroundResource(R.drawable.app_no_wifi);
		errorTv.setText(CommonUtil.getString(this, R.string.no_wifi));
	}

	@Override
	public void doTimeOut(String type) {
		super.doTimeOut(type);
		if (isRefreshing) {
			swipeLayout.setRefreshing(false);
			isRefreshing = false;
		}
		if (isLoadingMore) {
			swipeLayout.setLoading(false);
			isLoadingMore = false;
		}
		errorRl.setVisibility(View.VISIBLE);
		swipeLayout.setVisibility(View.GONE);
		error_iv.setBackgroundResource(R.drawable.app_no_wifi);
		errorTv.setText(CommonUtil.getString(this, R.string.no_wifi));
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals("exchangOrder")) {
			try {
				bean = JSONUtil.toJavaBean(ExchangeGoodOrderVO.class, data);
				if (bean.ordrelist.size() == 0) {
					errorRl.setVisibility(View.VISIBLE);

					error_iv.setImageResource(R.drawable.image_dingdan);
					errorTv.setText("没有找到您的订单信息");
					errorTvs.setVisibility(View.VISIBLE);
					errorTvs.setText("请前往商城购买");
				} else {
					errorRl.setVisibility(View.GONE);
				}
				if (index == 1) {
					orderList.clear();
					orderList.addAll(bean.ordrelist);
				} else {
					if (bean.ordrelist.size() == 0) {
						// 没有更多数据
						Toast("没有更多数据了");
						LogUtil.print("没有更多数据了");
					} else {
						orderList.addAll(bean.ordrelist);
					}
				}
				// adapter.setData(bean);
				adapter.notifyDataSetChanged();
				if (isRefreshing) {
					swipeLayout.setRefreshing(false);
					isRefreshing = false;
				}
				if (isLoadingMore) {
					swipeLayout.setLoading(false);
					isLoadingMore = false;
				}
				// LogUtil.print("result--size::"+bean.ordrelist.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.doCallBack(type, jsonString);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(OrderExchangeGoodAct.this,
				ProductOrderSuccessActivity.class);
		i.putExtra("exchangeOrderItemVO", bean.ordrelist.get(position));
		// i.putExtra("po", position);
		startActivity(i);
	}

	@Override
	public void onRefresh() {
		isRefreshing = true;
		index = 1;
		request();
	}

	@Override
	public void onLoad() {
		isLoadingMore = true;
		index++;
		request();
	}
}
