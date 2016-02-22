package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.pull.OnItemClickListener;

import com.sft.adapter.AppointmentDetailStudentHoriListAdapter;
import com.sft.common.Config.UserType;
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
		OnClickListener, OnItemClickListener {

	// 头像
	private ImageView headPicIm;
	//
	private MyAppointmentVO appointmentVO;
	//
	private int studentPage = 1;
	//
	private List<CommentUser> userList = new ArrayList<CommentUser>();
	//
	private AppointmentDetailStudentHoriListAdapter adapter;

	private ImageButton returnBtn;
	private ImageButton calenderBtn;
	private ImageButton chatBtn;
	private TextView coachNameTv;
	private ImageView schoolPicIm;
	private RatingBar coachLevelRb;

	private TextView classNameTv;
	private TextView classDetailTv;
	private TextView timeTv;
	private TextView schoolNameTv;
	private TextView trainingGroundsTv;
	private TextView shuttleAddressTv;

	private LinearLayout coachCancelLl;
	private ImageView coachPicIm;
	private TextView coachCancelTitleTv;
	private TextView coachCancelReasonTv;

	private RelativeLayout cancelAppointRl;
	private TextView belowTimeTv;
	private Button cancelBut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_detail);
		initView();
		initData();
		// setListener();
		// initMap();
		// obtainSameTimeStudent(studentPage);
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	@Override
	public void onItemClick(int position) {

	}

	private void initView() {
		//
		schoolPicIm = (ImageView) findViewById(R.id.appointment_detail_school_pic_iv);
		returnBtn = (ImageButton) findViewById(R.id.appointment_detail_left_btn);
		calenderBtn = (ImageButton) findViewById(R.id.appointment_detail_right_first_btn);
		chatBtn = (ImageButton) findViewById(R.id.appointment_detail_right_second_btn);
		headPicIm = (ImageView) findViewById(R.id.appointment_detail_headpic_im);
		coachNameTv = (TextView) findViewById(R.id.appointment_detail_coachname_tv);
		coachLevelRb = (RatingBar) findViewById(R.id.appointment_detail_coach_level_rb);

		//
		classNameTv = (TextView) findViewById(R.id.appointment_detail_class_name_tv);
		classDetailTv = (TextView) findViewById(R.id.appointment_detail_desc_tv);
		timeTv = (TextView) findViewById(R.id.appointment_detail_time_tv);
		schoolNameTv = (TextView) findViewById(R.id.appointment_detail_school_name_tv);
		trainingGroundsTv = (TextView) findViewById(R.id.appointment_detail_training_grounds_tv);
		shuttleAddressTv = (TextView) findViewById(R.id.appointment_detail_shuttle_address_tv);

		//
		coachCancelLl = (LinearLayout) findViewById(R.id.appointment_detail_coach_cancel_ll);
		coachPicIm = (ImageView) findViewById(R.id.appointment_detail_coach_pic_iv);
		coachCancelTitleTv = (TextView) findViewById(R.id.appointment_detail_coach_cancel_title_tv);
		coachCancelReasonTv = (TextView) findViewById(R.id.appointment_detail_coach_cancel_reason_tv);

		//
		cancelAppointRl = (RelativeLayout) findViewById(R.id.appointment_detail_cancel_appoint_rl);
		belowTimeTv = (TextView) findViewById(R.id.appointment_detail_below_time_tv);
		cancelBut = (Button) findViewById(R.id.appointment_detail_cancel_but);
	}

	private void initData() {
		appointmentVO = (MyAppointmentVO) getIntent().getSerializableExtra(
				"appointment");

		LinearLayout.LayoutParams headpicParams = (LinearLayout.LayoutParams) headPicIm
				.getLayoutParams();

		String url = appointmentVO.getCoachid().getHeadportrait()
				.getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headPicIm.setBackgroundResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPicIm,
					headpicParams.width, headpicParams.height);
		}
		coachNameTv.setText(appointmentVO.getCoachid().getName());
		// schoolTv.setText(appointmentVO.getCoachid().getDriveschoolinfo()
		// .getName());
		//
		// timeTv.setText(appointmentVO.getClassdatetimedesc());
		// shuttleTv.setText("接送地点:" + appointmentVO.getShuttleaddress());
		// studyProcessTv.setText(appointmentVO.getCourseprocessdesc());
		//
		// String trainPlace =
		// appointmentVO.getTrainfieldlinfo().getFieldname();
		// if (TextUtils.isEmpty(trainPlace)) {
		// trainPlace = appointmentVO.getCoachid().getDriveschoolinfo()
		// .getName();
		// if (TextUtils.isEmpty(trainPlace)) {
		// trainPlace = "暂无";
		// }
		// }
		// trainPlaceTv.setText("训练场地: " + trainPlace);
		//
		// String state = appointmentVO.getReservationstate();
		// if (state.equals(AppointmentResult.applying.getValue())
		// || state.equals(AppointmentResult.applyconfirm.getValue())) {
		// cancelLayout.setVisibility(View.VISIBLE);
		// } else if
		// (state.equals(AppointmentResult.unconfirmfinish.getValue())) {
		// confirmStudyBtn.setVisibility(View.VISIBLE);
		// } else if (state.equals(AppointmentResult.applyrefuse.getValue())
		// || state.equals(AppointmentResult.ucomments.getValue())) {
		// commentLayout.setVisibility(View.VISIBLE);
		// }
	}

	private void setListener() {
		returnBtn.setOnClickListener(this);
		calenderBtn.setOnClickListener(this);
		chatBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.appointment_detail_left_btn:
			finish();
			break;
		case R.id.appointment_detail_right_first_btn:
			break;
		case R.id.appointment_detail_right_second_btn:
			String chatId = appointmentVO.getCoachid().getCoachid();
			if (!TextUtils.isEmpty(chatId)) {
				intent = new Intent(this, ChatActivity.class);
				intent.putExtra("chatId", chatId);
				intent.putExtra("chatName", appointmentVO.getCoachid()
						.getName());
				intent.putExtra("chatUrl", appointmentVO.getCoachid()
						.getHeadportrait().getOriginalpic());
				intent.putExtra("userTypeNoAnswer", UserType.COACH.getValue());
				startActivity(intent);
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("无法获取对方信息");
			}
			break;

		}
		//
		// @Override
		// public synchronized boolean doCallBack(String type, Object
		// jsonString) {
		// if (super.doCallBack(type, jsonString)) {
		// return true;
		// }
		// try {
		// if (sameTimeStudent.equals(type)) {
		// if (dataArray != null) {
		// int length = dataArray.length();
		// if (length > 0)
		// studentPage++;
		// for (int i = 0; i < length; i++) {
		// CommentUser commentUser = JSONUtil.toJavaBean(
		// CommentUser.class, dataArray.getJSONObject(i)
		// .getJSONObject("userid"));
		// if (!commentUser.get_id()
		// .equals(app.userVO.getUserid()))
		// userList.add(commentUser);
		// }
		// }
		// if (adapter == null) {
		// adapter = new AppointmentDetailStudentHoriListAdapter(this,
		// userList);
		// } else {
		// adapter.setData(userList);
		// }
		// // studentListView.setAdapter(adapter);
		// // studentListView.setLoadMoreCompleted();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return true;
		// }
		//
		// @Override
		// public void doException(String type, Exception e, int code) {
		// if (sameTimeStudent.equals(type))
		// // studentListView.setLoadMoreCompleted();
		// super.doException(type, e, code);
		// }
		//
		// @Override
		// public void doTimeOut(String type) {
		// if (sameTimeStudent.equals(type))
		// // studentListView.setLoadMoreCompleted();
		// super.doTimeOut(type);
		// }
		//
		// @Override
		// public void onMapDoubleClick(LatLng arg0) {
		//
		// }
		//
		// @Override
		// protected void onActivityResult(int requestCode, int resultCode,
		// Intent
		// data) {
		// if (data == null) {
		// return;
		// }
		// Intent intent = new Intent(MyAppointmentActivity.class.getName());
		// intent.putExtra("refreshState", true);
		// intent.putExtra("position", getIntent().getIntExtra("position", 0));
		//
		// switch (requestCode) {
		// case R.id.appointment_detail_cancel_btn:
		// intent.putExtra("state", AppointmentResult.applycancel.getValue());
		// sendBroadcast(intent);
		// delayFinish();
		// break;
		// case R.id.appointment_detail_comment_btn:
		// if (resultCode == RESULT_OK) {
		// intent.putExtra("state", AppointmentResult.finish.getValue());
		// sendBroadcast(intent);
		// delayFinish();
		// }
		// break;
		// case R.id.appointment_detail_confirm_btn:
		// if (resultCode == RESULT_OK) {
		// intent.putExtra("state", AppointmentResult.finish.getValue());
		// sendBroadcast(intent);
		// }
		// delayFinish();
		// break;
		// case R.id.appointment_detail_complain_btn:
		// delayFinish();
		// break;
		// }
		// }
		//
		// private void delayFinish() {
		// new MyHandler(200) {
		// @Override
		// public void run() {
		// finish();
		// }
		// };
		// }
		//
		// @Override
		// public void onItemClick(int position) {
		// Intent intent = new Intent(this, StudentInfoActivity.class);
		// intent.putExtra("studentId", adapter.getItem(position).get_id());
		// startActivity(intent);
		// }
		//
		// // @Override
		// // public void onLoadMore() {
		// // obtainSameTimeStudent(studentPage);
		// // }
	}
}
