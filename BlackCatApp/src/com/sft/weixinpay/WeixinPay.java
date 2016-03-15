package com.sft.weixinpay;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.sft.util.LogUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付
 * @author pengdonghua
 *
 */
public class WeixinPay {

	private IWXAPI api;

	private Activity act;

	public WeixinPay(Activity a) {
		api = WXAPIFactory.createWXAPI(a, com.sft.common.Config.APP_ID_WEIXIN);

		api.registerApp(com.sft.common.Config.APP_ID_WEIXIN);
		act = a;
	}

	public void pay() {
		String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
		Toast.makeText(act, "锟斤拷取锟斤拷锟斤拷锟斤拷...", Toast.LENGTH_SHORT).show();
		try {
			byte[] buf = Util.httpGet(url);
			if (buf != null && buf.length > 0) {
				String content = new String(buf);
				Log.e("get server pay params:", content);
				JSONObject json = new JSONObject(content);
				if (null != json && !json.has("retcode")) {
					PayReq req = new PayReq();
					// req.appId = "wxf8b4f85f3a794e77"; // 锟斤拷锟斤拷锟斤拷appId
					req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
					Toast.makeText(act, "锟斤拷锟斤拷锟斤拷锟斤拷支锟斤拷", Toast.LENGTH_SHORT)
							.show();
					// 锟斤拷支锟斤拷之前锟斤拷锟斤拷锟接︼拷锟矫伙拷锟阶拷岬轿拷牛锟接︼拷锟斤拷鹊锟斤拷锟絀WXMsg.registerApp锟斤拷应锟斤拷注锟结到微锟斤拷
					api.sendReq(req);
				} else {
					Log.d("PAY_GET", "锟斤拷锟截达拷锟斤拷" + json.getString("retmsg"));
					Toast.makeText(act,
							"锟斤拷锟截达拷锟斤拷" + json.getString("retmsg"),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.d("PAY_GET", "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�");
				Toast.makeText(act, "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "锟届常锟斤拷" + e.getMessage());
			Toast.makeText(act, "锟届常锟斤拷" + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 解析 自己服务器的订单信息
	 * @param content
	 * @return
	 */
	public PayReq parse(String content) {
		
		try {
			JSONObject json = new JSONObject(content);
			if (null != json && !json.has("retcode")) {
				return parseJson(json);
//				return req;
			}
			
		} catch (Exception e) {
			Log.e("PAY_GET", "锟届常锟斤拷" + e.getMessage());
			Toast.makeText(act, "锟届常锟斤拷" + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
		return null;
	}
	
	public PayReq parseJson(JSONObject json) {
		
		try {
			if (null != json && !json.has("retcode")) {
				PayReq req = new PayReq();
				// req.appId = "wxf8b4f85f3a794e77"; // 锟斤拷锟斤拷锟斤拷appId
				req.appId = json.getString("appid");
				req.partnerId = json.getString("partnerid");
				req.prepayId = json.getString("prepayid");
				req.nonceStr = json.getString("noncestr");
				req.timeStamp = json.getString("timestamp");
				req.packageValue = json.getString("package");
				req.sign = json.getString("sign");
				req.extData = "app data";
				
				return req;
			}
			
		} catch (Exception e) {
			Log.e("PAY_GET", "失败" + e.getMessage());
			Toast.makeText(act, "aaa" + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
		return null;
	}
	
	public void pay(PayReq req){
		LogUtil.print(api+"requet--pay--->"+req);
		api.sendReq(req);
	}

}
