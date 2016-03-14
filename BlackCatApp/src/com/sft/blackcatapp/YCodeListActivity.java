package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.MyCodeListAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.MyCodeVO;

/**
 * 我的教练
 * 
 * @author Administrator
 * 
 */
public class YCodeListActivity extends BaseActivity implements
		OnItemClickListener {

	private static final String myCode = "myCode";

	//
	private XListView codeListView;

	private MyCodeListAdapter adapter;

	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_my_code);
		initView();
		setListener();
		obtainMyCode();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.my_code);

		codeListView = (XListView) findViewById(R.id.my_code_listview);
		codeListView.setPullLoadEnable(false);
		codeListView.setPullRefreshEnable(false);
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

	private void setListener() {
		codeListView.setPullRefreshEnable(false);
		codeListView.setPullLoadEnable(true);
		codeListView.setOnItemClickListener(this);
	}

	private void obtainMyCode() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myCode, this, Config.IP
				+ "api/v1/userinfo/getUserAvailableFcode", paramMap, 10000,
				headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(myCode)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0) {
						codeListView.setVisibility(View.VISIBLE);
						List<MyCodeVO> list = new ArrayList<MyCodeVO>();
						for (int i = 0; i < length; i++) {
							MyCodeVO codeVO = (MyCodeVO) JSONUtil.toJavaBean(
									MyCodeVO.class, dataArray.getJSONObject(i));
							list.add(codeVO);
						}
						adapter = new MyCodeListAdapter(this, list);
						codeListView.setAdapter(adapter);
						codeListView
								.setOnItemClickListener(YCodeListActivity.this);
					}
				}
			}
		} catch (Exception e) {

		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LogUtil.print("onitemclick");
		Toast("onclick");
		Intent intent = null;
		intent = new Intent();
		intent.putExtra("code", adapter.getItem(position - 1));
		setResult(RESULT_OK, intent);
		finish();

	}

}
