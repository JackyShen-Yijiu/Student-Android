package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;

import com.google.gson.reflect.TypeToken;
import com.jzjf.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.adapter.ConsultationAdapter;
import com.sft.api.ApiHttpClient;
import com.sft.dialog.CheckApplyDialog;
import com.sft.dialog.NoLoginDialog;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.view.RefreshLayout;
import com.sft.view.RefreshLayout.OnLoadListener;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.ConsultationVO;

/**
 * 咨询答疑
 * 
 * @author Administrator
 * 
 */
public class ConsultationlActivity extends BaseActivity implements
		BitMapURLExcepteionListner, OnItemClickListener, OnRefreshListener,
		OnLoadListener {

	private static final String headlineNews = "headlineNews";
	//
	//
	private RefreshLayout swipeLayout;
	private ListView consultationListView;
	private boolean isRefreshing = false;
	private boolean isLoadingMore = false;
	private List<ConsultationVO> consultationList = new ArrayList<ConsultationVO>();
	private ConsultationAdapter adapter;

	private int index = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_consultation);
		setTitleText(R.string.consulation);
		initView();
		ZProgressHUD.getInstance(this).setMessage("拼命加载中...");
		ZProgressHUD.getInstance(this).show();
		obtainConsultation();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		consultationListView = (ListView) findViewById(R.id.consutation_listview);
		swipeLayout = (RefreshLayout) findViewById(R.id.consutation_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setBackgroundColor(getResources().getColor(R.color.white));

		consultationListView.setOnItemClickListener(this);
		findViewById(R.id.consulation_to_ask).setOnClickListener(this);
		findViewById(R.id.consulation_to_call).setOnClickListener(this);
	}

	private void obtainConsultation() {

		RequestParams paramMap = new RequestParams();
		paramMap.put("index", index + "");
		ApiHttpClient.get("getuserconsult", paramMap, handler);
	}

	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte) {
			String value = parseJson(paramArrayOfByte);
			ZProgressHUD.getInstance(ConsultationlActivity.this).dismiss();
			if (!TextUtils.isEmpty(msg)) {
				// 加载失败，弹出失败对话框
				ZProgressHUD.getInstance(ConsultationlActivity.this).show();
				ZProgressHUD.getInstance(ConsultationlActivity.this)
						.dismissWithFailure(msg, 1000);
			} else {
				processSuccess(value);

			}
		}

		@Override
		public void onFailure(int paramInt, Header[] paramArrayOfHeader,
				byte[] paramArrayOfByte, Throwable paramThrowable) {

		}
	};

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
				// List<ConsultationVO> list = JSONUtil.parseJsonToList(value,
				// new TypeToken<List<ConsultationVO>>() {
				// }.getType());
				List<ConsultationVO> list = (List<ConsultationVO>) JSONUtil
						.parseJsonToList(value,
								new TypeToken<List<ConsultationVO>>() {
								}.getType());

				setData(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setData(final List<ConsultationVO> list) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (index == 1) {
					consultationList.clear();
					consultationList.addAll(list);
					if (adapter == null) {

						adapter = new ConsultationAdapter(
								ConsultationlActivity.this, consultationList,
								screenWidth);
						consultationListView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
				} else {
					if (list.size() == 0) {
						Toast("没有更多数据了");
						LogUtil.print("没有更多数据了");
					} else {
						consultationList.addAll(list);
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
		case R.id.consulation_to_ask:
			if (app.isLogin) {
				Intent intent = new Intent(this, ConsultationAskActivity.class);
				startActivity(intent);

			} else {
				NoLoginDialog dialog = new NoLoginDialog(this);
				dialog.show();
			}
			break;
		case R.id.consulation_to_call:
			CheckApplyDialog dialog = new CheckApplyDialog(this);
			dialog.setTextAndImage("呼叫", "400-101-6669", "取消",
					R.drawable.ic_question);
			dialog.showTitle("咨询电话");
			dialog.setListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:400-101-6669"));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			});
			dialog.show();

			break;
		}
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onRefresh() {
		isRefreshing = true;
		index = 1;
		obtainConsultation();
	}

	@Override
	public void onLoad() {
		isLoadingMore = true;
		index++;
		obtainConsultation();
	}

}
