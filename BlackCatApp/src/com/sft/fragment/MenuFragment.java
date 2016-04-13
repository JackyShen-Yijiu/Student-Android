package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.listener.ICallBack;

import com.google.gson.reflect.TypeToken;
import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.api.ApiHttpClient;
import com.sft.blackcatapp.ActivitiesActivity;
import com.sft.blackcatapp.ConsultationlActivity;
import com.sft.blackcatapp.EnrollSchoolActivity1;
import com.sft.blackcatapp.MessageActivity;
import com.sft.blackcatapp.NewActivitysActivity;
import com.sft.blackcatapp.NewComplaintActivity;
import com.sft.blackcatapp.NewPersonCenterAct;
import com.sft.blackcatapp.OrderExchangeGoodAct;
import com.sft.blackcatapp.SchoolBusRouteActivity;
import com.sft.blackcatapp.SettingActivity;
import com.sft.blackcatapp.TodaysAppointmentActivity;
import com.sft.blackcatapp.WalletActivity;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.BaseUtils;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.ActivitiesVO;
import com.sft.vo.LocationShowTypeVO;
import com.sft.vo.MyMoneyVO;
import com.sft.vo.UserVO;

public class MenuFragment extends Fragment implements OnItemClickListener,
		OnClickListener, ICallBack {
	// private ListView listView;
	private ArrayList<HashMap<String, String>> mMenuTitles;
	private SLMenuListOnItemClickListener mCallback;
	private SelectableRoundedImageView personIcon;
	private final static String openCity = "openCity";
	private static final String mymoney = "mymoney";
	private static final String userinfo = "userinfo";
	BlackCatApplication app;
	private TextView username;
	// private TextView drivingSchool;
	private TextView code;
	private Context mContext;
	// 放到钱包
	// private TextView earnings;
	// private TextView money;
	// private TextView couponcount;
	// private TextView phone;
	private String result;
	private String msg;

	private int showType;// 展示类型： 0驾校 1 教练;

	// private TextView left_tv_map;
	private String currCity;

	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (app == null) {
			app = BlackCatApplication.getInstance();
		}

		currCity = app.curCity;

		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.left_fragment_menu, null);
		initView(rootView);
		setRightText();
		// setData();
		initData();
		setListener();
		return rootView;
	}

	private void initData() {
		obtainMyMoney();

	}

	private void obtainMyMoney() {
		if (app.isLogin) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("usertype", "1");

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(mymoney, this, Config.IP
					+ "api/v1/userinfo/getmymoney", paramMap, 10000, headerMap);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.print("onResume---->menuFragment");
		if (app.isLogin) {
			obtainMyMoney();
			obtainPersionInfo();
		}
	}

	private void initView(View rootView) {

		// left_tv_map = (TextView) rootView.findViewById(R.id.left_tv_map);
		username = (TextView) rootView
				.findViewById(R.id.fragment_menu_username);
		// phone = (TextView) rootView.findViewById(R.id.fragment_menu_phone);
		// drivingSchool = (TextView) rootView
		// .findViewById(R.id.fragment_menu_driving_school);
		code = (TextView) rootView.findViewById(R.id.fragment_menu_code);
		// doubiNumber = (TextView) rootView
		// .findViewById(R.id.fragment_menu_number);
		// earnings = (TextView) rootView
		// .findViewById(R.id.fragment_menu_integrate_earnings);
		// couponcount = (TextView) rootView
		// .findViewById(R.id.fragment_menu_mall_couponcount);
		// money = (TextView) rootView.findViewById(R.id.fragment_menu_money);

		personIcon = (SelectableRoundedImageView) rootView
				.findViewById(R.id.fragment_menu_headpic_im);

		personIcon.setScaleType(ScaleType.CENTER_CROP);
		personIcon.setImageResource(R.drawable.login_head);
		personIcon.setOval(true);
		// listView.setCacheColorHint(android.R.color.transparent);
		// listView.setDividerHeight(0);
		// listView.setSelector(R.drawable.drawer_list_item_selector);

		if (app.isLogin) {

			setPersonInfo();
		}
		rootView.findViewById(R.id.fragment_menu_message_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_activity_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_signin_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_setting_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_wallet_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_complaint_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_classcar_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_information_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_myindent_btn)
				.setOnClickListener(this);

	}

	private void setPersonInfo() {
		RelativeLayout.LayoutParams headpicParam = (android.widget.RelativeLayout.LayoutParams) personIcon
				.getLayoutParams();
		String url = app.userVO.getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			personIcon.setImageResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, personIcon,
					headpicParam.width, headpicParam.height);
		}
		if (TextUtils.isEmpty(app.userVO.getDisplaymobile())) {

			username.setText("未登陆");
		} else {
			LogUtil.print("name:::=-->" + app.userVO.getName());
			if (app.userVO.getName() != null
					&& app.userVO.getName().trim().length() > 0) {
				username.setText(app.userVO.getName());
			} else {
				username.setText(app.userVO.getDisplaymobile());
			}

			// } else {
			// username.setText(app.userVO.getDisplaymobile());
			// }

			// if (TextUtils.isEmpty(app.userVO.getApplyschoolinfo().getName()))
			// {
			//
			// drivingSchool.setText("您未选择驾校");
			// } else {
			// drivingSchool
			// .setText(app.userVO.getApplyschoolinfo().getName());
			// }
		}
	}

	// private void setData() {
	// mMenuTitles = new ArrayList<HashMap<String, String>>();
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("item", "首页");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "查找驾校");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "消息");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "钱包");
	// mMenuTitles.add(map);
	// map = new HashMap<String, String>();
	// map.put("item", "我");
	// mMenuTitles.add(map);
	// myAdapter = new MyAdapter(getActivity(), mMenuTitles, icons);
	// listView.setAdapter(myAdapter);
	// }

	private void setListener() {
		// listView.setOnItemClickListener(this);
		personIcon.setOnClickListener(this);
		// phone.setOnClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, String> item = mMenuTitles.get(position);
		if (mCallback != null && item.containsKey("item")) {
			mCallback.selectItem(position, item.get("item"));
		}
	}

	/**
	 * 左侧菜单 点击回调接口
	 * 
	 * @author FX_SKY
	 * 
	 */
	public interface SLMenuListOnItemClickListener {

		public void selectItem(int position, String title);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.fragment_menu_headpic_im:
			if (app.isLogin) {
				intent = new Intent(getActivity(), NewPersonCenterAct.class);
				startActivity(intent);

			} else {
				BaseUtils.toLogin(getActivity());
				// NoLoginDialog dialog = new NoLoginDialog(getActivity());
				// dialog.show();
			}
			break;
		// 我的消息
		case R.id.fragment_menu_message_btn:
			if (app.isLogin) {
				intent = new Intent(mContext, MessageActivity.class);
				mContext.startActivity(intent);
			} else {
				BaseUtils.toLogin(getActivity());
				// NoLoginDialog dialog = new NoLoginDialog(mContext);
				// dialog.show();
			}
			break;
		// 活动
		case R.id.fragment_menu_activity_btn:
			if (app.isLogin) {
				Intent inten = new Intent(mContext, NewActivitysActivity.class);
				startActivity(inten);
			} else {
				BaseUtils.toLogin(getActivity());
			}
			break;
		case R.id.fragment_menu_signin_btn:
			if (app.isLogin) {
				intent = new Intent(mContext, TodaysAppointmentActivity.class);
				mContext.startActivity(intent);
			} else {
				BaseUtils.toLogin(getActivity());
				// NoLoginDialog dialog = new NoLoginDialog(mContext);
				// dialog.show();
			}
			break;

		// 设置
		case R.id.fragment_menu_setting_btn:
			intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			break;

		// 钱包
		case R.id.fragment_menu_wallet_btn:
			if (app.isLogin) {
				intent = new Intent(getActivity(), WalletActivity.class);
				startActivity(intent);
			} else {
				BaseUtils.toLogin(getActivity());
				// NoLoginDialog dialog = new NoLoginDialog(mContext);
				// dialog.show();
			}
			break;

		// 投诉
		case R.id.fragment_menu_complaint_btn:
			if (app.isLogin) {
				if (app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {

					intent = new Intent(getActivity(),
							NewComplaintActivity.class);
					startActivity(intent);
				} else {
					ZProgressHUD.getInstance(mContext).show();
					ZProgressHUD.getInstance(mContext).dismissWithFailure(
							"请等待审核");
				}
			} else {
				BaseUtils.toLogin(getActivity());
				// NoLoginDialog dialog = new NoLoginDialog(getActivity());
				// dialog.show();
			}
			break;
		case R.id.fragment_menu_myindent_btn:
			// 我的订单
			if (app.isLogin) {
				startActivityForResult(new Intent(mContext,
						OrderExchangeGoodAct.class), 9);
			} else {
				BaseUtils.toLogin(getActivity());
				// NoLoginDialog dialog = new NoLoginDialog(getActivity());
				// dialog.show();
			}
			break;

		case R.id.fragment_menu_information_btn:
			intent = new Intent(mContext, ConsultationlActivity.class);
			startActivity(intent);
			// 咨询
			break;
		case R.id.fragment_menu_classcar_btn:
			// 班车
			if (app != null && app.userVO != null
					&& app.userVO.getApplyschoolinfo() != null) {
				intent = new Intent(mContext, SchoolBusRouteActivity.class);
				intent.putExtra(schoolId, app.userVO.getApplyschoolinfo()
						.getId());
				mContext.startActivity(intent);
			} else {
				BaseUtils.toLogin(getActivity());
			}
			break;
		default:
			break;
		}
	}

	public static final String schoolId = "schoolId";

	protected void setRightText() {
		// Drawable cityIcon = getResources().getDrawable(
		// R.drawable.location_left_city);
		// left_tv_map.setCompoundDrawablesWithIntrinsicBounds(cityIcon, null,
		// null, null);
		// left_tv_map.setText(currCity);
	}

	private void obtainOpenCity() {
		HttpSendUtils.httpGetSend(openCity, this, Config.IP
				+ "api/v1/getopencity");

	}

	@Override
	public boolean doCallBack(String type, Object jsonString) {
		try {

			JSONObject jsonObject = new JSONObject(jsonString.toString());
			String msg = jsonObject.getString("msg");
			JSONObject data = jsonObject.getJSONObject("data");
			if (type.equals(mymoney)) {
				if (data != null) {
					MyMoneyVO myMoneyVO = JSONUtil.toJavaBean(MyMoneyVO.class,
							data);
					if (myMoneyVO != null) {
						setData(myMoneyVO);
						app.userVO.setFcode(myMoneyVO.getFcode());
					}
				}

				if (!TextUtils.isEmpty(msg)) {
					ZProgressHUD.getInstance(mContext).show();
					ZProgressHUD.getInstance(mContext).dismissWithFailure(msg,
							2000);
					return true;
				}
			} else if (type.equals(userinfo)) {
				if (data != null) {
					UserVO userVO = JSONUtil.toJavaBean(UserVO.class, data);
					if (userVO != null) {
						app.userVO
								.setApplycoachinfo(userVO.getApplycoachinfo());
						app.userVO.setApplyclasstypeinfo(userVO
								.getApplyclasstypeinfo());
						app.userVO.setApplyschoolinfo(userVO
								.getApplyschoolinfo());
						// app.userVO.setApplystate(userVO.getApplystate());
						setPersonInfo();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (code == 0) {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithFailure("网络异常", 2000);
		} else if (code == 500) {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext)
					.dismissWithFailure("服务器异常", 2000);
		} else {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithFailure(
					"type= " + type + " 异常  code=" + code, 2000);
		}
		if (e != null)
			e.printStackTrace();

	}

	@Override
	public void doTimeOut(String type) {
		ZProgressHUD.getInstance(mContext).show();
		ZProgressHUD.getInstance(mContext).dismissWithFailure(
				"type=" + type + " 超时");
	}

	private void setData(MyMoneyVO myMoneyVO) {
		app.currency = myMoneyVO.getWallet();
		LogUtil.print(myMoneyVO.getWallet() + "--");
		code.setText(myMoneyVO.getFcode());
		// if (TextUtils.isEmpty(myMoneyVO.getWallet())) {
		// doubiNumber.setText("0.00");
		//
		// }
		// doubiNumber.setText(myMoneyVO.getWallet());
		// earnings.setText(myMoneyVO.getWallet());
		// couponcount.setText(myMoneyVO.getCouponcount());
		// money.setText(myMoneyVO.getMoney());

	}

	private void obtainPersionInfo() {
		// Map<String, String> paramsMap = new HashMap<String, String>();
		// paramsMap.put("userid", app.userVO.getUserid());
		// paramsMap.put("type", "1");
		LogUtil.print(app.userVO.getUserid());
		HttpSendUtils.httpGetSend(userinfo, this,
				Config.IP + "api/v1/userinfo/getuserinfo" + "/1/userid/"
						+ app.userVO.getUserid());
		// HttpSendUtils.httpGetSend(userinfo, this, Config.IP
		// + "api/v1/userinfo/getuserinfo", paramsMap);
	}

	private void obtainActivities() {

		// System.out.println(app.curCity);
		RequestParams params = new RequestParams();
		params.put("cityname", app.curCity);
		ApiHttpClient.get("getactivity", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int paramInt,
							Header[] paramArrayOfHeader, byte[] paramArrayOfByte) {
						String value = parseJson(paramArrayOfByte);
						if (!TextUtils.isEmpty(msg)) {
							// 加载失败，弹出失败对话框
							Toast.makeText(mContext, msg, 0).show();
						} else {
							processSuccess(value);

						}
					}

					@Override
					public void onFailure(int paramInt,
							Header[] paramArrayOfHeader,
							byte[] paramArrayOfByte, Throwable paramThrowable) {

					}
				});

	}

	private void obtainlocationShowType() {

		// System.out.println(app.curCity);
		RequestParams params = new RequestParams();
		params.put("cityname", app.curCity);
		ApiHttpClient.get("getlocationShowType", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int paramInt,
							Header[] paramArrayOfHeader, byte[] paramArrayOfByte) {
						String value = parseJson(paramArrayOfByte);
						if (!TextUtils.isEmpty(msg)) {
							// 加载失败，弹出失败对话框
							Toast.makeText(mContext, msg, 0).show();
						} else {
							processlocationShowTypeResult(value);

						}
					}

					@Override
					public void onFailure(int paramInt,
							Header[] paramArrayOfHeader,
							byte[] paramArrayOfByte, Throwable paramThrowable) {

					}
				});

	}

	protected void processSuccess(String value) {
		if (value != null) {
			LogUtil.print(value);
			try {
				List<ActivitiesVO> activitiesList = (List<ActivitiesVO>) JSONUtil
						.parseJsonToList(value,
								new TypeToken<List<ActivitiesVO>>() {
								}.getType());

				String contenturl = activitiesList.get(0).getContenturl();

				// 打开活动界面
				if (!TextUtils.isEmpty(contenturl)) {
					Intent intent = new Intent(mContext,
							ActivitiesActivity.class);
					intent.putExtra("url", contenturl);
					startActivity(intent);
				} else {
					ZProgressHUD.getInstance(mContext).show();
					ZProgressHUD.getInstance(mContext).dismissWithFailure(
							"没有活动！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void processlocationShowTypeResult(String value) {
		if (value != null) {
			LogUtil.print(value);
			try {
				LocationShowTypeVO locationShowTypeVO = JSONUtil
						.parseJsonToBean(value, LocationShowTypeVO.class);

				showType = locationShowTypeVO.getShowtype();
				openSearchSchool();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void openSearchSchool() {
		if (showType == 0) {
			Intent intent = new Intent(mContext, EnrollSchoolActivity1.class);
			startActivityForResult(intent, 1);
		} else {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithFailure(
					"该地区没有驾校，请选择教练");
		}
	}

	private String parseJson(byte[] responseBody) {
		String value = null;
		JSONObject dataObject = null;
		JSONArray dataArray = null;
		String dataString = null;
		try {

			JSONObject jsonObject = new JSONObject(new String(responseBody));
			result = jsonObject.getString("type");
			msg = jsonObject.getString("msg");
			try {
				dataObject = jsonObject.getJSONObject("data");

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

		if (dataObject != null) {
			value = dataObject.toString();
		} else if (dataArray != null) {
			value = dataArray.toString();

		} else if (dataString != null) {
			value = dataString;
		}
		return value;
	}
}
