package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.SuccessVO;
import com.sft.vo.UserBaseStateVO;
import com.squareup.picasso.Picasso;

/**
 * 报名成功提示界面
 * 
 * @author Administrator
 * 
 */
public class EnrollSuccessActivity extends BaseActivity {

	private static final String applySuccess = "applySuccess";
	private Button button_sus;
	private TextView tv_qrcode;
	private TextView tvEndTime;
	
	private ImageView imgSchool;
	private TextView tvSchoolName,tvClassType,tvPayMoney,tvApplytime,
	tvState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.apply_commit);
		initView();
		setTitleText(R.string.enroll_infor);
		setListener();
		obtainApplySuccessInfo();
	}

	private void obtainApplySuccessInfo() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(applySuccess, this, Config.IP
				+ "api/v1/userinfo/getapplyschoolinfo", paramMap, 10000,
				headerMap);
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.enroll_success_title);
		app.isEnrollAgain = false;
		showTitlebarBtn(1);
		// showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		// setText(0, R.string.enroll_again);
		setTitleText("");
		button_sus = (Button) findViewById(R.id.button_sus);
		qrcode = (ImageView) findViewById(R.id.apply_commit_qrcode);
		tv_qrcode = (TextView) findViewById(R.id.tv_qrcode);
		carryData = (TextView) findViewById(R.id.apply_commit_carry_data);
		tvEndTime = (TextView) findViewById(R.id.act_apply_suuccess_endtime);
		
		imgSchool = (ImageView) findViewById(R.id.apply_commit_img);
		tvSchoolName = (TextView) findViewById(R.id.apply_commit_school_name);
		tvClassType  = (TextView) findViewById(R.id.apply_commit_class_type);
		tvPayMoney  = (TextView) findViewById(R.id.apply_commit_money);
		tvApplytime  = (TextView) findViewById(R.id.apply_commit_apply_time);
		tvState  = (TextView) findViewById(R.id.apply_commit_pay_state);
	}
	
	
//	private void obtainApplySuccessInfo() {
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("userid", app.userVO.getUserid());
//
//		Map<String, String> headerMap = new HashMap<String, String>();
//		headerMap.put("authorization", app.userVO.getToken());
//		HttpSendUtils.httpGetSend("applySchoolInfor", this, Config.IP
//				+ "api/v1/userinfo/getapplyschoolinfo", paramMap, 10000,
//				headerMap);
//	}

	private void setListener() {
		button_sus.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			setResult(9,getIntent());
			finish();
			break;
		case R.id.base_right_tv:
			// sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
			// "isEnrollSuccess", true));
			// finish();
			LogUtil.print(EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue()
					+ "==========" + app.userVO.getApplystate());
			if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
					app.userVO.getApplystate())) {
				// ZProgressHUD.getInstance(this).show();
				// ZProgressHUD.getInstance(this).dismissWithSuccess(
				// "审核已通过，不能再重新报名！");
			} else {

				app.isEnrollAgain = true;
				app.userVO.setApplystate(EnrollResult.SUBJECT_NONE.getValue());
				Intent intent = new Intent(this, ApplyActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		case R.id.button_sus:
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
			setResult(9,getIntent());
			finish();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			sendBroadcast(new Intent(MainActivity.class.getName()).putExtra(
					"isEnrollSuccess", true));
		}
		return super.onKeyDown(keyCode, event);
	}

	private void checkUserEnrollState() {
		if (app.userVO.getApplystate().equals(
				Config.EnrollResult.SUBJECT_NONE.getValue())) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("userid", app.userVO.getUserid());
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(checkEnrollState, this, Config.IP
					+ "api/v1/userinfo/getmyapplystate", paramsMap, 10000,
					headerMap);
		} else {
		}
	}

	private static final String checkEnrollState = "checkEnrollState";
	private ImageView qrcode;
	private TextView carryData;

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (type.equals(applySuccess)) {
				if (data != null) {
					SuccessVO successVO = JSONUtil.toJavaBean(SuccessVO.class,
							data);
					if (successVO == null) {
						LogUtil.print("successVO");
					}
					if (successVO != null) {
						// 保存报名信息
						setQrCode(successVO.scanauditurl);
						tv_qrcode.setText(successVO.userid);
						// app.userVO.setApplystate(EnrollResult.SUBJECT_ENROLLING
						// .getValue());
						checkUserEnrollState();
						if (successVO.applynotes != null) {
							carryData.setText(successVO.applynotes);
						}
						
						tvEndTime.setText("请您于 "+successVO.endtime+" 前携带资料前往您所报名的驾校确认报名信息，并支付报名费用。");
					}
				}
			} else if (type.equals(checkEnrollState)) {
				if (data != null) {
					UserBaseStateVO baseStateVO = JSONUtil.toJavaBean(
							UserBaseStateVO.class, data);
					if (!baseStateVO.getApplystate().equals(
							app.userVO.getApplystate())) {
						app.userVO.setApplystate(baseStateVO.getApplystate());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void setOffLine(SuccessVO successVO){
//		successVO.applyschoolinfo.name
//		successVO.applyclasstypeinfo.name;  //课程名称
//		successVO.applyclasstypeinfo.price;
//		successVO.applytime;// 报名时间
//		successVO.applystate //申请状态
//		successVO.applyclasstypeinfo.paytypestatus;//支付状态
//		successVO.applyclasstypeinfo.paytype  1 线下支付  2线上支付
		
//		tvSchoolName,tvClassType,tvPayMoney,tvApplytime,
//		tvPayState
		tvSchoolName.setText(successVO.applyschoolinfo.name);
		tvClassType.setText(successVO.applyclasstypeinfo.name);
		tvPayMoney.setText("实付款: ￥"+successVO.applyclasstypeinfo.price);
		tvApplytime.setText("报名时间:"+successVO.applytime);//UTC2LOC.instance.getDate(pay.creattime, "yyyy-MM-dd HH:mm:ss")
		
		LogUtil.print("paytypestatus---->" + successVO.paytypestatus);
		
		if(successVO.paytype.equals("1")){//线下支付
//			tvTitle.setText(successVO.applyschoolinfo.name+"(线下)");
			if(successVO.paytypestatus==20){//申请成功
				tvState.setText("报名成功");
			}else if(successVO.paytypestatus == 0){//未支付
				tvState.setText("未支付");
			}else if(successVO.paytypestatus == 30){//支付失败
				tvState.setText("支付失败");
			}
			
			
		}else{//线上支付
			if(successVO.paytypestatus==20){//申请成功
				tvState.setText("报名成功");
			}else if(successVO.paytypestatus == 0){//未支付
				tvState.setText("未支付");
			}else if(successVO.paytypestatus == 30){//支付失败
				tvState.setText("支付失败");
			}
		}
		if(!TextUtils.isEmpty(successVO.schoollogoimg)){
			LinearLayout.LayoutParams  headParams = (LinearLayout.LayoutParams) imgSchool
					.getLayoutParams();
			BitmapManager.INSTANCE.loadBitmap2(successVO.schoollogoimg, imgSchool,
					headParams.width, headParams.height);
		}
		
		
	}

	// 设置二维码
	private void setQrCode(String scanauditurl) {
		if (!TextUtils.isEmpty(scanauditurl)) {
			String url = Config.IP + "api/v1/create_qrcode?text="
					+ scanauditurl + "&size=10";
			System.out.println(url);
			// BitmapManager.INSTANCE.loadBitmap2(url, qrcode, headParam.width,
			// headParam.height);
			Picasso.with(getBaseContext()).load(url).into(qrcode);
		} else {
			qrcode.setImageResource(R.drawable.default_small_pic);
		}
	}

}
