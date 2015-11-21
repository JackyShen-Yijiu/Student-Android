package com.sft.adapter;

import java.util.List;

import com.sft.vo.ProductVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.sft.infinitescrollviewpager.BitmapManager;

@SuppressLint({ "InflateParams", "ResourceAsColor" })
public class MallProductAdapter extends BaseAdapter {

	private List<ProductVO> mData;
	private Context context;
	private int width;
	private int heigth;

	public MallProductAdapter(Context context, List<ProductVO> mData, int width) {
		this.mData = mData;
		this.context = context;
		this.width = width;
		this.heigth = width / 2;
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
			LinearLayout layout = new LinearLayout(context);
			ImageView image = new ImageView(context);
			LinearLayout.LayoutParams params = new LayoutParams(width, heigth);
			layout.addView(image, params);

			convertView = layout;
			holder = new ViewHolder();
			holder.image = image;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BitmapManager.INSTANCE.loadBitmap2(mData.get(position).getProductimg(), holder.image, width, heigth);

		return convertView;
	}

	private class ViewHolder {
		public ImageView image;
	}
}
