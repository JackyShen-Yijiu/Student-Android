package com.sft.adapter;

import java.util.List;

import com.sft.blackcatapp.R;
import com.sft.vo.commentvo.CommentUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.infinitescrollviewpager.CropRoundView;
import cn.sft.pull.OnItemClickListener;

@SuppressLint("InflateParams")
public class AppointmentDetailStudentHoriListAdapter
		extends RecyclerView.Adapter<AppointmentDetailStudentHoriListAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	private Context context;
	private List<CommentUser> mData;
	private boolean isLoadMore = false;

	public boolean isLoadMore() {
		return isLoadMore;
	}

	public AppointmentDetailStudentHoriListAdapter(Context context, List<CommentUser> values) {
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mData = values;
	}

	public void setData(List<CommentUser> values) {
		this.mData = values;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View itemView) {
			super(itemView);
			image = (CropRoundView) itemView.findViewById(R.id.appointment_detail_hori_headpic_im);
		}

		private CropRoundView image;
	}

	public CommentUser getItem(int position) {
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
		
		Drawable drawable = context.getResources().getDrawable(R.drawable.default_small_pic);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		holder.image.setLineWidth(0);
		holder.image.setBackgroundColor(Color.TRANSPARENT);
		holder.image.setBkground(bitmap);

		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();
		String url = mData.get(position).getHeadportrait().getOriginalpic();
		if (!TextUtils.isEmpty(url)) {
			BitmapManager.INSTANCE.loadBitmap(url, holder.image, headParams.width, headParams.height, 0);
		} else {
			holder.image.setBackgroundResource(R.drawable.default_small_pic);
		}

		if (position == mData.size() - 1) {
			isLoadMore = true;
		} else {
			isLoadMore = false;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = mInflater.inflate(R.layout.appointment_detail_horizontal_list_item, null);
		return new ViewHolder(view);
	}

}
