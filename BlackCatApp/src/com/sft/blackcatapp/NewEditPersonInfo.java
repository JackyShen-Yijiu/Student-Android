package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.common.Config;

/**
 * 编辑个人信息
 * 
 * @author Administrator
 * 
 */
public class NewEditPersonInfo extends BaseActivity implements
		OnCheckedChangeListener {

	private LinearLayout layout;

	private static final String changeAddress = "address";

	private LinearLayout.LayoutParams headParam;

	private SelectableRoundedImageView headPic;

	private EditText addressTv;

	private EditText nameTv;

	private EditText nickNameTv;

	private TextView phoneTv;

	private RadioButton nan;

	private RadioButton nv;

	private String type;

	private RadioGroup sexRg;
	private String selectSex = "男";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.new_activity_edit_person);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.eidt_info);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.save);

		layout = (LinearLayout) findViewById(R.id.edit_person_info_layout);

		addressTv = (EditText) findViewById(R.id.editText_address);
		phoneTv = (TextView) findViewById(R.id.textView_phone);
		nameTv = (EditText) findViewById(R.id.editText_name);
		nickNameTv = (EditText) findViewById(R.id.editText_nickName);

		nan = (RadioButton) findViewById(R.id.complaint_feedbackusertype_anonymous);
		nv = (RadioButton) findViewById(R.id.complaint_feedbackusertype_realname);
		sexRg = (RadioGroup) findViewById(R.id.complaint_feedbackusertype);

		// 圆形头像
		headPic = (SelectableRoundedImageView) findViewById(R.id.edit_person_info_headpic_im);
		headPic.setScaleType(ScaleType.CENTER_CROP);
		headPic.setImageResource(R.drawable.login_head);
		headPic.setOval(true);

		// 显示箭头
		Resources r = getResources();
		int size = (int) (18 * screenDensity);
		Drawable arrow = r.getDrawable(R.drawable.person_center_arrow);
		arrow.setBounds(0, 0, size, size);
		headParam = (LayoutParams) headPic.getLayoutParams();

	}

	private void initData() {
		String url = app.userVO.getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headPic.setBackgroundResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPic, headParam.width,
					headParam.height);
		}
		addressTv.setText(app.userVO.getAddress());
		phoneTv.setText(app.userVO.getDisplaymobile());
		nameTv.setText(app.userVO.getName());
		nickNameTv.setText(app.userVO.getNickname());

	}

	private void setListener() {
		phoneTv.setOnClickListener(this);
		sexRg.setOnCheckedChangeListener(this);
		layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			changeAddress();
			break;
		case R.id.edit_person_info_layout:
			intent = new Intent(this, CropImageActivity.class);
			break;
		case R.id.textView_phone:
			intent = new Intent(this, ChangePhoneActivity.class);
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void changeAddress() {
		String address = addressTv.getText().toString();
		String name = nameTv.getText().toString();
		String nickname = nickNameTv.getText().toString();
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("address", address);
		paramMap.put("name", name);
		paramMap.put("nickname", nickname);
		paramMap.put("gender", selectSex);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(changeAddress, this, Config.IP
				+ "api/v1/userinfo/updateuserinfo", paramMap, 10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(changeAddress)) {
			if (dataString != null) {
				app.userVO.setAddress(addressTv.getText().toString());
				app.userVO.setName(nameTv.getText().toString());
				app.userVO.setNickname(nickNameTv.getText().toString());
				app.userVO.setGender(selectSex);
				initData();
				finish();
			}
		}
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup Group, int Checkid) {

		if (Checkid == R.id.complaint_feedbackusertype_anonymous) {
			selectSex = "男";
		} else if (Checkid == R.id.complaint_feedbackusertype_realname) {
			selectSex = "女";
		}
	}
}
