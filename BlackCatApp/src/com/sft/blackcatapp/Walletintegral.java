package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import android.os.Bundle;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.IntegralListAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.walletvo.IntegralVO;

public class Walletintegral extends BaseActivity {
	private static final String integral = "integral";
	private XListView list_walletintegral;
	private IntegralListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_walletintegral);
		initView();
		initData();
		obtainWalletintegral();
	}

	private void obtainWalletintegral() {

		Map<String, String> paramMap = new HashMap<String, String>();

		LogUtil.print("<<<<<id111111111" + app.userVO.getUserid());
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("usertype", "1");
		paramMap.put("seqindex", "1");
		paramMap.put("count", "10");

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());

		HttpSendUtils.httpGetSend(integral, this, Config.IP
				+ "api/v1/userinfo/getmywallet", paramMap, 10000, headerMap);
	}

	private void initView() {

		list_walletintegral = (XListView) findViewById(R.id.list_walletintegral);
	}

	private void initData() {
		list_walletintegral.setPullRefreshEnable(false);
		list_walletintegral.setPullLoadEnable(false);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(integral)) {
				if (dataArray != null) {
					List<IntegralVO> list = new ArrayList<IntegralVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						IntegralVO integralVO = (IntegralVO) JSONUtil
								.toJavaBean(IntegralVO.class,
										dataArray.getJSONObject(i));
						list.add(integralVO);
					}
					if (list.size() > 0) {
						list_walletintegral.setAdapter(adapter);
					}
				}
			}
		} catch (Exception e) {

		}
		return true;
	}
}
