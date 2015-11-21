package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.sft.adapter.AppointmentDetailStudentHoriListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.AppointmentResult;
import com.sft.common.Config.UserType;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.commentvo.CommentUser;

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
import android.widget.TextView;
import android.widget.ZoomControls;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;
import cn.sft.pull.LoadMoreView;
import cn.sft.pull.LoadMoreView.LoadMoreListener;
import cn.sft.pull.OnItemClickListener;

/**
 * 预约详情
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class AppointmentDetailActivity extends BaseActivity
		implements OnClickListener, OnMapDoubleClickListener, OnItemClickListener, LoadMoreListener {

	private static final String sameTimeStudent = "sameTimeStudent";
	//
	private BaiduMap mBaiduMap;
	// 标题
	private TextView titleTv;
	//
	private ImageButton returnBtn, calenderBtn, chatBtn;
	// 头像
	private ImageView headPicIm;
	// 姓名，学校
	private TextView coachNameTv, schoolTv;
	//
	private TextView studyProcessTv, trainPlaceTv;
	//
	private LoadMoreView studentListView;
	// 投诉评论布局
	private LinearLayout commentLayout;
	// 投诉，评论按钮
	private Button complainBtn, commentBtn;
	// 确定学完按钮
	private Button confirmStudyBtn;
	// 取消预约布局
	private LinearLayout cancelLayout;
	// 取消按钮
	private Button cancelBtn;
	// 时间
	private TextView timeTv;
	// 接送地址
	private TextView shuttleTv;
	// 百度地图
	private MapView mapView;
	//
	private MyAppointmentVO appointmentVO;
	//
	private int studentPage = 1;
	//
	private List<CommentUser> userList = new ArrayList<CommentUser>();
	//
	private AppointmentDetailStudentHoriListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_detail);
		initView();
		initData();
		setListener();
		initMap();
		obtainSameTimeStudent(studentPage);
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		titleTv = (TextView) findViewById(R.id.appointment_detail_title_tv);
		returnBtn = (ImageButton) findViewById(R.id.appointment_detail_left_btn);
		calenderBtn = (ImageButton) findViewById(R.id.appointment_detail_right_first_btn);
		chatBtn = (ImageButton) findViewById(R.id.appointment_detail_right_second_btn);

		headPicIm = (ImageView) findViewById(R.id.appointment_detail_headpic_im);
		coachNameTv = (TextView) findViewById(R.id.appointment_detail_coachname_tv);
		schoolTv = (TextView) findViewById(R.id.appointment_detail_coachschool_tv);

		studyProcessTv = (TextView) findViewById(R.id.appointment_detail_process_tv);
		trainPlaceTv = (TextView) findViewById(R.id.appointment_detail_trainplace_tv);
		timeTv = (TextView) findViewById(R.id.appointment_detail_time_tv);
		shuttleTv = (TextView) findViewById(R.id.appointment_detail_shuttleplace_tv);

		commentLayout = (LinearLayout) findViewById(R.id.appointment_detail_comment_layout);
		cancelLayout = (LinearLayout) findViewById(R.id.appointment_detail_cancel_layout);
		confirmStudyBtn = (Button) findViewById(R.id.appointment_detail_confirm_btn);
		complainBtn = (Button) findViewById(R.id.appointment_detail_complain_btn);
		commentBtn = (Button) findViewById(R.id.appointment_detail_comment_btn);
		cancelBtn = (Button) findViewById(R.id.appointment_detail_cancel_btn);

		mapView = (MapView) findViewById(R.id.appointment_detail_bmapView);
		studentListView = (LoadMoreView) findViewById(R.id.appointment_detail_horizon_listview);
		studentListView.setPullLoadMoreEnable(true);
		studentListView.setHorizontal();

		titleTv.setText(R.string.appointment_detail);
	}

	private void initData() {
		appointmentVO = (MyAppointmentVO) getIntent().getSerializableExtra("appointment");

		LinearLayout.LayoutParams headpicParams = (LinearLayout.LayoutParams) headPicIm.getLayoutParams();

		String url = appointmentVO.getCoachid().getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headPicIm.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPicIm, headpicParams.width, headpicParams.height);
		}
		coachNameTv.setText(appointmentVO.getCoachid().getName());
		schoolTv.setText(appointmentVO.getCoachid().getDriveschoolinfo().getName());

		timeTv.setText(appointmentVO.getClassdatetimedesc());
		shuttleTv.setText("接送地点:" + appointmentVO.getShuttleaddress());
		studyProcessTv.setText(appointmentVO.getCourseprocessdesc());

		String trainPlace = appointmentVO.getTrainfieldlinfo().getName();
		if (TextUtils.isEmpty(trainPlace)) {
			trainPlace = appointmentVO.getCoachid().getDriveschoolinfo().getName();
			if (TextUtils.isEmpty(trainPlace)) {
				trainPlace = "暂无";
			}
		}
		trainPlaceTv.setText("训练场地: " + trainPlace);

		String state = appointmentVO.getReservationstate();
		if (state.equals(AppointmentResult.applying.getValue())
				|| state.equals(AppointmentResult.applyconfirm.getValue())) {
			cancelLayout.setVisibility(View.VISIBLE);
		} else if (state.equals(AppointmentResult.unconfirmfinish.getValue())) {
			confirmStudyBtn.setVisibility(View.VISIBLE);
		} else if (state.equals(AppointmentResult.applyrefuse.getValue())
				|| state.equals(AppointmentResult.ucomments.getValue())) {
			commentLayout.setVisibility(View.VISIBLE);
		}
	}

	private void initMap() {
		// 获取地图控件引用
		mapView = (MapView) findViewById(R.id.appointment_detail_bmapView);
		mBaiduMap = mapView.getMap();
		mBaiduMap.setOnMapDoubleClickListener(this);
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// 地图类型：普通地图
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().zoom(18f).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		int count = mapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}

		try {
			double latitude = Double.parseDouble(appointmentVO.getCoachid().getLatitude());
			double longtitude = Double.parseDouble(appointmentVO.getCoachid().getLongitude());
			LatLng point = new LatLng(latitude, longtitude);
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option = new MarkerOptions().position(point).anchor(0.5f, 0).icon(bitmap).perspective(true);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(option);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
			mBaiduMap.animateMapStatus(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void obtainSameTimeStudent(int page) {
		HttpSendUtils.httpGetSend(sameTimeStudent, this, Config.IP + "api/v1/courseinfo/sametimestudents/reservationid/"
				+ appointmentVO.get_id() + "/index/" + page);
	}

	private void setListener() {
		returnBtn.setOnClickListener(this);
		calenderBtn.setOnClickListener(this);
		chatBtn.setOnClickListener(this);
		studentListView.setLoadMoreListener(this);
		complainBtn.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		confirmStudyBtn.setOnClickListener(this);
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
				intent.putExtra("chatName", appointmentVO.getCoachid().getName());
				intent.putExtra("chatUrl", appointmentVO.getCoachid().getHeadportrait().getOriginalpic());
				intent.putExtra("userTypeNoAnswer", UserType.COACH.getValue());
				startActivity(intent);
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("无法获取对方信息");
			}
			break;
		case R.id.appointment_detail_complain_btn:
			intent = new Intent(this, ComplainActivity.class);
			intent.putExtra("appointmentVO", appointmentVO);
			intent.putExtra("position", getIntent().getIntExtra("position", 0));
			startActivityForResult(intent, v.getId());
			break;
		case R.id.appointment_detail_comment_btn:
			intent = new Intent(this, CommentActivity.class);
			intent.putExtra("appointmentVO", appointmentVO);
			intent.putExtra("position", getIntent().getIntExtra("position", 0));
			startActivityForResult(intent, v.getId());
			break;
		case R.id.appointment_detail_cancel_btn:
			intent = new Intent(this, CancelAppointmentActivity.class);
			intent.putExtra("appointmentVO", appointmentVO);
			intent.putExtra("position", getIntent().getIntExtra("position", 0));
			startActivityForResult(intent, v.getId());
			break;
		case R.id.appointment_detail_confirm_btn:
			intent = new Intent(this, ConfirmStudyActivity.class);
			intent.putExtra("appointmentVO", appointmentVO);
			intent.putExtra("position", getIntent().getIntExtra("position", 0));
			startActivityForResult(intent, v.getId());
			break;
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (sameTimeStudent.equals(type)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0)
						studentPage++;
					for (int i = 0; i < length; i++) {
						CommentUser commentUser = (CommentUser) JSONUtil.toJavaBean(CommentUser.class,
								dataArray.getJSONObject(i).getJSONObject("userid"));
						if (!commentUser.get_id().equals(app.userVO.getUserid()))
							userList.add(commentUser);
					}
				}
				if (adapter == null) {
					adapter = new AppointmentDetailStudentHoriListAdapter(this, userList);
				} else {
					adapter.setData(userList);
				}
				studentListView.setAdapter(adapter);
				studentListView.setLoadMoreCompleted();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (sameTimeStudent.equals(type))
			studentListView.setLoadMoreCompleted();
		super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (sameTimeStudent.equals(type))
			studentListView.setLoadMoreCompleted();
		super.doTimeOut(type);
	}

	@Override
	public void onMapDoubleClick(LatLng arg0) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		Intent intent = new Intent(MyAppointmentActivity.class.getName());
		intent.putExtra("refreshState", true);
		intent.putExtra("position", getIntent().getIntExtra("position", 0));

		switch (requestCode) {
		case R.id.appointment_detail_cancel_btn:
			intent.putExtra("state", AppointmentResult.applycancel.getValue());
			sendBroadcast(intent);
			delayFinish();
			break;
		case R.id.appointment_detail_comment_btn:
			if (resultCode == RESULT_OK) {
				intent.putExtra("state", AppointmentResult.finish.getValue());
				sendBroadcast(intent);
				delayFinish();
			}
			break;
		case R.id.appointment_detail_confirm_btn:
			if (resultCode == RESULT_OK) {
				intent.putExtra("state", AppointmentResult.finish.getValue());
				sendBroadcast(intent);
			}
			delayFinish();
			break;
		case R.id.appointment_detail_complain_btn:
			delayFinish();
			break;
		}
	}

	private void delayFinish() {
		new MyHandler(200) {
			@Override
			public void run() {
				finish();
			}
		};
	}

	@Override
	public void onItemClick(int position) {
		Intent intent = new Intent(this, StudentInfoActivity.class);
		intent.putExtra("studentId", adapter.getItem(position).get_id());
		startActivity(intent);
	}

	@Override
	public void onLoadMore() {
		obtainSameTimeStudent(studentPage);
	}
}
