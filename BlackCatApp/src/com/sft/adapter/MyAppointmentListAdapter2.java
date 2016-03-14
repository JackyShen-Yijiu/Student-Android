package com.sft.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config.AppointmentResult;
import com.sft.qrcode.EncodingHandler;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.QRCodeCreateVO;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class MyAppointmentListAdapter2 extends BaseExpandableListAdapter   {

	private Activity context;
	private LayoutInflater mInflater;
	

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<MyAppointmentVO>> mData = new HashMap<Integer, List<MyAppointmentVO>>();


	private int pos = 0; // 0：显示今天的 预约订单 ， 1：显示除今天以外的订单 ， 2：显示已完成的订单
	private SimpleDateFormat format;
	
	private String[] title = {"今日预约","未来预约","已完成预约"};
	
	private BlackCatApplication app;

	public MyAppointmentListAdapter2(Activity context, Map<Integer, List<MyAppointmentVO>> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
		format = new SimpleDateFormat("yyyy-MM-dd");
	}

	public void changeState(int position, String state) {
//		mData.get(position).setReservationstate(state);
//		notifyDataSetChanged();
	}

	public Map<Integer, List<MyAppointmentVO>> getData() {
		return mData;
	}

//	@Override
//	public MyAppointmentVO getItem(int arg0) {
//		return mData.get(arg0);
//	}
//
//	@Override
//	public long getItemId(int arg0) {
//		return arg0;
//	}

	class ViewHolder {

		public SelectableRoundedImageView headpic;
		public RelativeLayout layout;
		public TextView name;
		public TextView time;
		public TextView status;
		public TextView schoolinfo;
		public TextView classInfo;
		public View line, splitLine;
		public TextView tvQiandao;
		public ImageView imgQiandao;
		
		
	}
	
	/**
	 * 每一项
	 * @param list
	 * @param position
	 * @return
	 */
	private View getItemView( List<MyAppointmentVO> list,int position){
//		final int index = position;
		ViewHolder holder = null;
//		if (convertView == null) {
			holder = new ViewHolder();
			View convertView = mInflater.inflate(R.layout.my_appointment_list_item,
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
			holder.tvQiandao = (TextView) convertView
					.findViewById(R.id.my_appointment_item_qiandao_tv);
			holder.imgQiandao = (ImageView) convertView
					.findViewById(R.id.my_appointment_item_qiandao_ic);
			
			
//			convertView.setTag(holder);
//		} else {
//
//			holder = (ViewHolder) convertView.getTag();
//		}
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
		final MyAppointmentVO item = list.get(position);
		String state = item.getReservationstate();

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

		String url = item.getCoachid().getHeadportrait()
				.getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.headpic.setImageResource(R.drawable.default_small_pic);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.headpic,
					headParams.width, headParams.height);
		}

		String coachName = item.getCoachid().getName();
		holder.name.setText(coachName);

		String schoolName = item.getCoachid()
				.getDriveschoolinfo().getName();
		String trainPlace = item.getTrainfieldlinfo()
				.getFieldname();

		holder.classInfo.setText(item.getCourseprocessdesc());
		holder.schoolinfo.setText(schoolName + trainPlace);

		// 分块显示

		String todayPosition = UTC2LOC.instance.getDate(item
				.getBegintime(), "yyyy-MM-dd");
		String nextPosition = null;
		if (position == list.size() - 1) {
			holder.line.setVisibility(View.VISIBLE);
			holder.splitLine.setVisibility(View.GONE);
			nextPosition = UTC2LOC.instance.getDate(item
					.getBegintime(), "yyyy-MM-dd");
		} else {
			nextPosition = UTC2LOC.instance.getDate(list.get(position + 1)
					.getBegintime(), "yyyy-MM-dd");
		}
		if (CommonUtil.compare_date(format.format(new Date()), todayPosition) == 0) {
			holder.time.setText("今天  "
					+ UTC2LOC.instance.getDate(item
							.getBegintime(), "HH:mm")
					+ "-"
					+ UTC2LOC.instance.getDate(
							item.getEndtime(), "HH:mm"));
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
			holder.time.setText(item.getClassdatetimedesc());
			if (CommonUtil
					.compare_date(format.format(new Date()), nextPosition) < 0) {
				holder.line.setVisibility(View.VISIBLE);
				holder.splitLine.setVisibility(View.GONE);
			} else {
				holder.line.setVisibility(View.GONE);
				holder.splitLine.setVisibility(View.VISIBLE);
			}

		} else {
			holder.time.setText(item.getClassdatetimedesc());
			holder.line.setVisibility(View.VISIBLE);
			holder.splitLine.setVisibility(View.GONE);
		}

		holder.line.setVisibility(View.VISIBLE);
		//签到
		holder.tvQiandao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showPop(item);
				
			}
		});
		holder.imgQiandao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showPop(item);
			}
		});

		return convertView;
	}

//	@Override
//	public int getCount() {
//		return mData.size();
//	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		final int index = position;
//		ViewHolder holder = null;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.my_appointment_list_item,
//					null);
//
//			holder.headpic = (SelectableRoundedImageView) convertView
//					.findViewById(R.id.my_appointment_coach_headpic_im);
//			holder.name = (TextView) convertView
//					.findViewById(R.id.my_appointment_item_name_tv);
//			holder.time = (TextView) convertView
//					.findViewById(R.id.my_appointment_item_time_tv);
//			holder.status = (TextView) convertView
//					.findViewById(R.id.my_appointment_item_status_tv);
//			holder.layout = (RelativeLayout) convertView
//					.findViewById(R.id.my_appointment_item_layout);
//			holder.schoolinfo = (TextView) convertView
//					.findViewById(R.id.my_appointment_item_schoolinfo_tv);
//			holder.classInfo = (TextView) convertView
//					.findViewById(R.id.my_appointment_item_class_tv);
//			holder.line = convertView
//					.findViewById(R.id.my_appointment_item_line_iv);
//			holder.splitLine = convertView
//					.findViewById(R.id.my_appointment_item_split_line_iv);
//			holder.tvQiandao = (TextView) convertView
//					.findViewById(R.id.my_appointment_item_qiandao_tv);
//			holder.imgQiandao = (ImageView) convertView
//					.findViewById(R.id.my_appointment_item_qiandao_ic);
//			
//			
//			convertView.setTag(holder);
//		} else {
//
//			holder = (ViewHolder) convertView.getTag();
//		}
//		holder.headpic.setScaleType(ScaleType.CENTER_CROP);
//		holder.headpic.setImageResource(R.drawable.default_small_pic);
//		holder.headpic.setOval(true);
//		
//		
//		// holder.layout.setOnClickListener(new OnClickListener() {
//		//
//		// @Override
//		// public void onClick(View v) {
//		// LogUtil.print("预约详情ooo");
//		// context.sendBroadcast(new Intent(MyAppointmentActivity.class
//		// .getName()).putExtra("position", index).putExtra(
//		// "isJump", true));
//		// }
//		// });
//
//		String state = mData.get(position).getReservationstate();
//
//		if (state.equals(AppointmentResult.applyconfirm.getValue())) {
//			// 已接受
//			holder.status.setText("已接受");
//		} else if (state.equals(AppointmentResult.unconfirmfinish.getValue())) {
//			// 待确认学完
//			holder.status.setText("待确认学完");
//		} else if (state.equals(AppointmentResult.ucomments.getValue())) {
//			// 待评价
//			holder.status.setText("待评价");
//		} else {
//			if (state.equals(AppointmentResult.applying.getValue())) {
//				// 待接受
//				holder.status.setText("请求中");
//			} else if (state.equals(AppointmentResult.applycancel.getValue())) {
//				// 已取消
//				holder.status.setText("已取消");
//			} else if (state.equals(AppointmentResult.applyrefuse.getValue())) {
//				// 教练取消
//				holder.status.setText("教练取消");
//			} else if (state.equals(AppointmentResult.finish.getValue())) {
//				// 完成的订单
//				holder.status.setText("已完成");
//			} else if (state.equals(AppointmentResult.systemcancel.getValue())) {
//				// 系统取消
//				holder.status.setText("已完成");
//			} else if (state.equals(AppointmentResult.signfinish.getValue())) {
//				// 已签到
//				holder.status.setText("已签到");
//			} else if (state.equals(AppointmentResult.missclass.getValue())) {
//				// 漏课
//				holder.status.setText("已漏课");
//			}
//		}
//
//		LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) holder.headpic
//				.getLayoutParams();
//
//		String url = mData.get(position).getCoachid().getHeadportrait()
//				.getOriginalpic();
//		if (TextUtils.isEmpty(url)) {
//			holder.headpic.setImageResource(R.drawable.default_small_pic);
//		} else {
//			BitmapManager.INSTANCE.loadBitmap2(url, holder.headpic,
//					headParams.width, headParams.height);
//		}
//
//		String coachName = mData.get(position).getCoachid().getName();
//		holder.name.setText(coachName);
//
//		String schoolName = mData.get(position).getCoachid()
//				.getDriveschoolinfo().getName();
//		String trainPlace = mData.get(position).getTrainfieldlinfo()
//				.getFieldname();
//
//		holder.classInfo.setText(mData.get(position).getCourseprocessdesc());
//		holder.schoolinfo.setText(schoolName + trainPlace);
//
//		// 分块显示
//
//		String todayPosition = UTC2LOC.instance.getDate(mData.get(position)
//				.getBegintime(), "yyyy-MM-dd");
//		String nextPosition = null;
//		if (position == mData.size() - 1) {
//			holder.line.setVisibility(View.VISIBLE);
//			holder.splitLine.setVisibility(View.GONE);
//			nextPosition = UTC2LOC.instance.getDate(mData.get(position)
//					.getBegintime(), "yyyy-MM-dd");
//		} else {
//			nextPosition = UTC2LOC.instance.getDate(mData.get(position + 1)
//					.getBegintime(), "yyyy-MM-dd");
//		}
//		if (CommonUtil.compare_date(format.format(new Date()), todayPosition) == 0) {
//			holder.time.setText("今天  "
//					+ UTC2LOC.instance.getDate(mData.get(position)
//							.getBegintime(), "HH:mm")
//					+ "-"
//					+ UTC2LOC.instance.getDate(
//							mData.get(position).getEndtime(), "HH:mm"));
//			if (CommonUtil.compare_date(nextPosition, todayPosition) == 0) {
//				// 今天
//				holder.line.setVisibility(View.VISIBLE);
//				holder.splitLine.setVisibility(View.GONE);
//
//			} else {
//				holder.line.setVisibility(View.GONE);
//				holder.splitLine.setVisibility(View.VISIBLE);
//			}
//
//		} else if (CommonUtil.compare_date(format.format(new Date()),
//				todayPosition) < 0) {
//			holder.time.setText(mData.get(position).getClassdatetimedesc());
//			if (CommonUtil
//					.compare_date(format.format(new Date()), nextPosition) < 0) {
//				holder.line.setVisibility(View.VISIBLE);
//				holder.splitLine.setVisibility(View.GONE);
//			} else {
//				holder.line.setVisibility(View.GONE);
//				holder.splitLine.setVisibility(View.VISIBLE);
//			}
//
//		} else {
//			holder.time.setText(mData.get(position).getClassdatetimedesc());
//			holder.line.setVisibility(View.VISIBLE);
//			holder.splitLine.setVisibility(View.GONE);
//		}
//
//		holder.line.setVisibility(View.VISIBLE);
//		//签到
//		holder.tvQiandao.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//				
//			}
//		});
//		holder.tvQiandao.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//
//		return convertView;
//	}
	
	private void showPop(MyAppointmentVO myAppointmentVO){
		final PopupWindow pop = new PopupWindow(context);
		pop.setHeight(LayoutParams.MATCH_PARENT);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		View view = View.inflate(context, R.layout.pop_qr, null);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pop.dismiss();
				
			}
		});
		ImageView img = (ImageView) view.findViewById(R.id.pop_qr_img);
		img.setBackgroundColor(Color.WHITE);
		
		pop.setFocusable(true);  
		   
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());  //comment by danielinbiti,如果添加了这行，那么标注1和标注2那两行不用加，加上这行的效果是点popupwindow的外边也能关闭  
		view.setFocusable(true);//comment by danielinbiti,设置view能够接听事件，标注1  
		view.setFocusableInTouchMode(true);
		
		pop.setContentView(view);
		
		pop.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 300, 400);
		createQr(myAppointmentVO,img);
	}
	
	private void createQr(MyAppointmentVO myAppointmentVO,ImageView img){
		QRCodeCreateVO codeCreateVO = new QRCodeCreateVO();
		if (myAppointmentVO != null) {
			if (app == null) {
				app = BlackCatApplication.getInstance();
			}
			if (app != null) {

				codeCreateVO.setStudentId(app.userVO.getUserid());
				codeCreateVO.setStudentName(app.userVO.getName());
				codeCreateVO.setReservationId(myAppointmentVO.get_id());
				codeCreateVO.setCoachName(app.userVO.getApplycoachinfo()
						.getName());
				codeCreateVO.setCourseProcessDesc(app.userVO.getSubject()
						.getName());
				codeCreateVO.setCreateTime((new Date().getTime() / 1000) + "");
				codeCreateVO.setLatitude(app.latitude);
				codeCreateVO.setLocationAddress(app.userVO.getAddress());
				codeCreateVO.setLongitude(app.longtitude);
			}
		}
		// 生成二维码
		try {
			String contentString = JSONUtil.toJsonString(codeCreateVO);
			LogUtil.print("contentString---" + contentString);
			if (contentString != null && contentString.trim().length() > 0) {
				// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
						contentString, 500);
				img.setImageBitmap(qrCodeBitmap);
			} else {
				Toast.makeText(context, "签到二维码生成失败", Toast.LENGTH_SHORT).show();
//				toast.setText("生成二维码失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
		return mData.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		LogUtil.print("childview--->"+arg0+"arg1-->>>"+arg1);
		return getItemView(mData.get(arg0),arg1);
	}

	@Override
	public int getChildrenCount(int arg0) {
		return mData.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return mData.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		
		View view = View.inflate(context, R.layout.item_appoint_group, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.item_appoint_group_title);
		tvTitle.setText(title[arg0]);
		tvTitle.setClickable(true);
		
		ImageView mgroupimage=(ImageView)view.findViewById(R.id.item_appoint_group_img);  
        if(arg0 == 2){
        	 mgroupimage.setImageResource(R.drawable.ic_arrow_right);  
        }else{
        	if(!arg1){  
                mgroupimage.setImageResource(R.drawable.ic_arrow_right);  
            }else{
           	 mgroupimage.setImageResource(R.drawable.ic_arrow_bottom);  
            }
        }
        
        
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
