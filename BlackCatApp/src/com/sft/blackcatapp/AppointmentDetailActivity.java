package com.sft.blackcatapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.sft.adapter.AppointmentDetailStudentHoriListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.AppointmentResult;
import com.sft.common.Config.UserType;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.UTC2LOC;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.commentvo.CommentUser;
import com.squareup.picasso.Picasso;

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
	// 头像
	private SelectableRoundedImageView headPicIm;
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
	private Button cancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_appointment_detail);
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
		//
		schoolPicIm = (ImageView) findViewById(R.id.appointment_detail_school_pic_iv);
		returnBtn = (ImageButton) findViewById(R.id.appointment_detail_left_btn);
		calenderBtn = (ImageButton) findViewById(R.id.appointment_detail_right_first_btn);
		chatBtn = (ImageButton) findViewById(R.id.appointment_detail_right_second_btn);
		headPicIm = (SelectableRoundedImageView) findViewById(R.id.appointment_detail_headpic_im);
		coachNameTv = (TextView) findViewById(R.id.appointment_detail_coachname_tv);
		coachLevelRb = (RatingBar) findViewById(R.id.appointment_detail_coach_level_rb);
		headPicIm.setScaleType(ScaleType.CENTER_CROP);
		headPicIm.setImageResource(R.drawable.default_small_pic);
		headPicIm.setOval(true);
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
		cancelBtn = (Button) findViewById(R.id.appointment_detail_cancel_but);
	}

	private void setData(MyAppointmentVO appointmentVO) {

		if (appointmentVO == null) {
			return;
		}
		String schoolPicUrl = appointmentVO.getCoachid().getSchoolimage();
		if (TextUtils.isEmpty(schoolPicUrl)) {
			schoolPicIm.setBackgroundResource(R.drawable.applydefault);
		} else {
			// 图片高斯模糊
			try {
				Bitmap bitmap = Picasso.with(getApplicationContext())
						.load(schoolPicUrl).get();
				schoolPicIm.setBackgroundDrawable(CommonUtil
						.BoxBlurFilter(bitmap));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		LinearLayout.LayoutParams headpicParams = (LinearLayout.LayoutParams) headPicIm
				.getLayoutParams();

		String url = appointmentVO.getCoachid().getHeadportrait()
				.getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headPicIm.setImageResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPicIm,
					headpicParams.width, headpicParams.height);

		}
		coachNameTv.setText(appointmentVO.getCoachid().getName());
		if (!TextUtils.isEmpty(appointmentVO.getCoachid().getStarlevel())) {
			coachLevelRb.setRating(Integer.parseInt(appointmentVO.getCoachid()
					.getStarlevel()));
		}

		//
		classNameTv.setText(appointmentVO.getCourseprocessdesc());
		timeTv.setText(appointmentVO.getClassdatetimedesc());
		classDetailTv.setText(appointmentVO.getLearningcontent());
		schoolNameTv.setText(appointmentVO.getCoachid().getDriveschoolinfo()
				.getName());
		if (!TextUtils.isEmpty(appointmentVO.getTrainfieldlinfo().getName())) {
			trainingGroundsTv.setText(appointmentVO.getTrainfieldlinfo()
					.getName());
		}
		shuttleAddressTv.setText(appointmentVO.getShuttleaddress());

		String state = appointmentVO.getReservationstate();
		// 底部预约显示
		if (!TextUtils.isEmpty(state)) {

			if (state.equals(AppointmentResult.applyconfirm.getValue())
					|| state.equals(AppointmentResult.applying.getValue())) {
				// 预约中
				cancelAppointRl.setVisibility(View.VISIBLE);
				coachCancelLl.setVisibility(View.GONE);
				String belowTime = UTC2LOC.instance.getDate(
						appointmentVO.getBegintime(), "MM月dd日")
						+ " "
						+ UTC2LOC.instance.getDate(
								appointmentVO.getBegintime(), "HH:mm")
						+ "-"
						+ UTC2LOC.instance.getDate(appointmentVO.getEndtime(),
								"HH:mm")
						+ " "
						+ appointmentVO.getSubject().getName();

				belowTimeTv.setText(belowTime);
			} else if (state.equals(AppointmentResult.applyrefuse.getValue())) {
				// 教练取消
				cancelAppointRl.setVisibility(View.GONE);
				coachCancelLl.setVisibility(View.VISIBLE);

			} else {
				// 已完成
				cancelAppointRl.setVisibility(View.GONE);
				coachCancelLl.setVisibility(View.GONE);

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
		returnBtn.setOnClickListener(this);
		calenderBtn.setOnClickListener(this);
		chatBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
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
