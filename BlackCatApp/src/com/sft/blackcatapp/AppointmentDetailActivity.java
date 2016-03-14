package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.AppointmentDetailStudentHoriListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.AppointmentResult;
import com.sft.common.Config.UserType;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;
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
		addView(R.layout.activity_appointment_detail);
		setTitleText(R.string.appointment_detail);
		initView();
		appointmentVO = (MyAppointmentVO) getIntent().getSerializableExtra(
				"appointmentDetail");

		setData(appointmentVO);
		setListener();
		obtainAppointmentDetail();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		qrcodeIv = (ImageView) findViewById(R.id.appointment_detail_qrcode_iv);
		signKnowTv = (TextView) findViewById(R.id.appointment_detail_sign_know_tv);
		SpannableStringBuilder builder = new SpannableStringBuilder(signKnowTv
				.getText().toString());
		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		builder.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		classNameTv = (TextView) findViewById(R.id.appointment_detail_class_name_tv);
		timeTv = (TextView) findViewById(R.id.appointment_detail_time_tv);
		coachNameTv = (TextView) findViewById(R.id.appointment_detail_coach_name_tv);
		trainingGroundTv = (TextView) findViewById(R.id.appointment_detail_draining_ground_tv);
		coachDuihuaIv = (ImageView) findViewById(R.id.appointment_detail_coach_duihua_iv);
		signKnowTv.setText(builder);
		cancelBtn = (Button) findViewById(R.id.appointment_detail_cancel_but);
	}

	private void setData(MyAppointmentVO appointmentVO) {

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

		String state = appointmentVO.getReservationstate();
		// 底部预约显示
		if (!TextUtils.isEmpty(state)) {

			if (state.equals(AppointmentResult.applyconfirm.getValue())
					|| state.equals(AppointmentResult.applying.getValue())) {
				// 预约中
				cancelBtn.setEnabled(true);
				cancelBtn
						.setBackgroundResource(R.drawable.button_rounded_corners);
			} else {
				// 已完成
				cancelBtn.setEnabled(false);
				cancelBtn
						.setBackgroundResource(R.drawable.button_rounded_corners_gray);
			}
		}
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
		cancelBtn.setOnClickListener(this);
		coachDuihuaIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
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
			// 取消预约
			intent = new Intent(this, CancelAppointmentActivity.class);
			intent.putExtra("appointment", appointmentVO);
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
