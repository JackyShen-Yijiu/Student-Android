package com.sft.adapter;

import java.util.List;

import com.sft.blackcatapp.R;
import com.sft.vo.CoachVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;
import cn.sft.pull.OnItemClickListener;

@SuppressLint("InflateParams")
public class AppointmentCarCoachHoriListAdapter
		extends RecyclerView.Adapter<AppointmentCarCoachHoriListAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	private static boolean[] selected;
	private List<CoachVO> mData;
	private int width;
	private Context context;

	public AppointmentCarCoachHoriListAdapter(Context context, List<CoachVO> values, int width) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.mData = values;
		this.width = width;
		if (selected == null || selected.length != values.size()) {
			selected = new boolean[values.size()];
			for (int i = 0; i < values.size(); i++) {
				selected[i] = false;
			}
		}
	}

	public void setData(List<CoachVO> values) {
		selected = new boolean[values.size()];
		for (int i = 0; i < values.size(); i++) {
			selected[i] = false;
		}
		this.mData = values;
	}

	public int getSelected() {
		int i = 0;
		for (; i < mData.size(); i++) {
			if (selected[i]) {
				break;
			}
		}
		return i;
	}

	public void setSelected(int position) {
		for (int i = 0; i < mData.size(); i++) {
			selected[i] = false;
		}
		selected[position] = true;
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

		RelativeLayout.LayoutParams ckBkgruondParams = (RelativeLayout.LayoutParams) holder.ck.getLayoutParams();
		ckBkgruondParams.width = this.width;
		holder.shuttle.setVisibility(mData.get(position).getIs_shuttle().equals("true") ? View.VISIBLE : View.GONE);
		if (mData.get(position).isGeneral()) {
			holder.general.setVisibility(View.VISIBLE);
		} else {
			holder.general.setVisibility(View.GONE);
		}
		holder.name.setText(mData.get(position).getName());
		holder.school.setText(mData.get(position).getDriveschoolinfo().getName());
		holder.ck.setBackgroundResource(selected[position] ? R.drawable.appointment_car_coach_hori_select_bkground
				: R.drawable.appointment_car_coach_hori_noselect_bkground);

		RelativeLayout.LayoutParams headParam = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();
		String url = mData.get(position).getHeadportrait().getOriginalpic();
		holder.image.setTag(url);
		Log.e("ssssssssssssssssssssssssssssssssss", "position="+position+" "+holder.hashCode()+" image="+holder.image.hashCode()+" tag="+holder.image.getTag());
		if (!TextUtils.isEmpty(url)) {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.image, headParam.width, headParam.height);
		} else {
			holder.image.setBackgroundResource(R.drawable.default_small_pic);
		}

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = mInflater.inflate(R.layout.appointment_coach_list_item, null);
		return new ViewHolder(view);
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View itemView) {
			super(itemView);
			image = (ImageView) itemView.findViewById(R.id.appointment_car_headpic_item_im);
			name = (TextView) itemView.findViewById(R.id.appointment_car_coach_name_item_tv);
			shuttle = (TextView) itemView.findViewById(R.id.appointment_car_coach_item_shuttle);
			general = (TextView) itemView.findViewById(R.id.appointment_car_coach_item_general);
			school = (TextView) itemView.findViewById(R.id.appointment_car_school_item_tv);
			ck = (ImageView) itemView.findViewById(R.id.appointment_car_item_ck);
		}

		public ImageView image;
		public TextView name;
		public TextView shuttle;
		public TextView general;
		public TextView school;
		public ImageView ck;
	}

}
