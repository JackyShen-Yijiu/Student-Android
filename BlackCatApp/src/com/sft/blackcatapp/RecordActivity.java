package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.RecordAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.MyCuponVO;

public class RecordActivity extends BaseActivity {

	private ListView recordlist;
	private RecordAdapter recordAdapter;
	private ImageView error_iv;
	private RelativeLayout error_rl;
	private TextView error_tv;
	// 兑换券
	private final static String myCoinCertificate = "myCoinCertificate";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.record_juan);
		setTitleText(R.string.user_record);
		initView();
		obtainCoinCertificate();
	}

	private void initView() {
		boolean hasActionBar = getIntent()
				.getBooleanExtra("hasActionBar", true);
		if (hasActionBar) {
			setTitleBarVisible(View.VISIBLE);
		} else {
			setTitleBarVisible(View.GONE);
		}

		error_iv = (ImageView) findViewById(R.id.error_iv);
		error_rl = (RelativeLayout) findViewById(R.id.error_rl);
		error_tv = (TextView) findViewById(R.id.error_tv);

		recordlist = (ListView) findViewById(R.id.record_list);
	}

	// 获取兑换券
	private void obtainCoinCertificate() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myCoinCertificate, this, Config.IP
				+ "api/v1/userinfo/getcuponuselist", paramsMap, 10000,
				headerMap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		}
	}

	private List<MyCuponVO> myCuponList;

	private MyCuponVO myCuponVO;
	private String producttype;

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(myCoinCertificate)) {
				if (dataArray != null) {
					int length = dataArray.length();
					myCuponList = new ArrayList<MyCuponVO>();
					if (myCuponList.size() == 0) {
						error_rl.setVisibility(View.VISIBLE);
						error_iv.setImageResource(R.drawable.image_quan);
						recordlist.setVisibility(View.GONE);
						error_tv.setText("您还没有报名兑换券");
					}
					for (int i = 0; i < length; i++) {

						MyCuponVO myCuponVO = JSONUtil.toJavaBean(
								MyCuponVO.class, dataArray.getJSONObject(i));
						myCuponList.add(myCuponVO);

					}
					app.coupons = length;
					if (recordAdapter == null) {
						recordAdapter = new RecordAdapter(this, myCuponList,
								producttype);
					} else {
						recordAdapter.setData(myCuponList);
					}
					recordlist.setAdapter(recordAdapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
