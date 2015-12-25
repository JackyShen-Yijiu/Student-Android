package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.SchoolVO;

public class ApplyActivity extends BaseActivity {

	private LinearLayout applyClassTypeLayout;
	private static final String realName = "realName";
	private static final String contact = "contact";
	private static final String enroll = "enroll";
	private final static String carStyleString = "carStyle";

	String[] tempStrings;
	// 姓名输入框
	private EditText nameEt;
	// 联系方式输入框
	private EditText contactEt;
	// 报考驾校文本
	private TextView schoolTv;
	// 报考教练文本
	private TextView coachTv;
	// 报考车型文本
	private TextView carStyleTv;
	// 报考班级文本
	private TextView classTv;
	// 用户选择的驾校
	private SchoolVO school;
	// 班型id
	private ClassVO classId;
	// 车型
	private CarModelVO carStyle;
	// 用户选择的教练id
	private CoachVO coach;
	// 报名状态
	private String enrollState;

	private RelativeLayout enroll_rootlayout;
	private Button commitBtn;

	private static final String classInfo = "classInfo";
	private TextView licenseTypeC1;
	private TextView licenseTypeC2;
	private List<CarModelVO> listCarModelVOs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addView(R.layout.new_apply);
		initView();
		initData();
		setListener();
		obtainEnrollCarStyle();
	}

	private void obtainEnrollCarStyle() {
		HttpSendUtils.httpGetSend(carStyleString, this, Config.IP
				+ "api/v1/info/carmodel");
	}

	private void initView() {
		setTitleText(R.string.enroll_info_table);

		enroll_rootlayout = (RelativeLayout) findViewById(R.id.enroll_rootlayout);
		enroll_rootlayout.setFocusable(true);
		enroll_rootlayout.setFocusableInTouchMode(true);
		enroll_rootlayout.requestFocus();

		applyClassTypeLayout = (LinearLayout) findViewById(R.id.apply_class_type_ll);

		schoolTv = (TextView) findViewById(R.id.enroll_school_tv);
		coachTv = (TextView) findViewById(R.id.enroll_coach_tv);
		// carStyleTv = (TextView) findViewById(R.id.enroll_carstyle_tv);
		nameEt = (EditText) findViewById(R.id.enroll_name_et);
		contactEt = (EditText) findViewById(R.id.enroll_contact_et);

		commitBtn = (Button) findViewById(R.id.enroll_commit_btn);

		licenseTypeC1 = (TextView) findViewById(R.id.apply_license_type_c1);
		licenseTypeC2 = (TextView) findViewById(R.id.apply_license_type_c2);
	}

	private void initData() {
		enrollState = app.userVO.getApplystate();
		// String name = util.readParam(realName + app.userVO.getUserid());
		// if (!TextUtils.isEmpty(name)) {
		// nameEt.setText(name);
		// } else {
		// nameEt.setText(app.userVO.getName());
		// }
		// String contac = util.readParam(contact + app.userVO.getUserid());
		// if (!TextUtils.isEmpty(contac)) {
		// contactEt.setText(contac);
		// } else {
		// contactEt.setText(app.userVO.getMobile());
		// }
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
		// } else {
		// // 用户已经报过名
		// if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
		// enrollState)) {
		// commitBtn.setText("报名审核中...");
		// } else {
		// commitBtn.setText("已成功报名");
		// }
		// schoolTv.setText(app.userVO.getApplyschoolinfo().getName());
		// coachTv.setText(app.userVO.getApplycoachinfo().getName());
		// carStyleTv.setText(app.userVO.getCarmodel().getCode());
		// classTv.setText(app.userVO.getApplyclasstypeinfo().getName());
		//
		// nameEt.setEnabled(false);
		// contactEt.setEnabled(false);
		// commitBtn.setEnabled(false);
		// }
		// }
	}

	private void setListener() {
		commitBtn.setOnClickListener(this);
		schoolTv.setOnClickListener(this);
		// carStyleTv.setOnClickListener(this);
		coachTv.setOnClickListener(this);
		// classTv.setOnClickListener(this);

		nameEt.addTextChangedListener(new MyEditChangedListener(realName));
		contactEt.addTextChangedListener(new MyEditChangedListener(contact));

		licenseTypeC1.setOnClickListener(this);
		licenseTypeC2.setOnClickListener(this);
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
			case R.id.apply_license_type_c1:
				carStyle = listCarModelVOs.get(0);
				changeLicenseTextColor(1);
				break;
			case R.id.apply_license_type_c2:
				carStyle = listCarModelVOs.get(1);
				changeLicenseTextColor(2);
				break;
			case R.id.enroll_school_tv:
				intent = new Intent(this, EnrollSchoolActivity.class);
				if (school != null)
					intent.putExtra("school", school);
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
			case R.id.enroll_commit_btn:
				enroll();
				break;
			}
			if (intent != null) {
				startActivityForResult(intent, v.getId());
			}
		}
	}

	private void changeLicenseTextColor(int tag) {
		licenseTypeC1.setTextColor(CommonUtil.getColor(this,
				R.color.default_text_color));
		licenseTypeC2.setTextColor(CommonUtil.getColor(this,
				R.color.default_text_color));
		switch (tag) {
		case 1:
			licenseTypeC1.setTextColor(CommonUtil.getColor(this,
					R.color.app_main_color));
			break;
		case 2:
			licenseTypeC2.setTextColor(CommonUtil.getColor(this,
					R.color.app_main_color));

			break;

		default:
			break;
		}
	}

	private void enroll() {
		String checkResult = checkEnrollInfo();
		if (checkResult == null) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("name", nameEt.getText().toString());
			paramMap.put("telephone", contactEt.getText().toString());
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
		String name = nameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			return "姓名为空";
		}

		String phone = contactEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			return "联系方式为空";
		}
		if (school == null) {
			return "驾校为空";
		}
		if (coach == null) {
			return "教练为空";
		}
		if (classId == null) {
			return "班型为空";
		}
		if (carStyle == null) {
			return "车型为空";
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data == null) {
			return;
		}
		switch (requestCode) {
		case R.id.enroll_school_tv:
			// 报名页面选择学校
			SchoolVO tempSchool = (SchoolVO) data
					.getSerializableExtra("school");
			if (school == null || !school.equals(tempSchool)) {
				// 更换驾校，并删除了相应的冲突信息
				school = tempSchool;
				schoolTv.setText(school.getName());
				coach = null;
				coachTv.setText("");
				obtainEnrollClass();
				// classId = null;
				// classTv.setText("");
				// carStyle = null;
				// carStyleTv.setText("");
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

	@SuppressLint("NewApi")
	private void addClassType() {
		if (tempStrings == null || tempStrings.length == 0) {
			return;
		}
		switch (tempStrings.length) {
		case 1:
			TextView classTypeTv = new TextView(this);
			classTypeTv.setText(tempStrings[0]);
			classTypeTv.setTextColor(CommonUtil.getColor(this,
					R.color.default_text_color));
			classTypeTv.setTextSize(14);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			applyClassTypeLayout.addView(classTypeTv, params);
			break;

		default:
			for (int i = 0; i < tempStrings.length;) {

				LinearLayout ClassType = new LinearLayout(this);
				LayoutParams paramsLl = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				if (i != 0) {
					paramsLl.topMargin = CommonUtil.dip2px(this, 27);

				}
				ClassType.setLayoutDirection(LinearLayout.HORIZONTAL);
				TextView classTypeTv1 = new TextView(this);
				classTypeTv1.setText(tempStrings[i]);
				classTypeTv1.setTextColor(CommonUtil.getColor(this,
						R.color.default_text_color));
				classTypeTv1.setTextSize(14);
				LayoutParams params1 = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				ClassType.addView(classTypeTv1, params1);

				i++;
				if (i != tempStrings.length) {
					TextView classTypeTv2 = new TextView(this);
					classTypeTv2.setText(tempStrings[i]);
					classTypeTv2.setTextColor(CommonUtil.getColor(this,
							R.color.default_text_color));
					classTypeTv2.setTextSize(14);
					LayoutParams params2 = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params2.leftMargin = CommonUtil.dip2px(this, 25);
					ClassType.addView(classTypeTv2, params2);
					i++;
				}

				applyClassTypeLayout.addView(ClassType, paramsLl);
			}
			break;
		}
	}

	private void obtainEnrollClass() {
		if (school != null) {
			String schoolId = school.getSchoolid();
			LogUtil.print("schoolId===" + schoolId);
			HttpSendUtils.httpGetSend(classInfo, this, Config.IP
					+ "api/v1/driveschool/schoolclasstype/" + schoolId);
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (type.equals(classInfo)) {
				if (dataArray != null) {
					int length = dataArray.length();
					List<ClassVO> list = new ArrayList<ClassVO>();
					for (int i = 0; i < length; i++) {
						ClassVO classVO = (ClassVO) JSONUtil.toJavaBean(
								ClassVO.class, dataArray.getJSONObject(i));
						list.add(classVO);
					}
					setclassName(list);
				}
			} else if (type.equals(enroll)) {
				// Intent intent = new Intent(this,
				// EnrollSuccessActivity.class);
				// startActivity(intent);
				// finish();
			} else if (type.equals(carStyleString)) {
				if (dataArray != null) {
					int length = dataArray.length();
					listCarModelVOs = new ArrayList<CarModelVO>();
					for (int i = 0; i < length; i++) {
						CarModelVO carStyleVO = (CarModelVO) JSONUtil
								.toJavaBean(CarModelVO.class,
										dataArray.getJSONObject(i));
						listCarModelVOs.add(carStyleVO);
					}
					setLienseType(listCarModelVOs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void setLienseType(List<CarModelVO> listCarModelVOs) {
		licenseTypeC1.setText(listCarModelVOs.get(0).getCode() + "手动档");
		licenseTypeC2.setText(listCarModelVOs.get(1).getCode() + "自动档");
	}

	private void setclassName(List<ClassVO> list) {

		tempStrings = new String[list.size()];
		int i = 0;
		for (ClassVO classVO : list) {
			tempStrings[i] = classVO.getClassname() + "班¥" + classVO.getPrice();
			i++;
		}
		addClassType();
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