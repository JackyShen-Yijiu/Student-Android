package com.sft.adapter;

import java.util.ArrayList;
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
import com.sft.util.LogUtil;
import com.sft.vo.CoachVO;

@SuppressLint("InflateParams")
public class SchoolDetailCoachHoriListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CoachVO> mData;
	// private boolean[] isSelected;
	private List<Boolean> isSelected = new ArrayList<Boolean>();

	private int index = -1;

	public SchoolDetailCoachHoriListAdapter(Context context, List<CoachVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		// isSelected = ;
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
			convertView = mInflater.inflate(
					R.layout.activity_search_coach_list_item, null);
			holder = new ViewHolder();
			holder.coachName = (TextView) convertView
					.findViewById(R.id.search_coach_coachname_tv);
			holder.schoolName = (TextView) convertView
					.findViewById(R.id.search_coach_schoolname_tv);
			holder.headPic = (ImageView) convertView
					.findViewById(R.id.search_coach_headpin_im);
			holder.rateBar = (RatingBar) convertView
					.findViewById(R.id.search_coach_ratingBar);
			holder.shuttle = (TextView) convertView
					.findViewById(R.id.search_coach_shuttle);
			holder.general = (TextView) convertView
					.findViewById(R.id.search_coach_general);
			holder.teachAge = (TextView) convertView
					.findViewById(R.id.search_coach_age_tv);
			holder.price = (TextView) convertView
					.findViewById(R.id.search_coach_price_tv);
			holder.distance = (TextView) convertView
					.findViewById(R.id.search_coach_distance_tv);
			holder.selectIm = (ImageView) convertView
					.findViewById(R.id.search_coach_im);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// if (position >= isSelected.length) {
		// return convertView;
		// }
		// if (isSelected.size() - 1 < position) {
		//
		// }
		if (isSelected.size() > position && isSelected.get(position)) {
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
		holder.teachAge.setText("通过率: " + rate + "%");
		String age = mData.get(position).getSeniority();
		holder.teachAge.setText("驾龄:" + age + "年");
		String distance = mData.get(position).getDistance();
		int distanceInt = 0;
		try {
			distanceInt = Integer.parseInt(distance);
		} catch (NumberFormatException e) {
		}
		holder.distance.setText(distanceInt / 1000 + "KM");

		LinearLayout.LayoutParams headParam = (LinearLayout.LayoutParams) holder.headPic
				.getLayoutParams();

		String url = mData.get(position).getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.headPic.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.headPic,
					headParam.width, headParam.height);
		}
		holder.price.setText("¥" + mData.get(position).getMinprice() + "起");
		// if (position == mData.size() - 1) {
		LogUtil.print(mData.size() + "ssssssd===="
				+ mData.get(position).getName() + position);
		// }
		return convertView;
	}

	private class ViewHolder {
		public ImageView selectIm;
		public TextView coachName;
		public TextView schoolName;
		public ImageView headPic;
		public RatingBar rateBar;
		public TextView shuttle, general;
		public TextView teachAge, distance, price;
	}
}
