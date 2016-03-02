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

import com.jzjf.app.R;
import com.sft.adapter.IncomeListAdapter;
import com.sft.common.Config;
import com.sft.dialog.BonusDialog;
import com.sft.util.JSONUtil;
import com.sft.vo.IncomeVO;
import com.sft.vo.MyCuponVO;
import com.sft.vo.MyWalletVO;

/**
 * 我的钱包
 * 
 * @author Administrator
 * 
 */
public class Walletintegral extends BaseActivity {

	// 积分收益
	private final static String myWallet = "myWallet";

	// 收益列表
	private ListView incomeList;
	// 邀请好友
	private Button inviteBtn;
	//
	private Button changeBtn;

	// 邀请码
	private TextView invitCodeTv;

	// private int clickNum = 0;

	// 每次请求的数目
	private int pageCount = 10;
	// 当前请求的条数
	private int seqindex = 0;
	// 积分收益adapter
	private IncomeListAdapter walletAdapter;
	private String producttype;
	private WalletActivity parentActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walletintegral);
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

		inviteBtn = (Button) findViewById(R.id.my_wallet_invite_btn);

		changeBtn = (Button) findViewById(R.id.my_wallet_change_btn);

		incomeList = (ListView) findViewById(R.id.my_wallet_listview);

		invitCodeTv = (TextView) findViewById(R.id.my_wallet_invit_code_tv);

	}

	private void changeMoneyType() {
		setTitleText(R.string.my_wallet);

		invitCodeTv.setText("我的Y码： " + app.userVO.getInvitationcode());
		producttype = Config.MoneyType.INTEGRAL_RETURN.getValue();
		obtainWallet();

	}

	private void setListener() {
		inviteBtn.setOnClickListener(this);
		changeBtn.setOnClickListener(this);
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

	private List<IncomeVO> dataList = new ArrayList<IncomeVO>();
	private List<MyCuponVO> myCuponList;

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

					dataList.addAll(walletVO.getList());
					if (walletAdapter == null) {
						walletAdapter = new IncomeListAdapter(this, dataList,
								producttype);
					} else {
						walletAdapter.setData(dataList);
					}
					incomeList.setAdapter(walletAdapter);
					if (parentActivity != null) {
						parentActivity.tv_code.setText(walletVO.getWallet()
								+ "积分");
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
		case R.id.my_wallet_change_btn:

			MainActivity.TARGET_TAB = MainActivity.TAB_MALL;
			finish();
			// Intent intent = new Intent(this, MallActivity.class);
			// startActivity(intent);
			break;
		case R.id.my_wallet_invite_btn:
			Intent intent2 = new Intent(this, BonusDialog.class);
			startActivity(intent2);
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
