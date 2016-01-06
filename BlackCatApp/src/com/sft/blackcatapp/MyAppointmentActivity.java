package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import com.sft.adapter.MyAppointmentListAdapter;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.common.Config.AppointmentResult;
import com.sft.dialog.ApplyExamDialog;
import com.sft.dialog.CustomDialog;
import com.sft.util.JSONUtil;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.UserVO;
import com.sft.vo.uservo.StudentSubject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.pull.RefreshLoadMoreView;
import cn.sft.pull.RefreshLoadMoreView.RefreshLoadMoreListener;

/**
 * 我的预约
 * 
 * @author Administrator
 * 
 */
public class MyAppointmentActivity extends BaseActivity implements RefreshLoadMoreListener {

	private static final String reservation = "reservation";
	private static final String applyExam = "applyExam";
	private static final String MYPROGRESS = "getmyprogress";
	//
	private RefreshLoadMoreView appointmentList;
	//
	private MyAppointmentListAdapter adapter;
	//
	private Button appointmentBtn;
	//
	private TextView subjectValueTv, subjectTextTv;
	//
	private StudentSubject subject = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_my_appointment);
		initView();
		obtainOppointment();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		//更新数据显示
		if (subject != null) {
			String curProgress = subject.getProgress();
			subjectTextTv.setText(getString(R.string.cur_progress) + curProgress);
			subjectTextTv.setOnClickListener(this);
			Log.d("tag","我的预约-->>"+(subject.getReservation() + subject.getFinishcourse())+"total-->"+subject.getTotalcourse());
			if (subject.getReservation() + subject.getFinishcourse() >= subject.getTotalcourse()) {
				appointmentBtn.setText(app.userVO.getSubject().getName() + "学时已约满");
			} else {
				appointmentBtn.setOnClickListener(this);
			}
		} else {
			appointmentBtn.setVisibility(View.GONE);
			subjectTextTv.setText(getString(R.string.cur_progress));
		}
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.my_appointment);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.requirements);
		rightTV.setTextColor(Color.parseColor("#cccccc"));

		appointmentList = (RefreshLoadMoreView) findViewById(R.id.my_appointment_listview);
		appointmentBtn = (Button) findViewById(R.id.my_appointment_app_btn);
		subjectValueTv = (TextView) findViewById(R.id.my_appointment_subject_value_tv);
		subjectTextTv = (TextView) findViewById(R.id.my_appointment_subject_text_tv);

		String subjectId = app.userVO.getSubject().getSubjectid();
		subjectValueTv.setText(subjectId);

		if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
			subject = app.userVO.getSubjecttwo();
		} else if (subjectId.equals(Config.SubjectStatu.SUBJECT_THREE.getValue())) {
			subject = app.userVO.getSubjectthree();
		}
		
		
		
		//只会在初始化时 进行数据的更新，， 放到onresume中 
//		if (subject != null) {
//			String curProgress = subject.getProgress();
//			subjectTextTv.setText(getString(R.string.cur_progress) + curProgress);
//			subjectTextTv.setOnClickListener(this);
//			Log.d("tag","我的预约-->>"+(subject.getReservation() + subject.getFinishcourse())+"total-->"+subject.getTotalcourse());
//			if (subject.getReservation() + subject.getFinishcourse() >= subject.getTotalcourse()) {
//				appointmentBtn.setText(app.userVO.getSubject().getName() + "学时已约满");
//			} else {
//				appointmentBtn.setOnClickListener(this);
//			}
//		} else {
//			appointmentBtn.setVisibility(View.GONE);
//			subjectTextTv.setText(getString(R.string.cur_progress));
//		}

		appointmentList.setRefreshLoadMoreListener(this);
	}
	
	

	private void obtainOppointment() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("subjectid", getIntent().getStringExtra("subject"));
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(reservation, this, Config.IP + "api/v1/courseinfo/getmyreservation", paramMap, 10000,
				headerMap);
		requestStatus();
	}
	
	/**
	 * 学员预约--查询学生的学习进度
	 */
	private void requestStatus(){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(MYPROGRESS, this, Config.IP + "api/v1/userinfo/getmyprogress", paramMap, 10000,
				headerMap);
	}

	private boolean checkStudyTime() {
		if (subject != null) {
			if (subject.getFinishcourse() == (subject.getTotalcourse())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			if (checkStudyTime()) {
				applyExam();
			} else {
				ApplyExamDialog dialog = new ApplyExamDialog(this);
				dialog.show();
			}
			break;
		case R.id.my_appointment_app_btn:
			Intent intent = new Intent(this, AppointmentCarActivity.class);
			startActivityForResult(intent, v.getId());
			break;
		case R.id.my_appointment_subject_text_tv:
			try {
				List<MyAppointmentVO> list = adapter.getData();
				int index = 0;
				for (; index < list.size(); index++) {
					if (list.get(index).get_id().equals(subject.getReservationid())) {
						break;
					}
				}
				appointmentList.smoothscrollToIndex(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			obtainOppointment();
		}
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (type.equals(reservation))
			appointmentList.stopRefresh();
		super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (type.equals(reservation))
			appointmentList.stopRefresh();
		super.doTimeOut(type);
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
					Log.d("tag",length+"isRefresh---doCallBack>"+jsonString);
					for (int i = 0; i < length; i++) {
						MyAppointmentVO appointmentVO = (MyAppointmentVO) JSONUtil.toJavaBean(MyAppointmentVO.class,
								dataArray.getJSONObject(i));
						list.add(appointmentVO);
					}
					
					
					adapter = new MyAppointmentListAdapter(this, list);
					appointmentList.setAdapter(adapter);
					appointmentList.stopRefresh();
				}
			} else if (type.equals(applyExam)) {
				if (dataString != null) {
					CustomDialog dialog = new CustomDialog(this, CustomDialog.APPLY_EXAM);
					dialog.show();
				}
			}else if(type.equals(MYPROGRESS)){
				if(null!=data){
					UserVO userVo = (UserVO) JSONUtil.toJavaBean(UserVO.class,
							data);
					Log.d("tag","userVo--2f->"+data);
					String subjectId = userVo.getSubject().getSubjectid();
					StudentSubject tempSubject = null;
					//获取当前学习的 课，科目2 或者科目3
					if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
						tempSubject = userVo.getSubjecttwo();
					} else if (subjectId.equals(Config.SubjectStatu.SUBJECT_THREE.getValue())) {
						tempSubject = userVo.getSubjectthree();
					}
					
					if (tempSubject.getReservation() + tempSubject.getFinishcourse() >= tempSubject.getTotalcourse()) {
						appointmentBtn.setText(userVo.getSubject().getName() + "学时已约满");
						appointmentBtn.setOnClickListener(null);
					} else {
						appointmentBtn.setText( "预约学车");
						appointmentBtn.setOnClickListener(this);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void applyExam() {
		util.print("applyExam");
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(applyExam, this, Config.IP + "api/v1/userinfo/applyexamination", null, 10000,
				headerMap);
	}

	@Override
	public void forOperResult(Intent intent) {
		Log.d("tag","MyAppoint-->"+intent);
		if (!onClickSingleView()) {
			return;
		}
		boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
		if (isRefresh) {
			obtainOppointment();
		}

		boolean isApplyExam = intent.getBooleanExtra("isApplyExam", false);
		if (isApplyExam) {
			applyExam();
			return;
		}

		boolean isRefreshState = intent.getBooleanExtra("refreshState", false);
		if (isRefreshState) {
			int position = intent.getIntExtra("position", 0);
			String curState = adapter.getItem(position).getReservationstate();
			if (curState.equals(Config.AppointmentResult.applyrefuse.getValue())
					|| curState.equals(Config.AppointmentResult.applycancel.getValue())
					|| curState.equals(Config.AppointmentResult.finish.getValue())) {
				return;
			}
			String state = intent.getStringExtra("state");
			if (!curState.equals(state)){
				adapter.changeState(position, state);
				//状态已经改变了， 请求最新的数据
				obtainOppointment();
			}
				
			return;
		}
		boolean isJump = intent.getBooleanExtra("isJump", false);
		if (isJump) {
			int position = intent.getIntExtra("position", 0);
			Intent intent1 = new Intent(this, AppointmentDetailActivity.class);
			intent1.putExtra("appointment", adapter.getItem(position));
			intent1.putExtra("position", position);
			startActivity(intent1);
		}
	}

	@Override
	public void onRefresh() {
		obtainOppointment();
	}

	@Override
	public void onLoadMore() {

	}
	
	
}
