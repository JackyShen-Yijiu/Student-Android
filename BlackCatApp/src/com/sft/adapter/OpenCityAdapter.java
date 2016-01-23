package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.util.CommonUtil;
import com.sft.vo.OpenCityVO;

public class OpenCityAdapter extends BaseAdapter {

	private List<OpenCityVO> openCityList;
	private Context mContext;

	public OpenCityAdapter(Context context, List<OpenCityVO> openCityList) {
		this.openCityList = openCityList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return openCityList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LinearLayout layout = new LinearLayout(mContext);
			TextView textView = new TextView(mContext);
			LinearLayout.LayoutParams params = new LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layout.addView(textView, params);
			// textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.drawable.pop_window_bg);
			textView.setTextColor(CommonUtil.getColor(mContext,
					R.color.default_text_color));
			textView.setTextSize(14);

			convertView = layout;
			holder.city = textView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.city.setText(openCityList.get(position).getName());
		return convertView;
	}

	private class ViewHolder {
		private TextView city;
	}

}
