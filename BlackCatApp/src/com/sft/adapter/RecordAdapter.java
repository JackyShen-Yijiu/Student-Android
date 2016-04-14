package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.qrcode.EncodingHandler;
import com.sft.util.LogUtil;
import com.sft.util.UTC2LOC;
import com.sft.vo.MyCuponVO;

public class RecordAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MyCuponVO> mData;
	private String producttype;
	private Context context;

	public RecordAdapter(Context context, List<MyCuponVO> mData,
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
			convertView = mInflater.inflate(R.layout.record_item, null);

			holder.code = (ImageView) convertView.findViewById(R.id.iv_code);
			holder.date = (TextView) convertView
					.findViewById(R.id.mywallet_item_date_tv);
			holder.detail = (TextView) convertView.findViewById(R.id.tv_detail);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String url = mData.get(position).getOrderscanaduiturl();
		if (TextUtils.isEmpty(url)) {
			holder.code.setImageResource(R.drawable.code_null);
		} else {
			createQr(url, holder.code);
		}

		holder.date.setText(UTC2LOC.instance.getDate(mData.get(position)
				.getUsetime(), "截止日期:" + "yyyy/MM/dd"));

		holder.detail.setText(mData.get(position).getProductid()
				.getProductname());
		return convertView;
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
		public TextView detail;
		public ImageView code;
	}

}
