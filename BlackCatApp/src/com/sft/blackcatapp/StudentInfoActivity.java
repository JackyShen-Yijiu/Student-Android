package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sft.adapter.CoachCommentListAdapter;
import com.sft.adapter.CoachListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.SubjectStatu;
import com.sft.common.Config.UserType;
import com.sft.util.JSONUtil;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachCommentVO;
import com.sft.vo.CoachVO;
import com.sft.vo.UserVO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitmapManager;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

/**
 * 学员详情
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class StudentInfoActivity extends BaseActivity implements OnItemClickListener, IXListViewListener {

	private static final String myCoach = "myCoach";
	private static final String studentDetail = "studentDetail";
	private static final String coachComment = "coachComment";
	//
	private ScrollView scrollView;
	// 头像
	private ImageView headPicIm;
	// 姓名,id
	private TextView nameTv, idTv;
	// 学车信息
	private TextView studentCarTv;
	// 驾校
	private TextView schoolTv, carStyleTv, progressTv, addressTv;
	// 教练评论
	private TextView coachCommentTv;
	// 学员的教练
	private TextView studentCoachTv;
	//
	private TextView noCommentTv;
	private TextView noCoachTv;
	//
	private XListView commentList;
	private XListView coachList;

	//
	private RelativeLayout commentLayout, coachLayout;
	//
	private UserVO studentVO;
	//
	private int commentPage = 1;
	//
	private CoachCommentListAdapter adapter;
	//
	private List<CoachCommentVO> list;
	//
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_student_info);
		initView();
		obtainStudentDetail();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.student_detail);

		userId = getIntent().getStringExtra("studentId");

		if (userId.equals(app.userVO.getUserid())) {

		} else {
			showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN | BaseActivity.SHOW_RIGHT_BTN);
			setBtnBkground(R.drawable.base_left_btn_bkground, R.drawable.chat_btn_bkground);
		}

		scrollView = (ScrollView) findViewById(R.id.student_detail_scrollview);
		headPicIm = (ImageView) findViewById(R.id.student_headpic_im);
		nameTv = (TextView) findViewById(R.id.student_name_tv);
		idTv = (TextView) findViewById(R.id.student_id_tv);

		schoolTv = (TextView) findViewById(R.id.student_school_tv);
		carStyleTv = (TextView) findViewById(R.id.student_carstyle_tv);
		progressTv = (TextView) findViewById(R.id.student_progress_tv);
		addressTv = (TextView) findViewById(R.id.student_address_tv);
		commentList = (XListView) findViewById(R.id.student_comment_listview);
		noCommentTv = (TextView) findViewById(R.id.student_comment_nocomment_tv);
		commentLayout = (RelativeLayout) findViewById(R.id.student_comment_comment_layout);
		noCoachTv = (TextView) findViewById(R.id.student_comment_nocoach_tv);
		coachList = (XListView) findViewById(R.id.student_comment_coach_listview);
		coachLayout = (RelativeLayout) findViewById(R.id.student_comment_coach_layout);

		nameTv.getPaint().setFakeBoldText(true);
		studentCarTv = (TextView) findViewById(R.id.student_car_info_tv);
		studentCarTv.getPaint().setFakeBoldText(true);
		coachCommentTv = (TextView) findViewById(R.id.student_caochcomment_tv);
		coachCommentTv.getPaint().setFakeBoldText(true);
		studentCoachTv = (TextView) findViewById(R.id.student_comment_caoch_tv);
		studentCoachTv.getPaint().setFakeBoldText(true);

		headPicIm.getLayoutParams().height = (int) (screenWidth * 240 / 360f);

		commentList.setPullRefreshEnable(false);
		commentList.setPullLoadEnable(true);
		commentList.setXListViewListener(this);
		commentList.setVisibility(View.GONE);

		coachList.setPullRefreshEnable(false);
		coachList.setPullLoadEnable(false);
		coachList.setVisibility(View.GONE);

		commentLayout.setVisibility(View.VISIBLE);
		coachLayout.setVisibility(View.GONE);

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
		coachList.setOnTouchListener(new View.OnTouchListener() {

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

	private void setData() {
		if (studentVO != null) {
			LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) headPicIm.getLayoutParams();
			String url = studentVO.getHeadportrait().getOriginalpic();
			if (TextUtils.isEmpty(url)) {
				headPicIm.setBackgroundResource(R.drawable.default_big_pic);
			} else {
				BitmapManager.INSTANCE.loadBitmap2(url, headPicIm, headParams.width, headParams.height);
			}
			nameTv.setText(studentVO.getNickname());
			idTv.setText(studentVO.getDisplayuserid());

			schoolTv.setText(studentVO.getApplyschoolinfo().getName());
			carStyleTv.setText(studentVO.getCarmodel().getCode());
			String subjectId = studentVO.getSubject().getSubjectid();
			if (subjectId.equals(SubjectStatu.SUBJECT_TWO.getValue())) {
				progressTv.setText(studentVO.getSubject().getName());
			} else if (subjectId.equals(SubjectStatu.SUBJECT_THREE.getValue())) {
				progressTv.setText(studentVO.getSubject().getName());
			}
			addressTv.setText(studentVO.getAddress());
		}

	}

	private void obtainStudentDetail() {
		HttpSendUtils.httpGetSend(studentDetail, this,
				Config.IP + "api/v1/userinfo/getuserinfo" + "/1/userid/" + userId);
	}

	private void obtainCoachComment(int commentPage) {
		HttpSendUtils.httpGetSend(coachComment, this,
				Config.IP + "api/v1/courseinfo/getusercomment" + "/1/" + userId + "/" + commentPage);
	}

	private void obtainStudentCoach() {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myCoach, this, Config.IP + "api/v1/userinfo/getmycoachlist", null, 10000, headerMap);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(studentDetail)) {
				if (data != null) {
					studentVO = (UserVO) JSONUtil.toJavaBean(UserVO.class, data);
					setData();
					obtainCoachComment(commentPage);
				}
			} else if (type.equals(coachComment)) {
				if (dataArray != null) {
					if (list == null)
						list = new ArrayList<CoachCommentVO>();

					int selectIndex = list.size();
					int length = dataArray.length();
					if (length > 0) {
						commentPage++;
						commentList.setVisibility(View.VISIBLE);
						noCommentTv.setVisibility(View.GONE);
						// 动态设置高度
						RelativeLayout.LayoutParams listParams = (RelativeLayout.LayoutParams) commentList
								.getLayoutParams();
						listParams.height = (int) (150 * screenDensity);
					} else {
						if (selectIndex == 0) {
							// 此时没有教练评论，获取我的教练用于显示
							commentLayout.setVisibility(View.GONE);
							coachLayout.setVisibility(View.VISIBLE);
							obtainStudentCoach();
							return true;
						}
					}
					for (int i = 0; i < length; i++) {
						CoachCommentVO coachCommentVO = (CoachCommentVO) JSONUtil.toJavaBean(CoachCommentVO.class,
								dataArray.getJSONObject(i));
						list.add(coachCommentVO);
					}
					if (adapter == null) {
						adapter = new CoachCommentListAdapter(this, list);
					} else {
						adapter.setData(list);
					}
					commentList.setAdapter(adapter);
					commentList.setSelection(selectIndex);
					commentList.stopLoadMore();
				}
			} else if (type.equals(myCoach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0) {
						coachList.setVisibility(View.VISIBLE);
						noCoachTv.setVisibility(View.GONE);
					}
					List<CoachVO> list = new ArrayList<CoachVO>();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = (CoachVO) JSONUtil.toJavaBean(CoachVO.class, dataArray.getJSONObject(i));
						list.add(coachVO);
					}
					CoachListAdapter adapter = new CoachListAdapter(this, list);
					coachList.setAdapter(adapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
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
		case R.id.base_right_btn:
			String chatName = studentVO.getUserid();
			if (!TextUtils.isEmpty(chatName)) {
				intent = new Intent(this, ChatActivity.class);
				intent.putExtra("chatName", getChatNickName(studentVO));
				intent.putExtra("chatUrl", studentVO.getHeadportrait().getOriginalpic());
				intent.putExtra("chatId", studentVO.getUserid());
				intent.putExtra("userTypeNoAnswer", UserType.USER.getValue());
			} else {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("无法获取对方信息");
			}
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		obtainCoachComment(commentPage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	private String getChatNickName(UserVO userVO) {
		String nickName = userVO.getNickname();
		if (TextUtils.isEmpty(nickName)) {
			nickName = userVO.getName();
			if (TextUtils.isEmpty(nickName)) {
				nickName = userVO.getDisplaymobile();
			}
		}
		if (TextUtils.isEmpty(nickName)) {
			nickName = "陌生人";
		}
		return nickName;
	}
}
