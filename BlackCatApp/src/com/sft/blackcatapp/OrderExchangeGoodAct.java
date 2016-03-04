package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.ExchangeGoodOrderAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.ExchangeGoodOrderVO;

/**
 * 兑换商品订单
 * @author sun  2016-2-25 下午5:07:55
 *
 */
public class OrderExchangeGoodAct extends BaseActivity implements OnItemClickListener{

	private ListView lv;
	
	private ExchangeGoodOrderAdapter adapter;
	
	ExchangeGoodOrderVO bean ;
	private RelativeLayout errorRl;
	private TextView errorTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.fragment_coach_or_school);
		initView();
		request();
	}

	private void initView() {
		setTitleText("我的订单");
		
		errorRl= (RelativeLayout) findViewById(R.id.error_rl);
		errorTv = (TextView) findViewById(R.id.error_tv);
		errorTv.setText(R.string.no_exchange_order);
		lv = (ListView) findViewById(R.id.enroll_select_school_listview);
		bean = new ExchangeGoodOrderVO();
		adapter = new ExchangeGoodOrderAdapter(this, bean);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}
	
	private void request(){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("index", "1");
		paramMap.put("count", "10");

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("exchangOrder", this, Config.IP
				+ "api/v1/userinfo/getmyorderlist", paramMap, 10000, headerMap);
	}
	

	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.base_left_btn:
			finish();
			break;
		}
		super.onClick(v);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if(super.doCallBack(type, jsonString)){
			return true;
		}
		if(type.equals("exchangOrder")){
			try {
				bean =JSONUtil.toJavaBean(ExchangeGoodOrderVO.class, data);
				adapter.setData(bean);
				if(bean.ordrelist.size()==0){
					errorRl.setVisibility(View.VISIBLE);
				}else{
					errorRl.setVisibility(View.VISIBLE);
				}
//				adapter.notifyDataSetChanged();
				
				LogUtil.print("result--size::"+bean.ordrelist.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.doCallBack(type, jsonString);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(OrderExchangeGoodAct.this,ExchangeDetailAct.class);
		i.putExtra("bean", bean.ordrelist.get(position));
//		i.putExtra("po", position);
		startActivity(i);
	}
	
	

	
	
}
