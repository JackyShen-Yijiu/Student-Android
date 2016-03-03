package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
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
public class NewPersonCenterAct extends BaseActivity implements OnClickListener {

	private RelativeLayout layout;

	private final static String PAY_STATE = "pay_state";

	private static final String chageGender = "changeGender";

	private int selectGenderIndex = 0;
	private CharSequence[] array;

	private boolean first = true;
	private SelectableRoundedImageView headPicIm;
	private TextView tv_nickname, tv_adress, tv_class, tv_name, tv_phone,
			tv_six, tv_school, tv_style;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.act_person_center);
		initView();
		initData();
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
		array = getResources().getTextArray(R.array.gender);

		layout = (RelativeLayout) findViewById(R.id.person_center_layout);
		// 圆形头像
		headPicIm = (SelectableRoundedImageView) findViewById(R.id.person_center_headpic_im);
		headPicIm.setScaleType(ScaleType.CENTER_CROP);
		headPicIm.setImageResource(R.drawable.login_head);
		headPicIm.setOval(true);

		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_adress = (TextView) findViewById(R.id.tv_adress);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_six = (TextView) findViewById(R.id.tv_six);

		tv_class = (TextView) findViewById(R.id.tv_class);
		tv_style = (TextView) findViewById(R.id.tv_style);
		tv_school = (TextView) findViewById(R.id.tv_school);
	}

	private void initData() {

		RelativeLayout.LayoutParams headpicParam = (LayoutParams) headPicIm
				.getLayoutParams();
		String url = app.userVO.getHeadportrait().getOriginalpic();

		if (TextUtils.isEmpty(url)) {
			headPicIm.setBackgroundResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPicIm,
					headpicParam.width, headpicParam.height);
		}

		tv_school.setText(app.userVO.getApplyschoolinfo().getName());
		tv_class.setText(app.userVO.getApplyclasstypeinfo().getName());
		tv_style.setText(app.userVO.getCarmodel().getName());

		tv_six.setText(app.userVO.getGender());
		tv_adress.setText(app.userVO.getAddress());
		tv_phone.setText(app.userVO.getDisplaymobile());
		tv_name.setText(app.userVO.getName());
		tv_nickname.setText(app.userVO.getNickname());
		// 使用欢迎页面 请求到的数据
		// String enrollInfo = SharedPreferencesUtil.getString(this,
		// Config.USER_ENROLL_INFO, null);
		// if (!TextUtils.isEmpty(enrollInfo)) {
		// try {
		// SuccessVO successVO = JSONUtil.toJavaBean(SuccessVO.class,
		// enrollInfo);
		// tv_school.setText(successVO.applyschoolinfo.name);
		// tv_style.setText(successVO.carmodel.code
		// + successVO.carmodel.name);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		// 显示 welcome 更新的数据
		tv_school.setText(app.userVO.getApplyschoolinfo().getName());
		tv_style.setText(app.userVO.getCarmodel().getCode()
				+ app.userVO.getCarmodel().getName());
		tv_class.setText(app.userVO.getApplyclasstypeinfo().getName());

	}

	private void setListener() {
		layout.setOnClickListener(this);

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
			intent = new Intent(this, CropImageActivity.class);
			break;
		// 昵称
		case R.id.rl_nickname:
			intent = new Intent(this, ChangeSignActivity.class);
			intent.putExtra("type", "nickname");
			break;
		// 姓名
		case R.id.rl_name:
			intent = new Intent(this, ChangeSignActivity.class);
			intent.putExtra("type", "name");
			break;
		// 性别
		case R.id.rl_six:
			String gender = app.userVO.getGender();
			int index = -1;
			if (!TextUtils.isEmpty(gender)) {
				if (gender.equals("男")) {
					index = 0;
				} else {
					index = 1;
				}
			}
			showGender(index);
			break;
		// 电话
		case R.id.rl_phone:
			startActivity(new Intent(this, ChangePhoneActivity.class));
			break;
		// 地址
		case R.id.rl_adress:
			startActivity(new Intent(this, ChangeAddressActivity.class));
			break;
		// 我的收藏
		case R.id.rl_collect:
			intent = new Intent(this, MyFavouriteActiviy.class);
			startActivityForResult(intent, R.id.rl_collect);
			break;
		// 报名信息
		case R.id.rl_apply:
			startActivityForResult(new Intent(NewPersonCenterAct.this,
					MyOrderAct.class), 9);
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void showGender(int index) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择性别");
		builder.setSingleChoiceItems(array, index,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectGenderIndex = which;
						changeGender();
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	private void changeGender() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("gender", array[selectGenderIndex].toString());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(chageGender, this, Config.IP
				+ "api/v1/userinfo/updateuserinfo", paramMap, 10000, headerMap);
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
					Intent intent = new Intent(NewPersonCenterAct.this,
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
					Intent intent = new Intent(NewPersonCenterAct.this,
							EnrollSchoolActivity1.class);
					startActivity(intent);
					// finish();
				}
			};
		}
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
						app.userVO.setApplystate(EnrollResult.SUBJECT_NONE
								.getValue());

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
		} else if (type.equals(chageGender)) {
			if (dataString != null) {
				app.userVO.setGender(array[selectGenderIndex].toString());
				initData();
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
				tv_style.setText(carStyle.getName());
				return;
			}
			if (requestCode == R.id.person_center_school_value_tv
					&& resultCode == R.id.base_right_tv) {
				// 更新选择的驾校
				SchoolVO school = (SchoolVO) data
						.getSerializableExtra("school");
				tv_school.setText(school.getName());
				return;
			}
			if (requestCode == R.id.person_center_classtyle_value_tv) {
				// 更新选择的班级
				ClassVO classs = (ClassVO) data.getSerializableExtra("class");
				tv_class.setText(classs.getName());
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
}
