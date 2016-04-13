package com.sft.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;

import com.jzjf.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.adapter.MallProductAdapter;
import com.sft.api.ApiHttpClient;
import com.sft.blackcatapp.ProductDetailActivity;
import com.sft.common.Config;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.view.HeaderGridView;
import com.sft.view.RefreshLayout;
import com.sft.view.RefreshLayout.OnLoadListener;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.MallVO;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductVO;

public class MallFragment extends BaseFragment implements
		BitMapURLExcepteionListner, OnItemClickListener, OnRefreshListener,
		OnLoadListener, OnClickListener {
	private String cityname;
	private MyCuponVO myCuponVO;
	private Context mContext;

	private Bundle savedInstanceState;

	//
	private RefreshLayout swipeLayout;
	private HeaderGridView productListView;
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;
	private List<ProductVO> mainList = new ArrayList<ProductVO>();
	private MallProductAdapter adapter;

	private int index = 1;
	private String producttype;

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
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);

		screenWidth = wm.getDefaultDisplay().getWidth();
		producttype = "0";
		initViews(rootView);
		obtainMailProduct();
		return rootView;
	}

	private void initViews(View rootView) {
		ZProgressHUD.getInstance(mContext).setMessage("拼命加载中...");
		ZProgressHUD.getInstance(mContext).show();

		productListView = (HeaderGridView) rootView
				.findViewById(R.id.mall_listview);
		swipeLayout = (RefreshLayout) rootView
				.findViewById(R.id.mall_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setBackgroundColor(getResources().getColor(R.color.white));

		productListView.setOnItemClickListener(this);
		errorRl = (RelativeLayout) rootView.findViewById(R.id.error_rl);
		errorIv = (ImageView) rootView.findViewById(R.id.error_iv);
		errorTv = (TextView) rootView.findViewById(R.id.error_tv);
		errorRl.setVisibility(View.GONE);
		swipeLayout.setVisibility(View.VISIBLE);

		// 添加头部
		View view = View.inflate(mContext, R.layout.mall_header, null);
		productListView.addHeaderView(view);
		myIntegralTvTextView = (TextView) view
				.findViewById(R.id.mall_header_my_integral_tv);

		view.findViewById(R.id.mall_header_exchange_record_btn)
				.setOnClickListener(this);

		//
		if (app.currency != null) {
			myIntegralTvTextView.setText(app.currency);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void obtainMailProduct() {

		RequestParams paramMap = new RequestParams();
		paramMap.put("index", index + "");
		paramMap.put("count", "100");
		paramMap.put("producttype", "0");
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
			ZProgressHUD.getInstance(mContext).dismiss();
			if (!TextUtils.isEmpty(msg)) {
				// 加载失败，弹出失败对话框
				ZProgressHUD.getInstance(mContext).show();
				ZProgressHUD.getInstance(mContext)
						.dismissWithFailure(msg, 1000);
			} else {
				processSuccess(value);

			}
		}

		@Override
		public void onFailure(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte, Throwable paramThrowable) {
			ZProgressHUD.getInstance(mContext).dismiss();
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
				errorTv.setText(CommonUtil
						.getString(mContext, R.string.no_wifi));
			} else {
				errorIv.setBackgroundResource(R.drawable.app_no_wifi);
				errorTv.setText(CommonUtil
						.getString(mContext, R.string.no_wifi));
			}

		}
	};
	private RelativeLayout errorRl;
	private ImageView errorIv;
	private TextView errorTv;
	private int screenWidth;
	private TextView myIntegralTvTextView;

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
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (index == 1) {
					mainList.clear();
					mainList.addAll(mallVO.getMainlist());
					if (adapter == null) {

						adapter = new MallProductAdapter(mContext, mainList,
								screenWidth, producttype);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mall_header_exchange_record_btn:
			// 兑换记录
			break;

		default:
			break;
		}
	}
}
