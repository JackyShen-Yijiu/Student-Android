package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.SubjectVO;

public class SubjectListAdapter extends BaseAdapter {

	private Context context;
	private List<SubjectVO> subjects;
	private boolean[] isSelected;

	// 当前选择的下标
	private int index = -1;

	public SubjectListAdapter(Context context, List<SubjectVO> list) {
		this.context = context;
		this.subjects = list;
		isSelected = new boolean[subjects.size()];
		for (int i = 0; i < subjects.size(); i++) {
			isSelected[i] = false;
		}
	}

	public void setSelected(int index) {
		if (index >= 0) {
			for (int i = 0; i < subjects.size(); i++) {
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
		return subjects.size();
	}

	@Override
	public Object getItem(int paramInt) {
		return subjects.get(paramInt);
	}

	@Override
	public long getItemId(int paramInt) {
		return paramInt;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = View.inflate(context, R.layout.select_subject_list_item,
				null);
		TextView name = (TextView) convertView
				.findViewById(R.id.subject_item_name);
		ImageView checkBox = (ImageView) convertView
				.findViewById(R.id.select_subject_ck);

		if (position >= isSelected.length) {
			return convertView;
		}

		if (isSelected[position]) {
			checkBox.setBackgroundResource(R.drawable.select_class_ck_selected);
		} else {
			checkBox.setBackgroundResource(R.drawable.select_class_ck_noselect);
		}
		name.setText(subjects.get(position).getName());
		return convertView;
	}
}
