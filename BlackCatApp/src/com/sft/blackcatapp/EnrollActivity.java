package com.sft.blackcatapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzjf.app.R;
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
public class EnrollActivity extends BaseActivity {

	private static final String enroll = "enroll";
	private RelativeLayout rootLayout;
	// 头像图片
	private ImageView headPicIm;
	// 姓名输入框
	private EditText nameEt;
	// 身份证输入框
	private EditText cardEt;
	// 联系方式输入框
	private EditText contactEt;
	// 常用地址输入框
	private EditText addressEt;
	// 报考驾校文本
	private TextView schoolTv;
	// 报考车型文本
	private TextView carStyleTv;
	// 报考教练文本
	private TextView coachTv;
	// 报考班级文本
	private TextView classTv;
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
	// 布局按钮
	private RelativeLayout schoolRL;
	private RelativeLayout carStyleRL;
	private RelativeLayout coachRL;
	private RelativeLayout classRL;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.apply_driving);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		
		setTitleText(R.string.enroll_info_table);
		
		rootLayout = (RelativeLayout) findViewById(R.id.enroll_rootlayout);
		rootLayout.setFocusable(true);
		rootLayout.setFocusableInTouchMode(true);
		rootLayout.requestFocus();

		schoolTv = (TextView) findViewById(R.id.enroll_school_tv);
		carStyleTv = (TextView) findViewById(R.id.enroll_carstyle_tv);
		coachTv = (TextView) findViewById(R.id.enroll_coach_tv);
		classTv = (TextView) findViewById(R.id.enroll_class_tv);

		schoolRL = (RelativeLayout) findViewById(R.id.enroll_school_rl);
		carStyleRL = (RelativeLayout) findViewById(R.id.enroll_carstyle_rl);
		coachRL = (RelativeLayout) findViewById(R.id.enroll_coach_rl);
		classRL = (RelativeLayout) findViewById(R.id.enroll_class_rl);

		commitBtn = (Button) findViewById(R.id.enroll_commit_btn);

		resizeDrawalbeLeftSize();
	}

	private void initData() {

		enrollState = app.userVO.getApplystate();

		Intent intent = getIntent();
		if (intent.getBooleanExtra("userselect", false)) {
			onActivityResult(intent.getIntExtra("requestCode", 0),
					intent.getIntExtra("resultCode", 0), intent);
		} else {
			if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollState)) {
				// 没有报过名，读取数据库中保存的用户选择信息填充
				carStyle = Util.getEnrollUserSelectedCarStyle(this);
				if (carStyle != null) {
					carStyleTv.setText(carStyle.getCode());
				}

				school = Util.getEnrollUserSelectedSchool(this);
				if (school != null) {
					schoolTv.setText(school.getName());
				}

				classId = Util.getEnrollUserSelectedClass(this);
				if (classId != null) {
					classTv.setText(classId.getClassname());
				}

				coach = Util.getEnrollUserSelectedCoach(this);
				if (coach != null) {
					coachTv.setText(coach.getName());
				}
			} else {
				carStyleTv.setText(app.userVO.getCarmodel().getCode());
				schoolTv.setText(app.userVO.getApplyschoolinfo().getName());
				classTv.setText(app.userVO.getApplyclasstypeinfo().getName());
				coachTv.setText(app.userVO.getApplycoachinfo().getName());

			}
		}
	}

	private void resizeDrawalbeLeftSize() {
		// 显示箭头
		Resources r = getResources();
		int size = (int) (18 * screenDensity);
		Drawable arrow = r.getDrawable(R.drawable.person_center_arrow);
		arrow.setBounds(0, 0, size, size);

		carStyleTv.setCompoundDrawables(null, null, arrow, null);
		schoolTv.setCompoundDrawables(null, null, arrow, null);
		classTv.setCompoundDrawables(null, null, arrow, null);
		coachTv.setCompoundDrawables(null, null, arrow, null);
	}

	private void setListener() {
		commitBtn.setOnClickListener(this);

		schoolTv.setOnClickListener(this);
		carStyleTv.setOnClickListener(this);
		coachTv.setOnClickListener(this);
		classTv.setOnClickListener(this);

		// nameEt.addTextChangedListener(new MyEditChangedListener(realName));
		// cardEt.addTextChangedListener(new MyEditChangedListener(idcard));
		// contactEt.addTextChangedListener(new MyEditChangedListener(contact));
		// addressEt.addTextChangedListener(new MyEditChangedListener(address));
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
			case R.id.enroll_school_tv:
				intent = new Intent(this, EnrollSchoolActivity.class);
				if (school != null)
					intent.putExtra("school", school);
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
			case R.id.enroll_coach_tv:
				if (school == null) {
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this).dismissWithFailure("先选择驾校");
				} else {
					intent = new Intent(this, EnrollCoachActivity.class);
					intent.putExtra("schoolId", school.getSchoolid());
					if (coach != null)
						intent.putExtra("coach", coach);
				}
				break;
			case R.id.enroll_commit_btn:
				// enroll();
				Intent intentt = new Intent(this, EnrollNextActivity.class);
				startActivity(intentt);
				// finish();
				break;
			}
			if (intent != null) {
				startActivityForResult(intent, v.getId());
			}
		}
	}

	private String checkEnrollInfo() {
		if (carStyle == null) {
			
			return "车型为空";
		}
		if (school == null) {
			return "驾校为空";
		}
		if (classId == null) {
			return "班型为空";
		}
		if (coach == null) {
			return "教练为空";
		}
		return null;
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		// if (type.equals(enroll)) {
		// Intent intent = new Intent(this, EnrollNextActivity.class);
		// startActivity(intent);
		// finish();
		// }
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
				classTv.setText("");
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
			classTv.setText(classId.getClassname());
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
			classTv.setText("");
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

			classId = Util.getEnrollUserSelectedClass(this);
			if (classId != null) {
				classTv.setText(classId.getClassname());
			}

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
