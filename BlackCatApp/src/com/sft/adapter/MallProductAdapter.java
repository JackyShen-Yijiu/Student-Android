package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.sft.blackcatapp.R;
import com.sft.vo.ProductVO;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class MallProductAdapter extends BaseAdapter {

	private List<ProductVO> mData;
	private Context context;
	private int width;
	private int heigth;
	private String producttype;

	public MallProductAdapter(Context context, List<ProductVO> mData,
			int width, String producttype) {
		this.mData = mData;
		this.context = context;
		this.width = width;
		this.heigth = width / 2;
		this.producttype = producttype;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if ("0".equals(producttype)) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.mall_item, null);
				holder = new ViewHolder();
				holder.cuponLayout = (LinearLayout) convertView
						.findViewById(R.id.mall_item_cupon);
				holder.jifenLayout = (RelativeLayout) convertView
						.findViewById(R.id.mall_item_jifen);

				holder.image = (ImageView) convertView
						.findViewById(R.id.mall_item_img);
				holder.name = (TextView) convertView
						.findViewById(R.id.mall_item_name);
				holder.price = (TextView) convertView
						.findViewById(R.id.mall_item_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ProductVO productVO = mData.get(position);

			holder.jifenLayout.setVisibility(View.VISIBLE);
			holder.cuponLayout.setVisibility(View.GONE);
			BitmapManager.INSTANCE.loadBitmap2(productVO.getProductimg(),
					holder.image, width, heigth);
			holder.name.setText(productVO.getProductname());
			holder.price.setText(productVO.getProductprice() + "YB");

		} else {

			if (convertView == null) {
				// LinearLayout layout = new LinearLayout(context);
				// ImageView image = new ImageView(context);
				// LinearLayout.LayoutParams params = new LayoutParams(width,
				// heigth);
				// layout.addView(image, params);
				//
				// convertView = layout;
				convertView = View.inflate(context, R.layout.mall_item, null);
				holder = new ViewHolder();
				holder.cuponLayout = (LinearLayout) convertView
						.findViewById(R.id.mall_item_cupon);
				holder.jifenLayout = (RelativeLayout) convertView
						.findViewById(R.id.mall_item_jifen);
				holder.image = (ImageView) convertView
						.findViewById(R.id.mall_item_img);
				holder.cuponName = (TextView) convertView
						.findViewById(R.id.mall_item_cupon_name);
				holder.county = (TextView) convertView
						.findViewById(R.id.mall_item_cupon_county);
				holder.address = (TextView) convertView
						.findViewById(R.id.mall_item_address);
				holder.distinct = (TextView) convertView
						.findViewById(R.id.mall_item_distinct);
				holder.desc = (TextView) convertView
						.findViewById(R.id.mall_item_desc);
				holder.count = (TextView) convertView
						.findViewById(R.id.mall_item_count);
				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			ProductVO productVO = mData.get(position);

			holder.jifenLayout.setVisibility(View.GONE);
			holder.cuponLayout.setVisibility(View.VISIBLE);
			BitmapManager.INSTANCE.loadBitmap2(productVO.getProductimg(),
					holder.image, width, heigth);
			holder.cuponName.setText(productVO.getProductname());
			holder.county.setText(productVO.getCounty());
			holder.address.setText(productVO.getAddress());
			holder.distinct.setText(productVO.getDistinct());
			holder.desc.setText(productVO.getProductdesc());
			holder.count.setText(productVO.getCount());
		}

		return convertView;
	}

	private class ViewHolder {
		public ImageView image;

		public RelativeLayout jifenLayout;
		public TextView name;
		public TextView price;

		public LinearLayout cuponLayout;
		public TextView cuponName;
		public TextView county;
		public TextView address;
		public TextView distinct;
		public TextView desc;
		public TextView count;
	}
}
