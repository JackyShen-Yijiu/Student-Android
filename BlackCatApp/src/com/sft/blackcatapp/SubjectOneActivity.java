package com.sft.blackcatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jzjf.app.R;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.BaseUtils;
import com.sft.viewutil.ZProgressHUD;

/**
 * 科目一
 * 
 * @author Administrator
 * 
 */
public class SubjectOneActivity extends BaseActivity {

	//
	private LinearLayout twoLayout;
	//
	private LinearLayout threeLayout;
	//
	private RelativeLayout libraryBtn;
	private RelativeLayout examBtn, myFaultBtn, walletBtn, messageBtn, myBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.main_view_two);
		initView();
		// resizeLayout();
		// setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleBarVisible(View.GONE);
		showTitlebarBtn(0);
		setTitleText(R.string.school);

		// twoLayout = (LinearLayout) findViewById(R.id.main_two_layout);
		// threeLayout = (LinearLayout) findViewById(R.id.main_three_layout);
		//
		// libraryBtn = (RelativeLayout)
		// findViewById(R.id.main_appointment_layout);
		// examBtn = (RelativeLayout)
		// findViewById(R.id.main_appointment_car_layout);
		// myFaultBtn = (RelativeLayout)
		// findViewById(R.id.main_appointment_course_layout);
		// walletBtn = (RelativeLayout) findViewById(R.id.main_wallet_layout);
		// messageBtn = (RelativeLayout) findViewById(R.id.main_message_layout);
		// myBtn = (RelativeLayout) findViewById(R.id.main_my_layout);
		//
		// ImageView imageAppointment = (ImageView)
		// findViewById(R.id.main_appointment_im);
		// imageAppointment.setBackgroundResource(R.drawable.question_bank);
		// RelativeLayout.LayoutParams paramsAppointment =
		// (RelativeLayout.LayoutParams) imageAppointment
		// .getLayoutParams();
		// paramsAppointment.width = (int) (screenDensity * 118);
		// paramsAppointment.height = (int) (screenDensity * 131);
		//
		// ImageView imageAppointmentCar = (ImageView)
		// findViewById(R.id.main_appointment_car_im);
		// imageAppointmentCar.setBackgroundResource(R.drawable.mock_examination);
		// RelativeLayout.LayoutParams paramsAppointmentCar =
		// (RelativeLayout.LayoutParams) imageAppointmentCar
		// .getLayoutParams();
		// paramsAppointmentCar.width = (int) (screenDensity * 53);
		// paramsAppointmentCar.height = (int) (screenDensity * 90);
		//
		// ImageView imageCourse = (ImageView)
		// findViewById(R.id.main_appointment_course_im);
		// imageCourse.setBackgroundResource(R.drawable.my_mistakes);
		// RelativeLayout.LayoutParams paramsCourse =
		// (RelativeLayout.LayoutParams) imageCourse.getLayoutParams();
		// paramsCourse.width = (int) (screenDensity * 64);
		// paramsCourse.height = (int) (screenDensity * 64);
		//
		// ((TextView) findViewById(R.id.main_appointment_tv)).setText("科一" +
		// getString(R.string.question_bank));
		// ((TextView) findViewById(R.id.main_appointment_car_tv)).setText("科一"
		// + getString(R.string.mock_examination));
		// ((TextView)
		// findViewById(R.id.main_appointment_course_tv)).setText(R.string.my_mistakes);
		//
		// findViewById(R.id.main_appointment_layout).setBackgroundColor(Color.parseColor("#bd1f4a"));
		// findViewById(R.id.main_appointment_car_layout).setBackgroundColor(Color.parseColor("#ff6633"));
		// findViewById(R.id.main_appointment_course_layout).setBackgroundColor(Color.parseColor("#ff9900"));
		// findViewById(R.id.main_wallet_layout).setBackgroundColor(Color.parseColor("#01a300"));
		// findViewById(R.id.main_message_layout).setBackgroundColor(Color.parseColor("#0094a6"));
		// findViewById(R.id.main_my_layout).setBackgroundColor(Color.parseColor("#2d8aef"));

	}

	private void resizeLayout() {
		LinearLayout.LayoutParams threeLayoutParams = (LinearLayout.LayoutParams) threeLayout
				.getLayoutParams();
		threeLayoutParams.height = (int) ((screenWidth - 12 * screenDensity) / 3);
		LinearLayout.LayoutParams twoLayoutParams = (LinearLayout.LayoutParams) twoLayout
				.getLayoutParams();
		twoLayoutParams.height = (int) ((screenWidth - 11 * screenDensity) * 2 / 3);
	}

	private void setListener() {
		libraryBtn.setOnClickListener(this);
		examBtn.setOnClickListener(this);
		myFaultBtn.setOnClickListener(this);
		walletBtn.setOnClickListener(this);
		messageBtn.setOnClickListener(this);
		myBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.main_appointment_layout:
			// 题库
			if (app.questionVO != null) {
				intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("url", app.questionVO.getSubjectone()
						.getQuestionlisturl());
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.main_appointment_course_layout:
			// 我的错题
			if (app.isLogin) {
				if (app.questionVO != null) {
					intent = new Intent(this, QuestionActivity.class);
					intent.putExtra("url", app.questionVO.getSubjectone()
							.getQuestionerrorurl());
				} else {
					BaseUtils.toLogin(SubjectOneActivity.this);
					// ZProgressHUD.getInstance(this).show();
					// ZProgressHUD.getInstance(this).dismissWithFailure("暂无题库");
				}
			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case R.id.main_appointment_car_layout:
			// 模拟考试
			if (app.questionVO != null) {
				intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("url", app.questionVO.getSubjectone()
						.getQuestiontesturl());
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("暂无题库");
			}
			break;
		case R.id.main_wallet_layout:
			if (app.isLogin) {
				intent = new Intent(this, MyWalletActivity.class);
			} else {
				BaseUtils.toLogin(SubjectOneActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		case R.id.main_message_layout:
			if (app.isLogin) {
				intent = new Intent(this, MessageActivity.class);
			} else {
				BaseUtils.toLogin(SubjectOneActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		case R.id.main_my_layout:
			if (app.isLogin) {
				intent = new Intent(this, PersonCenterActivity.class);
			} else {
				BaseUtils.toLogin(SubjectOneActivity.this);
				// NoLoginDialog dialog = new NoLoginDialog(this);
				// dialog.show();
			}
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
}
