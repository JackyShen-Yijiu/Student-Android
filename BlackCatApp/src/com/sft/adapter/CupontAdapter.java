package com.sft.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.qrcode.EncodingHandler;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.view.NoScrollListView;
import com.sft.vo.MyCuponVO;

public class CupontAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MyCuponVO> mData;
	private String producttype;
	private Activity context;

	public CupontAdapter(Activity context, List<MyCuponVO> mData,
			String producttype) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
		this.producttype = producttype;
	}

	public void setData(List<MyCuponVO> mData) {
		this.mData = mData;
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
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mywallet_item_list, null);

			holder.code = (ImageView) convertView.findViewById(R.id.iv_code);

			holder.date = (TextView) convertView
					.findViewById(R.id.mywallet_item_date_tv);

			holder.zhankai = (ImageView) convertView
					.findViewById(R.id.iv_zhankai);
			// 下面的活动详情
			holder.ll_detail = (LinearLayout) convertView
					.findViewById(R.id.ll_detail);
			holder.list_detail = (NoScrollListView) convertView
					.findViewById(R.id.list_detail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		bindViewHolder(holder, position);
		return convertView;
	}

	private void bindViewHolder(final ViewHolder holder, final int position) {
		final String url = mData.get(position).getOrderscanaduiturl();
		DuihuanjuanAdapter duihuanjuanAdapter;
		duihuanjuanAdapter = new DuihuanjuanAdapter(context, mData
				.get(position).getUseproductidlist());
		holder.list_detail.setAdapter(duihuanjuanAdapter);

		holder.code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				final PopupWindow pop = new PopupWindow(context);
				// pop.setHeight(1180);
				pop.setHeight(LayoutParams.MATCH_PARENT);
				pop.setWidth(LayoutParams.MATCH_PARENT);
				// pop.
				View view = View.inflate(context, R.layout.pop_qr, null);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						pop.dismiss();
					}
				});
				ImageView img = (ImageView) view.findViewById(R.id.pop_qr_img);
				img.setBackgroundColor(Color.WHITE);

				// pop.setFocusable(true);

				// popupWindow.setBackgroundDrawable(new BitmapDrawable());
				// //comment by
				// danielinbiti,如果添加了这行，那么标注1和标注2那两行不用加，加上这行的效果是点popupwindow的外边也能关闭
				view.setFocusable(true);// comment by
										// danielinbiti,设置view能够接听事件，标注1
				view.setFocusableInTouchMode(true);

				pop.setContentView(view);

				pop.showAtLocation(context.getWindow().getDecorView(),
						Gravity.TOP, 0, 0);
				createQr(url, img);

			}
		});
		holder.zhankai.setOnClickListener(new OnClickListener() {
			boolean isExtend = false;// 是否展开

			@Override
			public void onClick(View paramView) {
				if (!isExtend) {
					holder.ll_detail.setVisibility(View.VISIBLE);
				} else {
					holder.ll_detail.setVisibility(View.GONE);
				}
				LogUtil.print("ssssssssss" + position);
				// notifyDataSetChanged();
				isExtend = !isExtend;
				holder.zhankai
						.setBackgroundResource(R.drawable.selector_zhankai);
			}
		});

		if (TextUtils.isEmpty(url)) {
			holder.code.setImageResource(R.drawable.code_null);
		} else {
			createQr(url, holder.code);
		}

		holder.date.setText(UTC2LOC.instance.getDate(mData.get(position)
				.getCreatetime(), "有效期至:" + "yyyy/MM/dd"));
		// duihuanjuanAdapter.setData(mData.get(position).getUseproductidlist());
	}

	private void createQr(String url, ImageView code) {
		// 生成二维码
		try {
			LogUtil.print("contentString---" + url);
			if (url != null && url.trim().length() > 0) {
				// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(url, 500);
				code.setImageBitmap(qrCodeBitmap);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class ViewHolder {
		public TextView date;
		public ImageView code;
		public LinearLayout ll_detail;
		public NoScrollListView list_detail;
		public ImageView zhankai;
	}

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.iv_zhankai:
	// if (isExtend) {
	// holder.ll_detail.setVisibility(View.VISIBLE);
	// } else {
	// holder.ll_detail.setVisibility(View.GONE);
	// }
	// LogUtil.print("ssssssssss");
	// notifyDataSetChanged();
	// isExtend = !isExtend;
	// holder.zhankai.setBackgroundResource(R.drawable.selector_zhankai);
	// break;
	// }
	// }
}
