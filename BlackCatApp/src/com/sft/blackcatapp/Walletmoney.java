package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.AmountInCashistAdapter;
import com.sft.common.Config;
import com.sft.dialog.BonusDialog;
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

	private RelativeLayout error_rl;

	private TextView error_tv;

	private TextView inviteBtn;

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
		ImageView error_iv = (ImageView) findViewById(R.id.error_iv);
		error_rl = (RelativeLayout) findViewById(R.id.error_rl);
		error_tv = (TextView) findViewById(R.id.error_tv);

		inviteBtn = (TextView) findViewById(R.id.my_wallet_invite_btn);

		incomeList = (ListView) findViewById(R.id.my_wallet_listview);

		invitCodeTv = (TextView) findViewById(R.id.my_wallet_invit_code_tv);

		inviteBtn.setOnClickListener(this);

	}

	private void changeMoneyType() {

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
					invitCodeTv.setText("我的Y码： " + amountInCashVO.getFcode());
					if (amountAdapter == null) {
						amountAdapter = new AmountInCashistAdapter(this,
								amountInCashVO.getMoneylist());
					} else {
						amountAdapter.setData(amountInCashVO.getMoneylist());
					}
					incomeList.setAdapter(amountAdapter);

					if (amountInCashVO.getMoneylist().size() == 0) {
						error_rl.setVisibility(View.VISIBLE);
						incomeList.setVisibility(View.GONE);
						error_tv.setText("您还没有可领取的现金额度");
					}
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
		case R.id.my_wallet_invite_btn:
			Intent intent2 = new Intent(this, BonusDialog.class);
			startActivity(intent2);
			break;
		}
	}

}
