package com.sft.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.common.Config.AppointmentResult;
import com.sft.util.CommonUtil;
import com.sft.util.UTC2LOC;
import com.sft.vo.MyAppointmentVO;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class MyAppointmentListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<MyAppointmentVO> mData;

	private int pos = 0; // 0：显示今天的 预约订单 ， 1：显示除今天以外的订单 ， 2：显示已完成的订单
	private SimpleDateFormat format;

	public MyAppointmentListAdapter(Context context, List<MyAppointmentVO> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
		format = new SimpleDateFormat("yyyy-MM-dd");
	}

	public void changeState(int position, String state) {
		mData.get(position).setReservationstate(state);
		notifyDataSetChanged();
	}

	public List<MyAppointmentVO> getData() {
		return mData;
	}

	@Override
	public MyAppointmentVO getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class ViewHolder {

		public SelectableRoundedImageView headpic;
		public RelativeLayout layout;
		public TextView name;
		public TextView time;
		public TextView status;
		public TextView schoolinfo;
		public TextView classInfo;
		public View line, splitLine;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int index = position;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.my_appointment_list_item,
					null);

			holder.headpic = (SelectableRoundedImageView) convertView
					.findViewById(R.id.my_appointment_coach_headpic_im);
			holder.name = (TextView) convertView
					.findViewById(R.id.my_appointment_item_name_tv);
			holder.time = (TextView) convertView
					.findViewById(R.id.my_appointment_item_time_tv);
			holder.status = (TextView) convertView
					.findViewById(R.id.my_appointment_item_status_tv);
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.my_appointment_item_layout);
			holder.schoolinfo = (TextView) convertView
					.findViewById(R.id.my_appointment_item_schoolinfo_tv);
			holder.classInfo = (TextView) convertView
					.findViewById(R.id.my_appointment_item_class_tv);
			holder.line = convertView
					.findViewById(R.id.my_appointment_item_line_iv);
			holder.splitLine = convertView
					.findViewById(R.id.my_appointment_item_split_line_iv);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		holder.headpic.setScaleType(ScaleType.CENTER_CROP);
		holder.headpic.setImageResource(R.drawable.default_small_pic);
		holder.headpic.setOval(true);
		// holder.layout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// LogUtil.print("预约详情ooo");
		// context.sendBroadcast(new Intent(MyAppointmentActivity.class
		// .getName()).putExtra("position", index).putExtra(
		// "isJump", true));
		// }
		// });

		String state = mData.get(position).getReservationstate();

		if (state.equals(AppointmentResult.applyconfirm.getValue())) {
			// 已接受
			holder.status.setText("已接受");
		} else if (state.equals(AppointmentResult.unconfirmfinish.getValue())) {
			// 待确认学完
			holder.status.setText("待确认学完");
		} else if (state.equals(AppointmentResult.ucomments.getValue())) {
			// 待评价
			holder.status.setText("待评价");
		} else {
			if (state.equals(AppointmentResult.applying.getValue())) {
				// 待接受
				holder.status.setText("请求中");
			} else if (state.equals(AppointmentResult.applycancel.getValue())) {
				// 已取消
				holder.status.setText("已取消");
			} else if (state.equals(AppointmentResult.applyrefuse.getValue())) {
				// 教练取消
				holder.status.setText("教练取消");
			} else if (state.equals(AppointmentResult.finish.getValue())) {
				// 完成的订单
				holder.status.setText("已完成");
			} else if (state.equals(AppointmentResult.systemcancel.getValue())) {
				// 系统取消
				holder.status.setText("已完成");
			} else if (state.equals(AppointmentResult.signfinish.getValue())) {
				// 已签到
				holder.status.setText("已签到");
			} else if (state.equals(AppointmentResult.missclass.getValue())) {
				// 漏课
				holder.status.setText("已漏课");
			}
		}

		LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) holder.headpic
				.getLayoutParams();

		String url = mData.get(position).getCoachid().getHeadportrait()
				.getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.headpic.setImageResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.headpic,
					headParams.width, headParams.height);
		}

		String coachName = mData.get(position).getCoachid().getName();
		holder.name.setText(coachName);

		String schoolName = mData.get(position).getCoachid()
				.getDriveschoolinfo().getName();
		String trainPlace = mData.get(position).getTrainfieldlinfo()
				.getFieldname();

		holder.classInfo.setText(mData.get(position).getCourseprocessdesc());
		holder.schoolinfo.setText(schoolName + trainPlace);

		// 分块显示

		String todayPosition = UTC2LOC.instance.getDate(mData.get(position)
				.getBegintime(), "yyyy-MM-dd");
		String nextPosition = null;
		if (position == mData.size() - 1) {
			holder.line.setVisibility(View.VISIBLE);
			holder.splitLine.setVisibility(View.GONE);
			nextPosition = UTC2LOC.instance.getDate(mData.get(position)
					.getBegintime(), "yyyy-MM-dd");
		} else {
			nextPosition = UTC2LOC.instance.getDate(mData.get(position + 1)
					.getBegintime(), "yyyy-MM-dd");
		}
		if (CommonUtil.compare_date(format.format(new Date()), todayPosition) == 0) {
			holder.time.setText("今天  "
					+ UTC2LOC.instance.getDate(mData.get(position)
							.getBegintime(), "HH:mm")
					+ "-"
					+ UTC2LOC.instance.getDate(
							mData.get(position).getEndtime(), "HH:mm"));
			if (CommonUtil.compare_date(nextPosition, todayPosition) == 0) {
				// 今天
				holder.line.setVisibility(View.VISIBLE);
				holder.splitLine.setVisibility(View.GONE);

			} else {
				holder.line.setVisibility(View.GONE);
				holder.splitLine.setVisibility(View.VISIBLE);
			}

		} else if (CommonUtil.compare_date(format.format(new Date()),
				todayPosition) < 0) {
			holder.time.setText(mData.get(position).getClassdatetimedesc());
			if (CommonUtil
					.compare_date(format.format(new Date()), nextPosition) < 0) {
				holder.line.setVisibility(View.VISIBLE);
				holder.splitLine.setVisibility(View.GONE);
			} else {
				holder.line.setVisibility(View.GONE);
				holder.splitLine.setVisibility(View.VISIBLE);
			}

		} else {
			holder.time.setText(mData.get(position).getClassdatetimedesc());
			holder.line.setVisibility(View.VISIBLE);
			holder.splitLine.setVisibility(View.GONE);
		}

		holder.line.setVisibility(View.VISIBLE);

		return convertView;
	}
}
