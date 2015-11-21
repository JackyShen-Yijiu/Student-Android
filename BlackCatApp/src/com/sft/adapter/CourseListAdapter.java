package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.blackcatapp.R;
import com.sft.vo.VideoVO;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class CourseListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<VideoVO> mData;
	private int width;

	public CourseListAdapter(Context context, List<VideoVO> mData, int width) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.width = width;
	}

	public void setData(List<VideoVO> list) {
		this.mData = list;
		notifyDataSetChanged();
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
			convertView = mInflater.inflate(R.layout.course_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.course_title_tv);
			holder.im = (ImageView) convertView
					.findViewById(R.id.course_item_im);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int height = (int) (width * 210 / 360f);
		ListView.LayoutParams param = new ListView.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, height);
		convertView.setLayoutParams(param);

		BitmapManager.INSTANCE.loadBitmap2(mData.get(position).getPictures(),
				holder.im, width, height);

		holder.title.setText(mData.get(position).getName());
		return convertView;
	}

	private class ViewHolder {
		public TextView title;
		public ImageView im;
	}
}
