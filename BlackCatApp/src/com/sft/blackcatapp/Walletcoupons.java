package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.CupontAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.IncomeVO;
import com.sft.vo.MyCuponVO;

/**
 * 我的钱包
 * 
 * @author Administrator
 * 
 */
public class Walletcoupons extends BaseActivity {

	// 兑换券
	private final static String myCoinCertificate = "myCoinCertificate";

	// 收益列表
	private ListView incomeList;
	//
	private Button changeBtn;

	// 邀请码
	private TextView invitCodeTv;

	// private int clickNum = 0;

	// 兑换券adapter
	private CupontAdapter cupontAdapter;

	private String producttype;
	private WalletActivity parentActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walletcoupons);
		initView();
		initData();
		setListener();
		changeMoneyType();
		parentActivity = (WalletActivity) getParent();
		// LogUtil.print(getParent() + "sun-->" + getCallingPackage());

	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		changeBtn = (Button) findViewById(R.id.my_wallet_change_btn);

		incomeList = (ListView) findViewById(R.id.my_wallet_listview);

		invitCodeTv = (TextView) findViewById(R.id.my_wallet_invit_code_tv);

	}

	private void changeMoneyType() {
		invitCodeTv.setText("我的Y码： " + app.userVO.getInvitationcode());
		producttype = Config.MoneyType.COIN_CERTIFICATE.getValue();
		obtainCoinCertificate();

	}

	private void setListener() {
		changeBtn.setOnClickListener(this);
	}

	private void initData() {

	}

	// 获取兑换券
	private void obtainCoinCertificate() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		// paramsMap.put("userid", "562cb02e93d4ca260b40e544");

		Map<String, String> headerMap = new HashMap<String, String>();
		// headerMap
		// .put("authorization",
		// "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI1NjJjYjAyZTkzZDRjYTI2MGI0MGU1NDQiLCJ0aW1lc3RhbXAiOiIyMDE1LTEwLTI1VDEwOjQwOjUwLjI1MVoiLCJhdWQiOiJibGFja2NhdGUiLCJpYXQiOjE0NDU3Njk2NTB9.ooSYJ5zJ7ZIsaVwK0o0UuFGMS_xJQhSNcBNEtNAB25w");
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myCoinCertificate, this, Config.IP
				+ "api/v1/userinfo/getmycupon", paramsMap, 10000, headerMap);
	}

	private List<IncomeVO> dataList = new ArrayList<IncomeVO>();
	private List<MyCuponVO> myCuponList;

	private MyCuponVO myCuponVO;

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
					for (int i = 0; i < length; i++) {

						MyCuponVO myCuponVO = JSONUtil.toJavaBean(
								MyCuponVO.class, dataArray.getJSONObject(i));
						myCuponList.add(myCuponVO);

					}
					app.coupons = length;
					if (cupontAdapter == null) {
						cupontAdapter = new CupontAdapter(this, myCuponList,
								producttype);
					} else {
						cupontAdapter.setData(myCuponList);
					}
					incomeList.setAdapter(cupontAdapter);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
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
		case R.id.my_wallet_change_btn:
			MainActivity.TARGET_TAB = MainActivity.TAB_MALL;
			finish();
//			Intent intent = new Intent(this, MallActivity.class);
//			startActivity(intent);
			break;
		}
	}

}
