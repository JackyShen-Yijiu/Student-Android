package com.sft.blackcatapp;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;

import com.google.gson.reflect.TypeToken;
import com.jzjf.app.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sft.adapter.ActivityAdapter;
import com.sft.api.ApiHttpClient;
import com.sft.dialog.BonusDialog;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.ActivitiesVO;

public class NewActivitysActivity extends BaseActivity implements
		OnItemClickListener, BitMapURLExcepteionListner {
	private static final String headlineNews = "headlineNews";
	private String cityname;
	private ListView listView_activitys;
	private List<ActivitiesVO> adList;
	private ActivityAdapter adapter;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activitys_list);
		mContext = this;
		cityname = app.curCity;
		LogUtil.print("gt" + cityname);
		obtainActivities();
		initView();
	}

	private void initView() {
		setTitleText(R.string.activitys);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.share);
		listView_activitys = (ListView) findViewById(R.id.listView_activitys);

		listView_activitys.setOnItemClickListener(this);
		leftBtn.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// Toast("click-->"+v);
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			Intent intent2 = new Intent(this, BonusDialog.class);
			startActivity(intent2);
			break;
		}
	}

	private void obtainActivities() {

		// System.out.println(app.curCity);
		RequestParams params = new RequestParams();
		params.put("cityname", app.curCity);
		ApiHttpClient.get("getactivity", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int paramInt,
							Header[] paramArrayOfHeader, byte[] paramArrayOfByte) {
						String value = parseJson(paramArrayOfByte);
						if (!TextUtils.isEmpty(msg)) {
							// 加载失败，弹出失败对话框
							Toast.makeText(mContext, msg, 0).show();
						} else {
							processSuccess(value);

						}
					}

					@Override
					public void onFailure(int paramInt,
							Header[] paramArrayOfHeader,
							byte[] paramArrayOfByte, Throwable paramThrowable) {

					}
				});
	}

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
				List<ActivitiesVO> activitiesList = (List<ActivitiesVO>) JSONUtil
						.parseJsonToList(value,
								new TypeToken<List<ActivitiesVO>>() {
								}.getType());

				LogUtil.print(value);
				adapter = new ActivityAdapter(mContext, activitiesList);
				listView_activitys.setAdapter(adapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView,
			int paramInt, long paramLong) {

		ActivitiesVO activitiesVO = (ActivitiesVO) paramAdapterView
				.getAdapter().getItem(paramInt);
		Intent intent = new Intent(this, WebViewActivitys.class);
		intent.putExtra("url", activitiesVO.getContenturl());
		startActivity(intent);
	}

}
