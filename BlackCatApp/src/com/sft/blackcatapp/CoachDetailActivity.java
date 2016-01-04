package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.sft.adapter.CoachCommentListAdapter;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachCommentVO;
import com.sft.vo.CoachVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.commonvo.Subject;

/**
 * 教练详情界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "ClickableViewAccessibility", "CutPasteId" })
public class CoachDetailActivity extends BaseActivity implements
		OnItemClickListener, IXListViewListener, OnCheckedChangeListener,
		OnSelectConfirmListener {

	private static final String coachDetail = "coachDetail";
	private static final String coachComment = "coachComment";
	private static final String addCoach = "addCoach";
	private static final String deleteCoach = "deleteCoach";
	private ScrollView scrollView;
	// 教练头像
	private ImageView coachHeadPicIm;
	// 收藏按钮
	private CheckBox collectCk;
	// 教练姓名
	private TextView coachNameTv;
	//
	private TextView shuttle, general;
	// 评分控件
	private RatingBar rateBar;
	// 训练场地
	private TextView placeTv;
	// 学校
	private TextView schoolTv;
	// 通过率
	private TextView rateTv;
	// 学员数目
	private TextView studentCountTv;
	// 接送
	private TextView shuttleTv;
	// 工作时间
	private TextView workTimeTv;
	// 科目
	private TextView courseTv;
	// 教龄
	private TextView seniorityTv;
	// 个人评价
	private TextView selfEvaluationTv;
	// 学员评价
	private XListView commentList;
	// 报名按钮
	private Button enrollBtn;
	// 教练信息
	private TextView coachInfo;
	// 学院评价
	private TextView studentEvaluation;
	// 暂无评论文本
	private TextView noCommentTv;
	// 教练对象
	private CoachVO coachVO;
	// 添加删除喜欢的教练
	private CheckBox addDeleteCoachCk;
	// 当前评论的页数
	private int commentPage = 1;
	//
	private List<CoachCommentVO> commentVoList;
	//
	private CoachCommentListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_coach_detail);
		initView();
		setListener();
		obtainEnrollCoachDetail();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		if (app.userVO == null
				|| app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			enrollBtn.setText(R.string.enroll);
		} else {
			if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_ENROLLING.getValue())) {
				enrollBtn.setText(R.string.verifying);
			} else {
				enrollBtn.setText(R.string.appointment);
			}
		}
		super.onResume();
	}

	private void initView() {
		showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN
				| BaseActivity.SHOW_RIGHT_BTN);
		setBtnBkground(R.drawable.base_left_btn_bkground, R.drawable.phone);
		setTitleText(R.string.coach_detail);

		scrollView = (ScrollView) findViewById(R.id.coach_detail_scrollview);
		addDeleteCoachCk = (CheckBox) findViewById(R.id.coach_detail_collection_ck);
		coachHeadPicIm = (ImageView) findViewById(R.id.coach_detail_headpic_im);
		collectCk = (CheckBox) findViewById(R.id.coach_detail_collection_ck);
		coachNameTv = (TextView) findViewById(R.id.coach_detail_coachname_tv);
		shuttle = (TextView) findViewById(R.id.coach_detail_shuttle);
		general = (TextView) findViewById(R.id.coach_detail_general);
		rateBar = (RatingBar) findViewById(R.id.coach_detail_ratingBar);
		placeTv = (TextView) findViewById(R.id.coach_detail_place_tv);
		seniorityTv = (TextView) findViewById(R.id.coach_detail_seniority_tv);

		schoolTv = (TextView) findViewById(R.id.coach_detail_school_tv);
		studentCountTv = (TextView) findViewById(R.id.coach_detail_student_count);
		rateTv = (TextView) findViewById(R.id.coach_detail_rate_tv);
		shuttleTv = (TextView) findViewById(R.id.coach_detail_shuttle_tv);
		workTimeTv = (TextView) findViewById(R.id.coach_detail_weektime_tv);
		courseTv = (TextView) findViewById(R.id.coach_detail_course_tv);

		selfEvaluationTv = (TextView) findViewById(R.id.coach_detail_evaluation_tv);
		coachInfo = (TextView) findViewById(R.id.coach_detail_coachinfo_tv);
		studentEvaluation = (TextView) findViewById(R.id.coach_detail_studentevaluation_tv);
		noCommentTv = (TextView) findViewById(R.id.coach_detail_noevaluation_tv);
		commentList = (XListView) findViewById(R.id.coach_detail_listview);
		commentList.setVisibility(View.GONE);
		noCommentTv.setVisibility(View.VISIBLE);

		enrollBtn = (Button) findViewById(R.id.coach_detail_enroll_btn);

		// 中文字体加粗
		selfEvaluationTv.getPaint().setFakeBoldText(true);
		coachInfo.getPaint().setFakeBoldText(true);
		coachNameTv.getPaint().setFakeBoldText(true);
		studentEvaluation.getPaint().setFakeBoldText(true);

		commentList.setPullRefreshEnable(false);
		commentList.setPullLoadEnable(true);
		commentList.setXListViewListener(this);

		coachVO = (CoachVO) getIntent().getSerializableExtra("coach");

		if (app.userVO == null) {
			collectCk.setEnabled(false);
			enrollBtn.setVisibility(View.GONE);
		} else {
			enrollBtn.setVisibility(View.VISIBLE);
		}

		if (app.favouriteCoach != null) {
			if (app.favouriteCoach.contains(coachVO)) {
				collectCk.setChecked(true);
			} else {
				collectCk.setChecked(false);
			}
		}
	}

	private void setListener() {
		addDeleteCoachCk.setOnCheckedChangeListener(this);
		placeTv.setOnClickListener(this);
		collectCk.setOnCheckedChangeListener(this);
		enrollBtn.setOnClickListener(this);
		commentList.setOnItemClickListener(this);
		commentList.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					scrollView.requestDisallowInterceptTouchEvent(false);
				} else {
					scrollView.requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});
	}

	private void obtainEnrollCoachDetail() {
		util.print("coachid=" + coachVO.getCoachid());
		HttpSendUtils.httpGetSend(coachDetail, this,
				Config.IP + "api/v1/userinfo/getuserinfo" + "/2/userid/"
						+ coachVO.getCoachid());
	}

	private void obtainCoachComment(int index) {
		HttpSendUtils.httpGetSend(coachComment, this,
				Config.IP + "api/v1/courseinfo/getusercomment" + "/2/"
						+ coachVO.getCoachid() + "/" + index);
	}

	private void setData() {
		if (coachVO != null) {
			String headRul = coachVO.getHeadportrait().getOriginalpic();
			RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) (findViewById(R.id.coach_detail_head_layout))
					.getLayoutParams();
			headParams.width = screenWidth;
			headParams.height = (int) (screenWidth * 2 / 3f);
			scrollView.scrollTo(0, 0);
			if (TextUtils.isEmpty(headRul)) {
				coachHeadPicIm
						.setBackgroundResource(R.drawable.default_big_pic);
			} else {
				BitmapManager.INSTANCE.loadBitmap2(headRul, coachHeadPicIm,
						screenWidth, headParams.height);
			}
			coachNameTv.setText(coachVO.getName());
			shuttle.setVisibility(coachVO.getIs_shuttle().equals("true") ? View.VISIBLE
					: View.GONE);
			List<Subject> subjects = coachVO.getSubject();
			String subject = "";
			for (int i = 0; i < subjects.size(); i++) {
				subject += subjects.get(i).getName();
				subject += " ";
			}
			if (subject.contains("科目二") && subject.contains("科目三")) {
				general.setVisibility(View.VISIBLE);
			} else {
				general.setVisibility(View.GONE);
			}
			String bar = coachVO.getStarlevel();
			try {
				rateBar.setRating(Float.parseFloat(bar));
			} catch (Exception e) {
				rateBar.setRating(5f);
			}
			shuttleTv.setText(coachVO.getShuttlemsg());
			courseTv.setText(subject);
			seniorityTv.setText(coachVO.getSeniority());
			placeTv.setText(coachVO.getDriveschoolinfo().getName());
			schoolTv.setText(coachVO.getTrainfieldlinfo().getName());
			workTimeTv.setText(coachVO.getWorktimedesc());
			studentCountTv.setText("学员" + coachVO.getStudentcoount() + "位:");
			rateTv.setText(coachVO.getPassrate() + "%");
			selfEvaluationTv.setText(coachVO.getIntroduction());
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
			setResult(v.getId(), getIntent());
			finish();
			break;
		case R.id.base_right_btn:
			try {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + coachVO.getMobile()));
				startActivity(phoneIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.coach_detail_place_tv:
			intent = new Intent(this, SchoolDetailActivity.class);
			SchoolVO schoolVO = new SchoolVO();
			schoolVO.setSchoolid(coachVO.getDriveschoolinfo().getId());
			intent.putExtra("school", schoolVO);
			startActivityForResult(intent, R.id.coach_detail_place_tv);
			break;
		case R.id.coach_detail_enroll_btn:
			if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_NONE.getValue())) {

				String checkResult = Util.isConfilctEnroll(coachVO);
				if (checkResult == null) {
					intent = new Intent();
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					intent.putExtra("coach", coachVO);
					setResult(RESULT_OK, intent);
					finish();
				} else if (checkResult.length() == 0) {
					app.selectEnrollCoach = coachVO;
					Util.updateEnrollCoach(this, coachVO, false);
					intent = new Intent();
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					intent.putExtra("coach", coachVO);
					setResult(RESULT_OK, intent);
					finish();
				} else if (checkResult.equals("refresh")) {
					app.selectEnrollCoach = coachVO;
					Util.updateEnrollCoach(this, coachVO, true);
					intent = new Intent();
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					intent.putExtra("coach", coachVO);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
							this, checkResult);
					dialog.show();
				}

			} else if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {
				intent = new Intent(this, AppointmentCarActivity.class);
				intent.putExtra("coach", coachVO);
				startActivity(intent);
			} else if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_ENROLLING.getValue())) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this)
						.dismissWithFailure("正在报名中，请等待审核");
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				// 用户在驾校详情页按回退键返回
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					data.putExtra("coach", coachVO);
					setResult(RESULT_OK, data);
					finish();
				}
			};
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(coachDetail)) {
			if (data != null) {
				try {
					coachVO = JSONUtil.toJavaBean(CoachVO.class, data);
					setData();
					obtainCoachComment(commentPage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (type.equals(coachComment)) {
			try {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0) {
						commentPage++;
						try {
							commentList.setVisibility(View.VISIBLE);
							noCommentTv.setVisibility(View.GONE);
							// 动态设置高度
							RelativeLayout.LayoutParams listParams = (RelativeLayout.LayoutParams) commentList
									.getLayoutParams();
							if (length > 1) {
								listParams.height = (int) (130 * screenDensity);
							}
						} catch (Exception e) {

						}
					}
					if (commentVoList == null)
						commentVoList = new ArrayList<CoachCommentVO>();
					for (int i = 0; i < length; i++) {
						CoachCommentVO coachCommentVO = JSONUtil.toJavaBean(
								CoachCommentVO.class,
								dataArray.getJSONObject(i));
						commentVoList.add(coachCommentVO);
					}
					int curPosition = 0;
					if (adapter == null) {
						adapter = new CoachCommentListAdapter(this,
								commentVoList);
					} else {
						curPosition = adapter.getCount();
						adapter.setData(commentVoList);
					}
					commentList.setAdapter(adapter);
					commentList.setSelection(curPosition);
					commentList.stopLoadMore();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (type.equals(addCoach)) {
			if (!app.favouriteCoach.contains(coachVO)) {
				app.favouriteCoach.add(coachVO);
				sendBroadcast(new Intent(MyFavouriteActiviy.class.getName())
						.putExtra("isRefresh", true).putExtra("activityName",
								FavouriteCoachActivity.class.getName()));
			}
		} else if (type.equals(deleteCoach)) {
			if (app.favouriteCoach.contains(coachVO)) {
				app.favouriteCoach.remove(coachVO);
				sendBroadcast(new Intent(MyFavouriteActiviy.class.getName())
						.putExtra("isRefresh", true).putExtra("activityName",
								FavouriteCoachActivity.class.getName()));
			}
		}
		return true;
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		obtainCoachComment(commentPage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());

		String url = Config.IP + "api/v1/userinfo/favoritecoach/"
				+ coachVO.getCoachid();
		if (isChecked) {
			HttpSendUtils.httpPutSend(addCoach, this, url, null, 10000,
					headerMap);
		} else {
			HttpSendUtils.httpDeleteSend(deleteCoach, this, url, null, 10000,
					headerMap);
		}
	}

	@Override
	public void forOperResult(Intent intent) {
		boolean showStudentInfo = intent.getBooleanExtra("showStudentInfo",
				false);
		if (showStudentInfo) {
			int position = intent.getIntExtra("position", 0);
			String studentId = ((CoachCommentVO) adapter.getItem(position))
					.getUserid().get_id();
			Intent intent2 = new Intent(this, StudentInfoActivity.class);
			intent2.putExtra("studentId", studentId);
			startActivity(intent2);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(R.id.base_left_btn, getIntent());
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void selectConfirm(boolean isConfirm, boolean isFreshAll) {
		if (isConfirm) {
			app.selectEnrollCoach = coachVO;
			Util.updateEnrollCoach(this, coachVO, isFreshAll);
			if (isFreshAll) {
				app.selectEnrollSchool = Util.getEnrollUserSelectedSchool(this);
				app.selectEnrollCarStyle = Util
						.getEnrollUserSelectedCarStyle(this);
				app.selectEnrollClass = Util.getEnrollUserSelectedClass(this);
			}
			Intent intent = new Intent();
			intent.putExtra("activityName",
					SubjectEnrollActivity.class.getName());
			intent.putExtra("coach", coachVO);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
