package com.sft.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sft.util.LogUtil;

public class PayUtils {

	// 商户PID
	public static final String PARTNER = "2088121519930520";
	// 商户收款账号
	public static final String SELLER = "ybpay@ybxch.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM1vo5i8yDX9XxbVh7jBBT5a68B3IDSv9Sn7BB/eUmMn/saqrnSlZnJTdnUor9YUuIwZWNaHbVFfIzoqFP9Nz+SxnvKrZx01+KwGkJkQWgwD19ZMDZ7W+Pl2p/dpSmOsXa5aVQ84/joapjzCU265wjoZeNgF+3Tlu85qucUqNtRBAgMBAAECgYAjSjw2LqfN7np5N7Ea4b1+2quI4m3b3yYkUgSBXPWWJvazNfrF+uvAU+oTcivSgLi7CFekcpsq0UcB5Ny6hqlhdsAQDYGUGu3HdO4+I7HT8kZVrNpH2Z10RUFeR1lCPg48zIrtBidQs+rbUffHz7WlW3LROGk9L1Zc2FZLpd/+6QJBAPhBZGzclHkRyz/RsHrRQIGa4ib1i4jWGb+K29Gf1J3VBqvvXdhji7gbQGK4CgOHkpwdwokf+MgEWWfHtJHq/a8CQQDT2EjafwluaHvXwvovklqJgiDHLm9bS6AerVThH/6Kc9oN0WV+hYObWx0wvSTHwDN+cX1g9+sSOIWMxApn6jkPAkBvoBejy1sHRTT6sTKFRumWXSorEmV3Va2mLq3Ah/l2Ln8deDfeQIRmd/pvaQk3pRILGGaMec/HQA1jWKycn+kJAkEAvTLGZjXdE3lHWbyuOvxn1bWdVqbXF19kYuGFFAbCC59KWNrx3Vya3I40dMoIZkn282+ifY8Np9lJFR4k+QfL6wJAGQWQcBrNPMHLbwkuwrmlHGw6bsA0GiZn/y4pfw82nQo2Q+xjb+w1EfUDB47eoQzRJHBSoRDYxlLbwQRjr3EOng==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

	private static final String RECALL_URL = "http://api.yibuxueche.com/paynotice/alipay";// "http://notify.msp.hk/notify.htm";

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	

	public void pay(final Activity c,final Handler mHandler,String payName,String payDetail,String money) {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(c)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									//
									c.finish();
								}
							}).show();
			return;
		}
		//
		String orderInfo = getOrderInfo(payName, payDetail, money);

		LogUtil.print("sign--0000---->" + orderInfo);
		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		LogUtil.print("sign------>" + sign);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(c);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v,final Activity context,final Handler mHandler) {
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(context);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion(Activity a) {
		PayTask payTask = new PayTask(a);
		String version = payTask.getVersion();
		Toast.makeText(a, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
	public void h5Pay(View v,Activity c) {
		Intent intent = new Intent(c, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 */
		String url = "http://m.taobao.com";
		// url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		extras.putString("url", url);
		intent.putExtras(extras);
		c.startActivity(intent);

	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + TradeNo + "\"";//getOutTradeNo()

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	
	String TradeNo;
	
	public void setTradeNo(String no){
		TradeNo = no;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	

}
