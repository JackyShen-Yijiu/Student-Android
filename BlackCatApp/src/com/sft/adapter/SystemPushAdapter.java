package com.sft.adapter;

import java.util.List;

import com.sft.blackcatapp.R;
import com.sft.vo.PushInnerVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class SystemPushAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<PushInnerVO> mData;

	public SystemPushAdapter(Context context, List<PushInnerVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
	}

	public void setData(List<PushInnerVO> data) {
		this.mData = data;
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

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_received_message, null);
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(R.id.tv_chatcontent);
			holder.time = (TextView) convertView.findViewById(R.id.timestamp);
			holder.im = (ImageView) convertView.findViewById(R.id.iv_userhead);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.time.setVisibility(View.GONE);
		holder.im.setBackgroundResource(R.drawable.system_message_icon);
		holder.content.setText(mData.get(position).getMsg_content());

		return convertView;
	}

	private class ViewHolder {
		public ImageView im;
		public TextView content;
		public TextView time;
	}

}
