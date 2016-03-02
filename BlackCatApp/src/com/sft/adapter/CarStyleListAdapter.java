package com.sft.adapter;

import java.util.List;

import com.jzjf.app.R;
import com.sft.vo.CarModelVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class CarStyleListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CarModelVO> mData;
	private boolean[] selected;
	private ColorStateList selectColor;
	private ColorStateList noSelectColor;

	private int index;

	public CarStyleListAdapter(Context context, List<CarModelVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		Resources resource = context.getResources();
		selectColor = resource.getColorStateList(R.color.app_main_color);
		noSelectColor = resource.getColorStateList(R.color.select_carstyle_text_noselected);
		this.selected = new boolean[mData.size()];
		for (int i = 0; i < mData.size(); i++) {
			selected[i] = false;
		}
	}

	public void setSelected(int index) {
		if (index >= 0) {
			for (int i = 0; i < mData.size(); i++) {
				selected[i] = false;
			}
			selected[index] = true;
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
			convertView = mInflater.inflate(R.layout.select_carstyle_list_item, null);
			holder = new ViewHolder();
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.select_carstyle_ck);
			holder.style = (TextView) convertView.findViewById(R.id.select_carstyle_style);
			holder.name = (TextView) convertView.findViewById(R.id.select_carstyle_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position >= selected.length) {
			return convertView;
		}

		holder.checkBox.setOnCheckedChangeListener(null);
		holder.checkBox.setChecked(selected[position]);
		if (selected[position]) {
			holder.name.setTextColor(selectColor);
			holder.style.setTextColor(selectColor);
		} else {
			holder.name.setTextColor(noSelectColor);
			holder.style.setTextColor(noSelectColor);
		}

		String style = mData.get(position).getCode();
		holder.style.setText(style);
		String name = mData.get(position).getName();
		holder.name.setText(name);

		holder.checkBox.setOnCheckedChangeListener(new MyCheckedChangeListener(position));

		return convertView;
	}

	private class MyCheckedChangeListener implements OnCheckedChangeListener {

		private int position;

		public MyCheckedChangeListener(int position) {
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				for (int i = 0; i < mData.size(); i++) {
					selected[i] = false;
				}
				selected[position] = isChecked;
				index = position;
			}
			notifyDataSetChanged();
		}

	}

	private class ViewHolder {
		public CheckBox checkBox;
		public TextView style;
		public TextView name;
	}
}
