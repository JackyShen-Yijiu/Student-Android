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
	private TextView inviteBtn;
	//

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

	private ImageView error_iv;

	private RelativeLayout error_rl;

	private TextView error_tv;

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

		error_iv = (ImageView) findViewById(R.id.error_iv);
		error_rl = (RelativeLayout) findViewById(R.id.error_rl);
		error_tv = (TextView) findViewById(R.id.error_tv);

		inviteBtn = (TextView) findViewById(R.id.my_wallet_invite_btn);

		incomeList = (ListView) findViewById(R.id.my_wallet_listview);

		invitCodeTv = (TextView) findViewById(R.id.my_wallet_invit_code_tv);

		error_iv = (ImageView) findViewById(R.id.error_iv);

	}

	private void changeMoneyType() {
		setTitleText(R.string.my_wallet);

		producttype = Config.MoneyType.INTEGRAL_RETURN.getValue();
		obtainWallet();

	}

	private void setListener() {
		inviteBtn.setOnClickListener(this);
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
					invitCodeTv.setText(app.userVO.getFcode());
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
					if (dataList.size() == 0) {
						error_rl.setVisibility(View.VISIBLE);
						error_iv.setImageResource(R.drawable.image_jifen);
						incomeList.setVisibility(View.GONE);
						error_tv.setText("您还没有奖励积分");
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

	@Override
	public void forOperResult(Intent intent) {
		boolean invite = intent.getBooleanExtra("sendInvite", false);
		if (invite) {
			Intent intent2 = new Intent(this, MyWalletInviteActivity.class);
			startActivity(intent2);
		}
	}

}
