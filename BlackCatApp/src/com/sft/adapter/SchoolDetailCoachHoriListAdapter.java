package com.sft.adapter;

import java.util.List;

import com.sft.blackcatapp.R;
import com.sft.vo.CoachVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.pull.OnItemClickListener;

@SuppressLint("InflateParams")
public class SchoolDetailCoachHoriListAdapter
		extends RecyclerView.Adapter<SchoolDetailCoachHoriListAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	private List<CoachVO> mData;
	private boolean isLoadMore = false;
	private Context context;

	public SchoolDetailCoachHoriListAdapter(Context context, List<CoachVO> values) {
		this.mData = values;
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public boolean isLoadMore() {
		return isLoadMore;
	}

	public void setData(List<CoachVO> mData) {
		this.mData = mData;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View itemView) {
			super(itemView);
			image = (ImageView) itemView.findViewById(R.id.select_coach_hori_headpic_im);
			name = (TextView) itemView.findViewById(R.id.select_coach_hori_name_tv);
		}

		private TextView name;
		private ImageView image;
	}

	public CoachVO getItem(int position) {
		return mData.get(position);
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		
		holder.itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (context instanceof OnItemClickListener) {
					OnItemClickListener listener = (OnItemClickListener) context;
					listener.onItemClick(position);
				}
			}
		});
		
		RelativeLayout.LayoutParams headParam = (LayoutParams) holder.image.getLayoutParams();

		String url = getItem(position).getHeadportrait().getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.image.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.image, headParam.width, headParam.height);
		}

		holder.name.setText(getItem(position).getName());

		if (position == mData.size() - 1) {
			isLoadMore = true;
		} else {
			isLoadMore = false;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = mInflater.inflate(R.layout.select_coach_horizontal_list_item, null);
		return new ViewHolder(view);
	}

}
