package com.sft.blackcatapp;

import java.util.HashMap;
import java.util.Map;

import com.sft.common.Config;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MyAppointmentVO;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

/**
 * 评论
 * 
 * @author Administrator
 * 
 */
public class CommentActivity extends BaseActivity implements OnRatingBarChangeListener {

	private static final String comment = "comment";
	//
	private RatingBar totalRatingBar, timeRatingBar, attitudeRatingBar, capacityRatingBar;
	private Button commitBtn;
	private EditText commentEt;
	private ImageView headpicIm;
	private TextView coachNameTv, schoolTv;

	private MyAppointmentVO appointmentVO;

	// 当前评分
	private int totalRate = 0;
	private int timeRate = 0;
	private int attitudeRate = 0;
	private int capacityRate = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_comment);
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
		setTitleText(R.string.comment);

		totalRatingBar = (RatingBar) findViewById(R.id.comment_total_ratingBar);
		timeRatingBar = (RatingBar) findViewById(R.id.comment_time_ratingBar);
		attitudeRatingBar = (RatingBar) findViewById(R.id.comment_attitude_ratingBar);
		capacityRatingBar = (RatingBar) findViewById(R.id.comment_capacity_ratingBar);
		commentEt = (EditText) findViewById(R.id.comment_et);
		commitBtn = (Button) findViewById(R.id.comment_btn);
		headpicIm = (ImageView) findViewById(R.id.comment_headpic_im);
		coachNameTv = (TextView) findViewById(R.id.comment_coachname_tv);
		schoolTv = (TextView) findViewById(R.id.comment_coachschool_tv);

		commentEt.setHint(setHint(R.string.write_comment));

		commitBtn.setFocusable(true);
		commitBtn.setFocusableInTouchMode(true);
		commitBtn.requestFocus();
	}

	private void initData() {
		appointmentVO = (MyAppointmentVO) getIntent().getSerializableExtra("appointmentVO");
		LinearLayout.LayoutParams headpicParams = (LinearLayout.LayoutParams) headpicIm.getLayoutParams();

		String url = appointmentVO.getCoachid().getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			headpicIm.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, headpicIm, headpicParams.width, headpicParams.height);
		}
		coachNameTv.setText(appointmentVO.getCoachid().getName());
		schoolTv.setText(appointmentVO.getCoachid().getDriveschoolinfo().getName());
		
		totalRatingBar.setRating(3);
		timeRatingBar.setRating(3);
		attitudeRatingBar.setRating(3);
		capacityRatingBar.setRating(3);
	}

	private void setListener() {
		commitBtn.setOnClickListener(this);
		totalRatingBar.setOnRatingBarChangeListener(this);
		timeRatingBar.setOnRatingBarChangeListener(this);
		attitudeRatingBar.setOnRatingBarChangeListener(this);
		capacityRatingBar.setOnRatingBarChangeListener(this);
	}

	private void comment() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("reservationid", appointmentVO.get_id());
		paramMap.put("starlevel", totalRate + "");
		paramMap.put("attitudelevel", attitudeRate + "");
		paramMap.put("timelevel", timeRate + "");
		paramMap.put("abilitylevel", capacityRate + "");
		paramMap.put("commentcontent", commentEt.getText().toString());

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpPostSend(comment, this, Config.IP + "api/v1/courseinfo/usercomment", paramMap, 10000,
				headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(comment)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("评论成功");
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

	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			Intent intent = new Intent();
			setResult(R.id.base_left_btn, intent);
			finish();
			break;
		case R.id.comment_btn:
			comment();
			break;
		}
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		switch (ratingBar.getId()) {
		case R.id.comment_total_ratingBar:
			totalRate = (int) Math.ceil(rating);
			break;
		case R.id.comment_time_ratingBar:
			timeRate = (int) Math.ceil(rating);
			break;
		case R.id.comment_attitude_ratingBar:
			attitudeRate = (int) Math.ceil(rating);
			break;
		case R.id.comment_capacity_ratingBar:
			capacityRate = (int) Math.ceil(rating);
			break;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(R.id.base_left_btn, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
