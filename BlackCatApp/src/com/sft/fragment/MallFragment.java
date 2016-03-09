package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sft.adapter.MallProductAdapter;
import com.sft.blackcatapp.MallActivity;
import com.sft.blackcatapp.ProductDetailActivity;
import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.MallVO;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductVO;

public class MallFragment extends BaseFragment implements
		BitMapURLExcepteionListner, OnItemClickListener, OnClickListener {
	private String producttype = "0";
	private static final String headlineNews = "headlineNews";
	private String cityname;
	private ListView productListView;
	private MyCuponVO myCuponVO;
	private List<ProductVO> adList;
	private Context mContext;
	private int screenWidth;
	private View line;
	private TextView couponsMall;
	private TextView integralMall;

	// private ArrayList<Fragment> fragments;
	private int line_width;
	private ViewPager viewPager;

	private LocalActivityManager activityManager = null;
	private Bundle savedInstanceState;
	private List<View> listViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mall, container,
				false);
		cityname = app.curCity;
		mContext = getActivity();
		initViews(rootView);
		setListener();
		// obtainHeadLineNews();
		// screenWidth = BaseUtils.getScreenWidth(mContext);
		return rootView;
	}

	private void initViews(View rootView) {
		// productListView = (ListView)
		// rootView.findViewById(R.id.mall_listview);
		// productListView.setOnItemClickListener(this);

		integralMall = (TextView) rootView.findViewById(R.id.integral_mall_tv);
		couponsMall = (TextView) rootView.findViewById(R.id.coupons_mall_tv);
		line = rootView.findViewById(R.id.mall_line);
		/*
		 * 在一个Activity的一部分中显示其他Activity”要用到LocalActivityManagerity
		 * 作用体现在manager获取View：manager.startActivity(String,
		 * Intent).getDecorView()
		 */
		activityManager = new LocalActivityManager((Activity) mContext, true);
		activityManager.dispatchCreate(savedInstanceState);

		// 加入2个子Activity
		Intent i1 = new Intent(mContext, MallActivity.class);
		i1.putExtra("moneytype", Config.MoneyType.INTEGRAL_RETURN.getValue());
		Intent i2 = new Intent(mContext, MallActivity.class);
		i2.putExtra("moneytype", Config.MoneyType.COIN_CERTIFICATE.getValue());

		listViews = new ArrayList<View>();
		listViews.add(activityManager.startActivity("integralMall", i1)
				.getDecorView());
		listViews.add(activityManager.startActivity("couponsMall", i2)
				.getDecorView());
		// fragments = new ArrayList<Fragment>();
		// fragments.add(new IntegralMallFragment());
		// fragments.add(new SubjectTwoFragment());

		line_width = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth()
				/ listViews.size();
		line.getLayoutParams().width = line_width;
		line.requestLayout();

		viewPager = (ViewPager) rootView
				.findViewById(R.id.fragment_mall_viewpager);
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup view, int position, Object arg2) {
				ViewPager pViewPager = ((ViewPager) view);
				pViewPager.removeView(listViews.get(position));
			}

			@Override
			public void finishUpdate(View arg0) {
			}

			@Override
			public int getCount() {
				return listViews.size();
			}

			@Override
			public Object instantiateItem(ViewGroup view, int position) {
				ViewPager pViewPager = ((ViewPager) view);
				pViewPager.addView(listViews.get(position));
				return listViews.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
			}

			@Override
			public Parcelable saveState() {
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
			}
		});
		// viewPager.setAdapter(new FragmentStatePagerAdapter(
		// getChildFragmentManager()) {
		//
		// @Override
		// public int getCount() {
		// return fragments.size();
		// }
		//
		// @Override
		// public Fragment getItem(int arg0) {
		// return fragments.get(arg0);
		// }
		// });
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				changeState(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				float tagerX = arg0 * line_width + arg2 / listViews.size();
				ViewPropertyAnimator.animate(line).translationX(tagerX)
						.setDuration(0);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void setListener() {
		integralMall.setOnClickListener(this);
		couponsMall.setOnClickListener(this);
	}

	private void changeState(int position) {
		integralMall.setTextColor(getResources().getColor(
				R.color.study_text_normal));
		couponsMall.setTextColor(getResources().getColor(
				R.color.study_text_normal));
		switch (position) {
		case 0:
			integralMall.setTextColor(getResources().getColor(
					R.color.study_text_selected));

			// ViewPropertyAnimator.animate(subjectOne).scaleX(1.2f)
			// .setDuration(200);
			// ViewPropertyAnimator.animate(subjectOne).scaleY(1.2f)
			// .setDuration(200);
			// ViewPropertyAnimator.animate(subjectTwo).scaleX(1.0f)
			// .setDuration(200);
			// ViewPropertyAnimator.animate(subjectTwo).scaleY(1.0f)
			// .setDuration(200);
			break;
		case 1:
			couponsMall.setTextColor(getResources().getColor(
					R.color.study_text_selected));

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void obtainHeadLineNews() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("index", "1");
		paramMap.put("count", "100");
		paramMap.put("producttype", producttype);
		// paramMap.put("cityname", cityname);
		HttpSendUtils.httpGetSend(headlineNews, this, Config.IP
				+ "api/v1/getmailproduct", paramMap);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ProductVO productVO = (ProductVO) parent.getAdapter().getItem(position);
		Intent intent = new Intent(mContext, ProductDetailActivity.class);
		intent.putExtra("product", productVO);

		if (Config.MoneyType.COIN_CERTIFICATE.getValue().equals(producttype)) {
			intent.putExtra("isCupon", true);
		}
		if (myCuponVO != null) {
			intent.putExtra("myCupon", myCuponVO);
		}
		startActivity(intent);
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(headlineNews)) {
				if (data != null) {
					MallVO mallVO = JSONUtil.toJavaBean(MallVO.class, data);
					adList = new ArrayList<ProductVO>();
					List<ProductVO> mainList = mallVO.getMainlist();
					if (mainList != null) {
						productListView.setAdapter(new MallProductAdapter(
								mContext, mainList, screenWidth, producttype));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.integral_mall_tv:
			viewPager.setCurrentItem(0);
			break;
		case R.id.coupons_mall_tv:
			viewPager.setCurrentItem(1);
			break;

		default:
			break;
		}
	}

}
