package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.MyAppointmentVO;
/**
 * 预约列表
 * @author pengdonghua
 *
 */
public class MyAppointmentListAdapter extends BaseAdapter{

	private Context context;
	
	private List<MyAppointmentVO> list;
 
	
	public MyAppointmentListAdapter(Context context,List<MyAppointmentVO> mData){
		this.context = context;
		list = mData;
	}
	
	public void setData(List<MyAppointmentVO> mData){
		list = mData;
		notifyDataSetChanged();
	}
	
	public void changeState(int position, String state) {
		list.get(position).setReservationstate(state);
		notifyDataSetChanged();
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	public List<MyAppointmentVO> getData(){
		return list;
	}

	@Override
	public MyAppointmentVO getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,R.layout.mywallet_list_item, null);
			holder.date = (TextView) convertView
					.findViewById(R.id.mywallet_item_date_tv);
			holder.name = (TextView) convertView
					.findViewById(R.id.mywallet_item_name_tv);
			holder.income = (TextView) convertView
					.findViewById(R.id.mywallet_item_income_tv);
			holder.title = (TextView) convertView
					.findViewById(R.id.my_wallet_title_tv);
			holder.name.setVisibility(View.GONE);
			holder.income.setTextSize(18);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		holder.title.setText(mData.get(position).getType());
//		holder.date.setText(UTC2LOC.instance.getDate(mData.get(position)
//				.getCreatetime(), "yyyy/MM/dd"));
//		holder.income.setText(mData.get(position).getIncome());
		return convertView;
	}

	static class ViewHolder {
		public TextView date;
		public TextView income;
		public TextView name;
		public TextView title;
	}
	
	
}
