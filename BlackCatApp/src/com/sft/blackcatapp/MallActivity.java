package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;

import com.sft.adapter.MallProductAdapter;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.MallVO;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductVO;

/**
 * 商城
 * 
 * @author Administrator
 * 
 */
public class MallActivity extends BaseActivity implements
		BitMapURLExcepteionListner, OnItemClickListener {

	private static final String headlineNews = "headlineNews";
	//
	// private String[] adImageUrl;
	// private LinearLayout dotLayout;
	// private ImageView[] imageViews;
	// private InfiniteViewPager topViewPager;
	private List<ProductVO> adList;
	// private ImageView defaultImage;
	// private int viewPagerHeight;
	// private RelativeLayout adLayout;

	private ListView productListView;
	private String producttype = "0";
	private String cityname;
	private MyCuponVO myCuponVO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_mall);
		cityname = app.curCity;
		LogUtil.print(cityname);
		producttype = getIntent().getStringExtra("moneytype");
		myCuponVO = (MyCuponVO) getIntent().getSerializableExtra("myCupon");
		initView();
		obtainHeadLineNews();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		if (Config.MoneyType.INTEGRAL_RETURN.getValue().equals(producttype)) {

			setTitleText("积分商城");
		} else if (Config.MoneyType.COIN_CERTIFICATE.getValue().equals(
				producttype)) {

			setTitleText("兑换商城");
		}

		// adLayout = (RelativeLayout) findViewById(R.id.mall_top_headpic_im);
		// topViewPager = (InfiniteViewPager)
		// findViewById(R.id.mall_top_viewpager);
		// defaultImage = (ImageView) findViewById(R.id.mall_top_defaultimage);
		// dotLayout = (LinearLayout) findViewById(R.id.mall_top_dotlayout);

		productListView = (ListView) findViewById(R.id.mall_listview);

		// LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams)
		// adLayout
		// .getLayoutParams();
		// headParams.width = screenWidth;
		// int height = (int) ((screenWidth - 12 * screenDensity) / 3
		// + (screenWidth - 11 * screenDensity) * 2 / 3 + statusbarHeight);
		// height += (115 * screenDensity);
		//
		// headParams.height = screenHeight - height;
		// viewPagerHeight = headParams.height;
		// setViewPager();

		productListView.setOnItemClickListener(this);
		// topViewPager.setPageChangeListener(this);
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
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		}
	}

	// private void setViewPager() {
	// InfinitePagerAdapter adapter = null;
	// int length = 0;
	// if (adImageUrl != null && adImageUrl.length > 0) {
	// adapter = new InfinitePagerAdapter(this, adImageUrl, screenWidth,
	// viewPagerHeight);
	// length = adImageUrl.length;
	// } else {
	// adapter = new InfinitePagerAdapter(this,
	// new int[] { R.drawable.defaultimage });
	// length = 1;
	// defaultImage.setVisibility(View.GONE);
	// }
	// adapter.setPageClickListener(new MyPageClickListener());
	// adapter.setURLErrorListener(this);
	// topViewPager.setAdapter(adapter);
	//
	// imageViews = new ImageView[length];
	// ImageView imageView = null;
	// dotLayout.removeAllViews();
	// LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
	// (int) (8 * screenDensity), (int) (4 * screenDensity));
	// dotLayout.addView(new TextView(this), textParams);
	// // 添加小圆点的图片
	// for (int i = 0; i < length; i++) {
	// imageView = new ImageView(this);
	// // 设置小圆点imageview的参数
	// imageView.setLayoutParams(new LayoutParams(
	// (int) (16 * screenDensity), (int) (4 * screenDensity)));// 创建一个宽高均为20
	// // 的布局
	// // 将小圆点layout添加到数组中
	// imageViews[i] = imageView;
	//
	// // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
	// if (i == 0) {
	// imageViews[i].setBackgroundColor(Color.parseColor("#21b8c6"));
	// } else {
	// imageViews[i].setBackgroundColor(Color.parseColor("#eeeeee"));
	// }
	// // 将imageviews添加到小圆点视图组
	// dotLayout.addView(imageViews[i]);
	// dotLayout.addView(new TextView(this), textParams);
	// }
	// }

	// private class MyPageClickListener implements PageClickListener {
	//
	// @Override
	// public void onPageClick(int position) {
	// try {
	// if (adList != null && adList.size() > position) {
	// Intent intent = new Intent(MallActivity.this,
	// ProductDetailActivity.class);
	// intent.putExtra("product", adList.get(position));
	// startActivity(intent);
	// }
	// } catch (Exception e) {
	// }
	// }
	// }

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
					// List<ProductVO> topList = mallVO.getToplist();
					// if (topList != null) {
					// int length = topList.size();
					// if (length > 0) {
					// adImageUrl = new String[length];
					// for (int i = 0; i < length; i++) {
					// adImageUrl[i] = topList.get(i).getProductimg();
					// }
					// setViewPager();
					// }
					// }

					List<ProductVO> mainList = mallVO.getMainlist();
					if (mainList != null) {
						productListView.setAdapter(new MallProductAdapter(this,
								mainList, screenWidth, producttype));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ProductVO productVO = (ProductVO) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this, ProductDetailActivity.class);
		intent.putExtra("product", productVO);
		if (Config.MoneyType.COIN_CERTIFICATE.getValue().equals(producttype)) {
			intent.putExtra("isCupon", true);
		}
		if (myCuponVO != null) {
			intent.putExtra("myCupon", myCuponVO);
		}
		startActivity(intent);
	}

	// @Override
	// public void onPageChanged(int position) {
	// if (imageViews != null) {
	// for (int i = 0; i < imageViews.length; i++) {
	// imageViews[position].setBackgroundColor(Color
	// .parseColor("#21b8c6"));
	// // 不是当前选中的page，其小圆点设置为未选中的状态
	// if (position != i) {
	// imageViews[i].setBackgroundColor(Color
	// .parseColor("#eeeeee"));
	// }
	// }
	// }
	// }
}
