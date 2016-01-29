package com.sft.blackcatapp;

import java.util.Set;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.easemob.chat.EMChatManager;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.SchoolVO;

/**
 * 个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonCenterActivity extends BaseActivity {

	private LinearLayout layout;
	private ImageView headPicIm;
	private TextView phoneTv, idTv;
	private TextView schoolTv, carStyleTv, favouriteTv, coachTv, settingTv,
			enrollDetailTv;
	private TextView schoolValueTv, carStyleValueTv;

	private Button logoutBtn;
	private TextView testingDetailTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_person_center);
		initView();
		initData();
		resizeDrawalbeLeftSize();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		initData();
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.person_center);

		layout = (LinearLayout) findViewById(R.id.person_center_layout);
		headPicIm = (ImageView) findViewById(R.id.person_center_headpic_im);
		phoneTv = (TextView) findViewById(R.id.person_center_phone_tv);
		idTv = (TextView) findViewById(R.id.person_center_id_tv);

		schoolTv = (TextView) findViewById(R.id.person_center_school_tv);
		carStyleTv = (TextView) findViewById(R.id.person_center_carstyle_tv);
		favouriteTv = (TextView) findViewById(R.id.person_center_favourite_tv);
		coachTv = (TextView) findViewById(R.id.person_center_coach_tv);
		settingTv = (TextView) findViewById(R.id.person_center_setting_tv);
		enrollDetailTv = (TextView) findViewById(R.id.person_center_enroll_detail_tv);
		schoolValueTv = (TextView) findViewById(R.id.person_center_school_value_tv);
		carStyleValueTv = (TextView) findViewById(R.id.person_center_carstyle_value_tv);
		testingDetailTV = (TextView) findViewById(R.id.person_center_testing_detail_tv);

		logoutBtn = (Button) findViewById(R.id.person_center_logout_btn);

		// if (app.userVO.getApplystate().equals(
		// EnrollResult.SUBJECT_NONE.getValue())) {
		// // 用户没有报名，但可能填写过一些信息
		// SchoolVO school = Util.getEnrollUserSelectedSchool(this);
		// if (school != null) {
		// schoolValueTv.setText(school.getName());
		// }
		//
		// CarModelVO carModel = Util.getEnrollUserSelectedCarStyle(this);
		// if (carModel != null) {
		// carStyleValueTv.setText(carModel.getName());
		// }
		// } else {
		// schoolValueTv.setText(app.userVO.getApplyschoolinfo().getName());
		// carStyleValueTv.setText(app.userVO.getCarmodel().getName());
		// }
	}

	private void initData() {
		phoneTv.setText(app.userVO.getDisplaymobile());
		idTv.setText(app.userVO.getDisplayuserid());

		LinearLayout.LayoutParams headpicParam = (LayoutParams) headPicIm
				.getLayoutParams();

		String url = app.userVO.getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headPicIm.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headPicIm,
					headpicParam.width, headpicParam.height);
		}

		schoolValueTv.setText(app.userVO.getApplyschoolinfo().getName());
		carStyleValueTv.setText(app.userVO.getCarmodel().getName());

		// 使用欢迎页面 请求到的数据
		// String enrollInfo = SharedPreferencesUtil.getString(this,
		// Config.USER_ENROLL_INFO, null);
		// if (!TextUtils.isEmpty(enrollInfo)) {
		// try {
		// SuccessVO successVO = JSONUtil.toJavaBean(SuccessVO.class,
		// enrollInfo);
		// schoolValueTv.setText(successVO.applyschoolinfo.name);
		// carStyleValueTv.setText(successVO.carmodel.code
		// + successVO.carmodel.name);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		// 显示 welcome 更新的数据
		schoolValueTv.setText(app.userVO.getApplyschoolinfo().getName());
		carStyleValueTv.setText(app.userVO.getCarmodel().getCode()
				+ app.userVO.getCarmodel().getName());

	}

	private void resizeDrawalbeLeftSize() {
		// 显示箭头
		Resources r = getResources();
		int size = (int) (18 * screenDensity);
		Drawable arrow = r.getDrawable(R.drawable.person_center_arrow);
		arrow.setBounds(0, 0, size, size);

		if (app.userVO.getApplystate().equals(Config.EnrollResult.SUBJECT_NONE)) {
			schoolValueTv.setCompoundDrawables(null, null, arrow, null);// 设置左图标
			carStyleValueTv.setCompoundDrawables(null, null, arrow, null);// 设置左图标
		} else {
			schoolValueTv.setCompoundDrawables(null, null, null, null);// 设置左图标
			carStyleValueTv.setCompoundDrawables(null, null, null, null);// 设置左图标
		}

		Drawable school = r.getDrawable(R.drawable.person_center_school);
		school.setBounds(0, 0, size, size);
		schoolTv.setCompoundDrawables(school, null, null, null);// 设置左图标

		Drawable carstyle = r.getDrawable(R.drawable.person_center_carstyle);
		carstyle.setBounds(0, 0, size, size);
		carStyleTv.setCompoundDrawables(carstyle, null, null, null);// 设置左图标

		Drawable favourite = r.getDrawable(R.drawable.person_center_favourite);
		favourite.setBounds(0, 0, size, size);
		favouriteTv.setCompoundDrawables(favourite, null, arrow, null);// 设置左图标

		Drawable coach = r.getDrawable(R.drawable.person_center_coach);
		coach.setBounds(0, 0, size, size);
		coachTv.setCompoundDrawables(coach, null, arrow, null);// 设置左图标

		Drawable setting = r.getDrawable(R.drawable.person_center_setting);
		setting.setBounds(0, 0, size, size);
		settingTv.setCompoundDrawables(setting, null, arrow, null);// 设置左图标
		//
		// Drawable enroll = r.getDrawable(R.drawable.person_center_setting);
		// setting.setBounds(0, 0, size, size);
		// enrollDetailTv.setCompoundDrawables(enroll, null, arrow, null);//
		// 设置左图标
	}

	private void setListener() {
		layout.setOnClickListener(this);
		schoolTv.setOnClickListener(this);
		carStyleTv.setOnClickListener(this);
		favouriteTv.setOnClickListener(this);
		coachTv.setOnClickListener(this);
		settingTv.setOnClickListener(this);
		enrollDetailTv.setOnClickListener(this);
		testingDetailTV.setOnClickListener(this);

		logoutBtn.setOnClickListener(this);
		if (app.userVO.getApplystate().equals(
				EnrollResult.SUBJECT_NONE.getValue())) {
			schoolValueTv.setOnClickListener(this);
			carStyleValueTv.setOnClickListener(this);
		}
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
		case R.id.person_center_layout:
			intent = new Intent(this, EditPersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.person_center_carstyle_tv:
			break;
		case R.id.person_center_coach_tv:
			intent = new Intent(this, MyCoachActivity.class);
			startActivity(intent);
			break;
		// case R.id.person_center_school_value_tv:
		// intent = new Intent(this, EnrollSchoolActivity.class);
		// SchoolVO school = Util.getEnrollUserSelectedSchool(this);
		// if (school != null) {
		// intent.putExtra("school", school);
		// }
		// startActivityForResult(intent, R.id.person_center_school_value_tv);
		// break;
		// case R.id.person_center_carstyle_value_tv:
		// intent = new Intent(this, EnrollCarStyleActivity.class);
		// CarModelVO carModel = Util.getEnrollUserSelectedCarStyle(this);
		// if (carModel != null) {
		// intent.putExtra("carStyle", carModel);
		// }
		// startActivityForResult(intent, R.id.person_center_carstyle_value_tv);
		// break;
		case R.id.person_center_favourite_tv:
			intent = new Intent(this, MyFavouriteActiviy.class);
			startActivityForResult(intent, R.id.person_center_favourite_tv);
			break;
		case R.id.person_center_logout_btn:
			ZProgressHUD.getInstance(this).setMessage("正在退出登录...");
			ZProgressHUD.getInstance(this).show();
			EMChatManager.getInstance().logout(null);
			setTag();
			break;
		case R.id.person_center_school_tv:
			break;
		case R.id.person_center_setting_tv:
			intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		// 报名详情
		case R.id.person_center_enroll_detail_tv:
			String applystate = app.userVO.getApplystate();
			if (EnrollResult.SUBJECT_NONE.getValue().equals(applystate)) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("您还没有报名");

				new MyHandler(1000) {
					@Override
					public void run() {
						Intent intent = new Intent(PersonCenterActivity.this,
								ApplyActivity.class);
						startActivity(intent);
						// finish();
					}
				};
			} else if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
					applystate)) {
				Intent intent1 = new Intent(PersonCenterActivity.this,
						EnrollSuccessActivity.class);
				startActivity(intent1);
			} else if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
					applystate)) {
				// Intent intent1 = new Intent(PersonCenterActivity.this,
				// EnrollSuccessActivity.class);
				// startActivity(intent1);
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名");
			}
			break;
		// 验证报名信息
		case R.id.person_center_testing_detail_tv:
			String applystates = app.userVO.getApplystate();
			if (EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
					applystates)) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("您已报名");
			} else {
				Intent intent1 = new Intent(PersonCenterActivity.this,
						TestingApplyActivity.class);
				startActivity(intent1);
			}

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (data != null) {
			if (requestCode == R.id.person_center_carstyle_value_tv) {
				// 更新
				CarModelVO carStyle = (CarModelVO) data
						.getSerializableExtra("carStyle");
				carStyleValueTv.setText(carStyle.getName());
				return;
			}
			if (requestCode == R.id.person_center_school_value_tv
					&& resultCode == R.id.base_right_tv) {
				// 更新选择的驾校
				SchoolVO school = (SchoolVO) data
						.getSerializableExtra("school");
				schoolValueTv.setText(school.getName());
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(RESULT_OK, data);
					finish();
				}
			};
		}
	}

	private void setTag() {
		if (app.isLogin) {
			JPushInterface.setAlias(this, "", new MyTagAliasCallback());
		}
	}

	private int sum = 0;

	private class MyTagAliasCallback implements TagAliasCallback {

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			sum++;
			if (arg0 != 0 && sum < 5) {
				setTag();
			} else {
				ZProgressHUD.getInstance(PersonCenterActivity.this).dismiss();
				util.saveParam(Config.LAST_LOGIN_PASSWORD, "");
				Intent intent = new Intent(PersonCenterActivity.this,
						LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}

	}
}
