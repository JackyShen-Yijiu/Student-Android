package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.easemob.chat.EMConversation;
import com.sft.adapter.SystemPushAdapter;
import com.sft.common.Config;
import com.sft.vo.PushInnerVO;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;
import cn.sft.sqlhelper.DBHelper;

/**
 * 系统消息界面
 * 
 * @author Administrator
 *
 */
public class SystemPushActivity extends BaseActivity implements OnRefreshListener {

	private SwipeRefreshLayout freshLayout;
	private ListView infoListView;
	private SystemPushAdapter adapter;
	private List<PushInnerVO> messages = new ArrayList<PushInnerVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_system);
		initView();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		setTitleText("系统消息");
		EMConversation conversation = new EMConversation(Config.SYSTEM_PUSH);
		conversation.resetUnreadMsgCount();

		freshLayout = (SwipeRefreshLayout) findViewById(R.id.systempush_layout);
		infoListView = (ListView) findViewById(R.id.systempush_listview);

		messages = loadMoreMsgFromDB(0, size);
		adapter = new SystemPushAdapter(this, messages);
		infoListView.setAdapter(adapter);

		infoListView.setSelection(messages.size() - 1);

		freshLayout.setOnRefreshListener(this);
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

	private synchronized void refresh(boolean newMessage) {
		if (adapter == null)
			return;
		EMConversation conversation = new EMConversation(Config.SYSTEM_PUSH);
		conversation.resetUnreadMsgCount();
		if (newMessage) {
			List<PushInnerVO> list = DBHelper.getInstance(this).query(PushInnerVO.class, 1);
			messages.add(list.get(0));
		}
		adapter.setData(messages);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void forOperResult(Intent intent) {
		if (intent.getBooleanExtra("refresh", false)) {
			refresh(true);
			infoListView.setSelection(messages.size() - 1);
		}
	}

	private boolean haveMoreData = true;
	private int size = 10;

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (messages.size() == 0) {
					toast.setText(getString(R.string.no_more_messages));
					haveMoreData = false;
				}
				if (haveMoreData) {
					try {
						List<PushInnerVO> list = loadMoreMsgFromDB(messages.size(), size);
						messages.addAll(0, list);
						if (list.size() > 0) {
							refresh(false);
							infoListView.setSelection(list.size() - 1);
							if (list.size() != size) {
								haveMoreData = false;
							}
						} else {
							toast.setText(getString(R.string.no_more_messages));
							haveMoreData = false;
						}
						freshLayout.setRefreshing(false);
					} catch (Exception e1) {
						e1.printStackTrace();
						freshLayout.setRefreshing(false);
						return;
					}
				} else {
					toast.setText(getString(R.string.no_more_messages));
					freshLayout.setRefreshing(false);
				}
			}
		}, 1000);
	}

	private List<PushInnerVO> loadMoreMsgFromDB(int postition, int size) {
		List<PushInnerVO> list = DBHelper.getInstance(this).query(PushInnerVO.class);
		if (list == null || list.size() == 0) {
			return new ArrayList<PushInnerVO>();
		}
		Collections.reverse(list);
		int end = postition + size;
		end = end < list.size() ? end : list.size();
		List<PushInnerVO> tempList = list.subList(postition, end);
		Collections.reverse(tempList);
		return tempList;
	}
}
