package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.blackcatapp.R;
import com.sft.vo.SchoolVO;

@SuppressLint("InflateParams")
public class SchoolListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<SchoolVO> mData;
	private boolean[] isSelected;

	private int index = -1;

	public SchoolListAdapter(Context context, List<SchoolVO> mData) {
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

	public void setData(List<SchoolVO> mData) {
		this.mData = mData;
	}

	public List<SchoolVO> getData() {
		return mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public SchoolVO getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.select_school_list_item,
					null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.select_school_coachname_tv);
			holder.headPic = (ImageView) convertView
					.findViewById(R.id.select_school_headpin_im);
			holder.location = (TextView) convertView
					.findViewById(R.id.select_school_location_tv);
			holder.rate = (RatingBar) convertView
					.findViewById(R.id.select_school_level_rb);
			holder.distance = (TextView) convertView
					.findViewById(R.id.select_school_distance_tv);
			holder.price = (TextView) convertView
					.findViewById(R.id.select_school_price_tv);
			holder.selectIm = (ImageView) convertView
					.findViewById(R.id.select_school_im);
			holder.coachAndComment = (TextView) convertView
					.findViewById(R.id.select_school_comment_coach_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position >= isSelected.length) {
			return convertView;
		}
		if (isSelected[position]) {
			holder.selectIm
					.setBackgroundResource(R.drawable.select_class_ck_selected);
		} else {
			holder.selectIm.setBackgroundResource(android.R.color.transparent);
		}

		String name = mData.get(position).getName();
		holder.name.setText(name);
		String location = mData.get(position).getAddress();
		holder.location.setText(location);
		// TODO 这里要改
		String level = mData.get(position).getSchoollevel();
		if (level != null && level.length() == 1) {

			holder.rate.setRating(Integer.parseInt(level));
		}
		String distance = mData.get(position).getDistance();
		holder.distance.setText(distance + "km");
		String price = mData.get(position).getPrice();
		holder.price.setText(price);

		holder.coachAndComment.setText(mData.get(position).getCommentcount()
				+ "条评论|" + mData.get(position).getCoachcount() + "名认证教练");
		String headUrl = mData.get(position).getLogoimg().getOriginalpic();

		LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) holder.headPic
				.getLayoutParams();

		if (TextUtils.isEmpty(headUrl)) {
			holder.headPic.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(headUrl, holder.headPic,
					headParams.width, headParams.height);
		}

		return convertView;
	}

	private class ViewHolder {
		public ImageView selectIm;
		public TextView name;
		public ImageView headPic;
		public TextView location;
		public RatingBar rate;
		public TextView distance;
		public TextView price;
		public TextView coachAndComment;
	}
}
