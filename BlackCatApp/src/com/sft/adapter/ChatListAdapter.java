package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.vo.ChatVO;

@SuppressLint("InflateParams")
public class ChatListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ChatVO> mData;

	public ChatListAdapter(Context context, List<ChatVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	public void addData(ChatVO chatVO) {
		mData.add(chatVO);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		String style = mData.get(position).getStyle();
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.chat_my_item, null);
			holder.otherContent = (TextView) convertView
					.findViewById(R.id.chat_other_content);
			holder.myContent = (TextView) convertView
					.findViewById(R.id.chat_my_content);
			holder.otherHeadPic = (ImageView) convertView
					.findViewById(R.id.chat_other_headpic);
			holder.myHeadPic = (ImageView) convertView
					.findViewById(R.id.chat_my_headpic);
			holder.date = (TextView) convertView.findViewById(R.id.chat_date);
			holder.myLayout = (RelativeLayout) convertView
					.findViewById(R.id.chat_my_layout);
			holder.otherLayout = (RelativeLayout) convertView
					.findViewById(R.id.chat_other_layout);
			holder.dateLayout = (RelativeLayout) convertView
					.findViewById(R.id.chat_date_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.myLayout.setVisibility(View.GONE);
		holder.otherLayout.setVisibility(View.GONE);
		holder.dateLayout.setVisibility(View.GONE);

		if (style.equals("0")) {
			holder.otherLayout.setVisibility(View.VISIBLE);
			holder.otherContent.setText(mData.get(position).getContent());
		} else if (style.equals("1")) {
			holder.myLayout.setVisibility(View.VISIBLE);
			holder.myContent.setText(mData.get(position).getContent());
		} else {
			holder.dateLayout.setVisibility(View.VISIBLE);
			holder.date.setText(style);
		}
		return convertView;
	}

	@SuppressWarnings("unused")
	private class ViewHolder {
		public TextView otherContent;
		public ImageView otherHeadPic;
		public TextView myContent;
		public ImageView myHeadPic;
		public TextView date;
		public RelativeLayout myLayout, otherLayout, dateLayout;
	}
}
