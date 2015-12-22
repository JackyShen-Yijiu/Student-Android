package com.sft.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sft.blackcatapp.R;

public class MyAdapter extends BaseAdapter {

	private List<HashMap<String, String>> list = null;
	private Context mContext;
	private int[] icons;

	public MyAdapter(Context mContext, List<HashMap<String, String>> list,
			int[] icons) {
		this.mContext = mContext;
		this.list = list;
		this.icons = icons;
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = View.inflate(mContext, R.layout.drawer_list_item, null);
			// view = LayoutInflater.from(mContext).inflate(
			// R.layout.drawer_list_item, null);
			viewHolder.title_tv = (TextView) view.findViewById(R.id.title_tv);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		HashMap<String, String> map = list.get(position);
		if (map.containsKey("item")) {
			String item = map.get("item");
			viewHolder.title_tv.setText(item);
			Drawable left = mContext.getResources()
					.getDrawable(icons[position]);
			viewHolder.title_tv.setCompoundDrawablesWithIntrinsicBounds(left,
					null, null, null);
			viewHolder.title_tv.setCompoundDrawablePadding(20);
		}
		return view;

	}

	final static class ViewHolder {
		TextView title_tv;
	}
}