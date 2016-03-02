package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;

/**
 * 取消预约
 * 
 * @author Administrator
 * 
 */
public class CancelAppointmentActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private static final String cancelAppointment = "cancelAppointment";
	//
	private RadioGroup radioGroup;
	private EditText cancelEt;
	private Button cancelBtn;

	private String cancelReason = "";
	private TextView cancelIntroTv;
	private boolean isSelectLast = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_cancel_appointment);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.cancel_appointment);
		cancelReason = CommonUtil.getString(this, R.string.cancel_1);
		radioGroup = (RadioGroup) findViewById(R.id.cancel_appiontment_radiogroup);
		cancelBtn = (Button) findViewById(R.id.cancel_appointment_btn);
		cancelEt = (EditText) findViewById(R.id.cancel_appointment_et);
		cancelIntroTv = (TextView) findViewById(R.id.cancel_appointment_intro);
		// cancelEt.setHint(setHint(R.string.cancel_appointment_instro));
	}

	private void setListener() {
		cancelBtn.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(this);
		cancelEt.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				cancelIntroTv.setText("取消预约说明" + s.length() + "/200");
				selectionStart = cancelEt.getSelectionStart();
				selectionEnd = cancelEt.getSelectionEnd();
				if (temp.length() > 200) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					cancelEt.setText(s);
					cancelEt.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
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
		case R.id.cancel_appointment_btn:
			cancelAppointment();
			break;
		}
	}

	private void cancelAppointment() {
		if (TextUtils.isEmpty(cancelReason)) {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure("请选择取消原因");
			return;
		} else {
			if (isSelectLast
					&& TextUtils.isEmpty(cancelEt.getText().toString())) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("请填写取消原因");
				return;
			}
		}
		MyAppointmentVO appointmentVO = (MyAppointmentVO) getIntent()
				.getSerializableExtra("appointment");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("reservationid", appointmentVO.get_id());
		paramMap.put("cancelreason", cancelReason);
		String cancelContent = cancelEt.getText().toString();
		paramMap.put("cancelcontent", TextUtils.isEmpty(cancelContent) ? ""
				: cancelContent);

		util.print("reservationid=" + appointmentVO.get_id() + " userid="
				+ app.userVO.getUserid());
		util.print("cancelReason=" + cancelReason + " content=" + cancelContent);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(cancelAppointment, this, Config.IP
				+ "api/v1/courseinfo/cancelreservation", paramMap, 10000,
				headerMap);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.cancel_appointment_first_btn:
			break;
		case R.id.cancel_appointment_second_btn:
			break;
		case R.id.cancel_appointment_three_btn:
			break;
		case R.id.cancel_appointment_foue_btn:
			isSelectLast = true;
			break;
		}
		RadioButton button = (RadioButton) findViewById(checkedId);
		cancelReason = button.getText().toString();
	};

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(cancelAppointment)) {
			if (!TextUtils.isEmpty(msg)) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure(msg, 1000);
				return false;
			}
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("已取消");
				new MyHandler(1000) {
					@Override
					public void run() {
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				};
			}
		}
		return true;
	}
}
