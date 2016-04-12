package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;

import com.jzjf.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.adapter.MallProductAdapter;
import com.sft.api.ApiHttpClient;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.view.RefreshLayout;
import com.sft.view.RefreshLayout.OnLoadListener;
import com.sft.viewutil.ZProgressHUD;
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
		BitMapURLExcepteionListner, OnItemClickListener, OnRefreshListener,
		OnLoadListener {

	private static final String headlineNews = "headlineNews";
	//
	// private String[] adImageUrl;
	// private LinearLayout dotLayout;
	// private ImageView[] imageViews;
	// private InfiniteViewPager topViewPager;
	// private List<ProductVO> adList;
	// private ImageView defaultImage;
	// private int viewPagerHeight;
	// private RelativeLayout adLayout;
	//
	private RefreshLayout swipeLayout;
	private GridView productListView;
	private String producttype = "0";
	private String cityname;
	private MyCuponVO myCuponVO;
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;
	private List<ProductVO> mainList = new ArrayList<ProductVO>();
	private MallProductAdapter adapter;

	private int index = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mall);
		cityname = app.curCity;
		producttype = getIntent().getStringExtra("moneytype");

		myCuponVO = (MyCuponVO) getIntent().getSerializableExtra("myCupon");
		initView();
		ZProgressHUD.getInstance(this).setMessage("拼命加载中...");
		ZProgressHUD.getInstance(this).show();
		obtainMailProduct();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		productListView = (GridView) findViewById(R.id.mall_listview);
		swipeLayout = (RefreshLayout) findViewById(R.id.mall_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setBackgroundColor(getResources().getColor(R.color.white));

		productListView.setOnItemClickListener(this);

		errorRl = (RelativeLayout) findViewById(R.id.error_rl);
		errorIv = (ImageView) findViewById(R.id.error_iv);
		errorTv = (TextView) findViewById(R.id.error_tv);
		errorRl.setVisibility(View.GONE);
		swipeLayout.setVisibility(View.VISIBLE);
	}

	private void obtainMailProduct() {

		RequestParams paramMap = new RequestParams();
		paramMap.put("index", index + "");
		paramMap.put("count", "100");
		paramMap.put("producttype", producttype);
		paramMap.put("cityname", cityname);
		// HttpSendUtils.httpGetSend(headlineNews, this, Config.IP
		// + "api/v1/getmailproduct", paramMap);
		ApiHttpClient.get("getmailproduct", paramMap, handler);
	}

	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte) {
			String value = parseJson(paramArrayOfByte);
			ZProgressHUD.getInstance(MallActivity.this).dismiss();
			if (!TextUtils.isEmpty(msg)) {
				// 加载失败，弹出失败对话框
				ZProgressHUD.getInstance(MallActivity.this).show();
				ZProgressHUD.getInstance(MallActivity.this).dismissWithFailure(
						msg, 1000);
			} else {
				processSuccess(value);

			}
		}

		@Override
		public void onFailure(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte, Throwable paramThrowable) {
			ZProgressHUD.getInstance(MallActivity.this).dismiss();
			if (isRefreshing) {
				swipeLayout.setRefreshing(false);
				isRefreshing = false;
			}
			if (isLoadingMore) {
				swipeLayout.setLoading(false);
				isLoadingMore = false;
			}

			// 显示空白页
			errorRl.setVisibility(View.VISIBLE);
			swipeLayout.setVisibility(View.GONE);
			if (paramInt == 0) {
				errorIv.setBackgroundResource(R.drawable.app_no_wifi);
				errorTv.setText(CommonUtil.getString(MallActivity.this,
						R.string.no_wifi));
			} else {
				errorIv.setBackgroundResource(R.drawable.app_no_wifi);
				errorTv.setText(CommonUtil.getString(MallActivity.this,
						R.string.no_wifi));
			}

		}
	};
	private RelativeLayout errorRl;
	private ImageView errorIv;
	private TextView errorTv;

	private String parseJson(byte[] responseBody) {
		String value = null;
		JSONObject dataObject = null;
		JSONArray dataArray = null;
		String dataString = null;
		try {

			JSONObject jsonObject = new JSONObject(new String(responseBody));
			result = jsonObject.getString("type");
			msg = jsonObject.getString("msg");
			try {
				dataObject = jsonObject.getJSONObject("data");

			} catch (Exception e2) {
				try {
					dataArray = jsonObject.getJSONArray("data");
				} catch (Exception e3) {
					dataString = jsonObject.getString("data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (dataObject != null) {
			value = dataObject.toString();
		} else if (dataArray != null) {
			value = dataArray.toString();

		} else if (dataString != null) {
			value = dataString;
		}
		return value;
	}

	protected void processSuccess(String value) {

		if (value != null) {
			LogUtil.print(value);
			try {
				MallVO mallVO = JSONUtil.toJavaBean(MallVO.class, value);

				setData(mallVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setData(final MallVO mallVO) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (index == 1) {
					mainList.clear();
					mainList.addAll(mallVO.getMainlist());
					if (adapter == null) {

						adapter = new MallProductAdapter(MallActivity.this,
								mainList, screenWidth, producttype);
						productListView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
				} else {
					if (mallVO.getMainlist().size() == 0) {
						Toast("没有更多数据了");
						LogUtil.print("没有更多数据了");
					} else {
						mainList.addAll(mallVO.getMainlist());
						adapter.notifyDataSetChanged();
					}
				}

				if (isRefreshing) {
					swipeLayout.setRefreshing(false);
					isRefreshing = false;
				}
				if (isLoadingMore) {
					swipeLayout.setLoading(false);
					isLoadingMore = false;
				}
			}
		});
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

					setData(mallVO);
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

	@Override
	public void onRefresh() {
		isRefreshing = true;
		index = 1;
		obtainMailProduct();
	}

	@Override
	public void onLoad() {
		isLoadingMore = true;
		index++;
		obtainMailProduct();
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
