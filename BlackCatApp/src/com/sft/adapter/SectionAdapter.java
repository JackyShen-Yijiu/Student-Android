package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.util.LogUtil;
import com.sft.vo.questionbank.TitleVO;

public class SectionAdapter extends BaseAdapter {

	private List<TitleVO> list = new ArrayList<TitleVO>();
	private Context context;

	public SectionAdapter(Context context, List<TitleVO> list) {
		this.context = context;
		this.list = list;

	}

	public void setData(List<TitleVO> data1) {
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
			LogUtil.print("---" + context);
			convertView = View.inflate(context, R.layout.item_list_section,
					null);
			holder.data = (TextView) convertView
					.findViewById(R.id.section_title);
			holder.number = (TextView) convertView
					.findViewById(R.id.section_number);
			holder.numbers = (TextView) convertView
					.findViewById(R.id.subject_number);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.data.setText(list.get(position).getTitle());

		holder.number.setText((position + 1) + "");

		holder.numbers.setText("(" + list.get(position).getCount() + "题)");
		return convertView;
	}

	private class ViewHolder {
		public TextView data;
		public TextView number;
		public TextView numbers;
	}

}
