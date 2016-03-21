package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.SchoolBusRouteNew;
import com.sft.vo.StationInfoVO;

/**
 * 班车 适配器 。内部 适配器
 * @author pengdonghua
 *
 */
public class SchoolBusAdapterInner2 extends BaseAdapter{
	
	List<StationInfoVO> list;
	
	Context context;

	public SchoolBusAdapterInner2(Context context,List<StationInfoVO> list){
		this.list = list;
		this.context = context;
	}
	
	public void setData(List<StationInfoVO> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context,R.layout.item_school_bus,
					null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_bus_title);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.item_bus_time);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvTitle.setText(list.get(position).stationname);
		holder.tvTime.setText(list.get(position).time);
		

		return convertView;
	}

	private class ViewHolder {
		public TextView tvTitle;
		public TextView tvTime;
		
	}

	
}
