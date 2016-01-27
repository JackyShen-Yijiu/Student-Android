package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.AmountInCashistAdapter;
import com.sft.adapter.CupontAdapter;
import com.sft.adapter.IncomeListAdapter;
import com.sft.common.Config;
import com.sft.dialog.BonusDialog;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.AmountInCashVO;
import com.sft.vo.IncomeVO;
import com.sft.vo.MyCuponVO;
import com.sft.vo.MyWalletVO;

/**
 * 我的钱包
 * 
 * @author Administrator
 * 
 */
public class MyWalletActivity extends BaseActivity {

	// 积分收益
	private final static String myWallet = "myWallet";
	// 兑换券
	private final static String myCoinCertificate = "myCoinCertificate";
	// 现金额
	private final static String myAmountInCash = "myAmountInCash";
	private LinearLayout containOneBtnLl;
	private LinearLayout containTwoBtnLl;
	// 收益列表
	private ListView incomeList;
	// 我的零钱
	private TextView myChangeTv;
	// 邀请好友
	private Button inviteBtn;
	//
	private Button changeBtn;
	//
	// private TextView showListTv;
	// //
	// private ImageView showImage;
	//
	private TextView invitCodeTv;

	// private int clickNum = 0;

	// 每次请求的数目
	private int pageCount = 10;
	// 当前请求的条数
	private int seqindex = 0;
	// 积分收益adapter
	private IncomeListAdapter walletAdapter;
	// 兑换券adapter
	private CupontAdapter cupontAdapter;
	// 现金额adapter
	private AmountInCashistAdapter amountAdapter;
	private String producttype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_my_wallet);
		moneytype = getIntent().getStringExtra("moneytype");
		initView();
		initData();
		setListener();
		changeMoneyType();

	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);

		containTwoBtnLl = (LinearLayout) findViewById(R.id.my_wallet_towbtn_ll);
		containOneBtnLl = (LinearLayout) findViewById(R.id.my_wallet__onebtn_ll);

		myChangeTv = (TextView) findViewById(R.id.my_wallet_change_tv);
		inviteBtn = (Button) findViewById(R.id.my_wallet_invite_btn);
		changeBtn = (Button) findViewById(R.id.my_wallet_change_btn);
		exchangeBtn = (Button) findViewById(R.id.my_wallet_exchange_btn);
		incomeList = (ListView) findViewById(R.id.my_wallet_listview);
		invitCodeTv = (TextView) findViewById(R.id.my_wallet_invit_code_tv);
		unitTv = (TextView) findViewById(R.id.my_wallet_unit_tv);
		invitNameTv = (TextView) findViewById(R.id.my_wallet_invit_name_tv);
	}

	private void changeMoneyType() {

		if (Config.MoneyType.INTEGRAL_RETURN.getValue().equals(moneytype)) {
			containTwoBtnLl.setVisibility(View.VISIBLE);
			containOneBtnLl.setVisibility(View.GONE);
			setTitleText(R.string.my_wallet);
			unitTv.setText("YB");
			invitNameTv.setText("我的邀请码:");
			invitCodeTv.setText(app.userVO.getInvitationcode());
			producttype = Config.MoneyType.INTEGRAL_RETURN.getValue();
			obtainWallet();
		} else if (Config.MoneyType.COIN_CERTIFICATE.getValue().equals(
				moneytype)) {
			producttype = Config.MoneyType.COIN_CERTIFICATE.getValue();
			setTitleText(R.string.my_cupon);
			unitTv.setText("张");
			invitNameTv.setText("");
			containTwoBtnLl.setVisibility(View.GONE);
			containOneBtnLl.setVisibility(View.VISIBLE);
			obtainCoinCertificate();
		} else if (Config.MoneyType.AMOUNT_IN_CASH.getValue().equals(moneytype)) {
			invitNameTv.setText("");
			producttype = Config.MoneyType.AMOUNT_IN_CASH.getValue();
			setTitleText(R.string.my_amount);
			unitTv.setText("元");
			exchangeBtn.setText("取现");
			exchangeBtn.setEnabled(false);
			exchangeBtn.setBackgroundColor(Color.parseColor("#999999"));
			containTwoBtnLl.setVisibility(View.GONE);
			containOneBtnLl.setVisibility(View.VISIBLE);
			obtainAmountInCash();
		}
	}

	private void setListener() {
		inviteBtn.setOnClickListener(this);
		// showListTv.setOnClickListener(this);
		changeBtn.setOnClickListener(this);
		exchangeBtn.setOnClickListener(this);
	}

	private void initData() {

	}

	// 获取积分收益
	private void obtainWallet() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		paramsMap.put("usertype", "1");
		paramsMap.put("seqindex", seqindex + "");
		paramsMap.put("count", pageCount + "");

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myWallet, this, Config.IP
				+ "api/v1/userinfo/getmywallet", paramsMap, 10000, headerMap);
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

	// 现金额
	private void obtainAmountInCash() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		// paramsMap.put("userid", "562cb02e93d4ca260b40e544");
		paramsMap.put("index", "1");
		paramsMap.put("count", "10");

		Map<String, String> headerMap = new HashMap<String, String>();
		// headerMap
		// .put("authorization",
		// "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI1NjJjYjAyZTkzZDRjYTI2MGI0MGU1NDQiLCJ0aW1lc3RhbXAiOiIyMDE1LTEwLTI1VDEwOjQwOjUwLjI1MVoiLCJhdWQiOiJibGFja2NhdGUiLCJpYXQiOjE0NDU3Njk2NTB9.ooSYJ5zJ7ZIsaVwK0o0UuFGMS_xJQhSNcBNEtNAB25w");
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils
				.httpGetSend(myAmountInCash, this, Config.IP
						+ "api/v1/userinfo/getmymoneylist", paramsMap, 10000,
						headerMap);
	}

	private List<IncomeVO> dataList = new ArrayList<IncomeVO>();
	private String moneytype;
	private Button exchangeBtn;
	private TextView unitTv;
	private List<MyCuponVO> myCuponList;
	private TextView invitNameTv;

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(myWallet)) {
				if (data != null) {
					MyWalletVO walletVO = JSONUtil.toJavaBean(MyWalletVO.class,
							data);
					app.currency = walletVO.getWallet();
					myChangeTv.setText(walletVO.getWallet());

					dataList.addAll(walletVO.getList());
					if (walletAdapter == null) {
						walletAdapter = new IncomeListAdapter(this, dataList,
								producttype);
					} else {
						walletAdapter.setData(dataList);
					}
					incomeList.setAdapter(walletAdapter);
				}
			} else if (type.equals(myCoinCertificate)) {
				if (dataArray != null) {
					int length = dataArray.length();
					myCuponList = new ArrayList<MyCuponVO>();
					for (int i = 0; i < length; i++) {

						MyCuponVO myCuponVO = JSONUtil.toJavaBean(
								MyCuponVO.class, dataArray.getJSONObject(i));
						myCuponList.add(myCuponVO);
						LogUtil.print(myCuponVO.getCreatetime());
					}
					myChangeTv.setText(myCuponList.size() + "");
					if (cupontAdapter == null) {
						cupontAdapter = new CupontAdapter(this, myCuponList);
					} else {
						cupontAdapter.setData(myCuponList);
					}
					incomeList.setAdapter(cupontAdapter);
				}
			} else if (type.equals(myAmountInCash)) {
				if (data != null) {
					AmountInCashVO amountInCashVO = JSONUtil.toJavaBean(
							AmountInCashVO.class, data);

					myChangeTv.setText(amountInCashVO.getMoney() + "");

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
		// case R.id.my_wallet_show_tv:
		// if (++clickNum % 2 == 0) {
		// incomeList.setVisibility(View.INVISIBLE);
		// showImage.setBackgroundResource(R.drawable.wallet_show);
		// } else {
		// incomeList.setVisibility(View.VISIBLE);
		// showImage.setBackgroundResource(R.drawable.wallet_hide);
		// }
		// break;
		case R.id.my_wallet_change_btn:
			Intent intent = new Intent(this, MallActivity.class);
			intent.putExtra("moneytype", moneytype);
			startActivity(intent);
			break;
		case R.id.my_wallet_invite_btn:
			Intent intent2 = new Intent(this, BonusDialog.class);
			startActivity(intent2);
			break;
		case R.id.my_wallet_exchange_btn:
			Intent intent1 = new Intent(this, MallActivity.class);
			intent1.putExtra("moneytype", moneytype);
			if (myCuponList != null && myCuponList.size() > 0) {
				intent1.putExtra("myCupon", myCuponList.get(0));

			}
			startActivity(intent1);
			break;
		}
	}

	@Override
	public void forOperResult(Intent intent) {
		boolean invite = intent.getBooleanExtra("sendInvite", false);
		if (invite) {
			Intent intent2 = new Intent(this, MyWalletInviteActivity.class);
			startActivity(intent2);
		}
	}
}
