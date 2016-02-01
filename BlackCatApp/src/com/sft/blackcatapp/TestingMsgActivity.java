package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.CustomDialog;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.SchoolVO;

/**
 * 报名界面
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("unused")
public class TestingMsgActivity extends BaseActivity {

	public static final String ISFROMTESTINGMSG = "isFromTestingMsg";

	private static final String enroll = "enroll";
	private RelativeLayout rootLayout;
	// 姓名输入框
	private EditText nameEt;
	// 身份证输入框
	private EditText cardEt;
	// 联系方式输入框
	private EditText contactEt;
	// 报考驾校文本
	private TextView schoolTv;
	// 报考车型文本
	private TextView carStyleTv;
	// 报考教练文本
	private TextView coachTv;
	// 报考班级文本
	// private TextView classTv;
	// 科目文本
	private TextView subjectTV;
	// 提交按钮
	private Button commitBtn;
	// 用户选择的驾校
	private SchoolVO school;
	// 用户选择的教练id
	private CoachVO coach;
	// 班型id
	private ClassVO classId;
	// 车型
	private CarModelVO carStyle;
	// 报名成功对话框
	private CustomDialog successDialog;
	// 报名状态
	private String enrollState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.testing_msg);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		setTitleText(R.string.enroll_info_table);

		rootLayout = (RelativeLayout) findViewById(R.id.testing_rootlayout);
		// rootLayout.setFocusableInTouchMode(true);
		// rootLayout.requestFocus();

		schoolTv = (TextView) findViewById(R.id.enroll_school_tv);
		carStyleTv = (TextView) findViewById(R.id.enroll_carstyle_tv);
		coachTv = (TextView) findViewById(R.id.enroll_coach_tv);
		// classTv = (TextView) findViewById(R.id.enroll_class_tv);
		subjectTV = (TextView) findViewById(R.id.enroll_subject_tv);

		commitBtn = (Button) findViewById(R.id.enroll_commit_btn);

		resizeDrawalbeLeftSize();
	}

	private void initData() {

		enrollState = app.userVO.getApplystate();
		//
		// Intent intent = getIntent();
		// if (intent.getBooleanExtra("userselect", false)) {
		// onActivityResult(intent.getIntExtra("requestCode", 0),
		// intent.getIntExtra("resultCode", 0), intent);
		// } else {
		// if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollState)) {
		// // 没有报过名，读取数据库中保存的用户选择信息填充
		// school = Util.getEnrollUserSelectedSchool(this);
		// if (school != null) {
		// schoolTv.setText(school.getName());
		// }
		//
		// carStyle = Util.getEnrollUserSelectedCarStyle(this);
		// if (carStyle != null) {
		// carStyleTv.setText(carStyle.getCode());
		// }
		//
		// classId = Util.getEnrollUserSelectedClass(this);
		// if (classId != null) {
		// classTv.setText(classId.getClassname());
		// }
		//
		// coach = Util.getEnrollUserSelectedCoach(this);
		// if (coach != null) {
		// coachTv.setText(coach.getName());
		// }
		//
		// } else {
		// // 用户已经报过名
		// if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
		// enrollState)) {
		//
		// }
		//
		// schoolTv.setText(app.userVO.getApplyschoolinfo().getName());
		// coachTv.setText(app.userVO.getApplycoachinfo().getName());
		// carStyleTv.setText(app.userVO.getCarmodel().getCode());
		// classTv.setText(app.userVO.getApplyclasstypeinfo().getName());
		// subjectTV.setText(app.userVO.getSubject().getName());
		// commitBtn.setEnabled(false);
		// }
		// }
	}

	private void resizeDrawalbeLeftSize() {
		// 显示箭头
		Resources r = getResources();
		int size = (int) (18 * screenDensity);
		Drawable arrow = r.getDrawable(R.drawable.person_center_arrow);
		arrow.setBounds(0, 0, size, size);

		schoolTv.setCompoundDrawables(null, null, arrow, null);
		carStyleTv.setCompoundDrawables(null, null, arrow, null);
		coachTv.setCompoundDrawables(null, null, arrow, null);
		// classTv.setCompoundDrawables(null, null, arrow, null);
		subjectTV.setCompoundDrawables(null, null, arrow, null);
	}

	private void setListener() {
		commitBtn.setOnClickListener(this);

		schoolTv.setOnClickListener(this);
		carStyleTv.setOnClickListener(this);
		coachTv.setOnClickListener(this);
		// classTv.setOnClickListener(this);
		subjectTV.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		if (v.getId() == R.id.base_left_btn) {
			finish();
			return;
		}
		if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollState)) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.enroll_school_tv:
				intent = new Intent(this, EnrollSchoolActivity1.class);
				if (school != null)
					intent.putExtra("school", school);
				intent.putExtra("select", 1);
				break;
			case R.id.enroll_carstyle_tv:
				if (school == null) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure("先选择驾校");
				} else {
					intent = new Intent(this, EnrollCarStyleActivity.class);
					if (carStyle != null)
						intent.putExtra("carStyle", carStyle);
				}
				break;
			case R.id.enroll_coach_tv:
				if (school == null) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure("先选择驾校");
				} else {
					intent = new Intent(this, EnrollCoachActivity.class);
					intent.putExtra("schoolId", school.getSchoolid());
					if (coach != null)
						intent.putExtra("coach", coach);
					intent.putExtra(ISFROMTESTINGMSG, true);
				}
				break;
			case R.id.enroll_class_tv:
				if (school == null) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure("先选择驾校");
				} else {
					intent = new Intent(this, EnrollClassActivity.class);
					intent.putExtra("schoolId", school.getSchoolid());
					if (classId != null)
						intent.putExtra("class", classId);
				}
				break;
			case R.id.enroll_subject_tv:

				break;
			case R.id.enroll_commit_btn:
				enroll();
				break;
			}
			if (intent != null) {
				startActivityForResult(intent, v.getId());
			}
		}
	}

	private void enroll() {
		String checkResult = checkEnrollInfo();
		if (checkResult == null) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("schoolid", school.getSchoolid());
			paramMap.put("coachid", coach.getCoachid());
			paramMap.put("classtypeid", classId.getCalssid());
			paramMap.put("carmodel", carStyle.toString());

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(enroll, this, Config.IP
					+ "api/v1/userinfo/userapplyschool", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
	}

	private String checkEnrollInfo() {
		if (school == null) {
			return "驾校为空";
		}
		if (coach == null) {
			return "教练为空";
		}
		// if (classId == null) {
		// return "班型为空";
		// }
		if (carStyle == null) {
			return "车型为空";
		}
		return null;
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(enroll)) {
			Intent intent = new Intent(this, TestingCommit.class);
			startActivity(intent);
			finish();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data == null) {
			return;
		}
		switch (requestCode) {
		case R.id.enroll_school_tv:
			// 报名页面选择学校
			school = (SchoolVO) data.getSerializableExtra("school");
			if (school != null) {
				schoolTv.setText(school.getName());
			}

			break;
		case R.id.main_appointment_course_layout:
			// 点击首页驾校卡
			SchoolVO tempSchool = (SchoolVO) data
					.getSerializableExtra("school");
			if (school == null || !school.equals(tempSchool)) {
				// 更换驾校，并删除了相应的冲突信息
				school = tempSchool;
				schoolTv.setText(school.getName());
				coach = null;
				coachTv.setText("");
				classId = null;
				// classTv.setText("");
				carStyle = null;
				carStyleTv.setText("");
			}

			coach = (CoachVO) data.getSerializableExtra("coach");
			if (coach != null) {
				coachTv.setText(coach.getName());
			}
			break;
		case R.id.enroll_carstyle_tv:
			// 报名页面选择车型，在选择车型的页面进行了保存
			carStyle = (CarModelVO) data.getSerializableExtra("carStyle");
			carStyleTv.setText(carStyle.getCode());
			break;
		case R.id.enroll_coach_tv:
			// 报名页面选择教练
			coach = (CoachVO) data.getSerializableExtra("coach");
			coachTv.setText(coach.getName());
			break;
		case R.id.enroll_class_tv:
			// 报名页面选择班级
			classId = (ClassVO) data.getSerializableExtra("class");
			// classTv.setText(classId.getClassname());
			break;
		case R.id.main_appointment_layout:
			// 报名首页点击教练卡，选择教练点击报完成后
		case R.id.main_my_layout:
			// 我喜欢的教练，驾校
			school = null;
			schoolTv.setText("");
			coach = null;
			coachTv.setText("");
			classId = null;
			// classTv.setText("");
			carStyle = null;
			carStyleTv.setText("");

			coach = (CoachVO) data.getSerializableExtra("coach");

			if (coach != null) {
				coachTv.setText(coach.getName());
				school = Util.getEnrollUserSelectedSchool(this);
				if (school != null) {
					schoolTv.setText(school.getName());
				}
			} else {
				school = (SchoolVO) data.getSerializableExtra("school");
				if (school != null) {
					schoolTv.setText(school.getName());
				}
			}

			carStyle = Util.getEnrollUserSelectedCarStyle(this);
			if (carStyle != null) {
				carStyleTv.setText(carStyle.getCode());
			}

			// classId = Util.getEnrollUserSelectedClass(this);
			// if (classId != null) {
			// classTv.setText(classId.getClassname());
			// }

			break;
		}
	}

	private class MyEditChangedListener implements TextWatcher {
		private String style;

		public MyEditChangedListener(String style) {
			this.style = style;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			util.saveParam(style + app.userVO.getUserid(), s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	}

}
