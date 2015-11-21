package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sft.adapter.IncomeListAdapter;
import com.sft.common.Config;
import com.sft.dialog.BonusDialog;
import com.sft.util.JSONUtil;
import com.sft.vo.IncomeVO;
import com.sft.vo.MyWalletVO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

/**
 * 我的钱包
 * 
 * @author Administrator
 * 
 */
public class MyWalletActivity extends BaseActivity {

	private final static String myWallet = "myWallet";
	// 收益列表
	private ListView incomeList;
	// 我的零钱
	private TextView myChangeTv;
	// 邀请好友
	private Button inviteBtn;
	//
	private Button changeBtn;
	//
	private TextView showListTv;
	//
	private ImageView showImage;
	//
	private TextView invitCodeTv;

	private int clickNum = 0;

	// 每次请求的数目
	private int pageCount = 10;
	// 当前请求的条数
	private int seqindex = 0;
	//
	private IncomeListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_my_wallet);
		initView();
		initData();
		setListener();
		obtainWallet();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.my_wallet);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);

		myChangeTv = (TextView) findViewById(R.id.my_wallet_change_tv);
		inviteBtn = (Button) findViewById(R.id.my_wallet_invite_btn);
		changeBtn = (Button) findViewById(R.id.my_wallet_change_btn);
		incomeList = (ListView) findViewById(R.id.my_wallet_listview);
		invitCodeTv = (TextView) findViewById(R.id.my_wallet_invit_code_tv);
		showListTv = (TextView) findViewById(R.id.my_wallet_show_tv);
		showImage = (ImageView) findViewById(R.id.my_wallet_show_im);
	}

	private void setListener() {
		inviteBtn.setOnClickListener(this);
		showListTv.setOnClickListener(this);
		changeBtn.setOnClickListener(this);
	}

	private void initData() {
		invitCodeTv.setText(app.userVO.getInvitationcode());
	}

	private void obtainWallet() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		paramsMap.put("usertype", "1");
		paramsMap.put("seqindex", seqindex + "");
		paramsMap.put("count", pageCount + "");

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myWallet, this, Config.IP + "api/v1/userinfo/getmywallet", paramsMap, 10000,
				headerMap);
	}

	private List<IncomeVO> dataList = new ArrayList<IncomeVO>();

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(myWallet)) {
				if (data != null) {
					MyWalletVO walletVO = (MyWalletVO) JSONUtil.toJavaBean(MyWalletVO.class, data);
					app.currency = walletVO.getWallet();
					myChangeTv.setText(walletVO.getWallet());

					dataList.addAll(walletVO.getList());
					if (adapter == null) {
						adapter = new IncomeListAdapter(this, dataList);
					} else {
						adapter.setData(dataList);
					}
					incomeList.setAdapter(adapter);
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
		case R.id.my_wallet_show_tv:
			if (++clickNum % 2 == 0) {
				incomeList.setVisibility(View.INVISIBLE);
				showImage.setBackgroundResource(R.drawable.wallet_show);
			} else {
				incomeList.setVisibility(View.VISIBLE);
				showImage.setBackgroundResource(R.drawable.wallet_hide);
			}
			break;
		case R.id.my_wallet_change_btn:
			Intent intent = new Intent(this, MallActivity.class);
			startActivity(intent);
			break;
		case R.id.my_wallet_invite_btn:
			BonusDialog dialog = new BonusDialog(this);
			dialog.show();
			break;
		}
	}

	@Override
	public void forOperResult(Intent intent) {
		boolean invite = intent.getBooleanExtra("sendInvite", false);
		if (invite) {
//			Intent intent2 = new Intent(this, MyWalletInviteActivity.class);
//			startActivity(intent2);
		}
	}
}
