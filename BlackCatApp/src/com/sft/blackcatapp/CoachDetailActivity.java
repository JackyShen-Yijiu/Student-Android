package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.MyHandler;

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
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.view.WordWrapView;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachCommentVO;
import com.sft.vo.CoachVO;
import com.sft.vo.TagsList;
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
	// 可授科目
	private TextView subjectTv;
	// 距离
	private TextView distanceTv;
	// 科目
	private TextView courseTv;
	// 教龄
	private TextView seniorityTv;
	// 个人评价
	private TextView selfEvaluationTitleTv;
	// 个人评价
	private TextView selfEvaluationTv;
	// 个人评价更多按钮
	private TextView selfEvaluationMoreTv;
	// 学员评价
	private XListView commentList;
	// 报名按钮
	// private Button enrollBtn;
	// 教练信息
	private TextView coachInfo;
	// 学院评价
	private TextView studentEvaluation;
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
	private WordWrapView personLabel;

	// 课程费用
	private ListView courseFeeListView;
	private SchoolDetailCourseFeeAdapter courseFeeAdapter;

	private String schoolId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_coach_detail);
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

	private void initView() {
		schoolId = getIntent().getStringExtra("schoolId");

		showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN
				| BaseActivity.SHOW_RIGHT_BTN);
		setBtnBkground(R.drawable.base_left_btn_bkground, R.drawable.phone);
		setTitleText(R.string.coach_detail);

		scrollView = (ScrollView) findViewById(R.id.coach_detail_scrollview);
		// addDeleteCoachCk = (CheckBox)
		// findViewById(R.id.coach_detail_collection_ck);
		coachHeadPicIm = (ImageView) findViewById(R.id.coach_detail_headicon_im);
		collectCk = (CheckBox) findViewById(R.id.coach_detail_collection_ck);
		coachNameTv = (TextView) findViewById(R.id.coach_detail_coachname_tv);
		shuttle = (TextView) findViewById(R.id.coach_detail_shuttle);
		general = (TextView) findViewById(R.id.coach_detail_general);
		rateBar = (RatingBar) findViewById(R.id.coach_detail_ratingBar);
		placeTv = (TextView) findViewById(R.id.coach_detail_place_tv);
		seniorityTv = (TextView) findViewById(R.id.coach_detail_coach_teachage_tv);

		schoolTv = (TextView) findViewById(R.id.coach_detail_school_tv);
		// studentCountTv = (TextView)
		// findViewById(R.id.coach_detail_student_count);
		// rateTv = (TextView) findViewById(R.id.coach_detail_rate_tv);
		// shuttleTv = (TextView) findViewById(R.id.coach_detail_shuttle);
		// workTimeTv = (TextView) findViewById(R.id.coach_detail_weektime_tv);
		// courseTv = (TextView) findViewById(R.id.coach_detail_course_tv);

		selfEvaluationTitleTv = (TextView) findViewById(R.id.coach_detail_evaluationtitle_tv);
		selfEvaluationTv = (TextView) findViewById(R.id.coach_detail_evaluation_tv);
		selfEvaluationMoreTv = (TextView) findViewById(R.id.coach_detail_evaluation_more_tv);
		coachInfo = (TextView) findViewById(R.id.coach_detail_coachinfo_tv);
		studentEvaluation = (TextView) findViewById(R.id.coach_detail_studentevaluation_tv);
		noCommentTv = (TextView) findViewById(R.id.coach_detail_noevaluation_tv);
		commentList = (XListView) findViewById(R.id.coach_detail_listview);
		commentList.setFocusable(false);
		carTypeTv = (TextView) findViewById(R.id.coach_detail_car_style_tv);
		subjectTv = (TextView) findViewById(R.id.coach_detail_enable_subject_tv);
		distanceTv = (TextView) findViewById(R.id.coach_detail_distance_tv);
		trainPicLayout = (LinearLayout) findViewById(R.id.coach_detail_train_pic_ll);
		personLabel = (WordWrapView) findViewById(R.id.coach_detail_personality_labels);
		personLabel.showColor(true);
		courseFeeListView = (ListView) findViewById(R.id.coash_detail_course_fee_listview);
		courseFeeListView.setFocusable(false);
		// 如果是预约时更多教练放入教练详情，此处不显示
		courseFeeIm = (ImageView) findViewById(R.id.caoch_detail_course_fee_im);
		courseFeeRl = (RelativeLayout) findViewById(R.id.caoch_detail_course_fee_rl);
		String where = getIntent().getStringExtra("where");
		if (!TextUtils.isEmpty(where)
				&& AppointmentMoreCoachActivity.class.getName().equals(where)) {
			courseFeeIm.setVisibility(View.GONE);
			courseFeeRl.setVisibility(View.GONE);
		}

		commentList.setVisibility(View.GONE);
		noCommentTv.setVisibility(View.VISIBLE);

		// enrollBtn = (Button) findViewById(R.id.coach_detail_enroll_btn);

		// 中文字体加粗
		selfEvaluationTitleTv.getPaint().setFakeBoldText(true);
		coachInfo.getPaint().setFakeBoldText(true);
		coachNameTv.getPaint().setFakeBoldText(true);
		studentEvaluation.getPaint().setFakeBoldText(true);

		commentList.setPullRefreshEnable(false);
		commentList.setPullLoadEnable(true);
		commentList.setXListViewListener(this);

		coachVO = (CoachVO) getIntent().getSerializableExtra("coach");

		shuttle.setVisibility(View.GONE);
		general.setVisibility(View.GONE);
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
	}

	private void setListener() {
		// addDeleteCoachCk.setOnCheckedChangeListener(this);
		// placeTv.setOnClickListener(this);
		collectCk.setOnCheckedChangeListener(this);
		// enrollBtn.setOnClickListener(this);
		commentList.setOnItemClickListener(this);
		selfEvaluationMoreTv.setOnClickListener(this);
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
		if (coachVO != null) {
			RelativeLayout.LayoutParams headParam = (RelativeLayout.LayoutParams) coachHeadPicIm
					.getLayoutParams();

			String url = coachVO.getHeadportrait().getOriginalpic();
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
			subjectTv.setText(subjectString);
			distanceTv.setText(coachVO.getDistance());
			// workTimeTv.setText(coachVO.getWorktimedesc());
			// studentCountTv.setText("学员" + coachVO.getStudentcoount() + "位:");
			// rateTv.setText(coachVO.getPassrate() + "%");

			// 动态添加训练场地的图片
			String[] trainPicStrings = coachVO.getTrainfieldlinfo()
					.getPictures();
			for (int i = 0; i < trainPicStrings.length; i++) {
				ImageView imageView = new ImageView(this);
				LayoutParams params = (LayoutParams) imageView
						.getLayoutParams();
				imageView.setScaleType(ScaleType.CENTER_CROP);
				if (i != 0) {
					params.leftMargin = dp2px(15);
				}
				BitmapManager.INSTANCE.loadBitmap2(trainPicStrings[i],
						imageView, dp2px(90), dp2px(60));
				trainPicLayout.addView(imageView, params);
			}
			// 设置课程费用
			if (coachVO.getServerclasslist() != null) {

				courseFeeAdapter = new SchoolDetailCourseFeeAdapter(
						coachVO.getServerclasslist(), this, mListener,
						enrollState);
				courseFeeListView.setAdapter(courseFeeAdapter);
			}

			// 设置个人说明信息
			selfEvaluationTv.setText(coachVO.getIntroduction());
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
		Intent i = new Intent(CoachDetailActivity.this, ApplyActivity.class);
		i.putExtra("coach", coachVO);
		i.putExtra("schoolId", schoolId);
		i.putExtra("class", classe);
		i.putExtra(SearchCoachActivity.from_searchCoach_enroll, true);
		startActivity(i);
		// coachVO.getDriveschoolinfo().
		// i.putExtra("school", "");
		// qw

	}

	/**
	 * 设置标签
	 */
	private void addTags() {
		if (personLabel.getChildCount() > 0) {
			return;
		}
		List<TagsList> list = new ArrayList<TagsList>();
		for (TagsList labelBean : coachVO.getTagslist()) {
			list.add(labelBean);
		}
		personLabel.setData(list);
		personLabel.removeAllViews();
		// String[] strs = {"个性标签","包接送","五星级教练","不吸烟","态度极好","免费提供水服务","不收彩礼"};
		for (int i = 0; i < list.size(); i++) {
			TextView textview = new TextView(this);
			textview.setTextColor(Color.parseColor("#333333"));
			textview.setText(list.get(i).getTagname());
			personLabel.addView(textview);
		}
	}

	int minHeight;// 2行时候的高度
	int maxHeight;// schoolInTv总的高度
	private boolean isExtend = false;// 是否展开
	private boolean isRunAnim = false;
	private ImageView courseFeeIm;
	private RelativeLayout courseFeeRl;

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
		case R.id.coach_detail_evaluation_more_tv:

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
			isExtend = !isExtend;
			selfEvaluationMoreTv.setText(isExtend ? "收起" : "更多");
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
}
