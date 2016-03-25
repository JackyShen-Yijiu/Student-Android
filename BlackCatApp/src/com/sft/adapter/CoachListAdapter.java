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
import android.widget.ImageView.ScaleType;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.adapter.SchoolDetailCourseFeeAdapter.MyClickListener;
import com.sft.vo.CoachVO;
import com.sft.vo.commonvo.Subject;

@SuppressLint("InflateParams")
public class CoachListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CoachVO> mData;
	// private boolean[] isSelected;
	private List<Boolean> isSelected = new ArrayList<Boolean>();

	private int index = -1;
	private Context mContext;
	private MyClickListener mListener;

	public CoachListAdapter(Context context, List<CoachVO> mData,
			MyClickListener listener) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		mContext = context;
		mListener = listener;
		// isSelected = ;
		for (int i = 0; i < mData.size(); i++) {
			isSelected.add(false);
		}
	}

	public CoachListAdapter(Context context, List<CoachVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		mContext = context;
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
			convertView = mInflater.inflate(R.layout.select_coach_list_item,
					null);
			holder = new ViewHolder();
			holder.coachName = (TextView) convertView
					.findViewById(R.id.select_coach_coachname_tv);
			holder.subjects = (TextView) convertView
					.findViewById(R.id.select_coach_subjects_tv);
			holder.headPic = (SelectableRoundedImageView) convertView
					.findViewById(R.id.select_coach_headpin_im);
			holder.rateBar = (RatingBar) convertView
					.findViewById(R.id.select_coach_ratingBar);
			holder.comment = (TextView) convertView
					.findViewById(R.id.select_coach_comment_tv);
			holder.teachAge = (TextView) convertView
					.findViewById(R.id.select_coach_teacherage_tv);
			holder.passRate = (TextView) convertView
					.findViewById(R.id.select_coach_passrate_tv);
			holder.selectIm = (ImageView) convertView
					.findViewById(R.id.select_coach_im);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.headPic.setScaleType(ScaleType.CENTER_CROP);
		holder.headPic.setImageResource(R.drawable.login_head);
		holder.headPic.setOval(true);
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
		// if (mData.get(position).getIs_shuttle().equals("true")) {
		// holder.shuttle.setVisibility(View.VISIBLE);
		// } else {
		// holder.shuttle.setVisibility(View.GONE);
		// }
		// if (mData.get(position).isGeneral()) {
		// holder.general.setVisibility(View.VISIBLE);
		// } else {
		// holder.general.setVisibility(View.GONE);
		// }
		String coachName = mData.get(position).getName();
		holder.coachName.setText(coachName);
		String rateBar = mData.get(position).getStarlevel();
		try {
			holder.rateBar.setRating(Float.parseFloat(rateBar));
		} catch (Exception e) {
			holder.rateBar.setRating(0f);
		}
		String subjectString = "";
		for (Subject subject : mData.get(position).getSubject()) {
			subjectString = subject.getName() + " ";
		}
		holder.subjects.setText(subjectString);
		holder.passRate
				.setText("通过率" + mData.get(position).getPassrate() + "%");
		holder.comment.setText(mData.get(position).getCommentcount() + "条评论");

		if (mData.get(position).getSeniority().contains("年")) {
			holder.teachAge.setText(mData.get(position).getSeniority() + "教龄");
		} else {
			holder.teachAge.setText(mData.get(position).getSeniority() + "年教龄");
		}

		RelativeLayout.LayoutParams headParam = (RelativeLayout.LayoutParams) holder.headPic
				.getLayoutParams();

		String url = mData.get(position).getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.headPic.setBackgroundResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.headPic,
					headParam.width, headParam.height);
		}
		// if (position == mData.size() - 1) {
		// LogUtil.print(mData.size() + "ssssssd===="
		// + mData.get(position).getName() + position);
		// }

		holder.headPic.setTag(position);
		holder.headPic.setOnClickListener(mListener);
		return convertView;
	}

	private class ViewHolder {
		public ImageView selectIm;
		public SelectableRoundedImageView headPic;
		public TextView coachName;
		public RatingBar rateBar;
		public TextView subjects;
		public TextView passRate;
		public TextView comment;
		public TextView teachAge;
	}
}
