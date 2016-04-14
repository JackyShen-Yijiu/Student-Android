package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.util.LogUtil;
import com.sft.vo.ActSelecttVO;

public class DuihuanjuanAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ActSelecttVO> mData;

	public DuihuanjuanAdapter(Context context, List<ActSelecttVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		LogUtil.print("ssssssss222" + mData);
		LogUtil.print("ssssssss111" + mData.size());
	}

	public void setData(List<ActSelecttVO> mData) {
		this.mData = mData;

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
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.juan_list_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.detail_name);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(mData.get(position).getProductname());
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
	}

}