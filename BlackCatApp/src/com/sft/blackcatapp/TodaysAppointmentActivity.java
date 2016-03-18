package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.adapter.TodayAppointmentAdapter;
import com.sft.common.Config;
import com.sft.dialog.NoCommentDialog;
import com.sft.dialog.NoCommentDialog.ClickListenerInterface;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;

public class TodaysAppointmentActivity extends BaseActivity implements
		OnItemClickListener, OnRefreshListener {

	private static final String NOT_COMMENT = "nocomment";
	private static final String comment = "comment";
	private static final String reservation = "reservation";
	private SwipeRefreshLayout swipeLayout;
	private ListView mListView;
	private TodayAppointmentAdapter adapter;
	private boolean isRefreshing = false;
	private List<MyAppointmentVO> mList = new ArrayList<MyAppointmentVO>();
	private RelativeLayout order_ll;
	private NoCommentDialog commentDialog;
	private MyAppointmentVO myAppointmentVO;
	private RelativeLayout error_rl;
	private TextView error_tv;
	private ImageView error_iv;
	private TextView errorTvs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_today_appointment);
		initView();
		if (app.userVO != null && !app.userVO.getApplystate().equals("0")) {
			// 获取未评论列表
			obtainNotComments();
		}
		obtainOppointment();
	}

	private void initView() {
		setTitleText(R.string.today_appointment);

		error_rl = (RelativeLayout) findViewById(R.id.error_rl);
		error_tv = (TextView) findViewById(R.id.error_tv);
		error_iv = (ImageView) findViewById(R.id.error_iv);
		errorTvs = (TextView) findViewById(R.id.error_tvs);

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.today_appointment_swipe_container);
		order_ll = (RelativeLayout) findViewById(R.id.order_ll);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mListView = (ListView) findViewById(R.id.today_appointment_listview);
		mListView.setOnItemClickListener(this);
	}

	/**
	 * 获取预约列表
	 */
	private void obtainOppointment() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("subjectid", app.userVO.getSubject().getSubjectid());
		paramMap.put("reservationstate", "3");
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(reservation, this, Config.IP
				+ "api/v1/courseinfo/getmyreservation", paramMap, 10000,
				headerMap);
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (type.equals(reservation))
			super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (type.equals(reservation))
			super.doTimeOut(type);
	}

	/**
	 * 获取未评论列表
	 */
	private void obtainNotComments() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		LogUtil.print("subject---Id==>"
				+ app.userVO.getSubject().getSubjectid());
		paramMap.put("subjectid", app.userVO.getSubject().getSubjectid());// 订单的状态
																			// //
																			// 0
																			// 订单生成
																			// 2
																			// 支付成功
																			// 3
																			// 支付失败
																			// 4
																			// 订单取消
																			// -1
																			// 全部(未支付的订单)

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(NOT_COMMENT, this, Config.IP
				+ "api/v1/courseinfo/getmyuncommentreservation", paramMap,
				10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(reservation)) {
				if (dataArray != null) {
					List<MyAppointmentVO> list = new ArrayList<MyAppointmentVO>();
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						MyAppointmentVO appointmentVO = JSONUtil.toJavaBean(
								MyAppointmentVO.class,
								dataArray.getJSONObject(i));
						if (appointmentVO != null) {
							// 只显示今天的预约
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date date = format
									.parse(UTC2LOC.instance.getDate(
											appointmentVO.getBegintime(),
											"yyyy-MM-dd"));
							LogUtil.print(UTC2LOC.instance.getDate(
									appointmentVO.getBegintime(), "yyyy-MM-dd"));
							Date today = new Date();
							if (Util.isSameDate(today, date)) {
								list.add(appointmentVO);
							}
						}
					}

					LogUtil.print(length + "预约个数：--" + list.size());
					if (list.size() <= 0) {
						order_ll.setBackgroundResource(R.drawable.order_bg);

						error_rl.setVisibility(View.VISIBLE);
						error_iv.setImageResource(R.drawable.image_yuyue);
						error_tv.setText("没有找到您的预约信息");

					} else {
						if (adapter == null) {
							mList.clear();
							mList.addAll(list);
							adapter = new TodayAppointmentAdapter(mList, this);
							mListView.setAdapter(adapter);
						}
						mList.clear();

						mList.addAll(list);
					}

				}
				if (isRefreshing) {
					swipeLayout.setRefreshing(false);
					isRefreshing = false;
				}
			} else if (type.equals(NOT_COMMENT)) {
				// 未评论 的 预约列表
				LogUtil.print("notcomment::::>>" + jsonString);
				if (dataArray != null) {
					int length = dataArray.length();
					if (length == 0) {// 不存在 未评论
						// if (null != commentDialog) {// 不是空
						// commentDialog.dismiss();
						// return true;
						// }
						// // 获取活动
						// df = new SimpleDateFormat("yyyy-MM-dd");
						// String todayIsOpen = SharedPreferencesUtil.getString(
						// this, TODAY_IS_OPEN_ACTIVITIES, "");
						// if (!df.format(new Date()).toString()
						// .equals(todayIsOpen)) {
						// obtainActivities();
						// }
						return true;
					}
					// 开始 显示对话框
					if (commentDialog != null && commentDialog.isShowing()) {
						return true;
					}
					try {
						myAppointmentVO = JSONUtil.toJavaBean(
								MyAppointmentVO.class,
								dataArray.getJSONObject(0));
					} catch (Exception e) {
						e.printStackTrace();

					}
					commentDialog = new NoCommentDialog(this);
					commentDialog.setCancelable(false);
					commentDialog.setCanceledOnTouchOutside(false);
					commentDialog
							.setClicklistener(new NoCommentDialogClickListener());
					commentDialog.show();

					// List<MyAppointmentVO> list = new
					// ArrayList<MyAppointmentVO>();
					//
					// for (int i = 0; i < length; i++) {
					// MyAppointmentVO appointmentVO = JSONUtil.toJavaBean(
					// MyAppointmentVO.class,
					// dataArray.getJSONObject(i));
					// list.add(appointmentVO);
					// }

				}

			} else if (type.equals(comment)) {
				if (dataString != null) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithSuccess("评论成功");
					commentDialog.dismiss();
					// new MyHandler(1000) {
					// @Override
					// public void run() {
					// Intent intent = new Intent();
					// setResult(RESULT_OK, intent);
					// finish();
					// }
					// };
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// if (!onClickSingleView()) {
		// return;
		// }
		LogUtil.print("sssssssssssssssssss");
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		MyAppointmentVO myAppointmentVO = adapter.getItem(position);
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
				ZProgressHUD.getInstance(this).dismissWithSuccess(
						"请在开课前15分钟内签到");
				ZProgressHUD.getInstance(this).show();
			} else if (diffEndTime / 1000 / 60 < 0) {
				ZProgressHUD.getInstance(this).dismissWithSuccess(
						"您的课程已结束，不能再签到");
				ZProgressHUD.getInstance(this).show();
			} else {
				Intent intent = new Intent(this, QRCodeCreateActivity.class);
				intent.putExtra("myappointment", myAppointmentVO);
				startActivity(intent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		isRefreshing = true;
		obtainOppointment();
	}

	private void comment(String reservationId, String starLevel, String content) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("reservationid", reservationId);
		paramMap.put("starlevel", starLevel);
		paramMap.put("attitudelevel", starLevel);
		paramMap.put("timelevel", starLevel);
		paramMap.put("abilitylevel", starLevel);
		paramMap.put("commentcontent", content);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(comment, this, Config.IP
				+ "api/v1/courseinfo/usercomment", paramMap, 10000, headerMap);
	}

	class NoCommentDialogClickListener implements ClickListenerInterface {

		@Override
		public void getMoreClick() {
			// 跳转到评论页面
			Intent intent = new Intent(TodaysAppointmentActivity.this,
					CommentActivity.class);
			intent.putExtra("appointmentVO", myAppointmentVO);
			intent.putExtra("position", getIntent().getIntExtra("position", 0));
			startActivityForResult(intent, 0);
			commentDialog.dismiss();
		}

		@Override
		public void commintClick() {
			int rating = (int) commentDialog.getRatingBar().getRating();
			String content = commentDialog.getEditText().getText().toString();
			LogUtil.print(rating + content);

			if (TextUtils.isEmpty(content.trim())) {
				commentDialog.showErrorHint(true);
			} else {
				commentDialog.showErrorHint(false);
				comment(myAppointmentVO.get_id(), rating + "", content);
			}
		}
	}
}
