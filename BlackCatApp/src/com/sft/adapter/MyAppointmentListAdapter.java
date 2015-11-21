package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.blackcatapp.MyAppointmentActivity;
import com.sft.blackcatapp.R;
import com.sft.common.Config.AppointmentResult;
import com.sft.vo.MyAppointmentVO;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class MyAppointmentListAdapter extends
		RecyclerView.Adapter<MyAppointmentListAdapter.ViewHolder> {

	private Context context;
	private LayoutInflater mInflater;
	private List<MyAppointmentVO> mData;

	public MyAppointmentListAdapter(Context context, List<MyAppointmentVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
	}

	public void changeState(int position, String state) {
		mData.get(position).setReservationstate(state);
		notifyDataSetChanged();
	}

	public List<MyAppointmentVO> getData(){
		return mData;
	}
	
	public MyAppointmentVO getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View itemView) {
			super(itemView);
			headpic = (ImageView) itemView
					.findViewById(R.id.my_appointment_coach_headpic_im);
			name = (TextView) itemView
					.findViewById(R.id.my_appointment_item_name_tv);
			time = (TextView) itemView
					.findViewById(R.id.my_appointment_item_time_tv);
			status = (TextView) itemView
					.findViewById(R.id.my_appointment_item_status_tv);
			circle = (ImageView) itemView
					.findViewById(R.id.my_appointment_item_circle_im);
			line = (ImageView) itemView
					.findViewById(R.id.my_appointment_item_rightline_im);
			layout = (RelativeLayout) itemView
					.findViewById(R.id.my_appointment_item_layout);
			coachinfo = (TextView) itemView
					.findViewById(R.id.my_appointment_item_coachinfo_tv);
		}

		public ImageView headpic;
		public ImageView circle;
		public ImageView line;
		public RelativeLayout layout;
		public TextView name;
		public TextView time;
		public TextView status;
		public TextView coachinfo;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		holder.layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.sendBroadcast(new Intent(MyAppointmentActivity.class
						.getName()).putExtra("position", position).putExtra(
						"isJump", true));
			}
		});

		String state = mData.get(position).getReservationstate();

		if (state.equals(AppointmentResult.applyconfirm.getValue())) {
			// 已接受
			holder.circle.setBackgroundResource(R.drawable.finish_circle);
			holder.status.setText("已接受");
			holder.status.setTextColor(Color.parseColor("#26cf9a"));
			holder.line.setBackgroundColor(Color.parseColor("#caf7e9"));
		} else if (state.equals(AppointmentResult.unconfirmfinish.getValue())) {
			// 待确认学完
			holder.circle.setBackgroundResource(R.drawable.evaluate_circle);
			holder.status.setText("待确认学完");
			holder.status.setTextColor(Color.parseColor("#ff9333"));
			holder.line.setBackgroundColor(Color.parseColor("#ffe9bd"));
		} else if (state.equals(AppointmentResult.ucomments.getValue())) {
			// 待评价
			holder.circle.setBackgroundResource(R.drawable.evaluate_circle);
			holder.status.setText("待评价");
			holder.status.setTextColor(Color.parseColor("#ff9333"));
			holder.line.setBackgroundColor(Color.parseColor("#ffe9bd"));
		} else {
			holder.circle.setBackgroundResource(R.drawable.appointment_circle);
			if (state.equals(AppointmentResult.applying.getValue())) {
				// 待接受
				holder.status.setText("待接受");
				holder.status.setTextColor(Color.parseColor("#ff6633"));
				holder.line.setBackgroundColor(Color.parseColor("#ff6633"));
			} else if (state.equals(AppointmentResult.applycancel.getValue())) {
				// 已取消
				holder.status.setText("已取消");
				holder.status.setTextColor(Color.parseColor("#999999"));
				holder.line.setBackgroundColor(Color.parseColor("#999999"));
			} else if (state.equals(AppointmentResult.applyrefuse.getValue())) {
				// 教练取消
				holder.status.setText("教练取消");
				holder.status.setTextColor(Color.parseColor("#999999"));
				holder.line.setBackgroundColor(Color.parseColor("#999999"));
			} else if (state.equals(AppointmentResult.finish.getValue())) {
				// 完成的订单
				holder.status.setText("完成");
				holder.status.setTextColor(Color.parseColor("#999999"));
				holder.line.setBackgroundColor(Color.parseColor("#999999"));
			}
		}

		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) holder.headpic
				.getLayoutParams();

		String url = mData.get(position).getCoachid().getHeadportrait()
				.getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.headpic.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.headpic,
					headParams.width, headParams.height);
		}

		holder.name.setText(mData.get(position).getCourseprocessdesc());
		holder.time.setText(mData.get(position).getClassdatetimedesc());

		String coachName = mData.get(position).getCoachid().getName();
		String schoolName = mData.get(position).getCoachid()
				.getDriveschoolinfo().getName();
		String trainPlace = mData.get(position).getTrainfieldlinfo().getName();

		holder.coachinfo.setText(coachName + ", "
				+ (TextUtils.isEmpty(trainPlace) ? schoolName : trainPlace));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = mInflater.inflate(R.layout.my_appointment_list_item, null);
		return new ViewHolder(view);
	}
}
