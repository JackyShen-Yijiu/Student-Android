package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.SchoolBusRoute;
import com.sft.vo.SchoolBusRouteNew;
import com.sft.vo.SchoolBusRouteParentVO;
import com.sft.vo.StationInfoVO;

/**
 * 班车路线。。 可以展开
 * @author pengdonghua
 *
 */
public class SchoolBusAdapter extends BaseExpandableListAdapter{
	
//	private List<List<StationInfoVO>> data = new ArrayList<List<SchoolBusRouteNew>>();

	/**parent 实体类*/
	private List<SchoolBusRouteNew> list = new ArrayList<SchoolBusRouteNew>();
	
	private Context context;
	
	public SchoolBusAdapter(Context context){
		this.context = context;
	}
	
	public void setData(List<SchoolBusRouteNew> l){
		this.list = l;
		notifyDataSetChanged();
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		return list.get(arg0).getStationinfo().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		return getChildview(list.get(arg0).getStationinfo(), arg1);
	}
	
	private View getChildview(List<StationInfoVO> list,int po){
		StationInfoVO bean = list.get(po);
		
		View v = View.inflate(context, R.layout.item_school_bus, null);
		ImageView img = (ImageView) v.findViewById(R.id.item_bus_img);
		ImageView imgbg = (ImageView) v.findViewById(R.id.item_bus_img_bg);
		TextView tvTitle = (TextView) v.findViewById(R.id.item_bus_title);
		TextView tvTime = (TextView) v.findViewById(R.id.item_bus_time);
		tvTitle.setText(bean.stationname);
		tvTime.setText("到站时间:"+bean.time);
		if(po == 0){//第一个
			img.setImageResource(R.drawable.icon_bus);
			imgbg.setVisibility(View.VISIBLE);
		}else if(po == list.size() -1){//最后一个
			img.setImageResource(R.drawable.icon_bus);
			imgbg.setVisibility(View.INVISIBLE);
		}else{//中间数据
			img.setImageResource(R.drawable.icon_yuan);
			imgbg.setVisibility(View.VISIBLE);
		}
		
		
		return v;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return list.get(arg0).getStationinfo().size();
	}

	@Override
	public Object getGroup(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		
		View view = View.inflate(context, R.layout.item_bus_parent, null);
		TextView tv = (TextView) view.findViewById(R.id.item_bus_parent_title);
		ImageView imgRight = (ImageView) view.findViewById(R.id.item_bus_parent_img);
		tv.setText(list.get(arg0).getRoutename());
		if(arg1){
			imgRight.setImageResource(R.drawable.arrow_below);
		}else{
			imgRight.setImageResource(R.drawable.arrow_right);
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
