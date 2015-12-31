package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.listener.ICallBack;

import com.joooonho.SelectableRoundedImageView;
import com.sft.blackcatapp.EnrollSchoolActivity;
import com.sft.blackcatapp.MainActivity;
import com.sft.blackcatapp.MessageActivity;
import com.sft.blackcatapp.MyWalletActivity;
import com.sft.blackcatapp.PersonCenterActivity;
import com.sft.blackcatapp.R;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyMoneyVO;

public class MenuFragment extends Fragment implements OnItemClickListener,
		OnClickListener, ICallBack {
	// private ListView listView;
	private ArrayList<HashMap<String, String>> mMenuTitles;
	private SLMenuListOnItemClickListener mCallback;
	private SelectableRoundedImageView personIcon;

	private static final String mymoney = "mymoney";
	BlackCatApplication app;
	private TextView username;
	private TextView drivingSchool;
	private TextView code;
	private Context mContext;
	private TextView doubiNumber;
	private TextView earnings;
	private TextView money;
	private TextView couponcount;
	private TextView phone;

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
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_menu_new, null);
		initView(rootView);
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
		if (app.isLogin) {

			setPersonInfo();
		}
	}

	private void initView(View rootView) {
		username = (TextView) rootView
				.findViewById(R.id.fragment_menu_username);
		phone = (TextView) rootView.findViewById(R.id.fragment_menu_phone);
		drivingSchool = (TextView) rootView
				.findViewById(R.id.fragment_menu_driving_school);
		code = (TextView) rootView.findViewById(R.id.fragment_menu_code);
		doubiNumber = (TextView) rootView
				.findViewById(R.id.fragment_menu_number);
		earnings = (TextView) rootView
				.findViewById(R.id.fragment_menu_integrate_earnings);
		couponcount = (TextView) rootView
				.findViewById(R.id.fragment_menu_mall_couponcount);
		money = (TextView) rootView.findViewById(R.id.fragment_menu_money);
		personIcon = (SelectableRoundedImageView) rootView
				.findViewById(R.id.fragment_menu_headpic_im);
		personIcon.setScaleType(ScaleType.CENTER_CROP);
		personIcon.setImageResource(R.drawable.default_small_pic);
		personIcon.setOval(true);
		// listView.setCacheColorHint(android.R.color.transparent);
		// listView.setDividerHeight(0);
		// listView.setSelector(R.drawable.drawer_list_item_selector);

		if (app.isLogin) {

			setPersonInfo();
		}

		rootView.findViewById(R.id.fragment_menu_content_first)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_content_seccond)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_content_third)
				.setOnClickListener(this);

		// 底部图片点击
		rootView.findViewById(R.id.fragment_menu_home_btn).setOnClickListener(
				this);
		rootView.findViewById(R.id.fragment_menu_driving_school_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_message_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_mall_btn).setOnClickListener(
				this);
		rootView.findViewById(R.id.fragment_menu_activity_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_signin_btn)
				.setOnClickListener(this);
		rootView.findViewById(R.id.fragment_menu_setting_btn)
				.setOnClickListener(this);

	}

	private void setPersonInfo() {
		RelativeLayout.LayoutParams headpicParam = (android.widget.RelativeLayout.LayoutParams) personIcon
				.getLayoutParams();
		String url = app.userVO.getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			personIcon.setImageResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, personIcon,
					headpicParam.width, headpicParam.height);
		}
		if (TextUtils.isEmpty(app.userVO.getDisplaymobile())) {

			username.setText("未登陆");
		} else {
			username.setText(app.userVO.getDisplaymobile());
		}
		if (TextUtils.isEmpty(app.userVO.getApplyschoolinfo().getName())) {

			drivingSchool.setText("您未选择驾校");
		} else {
			drivingSchool.setText(app.userVO.getApplyschoolinfo().getName());
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
		phone.setOnClickListener(this);

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
				intent = new Intent(getActivity(), PersonCenterActivity.class);
				startActivity(intent);

			} else {
				NoLoginDialog dialog = new NoLoginDialog(getActivity());
				dialog.show();
			}
			break;

		case R.id.fragment_menu_home_btn:
			((MainActivity) mContext).changeMenu();
			break;
		case R.id.fragment_menu_driving_school_btn:
			intent = new Intent(mContext, EnrollSchoolActivity.class);
			intent.putExtra("isFromMenu", true);
			mContext.startActivity(intent);
			break;
		case R.id.fragment_menu_message_btn:
			if (app.isLogin) {
				intent = new Intent(mContext, MessageActivity.class);
				mContext.startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(mContext);
				dialog.show();
			}
			break;
		// case R.id.fragment_menu_mall_btn:
		// if (app.isLogin) {
		// intent = new Intent(mContext, MyWalletActivity.class);
		// mContext.startActivity(intent);
		// } else {
		// NoLoginDialog dialog = new NoLoginDialog(mContext);
		// dialog.show();
		// }
		// break;
		case R.id.fragment_menu_activity_btn:

			break;
		case R.id.fragment_menu_signin_btn:

			break;
		case R.id.fragment_menu_setting_btn:

			if (app.isLogin) {
				intent = new Intent(getActivity(), PersonCenterActivity.class);
				startActivity(intent);

			} else {
				NoLoginDialog dialog = new NoLoginDialog(getActivity());
				dialog.show();
			}

			break;
		case R.id.fragment_menu_phone:
			String number = CommonUtil.getString(mContext, R.string.yibu_phone);
			// 用intent启动拨打电话
			Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ number));
			startActivity(intent1);
			break;
		case R.id.fragment_menu_content_first:
			// 积分
			if (app.isLogin) {
				intent = new Intent(mContext, MyWalletActivity.class);
				intent.putExtra("moneytype",
						Config.MoneyType.INTEGRAL_RETURN.getValue());
				mContext.startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(mContext);
				dialog.show();
			}
			break;
		case R.id.fragment_menu_content_seccond:
			// 兑换券
			if (app.isLogin) {
				intent = new Intent(mContext, MyWalletActivity.class);
				intent.putExtra("moneytype",
						Config.MoneyType.COIN_CERTIFICATE.getValue());
				mContext.startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(mContext);
				dialog.show();
			}
			break;
		case R.id.fragment_menu_content_third:
			// 现金
			if (app.isLogin) {
				intent = new Intent(mContext, MyWalletActivity.class);
				intent.putExtra("moneytype",
						Config.MoneyType.AMOUNT_IN_CASH.getValue());
				mContext.startActivity(intent);
			} else {
				NoLoginDialog dialog = new NoLoginDialog(mContext);
				dialog.show();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean doCallBack(String type, Object jsonString) {
		try {

			JSONObject jsonObject = new JSONObject(jsonString.toString());
			String msg = jsonObject.getString("msg");
			JSONObject data = jsonObject.getJSONObject("data");
			if (data != null) {
				MyMoneyVO myMoneyVO = JSONUtil
						.toJavaBean(MyMoneyVO.class, data);
				if (myMoneyVO != null) {
					setData(myMoneyVO);
				}
			}

			if (!TextUtils.isEmpty(msg)) {
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext)
						.dismissWithFailure(msg, 2000);
				return true;
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
		code.setText(myMoneyVO.getFcode());
		if (TextUtils.isEmpty(myMoneyVO.getWallet())) {
			doubiNumber.setText("0.00");

		}
		doubiNumber.setText(myMoneyVO.getWallet());
		earnings.setText(myMoneyVO.getWallet());
		couponcount.setText(myMoneyVO.getCouponcount());
		money.setText(myMoneyVO.getMoney());

	}
}
