package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.questionbank.Chapter;

public class SectionAdapter extends BaseAdapter {

	private List<Chapter> list = new ArrayList<Chapter>();
	private Context context;

	public SectionAdapter(Context context, List<Chapter> list) {
		this.context = context;
		this.list = list;

	}

	public void setData(List<Chapter> data1) {
		this.list = data1;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_list_section,
					null);
			holder.data = (TextView) convertView
					.findViewById(R.id.section_title);
			holder.number = (TextView) convertView
					.findViewById(R.id.section_number);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.data.setText(list.get(position).getTitle());

		holder.number.setText(list.size() + "");
		return convertView;
	}

	private class ViewHolder {
		public TextView data;
		public TextView number;

	}

}
