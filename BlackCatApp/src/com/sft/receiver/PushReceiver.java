package com.sft.receiver;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.sft.blackcatapp.AboutUsActivity;
import com.sft.blackcatapp.AppointmentDetailActivity;
import com.sft.blackcatapp.BaseActivity;
import com.sft.blackcatapp.MessageActivity;
import com.sft.blackcatapp.MyWalletActivity;
import com.sft.blackcatapp.SystemPushActivity;
import com.sft.blackcatapp.WelcomeActivity;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.common.Config.PushType;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.PushInnerVO;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.listener.ICallBack;
import cn.sft.sqlhelper.DBHelper;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver implements ICallBack {
	private static final String TAG = "JPush";
	private static final String appointmentDetail = "appointmentDetail";

	private BlackCatApplication app = BlackCatApplication.getInstance();

	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Bundle bundle = intent.getExtras();
		Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		Log.e(TAG, "isLogin=" + app.isLogin);
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			// String regId =
			// bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject json = new JSONObject(extra);
				PushInnerVO pushInnerVO = (PushInnerVO) JSONUtil.toJavaBean(PushInnerVO.class,
						new JSONObject(json.getString("data")));
				String type = pushInnerVO.getType();
				if (type.equals(PushType.systemmsg.getValue()) && app.isLogin) {
					EMConversation conversation = new EMConversation(Config.SYSTEM_PUSH);
					EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
					conversation.addMessage(message);
					DBHelper.getInstance(context).insert(pushInnerVO);
					if (BaseActivity.action.contains("MessageActivity")) {
						context.sendBroadcast(new Intent(MessageActivity.class.getName()).putExtra("refresh", true));
					} else if (BaseActivity.action.contains("SystemPushActivity")) {
						context.sendBroadcast(new Intent(SystemPushActivity.class.getName()).putExtra("refresh", true));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// int notifactionId =
			// bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			// Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			// Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
			// Log.e(TAG, "[MyReceiver] " +
			// bundle.getString(JPushInterface.EXTRA_ALERT));
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

			try {
				JSONObject json = new JSONObject(extra);
				PushInnerVO pushInnerVO = (PushInnerVO) JSONUtil.toJavaBean(PushInnerVO.class,
						new JSONObject(json.getString("data")));
				String type = pushInnerVO.getType();
				if (app.isLogin) {
					if (PushType.newversion.getValue().equals(type)) {
						if (!BaseActivity.action.contains("AboutUsActivity")) {
							Intent service = new Intent(context, AboutUsActivity.class);
							service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							service.putExtra("update", true);
							context.startActivity(service);
						} else {
							context.sendBroadcast(
									new Intent(AboutUsActivity.class.getName()).putExtra("newVersion", true));
						}
					} else if (PushType.reservationcancel.getValue().equals(type)) {
						obtainAppointmentDetail(pushInnerVO.getReservationid());
					} else if (PushType.reservationcoachcomment.getValue().equals(type)) {
						obtainAppointmentDetail(pushInnerVO.getReservationid());
					} else if (PushType.reservationsucess.getValue().equals(type)) {
						obtainAppointmentDetail(pushInnerVO.getReservationid());
					} else if (PushType.userapplysuccess.getValue().equals(type)) {

					} else if (PushType.walletupdate.getValue().equals(type)) {
						if (!BaseActivity.action.contains("MyWalletActivity")) {
							intent.setClass(context, MyWalletActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
						}
					} else if (type.equals(PushType.systemmsg.getValue())) {
						if (!BaseActivity.action.contains("SystemPushActivity")) {
							intent.setClass(context, SystemPushActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
						}
					}
				} else {
					intent = new Intent(context, WelcomeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
				intent = new Intent(context, WelcomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			// Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " +
			// bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			// boolean connected =
			// intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE,
			// false);
			// Log.e(TAG, "[MyReceiver]" + intent.getAction() + " connected
			// state change to " + connected);
		} else {
			// Log.e(TAG, "[MyReceiver] Unhandled intent - " +
			// intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private void obtainAppointmentDetail(String id) {
		if (app == null)
			app = BlackCatApplication.getInstance();
		if (!TextUtils.isEmpty(id) && app.userVO != null) {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(appointmentDetail, this,
					Config.IP + "api/v1/courseinfo/userreservationinfo/" + id, null, 10000, headerMap);
		}
	}

	protected String result = "";
	protected String msg = "";
	protected JSONObject data = null;
	protected JSONArray dataArray = null;
	protected String dataString = null;

	@Override
	public boolean doCallBack(String type, Object jsonString) {
		Log.e("", "type=" + type);
		try {
			JSONObject jsonObject = new JSONObject(jsonString.toString());
			result = jsonObject.getString("type");
			msg = jsonObject.getString("msg");
			try {
				data = jsonObject.getJSONObject("data");
			} catch (Exception e2) {
				try {
					dataArray = jsonObject.getJSONArray("data");
				} catch (Exception e3) {
					dataString = jsonObject.getString("data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(msg)) {
			ZProgressHUD.getInstance(context).show();
			ZProgressHUD.getInstance(context).dismissWithFailure(msg, 2000);
			return true;
		}

		try {
			if (type.equals(appointmentDetail)) {
				if (data != null) {
					MyAppointmentVO appointmentVO = (MyAppointmentVO) JSONUtil.toJavaBean(MyAppointmentVO.class, data);
					Intent intent = new Intent(context, AppointmentDetailActivity.class);
					intent.putExtra("appointment", appointmentVO);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void doException(String type, Exception e, int code) {

	}

	@Override
	public void doTimeOut(String type) {

	}

}
