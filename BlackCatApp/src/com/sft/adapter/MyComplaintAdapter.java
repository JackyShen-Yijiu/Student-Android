package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.util.UTC2LOC;
import com.sft.vo.ComplaintListVO;
/**
 * 我的投诉
 * @author sun  2016-3-1 下午9:57:48
 *
 */
public class MyComplaintAdapter extends BaseAdapter{

	private List<ComplaintListVO> list = new ArrayList<ComplaintListVO>();
	
	private Context context;
	
	public MyComplaintAdapter(Context context,List<ComplaintListVO> list){
		this.context = context;
		this.list = list;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,R.layout.item_my_complains, null);
			holder.date = (TextView) convertView
					.findViewById(R.id.item_my_complain_time);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_my_complain_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.item_my_complain_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText("投诉教练:"+list.get(position).coachid.getName());
		holder.content.setText(list.get(position).complaint.reason);
		
		holder.date.setText(""+UTC2LOC.instance.getDate(list.get(position).complaint.complainttime, "MM/dd HH:ss"));
		
//		holder.title.setText(mData.get(position).getType());
//		holder.date.setText(UTC2LOC.instance.getDate(mData.get(position)
//				.getCreatetime(), "yyyy/MM/dd"));
//		holder.income.setText(mData.get(position).getIncome());
		return convertView;
	}

	private class ViewHolder {
		public TextView date;
		public TextView name;
		public TextView content;
	}
}
