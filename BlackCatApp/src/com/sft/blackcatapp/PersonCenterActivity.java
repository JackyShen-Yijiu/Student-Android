package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.easemob.chat.EMChatManager;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.SharedPreferencesUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.PayOrderVO;
import com.sft.vo.SchoolVO;

/**
 * 个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonCenterActivity extends BaseActivity {

	private LinearLayout layout;
	private ImageView headPicIm;
	private TextView phoneTv, idTv;
	private TextView schoolTv, carStyleTv, favouriteTv, coachTv, settingTv,
			enrollDetailTv;
	private TextView schoolValueTv, carStyleValueTv;

	private Button logoutBtn;
	private TextView testingDetailTV;
	
	private final static String PAY_STATE = "pay_state";
	
	private boolean first = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_person_center);
		initView();
		initData();
		resizeDrawalbeLeftSize();
		setListener();
		//请求 订单状态
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

		layout = (LinearLayout) findViewById(R.id.person_center_layout);
		headPicIm = (ImageView) findViewById(R.id.person_center_headpic_im);
		phoneTv = (TextView) findViewById(R.id.person_center_phone_tv);
		idTv = (TextView) findViewById(R.id.person_center_id_tv);

		schoolTv = (TextView) findViewById(R.id.person_center_school_tv);
		carStyleTv = (TextView) findViewById(R.id.person_center_carstyle_tv);
		favouriteTv = (TextView) findViewById(R.id.person_center_favourite_tv);
		coachTv = (TextView) findViewById(R.id.person_center_coach_tv);
		settingTv = (TextView) findViewById(R.id.person_center_setting_tv);
		enrollDetailTv = (TextView) findViewById(R.id.person_center_enroll_detail_tv);
		schoolValueTv = (TextView) findViewById(R.id.person_center_school_value_tv);
		carStyleValueTv = (TextView) findViewById(R.id.person_center_carstyle_value_tv);
		testingDetailTV = (TextView) findViewById(R.id.person_center_testing_detail_tv);

		logoutBtn = (Button) findViewById(R.id.person_center_logout_btn);

		// if (app.userVO.getApplystate().equals(
		// EnrollResult.SUBJECT_NONE.getValue())) {
		// // 用户没有报名，但可能填写过一些信息
		// SchoolVO school = Util.getEnrollUserSelectedSchool(this);
		// if (school != null) {
		// schoolValueTv.setText(school.getName());
		// }
		//
		// CarModelVO carModel = Util.getEnrollUserSelectedCarStyle(this);
		// if (carModel != null) {
		// carStyleValueTv.setText(carModel.getName());
		// }
		// } else {
		// schoolValueTv.setText(app.userVO.getApplyschoolinfo().getName());
		// carStyleValueTv.setText(app.userVO.getCarmodel().getName());
		// }
	}

	private void initData() {
		phoneTv.setText(app.userVO.getDisplaymobile());
		idTv.setText(app.userVO.getDisplayuserid());

		LinearLayout.LayoutParams headpicParam = (LayoutParams) headPicIm
				.getLayoutParams();

		String url = app.userVO.getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headPicIm.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPicIm,
					headpicParam.width, headpicParam.height);
		}

		schoolValueTv.setText(app.userVO.getApplyschoolinfo().getName());
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

	}

	private void resizeDrawalbeLeftSize() {
		// 显示箭头
		Resources r = getResources();
		int size = (int) (18 * screenDensity);
		Drawable arrow = r.getDrawable(R.drawable.person_center_arrow);
		arrow.setBounds(0, 0, size, size);

		if (app.userVO.getApplystate().equals(Config.EnrollResult.SUBJECT_NONE)) {
			schoolValueTv.setCompoundDrawables(null, null, arrow, null);// 设置左图标
			carStyleValueTv.setCompoundDrawables(null, null, arrow, null);// 设置左图标
		} else {
			schoolValueTv.setCompoundDrawables(null, null, null, null);// 设置左图标
			carStyleValueTv.setCompoundDrawables(null, null, null, null);// 设置左图标
		}

		Drawable school = r.getDrawable(R.drawable.person_center_school);
		school.setBounds(0, 0, size, size);
		schoolTv.setCompoundDrawables(school, null, null, null);// 设置左图标

		Drawable carstyle = r.getDrawable(R.drawable.person_center_carstyle);
		carstyle.setBounds(0, 0, size, size);
		carStyleTv.setCompoundDrawables(carstyle, null, null, null);// 设置左图标

		Drawable favourite = r.getDrawable(R.drawable.person_center_favourite);
		favourite.setBounds(0, 0, size, size);
		favouriteTv.setCompoundDrawables(favourite, null, arrow, null);// 设置左图标

		Drawable coach = r.getDrawable(R.drawable.person_center_coach);
		coach.setBounds(0, 0, size, size);
		coachTv.setCompoundDrawables(coach, null, arrow, null);// 设置左图标

		Drawable setting = r.getDrawable(R.drawable.person_center_setting);
		setting.setBounds(0, 0, size, size);
		settingTv.setCompoundDrawables(setting, null, arrow, null);// 设置左图标
		//
		// Drawable enroll = r.getDrawable(R.drawable.person_center_setting);
		// setting.setBounds(0, 0, size, size);
		// enrollDetailTv.setCompoundDrawables(enroll, null, arrow, null);//
		// 设置左图标
	}

	private void setListener() {
		layout.setOnClickListener(this);
		schoolTv.setOnClickListener(this);
		carStyleTv.setOnClickListener(this);
		favouriteTv.setOnClickListener(this);
		coachTv.setOnClickListener(this);
		settingTv.setOnClickListener(this);
		enrollDetailTv.setOnClickListener(this);
		testingDetailTV.setOnClickListener(this);

		logoutBtn.setOnClickListener(this);
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
			intent = new Intent(this, EditPersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.person_center_carstyle_tv:
			break;
		case R.id.person_center_coach_tv:
			intent = new Intent(this, MyCoachActivity.class);
			startActivity(intent);
			break;
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
			EMChatManager.getInstance().logout(null);
			setTag();
			break;
		case R.id.person_center_school_tv:
			break;
		case R.id.person_center_setting_tv:
			intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		// 报名详情
		case R.id.person_center_enroll_detail_tv:
			
			if(applystate <0){//尚未请求，获取数据
				isApplyOk();
				return;
			}else{
				doAppResult(applystate+"",paytype);
			}
			
			
			
			break;
		// 验证报名信息
		case R.id.person_center_testing_detail_tv:
			Intent intent1 = null;
			String applystates = app.userVO.getApplystate();
			if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
					applystates)) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名成功");
			} else if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
					applystates)) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名，请等待审核");
			} else {
				boolean isclickconfirm = SharedPreferencesUtil.getBoolean(this,
						MainActivity.ISCLICKCONFIRM, false);
//				Log.
				if (isclickconfirm) {
					intent1 = new Intent(PersonCenterActivity.this,
							TestingPhoneActivity.class);
//					startActivity(intent1);
					startActivityForResult(intent1, 8);
				} else {
					intent1 = new Intent(PersonCenterActivity.this,
							EnrollSchoolActivity1.class);
					startActivity(intent1);
					
				}
			}

			break;
		}
	}
	
	/**
	 * 是否报名，线上支付1 提示，已报名，请等待审核，线下支付,进入二维码页面
	 * @return
	 */
	private void isApplyOk(){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
//		LogUtil.print("subject---Id==>"+app.userVO.getSubject().getSubjectid());
//		paramMap.put("subjectid",app.userVO.getSubject().getSubjectid());//订单的状态 // 0 订单生成 2 支付成功 3 支付失败 4 订单取消 -1 全部(未支付的订单)
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(PAY_STATE, this, Config.IP
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
	private void doAppResult(String applyState,int payType){
//		String applystate = app.userVO.getApplystate();
		LogUtil.print("state-->"+applyState+"type::"+payType);
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
		}else if (hasNotPay) {//线上支付,存在未支付订单
			
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
		}else if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
				applyState) && payType == 2) {//线上支付
			
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名,正在等待审核");
			
		}  else if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
				applyState) && payType == 1) {//线下支付 跳转到  二维码页面
			Intent intent1 = new Intent(PersonCenterActivity.this,
					EnrollSuccessActivity.class);
			startActivity(intent1);
		} else if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
				applyState)) {
			// Intent intent1 = new Intent(PersonCenterActivity.this,
			// EnrollSuccessActivity.class);
			// startActivity(intent1);
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名");
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
		if(type.equals(PAY_STATE)){
			LogUtil.print(type+"---"+jsonString);
			try {
				applystate = data.getInt("applystate");//申请状态  0 未报名 1 申请中 2 申请成功 
				paytypestatus = data.getInt("paytypestatus");//0 未支付 20支付成功(等待验证) 30 支付失败 
				paytype = data.getInt("paytype");// 1 线下支付， 2 线上支付
				
				app.userVO.setApplystate(applystate+"");
				if(!first)
					doAppResult(applystate+"",paytype);
				first = false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
		}else if(type.equals("notPay")){
			int length = dataArray.length();
			for (int i = 0; i < length; i++) {
				PayOrderVO pay;
				try {
					pay = JSONUtil.toJavaBean(PayOrderVO.class,
							dataArray.getJSONObject(i));
					if(pay.userpaystate.equals("0") ||pay.userpaystate.equals("3")){//订单刚生成，支付失败
						
						//存在未支付订单
						app.userVO.setApplystate(EnrollResult.SUBJECT_NONE.getValue());
						
						app.isEnrollAgain = true;
						hasNotPay = true;
						break;
					}else{
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
		if(requestCode == 8){//验证报名信息，，
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
