package com.sft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.vo.ExchangeGoodOrderVO;
import com.sft.vo.ExchangeOrderItemVO;
/**
 * 兑换商品订单
 * @author sun  2016-2-26 下午4:38:09
 *
 */
public class ExchangeGoodOrderAdapter extends BaseAdapter{

	private ExchangeGoodOrderVO bean;
	
	private Context c;
	
	public ExchangeGoodOrderAdapter(Context context,ExchangeGoodOrderVO bean){
		this.bean = bean;
		c = context;
	}
	
	public void setData(ExchangeGoodOrderVO b){
		this.bean = b;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return bean.ordrelist.size();
	}

	@Override
	public Object getItem(int position) {
		return bean.ordrelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(c,R.layout.item_order, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.item_order_img);
			holder.title = (TextView) convertView.findViewById(R.id.item_order_title);
			holder.left1 = (TextView) convertView.findViewById(R.id.item_order_left1);
			holder.left2 = (TextView) convertView.findViewById(R.id.item_order_left2);
			holder.right10 = (TextView) convertView.findViewById(R.id.item_order_right10);
			holder.right11 = (TextView) convertView.findViewById(R.id.item_order_right11);
			holder.right2 = (TextView) convertView.findViewById(R.id.item_order_right2);
			holder.btn2 = (Button) convertView.findViewById(R.id.item_order_button1);
			convertView.findViewById(R.id.item_order_button2).setVisibility(View.GONE);
			holder.right11.setText("实付金额:");
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ExchangeOrderItemVO b = bean.ordrelist.get(position);
		holder.title.setText(b.productname);
		holder.left1.setText("兑换金额:"+b.productprice+"YB");
		holder.left2.setText("下单时间:"+b.createtime);
		holder.right10.setText(b.productprice+"YB");
		if (b.orderstate.equals("1")) {
			holder.right2.setText("兑换成功");
		}else{
			holder.right2.setText("兑换失败");
		}
		return convertView;
	}

	private class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView left1;
		public TextView left2;
		public TextView right10;
		public TextView right11;
		public TextView right2;
		public Button btn2;
		
	}
	
	
	
}
