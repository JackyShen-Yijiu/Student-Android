package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sft.common.Config;
import com.sft.dialog.CustomDialog;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.EnrollVertifyVO;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.sqlhelper.DBHelper;

/**
 * 报名验证
 * 
 * @author Administrator
 * 
 */
public class EnrollVerifyActivity extends BaseActivity {

	private static final String vertify = "vertify";
	// 学号
	private EditText studentNumberEt;
	// 准考证号
	private EditText examNumberEt;
	// 真实姓名
	private EditText nameEt;
	// 身份证号
	private EditText idCardEt;
	// 联系方式
	private EditText contactEt;
	// 常用地址
	private EditText addressEt;
	// 所在驾校
	private EditText schoolEt;
	// 提交
	private Button commitBtn;
	//
	private EnrollVertifyVO vertifyVO = new EnrollVertifyVO();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_verify);
		initView();
		initData();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.enroll_vertify);

		commitBtn = (Button) findViewById(R.id.vertify_commit_btn);
		studentNumberEt = (EditText) findViewById(R.id.vertify_student_et);
		examNumberEt = (EditText) findViewById(R.id.vertify_exam_et);
		nameEt = (EditText) findViewById(R.id.vertify_name_et);
		contactEt = (EditText) findViewById(R.id.vertify_contact_et);
		idCardEt = (EditText) findViewById(R.id.vertify_idcard_et);
		addressEt = (EditText) findViewById(R.id.vertify_address_et);
		schoolEt = (EditText) findViewById(R.id.vertify_school_et);

		studentNumberEt.setHint(setHint(R.string.student_number));
		examNumberEt.setHint(setHint(R.string.exam_number));
		nameEt.setHint(setHint(R.string.real_name));
		contactEt.setHint(setHint(R.string.contact));
		idCardEt.setHint(setHint(R.string.idcard));
		addressEt.setHint(setHint(R.string.address));
		schoolEt.setHint(setHint(R.string.belongto_school));
	}

	private void initData() {
		if (!TextUtils.isEmpty(app.userVO.getName()))
			nameEt.setText(app.userVO.getName());
		if (!TextUtils.isEmpty(app.userVO.getApplyschoolinfo().getName()))
			schoolEt.setText(app.userVO.getApplyschoolinfo().getName());
		if (!TextUtils.isEmpty(app.userVO.getAddress()))
			addressEt.setText(app.userVO.getAddress());
		if (!TextUtils.isEmpty(app.userVO.getMobile()))
			contactEt.setText(app.userVO.getMobile());

		List<EnrollVertifyVO> vertifyList = DBHelper.getInstance(this).query(EnrollVertifyVO.class, "userid",
				app.userVO.getUserid());
		if (vertifyList != null && vertifyList.size() > 0) {
			vertifyVO = vertifyList.get(0);
			if (!TextUtils.isEmpty(vertifyVO.getName()))
				nameEt.setText(vertifyVO.getName());
			if (!TextUtils.isEmpty(vertifyVO.getSchoolName()))
				schoolEt.setText(vertifyVO.getSchoolName());
			if (!TextUtils.isEmpty(vertifyVO.getContact()))
				contactEt.setText(vertifyVO.getContact());
			if (!TextUtils.isEmpty(vertifyVO.getIdCard()))
				idCardEt.setText(vertifyVO.getIdCard());
			if (!TextUtils.isEmpty(vertifyVO.getAddress()))
				addressEt.setText(vertifyVO.getAddress());
			if (!TextUtils.isEmpty(vertifyVO.getStudentNumber()))
				studentNumberEt.setText(vertifyVO.getStudentNumber());
			if (!TextUtils.isEmpty(vertifyVO.getExamNumber()))
				examNumberEt.setText(vertifyVO.getExamNumber());
		}
	}

	private void setListener() {
		commitBtn.setOnClickListener(this);
		nameEt.addTextChangedListener(new MyEditChangedListener(nameEt.getId()));
		schoolEt.addTextChangedListener(new MyEditChangedListener(schoolEt.getId()));
		contactEt.addTextChangedListener(new MyEditChangedListener(contactEt.getId()));
		idCardEt.addTextChangedListener(new MyEditChangedListener(idCardEt.getId()));
		addressEt.addTextChangedListener(new MyEditChangedListener(addressEt.getId()));
		studentNumberEt.addTextChangedListener(new MyEditChangedListener(studentNumberEt.getId()));
		examNumberEt.addTextChangedListener(new MyEditChangedListener(examNumberEt.getId()));
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.vertify_commit_btn:
			enrollVertify();
			break;
		case R.id.base_left_btn:
			finish();
			break;
		default:
			break;
		}
	}

	private void enrollVertify() {
		DBHelper.getInstance(this).insert(vertifyVO);
		String checkResult = checkVertifyInfo();
		if (checkResult == null) {
			// 报名验证
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("name", nameEt.getText().toString());
			paramsMap.put("idcardnumber", idCardEt.getText().toString());
			paramsMap.put("telephone", contactEt.getText().toString());
			paramsMap.put("address", addressEt.getText().toString());
			paramsMap.put("userid", app.userVO.getUserid());
			paramsMap.put("schoolid", app.userVO.getApplyschoolinfo().getId());
			paramsMap.put("ticketnumber", examNumberEt.getText().toString());
			paramsMap.put("studentid", studentNumberEt.getText().toString());
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(vertify, this, Config.IP + "api/v1/userinfo/enrollverification", paramsMap,
					10000, headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
	}

	private String checkVertifyInfo() {
		String studentNumber = studentNumberEt.getText().toString();
		if (TextUtils.isEmpty(studentNumber)) {
			return "学号为空";
		}
		String examNumber = examNumberEt.getText().toString();
		if (TextUtils.isEmpty(examNumber)) {
			return "准考证号为空";
		}
		String name = nameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			return "姓名为空";
		}
		String idCard = idCardEt.getText().toString();
		if (TextUtils.isEmpty(idCard)) {
			return "身份证号为空";
		}
		String contact = contactEt.getText().toString();
		if (TextUtils.isEmpty(contact)) {
			return "联系方式为空";
		}
		String address = addressEt.getText().toString();
		if (TextUtils.isEmpty(address)) {
			return "常用地址为空";
		}
		String school = schoolEt.getText().toString();
		if (TextUtils.isEmpty(school)) {
			return "报考为空";
		}
		return null;
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(vertify)) {
			if (dataString != null) {
				CustomDialog dialog = new CustomDialog(this, CustomDialog.VERTIFY_ENROLL);
				dialog.show();
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						sendBroadcast(new Intent(OldMainActivity.class.getName()).putExtra("isVertify", true));
						finish();
					}
				});
			}
		}
		return true;
	}

	private class MyEditChangedListener implements TextWatcher {
		private int viewId;

		public MyEditChangedListener(int viewId) {
			this.viewId = viewId;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

			switch (viewId) {
			case R.id.vertify_name_et:
				vertifyVO.setName(s.toString());
				break;
			case R.id.vertify_student_et:
				vertifyVO.setName(s.toString());
				break;
			case R.id.vertify_exam_et:
				vertifyVO.setExamNumber(s.toString());
				break;
			case R.id.vertify_contact_et:
				vertifyVO.setContact(s.toString());
				break;
			case R.id.vertify_idcard_et:
				vertifyVO.setIdCard(s.toString());
				break;
			case R.id.vertify_address_et:
				vertifyVO.setAddress(s.toString());
				break;
			case R.id.vertify_school_et:
				vertifyVO.setSchoolName(s.toString());
				break;
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	@Override
	protected void onPause() {
		DBHelper.getInstance(this).insert(vertifyVO);
		super.onPause();
	}
}
