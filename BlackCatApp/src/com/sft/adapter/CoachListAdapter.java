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
import com.sft.vo.CoachVO;

@SuppressLint("InflateParams")
public class CoachListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CoachVO> mData;
	private boolean[] isSelected;

	private int index = -1;

	public CoachListAdapter(Context context, List<CoachVO> mData) {
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

	public void setData(List<CoachVO> mData) {
		this.mData = mData;
	}

	public List<CoachVO> getData() {
		return mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public CoachVO getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.select_coach_list_item,
					null);
			holder = new ViewHolder();
			holder.coachName = (TextView) convertView
					.findViewById(R.id.select_coach_coachname_tv);
			holder.schoolName = (TextView) convertView
					.findViewById(R.id.select_coach_schoolname_tv);
			holder.headPic = (ImageView) convertView
					.findViewById(R.id.select_coach_headpin_im);
			holder.rateBar = (RatingBar) convertView
					.findViewById(R.id.select_coach_ratingBar);
			holder.shuttle = (TextView) convertView
					.findViewById(R.id.select_coach_shuttle);
			holder.general = (TextView) convertView
					.findViewById(R.id.select_coach_general);
			holder.rate = (TextView) convertView
					.findViewById(R.id.select_coach_rate_tv);
			holder.age = (TextView) convertView
					.findViewById(R.id.select_coach_age_tv);
			holder.distance = (TextView) convertView
					.findViewById(R.id.select_coach_distance_tv);
			holder.selectIm = (ImageView) convertView
					.findViewById(R.id.select_coach_im);
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
		if (mData.get(position).getIs_shuttle().equals("true")) {
			holder.shuttle.setVisibility(View.VISIBLE);
		} else {
			holder.shuttle.setVisibility(View.GONE);
		}
		if (mData.get(position).isGeneral()) {
			holder.general.setVisibility(View.VISIBLE);
		} else {
			holder.general.setVisibility(View.GONE);
		}
		String coachName = mData.get(position).getName();
		holder.coachName.setText(coachName);
		String schoolName = mData.get(position).getDriveschoolinfo().getName();
		holder.schoolName.setText(schoolName);
		String rateBar = mData.get(position).getStarlevel();
		try {
			holder.rateBar.setRating(Float.parseFloat(rateBar));
		} catch (Exception e) {
			holder.rateBar.setRating(0f);
		}
		String rate = mData.get(position).getPassrate();
		holder.rate.setText("通过率: " + rate);
		String age = mData.get(position).getSeniority();
		holder.age.setText("工作年限:" + age);
		String distance = mData.get(position).getDistance();
		holder.distance.setText(distance + "KM");

		LinearLayout.LayoutParams headParam = (LinearLayout.LayoutParams) holder.headPic
				.getLayoutParams();

		String url = mData.get(position).getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.headPic.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.headPic,
					headParam.width, headParam.height);
		}

		return convertView;
	}

	private class ViewHolder {
		public ImageView selectIm;
		public TextView coachName;
		public TextView schoolName;
		public ImageView headPic;
		public RatingBar rateBar;
		public TextView shuttle, general;
		public TextView rate, age, distance;
	}
}
