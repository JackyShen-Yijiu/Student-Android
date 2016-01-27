package com.sft.blackcatapp;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sft.common.BlackCatApplication;
import com.sft.dialog.AsyncProgressDialog;
import com.sft.dialog.BaseDialogWrapper;
import com.sft.util.PhotoUtil;

public class NewApplystateActivity extends BaseActivity implements
		OnClickListener {
	private RadioButton rb_apply_coach;
	private RadioButton rb_apply_school;
	private RadioGroup rgroup_apply;
	private BaseDialogWrapper mAvatarDialog;
	private File mPhotoFile;
	private ImageView iv_applystate;
	private ImageView iv_applystates;
	private AsyncProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.new_activity_applystate);
		initView();
		Listener();
	}

	private void initView() {
		setTitleText(R.string.apply_left);

		rb_apply_coach = (RadioButton) findViewById(R.id.rb_apply_coach);
		rb_apply_school = (RadioButton) findViewById(R.id.rb_apply_school);
		rgroup_apply = (RadioGroup) findViewById(R.id.rgroup_apply);
		iv_applystate = (ImageView) findViewById(R.id.iv_applystate);
		iv_applystates = (ImageView) findViewById(R.id.iv_applystates);

	}

	private void Listener() {
		iv_applystate.setOnClickListener(this);
		iv_applystates.setOnClickListener(this);
		rgroup_apply.setOnClickListener(this);
		rb_apply_coach.setOnClickListener(this);
		rb_apply_school.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 投诉教练
		case R.id.rb_apply_coach:

			break;
		// 投诉驾校
		case R.id.rb_apply_school:

			break;
		// 添加投诉图片
		case R.id.iv_applystate:
			if (mAvatarDialog == null) {
				mAvatarDialog = new BaseDialogWrapper(
						NewApplystateActivity.this, getPhotoDialogView());
				mAvatarDialog.setFullWidth(true);
			}
			mAvatarDialog.setGravity(Gravity.BOTTOM);
			mAvatarDialog.showDialog();
			break;
		case R.id.iv_applystates:
			if (mAvatarDialog == null) {
				mAvatarDialog = new BaseDialogWrapper(
						NewApplystateActivity.this, getPhotoDialogView());
				mAvatarDialog.setFullWidth(true);
			}
			mAvatarDialog.setGravity(Gravity.BOTTOM);
			mAvatarDialog.showDialog();
			break;
		// 拍照
		case R.id.tv_take_photo:
			mAvatarDialog.dismissDialog();
			mPhotoFile = PhotoUtil.getPhotoFile(BlackCatApplication
					.getInstance());
			startActivityForResult(PhotoUtil.getTakePickIntent(mPhotoFile),
					PhotoUtil.PICTRUE_FROM_CAMERA);
			break;
		// 拍照取消
		case R.id.tv_choose_cancel:
			mAvatarDialog.dismissDialog();
			break;
		// 返回
		case R.id.base_left_btn:
			finish();
			break;
		}
	}

	private View getPhotoDialogView() {
		View dialogView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_photo_picker, null);
		dialogView.findViewById(R.id.tv_choose_cancel).setOnClickListener(this);
		dialogView.findViewById(R.id.tv_take_photo).setOnClickListener(this);
		return dialogView;
	}

	private Uri mPhotoUri;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PhotoUtil.PICTRUE_FROM_CAMERA:
			// 拍好了照片 应该跳转
			// TODO 判断照片有没有拍照
			if (mPhotoFile != null && mPhotoFile.exists()) {
				mPhotoUri = Uri.fromFile(mPhotoFile);
				loadPhoto(mPhotoUri);
			}
			break;
		case PhotoUtil.PICTRUE_FROM_GALLERY:
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					mPhotoUri = uri;
					loadPhoto(mPhotoUri);
				}
			}
			break;
		default:
			break;
		}
	}

	private void loadPhoto(Uri uri) {
		if (uri == null) {
			return;
		}
		mDialog = new AsyncProgressDialog(this);
		mDialog.show();

	}
}
