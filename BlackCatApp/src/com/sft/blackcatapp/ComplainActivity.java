package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.dialog.CustomDialog;
import com.sft.vo.MyAppointmentVO;

/**
 * 投诉
 * 
 * @author Administrator
 * 
 */
public class ComplainActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private static final String complain = "complain";
	private RadioGroup radioGroup;
	private EditText cancelEt;
	private Button cancelBtn;

	private String complainReason = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_complain);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.complain);

		radioGroup = (RadioGroup) findViewById(R.id.complain_radiogroup);
		cancelBtn = (Button) findViewById(R.id.complain_btn);
		cancelEt = (EditText) findViewById(R.id.complain_et);

		cancelEt.setHint(setHint(R.string.opinions_suggestions));
	}

	private void setListener() {
		cancelBtn.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(this);
	}

	private void complain() {
		MyAppointmentVO appointmentVO = (MyAppointmentVO) getIntent()
				.getSerializableExtra("appointmentVO");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("reservationid", appointmentVO.get_id());
		paramMap.put("reason", complainReason);
		paramMap.put("complaintcontent", cancelEt.getText().toString());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils
				.httpPostSend(complain, this, Config.IP
						+ "api/v1/courseinfo/usercomplaint", paramMap, 10000,
						headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(complain)) {
			if (dataString != null) {
				CustomDialog dialog = new CustomDialog(this,
						CustomDialog.APPOINTMENT_COMPLAIN);
				dialog.show();
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						new MyHandler(100) {
							@Override
							public void run() {
								finish();
							}
						};
					}
				});
			}
		}
		return true;
	}

	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.complain_btn:
			complain();
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton btn = null;
		switch (checkedId) {
		case R.id.complain_first_btn:
			btn = (RadioButton) group.getChildAt(0);
			break;
		case R.id.complain_second_btn:
			btn = (RadioButton) group.getChildAt(1);
			break;
		case R.id.complain_three_btn:
			btn = (RadioButton) group.getChildAt(2);
			break;
		case R.id.complain_four_btn:
			btn = (RadioButton) group.getChildAt(3);
			break;
		}
		if (btn != null) {
			complainReason = btn.getText().toString();
		}
	};

}
