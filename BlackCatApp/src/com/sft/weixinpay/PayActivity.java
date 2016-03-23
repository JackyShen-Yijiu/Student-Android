package com.sft.weixinpay;

import android.app.Activity;
import android.os.Bundle;

import com.sft.common.Config;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayActivity extends Activity {

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay);

		api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WEIXIN);

	}

}
