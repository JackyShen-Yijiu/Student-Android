package com.jzjf.app.wxapi;


import com.jzjf.app.R;
import com.sft.blackcatapp.NewConfirmOrderActivity;
import com.sft.common.Config;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WEIXIN);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode+"String-->"+resp.errStr);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			NewConfirmOrderActivity.weixinPayState = resp.errCode;
			if(resp.errCode == 0){//支付成功
				Toast.makeText(this,"微信支付成功", Toast.LENGTH_SHORT).show();
			}else if(resp.errCode == -1){//签名错误等
				Toast.makeText(this,"微信支付失败", Toast.LENGTH_SHORT).show();
			}else{//其他错误
				Toast.makeText(this,"微信支付取消", Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
}