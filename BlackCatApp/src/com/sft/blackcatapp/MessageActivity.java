package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.sqlhelper.DBHelper;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.emchat.ChatAllHistoryAdapter;
import com.sft.vo.PushInnerVO;

/**
 * 消息界面
 * 
 */
public class MessageActivity extends BaseActivity implements
		OnItemClickListener, EMEventListener {

	private ListView messageListView;
	private ChatAllHistoryAdapter adapter;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();
	private RelativeLayout mes_bg;
	private RelativeLayout error_rl;
	private TextView error_tv;
	private TextView error_tvs;
	private ImageView error_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_message);
		initView();
	}

	@Override
	protected void onResume() {
		refresh();
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {

		setTitleText("消息");

		messageListView = (ListView) findViewById(R.id.message_list);

		mes_bg = (RelativeLayout) findViewById(R.id.mes_bg);

		error_iv = (ImageView) findViewById(R.id.error_iv);
		error_rl = (RelativeLayout) findViewById(R.id.error_rl);
		error_tv = (TextView) findViewById(R.id.error_tv);
		error_tvs = (TextView) findViewById(R.id.error_tvs);
		error_rl.setVisibility(View.GONE);
		messageListView.setVisibility(View.VISIBLE);
		messageListView.setOnItemClickListener(this);

		conversationList.addAll(loadConversationsWithRecentChat());

		adapter = new ChatAllHistoryAdapter(this, conversationList);
		// 设置adapter
		messageListView.setAdapter(adapter);

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

	/**
	 * 刷新页面
	 */
	public void refresh() {
		util.print("refresh");
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}

		PushInnerVO lastPushVO = null;
		// 获取系统消息
		List<PushInnerVO> systemList = DBHelper.getInstance(this).query(
				PushInnerVO.class, 1);
		if (systemList != null && systemList.size() > 0) {
			lastPushVO = systemList.get(0);
		}
		if (lastPushVO != null) {
			EMConversation systemConvesation = new EMConversation(
					Config.SYSTEM_PUSH);
			conversationList.add(0, systemConvesation);
		}
		if (list.size() <= 0) {
			// mes_bg.setBackgroundResource(R.drawable.mes_bg);
			error_rl.setVisibility(View.VISIBLE);
			error_iv.setImageResource(R.drawable.image_xiaoxi);
			messageListView.setVisibility(View.GONE);
			error_tv.setText("没有找到您的聊天信息");
			error_tvs.setVisibility(View.VISIBLE);
			error_tvs.setText("您可以通过预约教练和相邻时段学员发起聊天");
		}

		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;

						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		EMConversation conversation = adapter.getItem(position);
		String username = conversation.getUserName();
		if (username.equals(Config.SYSTEM_PUSH)) {
			Intent intent = new Intent(this, SystemPushActivity.class);
			startActivity(intent);
			return;
		}
		if (username.equals(app.userVO.getUserid()))
			toast.setText(getString(R.string.Cant_chat_with_yourself));
		else {
			// 进入聊天页面
			Intent intent = new Intent(this, ChatActivity.class);
			if (conversation.isGroup()) {
				if (conversation.getType() == EMConversationType.ChatRoom) {
					// it is group chat
					intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
					intent.putExtra("groupId", username);
				} else {
					// it is group chat
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
					intent.putExtra("groupId", username);
				}
			} else {
				// it is single chat
				EMMessage lastMessage = conversation.getLastMessage();
				if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
					String name = lastMessage.getStringAttribute(
							Config.CHAT_NICK_NAME, "陌生人");
					intent.putExtra("chatName", name);
					intent.putExtra("chatUrl", lastMessage.getStringAttribute(
							Config.CHAT_HEAD_RUL, ""));
					intent.putExtra("chatId", username);
				} else {
					String name = lastMessage.getStringAttribute(
							Config.CHAT_NICK_NAME_NOANSWER, "陌生人");
					intent.putExtra("chatName", name);
					intent.putExtra("chatUrl", lastMessage.getStringAttribute(
							Config.CHAT_HEAD_RUL_NOANSWER, ""));
					intent.putExtra("chatId", username);
					intent.putExtra("userIdNoAnswer",
							lastMessage.getStringAttribute(
									Config.CHAT_USERID_NOANSWER, ""));
					intent.putExtra("userTypeNoAnswer", lastMessage
							.getStringAttribute(Config.CHAT_USERTYPE_NOANSWER,
									""));
				}
			}
			startActivity(intent);
		}
	}

	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});

			break;
		}
		case EventDeliveryAck: {
			// 获取到message
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
			break;
		}
		case EventReadAck: {
			// 获取到message
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
			break;
		}
		case EventOfflineMessage: {
			refresh();
			break;
		}
		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		EMChatManager.getInstance().unregisterEventListener(this);
		super.onPause();
	}

	@Override
	public void forOperResult(Intent intent) {
		util.print("forOperResult");
		if (intent.getBooleanExtra("refresh", false)) {
			util.print("refresh");
			refresh();
		}
	}
}
