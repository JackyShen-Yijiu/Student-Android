package com.sft.adapter;

import java.util.List;

import com.jzjf.app.R;
import com.sft.vo.ClassVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ClasslListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ClassVO> mData;

	private boolean[] isSelected;

	// 当前选择的下标
	private int index = -1;

	public ClasslListAdapter(Context context, List<ClassVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		isSelected = new boolean[mData.size()];
		for (int i = 0; i < mData.size(); i++) {
			isSelected[i] = false;
		}
	}

	public void setSelected(int index) {
		if (index >= 0) {
			for (int i = 0; i < mData.size(); i++) {
				isSelected[i] = false;
			}
			isSelected[index] = true;
			this.index = index;
		}
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public ClassVO getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.select_class_list_item, null);
			holder = new ViewHolder();
			holder.checkBox = (ImageView) convertView.findViewById(R.id.select_class_ck);
			holder.title = (TextView) convertView.findViewById(R.id.select_class_title_tv);
			holder.content = (TextView) convertView.findViewById(R.id.select_class_content_tv);
			holder.price = (TextView) convertView.findViewById(R.id.select_class_price_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position >= isSelected.length) {
			return convertView;
		}

		if (isSelected[position]) {
			holder.checkBox.setBackgroundResource(R.drawable.select_class_ck_selected);
		} else {
			holder.checkBox.setBackgroundResource(R.drawable.select_class_ck_noselect);
		}

		String title = mData.get(position).getClassname();
		holder.title.setText(title);
		String content = mData.get(position).getClassdesc();
		holder.content.setText(content);
		String price = mData.get(position).getPrice();
		holder.price.setText("¥ " + price);

		return convertView;
	}

	private class ViewHolder {
		public ImageView checkBox;
		public TextView title;
		public TextView content;
		public TextView price;
	}

}
