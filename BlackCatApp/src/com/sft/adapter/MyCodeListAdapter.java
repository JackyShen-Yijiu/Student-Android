package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.adapter.SchoolDetailCourseFeeAdapter.MyClickListener;
import com.sft.vo.MyCodeVO;

public class MyCodeListAdapter extends BaseAdapter {
	private List<MyCodeVO> mData;
	private MyClickListener mListener = null;
	private LayoutInflater mInflater;
	private List<Boolean> isSelected = new ArrayList<Boolean>();
	private int index = -1;
	private Context mContext;

	public MyCodeListAdapter(Context context, List<MyCodeVO> mData,
			MyClickListener listener) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		mContext = context;
		mListener = listener;
		// isSelected = ;
		for (int i = 0; i < mData.size(); i++) {
			isSelected.add(false);
		}
	}

	public MyCodeListAdapter(Context context, List<MyCodeVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		mContext = context;
		// isSelected = ;
		for (int i = 0; i < mData.size(); i++) {
			isSelected.add(false);
		}
	}

	public void setSelected(int index) {
		if (index >= 0) {
			for (int i = 0; i < mData.size(); i++) {
				if (isSelected.size() - 1 > i) {
					isSelected.set(i, false);
				} else {
					isSelected.add(false);
				}
			}
			isSelected.set(index, true);
			this.index = index;
		}
	}

	public int getIndex() {
		return index;
	}

	public void setData(List<MyCodeVO> mData) {
		this.mData = mData;
	}

	public List<MyCodeVO> getData() {
		return mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public MyCodeVO getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.select_code_list_item,
					null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.code = (TextView) convertView.findViewById(R.id.tv_code);
			holder.date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.rl_code = (RelativeLayout) convertView
					.findViewById(R.id.rl_code);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(mData.get(position).getName());
		holder.code.setText(mData.get(position).getYcode());
		holder.date.setText(mData.get(position).getDate());

		holder.rl_code.setTag(position);
		if (mListener != null)
			holder.rl_code.setOnClickListener(mListener);
		return convertView;
	}

	private class ViewHolder {
		public RelativeLayout rl_code;
		public TextView name;
		public TextView code;
		public TextView date;
	}

}
