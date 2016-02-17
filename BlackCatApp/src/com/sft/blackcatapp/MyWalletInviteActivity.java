package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;

/**
 * 发送邀请
 * 
 * @author Administrator
 * 
 */
public class MyWalletInviteActivity extends BaseActivity implements
		OnClickListener {

	// 微信
	private ImageButton weixinBtn;
	// 微博
	private ImageButton weiboBtn;
	// qq
	private ImageButton qqBtn;
	// 短信
	private ImageButton messageBtn;
	//
	private ImageView closeIm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet_invite);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

	private void initView() {
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);// 需要添加的语句

		weixinBtn = (ImageButton) findViewById(R.id.send_invite_weixin_btn);
		weiboBtn = (ImageButton) findViewById(R.id.send_invite_weibo_btn);
		qqBtn = (ImageButton) findViewById(R.id.send_invite_qq_btn);
		messageBtn = (ImageButton) findViewById(R.id.send_invite_message_btn);

		closeIm = (ImageView) findViewById(R.id.send_invite_close_im);
	}

	private void setListener() {
		weixinBtn.setOnClickListener(this);
		weiboBtn.setOnClickListener(this);
		qqBtn.setOnClickListener(this);
		messageBtn.setOnClickListener(this);
		closeIm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.send_invite_weixin_btn:
			break;
		case R.id.send_invite_weibo_btn:
			break;
		case R.id.send_invite_qq_btn:
			break;
		case R.id.send_invite_message_btn:
			break;
		case R.id.send_invite_close_im:
			finish();
			break;
		}
	}
}
