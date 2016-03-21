package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.util.LogUtil;
import com.sft.vo.SchoolBusRouteNew;
import com.sft.vo.StationInfoVO;

/**
 * 班车 适配器 。。新的
 * 
 * @author pengdonghua
 * 
 */
public class SchoolBusAdapter2 extends BaseAdapter {

	List<SchoolBusRouteNew> list;

	Context context;

	public SchoolBusAdapter2(Context context, List<SchoolBusRouteNew> list) {
		this.list = list;
		this.context = context;
	}

	public void setData(List<SchoolBusRouteNew> list) {
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
		final ViewHolder holder;
		// SchoolBusRouteNew SchoolVO = list.get(position);
		SchoolBusRouteNew bean = list.get(position);
		List<StationInfoVO> stations = bean.getStationinfo();
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.item_schoolbus_parent2, null);
			holder = new ViewHolder();
			
			holder.rlTop = (RelativeLayout) convertView
					.findViewById(R.id.item_bus_parent);
			
			holder.tvRootName = (TextView) convertView
					.findViewById(R.id.item_bus_parent_title);
			holder.tvRootArrow = (ImageView) convertView
					.findViewById(R.id.item_bus_parent_img);
			
			holder.tvTitleStart = (TextView) convertView
					.findViewById(R.id.item_bus_title_start);
			holder.tvTimeStart = (TextView) convertView
					.findViewById(R.id.item_bus_time_start);
			holder.tvTitleEnd = (TextView) convertView
					.findViewById(R.id.item_bus_title_end);
			holder.tvTimeEnd = (TextView) convertView
					.findViewById(R.id.item_bus_time_end);

			// holder.imgbgStart= (ImageView) convertView
			// .findViewById(R.id.item_bus)

			holder.lv = (ListView) convertView
					.findViewById(R.id.item_school_lv);
			holder.adapter = new SchoolBusAdapterInner2(context, stations);
			holder.lv.setAdapter(holder.adapter);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		LogUtil.print("rootName-->"+bean.getRoutename());
		holder.tvRootName.setText(bean.getRoutename());
		// 第一个
		if (stations.size() > 0) {
			holder.tvTimeStart.setText(stations.get(0).time);
			holder.tvTitleStart.setText(stations.get(0).stationname);
		}
		// 最后一个
		if (stations.size() > 1) {
			holder.tvTitleEnd
					.setText(stations.get(stations.size() - 1).stationname);
			holder.tvTimeEnd.setText(stations.get(0).time);
		}
		if (stations.size() > 2) {
			holder.adapter.setData(stations.subList(1, stations.size()-1));
			setListViewHeightBasedOnChildren(holder.lv);
		}
		
		holder.rlTop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(holder.lv.getVisibility() == View.VISIBLE){
					holder.lv.setVisibility(View.GONE);
					holder.tvRootArrow.setImageResource(R.drawable.icon_more_shang);
				}else{
					holder.lv.setVisibility(View.VISIBLE);
					holder.tvRootArrow.setImageResource(R.drawable.icon_more_down);
				}
			}
		});

		return convertView;
	}

	private class ViewHolder {
		public RelativeLayout rlTop;
		public TextView tvRootName;
		/**线路*/
		public ImageView tvRootArrow;
		
		
		
		public TextView tvTitleStart;
		public TextView tvTimeStart;
		public ImageView imgIconStart;
		public ImageView imgbgStart;

		public ImageView imgIconEnd;
		public ImageView imgbgEnd;

		public TextView tvTitleEnd;
		public TextView tvTimeEnd;
		public ListView lv;
		SchoolBusAdapterInner2 adapter;

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
