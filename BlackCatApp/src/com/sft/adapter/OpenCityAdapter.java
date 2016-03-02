package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.jzjf.app.R;
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
			RelativeLayout layout = new RelativeLayout(mContext);
			TextView textView = new TextView(mContext);
			RelativeLayout.LayoutParams params = new LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			params.bottomMargin = 25;
			params.topMargin = 25;
			layout.addView(textView, params);
			// textView.setGravity(Gravity.CENTER);
			// textView.setBackgroundResource(R.drawable.pop_window_bg);
			textView.setTextColor(CommonUtil.getColor(mContext,
					R.color.white));//default_text_color
			textView.setTextSize(14);
			textView.setId(position);

			ImageView imageView = new ImageView(mContext);
			imageView.setBackgroundColor(CommonUtil.getColor(mContext,
					R.color.white_pure));
			RelativeLayout.LayoutParams params1 = new LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params1.height = 1;
			params1.leftMargin = 10;
			params1.rightMargin = 10;
			params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
			layout.addView(imageView, params1);
			convertView = layout;
			holder.city = textView;
			if (position == openCityList.size() - 1) {
				imageView.setVisibility(View.INVISIBLE);
			}
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
