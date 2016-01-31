package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.blackcatapp.R;
import com.sft.util.DateUtil;
import com.sft.vo.ActivitiesVO;

public class ActivityAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<ActivitiesVO> mData;
	private List<Boolean> isSelected = new ArrayList<Boolean>();

	private int index = -1;

	public ActivityAdapter(Context context, List<ActivitiesVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
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

	public void setData(List<ActivitiesVO> mData) {
		this.mData = mData;
	}

	public List<ActivitiesVO> getData() {
		return mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public ActivitiesVO getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ActivitiesVO activitiesVO = mData.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.select_activity_list_item,
					null);
			holder = new ViewHolder();
			holder.web = (ImageView) convertView
					.findViewById(R.id.activity_web);
			holder.time = (TextView) convertView
					.findViewById(R.id.textView_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String activityUrl = activitiesVO.getTitleimg();
		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) holder.web
				.getLayoutParams();

		if (TextUtils.isEmpty(activityUrl)) {
			holder.web.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(activityUrl, holder.web,
					headParams.width, headParams.height);
		}
		holder.time.setText("截止时间"
				+ DateUtil.parseTime(activitiesVO.getEnddate()));

		return convertView;
	}

	private class ViewHolder {
		public ImageView web;
		public TextView time;
	}
}
