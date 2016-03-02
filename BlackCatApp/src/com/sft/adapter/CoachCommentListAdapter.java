package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.blackcatapp.CoachDetailActivity;
import com.jzjf.app.R;
import com.sft.util.UTC2LOC;
import com.sft.vo.CoachCommentVO;

@SuppressLint("InflateParams")
public class CoachCommentListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CoachCommentVO> mData;
	private Context context;

	public CoachCommentListAdapter(Context context, List<CoachCommentVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
	}

	public void setData(List<CoachCommentVO> mData) {
		this.mData = mData;
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
			convertView = mInflater.inflate(R.layout.coach_detail_list_item2,
					null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.coach_detail_comment_name_tv);
			holder.headPic = (ImageView) convertView
					.findViewById(R.id.coach_detail_comment_headpic_im);
			holder.time = (TextView) convertView
					.findViewById(R.id.coach_detail_comment_time_tv);
			holder.content = (TextView) convertView
					.findViewById(R.id.coach_detail_comment_content_tv);
			holder.classType = (TextView) convertView
					.findViewById(R.id.coach_detail_comment_classtype_tv);
			holder.rb = (RatingBar) convertView
					.findViewById(R.id.coach_detail_comment_rb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String name = mData.get(position).getUserid().getName();
		holder.name.setText(name);
		String time = UTC2LOC.instance.getDate(mData.get(position)
				.getFinishtime(), "yyyy-MM-dd");
		holder.time.setText(time);
		String content = mData.get(position).getComment().getCommentcontent();
		holder.content.setText(content);

		if (mData.get(position).getUserid() != null
				&& mData.get(position).getUserid().getApplyclasstypeinfo() != null) {
			holder.classType.setText(mData.get(position).getUserid()
					.getApplyclasstypeinfo().getName());
		}
		RelativeLayout.LayoutParams headParam = (android.widget.RelativeLayout.LayoutParams) holder.headPic
				.getLayoutParams();

		String url = mData.get(position).getUserid().getHeadportrait()
				.getOriginalpic();

		if (!TextUtils.isEmpty(url)) {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.headPic,
					headParam.width, headParam.height);
		} else {
			holder.headPic.setBackgroundResource(R.drawable.default_small_pic);
		}

		holder.headPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.sendBroadcast(new Intent(CoachDetailActivity.class
						.getName()).putExtra("position", position).putExtra(
						"showStudentInfo", true));
			}
		});
//		mData.get(position).
		
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
		public ImageView headPic;
		public TextView time;
		public TextView content;
		public TextView classType;
		public RatingBar rb;

	}
}
