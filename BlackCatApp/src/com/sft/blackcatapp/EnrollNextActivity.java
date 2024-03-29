package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.blackcatapp.R;
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
public class EnrollNextActivity extends BaseActivity {

	private static final String realName = "realName";
	private static final String idcard = "idcard";
	private static final String contact = "contact";
	private static final String address = "address";
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.apply_msg);
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

		commitBtn = (Button) findViewById(R.id.enroll_commit_btn);
		headPicIm = (ImageView) findViewById(R.id.enroll_headpic_im);

		nameEt = (EditText) findViewById(R.id.enroll_name_et);
		cardEt = (EditText) findViewById(R.id.enroll_card_et);
		contactEt = (EditText) findViewById(R.id.enroll_contact_et);
		addressEt = (EditText) findViewById(R.id.enroll_address_et);

		nameEt.setHint(setHint(R.string.real_name));
		cardEt.setHint(setHint(R.string.idcard));
		contactEt.setHint(setHint(R.string.contact));
		addressEt.setHint(setHint(R.string.address));

		// resizeDrawalbeLeftSize();
	}

	private void initData() {

		enrollState = app.userVO.getApplystate();

		String name = util.readParam(realName + app.userVO.getUserid());
		if (!TextUtils.isEmpty(name)) {
			nameEt.setText(name);
		} else {
			nameEt.setText(app.userVO.getName());
		}
		String contac = util.readParam(contact + app.userVO.getUserid());
		if (!TextUtils.isEmpty(contac)) {
			contactEt.setText(contac);
		} else {
			contactEt.setText(app.userVO.getMobile());
		}
		String card = util.readParam(idcard + app.userVO.getUserid());
		if (!TextUtils.isEmpty(card)) {
			cardEt.setText(card);
		} else {
			cardEt.setText(app.userVO.getIdcardnumber());
		}
		String add = util.readParam(address + app.userVO.getUserid());
		if (!TextUtils.isEmpty(add)) {
			addressEt.setText(add);
		} else {
			addressEt.setText(app.userVO.getAddress());
		}

		Intent intent = getIntent();
		if (intent.getBooleanExtra("userselect", false)) {
			onActivityResult(intent.getIntExtra("requestCode", 0),
					intent.getIntExtra("resultCode", 0), intent);
		} else {
			if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollState)) {
				// 没有报过名，读取数据库中保存的用户选择信息填充
				school = Util.getEnrollUserSelectedSchool(this);
				if (school != null) {
					// schoolTv.setText(school.getName());
				}

				carStyle = Util.getEnrollUserSelectedCarStyle(this);
				if (carStyle != null) {
					// carStyleTv.setText(carStyle.getCode());
				}

				classId = Util.getEnrollUserSelectedClass(this);
				if (classId != null) {
					// classTv.setText(classId.getClassname());
				}

				coach = Util.getEnrollUserSelectedCoach(this);
				if (coach != null) {
					// coachTv.setText(coach.getName());
				}
			} else {
				// 用户已经报过名
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

				nameEt.setEnabled(false);
				contactEt.setEnabled(false);
				cardEt.setEnabled(false);
				addressEt.setEnabled(false);
				commitBtn.setEnabled(false);
			}
		}
	}

	private void resizeDrawalbeLeftSize() {
		// 显示箭头
		Resources r = getResources();
		int size = (int) (18 * screenDensity);
		Drawable arrow = r.getDrawable(R.drawable.person_center_arrow);
		arrow.setBounds(0, 0, size, size);

		// schoolTv.setCompoundDrawables(null, null, arrow, null);
		carStyleTv.setCompoundDrawables(null, null, arrow, null);
		coachTv.setCompoundDrawables(null, null, arrow, null);
		classTv.setCompoundDrawables(null, null, arrow, null);
	}

	private void setListener() {
		commitBtn.setOnClickListener(this);

		nameEt.addTextChangedListener(new MyEditChangedListener(realName));
		cardEt.addTextChangedListener(new MyEditChangedListener(idcard));
		contactEt.addTextChangedListener(new MyEditChangedListener(contact));
		addressEt.addTextChangedListener(new MyEditChangedListener(address));
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
			paramMap.put("name", nameEt.getText().toString());
			paramMap.put("idcardnumber", cardEt.getText().toString());
			paramMap.put("telephone", contactEt.getText().toString());
			paramMap.put("address", addressEt.getText().toString());
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
		String idcard = cardEt.getText().toString();
		if (TextUtils.isEmpty(idcard)) {
			return "身份证号为空";
		}

		String phone = contactEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			return "联系方式为空";
		}
		String address = addressEt.getText().toString();
		if (TextUtils.isEmpty(address)) {
			return "常用地址为空";
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
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(enroll)) {
			Intent intent = new Intent(this, EnrollSuccessActivity.class);
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
