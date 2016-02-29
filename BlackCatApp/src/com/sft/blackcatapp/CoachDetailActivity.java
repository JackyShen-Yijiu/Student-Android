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
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

import com.joooonho.SelectableRoundedImageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.sft.adapter.CoachCommentListAdapter;
import com.sft.adapter.SchoolDetailCourseFeeAdapter;
import com.sft.adapter.SchoolDetailCourseFeeAdapter.MyClickListener;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.dialog.NoLoginDialog;
import com.sft.fragment.MenuFragment;
import com.sft.util.BaseUtils;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.viewutil.MyScrollView;
import com.sft.viewutil.MyScrollView.scrollStateListener;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachCommentVO;
import com.sft.vo.CoachVO;
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
		OnSelectConfirmListener, android.widget.RadioGroup.OnCheckedChangeListener, scrollStateListener {

	private static final String coachDetail = "coachDetail";
	private static final String coachComment = "coachComment";
	private static final String addCoach = "addCoach";
	private static final String deleteCoach = "deleteCoach";
	private MyScrollView scrollView;
	// 教练头像
	private com.joooonho.SelectableRoundedImageView coachHeadPicIm;
	// 收藏按钮
	private CheckBox collectCk;
	// 教练姓名
	private TextView coachNameTv;
	//
//	private TextView shuttle, general;
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
	// 可授科目
	private TextView subjectTv;
	// 距离
//	private TextView distanceTv;
	// 科目
	private TextView courseTv;
	// 教龄
	private TextView seniorityTv;
	// 个人评价
//	private TextView selfEvaluationTitleTv;
	// 个人评价
	private TextView selfEvaluationTv;
	// 个人评价更多按钮
//	private TextView selfEvaluationMoreTv;
	/**标签*/
	private TextView tagTv;
	// 学员评价
	private XListView commentList;
	// 报名按钮
	// private Button enrollBtn;
	// 教练信息
//	private TextView coachInfo;
	// 学院评价
//	private TextView studentEvaluation;
	// 暂无评论文本
	private TextView noCommentTv;
	// 车型
	private TextView carTypeTv;
	// 教练对象
	private CoachVO coachVO;
	// // 添加删除喜欢的教练
	// private CheckBox addDeleteCoachCk;
	// 当前评论的页数
	private int commentPage = 1;
	//
	private List<CoachCommentVO> commentVoList;
	//
	private CoachCommentListAdapter adapter;
	private LinearLayout trainPicLayout;
//	private WordWrapView personLabel;

	// 课程费用
	private XListView courseFeeListView;
	private SchoolDetailCourseFeeAdapter courseFeeAdapter;

	private String schoolId = "";
	
	private RadioGroup radioGroup,radioGroupTop;
	
	private RadioButton rbCourse,rbCourseTop,rbComment,rbCommentTop;
	
	private ImageButton imgLeft,imgBus,imgPhone;
	private TextView tvTitle;
	/**屏幕 */
	private int ScreenHeight;
	
	
	private View viewTop;
	private View viewTopStatic;
	private View titleLayout;
	
	private TextView tvNoTrain;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		addView(R.layout.activity_coach_detail2);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_coach_detail2);
		ScreenHeight = BaseUtils.getScreenHeight(this);
		initView();
		setListener();
		obtainEnrollCoachDetail();
	}

	private String enrollState;

	@Override
	protected void onResume() {
		register(getClass().getName());
		if (app.userVO == null
				|| app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			enrollState = EnrollResult.SUBJECT_NONE.getValue();
		} else if (app.userVO.getApplystate().equals(
				EnrollResult.SUBJECT_ENROLLING.getValue())) {
			enrollState = EnrollResult.SUBJECT_ENROLLING.getValue();
		} else {
			enrollState = EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue();
		}
		super.onResume();
	}
	
	private void initTitle(){
		titleLayout = findViewById(R.id.base_titlebar_layout);
		imgLeft = (ImageButton) findViewById(R.id.base_left_btn);
		imgBus = (ImageButton) findViewById(R.id.base_right_btn2);
		imgPhone = (ImageButton) findViewById(R.id.base_right_btn);
		tvTitle = (TextView) findViewById(R.id.base_title_tv);
		
		imgLeft.setBackgroundResource(R.drawable.base_left_btn_bkground);
		imgBus.setImageResource(R.drawable.bus_white_icon);
		imgPhone.setImageResource(R.drawable.phone_white_icon);
		
		imgBus.setVisibility(View.VISIBLE);
		imgLeft.setOnClickListener(this);
		imgBus.setOnClickListener(this);
		imgPhone.setOnClickListener(this);
		
		
	}

	private void initView() {
		
		initTitle();
		
		// 顶部红色背景
		viewTop = (View) findViewById(R.id.coach_detail_top_ll);
		viewTopStatic = (View) findViewById(R.id.coach_detail_top_static);
		
		
		
		schoolId = getIntent().getStringExtra("schoolId");

//		showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN
//				| BaseActivity.SHOW_RIGHT_BTN);
//		setBtnBkground(R.drawable.base_left_btn_bkground, R.drawable.phone);
//		setTitleText(R.string.coach_detail);

		scrollView = (MyScrollView) findViewById(R.id.coach_detail_scrollview);
		scrollView.setOnStateListener(this);
		// addDeleteCoachCk = (CheckBox)
		// findViewById(R.id.coach_detail_collection_ck);
		coachHeadPicIm = (SelectableRoundedImageView) findViewById(R.id.coach_detail_headicon_im);
		coachHeadPicIm.setScaleType(ScaleType.CENTER_CROP);
		coachHeadPicIm.setImageResource(R.drawable.default_small_pic);
		coachHeadPicIm.setOval(true);
		
		collectCk = (CheckBox) findViewById(R.id.coach_detail_collection_ck);
		coachNameTv = (TextView) findViewById(R.id.coach_detail_name_tv);
//		shuttle = (TextView) findViewById(R.id.coach_detail_shuttle);
//		general = (TextView) findViewById(R.id.coach_detail_general);
		rateBar = (RatingBar) findViewById(R.id.coach_detail_ratingBar);
		placeTv = (TextView) findViewById(R.id.coach_detail_training_tv);
		seniorityTv = (TextView) findViewById(R.id.coach_detail_coach_teachage_tv);

		schoolTv = (TextView) findViewById(R.id.coach_detail_school_tv);
		// studentCountTv = (TextView)
		// findViewById(R.id.coach_detail_student_count);
		// rateTv = (TextView) findViewById(R.id.coach_detail_rate_tv);
		// shuttleTv = (TextView) findViewById(R.id.coach_detail_shuttle);
		// workTimeTv = (TextView) findViewById(R.id.coach_detail_weektime_tv);
		// courseTv = (TextView) findViewById(R.id.coach_detail_course_tv);

//		selfEvaluationTitleTv = (TextView) findViewById(R.id.coach_detail_evaluationtitle_tv);
		selfEvaluationTv = (TextView) findViewById(R.id.coach_detail_introduction_tv);
//		selfEvaluationMoreTv = (TextView) findViewById(R.id.coach_detail_evaluation_more_tv);
//		coachInfo = (TextView) findViewById(R.id.coach_detail_coachinfo_tv);
//		studentEvaluation = (TextView) findViewById(R.id.coach_detail_studentevaluation_tv);
		noCommentTv = (TextView) findViewById(R.id.coach_detail_noevaluation_tv);
		commentList = (XListView) findViewById(R.id.coach_detail_comments_listview);
		commentList.setFocusable(false);
		
		commentList.setPullRefreshEnable(false);
		carTypeTv = (TextView) findViewById(R.id.coach_detail_car_style_tv);
		subjectTv = (TextView) findViewById(R.id.coach_detail_enable_subject_tv);
//		distanceTv = (TextView) findViewById(R.id.coach_detail_distance_tv);
		trainPicLayout = (LinearLayout) findViewById(R.id.coach_detail_train_pic_ll);
//		personLabel = (WordWrapView) findViewById(R.id.coach_detail_personality_labels);
//		personLabel.showColor(true);
		courseFeeListView = (XListView) findViewById(R.id.coach_detail_classes_listview);
		courseFeeListView.setFocusable(false);
		courseFeeListView.setPullRefreshEnable(false);
		tagTv = (TextView) findViewById(R.id.coach_detail_tag_tv);
		
		tvNoTrain = (TextView) findViewById(R.id.coach_detail_nopic_tv);
		
		// 如果是预约时更多教练放入教练详情，此处不显示
//		courseFeeIm = (ImageView) findViewById(R.id.caoch_detail_course_fee_im);
//		courseFeeRl = (RelativeLayout) findViewById(R.id.caoch_detail_course_fee_rl);
//		String where = getIntent().getStringExtra("where");
//		if (!TextUtils.isEmpty(where)
//				&& AppointmentMoreCoachActivity.class.getName().equals(where)) {
//			courseFeeIm.setVisibility(View.GONE);
//			courseFeeRl.setVisibility(View.GONE);
//		}
		radioGroup = (RadioGroup) findViewById(R.id.coach_detail_radiogroup);
		radioGroupTop = (RadioGroup) findViewById(R.id.coach_detail_radiogroup_top);
		rbCourse = (RadioButton) findViewById(R.id.coach_detail_course_fee_rb);
		rbCourseTop = (RadioButton) findViewById(R.id.coach_detail_course_fee_rb_top);
		rbComment = (RadioButton) findViewById(R.id.coach_detail_coach_info_rb);
		rbCommentTop = (RadioButton) findViewById(R.id.coach_detail_coach_info_rb_top);
		
		radioGroup.setOnCheckedChangeListener(this);
		radioGroupTop.setOnCheckedChangeListener(this);
		commentList.setVisibility(View.GONE);
//		noCommentTv.setVisibility(View.GONE);

		// enrollBtn = (Button) findViewById(R.id.coach_detail_enroll_btn);

		// 中文字体加粗
//		selfEvaluationTitleTv.getPaint().setFakeBoldText(true);
//		coachInfo.getPaint().setFakeBoldText(true);
		coachNameTv.getPaint().setFakeBoldText(true);
//		studentEvaluation.getPaint().setFakeBoldText(true);

		commentList.setPullRefreshEnable(false);
		commentList.setPullLoadEnable(true);
		commentList.setXListViewListener(this);

		coachVO = (CoachVO) getIntent().getSerializableExtra("coach");

//		shuttle.setVisibility(View.GONE);
//		general.setVisibility(View.GONE);
		// if (app.userVO == null) {
		// collectCk.setEnabled(false);
		// enrollBtn.setVisibility(View.GONE);
		// } else {
		// enrollBtn.setVisibility(View.VISIBLE);
		// }

		if (app.favouriteCoach != null) {
			if (app.favouriteCoach.contains(coachVO)) {
				collectCk.setChecked(true);
			} else {
				collectCk.setChecked(false);
			}
		}
		
		alphaIn = AnimationUtils.loadAnimation(CoachDetailActivity.this,
				R.anim.alpha_in);
		alphaOut = AnimationUtils.loadAnimation(CoachDetailActivity.this,
				R.anim.alpha_out);
		scaleSmall = AnimationUtils.loadAnimation(CoachDetailActivity.this,
				R.anim.scale_small);
		scaleBig = AnimationUtils.loadAnimation(CoachDetailActivity.this,
				R.anim.scale_big);
	}

	private void setListener() {
		// addDeleteCoachCk.setOnCheckedChangeListener(this);
		// placeTv.setOnClickListener(this);
		collectCk.setOnCheckedChangeListener(this);
		// enrollBtn.setOnClickListener(this);
		commentList.setOnItemClickListener(this);
//		selfEvaluationMoreTv.setOnClickListener(this);
		// commentList.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_UP) {
		// scrollView.requestDisallowInterceptTouchEvent(false);
		// } else {
		// scrollView.requestDisallowInterceptTouchEvent(true);
		// }
		// return false;
		// }
		// });
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
		LogUtil.print("setData-->"+coachVO);
		
		if (coachVO != null) {
			LinearLayout.LayoutParams headParam = (LinearLayout.LayoutParams) coachHeadPicIm
					.getLayoutParams();

			String url = coachVO.getHeadportrait().getOriginalpic();
			LogUtil.print("headIcon-->"+url);
			if (TextUtils.isEmpty(url)) {
				coachHeadPicIm
						.setBackgroundResource(R.drawable.default_small_pic);
			} else {
				BitmapManager.INSTANCE.loadBitmap2(url, coachHeadPicIm,
						headParam.width, headParam.height);
			}
			
			coachNameTv.setText(coachVO.getName());
			// shuttle.setVisibility("true".equals(coachVO.getIs_shuttle()) ?
			// View.VISIBLE
			// : View.GONE);
			List<Subject> subjects = coachVO.getSubject();
			String subject = "";
			for (int i = 0; i < subjects.size(); i++) {
				subject += subjects.get(i).getName();
				subject += " ";
			}
			// if (subject.contains("科目二") && subject.contains("科目三")) {
			// general.setVisibility(View.VISIBLE);
			// } else {
			// general.setVisibility(View.GONE);
			// }
			String bar = coachVO.getStarlevel();
			try {
				rateBar.setRating(Float.parseFloat(bar));
			} catch (Exception e) {
				rateBar.setRating(5f);
			}

			// courseTv.setText(subject);
			seniorityTv.setText("教龄:" + coachVO.getSeniority() + "年");
			LogUtil.print("DriveSchool---->>>"+coachVO.getDriveschoolinfo());
			if(coachVO.getDriveschoolinfo()!=null)
				placeTv.setText(coachVO.getDriveschoolinfo().getName());
			schoolTv.setText(coachVO.getTrainfieldlinfo().getFieldname());
			
			carTypeTv.setText(coachVO.getCartype());
			String subjectString = "";
			for (int i = 0; i < coachVO.getSubject().size(); i++) {
				if (i == coachVO.getSubject().size() - 1) {
					subjectString += coachVO.getSubject().get(i).getName();
				} else {
					subjectString += coachVO.getSubject().get(i).getName()
							+ "/";
				}
			}
			LogUtil.print("setData--123>"+subjectString);
			subjectTv.setText(subjectString);
//			distanceTv.setText(coachVO.getDistance());
			// workTimeTv.setText(coachVO.getWorktimedesc());
			// studentCountTv.setText("学员" + coachVO.getStudentcoount() + "位:");
			// rateTv.setText(coachVO.getPassrate() + "%");

			// 动态添加训练场地的图片
			String[] trainPicStrings = coachVO.getTrainfieldlinfo()
					.getPictures();
			if(trainPicStrings.length>0){
				for (int i = 0; i < trainPicStrings.length; i++) {
					ImageView imageView = new ImageView(this);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					if (i != 0) {
						params.leftMargin = dp2px(15);
					}
					BitmapManager.INSTANCE.loadBitmap2(trainPicStrings[i],
							imageView, dp2px(90), dp2px(60));
					trainPicLayout.addView(imageView, params);
				}
				tvNoTrain.setVisibility(View.GONE);
			}else{
				tvNoTrain.setVisibility(View.VISIBLE);
			}
			
			// 设置课程费用
			if (coachVO.getServerclasslist() != null) {

				courseFeeAdapter = new SchoolDetailCourseFeeAdapter(
						coachVO.getServerclasslist(), this, mListener,
						enrollState);
				courseFeeAdapter.setName(coachVO.getDriveschoolinfo().getName());
				courseFeeListView.setAdapter(courseFeeAdapter);
				
				if(coachVO.getServerclasslist().size()==0){
					commentList.setVisibility(View.VISIBLE);
				}else{
					commentList.setVisibility(View.GONE);
					courseFeeListView.setVisibility(View.VISIBLE);
				}
					
				setListViewHeightBasedOnChildren(courseFeeListView);
				LogUtil.print("setdata-->>课程费用-->"+coachVO.getServerclasslist().size()+"评论--->"+courseFeeAdapter.getCount());
			}else{
				noCommentTv.setText("暂无课程");
				noCommentTv.setVisibility(View.GONE);
			}
			
			if(null == coachVO.getIntroduction()){
				// 设置个人说明信息
				selfEvaluationTv.setText("这个教练很懒，什么也没留下");
			}else{
				// 设置个人说明信息
				selfEvaluationTv.setText(coachVO.getIntroduction());
			}
			
			showCoachIntro();

			// 添加个性标签
			addTags();
		}
	}

	/**
	 * 实现类，响应按钮点击事件
	 */
	private MyClickListener mListener = new MyClickListener() {
		@Override
		public void myOnClick(int position, View v) {
			boolean isFromSearchCoach = getIntent().getBooleanExtra(
					SearchCoachActivity.from_searchCoach_enroll, false);
			Intent intent = null;
			//未登录
			if(!app.isLogin){
				NoLoginDialog dialog = new NoLoginDialog(CoachDetailActivity.this);
				dialog.show();
				return ;
			}
			
			
			if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_NONE.getValue())) {

				String checkResult = Util.isConfilctEnroll(coachVO);
				LogUtil.print("check--->" + checkResult);
				if (checkResult == null) {

					toPay(position);
					// intent = new Intent();
					// intent.putExtra("activityName",
					// SubjectEnrollActivity.class.getName());
					// intent.putExtra("coach", coachVO);
					// intent.putExtra(
					// SearchCoachActivity.from_searchCoach_enroll,
					// isFromSearchCoach);
					// setResult(RESULT_OK, intent);
					// finish();
				} else if (checkResult.length() == 0) {
					app.selectEnrollCoach = coachVO;
					Util.updateEnrollCoach(CoachDetailActivity.this, coachVO,
							false);
					intent = new Intent();
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					intent.putExtra("coach", coachVO);
					intent.putExtra(
							SearchCoachActivity.from_searchCoach_enroll,
							isFromSearchCoach);
					setResult(RESULT_OK, intent);
					finish();
				} else if (checkResult.equals("refresh")) {
					toPay(position);
					// app.selectEnrollCoach = coachVO;
					// Util.updateEnrollCoach(CoachDetailActivity.this, coachVO,
					// true);
					// intent = new Intent();
					// intent.putExtra("activityName",
					// SubjectEnrollActivity.class.getName());
					// intent.putExtra("coach", coachVO);
					// intent.putExtra(
					// SearchCoachActivity.from_searchCoach_enroll,
					// isFromSearchCoach);
					// setResult(RESULT_OK, intent);
					// finish();
				} else {
					EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
							CoachDetailActivity.this, checkResult);
					dialog.show();
				}

			} else if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {
				intent = new Intent(CoachDetailActivity.this,
						AppointmentCarActivity.class);
				intent.putExtra("coach", coachVO);
				startActivity(intent);
			} else if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_ENROLLING.getValue())) {
				ZProgressHUD.getInstance(CoachDetailActivity.this).show();
				ZProgressHUD.getInstance(CoachDetailActivity.this)
						.dismissWithFailure("正在报名中，请等待审核");
			}
		}
	};

	/**
	 * 跳转到 支付页面
	 */
	private void toPay(int po) {
		ClassVO classe = courseFeeAdapter.getItem(po);
//		LogUtil.print("classTypeId:---->"+classe.getCalssid()+"id:::>>"+classe.get_id());
		Intent i = new Intent(CoachDetailActivity.this, ApplyActivity.class);
		i.putExtra("coach", coachVO);
		i.putExtra("schoolId", schoolId);
		i.putExtra("class", classe);
		i.putExtra("from", 1);
		i.putExtra(SearchCoachActivity.from_searchCoach_enroll, true);
//		startActivity(i);
		startActivityForResult(i, 9);
		// coachVO.getDriveschoolinfo().
		// i.putExtra("school", "");
		// qw

	}

	/**
	 * 设置标签
	 */
	private void addTags() {
//		if (personLabel.getChildCount() > 0) {
//			return;
//		}
//		List<TagsList> list = new ArrayList<TagsList>();
//		for (TagsList labelBean : coachVO.getTagslist()) {
//			list.add(labelBean);
//		}
//		personLabel.setData(list);
//		personLabel.removeAllViews();
//		// String[] strs = {"个性标签","包接送","五星级教练","不吸烟","态度极好","免费提供水服务","不收彩礼"};
//		for (int i = 0; i < list.size(); i++) {
//			TextView textview = new TextView(this);
//			textview.setTextColor(Color.parseColor("#333333"));
//			textview.setText(list.get(i).getTagname());
//			personLabel.addView(textview);
//		}
	}

	int minHeight;// 2行时候的高度
	int maxHeight;// schoolInTv总的高度
	private boolean isExtend = false;// 是否展开
	private boolean isRunAnim = false;
//	private ImageView courseFeeIm;
//	private RelativeLayout courseFeeRl;

	private void showCoachIntro() {
		selfEvaluationTv.setMaxLines(2);
		selfEvaluationTv.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						selfEvaluationTv.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						minHeight = selfEvaluationTv.getMeasuredHeight();// 得到5行时候的高度

						// 2.让tv_des先显示全部的文本，再获取总高度
						selfEvaluationTv.setMaxLines(Integer.MAX_VALUE);// 显示全部文本
						selfEvaluationTv.getViewTreeObserver()
								.addOnGlobalLayoutListener(
										new OnGlobalLayoutListener() {
											@Override
											public void onGlobalLayout() {
												selfEvaluationTv
														.getViewTreeObserver()
														.removeGlobalOnLayoutListener(
																this);
												// 获取总高度
												maxHeight = selfEvaluationTv
														.getHeight();

												LogUtil.print("minHeight-"
														+ minHeight
														+ "+maxHeight"
														+ maxHeight);
												selfEvaluationTv
														.getLayoutParams().height = minHeight;
												selfEvaluationTv
														.requestLayout();
											}
										});
					}
				});
	}

	private int dp2px(int dp) {
		return (int) (this.getResources().getDisplayMetrics().density * dp + 0.5);
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
		case R.id.base_right_btn2://班车
			Intent i = new Intent(CoachDetailActivity.this,
					SchoolBusRouteActivity.class);
//			i.putExtra("school_route", school);
			i.putExtra(MenuFragment.schoolId, coachVO.getDriveschoolinfo().getId());
			startActivity(i);
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
		// case R.id.coach_detail_place_tv:
		// intent = new Intent(this, SchoolDetailActivity.class);
		// SchoolVO schoolVO = new SchoolVO();
		// schoolVO.setSchoolid(coachVO.getDriveschoolinfo().getId());
		// intent.putExtra("school", schoolVO);
		// startActivityForResult(intent, R.id.coach_detail_place_tv);
		// break;

		// 个人信息说明
		case R.id.coach_detail_introduction_tv:

			if (isRunAnim) {
				return;
			}

			ValueAnimator animator;
			if (isExtend) {
				// 执行收缩动画
				animator = ValueAnimator.ofInt(maxHeight, minHeight);
			} else {
				// 执行扩展动画
				animator = ValueAnimator.ofInt(minHeight, maxHeight);
			}

			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					int animatedValue = (Integer) animator.getAnimatedValue();
					selfEvaluationTv.getLayoutParams().height = animatedValue;
					selfEvaluationTv.requestLayout();

					scrollView.scrollBy(0, maxHeight - minHeight);
				}
			});
			animator.addListener(new AppDesAnimListener());
			animator.setDuration(350);
			animator.start();

			// 标记值取反
//			isExtend = !isExtend;
//			selfEvaluationMoreTv.setText(isExtend ? "收起" : "更多");
			break;
		}
	}

	class AppDesAnimListener implements AnimatorListener {
		@Override
		public void onAnimationCancel(Animator arg0) {
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			isRunAnim = false;
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
		}

		@Override
		public void onAnimationStart(Animator arg0) {
			isRunAnim = true;
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
//							commentList.setVisibility(View.VISIBLE);
//							noCommentTv.setVisibility(View.GONE);
							// 动态设置高度
							RelativeLayout.LayoutParams listParams = (RelativeLayout.LayoutParams) commentList
									.getLayoutParams();
							if (length > 1) {
								listParams.height = (int) (130 * screenDensity);
							}
						} catch (Exception e) {

						}
					} else if (length == 0) {
						toast.setText("没有更多数据了");
						commentList.setPullLoadEnable(false);
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
					setListViewHeightBasedOnChildren(commentList);
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
		if(!app.isLogin)
			return;
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

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		// TODO Auto-generated method stub
		switch(checkedId){
		case R.id.coach_detail_course_fee_rb:
		case R.id.coach_detail_course_fee_rb_top://课程费用
			courseFeeListView.setVisibility(View.VISIBLE);
			commentList.setVisibility(View.GONE);
			rbCourse.setChecked(true);
			rbCourseTop.setChecked(true);
//			Toast("onChecket-<<"+coachVO.getServerclasslist());
			if(coachVO.getServerclasslist()!=null){
				
				if(coachVO.getServerclasslist().size() == 0){
					courseFeeListView.setVisibility(View.GONE);
					noCommentTv.setText("暂无课程");
					noCommentTv.setVisibility(View.VISIBLE);
				}else{
					courseFeeListView.setVisibility(View.VISIBLE);
					noCommentTv.setVisibility(View.GONE);
				}
			}else{
				noCommentTv.setText("暂无课程");
				noCommentTv.setVisibility(View.VISIBLE);
			}
			
			break;
		case R.id.coach_detail_coach_info_rb:
		case R.id.coach_detail_coach_info_rb_top://评论
//			Toast("评论--->"+courseFeeListView.getVisibility());
			courseFeeListView.setVisibility(View.GONE);
			commentList.setVisibility(View.VISIBLE);
			rbComment.setChecked(true);
			rbCommentTop.setChecked(true);
			if(commentVoList!=null){
				if(commentVoList.size() == 0){//
					commentList.setVisibility(View.GONE);
					noCommentTv.setText("暂无评论");
					noCommentTv.setVisibility(View.VISIBLE);
					
				}else{
					commentList.setVisibility(View.VISIBLE);
					noCommentTv.setVisibility(View.GONE);
				}
			}else{
				noCommentTv.setText("暂无评论");
				noCommentTv.setVisibility(View.VISIBLE);
			}
			
			break;
		}
		
	}

	@Override
	public void onScrollChanged(int t, int oldt) {
		// TODO Auto-generated method stub
		int[] location = new int[2];
		coachNameTv.getLocationOnScreen(location);
		int y = location[1];

		int[] location2 = new int[2];
		radioGroup.getLocationOnScreen(location2);
		int y1 = location2[1];
		// LogUtil.print(viewTop.getVisibility()+"ScrollView---onScrollChangedt::>"+(viewTopStatic.getY()+viewTopStatic.getHeight())+"Name::>>>"+y);

		if (topStatic == 0) {// 初始化
			topStatic = viewTopStatic.getY() + viewTopStatic.getHeight();
			topTab = radioGroupTop.getY();
		} else if (topStatic < y) {// 刚进入 状态
			if (viewTop.getVisibility() != View.GONE) {
				viewTop.setVisibility(View.GONE);
				titleLayout.setBackgroundResource(android.R.color.transparent);
				collectCk.setVisibility(View.VISIBLE);
				coachNameTv.setVisibility(View.VISIBLE);
				tvTitle.setText("");
				titleLayout.startAnimation(alphaOut);
				viewTop.startAnimation(alphaOut);

				collectCk.startAnimation(scaleBig);
				//
			}
			noCommentTv.setHeight(ScreenHeight - y1);
			LogUtil.print(topTab + "yyyyyyy" + y1 + "TextViewHeight::::>>"
					+ noCommentTv.getHeight());
		} else if (topStatic > y || topStatic == y) {// 已经滑动很多，
			if (viewTop.getVisibility() != View.VISIBLE) {
				viewTop.setVisibility(View.VISIBLE);
				titleLayout.setBackgroundResource(R.color.new_app_main_color);
				collectCk.setVisibility(View.INVISIBLE);
				coachNameTv.setVisibility(View.INVISIBLE);
				tvTitle.setText(coachVO.getName());
				
				viewTop.startAnimation(alphaIn);
				titleLayout.startAnimation(alphaIn);
				collectCk.startAnimation(scaleSmall);
				// 设置 noCoach 的高度
			}
			if (topTab < y1) {// 还没有到 最上面
				if (noCommentTv.getVisibility() == View.VISIBLE) {
					int height = (ScreenHeight - y1) > noCommentTv.getHeight() ? noCommentTv
							.getHeight() : ScreenHeight - y1;
							noCommentTv.setHeight(height);
					LogUtil.print(height + "yyyyyyy222>>" + y1
							+ "TextViewHeight::::>>" + noCommentTv.getHeight());

				} else {
					noCommentTv.setHeight(ScreenHeight - y1);
				}

			} else {// 已经在最上面了
				noCommentTv.setHeight((int) (ScreenHeight - topStatic - 180));
			}

			// 滑动到 课程费用/教练信息
			if (topTab > y1) {// 显示固定的，
				if (radioGroupTop.getVisibility() != View.VISIBLE)
					radioGroupTop.setVisibility(View.VISIBLE);
			} else {// 隐藏固定的
				if (radioGroupTop.getVisibility() != View.INVISIBLE)
					radioGroupTop.setVisibility(View.INVISIBLE);
			}

		}

		// 处理 没有教练或者没有课程的信息
	}
	
	private float topStatic = 0;
	private float topTab = 0;
	
	Animation alphaIn;
	Animation alphaOut;
	Animation scaleBig;
	Animation scaleSmall;
	
}
