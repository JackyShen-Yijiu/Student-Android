package com.sft.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.vo.ExchangeGoodOrderVO;
import com.sft.vo.ExchangeOrderItemVO;

/**
 * 兑换商品订单
 * 
 * @author sun 2016-2-26 下午4:38:09
 * 
 */
public class ExchangeGoodOrderAdapter extends BaseAdapter {

	private ExchangeGoodOrderVO bean;
	private List<ExchangeOrderItemVO> list;
	private Activity c;

	public ExchangeGoodOrderAdapter(Activity context,
			List<ExchangeOrderItemVO> list) {
		this.list = list;
		c = context;
	}

	public void setData(ExchangeGoodOrderVO b) {
		this.bean = b;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// LinearLayout.LayoutParams headParams = null;
		if (convertView == null) {
			convertView = View.inflate(c, R.layout.item_order, null);
			holder = new ViewHolder();
			holder.img = (SelectableRoundedImageView) convertView
					.findViewById(R.id.item_order_img);
			holder.title = (TextView) convertView
					.findViewById(R.id.item_order_title);
			holder.left1 = (TextView) convertView
					.findViewById(R.id.item_order_left1);
			holder.left2 = (TextView) convertView
					.findViewById(R.id.item_order_left2);
			holder.right10 = (TextView) convertView
					.findViewById(R.id.item_order_right10);
			holder.right11 = (TextView) convertView
					.findViewById(R.id.item_order_right11);
			holder.right2 = (TextView) convertView
					.findViewById(R.id.item_order_right2);
			holder.btn2 = (TextView) convertView
					.findViewById(R.id.item_order_button1);
			holder.btn2.setText("再次兑换");
			// holder.btn2.setVisibility(View.GONE);
			convertView.findViewById(R.id.item_order_button2).setVisibility(
					View.GONE);
			holder.right11.setText("实付金额:");

			holder.img.setScaleType(ScaleType.CENTER_CROP);
			holder.img.setImageResource(R.drawable.default_small_pic);
			holder.img.setOval(true);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ExchangeOrderItemVO b = list.get(position);
		holder.title.setText(b.productname);
		holder.left1.setText("兑换金额:" + b.productprice + "YB");
		holder.left2.setText("下单时间:" + b.createtime);
		holder.right10.setText(b.productprice + "YB");
		if (b.orderstate.equals("1")) {
			holder.right2.setText("兑换成功");
		} else {
			holder.right2.setText("兑换失败");
		}

		if (TextUtils.isEmpty(b.productimg)) {
			holder.img.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) holder.img
					.getLayoutParams();
			BitmapManager.INSTANCE.loadBitmap2(b.productimg, holder.img,
					headParams.width, headParams.height);
		}
		holder.btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				c.setResult(9);
				c.finish();
			}
		});
		return convertView;
	}

	private class ViewHolder {
		public SelectableRoundedImageView img;
		public TextView title;
		public TextView left1;
		public TextView left2;
		public TextView right10;
		public TextView right11;
		public TextView right2;
		public TextView btn2;

	}

}
