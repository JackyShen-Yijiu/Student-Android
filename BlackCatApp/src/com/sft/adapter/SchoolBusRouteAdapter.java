package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.SchoolBusRoute;

public class SchoolBusRouteAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<SchoolBusRoute> busRoutes;

	public SchoolBusRouteAdapter(Context context, List<SchoolBusRoute> busRoutes) {
		super();
		this.mContext = mContext;
		this.busRoutes = busRoutes;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return busRoutes.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_bus_route_item,
					null);
			holder = new ViewHolder();
			holder.busName = (TextView) convertView
					.findViewById(R.id.bus_route_item_name);
			holder.busContext = (TextView) convertView
					.findViewById(R.id.bus_route_item_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SchoolBusRoute busRoute = busRoutes.get(position);
		holder.busName.setText(busRoute.getRoutename());
		holder.busContext.setText(busRoute.getRoutecontent());
		return convertView;
	}

	private class ViewHolder {
		public TextView busName;
		public TextView busContext;
	}

}
