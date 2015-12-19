package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

import com.sft.adapter.SchoolListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.HeadLineNewsVO;
import com.sft.vo.SchoolVO;

/**
 * 选择驾校界面
 * 
 * @author Administrator
 * 
 */
public class EnrollSchoolActivity extends BaseActivity implements
		OnItemClickListener, OnSelectConfirmListener,
		BitMapURLExcepteionListner, PageChangeListener {

	private final static String nearBySchool = "nearBySchool";
	private static final String headlineNews = "headlineNews";
	// 学校列表
	private XListView schoolList;

	private SchoolVO selectSchool;
	//
	private SchoolListAdapter adapter;
	// 当前选择的学校
	private SchoolVO school;

	private Context mContext;

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
	private InfiniteViewPager topViewPager;
	/**
	 * 广告内容
	 */
	private List<HeadLineNewsVO> adList;
	/**
	 * 广告默认图片
	 */
	private ImageView defaultImage;

	private int viewPagerHeight;
	private RelativeLayout adLayout;

	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_school);
		mContext = this;
		initView();
		obtainHeadLineNews();
		setListener();
		obtainNearBySchool();
	}

	// 获取头部轮播图图片
	private void obtainHeadLineNews() {
		HttpSendUtils.httpGetSend(headlineNews, this, Config.IP
				+ "api/v1/info/headlinenews");

	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.select_school);

		schoolList = (XListView) findViewById(R.id.enroll_select_school_listview);
		schoolList.setPullRefreshEnable(false);
		schoolList.setPullLoadEnable(false);

		selectSchool = (SchoolVO) getIntent().getSerializableExtra("school");

		if (app.userVO != null
				&& app.userVO.getApplystate().equals(
						EnrollResult.SUBJECT_NONE.getValue())) {
			showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
			setText(0, R.string.finish);
		}

		View headerView = View.inflate(mContext, R.layout.enroll_school_header,
				null);

		schoolList.addHeaderView(headerView);
		adLayout = (RelativeLayout) findViewById(R.id.enroll_school_top_headpic_im);
		topViewPager = (InfiniteViewPager) findViewById(R.id.enroll_school_top_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.enroll_school_top_dotlayout);
		defaultImage = (ImageView) findViewById(R.id.enroll_school_top_defaultimage);

		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) adLayout
				.getLayoutParams();
		headParams.width = screenWidth;
		int height = (int) ((screenWidth - 16 * screenDensity) / 3
				+ (screenWidth - 12 * screenDensity) * 2 / 3 + statusbarHeight);
		height += (63 * screenDensity);

		headParams.height = screenHeight - height;
		viewPagerHeight = headParams.height;
		setViewPager();

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
			defaultImage.setVisibility(View.GONE);
		}
		adapter.setPageClickListener(new MyPageClickListener());
		adapter.setURLErrorListener(this);
		topViewPager.setAdapter(adapter);

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
					(int) (6 * screenDensity), (int) (6 * screenDensity)));// 创建一个宽高均为20
			// 的布局
			// 将小圆点layout添加到数组中
			imageView
					.setBackgroundResource(R.drawable.enroll_school_dot_selector);
			imageViews[i] = imageView;

			// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
			if (i == 0) {
				imageView.setEnabled(true);
			} else {
				imageView.setEnabled(false);
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

	private void setData(List<SchoolVO> school, int selectIndex) {
		if (selectIndex >= 0) {
			// 将已选择的驾校放在第一位
			school.add(0, school.get(selectIndex));
			school.remove(selectIndex + 1);
		}
		adapter = new SchoolListAdapter(this, school);
		if (selectIndex >= 0) {
			adapter.setSelected(0);
		}
		schoolList.setAdapter(adapter);
	}

	private void obtainNearBySchool() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "10000");
		paramMap.put("index", "1");
		paramMap.put("count", "10");
		HttpSendUtils.httpGetSend(nearBySchool, this, Config.IP
				+ "api/v1/searchschool", paramMap);
	}

	private void setListener() {
		schoolList.setOnItemClickListener(this);
		topViewPager.setPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			if (adapter == null || adapter.getIndex() < 0) {
				finish();
				break;
			}

			school = adapter.getItem(adapter.getIndex());
			String checkResult = Util.isConfilctEnroll(school);
			if (checkResult == null) {
				setResult(v.getId(), new Intent().putExtra("school", school));
				finish();
			} else if (checkResult.length() == 0) {
				app.selectEnrollSchool = school;
				Util.updateEnrollSchool(this, school, false);
				setResult(v.getId(), new Intent().putExtra("school", school));
				finish();
			} else {
				// 提示
				EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
						this, checkResult);
				dialog.show();
			}

			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, SchoolDetailActivity.class);
		SchoolVO schoolVO = adapter.getItem(position - 1);
		intent.putExtra("school", schoolVO);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, final int resultCode,
			final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				SchoolVO school = (SchoolVO) data
						.getSerializableExtra("school");
				if (app.userVO != null
						&& app.userVO.getApplystate().equals(
								EnrollResult.SUBJECT_NONE.getValue())
						&& school != null) {
					int position = adapter.getData().indexOf(school);
					adapter.setSelected(position);
					adapter.notifyDataSetChanged();
				}
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(resultCode, data);
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

		try {
			if (type.equals(nearBySchool)) {
				if (dataArray != null) {
					try {
						int selectIndex = -1;
						int length = dataArray.length();
						List<SchoolVO> schoolList = new ArrayList<SchoolVO>();
						for (int i = 0; i < length; i++) {
							SchoolVO schoolVO;
							schoolVO = JSONUtil.toJavaBean(SchoolVO.class,
									dataArray.getJSONObject(i));
							if (selectSchool != null) {
								if (selectSchool.getSchoolid().equals(
										schoolVO.getSchoolid())) {
									selectIndex = i;
								}
							}
							schoolList.add(schoolVO);
						}
						setData(schoolList, selectIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (type.equals(headlineNews)) {
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
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
			setResult(R.id.base_right_tv,
					new Intent().putExtra("school", school));
			finish();
		}
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public void onPageChanged(int position) {
		if (imageViews != null) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position].setEnabled(true);
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (position != i) {
					imageViews[i].setEnabled(false);
				}
			}
		}
	}

}
