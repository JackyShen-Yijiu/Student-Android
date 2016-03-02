package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.AmountInCashistAdapter;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.AmountInCashVO;
import com.sft.vo.IncomeVO;
import com.sft.vo.MyCuponVO;

/**
 * 我的钱包
 * 
 * @author Administrator
 * 
 */
public class Walletmoney extends BaseActivity {

	// 现金额
	private final static String myAmountInCash = "myAmountInCash";

	// 收益列表
	private ListView incomeList;
	//

	// 邀请码
	private TextView invitCodeTv;

	// private int clickNum = 0;

	// 现金额adapter
	private AmountInCashistAdapter amountAdapter;

	private String producttype;
	private WalletActivity parentActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walletmoney);
		initView();
		initData();
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

		incomeList = (ListView) findViewById(R.id.my_wallet_listview);

		invitCodeTv = (TextView) findViewById(R.id.my_wallet_invit_code_tv);

	}

	private void changeMoneyType() {
		invitCodeTv.setText("我的Y码： " + app.userVO.getInvitationcode());
		producttype = Config.MoneyType.AMOUNT_IN_CASH.getValue();
		obtainAmountInCash();

	}

	private void initData() {

	}

	// 现金额
	private void obtainAmountInCash() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		paramsMap.put("index", "1");
		paramsMap.put("count", "10");

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils
				.httpGetSend(myAmountInCash, this, Config.IP
						+ "api/v1/userinfo/getmymoneylist", paramsMap, 10000,
						headerMap);
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
			if (type.equals(myAmountInCash)) {
				if (data != null) {
					AmountInCashVO amountInCashVO = JSONUtil.toJavaBean(
							AmountInCashVO.class, data);
					app.money = amountInCashVO.getMoney();
					if (amountAdapter == null) {
						amountAdapter = new AmountInCashistAdapter(this,
								amountInCashVO.getMoneylist());
					} else {
						amountAdapter.setData(amountInCashVO.getMoneylist());
					}
					incomeList.setAdapter(amountAdapter);
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
		}
	}

}