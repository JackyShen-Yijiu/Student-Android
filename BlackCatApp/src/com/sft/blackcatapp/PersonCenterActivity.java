package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.PayOrderVO;
import com.sft.vo.SchoolVO;

/**
 * 个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonCenterActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout layout;
	private TextView schoolTv, carStyleTv;
	private TextView schoolValueTv, carStyleValueTv;

	private final static String PAY_STATE = "pay_state";

	private boolean first = true;
	private TextView classValueTv;
	private SelectableRoundedImageView headPicIm;
	private RelativeLayout enrollDetailTv;
	private RelativeLayout favouriteTv;
	private RelativeLayout rl_nickname, rl_adress, rl_class, rl_name, rl_phone,
			rl_six, rl_school, rl_style, rl_apply, rl_collect;
	private TextView tv_nickname, tv_adress, tv_class, tv_name, tv_phone,
			tv_six, tv_school, tv_style, tv_apply, tv_collect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.new_activity_person_center);
		initView();
		initData();
		// resizeDrawalbeLeftSize();
		setListener();
		// 请求 订单状态
		isApplyOk();
		requestNotFinshOrder();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		initData();
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.person_center);

		layout = (RelativeLayout) findViewById(R.id.person_center_layout);
		// 圆形头像
		headPicIm = (SelectableRoundedImageView) findViewById(R.id.person_center_headpic_im);
		headPicIm.setScaleType(ScaleType.CENTER_CROP);
		headPicIm.setImageResource(R.drawable.login_head);
		headPicIm.setOval(true);

		// phoneTv = (TextView) findViewById(R.id.person_center_phone_tv);
		// idTv = (TextView) findViewById(R.id.person_center_id_tv);

		schoolTv = (TextView) findViewById(R.id.person_center_school_tv);
		carStyleTv = (TextView) findViewById(R.id.person_center_carstyle_tv);
		favouriteTv = (RelativeLayout) findViewById(R.id.person_center_favourite_tv);

		// 我的教练
		// coachTv = (TextView) findViewById(R.id.person_center_coach_tv);
		enrollDetailTv = (RelativeLayout) findViewById(R.id.person_center_enroll_detail_tv);
		schoolValueTv = (TextView) findViewById(R.id.person_center_school_value_tv);
		carStyleValueTv = (TextView) findViewById(R.id.person_center_carstyle_value_tv);
		classValueTv = (TextView) findViewById(R.id.person_center_classtyle_value_tv);

		// rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
		// rl_adress = (RelativeLayout) findViewById(R.id.rl_adress);
		// rl_name = (RelativeLayout) findViewById(R.id.rl_name);
		// rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
		// rl_class = (RelativeLayout) findViewById(R.id.rl_class);
		// rl_six = (RelativeLayout) findViewById(R.id.rl_six);
		// rl_collect = (RelativeLayout) findViewById(R.id.rl_collect);
		// rl_style = (RelativeLayout) findViewById(R.id.rl_style);
		// rl_apply = (RelativeLayout) findViewById(R.id.rl_apply);
		// rl_school = (RelativeLayout) findViewById(R.id.rl_school);

		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_adress = (TextView) findViewById(R.id.tv_adress);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_class = (TextView) findViewById(R.id.tv_class);
		tv_six = (TextView) findViewById(R.id.tv_six);
		tv_collect = (TextView) findViewById(R.id.tv_collect);
		tv_style = (TextView) findViewById(R.id.tv_style);
		tv_apply = (TextView) findViewById(R.id.tv_apply);
		tv_school = (TextView) findViewById(R.id.tv_school);
	}

	private void initData() {
		// phoneTv.setText(app.userVO.getDisplaymobile());
		//
		// idTv.setText(app.userVO.getDisplayuserid());

		RelativeLayout.LayoutParams headpicParam = (LayoutParams) headPicIm
				.getLayoutParams();
		String url = app.userVO.getHeadportrait().getOriginalpic();

		if (TextUtils.isEmpty(url)) {
			headPicIm.setBackgroundResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPicIm,
					headpicParam.width, headpicParam.height);
		}

		schoolValueTv.setText(app.userVO.getApplyschoolinfo().getName());
		classValueTv.setText(app.userVO.getApplyclasstypeinfo().getName());
		carStyleValueTv.setText(app.userVO.getCarmodel().getName());

		// 使用欢迎页面 请求到的数据
		// String enrollInfo = SharedPreferencesUtil.getString(this,
		// Config.USER_ENROLL_INFO, null);
		// if (!TextUtils.isEmpty(enrollInfo)) {
		// try {
		// SuccessVO successVO = JSONUtil.toJavaBean(SuccessVO.class,
		// enrollInfo);
		// schoolValueTv.setText(successVO.applyschoolinfo.name);
		// carStyleValueTv.setText(successVO.carmodel.code
		// + successVO.carmodel.name);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		// 显示 welcome 更新的数据
		schoolValueTv.setText(app.userVO.getApplyschoolinfo().getName());
		carStyleValueTv.setText(app.userVO.getCarmodel().getCode()
				+ app.userVO.getCarmodel().getName());
		classValueTv.setText(app.userVO.getApplyclasstypeinfo().getName());

	}

	private void setListener() {
		layout.setOnClickListener(this);
		favouriteTv.setOnClickListener(this);
		enrollDetailTv.setOnClickListener(this);

		if (app.userVO.getApplystate().equals(
				EnrollResult.SUBJECT_NONE.getValue())) {
			schoolValueTv.setOnClickListener(this);
			carStyleValueTv.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.person_center_layout:
			intent = new Intent(this, NewEditPersonInfo.class);
			startActivity(intent);
			break;
		case R.id.person_center_carstyle_tv:
			break;
		// 我的教练
		// case R.id.person_center_coach_tv:
		// intent = new Intent(this, MyCoachActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.person_center_school_value_tv:
		// intent = new Intent(this, EnrollSchoolActivity.class);
		// SchoolVO school = Util.getEnrollUserSelectedSchool(this);
		// if (school != null) {
		// intent.putExtra("school", school);
		// }
		// startActivityForResult(intent, R.id.person_center_school_value_tv);
		// break;
		// case R.id.person_center_carstyle_value_tv:
		// intent = new Intent(this, EnrollCarStyleActivity.class);
		// CarModelVO carModel = Util.getEnrollUserSelectedCarStyle(this);
		// if (carModel != null) {
		// intent.putExtra("carStyle", carModel);
		// }
		// startActivityForResult(intent, R.id.person_center_carstyle_value_tv);
		// break;
		case R.id.person_center_favourite_tv:
			intent = new Intent(this, MyFavouriteActiviy.class);
			startActivityForResult(intent, R.id.person_center_favourite_tv);
			break;
		case R.id.person_center_logout_btn:
			ZProgressHUD.getInstance(this).setMessage("正在退出登录...");
			ZProgressHUD.getInstance(this).show();
			LogUtil.print("isLogin--->" + app.isLogin);
			// EMChatManager.getInstance().logout(null);
			setTag();
			break;
		case R.id.person_center_school_tv:
			break;
		// 报名详情
		case R.id.person_center_enroll_detail_tv:

			startActivityForResult(new Intent(PersonCenterActivity.this,
					MyOrderAct.class), 9);

			// if (applystate < 0) {// 尚未请求，获取数据
			// isApplyOk();
			// return;
			// } else {
			// doAppResult(applystate + "", paytype);
			// }

			break;
		// 验证报名信息
		// case R.id.person_center_testing_detail_tv:
		// Intent intent1 = null;
		// String applystates = app.userVO.getApplystate();
		// if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
		// applystates)) {
		// ZProgressHUD.getInstance(this).show();
		// ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名成功");
		// } else if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
		// applystates)) {
		// ZProgressHUD.getInstance(this).show();
		// ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名，请等待审核");
		// } else {
		// boolean isclickconfirm = SharedPreferencesUtil.getBoolean(this,
		// MainActivity.ISCLICKCONFIRM, false);
		// // Log.
		// if (isclickconfirm) {
		// intent1 = new Intent(PersonCenterActivity.this,
		// TestingPhoneActivity.class);
		// // startActivity(intent1);
		// startActivityForResult(intent1, 8);
		// } else {
		// intent1 = new Intent(PersonCenterActivity.this,
		// EnrollSchoolActivity1.class);
		// startActivity(intent1);
		//
		// }
		// }
		//
		// break;
		}
	}

	/**
	 * 是否报名，线上支付1 提示，已报名，请等待审核，线下支付,进入二维码页面
	 * 
	 * @return
	 */
	private void isApplyOk() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		// LogUtil.print("subject---Id==>"+app.userVO.getSubject().getSubjectid());
		// paramMap.put("subjectid",app.userVO.getSubject().getSubjectid());//订单的状态
		// // 0 订单生成 2 支付成功 3 支付失败 4 订单取消 -1 全部(未支付的订单)

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils
				.httpGetSend(PAY_STATE, this, Config.IP
						+ "api/v1/userinfo/getmyapplystate", paramMap, 10000,
						headerMap);
	}

	/**
	 * 获取未完成订单
	 */
	private void requestNotFinshOrder() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("orderstate", "0");// 订单的状态 // 0 订单生成 2 支付成功 3 支付失败 4 订单取消
										// -1 全部(未支付的订单)

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend("notPay", this, Config.IP
				+ "api/v1/userinfo/getmypayorder", paramMap, 10000, headerMap);

	}

	/**
	 * 处理结果
	 */
	private void doAppResult(String applyState, int payType) {
		// String applystate = app.userVO.getApplystate();
		LogUtil.print("state-->" + applyState + "type::" + payType);
		if (EnrollResult.SUBJECT_NONE.getValue().equals(applyState)) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("您还没有报名");

			new MyHandler(1000) {
				@Override
				public void run() {
					Intent intent = new Intent(PersonCenterActivity.this,
							EnrollSchoolActivity1.class);
					startActivity(intent);
					// finish();
				}
			};
		} else if (hasNotPay) {// 线上支付,存在未支付订单

			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithSuccess("存在未支付订单，请支付");
			new MyHandler(1000) {
				@Override
				public void run() {
					Intent intent = new Intent(PersonCenterActivity.this,
							EnrollSchoolActivity1.class);
					startActivity(intent);
					// finish();
				}
			};
		}
		// else if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(applyState)
		// && payType == 2) {// 线上支付
		//
		// ZProgressHUD.getInstance(this).show();
		// ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名,正在等待审核");
		//
		// } else if
		// (EnrollResult.SUBJECT_ENROLLING.getValue().equals(applyState)
		// && payType == 1) {// 线下支付 跳转到 二维码页面
		// Intent intent1 = new Intent(PersonCenterActivity.this,
		// EnrollSuccessActivity.class);
		// startActivity(intent1);
		// } else if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
		// applyState)) {
		// // Intent intent1 = new Intent(PersonCenterActivity.this,
		// // EnrollSuccessActivity.class);
		// // startActivity(intent1);
		// ZProgressHUD.getInstance(this).show();
		// ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名");
		// }
	}

	int applystate = -2;
	int paytypestatus = -2;
	int paytype = -2;
	boolean hasNotPay = false;

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(PAY_STATE)) {
			LogUtil.print(type + "---" + jsonString);
			try {
				applystate = data.getInt("applystate");// 申请状态 0 未报名 1 申请中 2
														// 申请成功
				paytypestatus = data.getInt("paytypestatus");// 0 未支付
																// 20支付成功(等待验证)
																// 30 支付失败
				paytype = data.getInt("paytype");// 1 线下支付， 2 线上支付
				if (app.userVO != null)
					app.userVO.setApplystate(applystate + "");
				if (!first)
					doAppResult(applystate + "", paytype);
				first = false;
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (type.equals("notPay")) {
			int length = dataArray.length();
			for (int i = 0; i < length; i++) {
				PayOrderVO pay;
				try {
					pay = JSONUtil.toJavaBean(PayOrderVO.class,
							dataArray.getJSONObject(i));
					if (pay.userpaystate.equals("0")
							|| pay.userpaystate.equals("3")) {// 订单刚生成，支付失败

						// 存在未支付订单
						// app.userVO.setApplystate(EnrollResult.SUBJECT_NONE
						// .getValue());

						app.isEnrollAgain = true;
						hasNotPay = true;
						break;
					} else {
						hasNotPay = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		// Toast("onactivityResult");
		if (requestCode == 8) {// 验证报名信息，，
			first = true;
			isApplyOk();
		}
		if (data != null) {
			if (requestCode == R.id.person_center_carstyle_value_tv) {
				// 更新
				CarModelVO carStyle = (CarModelVO) data
						.getSerializableExtra("carStyle");
				carStyleValueTv.setText(carStyle.getName());
				return;
			}
			if (requestCode == R.id.person_center_school_value_tv
					&& resultCode == R.id.base_right_tv) {
				// 更新选择的驾校
				SchoolVO school = (SchoolVO) data
						.getSerializableExtra("school");
				schoolValueTv.setText(school.getName());
				return;
			}
			if (requestCode == R.id.person_center_classtyle_value_tv) {
				// 更新选择的班级
				ClassVO classs = (ClassVO) data.getSerializableExtra("class");
				classValueTv.setText(classs.getName());
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(RESULT_OK, data);
					finish();
				}
			};
		}
		if (requestCode == 9 && resultCode == 0) {// 结束当前页面
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(RESULT_OK, data);
					finish();
				}
			};
		}

	}

	private void setTag() {
		if (app.isLogin) {
			JPushInterface.setAlias(this, "", new MyTagAliasCallback());
		}
	}

	private int sum = 0;

	private class MyTagAliasCallback implements TagAliasCallback {

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			Toast("callback");
			LogUtil.print("MyTagCallback---->" + arg1);
			sum++;
			if (arg0 != 0 && sum < 5) {
				setTag();
			} else {
				ZProgressHUD.getInstance(PersonCenterActivity.this).dismiss();
				util.saveParam(Config.LAST_LOGIN_PASSWORD, "");
				Intent intent = new Intent(PersonCenterActivity.this,
						LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}

	}
}
