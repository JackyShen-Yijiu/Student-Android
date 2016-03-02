package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.UTC2LOC;
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
			holder.productImage = (ImageView) convertView
					.findViewById(R.id.mall_item_product_img);
			holder.productName = (TextView) convertView
					.findViewById(R.id.mall_item_product_name);
			holder.couponNum = (TextView) convertView
					.findViewById(R.id.mall_item_coupon_num);
			holder.productAddress = (TextView) convertView
					.findViewById(R.id.mall_item_address);
			holder.couponUsed = (TextView) convertView
					.findViewById(R.id.mall_item_used_coupon_nums);
			holder.couponPurplus = (TextView) convertView
					.findViewById(R.id.mall_item_surplus_coupon_nums);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		ProductVO productVO = mData.get(position);

		if (TextUtils.isEmpty(productVO.getProductimg())) {
			holder.productImage.setBackgroundResource(R.drawable.defaultimage);
		} else {

			BitmapManager.INSTANCE.loadBitmap2(productVO.getProductimg(),
					holder.productImage, width, heigth);
		}
		holder.productName.setText("价值" + productVO.getProductprice() + "元"
				+ productVO.getProductname());

		holder.couponUsed.setText("已有" + productVO.getBuycount() + "人兑换");
		holder.couponPurplus
				.setText("剩余"
						+ (productVO.getProductcount() - productVO
								.getBuycount()) + "份");

		if (Config.MoneyType.INTEGRAL_RETURN.getValue().equals(producttype)) {

			holder.productAddress.setText("截止日期："
					+ UTC2LOC.instance.getDate(productVO.getEnddate(),
							"yyyy-MM-dd"));
			holder.couponNum.setText("兑换积分：" + productVO.getProductprice());
		} else if (Config.MoneyType.COIN_CERTIFICATE.getValue().equals(
				producttype)) {
			holder.productAddress.setText("地址：" + productVO.getAddress());
			holder.couponNum.setText("兑换券：1张");
		}
		return convertView;
	}

	private class ViewHolder {
		public ImageView productImage;

		public TextView productName;
		public TextView couponNum;

		public TextView productAddress;
		public TextView couponUsed;
		public TextView couponPurplus;
	}
}
