package com.sft.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.util.CommonUtil;
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
			holder.productPicIv = (ImageView) convertView
					.findViewById(R.id.product_order_pic);
			holder.productNameTv = (TextView) convertView
					.findViewById(R.id.product_order_name_tv);
			holder.productPriceTv = (TextView) convertView
					.findViewById(R.id.product_order_price_tv);
			holder.productTimeTv = (TextView) convertView
					.findViewById(R.id.product_order_time_tv);
			holder.productStatus = (TextView) convertView
					.findViewById(R.id.product_order_status_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ExchangeOrderItemVO b = list.get(position);
		if (TextUtils.isEmpty(b.productimg)) {
			holder.productPicIv
					.setBackgroundResource(R.drawable.default_small_pic);
		} else {
			LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) holder.productPicIv
					.getLayoutParams();
			BitmapManager.INSTANCE.loadBitmap2(b.productimg,
					holder.productPicIv, headParams.width, headParams.height);
		}
		holder.productNameTv.setText(b.productname);
		holder.productPriceTv.setText(b.productprice*b.count + "");
		holder.productTimeTv.setText(b.createtime);
		if ("5".equals(b.orderstate)) {
			holder.productStatus.setText("已领取");
			holder.productStatus.setTextColor(CommonUtil.getColor(c,
					R.color.new_button_color));
		} else {
			holder.productStatus.setText("未领取");
			holder.productStatus.setTextColor(CommonUtil.getColor(c,
					R.color.new_app_main_color));
		}
		return convertView;
	}

	private class ViewHolder {

		private ImageView productPicIv;
		private TextView productNameTv;
		private TextView productPriceTv;
		private TextView productTimeTv;
		private TextView productStatus;
	}

}
