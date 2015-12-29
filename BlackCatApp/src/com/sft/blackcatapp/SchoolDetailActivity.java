package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.InfinitePagerAdapter;
import cn.sft.infinitescrollviewpager.InfiniteViewPager;
import cn.sft.infinitescrollviewpager.MyHandler;
import cn.sft.infinitescrollviewpager.PageChangeListener;
import cn.sft.infinitescrollviewpager.PageClickListener;
import cn.sft.pull.LoadMoreView;
import cn.sft.pull.LoadMoreView.LoadMoreListener;
import cn.sft.pull.OnItemClickListener;

import com.sft.adapter.SchoolDetailCoachHoriListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CoachVO;
import com.sft.vo.HeadLineNewsVO;
import com.sft.vo.SchoolVO;

/**
 * 驾校详情界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class SchoolDetailActivity extends BaseActivity implements
		BitMapURLExcepteionListner, PageChangeListener,
		OnCheckedChangeListener, OnSelectConfirmListener, OnItemClickListener,
		LoadMoreListener {

	private static final String schoolType = "school";
	private static final String coach = "coach";
	private static final String addSchool = "addSchool";
	private static final String deleteSchool = "deleteSchool";
	private static final String headLineNews = "headLineNews";
	/**
	 * 广告栏的地址
	 */
	private String[] adImageUrl;
	/**
	 * 包含小圆点的layout
	 */
	private LinearLayout dotLayout;
	/**
	 * 小圆点的集合
	 */
	private ImageView[] imageViews;
	/**
	 * 广告栏
	 */
	private InfiniteViewPager viewPager;
	/**
	 * 广告内容
	 */
	private List<HeadLineNewsVO> adList;

	private int viewPagerHeight;
	private RelativeLayout adLayout;
	// 驾校信息
	private TextView schoolInfoTv;
	// 驾校简介
	private TextView schoolInstructionTv;
	// 驾校教练列表
	private LoadMoreView horizontalListView;
	//
	private SchoolDetailCoachHoriListAdapter adapter;
	// 报名按钮
	private Button enrollBtn;
	// 用户查看的驾校
	private SchoolVO school;
	// 驾校名称
	private TextView schoolNameTv;
	// 驾校地址
	private TextView schoolAddressTv;
	// 通过率
	private TextView schoolRateTv;
	// 营业时间
	private TextView workTimeTv;
	// 驾校简介
	private TextView schoolInTv;
	// 添加删除教练
	private CheckBox addDeleteSchoolCk;
	//
	private TextView noCoahTv;

	//
	private int coachPage = 1;
	//
	private List<CoachVO> coachList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_school_detail);
		initView();
		setListener();
		obtainEnrollSchoolDetail();
	}

	private void obtainSchoolCoach(int coachPage) {
		HttpSendUtils.httpGetSend(coach, this, Config.IP
				+ "api/v1/getschoolcoach/" + school.getSchoolid() + "/"
				+ coachPage);
	}

	private void obtainHeadLineNews() {
		HttpSendUtils.httpGetSend(headLineNews, this, Config.IP
				+ "api/v1/info/headlinenews");
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		if (app.userVO == null
				|| app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			enrollBtn.setText(R.string.enroll);
		} else if (app.userVO.getApplystate().equals(
				EnrollResult.SUBJECT_ENROLLING.getValue())) {
			enrollBtn.setText(R.string.verifying);
		} else {
			enrollBtn.setText(R.string.appointment);
		}
		super.onResume();
	};

	private void initView() {
		showTitlebarBtn(BaseActivity.SHOW_LEFT_BTN
				| BaseActivity.SHOW_RIGHT_BTN);
		setBtnBkground(R.drawable.base_left_btn_bkground, R.drawable.phone);
		setTitleText(R.string.school_detail);

		adLayout = (RelativeLayout) findViewById(R.id.school_detail_headpic_im);
		viewPager = (InfiniteViewPager) findViewById(R.id.school_detail_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.school_detail_dotlayout);

		horizontalListView = (LoadMoreView) findViewById(R.id.select_coach_horizon_listview);
		horizontalListView.setPullLoadMoreEnable(true);
		horizontalListView.setHorizontal();

		schoolInfoTv = (TextView) findViewById(R.id.school_detail_schoolinfo_tv);
		schoolInstructionTv = (TextView) findViewById(R.id.school_detail_schoolinstruction_tv);
		enrollBtn = (Button) findViewById(R.id.coach_detail_enroll_btn);
		schoolNameTv = (TextView) findViewById(R.id.school_detail_name_tv);
		schoolAddressTv = (TextView) findViewById(R.id.school_detail_place_tv);
		schoolRateTv = (TextView) findViewById(R.id.coach_detail_rate_tv);
		workTimeTv = (TextView) findViewById(R.id.school_detail_weektime_tv);
		schoolInTv = (TextView) findViewById(R.id.coach_detail_introduction_tv);
		addDeleteSchoolCk = (CheckBox) findViewById(R.id.school_detail_collection_ck);
		noCoahTv = (TextView) findViewById(R.id.select_coach_horizon_no_tv);

		noCoahTv.setVisibility(View.VISIBLE);
		horizontalListView.setVisibility(View.GONE);

		schoolInfoTv.getPaint().setFakeBoldText(true);
		schoolInstructionTv.getPaint().setFakeBoldText(true);

		school = (SchoolVO) getIntent().getSerializableExtra("school");

		if (app.userVO == null) {
			enrollBtn.setVisibility(View.GONE);
			addDeleteSchoolCk.setEnabled(false);
		} else {
			enrollBtn.setVisibility(View.VISIBLE);
		}

		if (app.favouriteSchool != null) {
			if (app.favouriteSchool.contains(school)) {
				addDeleteSchoolCk.setChecked(true);
			} else {
				addDeleteSchoolCk.setChecked(false);
			}
		}

		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) adLayout
				.getLayoutParams();
		headParams.width = screenWidth;
		headParams.height = (int) (screenWidth * 2 / 3f);
		viewPagerHeight = headParams.height;
		setViewPager();
	}

	private void setListener() {
		schoolAddressTv.setOnClickListener(this);
		enrollBtn.setOnClickListener(this);
		viewPager.setPageChangeListener(this);
		horizontalListView.setLoadMoreListener(this);
		addDeleteSchoolCk.setOnCheckedChangeListener(this);
	}

	private void setData() {
		if (school != null) {
			adImageUrl = school.getPictures();
			setViewPager();
			schoolNameTv.setText(school.getName());
			schoolAddressTv.setText(school.getAddress());
			schoolRateTv.setText(school.getPassingrate() + "%");
			workTimeTv.setText(school.getHours());
			schoolInTv.setText(school.getIntroduction());
		}
	}

	private void obtainEnrollSchoolDetail() {
		HttpSendUtils.httpGetSend(schoolType, this, Config.IP
				+ "api/v1/driveschool/getschoolinfo/" + school.getSchoolid());
	}

	private void setViewPager() {
		InfinitePagerAdapter adapter = null;
		int length = 0;
		if (adImageUrl != null && adImageUrl.length > 0) {
			adapter = new InfinitePagerAdapter(this, adImageUrl, screenWidth,
					viewPagerHeight);
			length = adImageUrl.length;
		} else {
			adapter = new InfinitePagerAdapter(this,
					new int[] { R.drawable.defaultimage });
			length = 1;
		}
		adapter.setPageClickListener(new MyPageClickListener());
		adapter.setURLErrorListener(this);
		viewPager.setAdapter(adapter);

		imageViews = new ImageView[length];
		ImageView imageView = null;
		dotLayout.removeAllViews();
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				(int) (8 * screenDensity), (int) (4 * screenDensity));
		dotLayout.addView(new TextView(this), textParams);
		// 添加小圆点的图片
		for (int i = 0; i < length; i++) {
			imageView = new ImageView(this);
			// 设置小圆点imageview的参数
			imageView.setLayoutParams(new LayoutParams(
					(int) (16 * screenDensity), (int) (4 * screenDensity)));// 创建一个宽高均为20
			// 的布局
			// 将小圆点layout添加到数组中
			imageViews[i] = imageView;

			// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
			if (i == 0) {
				imageViews[i].setBackgroundColor(Color.parseColor("#21b8c6"));
			} else {
				imageViews[i].setBackgroundColor(Color.parseColor("#eeeeee"));
			}
			// 将imageviews添加到小圆点视图组
			dotLayout.addView(imageViews[i]);
			dotLayout.addView(new TextView(this), textParams);
		}
	}

	private class MyPageClickListener implements PageClickListener {

		@Override
		public void onPageClick(int position) {
			try {
				if (adList != null && adList.size() > position) {
					String url = adList.get(position).getHeadportrait()
							.getOriginalpic();
					if (!TextUtils.isEmpty(url)) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri content_url = Uri.parse(url);
						intent.setData(content_url);
						startActivity(intent);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void doException(String type, Exception e, int code) {
		if (coach.equals(type))
			horizontalListView.setLoadMoreCompleted();
		super.doException(type, e, code);
	}

	@Override
	public void doTimeOut(String type) {
		if (coach.equals(type))
			horizontalListView.setLoadMoreCompleted();
		super.doTimeOut(type);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(schoolType)) {
				if (data != null) {
					school = JSONUtil.toJavaBean(SchoolVO.class, data);
					obtainHeadLineNews();
					setData();
				}
			} else if (type.equals(coach)) {
				if (dataArray != null) {
					int length = dataArray.length();
					if (length > 0) {
						coachPage++;
						noCoahTv.setVisibility(View.GONE);
						horizontalListView.setVisibility(View.VISIBLE);
					}
					if (coachList == null)
						coachList = new ArrayList<CoachVO>();
					for (int i = 0; i < length; i++) {
						CoachVO coachVO = JSONUtil.toJavaBean(CoachVO.class,
								dataArray.getJSONObject(i));
						coachList.add(coachVO);
					}
					if (adapter == null) {
						adapter = new SchoolDetailCoachHoriListAdapter(this,
								coachList);
					} else {
						adapter.setData(coachList);
					}
					horizontalListView.setAdapter(adapter);
					horizontalListView.setLoadMoreCompleted();
				}
			} else if (type.equals(headLineNews)) {
				if (dataArray != null) {
					int length = dataArray.length();
					adList = new ArrayList<HeadLineNewsVO>();
					if (length > 0) {
						adImageUrl = new String[length];
					}
					for (int i = 0; i < length; i++) {
						HeadLineNewsVO headLineNewsVO = JSONUtil.toJavaBean(
								HeadLineNewsVO.class,
								dataArray.getJSONObject(i));
						adList.add(headLineNewsVO);
						adImageUrl[i] = headLineNewsVO.getHeadportrait()
								.getOriginalpic();
					}
					if (length > 0) {
						setViewPager();
					}
				}
				obtainSchoolCoach(coachPage);
			} else if (type.equals(addSchool)) {
				if (!app.favouriteSchool.contains(school)) {
					app.favouriteSchool.add(school);
					sendBroadcast(new Intent(MyFavouriteActiviy.class.getName())
							.putExtra("isRefresh", true).putExtra(
									"activityName",
									FavouriteSchoolActivity.class.getName()));
				}
			} else if (type.equals(deleteSchool)) {
				if (app.favouriteSchool.contains(school)) {
					app.favouriteSchool.remove(school);
					sendBroadcast(new Intent(MyFavouriteActiviy.class.getName())
							.putExtra("isRefresh", true).putExtra(
									"activityName",
									FavouriteSchoolActivity.class.getName()));
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
			setResult(v.getId(), getIntent());
			finish();
			break;
		case R.id.base_right_btn:
			try {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + school.getPhone()));
				startActivity(phoneIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.coach_detail_enroll_btn:
			if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_NONE.getValue())) {
				String checkResult = Util.isConfilctEnroll(school);
				if (checkResult == null) {
					intent = new Intent();
					intent.putExtra("school", school);
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					setResult(RESULT_OK, intent);
					finish();
				} else if (checkResult.length() == 0) {
					app.selectEnrollSchool = school;
					Util.updateEnrollSchool(this, school, false);
					intent = new Intent();
					intent.putExtra("school", school);
					intent.putExtra("activityName",
							SubjectEnrollActivity.class.getName());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					// 提示
					EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
							this, checkResult);
					dialog.show();
				}
			} else if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue())) {
				intent = new Intent(this, AppointmentCarActivity.class);
				startActivity(intent);
			} else if (app.userVO.getApplystate().equals(
					EnrollResult.SUBJECT_ENROLLING.getValue())) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this)
						.dismissWithFailure("正在报名中，请等待审核");
			}
			break;
		case R.id.school_detail_place_tv:
			intent = new Intent(this, MapActivity.class);
			intent.putExtra("longtitude", school.getLongitude());
			intent.putExtra("latitude", school.getLatitude());
			intent.putExtra("title", school.getName());
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onPageChanged(int position) {
		if (imageViews != null) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position].setBackgroundColor(Color
						.parseColor("#21b8c6"));
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (position != i) {
					imageViews[i].setBackgroundColor(Color
							.parseColor("#eeeeee"));
				}
			}
		}
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					data.putExtra("school", school);
					setResult(RESULT_OK, data);
					finish();
				}
			};
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());

		String url = Config.IP + "api/v1/userinfo/favoriteschool/"
				+ school.getSchoolid();
		if (isChecked) {
			HttpSendUtils.httpPutSend(addSchool, this, url, null, 10000,
					headerMap);
		} else {
			HttpSendUtils.httpDeleteSend(deleteSchool, this, url, null, 10000,
					headerMap);
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
			app.selectEnrollSchool = school;
			Util.updateEnrollSchool(this, school, isFreshAll);
			if (isFreshAll) {
				app.selectEnrollCoach = Util.getEnrollUserSelectedCoach(this);
				app.selectEnrollCarStyle = Util
						.getEnrollUserSelectedCarStyle(this);
				app.selectEnrollClass = Util.getEnrollUserSelectedClass(this);
			}
			Intent intent = new Intent();
			intent.putExtra("school", school);
			intent.putExtra("activityName",
					SubjectEnrollActivity.class.getName());
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void onItemClick(int position) {
		Intent intent = new Intent(this, CoachDetailActivity.class);
		CoachVO coachVO = adapter.getItem(position);
		intent.putExtra("coach", coachVO);
		startActivityForResult(intent, horizontalListView.getId());
	}

	@Override
	public void onLoadMore() {
		obtainSchoolCoach(coachPage);
	}
}
