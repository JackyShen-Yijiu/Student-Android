package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.common.Config.AppointmentResult;
import com.sft.common.Config.SubjectStatu;
import com.sft.viewutil.StudyContentLayout;
import com.sft.viewutil.StudyContentLayout.StudyContentSelectChangeListener;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;

/**
 * 确认学完界面
 * 
 * @author Administrator
 * 
 */
public class ConfirmStudyActivity extends BaseActivity implements
		StudyContentSelectChangeListener {

	private static final String confirmStudy = "confirmStudy";
	private StudyContentLayout contentLayout;
	private EditText confirmStudyEt;
	private ImageView headPicIm;
	private TextView coachNameTv;
	private TextView coachSchoolTv;
	private Button confirmBtn;
	private TextView contentTitleTv;

	private MyAppointmentVO appointmentVO;

	private SparseArray<String> studyContent = new SparseArray<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_confirm_study);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.study_content_confirm);

		contentLayout = (StudyContentLayout) findViewById(R.id.confirm_study_content_layout);
		contentTitleTv = (TextView) findViewById(R.id.confirm_study_content_tv);
		headPicIm = (ImageView) findViewById(R.id.confirm_study_headpic_im);
		coachNameTv = (TextView) findViewById(R.id.confirm_study_coachname_tv);
		coachSchoolTv = (TextView) findViewById(R.id.confirm_study_coachschool_tv);
		confirmStudyEt = (EditText) findViewById(R.id.confirm_study_et);
		confirmBtn = (Button) findViewById(R.id.confirm_study_btn);

		confirmBtn.setOnClickListener(this);

		confirmBtn.setFocusable(true);
		confirmBtn.setFocusableInTouchMode(true);
		confirmBtn.requestFocus();
	}

	private void initData() {
		String subjectId = app.userVO.getSubject().getSubjectid();
		if (subjectId.equals(Config.SubjectStatu.SUBJECT_TWO.getValue())) {
			contentLayout.setContent(app.subjectTwoContent);
		} else if (subjectId.equals(Config.SubjectStatu.SUBJECT_THREE
				.getValue())) {
			contentLayout.setContent(app.subjectThreeContent);
		}
		if (app.userVO.getSubject().getSubjectid()
				.equals(SubjectStatu.SUBJECT_TWO.getValue())) {
			contentTitleTv.setText(R.string.subject_two_content);
		} else if (app.userVO.getSubject().getSubjectid()
				.equals(SubjectStatu.SUBJECT_THREE.getValue())) {
			contentTitleTv.setText(R.string.subject_three_content);
		}
		confirmStudyEt.setHint(setHint(R.string.other_study_discr));

		appointmentVO = (MyAppointmentVO) getIntent().getSerializableExtra(
				"appointmentVO");
		if (appointmentVO != null) {
			String headUrl = appointmentVO.getCoachid().getHeadportrait()
					.getOriginalpic();
			LinearLayout.LayoutParams headParam = (LinearLayout.LayoutParams) headPicIm
					.getLayoutParams();
			if (TextUtils.isEmpty(headUrl)) {
				headPicIm.setBackgroundResource(R.drawable.default_small_pic);
			} else {
				BitmapManager.INSTANCE.loadBitmap2(headUrl, headPicIm,
						headParam.width, headParam.height);
			}
			coachSchoolTv.setText(appointmentVO.getCoachid()
					.getDriveschoolinfo().getName());
			coachNameTv.setText(appointmentVO.getCoachid().getName());
		}
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
		case R.id.confirm_study_btn:
			if (studyContent.size() == 0) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("请选择教学内容");
				return;
			}
			if (appointmentVO.getReservationstate().equals(
					AppointmentResult.unconfirmfinish.getValue())) {
				confirmStudy();
			} else {
				Intent intent = new Intent(this, CommentActivity.class);
				intent.putExtra("appointmentVO", appointmentVO);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivityForResult(intent, confirmBtn.getId());
			}
			break;
		}
	}

	private void confirmStudy() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", app.userVO.getUserid());
		paramsMap.put("reservationid", appointmentVO.get_id());

		String content = "";
		Log.d("tag","confirm--studyact-->"+studyContent.size());
		for (int i = 0; i < studyContent.size(); i++) {
			String value = studyContent.get(studyContent.keyAt(i));
			content += (value + "、");
		}
		content = content.substring(0, content.length() - 1);
		paramsMap.put("learningcontent", content);

		String otherContent = confirmStudyEt.getText().toString();
		if (!TextUtils.isEmpty(otherContent)) {
			paramsMap.put("contentremarks", otherContent);
		}

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());

		HttpSendUtils.httpPostSend(confirmStudy, this, Config.IP
				+ "api/v1/courseinfo/finishreservation", paramsMap, 10000,
				headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(confirmStudy)) {
			if (dataString != null) {
				// 学员确认科目所学内容后跳转
				Intent intent = new Intent(this, CommentActivity.class);
				intent.putExtra("appointmentVO", appointmentVO);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				startActivityForResult(intent, confirmBtn.getId());
			}
		}
		return true;
	}

	@Override
	public void onStudyContentChange(int index, boolean isChecked,
			String content) {
		if (isChecked) {
			studyContent.put(index, content);
		} else {
			studyContent.remove(index);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, final int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode != RESULT_OK) {
				Intent intent = new Intent(
						MyAppointmentActivity.class.getName());
				intent.putExtra("refreshState", true);
				intent.putExtra("position",
						getIntent().getIntExtra("position", 0));
				intent.putExtra("state", AppointmentResult.ucomments.getValue());
				sendBroadcast(intent);
			}
			// 页面回退
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(resultCode, data);
					finish();
				}
			};
		}
	}
}
