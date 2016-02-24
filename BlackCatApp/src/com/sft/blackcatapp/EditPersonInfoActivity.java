package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.sft.common.Config;

/**
 * 编辑个人信息
 * 
 * @author Administrator
 * 
 */
public class EditPersonInfoActivity extends BaseActivity {

	private LinearLayout layout;

	private TextView genderTv, signTv, addressTv, phoneTv, nameTv, nickNameTv;

	private RelativeLayout signLayout;

	private static final String chageGender = "changeGender";

	private int selectGenderIndex = 0;
	private CharSequence[] array;

	private LinearLayout.LayoutParams headParam;

	private SelectableRoundedImageView headPic;

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
		array = getResources().getTextArray(R.array.gender);

		layout = (LinearLayout) findViewById(R.id.edit_person_info_layout);
		genderTv = (TextView) findViewById(R.id.edit_person_info_gender_tv);
		signTv = (TextView) findViewById(R.id.edit_person_info_sign_tv);
		signLayout = (RelativeLayout) findViewById(R.id.edit_person_info_sign_tv_layout);
		addressTv = (TextView) findViewById(R.id.edit_person_info_address_tv);
		phoneTv = (TextView) findViewById(R.id.edit_person_info_phone_tv);
		nameTv = (TextView) findViewById(R.id.edit_person_info_name_tv);
		nickNameTv = (TextView) findViewById(R.id.edit_person_info_nickname_tv);

		// 圆形头像
		headPic = (SelectableRoundedImageView) findViewById(R.id.edit_person_info_headpic_im);
		headPic.setScaleType(ScaleType.CENTER_CROP);
		headPic.setImageResource(R.drawable.default_small_pic);
		headPic.setOval(true);

		// 显示箭头
		Resources r = getResources();
		int size = (int) (18 * screenDensity);
		Drawable arrow = r.getDrawable(R.drawable.person_center_arrow);
		arrow.setBounds(0, 0, size, size);
		genderTv.setCompoundDrawables(null, null, arrow, null);
		signTv.setCompoundDrawables(null, null, arrow, null);
		addressTv.setCompoundDrawables(null, null, arrow, null);
		phoneTv.setCompoundDrawables(null, null, arrow, null);
		headParam = (LayoutParams) headPic.getLayoutParams();

	}

	private void initData() {
		String url = app.userVO.getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headPic.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPic, headParam.width,
					headParam.height);
		}
		genderTv.setText(app.userVO.getGender());
		signTv.setText(app.userVO.getSignature());
		addressTv.setText(app.userVO.getAddress());
		phoneTv.setText(app.userVO.getDisplaymobile());
		nameTv.setText(app.userVO.getName());
		nickNameTv.setText(app.userVO.getNickname());
	}

	private void setListener() {
		layout.setOnClickListener(this);
		genderTv.setOnClickListener(this);
		signLayout.setOnClickListener(this);
		addressTv.setOnClickListener(this);
		phoneTv.setOnClickListener(this);
		nameTv.setOnClickListener(this);
		nickNameTv.setOnClickListener(this);
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
		case R.id.edit_person_info_layout:
			intent = new Intent(this, CropImageActivity.class);
			break;
		case R.id.edit_person_info_gender_tv:
			String gender = app.userVO.getGender();
			int index = -1;
			if (!TextUtils.isEmpty(gender)) {
				if (gender.equals("男")) {
					index = 0;
				} else {
					index = 1;
				}
			}
			showGender(index);
			break;
		case R.id.edit_person_info_address_tv:
			intent = new Intent(this, ChangeAddressActivity.class);
			break;
		case R.id.edit_person_info_phone_tv:
			intent = new Intent(this, ChangePhoneActivity.class);
			break;
		case R.id.edit_person_info_sign_tv_layout:
			intent = new Intent(this, ChangeSignActivity.class);
			intent.putExtra("type", "sign");
			break;
		case R.id.edit_person_info_name_tv:
			intent = new Intent(this, ChangeSignActivity.class);
			intent.putExtra("type", "name");
			break;
		case R.id.edit_person_info_nickname_tv:
			intent = new Intent(this, ChangeSignActivity.class);
			intent.putExtra("type", "nickname");
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	private void showGender(int index) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择性别");
		builder.setSingleChoiceItems(array, index,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectGenderIndex = which;
						changeGender();
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	private void changeGender() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("gender", array[selectGenderIndex].toString());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(chageGender, this, Config.IP
				+ "api/v1/userinfo/updateuserinfo", paramMap, 10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(chageGender)) {
			if (dataString != null) {
				app.userVO.setGender(array[selectGenderIndex].toString());
				initData();
			}
		}
		return true;
	}
}
