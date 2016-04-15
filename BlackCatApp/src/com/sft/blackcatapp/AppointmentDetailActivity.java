package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.AppointmentDetailStudentHoriListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.UserType;
import com.sft.qrcode.EncodingHandler;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.QRCodeCreateVO;
import com.sft.vo.commentvo.CommentUser;

/**
 * 预约详情
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class AppointmentDetailActivity extends BaseActivity implements
		OnClickListener {

	private static final String appointmentDetail = "appointmentDetail";
	//
	private MyAppointmentVO appointmentVO;
	//
	private int studentPage = 1;
	//
	private List<CommentUser> userList = new ArrayList<CommentUser>();
	//
	private AppointmentDetailStudentHoriListAdapter adapter;

	private Button cancelBtn;
	private ImageView qrcodeIv;
	private TextView signKnowTv;
	private TextView classNameTv;
	private TextView timeTv;
	private TextView coachNameTv;
	private TextView trainingGroundTv;
	private ImageView coachDuihuaIv;
	private TextView signInTimeTv;
	private TextView stopCarTv;
	private RatingBar ratingBar;
	private TextView evaluateContentTv;
	private TextView evaluateTimeTv;
	private LinearLayout commentLl;
	private TextView appointState;
	private TextView appointInfoTv;
	private RelativeLayout stopCarRl;
	private RelativeLayout signInTimeRl;
	private View signInTimeLine;
	private View stopCarLine;
	private LinearLayout cancelBtnLl;
	private TextView cancelBtnHintTv;
	private LinearLayout coachRefuseLl;
	private TextView coachRefuseReasonTv;
	private TextView coachRefuseTimeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// // 透明状态栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// }
		setTitleText(R.string.appointment_detail);
		appointmentVO = (MyAppointmentVO) getIntent().getSerializableExtra(
				"appointmentDetail");
		if (appointmentVO != null) {
			if (appointmentVO.getReservationstate().equals(
					Config.AppointmentResult.finish.getValue())) {
				addView(R.layout.activity_appointment_detail_finished);
				initFinishView();
				setFinishData(appointmentVO);

			} else {
				addView(R.layout.activity_appointment_detail);
				initView();
				setData(appointmentVO);

			}
			setListener();
		}
		obtainAppointmentDetail();
	}

	private void initFinishView() {
		classNameTv = (TextView) findViewById(R.id.appointment_detail_class_name_tv);
		timeTv = (TextView) findViewById(R.id.appointment_detail_time_tv);
		signInTimeTv = (TextView) findViewById(R.id.appointment_detail_signin_time_tv);
		stopCarTv = (TextView) findViewById(R.id.appointment_detail_stopcar_tv);
		coachNameTv = (TextView) findViewById(R.id.appointment_detail_coach_name_tv);
		trainingGroundTv = (TextView) findViewById(R.id.appointment_detail_draining_ground_tv);
		ratingBar = (RatingBar) findViewById(R.id.appointment_detail_ratingBar);
		evaluateContentTv = (TextView) findViewById(R.id.appointment_detail_evaluate_content_tv);
		evaluateTimeTv = (TextView) findViewById(R.id.appointment_detail_evaluate_time_tv);
		commentLl = (LinearLayout) findViewById(R.id.appointment_detail_comment_ll);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		appointState = (TextView) findViewById(R.id.appointment_detail_appoint_state);
		qrcodeIv = (ImageView) findViewById(R.id.appointment_detail_qrcode_iv);
		appointInfoTv = (TextView) findViewById(R.id.appointment_detail_appoint_info);
		signKnowTv = (TextView) findViewById(R.id.appointment_detail_sign_know_tv);
		SpannableStringBuilder builder = new SpannableStringBuilder(signKnowTv
				.getText().toString());
		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		builder.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		stopCarLine = findViewById(R.id.appointment_detail_stopcar_line);
		signInTimeLine = findViewById(R.id.appointment_detail_signin_line);
		signInTimeRl = (RelativeLayout) findViewById(R.id.appointment_detail_signin_rl);
		stopCarRl = (RelativeLayout) findViewById(R.id.appointment_detail_stopcar_rl);
		signInTimeTv = (TextView) findViewById(R.id.appointment_detail_signin_time_tv);
		stopCarTv = (TextView) findViewById(R.id.appointment_detail_stopcar_tv);
		classNameTv = (TextView) findViewById(R.id.appointment_detail_class_name_tv);
		timeTv = (TextView) findViewById(R.id.appointment_detail_time_tv);
		coachNameTv = (TextView) findViewById(R.id.appointment_detail_coach_name_tv);
		trainingGroundTv = (TextView) findViewById(R.id.appointment_detail_draining_ground_tv);
		coachDuihuaIv = (ImageView) findViewById(R.id.appointment_detail_coach_duihua_iv);
		signKnowTv.setText(builder);
		cancelBtn = (Button) findViewById(R.id.appointment_detail_cancel_but);
		cancelBtnHintTv = (TextView) findViewById(R.id.appointment_detail_btn_hint_tv);
		cancelBtnLl = (LinearLayout) findViewById(R.id.appointment_detail_but_ll);

		coachRefuseLl = (LinearLayout) findViewById(R.id.appointment_detail_coach_refuse_ll);
		coachRefuseLl.setVisibility(View.GONE);
		coachRefuseReasonTv = (TextView) findViewById(R.id.appointment_detail_coach_refuse_reason_tv);
		coachRefuseTimeTv = (TextView) findViewById(R.id.appointment_detail_coach_refuse_time_tv);
	}

	private void setData(MyAppointmentVO appointmentVO) {

		if (appointmentVO == null) {
			return;
		}

		//
		if (appointmentVO.getReservationstate().equals(
				Config.AppointmentResult.applying.getValue())) {
			appointState.setText("预约请求中");
			qrcodeIv.setImageResource(R.drawable.appointment_detail_applying);
			appointInfoTv.setText("教练还没有接受预约，请耐心等一下哦");
			appointInfoTv.setTextSize(14);
			signKnowTv.setVisibility(View.GONE);
			// 开课24小时内不能取消
			long beginTime = UTC2LOC.instance.getDates(
					appointmentVO.getBegintime(), "yyyy-MM-dd HH:mm:ss")
					.getTime()
					- new Date().getTime();
			if (beginTime / 1000 / 60 / 60 < 24) {
				cancelBtn.setEnabled(false);
				cancelBtn
						.setBackgroundResource(R.drawable.button_rounded_corners_gray);
			} else {
				cancelBtn.setEnabled(true);
				cancelBtn
						.setBackgroundResource(R.drawable.button_rounded_corners);
			}

		} else if (appointmentVO.getReservationstate().equals(
				Config.AppointmentResult.applyconfirm.getValue())) {

			// 开课24小时内不能取消
			long beginTime = UTC2LOC.instance.getDates(
					appointmentVO.getBegintime(), "yyyy-MM-dd HH:mm:ss")
					.getTime()
					- new Date().getTime();
			if (beginTime / 1000 / 60 / 60 < 24) {
				cancelBtn.setEnabled(false);
				cancelBtn
						.setBackgroundResource(R.drawable.button_rounded_corners_gray);
			} else {
				cancelBtn.setEnabled(true);
				cancelBtn
						.setBackgroundResource(R.drawable.button_rounded_corners);
			}
			if (checkIsSignIn(appointmentVO)) {
				// 可签到
				appointState.setText("预约已接受");
				appointInfoTv.setText("（给教练扫一扫二维码即可签到）");
				signKnowTv.setVisibility(View.VISIBLE);
				createQrcode();
			} else {
				// 不可签到
				appointState.setText("预约已接受");
				appointInfoTv.setText("没有到签到时间");
				appointInfoTv.setTextSize(14);
				signKnowTv.setVisibility(View.VISIBLE);
				qrcodeIv.setImageResource(R.drawable.appointment_detail_applyconfirm);
			}
		} else if (appointmentVO.getReservationstate().equals(
				Config.AppointmentResult.signfinish.getValue())) {

			appointState.setText("预约已签到");
			appointInfoTv.setText("该预约已签到");
			appointInfoTv.setTextSize(14);
			signKnowTv.setVisibility(View.VISIBLE);

			// 签到信息
			signInTimeLine.setVisibility(View.VISIBLE);
			signInTimeRl.setVisibility(View.VISIBLE);
			stopCarLine.setVisibility(View.GONE);
			stopCarRl.setVisibility(View.GONE);
			signInTimeTv.setText(UTC2LOC.instance.getDate(
					appointmentVO.getSigintime(), "HH:mm"));
			// stopCarTv.setText(appointmentVO.getLearningcontent());
			qrcodeIv.setImageResource(R.drawable.appointment_detail_signfinish);
			cancelBtnLl.setVisibility(View.GONE);
		} else if (appointmentVO.getReservationstate().equals(
				Config.AppointmentResult.systemcancel.getValue())) {
			appointState.setText("预约被取消");
			qrcodeIv.setImageResource(R.drawable.appointment_detail_systemcancel);
			appointInfoTv.setText("抱歉，由于驾校或其他原因，该预约已被系统取消您可重新预约");
			appointInfoTv.setTextSize(14);
			signKnowTv.setVisibility(View.GONE);
			cancelBtnLl.setVisibility(View.GONE);

		} else if (appointmentVO.getReservationstate().equals(
				Config.AppointmentResult.missclass.getValue())) {
			appointState.setText("该预约漏课");
			qrcodeIv.setImageResource(R.drawable.appointment_detail_systemcancel);
			appointInfoTv.setText("请及时联系客服进行补课事宜");
			appointInfoTv.setTextSize(14);
			signKnowTv.setVisibility(View.GONE);
			cancelBtnHintTv.setVisibility(View.GONE);
			cancelBtn.setText("联系客服");
			LinearLayout.LayoutParams params = (LayoutParams) cancelBtn
					.getLayoutParams();
			params.topMargin = CommonUtil.px2dip(this, 400);
			cancelBtn.setLayoutParams(params);

		} else if (appointmentVO.getReservationstate().equals(
				Config.AppointmentResult.applyrefuse.getValue())) {
			appointState.setText("预约被拒绝");
			qrcodeIv.setImageResource(R.drawable.appointment_detail_applyrefuse);
			appointInfoTv.setVisibility(View.GONE);
			signKnowTv.setVisibility(View.GONE);
			cancelBtnLl.setVisibility(View.GONE);
			coachRefuseLl.setVisibility(View.VISIBLE);
			coachRefuseReasonTv.setText(appointmentVO.getCancelreason()
					.getCancelcontent());
			coachRefuseTimeTv.setText(UTC2LOC.instance.getDate(
					appointmentVO.getSigintime(), "MM/dd HH:mm"));
		} else {
			appointState.setText("预约已接受");
			qrcodeIv.setImageResource(R.drawable.appointment_detail_applyconfirm);
			cancelBtnLl.setVisibility(View.GONE);
			appointInfoTv.setVisibility(View.GONE);
			signKnowTv.setVisibility(View.GONE);
		}

		coachNameTv.setText(appointmentVO.getCoachid().getName() + "  教练");

		//
		if (!TextUtils.isEmpty(appointmentVO.getCourseprocessdesc())) {
			classNameTv.setText(appointmentVO.getCourseprocessdesc());
		}
		if (!TextUtils.isEmpty(appointmentVO.getClassdatetimedesc())) {
			timeTv.setText(appointmentVO.getClassdatetimedesc());
		}
		if (!TextUtils.isEmpty(appointmentVO.getTrainfieldlinfo().getName())) {
			trainingGroundTv.setText(appointmentVO.getTrainfieldlinfo()
					.getName());
		}

		String state = appointmentVO.getReservationstate();
		// 底部预约显示
		// if (!TextUtils.isEmpty(state)) {
		//
		// if (state.equals(AppointmentResult.applyconfirm.getValue())
		// || state.equals(AppointmentResult.applying.getValue())) {
		// // 预约中
		// cancelBtn.setEnabled(true);
		// cancelBtn
		// .setBackgroundResource(R.drawable.button_rounded_corners);
		// } else {
		// // 已完成
		// cancelBtn.setEnabled(false);
		// cancelBtn
		// .setBackgroundResource(R.drawable.button_rounded_corners_gray);
		// }
		// }
	}

	private boolean checkIsSignIn(MyAppointmentVO myAppointmentVO) {
		String beginTime = UTC2LOC.instance.getDate(
				myAppointmentVO.getBegintime(), "hh:mm");
		String endTime = UTC2LOC.instance.getDate(myAppointmentVO.getEndtime(),
				"hh:mm");

		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		try {
			long diffBeginTime = UTC2LOC.instance.getDates(
					myAppointmentVO.getBegintime(), "yyyy-MM-dd HH:mm:ss")
					.getTime()
					- new Date().getTime();
			long diffEndTime = UTC2LOC.instance.getDates(
					myAppointmentVO.getEndtime(), "yyyy-MM-dd HH:mm:ss")
					.getTime()
					- new Date().getTime();
			LogUtil.print("diffEndTime--" + diffEndTime);
			if (diffBeginTime / 1000 / 60 > 15) {
				// ZProgressHUD.getInstance(this).dismissWithSuccess(
				// "请在开课前15分钟内签到");
				// ZProgressHUD.getInstance(this).show();
				return false;
			} else if (diffEndTime / 1000 / 60 < 0) {
				// ZProgressHUD.getInstance(this).dismissWithSuccess(
				// "您的课程已结束，不能再签到");
				// ZProgressHUD.getInstance(this).show();
				return false;
			} else {
				return true;
				// Intent intent = new Intent(this, QRCodeCreateActivity.class);
				// intent.putExtra("myappointment", myAppointmentVO);
				// startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private void createQrcode() {
		QRCodeCreateVO codeCreateVO = new QRCodeCreateVO();
		if (appointmentVO != null) {

			if (app != null) {

				codeCreateVO.setStudentId(app.userVO.getUserid());
				codeCreateVO.setStudentName(app.userVO.getName());
				codeCreateVO.setReservationId(appointmentVO.get_id());
				codeCreateVO.setCoachName(app.userVO.getApplycoachinfo()
						.getName());
				codeCreateVO.setCourseProcessDesc(app.userVO.getSubject()
						.getName());
				codeCreateVO.setCreateTime((new Date().getTime() / 1000) + "");
				codeCreateVO.setLatitude(app.latitude);
				codeCreateVO.setLocationAddress(app.userVO.getAddress());
				codeCreateVO.setLongitude(app.longtitude);
			}
		}
		// 生成二维码
		try {
			String contentString = JSONUtil.toJsonString(codeCreateVO);
			LogUtil.print("contentString---" + contentString);
			if (contentString != null && contentString.trim().length() > 0) {
				// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
						contentString, 500);
				qrcodeIv.setImageBitmap(qrCodeBitmap);
			} else {
				toast.setText("生成二维码失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setFinishData(MyAppointmentVO appointmentVO) {

		if (appointmentVO == null) {
			return;
		}

		coachNameTv.setText(appointmentVO.getCoachid().getName() + "  教练");

		//
		if (!TextUtils.isEmpty(appointmentVO.getCourseprocessdesc())) {
			classNameTv.setText(appointmentVO.getCourseprocessdesc());
		}
		if (!TextUtils.isEmpty(appointmentVO.getClassdatetimedesc())) {
			timeTv.setText(appointmentVO.getClassdatetimedesc());
		}
		if (!TextUtils.isEmpty(appointmentVO.getTrainfieldlinfo().getName())) {
			trainingGroundTv.setText(appointmentVO.getTrainfieldlinfo()
					.getName());
		}
		if (!TextUtils.isEmpty(appointmentVO.getSigintime())) {
			signInTimeTv.setText(appointmentVO.getSigintime());
		}
		if (!TextUtils.isEmpty(appointmentVO.getLearningcontent())) {
			signInTimeTv.setText(appointmentVO.getLearningcontent());
		}

		if (appointmentVO.getComment() != null) {
			evaluateContentTv.setText(appointmentVO.getComment()
					.getCommentcontent());
			evaluateTimeTv.setText(UTC2LOC.instance.getDate(appointmentVO
					.getComment().getCommenttime(), "MM/dd HH:mm"));
			ratingBar.setNumStars(Integer.parseInt(appointmentVO.getComment()
					.getStarlevel()));
		} else {
			commentLl.setVisibility(View.GONE);
		}

		// String state = appointmentVO.getReservationstate();
	}

	private void obtainAppointmentDetail() {
		if (!TextUtils.isEmpty(appointmentVO.get_id()) && app.userVO != null) {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpGetSend(appointmentDetail, this,
					Config.IP + "api/v1/courseinfo/userreservationinfo/"
							+ appointmentVO.get_id(), null, 10000, headerMap);
		}
	}

	private void setListener() {
		if (cancelBtn != null)
			cancelBtn.setOnClickListener(this);
		if (coachDuihuaIv != null)
			coachDuihuaIv.setOnClickListener(this);
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
		case R.id.appointment_detail_coach_duihua_iv:
			String chatId = appointmentVO.getCoachid().getCoachid();
			if (!TextUtils.isEmpty(chatId)) {
				intent = new Intent(this, ChatActivity.class);
				intent.putExtra("chatId", chatId);
				intent.putExtra("chatName", appointmentVO.getCoachid()
						.getName());
				intent.putExtra("chatUrl", appointmentVO.getCoachid()
						.getHeadportrait().getOriginalpic());
				intent.putExtra("userTypeNoAnswer", UserType.COACH.getValue());
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("无法获取对方信息");
			}
			break;
		case R.id.appointment_detail_cancel_but:
			if (appointmentVO.getReservationstate().equals(
					Config.AppointmentResult.missclass.getValue())) {
				// 打电话
				LogUtil.print("电话---" + appointmentVO.getSchoolmobile());
				Intent intent1 = new Intent(Intent.ACTION_DIAL,
						Uri.parse("tel:" + appointmentVO.getSchoolmobile()));
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent1);
			} else {
				// 取消预约
				intent = new Intent(this, CancelAppointmentActivity.class);
				intent.putExtra("appointment", appointmentVO);
			}

			break;

		}
		if (intent != null) {
			startActivityForResult(intent, 0);
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(appointmentDetail)) {
				if (data != null) {
					MyAppointmentVO appointmentVO = JSONUtil.toJavaBean(
							MyAppointmentVO.class, data);
					setData(appointmentVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			// if(requestCode == R.id.appointment_detail_cancel_but){
			//
			// }
			obtainAppointmentDetail();
		}

	}
}
