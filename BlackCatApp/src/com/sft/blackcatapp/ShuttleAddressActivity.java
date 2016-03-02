package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.sft.adapter.HistoryShuttleAddressListAdapter;
import com.sft.adapter.SuggestAddressListAdapter;
import com.jzjf.app.R;
import com.sft.viewutil.ClearEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 接送地址
 * 
 * @author Administrator
 * 
 */
public class ShuttleAddressActivity extends BaseActivity implements OnItemClickListener {

	//
	private TextView changeAddressTv;
	//
	private LinearLayout changeAddressLayout;
	//
	private ClearEditText changeAddreddEt;
	// 历史接送地址布局
	private LinearLayout listLayout;
	//
	private ListView historyShuttleAddressList;
	//
	private ListView suggestListView;
	//
	private SuggestAddressListAdapter adapter;
	//
	private SuggestionSearch mSuggestionSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_shuttle_address);
		initView();
		initData();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.shuttle_address);
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);

		changeAddressTv = (TextView) findViewById(R.id.shuttle_address_change_tv);
		changeAddreddEt = (ClearEditText) findViewById(R.id.shuttle_address_edittext);
		changeAddressLayout = (LinearLayout) findViewById(R.id.shuttle_address_et_layout);
		listLayout = (LinearLayout) findViewById(R.id.shuttle_address_list_layout);
		historyShuttleAddressList = (ListView) findViewById(R.id.shuttle_address_listview);
		suggestListView = (ListView) findViewById(R.id.shuttle_address_suggest_list);

		setText("", app.curCity);
	}

	private void initData() {
		if (app.userVO.getAddresslist() == null || app.userVO.getAddresslist().length == 0) {
			setHistoryListLayoutVisible(false);
		} else {
			setHistoryListLayoutVisible(true);
			HistoryShuttleAddressListAdapter adapter = new HistoryShuttleAddressListAdapter(this,
					Arrays.asList(app.userVO.getAddresslist()));
			historyShuttleAddressList.setAdapter(adapter);
		}
	}

	private void setHistoryListLayoutVisible(boolean b) {
		listLayout.setVisibility(b ? View.VISIBLE : View.GONE);
		suggestListView.setVisibility(!b ? View.VISIBLE : View.GONE);
	}

	private void setListener() {
		changeAddressTv.setOnClickListener(this);
		suggestListView.setOnItemClickListener(this);
		historyShuttleAddressList.setOnItemClickListener(this);

		changeAddreddEt.addTextChangedListener(new EditTextContentChangedListener());
	}

	private class EditTextContentChangedListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (s == null || s.length() == 0) {
				setHistoryListLayoutVisible(true);
			} else {
				setHistoryListLayoutVisible(false);
				// 联网搜索地址显示
				suggestSearch();
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

	}

	private void suggestSearch() {
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(listener);
		mSuggestionSearch.requestSuggestion(
				(new SuggestionSearchOption()).keyword(changeAddreddEt.getText().toString()).city(app.curCity));
	}

	private OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
		public void onGetSuggestionResult(SuggestionResult res) {
			if (res == null || res.getAllSuggestions() == null) {
				return;
				// 未找到相关结果
			}
			mSuggestionSearch.destroy();
			// 获取在线建议检索结果
			List<SuggestionInfo> list = res.getAllSuggestions();
			List<String> addressList = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				addressList.add(list.get(i).key);
			}
			if (adapter == null) {
				adapter = new SuggestAddressListAdapter(ShuttleAddressActivity.this, addressList);
			} else {
				adapter.setData(addressList);
			}
			suggestListView.setAdapter(adapter);
		}
	};

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
			break;
		case R.id.shuttle_address_change_tv:
			v.setVisibility(View.GONE);
			changeAddreddEt.requestFocus();
			changeAddressLayout.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		String address = (String) parent.getAdapter().getItem(position);
		intent.putExtra("address", address);
		setResult(RESULT_OK, intent);
		finish();
	}

}
